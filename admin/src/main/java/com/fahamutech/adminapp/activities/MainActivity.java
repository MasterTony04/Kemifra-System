package com.fahamutech.adminapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.fahamutech.adminapp.R;
import com.fahamutech.adminapp.adapter.HomePageFragmentAdapter;
import com.fahamutech.adminapp.chat21.MySplashActivity;
import com.fahamutech.adminapp.database.noSql.HomeNoSqlDatabase;
import com.fahamutech.adminapp.model.Testimony;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.chat21.android.core.authentication.ChatAuthentication;
import org.chat21.android.ui.login.activities.ChatLoginActivity;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private HomeNoSqlDatabase homeNoSqlDatabase;

    @Override
    protected void onStart() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, ChatLoginActivity.class));
        }
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        bindView();
        setSupportActionBar(toolbar);

        //init database
        homeNoSqlDatabase = new HomeNoSqlDatabase(this);

        //tab layout
        initViewPager();

        //for testing
        fab.setOnClickListener(view -> {
            Snackbar.make(view, "Chat is opening", Snackbar.LENGTH_SHORT).show();
            startActivity(new Intent(this, MySplashActivity.class));
        });
    }

    @Override
    protected void onResume() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, ChatLoginActivity.class));
        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new_category) {
            startActivity(new Intent(this, CategoryActivity.class));
            return true;
        } else if (id == R.id.action_new_article) {
            startActivity(new Intent(this, NewArticleActivity.class));
            return true;
        } else if (id == R.id.action_delete_article) {
            startActivity(new Intent(this, DeleteArticleActivity.class));
            return true;
        } else if (id == R.id.action_new_testimony) {
            ImagePicker.create(this)
                    .single()
                    .showCamera(true)
                    .theme(R.style.AppTheme)
                    .limit(1)
                    .returnMode(ReturnMode.ALL)
                    .start();
            return true;
        } else if (id == R.id.action_doctor_logout) {
            Toast.makeText(this, "Logout, pleas wait...", Toast.LENGTH_SHORT).show();
            ChatAuthentication.getInstance().signOut("doctorApp", new ChatAuthentication.OnChatLogoutCallback() {
                @Override
                public void onChatLogoutSuccess() {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        FirebaseAuth.getInstance().signOut();
                    }
                    Toast.makeText(MainActivity.this, "Successful logout", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, ChatLoginActivity.class));
                    finish();
                }

                @Override
                public void onChatLogoutError(Exception e) {
                    Toast.makeText(MainActivity.this, "Logout failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image img = ImagePicker.getFirstImageOrNull(data);
            uploadTestimony(img.getPath(), img.getName());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void bindView() {
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.home_chat_fab);
        viewPager = findViewById(R.id.home_viewpager);
        tabLayout = findViewById(R.id.home_tab_layout);
    }

    private void initViewPager() {
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.setAdapter(new HomePageFragmentAdapter(getSupportFragmentManager()));
    }

    private void uploadTestimony(String fileLocation, String fileName) {

        //progress dialog
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Upload Testimony")
                .content("Please wait...")
                .canceledOnTouchOutside(false)
                .progress(true, 1)
                .positiveText("Close")
                .onPositive((dialog1, which) -> {
                    dialog1.dismiss();
                })
                .build();
        dialog.show();

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        //save to firebase
        FirebaseStorage storage = FirebaseStorage.getInstance();
        //StorageReference reference = storage.getReference();
        StorageReference child = storage.getReference()
                .child("testimony" + "/" + year + "/" + month + "/" + fileName);
        child.putFile(Uri.fromFile(new File(fileLocation)))
                .addOnProgressListener(taskSnapshot -> {
                    //dialog.setProgress((int) (taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount()));
                    Log.e("BYTE TRANS : ", String.valueOf(taskSnapshot.getBytesTransferred()));
                })
                .continueWithTask(task -> {
                    if (!task.isComplete()) {
                        try {
                            Log.e("UPLOAD ERROR ", task.getException().getMessage());
                        } catch (Throwable ignore) {

                        }
                    }
                    return child.getDownloadUrl();
                })
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String downloadUri = Objects.requireNonNull(task.getResult()).toString();
                        if (downloadUri == null) downloadUri = "";

                        //fill the url to the firestore
                        homeNoSqlDatabase.addTestimony(
                                new Testimony(downloadUri, String.valueOf(new Date().getTime())),
                                data -> {
                                    //done
                                    Log.e("upload category", "Done upload testimony");
                                    Toast.makeText(this, "Done create testimony", Toast.LENGTH_SHORT).show();
                                },
                                data -> {
                                    //fails
                                    Log.e("upload category", "Fail to upload testimony " + data);
                                    Toast.makeText(this, "Fail to create testimony", Toast.LENGTH_SHORT).show();
                                });
                        dialog.dismiss();

                    }
                });
    }
}
