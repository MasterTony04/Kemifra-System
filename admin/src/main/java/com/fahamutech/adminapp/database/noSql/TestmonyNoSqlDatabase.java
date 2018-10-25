package com.fahamutech.adminapp.database.noSql;

import android.content.Context;
import android.util.Log;

import com.fahamutech.adminapp.database.DataBaseCallback;
import com.fahamutech.adminapp.database.connector.TestimonyDataSource;
import com.fahamutech.adminapp.model.ITestimony;
import com.fahamutech.adminapp.model.Testimony;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.SetOptions;

public class TestmonyNoSqlDatabase extends NoSqlDatabase implements TestimonyDataSource {

    public TestmonyNoSqlDatabase(Context context) {
        super(context);
    }


    @Override
    public void createTestimony(Testimony testimony, DataBaseCallback... callbacks) {
        DocumentReference document = firestore.collection("TESTIMONY").document();
        testimony.setId(document.getId());
        document.set(testimony, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    if (callbacks != null) callbacks[0].then("data");
                })
                .addOnFailureListener(e -> {
                    if (callbacks != null) callbacks[1].then("fail " + e.getLocalizedMessage());
                });
    }

    @Override
    public void deleteTestimony(String docId, DataBaseCallback... callbacks) {
        Log.e("TAG*****", docId);
        firestore.collection("TESTIMONY").document(docId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    if (callbacks != null) callbacks[0].then("done");
                })
                .addOnFailureListener(e -> {
                    if (callbacks != null) callbacks[1].then("fail " + e.getLocalizedMessage());
                });
    }
}
