package com.fahamutech.adminapp.database.connector;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.fahamutech.adminapp.database.DataBaseCallback;
import com.fahamutech.adminapp.model.Article;

public interface ArticleDataSource {
    void getAllById(String categoryId, RecyclerView recyclerView,
                    SwipeRefreshLayout swipeRefreshLayout);

    void getAll(DataBaseCallback... callbacks);

    void createArticle(Article article, DataBaseCallback... callbacks);

    void deleteArticle(String docId, DataBaseCallback... callbacks);
}
