package org.chat21.android.ui.login.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.chat21.android.R;
import org.chat21.android.core.ChatManager;
import org.chat21.android.core.authentication.task.RefreshFirebaseInstanceIdTask;
import org.chat21.android.core.exception.ChatFieldNotFoundException;
import org.chat21.android.core.users.models.ChatUser;
import org.chat21.android.core.users.models.IChatUser;
import org.chat21.android.ui.ChatUI;
import org.chat21.android.ui.contacts.activites.ContactListActivity;
import org.chat21.android.ui.conversations.listeners.OnNewConversationClickListener;
import org.chat21.android.utils.StringUtils;

import java.util.Map;

import static org.chat21.android.ui.ChatUI.BUNDLE_SIGNED_UP_USER_EMAIL;
import static org.chat21.android.ui.ChatUI.BUNDLE_SIGNED_UP_USER_PASSWORD;
import static org.chat21.android.ui.ChatUI.REQUEST_CODE_SIGNUP_ACTIVITY;
import static org.chat21.android.utils.DebugConstants.DEBUG_LOGIN;

/**
 * Created by stefanodp91 on 21/12/17.
 */

public class ChatLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ChatLoginActivity";

    private Toolbar toolbar;
    private EditText vEmail;
    private EditText vPassword;
    private Button vLogin;
    private Button vSignUp;
    private Button vReset;
    private FirebaseAuth mAuth;

//    private String email, username, password;

    private interface OnUserLookUpComplete {
        void onUserRetrievedSuccess(IChatUser loggedUser);

        void onUserRetrievedError(Exception e);
    }

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_login);
        binView();

        mAuth = FirebaseAuth.getInstance();

        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle("Login");
            supportActionBar.setDisplayHomeAsUpEnabled(false);
        }
        initPasswordIMEAction();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
//        ChatAuthentication.getInstance().getFirebaseAuth()
//                .addAuthStateListener(ChatAuthentication.getInstance().getAuthListener());
//
//        Log.d(DEBUG_LOGIN, "ChatLoginActivity.onStart: auth state listener attached ");
    }

    private void binView() {
        toolbar = findViewById(R.id.toolbar);
        vLogin = findViewById(R.id.login);
        vLogin.setOnClickListener(this);

        vSignUp = findViewById(R.id.signup);
        vSignUp.setOnClickListener(this);

        vEmail = findViewById(R.id.email);
        vPassword = findViewById(R.id.password);

        vReset = findViewById(R.id.reset);
        vReset.setOnClickListener(this);
    }

    @Override
    public void onStop() {
//        ChatAuthentication.getInstance().removeAuthStateListener();
//        Log.d(DEBUG_LOGIN, "ChatLoginActivity.onStart: auth state listener detached ");
        super.onStop();
        hideProgressDialog();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.login) {
            signIn(vEmail.getText().toString(), vPassword.getText().toString());
//            performLogin();
        } else if (viewId == R.id.signup) {
            startSignUpActivity();
        } else if (viewId == R.id.reset) {
            String email = vEmail.getText().toString();
            if (!StringUtils.isValid(email) || !email.contains("@")) {
                vEmail.setError("Required.");
                vEmail.requestFocus();
            } else {
                resetPassword(email, view);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SIGNUP_ACTIVITY) {
            if (resultCode == RESULT_OK) {

                // set username
                String email = data.getStringExtra(BUNDLE_SIGNED_UP_USER_EMAIL);
//                vEmail.setText(email);

                // set password
                String password = data.getStringExtra(BUNDLE_SIGNED_UP_USER_PASSWORD);
//                vPassword.setText(password);

                signIn(email, password);
            }
        } else super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //startActivity(new Intent(this, YourActivity.class));
                Log.e("TAG**LOGIN", "login clicked");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void resetPassword(String email, View view) {
        Snackbar snak = Snackbar.make(view, "Sending reset email...", Snackbar.LENGTH_LONG);
        snak.show();
        mAuth.sendPasswordResetEmail(vEmail.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this,
                                "Reset instruction sent to : " + email, Toast.LENGTH_LONG).show();
                        snak.dismiss();
                    } else {
                        Toast.makeText(this,
                                "Reset password fails try again", Toast.LENGTH_LONG).show();
                        snak.dismiss();
                    }
                });
    }

    private void initPasswordIMEAction() {
        Log.d(DEBUG_LOGIN, "initPasswordIMEAction");

        /**
         * on ime click
         * source:
         * http://developer.android.com/training/keyboard-input/style.html#Action
         */
        vPassword.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Log.d(DEBUG_LOGIN, "ChatLoginActivity.initPasswordIMEAction");
                signIn(vEmail.getText().toString(), vPassword.getText().toString());
                handled = true;
            }
            return handled;
        });
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);

        vEmail.setText(email);
        vPassword.setText(password);

        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.e(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();

                        if (user != null) {
                            //send verification email
                            //if (user.isEmailVerified()) user.sendEmailVerification();

                            lookUpContactById(user.getUid(), new OnUserLookUpComplete() {
                                @Override
                                public void onUserRetrievedSuccess(IChatUser loggedUser) {
                                    Log.e(TAG, "ChatLoginActivity.signInWithEmail." +
                                            "onUserRetrievedSuccess: loggedUser == " +
                                            loggedUser.toString());
                                    ChatManager.Configuration mChatConfiguration = new ChatManager
                                            .Configuration
                                            .Builder(ChatManager.Configuration.appId)
                                            .build();

                                    //start a service
                                    ChatManager.start(ChatLoginActivity.this, mChatConfiguration, loggedUser);
                                    Log.i(TAG, "chat has been initialized with success");

                                    // get device token
                                    new RefreshFirebaseInstanceIdTask().execute();
                                    ChatUI.getInstance().setContext(ChatLoginActivity.this);

                                    Log.i(TAG, "ChatUI has been initialized with success");

                                    ChatUI.getInstance().enableGroups(true);

                                    // set on new conversation click listener
                                    // final IChatUser support = new ChatUser("support", "Chat21 Support");
                                    final IChatUser support = null;
                                    ChatUI.getInstance().setOnNewConversationClickListener(
                                            (OnNewConversationClickListener) () -> {
                                                if (support != null) {
                                                    ChatUI.getInstance()
                                                            .openConversationMessagesActivity(support);
                                                } else {
                                                    Intent intent = new Intent(getApplicationContext(),
                                                            ContactListActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    // start activity from context
                                                    startActivity(intent);
                                                }
                                            }
                                    );
                                    Log.e(TAG, "ChatUI has been initialized with success");
//                                    //setResult(Activity.RESULT_OK);
                                    hideProgressDialog();
                                }

                                @Override
                                public void onUserRetrievedError(Exception e) {
                                    Log.e(TAG, "ChatLoginActivity.signInWithEmail" +
                                            ".onUserRetrievedError: " + e.toString());
                                    hideProgressDialog();
                                }
                            });
                            // enable persistence must be made before any other usage of FirebaseDatabase instance.
                            try {
                                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                            } catch (Exception e) {
                                Log.w(TAG, e.toString());
                            }
                        }
                        hideProgressDialog();
                        //setResult(Activity.RESULT_OK);
                        finish();

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());

                        Toast.makeText(ChatLoginActivity.this, "Authentication failed. Try again",
                                Toast.LENGTH_SHORT).show();
                        hideProgressDialog();
//                            setResult(Activity.RESULT_CANCELED);
//                            finish();
//                            updateUI(null);
                    }

                });
        // [END sign_in_with_email]
    }

    private void startSignUpActivity() {
        Intent intent = new Intent(this, ChatSignUpActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SIGNUP_ACTIVITY);
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = vEmail.getText().toString();
        if (!StringUtils.isValid(email)) {
            vEmail.setError("Required.");
            valid = false;
        } else if (StringUtils.isValid(email) && !StringUtils.validateEmail(email)) {
            vEmail.setError("Not valid email.");
            valid = false;
        } else {
            vEmail.setError(null);
        }
        String password = vPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            vPassword.setError("Required.");
            valid = false;
        } else {
            vPassword.setError(null);
        }

        return valid;
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
//            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void lookUpContactById(String userId, final OnUserLookUpComplete onUserLookUpComplete) {

        DatabaseReference contactsNode;
        if (StringUtils.isValid(ChatManager.Configuration.firebaseUrl)) {
            contactsNode = FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(ChatManager.Configuration.firebaseUrl)
                    .child("/apps/" + ChatManager.Configuration.appId + "/contacts/" + userId);
        } else {
            contactsNode = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("/apps/" + ChatManager.Configuration.appId + "/contacts/" + userId);
        }

        contactsNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(DEBUG_LOGIN, "ChatLoginActivity.lookUpContactById: dataSnapshot == " + dataSnapshot.toString());

                if (dataSnapshot.getValue() != null) {
                    try {
                        IChatUser loggedUser = decodeContactSnapShop(dataSnapshot);
                        Log.d(DEBUG_LOGIN, "ChatLoginActivity.lookUpContactById.onDataChange: loggedUser == " + loggedUser.toString());
                        onUserLookUpComplete.onUserRetrievedSuccess(loggedUser);
                    } catch (ChatFieldNotFoundException e) {
                        Log.e(DEBUG_LOGIN, "ChatLoginActivity.lookUpContactById.onDataChange: " + e.toString());
                        onUserLookUpComplete.onUserRetrievedError(e);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(DEBUG_LOGIN, "ChatLoginActivity.lookUpContactById: " + databaseError.toString());
                onUserLookUpComplete.onUserRetrievedError(databaseError.toException());
            }
        });
    }

    private static IChatUser decodeContactSnapShop(DataSnapshot dataSnapshot) throws ChatFieldNotFoundException {
        Log.v(TAG, "decodeContactSnapShop called");

        Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

//        String contactId = dataSnapshot.getKey();

        String uid = (String) map.get("uid");
        if (uid == null) {
            throw new ChatFieldNotFoundException("Required uid field is null for contact id : " + uid);
        }

//        String timestamp = (String) map.get("timestamp");

        String lastname = (String) map.get("lastname");
        String firstname = (String) map.get("firstname");
        String imageurl = (String) map.get("imageurl");
        String email = (String) map.get("email");


        IChatUser contact = new ChatUser();
        contact.setId(uid);
        contact.setEmail(email);
        contact.setFullName(firstname + " " + lastname);
        contact.setProfilePictureUrl(imageurl);

        Log.v(TAG, "decodeContactSnapShop.contact : " + contact);

        return contact;
    }
}