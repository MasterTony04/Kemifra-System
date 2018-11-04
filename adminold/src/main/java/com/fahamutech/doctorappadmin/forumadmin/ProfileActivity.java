package com.fahamu.tech.chat.forumadmin;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.fahamu.tech.chat.forumadmin.database.DataBaseCallback;
import com.fahamu.tech.chat.forumadmin.database.PostNoSqlDataBase;
import com.fahamu.tech.chat.forumadmin.database.UserDataSource;
import com.fahamu.tech.chat.forumadmin.database.UserNoSqlDataBase;
import com.fahamu.tech.chat.forumadmin.model.Patient;
import com.fahamu.tech.chat.forumadmin.model.UserSubscription;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private TextInputEditText phoneEditText;
    private TextInputEditText fullName;
    private TextInputEditText email;
    private TextInputEditText addressTextEdit;
    private Button updateProfileButton;
    private TextView amountTextView;
    private TextInputEditText subscriptionTextEdit;
    private TextInputEditText statusTextEdit;
    private Button payButton;
    private Button receiptButton;
    private CircleImageView circleImageView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private UserDataSource noSqlDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_activity_sprofile);
        bindView();

        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setTitle("Profile");
        }

        noSqlDatabase = new UserNoSqlDataBase(this);

        //for testing
        contactUs();

        //listener
        buttons();

        //user data
        getUserDetails();

        //subscription details
        //getSubDetails();
    }

    private void getUserDetails() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) noSqlDatabase.getUser(currentUser.getUid(), swipeRefreshLayout,
                (DataBaseCallback) data -> {
                    Patient patient = (Patient) data;
                    fullName.setText(patient.getName());
                    email.setText(patient.getEmail());
                    phoneEditText.setText(patient.getPhoneNumber());
                    addressTextEdit.setText(patient.getAddress());
                    try {
                        Glide.with(this).load(patient.getPhoto()).into(circleImageView);
                    } catch (Throwable ignore) {
                    }
                    //call get subscription
                    getSubDetails();
                });
    }

    private void updateProfile() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) noSqlDatabase.updateUser(currentUser.getUid()
                , swipeRefreshLayout
                , fullName, email, phoneEditText, addressTextEdit);
    }

    private void getSubDetails() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            noSqlDatabase.getUserSubscription(currentUser.getEmail(), swipeRefreshLayout,
                    (DataBaseCallback) data -> {
                        UserSubscription subscription = (UserSubscription) data;
                        amountTextView.setText("0");
                    });
        }
    }

    private void bindView() {
        fullName = findViewById(R.id.profile_fullname);
        email = findViewById(R.id.profile_email);
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        phoneEditText = findViewById(R.id.profile_phone);
        addressTextEdit = findViewById(R.id.profile_address);
        updateProfileButton = findViewById(R.id.profile_update_profile);
        amountTextView = findViewById(R.id.profile_amount);
        subscriptionTextEdit = findViewById(R.id.profile_subscription);
        statusTextEdit = findViewById(R.id.profile_status);
        payButton = findViewById(R.id.profile_payment);
        receiptButton = findViewById(R.id.profile_payment_history);
        circleImageView = findViewById(R.id.profile_image);
        swipeRefreshLayout = findViewById(R.id.profile_swipe);
    }

    private void buttons() {
        receiptButton.setOnClickListener(v -> {
            startActivity(new Intent(this, ReceiptsActivity.class));
        });

        payButton.setOnClickListener(v -> {
            payDialog();
        });

        updateProfileButton.setOnClickListener(v -> {
            updateProfile();
            Snackbar.make(v, "Updating profile...", Snackbar.LENGTH_SHORT).show();
        });
    }

    private void payDialog() {
        new MaterialDialog.Builder(this)
                .title("Choose Package")
                .customView(R.layout.forum_checkout, true)
                .positiveText("Pay")
                .negativeText("Cancel")
                .autoDismiss(false)
                .canceledOnTouchOutside(false)
                .onNegative((dialog, which) -> {
                    Snackbar.make(payButton,
                            "Payment Canceled", Snackbar.LENGTH_SHORT).show();
                    dialog.dismiss();
                })
                .onPositive((dialog, which) -> {
                    View customView = dialog.getCustomView();
                    if (customView != null) {
                        String amount;
                        Intent intent = new Intent(this, PayActivity.class);
                        RadioButton month = customView.findViewById(R.id.pay_monthly);
                        RadioButton sixMonth = customView.findViewById(R.id.pay_six_month);
                        RadioButton year = customView.findViewById(R.id.pay_twelve_month);

                        if (month.isChecked()) {
                            amount = "10000";
                            intent.putExtra("_amount", amount);
                            dialog.dismiss();
                            startActivity(intent);
                            //Snackbar.make(customView, "Month", Snackbar.LENGTH_SHORT).show();
                        } else if (sixMonth.isChecked()) {
                            amount = "50000";
                            intent.putExtra("_amount", amount);
                            dialog.dismiss();
                            startActivity(intent);
                            //Snackbar.make(customView, "6 month", Snackbar.LENGTH_SHORT).show();
                        } else if (year.isChecked()) {
                            amount = "100000";
                            intent.putExtra("_amount", amount);
                            dialog.dismiss();
                            startActivity(intent);
                            //Snackbar.make(customView, "year", Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(customView, "Choose package first", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                })
                .show();
    }

    private void contactUs() {
        fab.setOnClickListener(view ->
                Snackbar.make(view, "Help text", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.forum_profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.profile_logout) {
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    //todo : this method is to be changed
    private void logout() {
        //set offline flag
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        new PostNoSqlDataBase(this).offline(uid);

        //logout
        // FirebaseAuth mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut();
        FirebaseAuth.getInstance().signOut();
        //go to the main activity
        startActivity(new Intent(this, SignUpActivity.class));
        finish();
    }

}
