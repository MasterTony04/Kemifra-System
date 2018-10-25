package com.fahamutech.adminapp.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.fahamutech.adminapp.R;
import com.fahamutech.adminapp.adapter.DelArtAdapter;
import com.fahamutech.adminapp.database.connector.ArticleDataSource;
import com.fahamutech.adminapp.database.noSql.ArticlesNoSqlDatabase;
import com.fahamutech.adminapp.model.Article;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.List;

public class DeleteArticleActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ArticleDataSource articleDataSource;
    private ListenerRegistration all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_article);
        bindView();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setTitle("Delete Article");
            supportActionBar.setSubtitle("Long click to delete");
        }

        //init database
        articleDataSource = new ArticlesNoSqlDatabase(this);
        initView();

        swipeRefreshLayout.setOnRefreshListener(this::initView);
    }

    private void bindView() {
        swipeRefreshLayout = findViewById(R.id.delete_articles_swipe);
        recyclerView = findViewById(R.id.delete_article_recy);
    }

    private void initView() {
        swipeRefreshLayout.setRefreshing(true);
        all = (ListenerRegistration) articleDataSource.getAll(
                data -> {
                    //done
                    List<Article> articles = (List<Article>) data;
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    recyclerView.setAdapter(new DelArtAdapter(articles, this));
                    swipeRefreshLayout.setRefreshing(false);
                }
        );
    }

    @Override
    protected void onDestroy() {
        if (all != null) all.remove();
        super.onDestroy();
    }
}
