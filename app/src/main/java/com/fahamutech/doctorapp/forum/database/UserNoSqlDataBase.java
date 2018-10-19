package com.fahamutech.doctorapp.forum.database;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;

import com.fahamutech.doctorapp.forum.model.Patient;
import com.fahamutech.doctorapp.forum.model.Receipt;
import com.fahamutech.doctorapp.forum.model.UserSubscription;
import com.fahamutech.doctorapp.session.Session;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;

import static android.media.CamcorderProfile.get;

public class UserNoSqlDataBase extends NoSqlDatabase implements UserDataSource {

    public UserNoSqlDataBase(Context context) {
        super(context);
    }

    /**
     * create the patient to the database
     *
     * @param patient the patient object
     */
    @Override
    public void createUser(Patient patient) {
        Log.e(TAG, "start create patient");
        DocumentReference document = firestore.collection(ForumC.FORUM_USER.name())
                .document(patient.getEmail());
        document.set(patient, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.e(TAG, "successful patient added"))
                .addOnFailureListener(e -> Log.e(TAG, "Failed to add patient : " + e.getMessage()));
    }

    @Override
    public void getUser(String userId, SwipeRefreshLayout swipeRefreshLayout,
                        DataBaseCallback... dataBaseCallbacks) {
        swipeRefreshLayout.setRefreshing(true);

        firestore.collection(ForumC.FORUM_USER.name())
                .whereEqualTo("id", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null) {
                        List<Patient> patients = queryDocumentSnapshots.toObjects(Patient.class);
                        if (patients.size() > 0) {
                            Patient patient = patients.get(0);
                            if (dataBaseCallbacks != null) {
                                dataBaseCallbacks[0].then(patient);
                            }
                        }
                    }
                    swipeRefreshLayout.setRefreshing(false);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "failed to get user details, reason : " + e);
                    swipeRefreshLayout.setRefreshing(false);
                });
    }

    @Override
    public void getUserSubscription(String docId, SwipeRefreshLayout swipeRefreshLayout,
                                    DataBaseCallback... dataBaseCallbacks) {
        swipeRefreshLayout.setRefreshing(true);
        firestore.collection(ForumC.FORUM_USER.name())
                .document(docId)
                .collection(ForumC.SUBSCRIPTION.name())
                .orderBy("end")
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<UserSubscription> userSubscriptions
                            = queryDocumentSnapshots.toObjects(UserSubscription.class);
                    if (userSubscriptions.size() > 0) {
                        dataBaseCallbacks[0].then(userSubscriptions.get(0));
                    } else {
                        dataBaseCallbacks[0].then(null);
                        Log.e(TAG, "Patient subscription is empty");
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    swipeRefreshLayout.setRefreshing(false);
                })
                .addOnFailureListener(e -> {
                    dataBaseCallbacks[0].then(null);
                    Log.e(TAG, "Failed to get user subscription, reason : " + e);
                    swipeRefreshLayout.setRefreshing(false);
                });
    }

    @Override
    public void updateUser(String userId, SwipeRefreshLayout swipeRefreshLayout, View... view) {
        swipeRefreshLayout.setRefreshing(true);
        TextInputEditText name = (TextInputEditText) view[0];
        TextInputEditText email = (TextInputEditText) view[1];
        TextInputEditText phone = (TextInputEditText) view[2];
        TextInputEditText address = (TextInputEditText) view[3];

        Session session = new Session(context);
        Patient savedUser = session.getSavedUser();
        savedUser.setPhoneNumber(phone.getText().toString());
        savedUser.setAddress(address.getText().toString());
        session.saveUser(savedUser);

        HashMap<String, Object> data = new HashMap<>();
        data.put("phoneNumber", phone.getText().toString());
        data.put("address", address.getText().toString());
        firestore.collection(ForumC.FORUM_USER.name())
                .document(email.getText().toString())
                .update(data)
                .addOnSuccessListener(aVoid -> {
                    Snackbar.make(email, "Profile updated", Snackbar.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to update profile, reason : " + e);
                    swipeRefreshLayout.setRefreshing(false);
                });
    }

    @Override
    public void getReceipts(String docId, SwipeRefreshLayout swipeRefreshLayout,
                            DataBaseCallback... dataBaseCallbacks) {
        swipeRefreshLayout.setRefreshing(true);
        firestore.collection(ForumC.FORUM_USER.name())
                .document(docId)
                .collection(ForumC.SUBSCRIPTION.name())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    dataBaseCallbacks[0].then(queryDocumentSnapshots.toObjects(Receipt.class));
                    swipeRefreshLayout.setRefreshing(false);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to get user receipts");
                    swipeRefreshLayout.setRefreshing(false);
                });
    }

}
