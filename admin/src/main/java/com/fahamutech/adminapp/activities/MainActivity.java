package com.fahamutech.adminapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.fahamutech.adminapp.R;
import com.fahamutech.adminapp.adapter.HomePageFragmentAdapter;
import com.fahamutech.adminapp.chat21.MySplashActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private ViewPager viewPager;
    private TabLayout tabLayout;


    @Override
    protected void onStart() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, MySplashActivity.class));
        }
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        bindView();
        setSupportActionBar(toolbar);

        //tab layout
        initViewPager();

        //for testing
        fab.setOnClickListener(view -> {
            Snackbar.make(view, "Chat is opening", Snackbar.LENGTH_SHORT).show();
            //startActivity(new Intent(this, MySplashActivity.class));
        });
    }

    @Override
    protected void onResume() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, MySplashActivity.class));
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
        }else if (id==R.id.action_delete_article){
            startActivity(new Intent(this,DeleteArticleActivity.class));
        }

        return super.onOptionsItemSelected(item);
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
}
