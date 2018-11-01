package com.fahamutech.doctorapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.fahamutech.doctorapp.R;
import com.fahamutech.doctorapp.adapter.HomePageFragmentAdapter;
import com.fahamutech.doctorapp.chat21.ChatMainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.chat21.android.ui.login.activities.ChatLoginActivity;

public class MainActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private BillingProcessor billingProcessor;

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

        //billing
        billingProcessor = BillingProcessor
                .newBillingProcessor(this, getString(R.string.play_licence), this);
        billingProcessor.initialize();

        //tab layout
        initViewPager();

        //pay
        initVipContent();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!billingProcessor.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initVipContent() {
        fab.setOnClickListener(view -> {
            Snackbar.make(view, "Chat is opening...", Snackbar.LENGTH_SHORT).show();
            boolean iabServiceAvailable = BillingProcessor.isIabServiceAvailable(this);
            if (iabServiceAvailable) {
                if (billingProcessor.isSubscriptionUpdateSupported()) {

                    if (billingProcessor.isSubscribed(getString(R.string.chat_sub_product))) {
                        onProductPurchased(getString(R.string.chat_sub_product),
                                billingProcessor.getSubscriptionTransactionDetails(getString(R.string.chat_sub_product)));
                    } else {
                        Log.e("TAG**PURCHASE ", "purchase initialized");
                        billingProcessor.subscribe(this, getString(R.string.chat_sub_product));
                    }
                }
            }
        });
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

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        Log.e("PAY PURCHASED", productId);
        //todo : check the date of the product purchase in order to consume it
        startActivity(new Intent(this, ChatMainActivity.class));
    }

    @Override
    public void onPurchaseHistoryRestored() {
        Log.e("PAY RESTORE", "purchase restored");
    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        Log.e("PAY ERROR", "error when try to bill :-> code " + String.valueOf(errorCode));
    }

    @Override
    public void onBillingInitialized() {
        Log.e("TAG BILL", "payment is initialized");
    }

    @Override
    protected void onDestroy() {
        if (billingProcessor != null) {
            billingProcessor.release();
        }
        super.onDestroy();
    }
}
