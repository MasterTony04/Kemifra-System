package com.fahamutech.adminapp.database.noSql;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.fahamutech.adminapp.adapter.ArtAdapter;
import com.fahamutech.adminapp.database.DataBaseCallback;
import com.fahamutech.adminapp.database.connector.ArticleDataSource;
import com.fahamutech.adminapp.model.Article;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import java.util.List;

public class ArticlesNoSqlDatabase extends NoSqlDatabase implements ArticleDataSource {

    public ArticlesNoSqlDatabase(Context context) {
        super(context);
    }

    @Override
    public void getAllById(String categoryId, RecyclerView recyclerView,
                           SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setRefreshing(true);
        firestore.collection(NoSqlColl.ARTICLES.name())
                .whereEqualTo("categoryId", categoryId)
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Article> articles = queryDocumentSnapshots.toObjects(Article.class);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(new ArtAdapter(articles, context));
                    swipeRefreshLayout.setRefreshing(false);
                })
                .addOnFailureListener(e -> {
                    Snackbar.make(swipeRefreshLayout, "Failed to get articles", Snackbar.LENGTH_SHORT).show();
                    Log.e("TAG ARTICLES", String.valueOf(e));
                    swipeRefreshLayout.setRefreshing(false);
                });
    }

    @Override
    public void getAll(DataBaseCallback... callbacks) {
        firestore.collection(NoSqlColl.ARTICLES.name()).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null) {
                        List<Article> articles = queryDocumentSnapshots.toObjects(Article.class);
                        if (callbacks != null) callbacks[0].then(articles);
                    }
                })
                .addOnFailureListener(e -> {
                    if (callbacks != null) callbacks[1].then("fail " + e.getLocalizedMessage());
                });
    }

    @Override
    public void createArticle(Article article, DataBaseCallback... callbacks) {
        DocumentReference document = firestore.collection(NoSqlColl.ARTICLES.name()).document();
        article.setId(document.getId());
        document.set(article, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    if (callbacks != null) callbacks[0].then("done");
                })
                .addOnFailureListener(e -> {
                    if (callbacks != null) callbacks[1].then("fail " + e.getLocalizedMessage());
                });
    }

    @Override
    public void deleteArticle(String docId, DataBaseCallback... callbacks) {
        firestore.collection(NoSqlColl.ARTICLES.name()).document(docId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    if (callbacks != null) callbacks[0].then("done");
                })
                .addOnFailureListener(e -> {
                    if (callbacks != null) callbacks[1].then("fail " + e.getLocalizedMessage());
                });
    }


}
