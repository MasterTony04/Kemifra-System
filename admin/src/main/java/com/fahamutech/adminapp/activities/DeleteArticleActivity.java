package com.fahamutech.adminapp.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.fahamutech.adminapp.R;
import com.fahamutech.adminapp.database.connector.ArticleDataSource;
import com.fahamutech.adminapp.database.noSql.ArticlesNoSqlDatabase;

public class DeleteArticleActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ArticleDataSource articleDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_article);
        bindView();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view ->
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show());

        //init database
        articleDataSource = new ArticlesNoSqlDatabase(this);
        initView();
    }

    private void bindView() {
        swipeRefreshLayout = findViewById(R.id.delete_articles_swipe);
        recyclerView = findViewById(R.id.delete_article_recy);
    }
    private void initView(){
        articleDataSource.getAll(
                data->{
                    //done

                },
                data->{
                    //error
                }
        );
    }

}
