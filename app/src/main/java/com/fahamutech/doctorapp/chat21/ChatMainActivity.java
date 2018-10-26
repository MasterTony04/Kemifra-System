package com.fahamutech.doctorapp.chat21;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.fahamutech.doctorapp.R;
import com.fahamutech.doctorapp.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.chat21.android.core.ChatManager;
import org.chat21.android.core.authentication.ChatAuthentication;
import org.chat21.android.core.users.models.ChatUser;
import org.chat21.android.core.users.models.IChatUser;
import org.chat21.android.ui.ChatUI;
import org.chat21.android.ui.users.activities.PublicProfileActivity;

public class ChatMainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);
        bindView();
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle("Conservations");
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        checkUserLogin();
    }

    private void bindView() {
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    protected void onResume() {
        checkUserLogin();
        super.onResume();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            Log.e("TAG **USER", "user done login");
//            checkUserLogin();
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            Toast.makeText(this, "Log out, please wait...", Toast.LENGTH_SHORT).show();
            ChatAuthentication.getInstance().signOut("doctorChat",
                    new ChatAuthentication.OnChatLogoutCallback() {
                        @Override
                        public void onChatLogoutSuccess() {
                            FirebaseAuth.getInstance().signOut();
                            Toast.makeText(ChatMainActivity.this,
                                    "Successful logout", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ChatMainActivity.this, MainActivity.class));
                            finish();
                        }

                        @Override
                        public void onChatLogoutError(Exception e) {
                            Toast.makeText(ChatMainActivity.this,
                                    "Fail to logout, try again", Toast.LENGTH_SHORT).show();
                        }
                    });

        } else if (item.getItemId() == R.id.profile) {
            showProfile();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showProfile() {

        if (ChatAuthentication.getInstance().getFirebaseAuth().getCurrentUser() != null) {
            IChatUser loggedUser = ChatManager.getInstance().getLoggedUser();
            loggedUser.setEmail(ChatAuthentication.getInstance().getEmail());
            loggedUser.setFullName(ChatAuthentication.getInstance().getFullName());
            loggedUser.setId(ChatAuthentication.getInstance().getFirebaseAuth().getUid());

            Intent intent = new Intent(this, PublicProfileActivity.class);
            intent.putExtra(ChatUI.BUNDLE_RECIPIENT, loggedUser);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initChat(IChatUser loggedUser) {
        //chat
        // optional

//        // mandatory
//        // it creates the chat configurations
//        ChatManager.Configuration mChatConfiguration =
//                new ChatManager.Configuration.Builder("doctorChat")
////                        .firebaseUrl("https://doctor-fahamutech.firebaseio.com")
////                        .storageBucket("doctor-fahamutech.appspot.com")
//                        .build();

        loggedUser.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        ///FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        ChatManager.start(this, "doctorChat", loggedUser);
//        ChatAuthentication.getInstance().createAuthListener(user -> {
//            if (user != null) {
//                Log.e("TAG**USER", "user is not logout");
//            } else Log.e("TAG**USER", "user is logout");
//        });
        ChatUI instance = ChatUI.getInstance();
        instance.setContext(this);
        instance.enableGroups(true);
//        instance.setOnAttachClickListener((OnAttachClickListener) object -> {
//            Log.e("Document attached", "TAGGG*****");
//        });
        instance.openConversationsListFragment(getSupportFragmentManager(), R.id.frame);
    }

    private void checkUserLogin() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            initChat(new ChatUser(currentUser.getUid(), currentUser.getDisplayName()));
        } else {
            startActivity(new Intent(this, MySplashActivity.class));
            finish();
        }
    }
}
