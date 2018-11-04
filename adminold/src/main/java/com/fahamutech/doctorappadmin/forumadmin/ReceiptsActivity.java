package com.fahamu.tech.chat.forumadmin;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.fahamu.tech.chat.forumadmin.adapter.ReceiptsAdapter;
import com.fahamu.tech.chat.forumadmin.database.DataBaseCallback;
import com.fahamu.tech.chat.forumadmin.database.UserDataSource;
import com.fahamu.tech.chat.forumadmin.database.UserNoSqlDataBase;
import com.fahamu.tech.chat.forumadmin.model.Receipt;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ReceiptsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private UserDataSource userDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_activity_receipts);
        bindView();

        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setTitle("Payment History");
        }

        //debug
        fabAction();
        //initialize ui with fake data
        initUI();
        //testing
        swipeRefreshLayout.setOnRefreshListener(this::initUI);
    }

    private void bindView() {
        toolbar = findViewById(R.id.toolbar);
        swipeRefreshLayout = findViewById(R.id.receipt_swipe);
        recyclerView = findViewById(R.id.receipt_recy);
        fab = findViewById(R.id.fab);
    }

    private void fabAction() {
        fab.setOnClickListener(view ->
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show());
    }

    private void initUI() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userDataSource = new UserNoSqlDataBase(this);
            userDataSource.getReceipts(currentUser.getEmail(), swipeRefreshLayout, (DataBaseCallback) data -> {
                List<Receipt> receipts = (List<Receipt>) data;
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(new ReceiptsAdapter(this, receipts));
            });
        }
    }

}
