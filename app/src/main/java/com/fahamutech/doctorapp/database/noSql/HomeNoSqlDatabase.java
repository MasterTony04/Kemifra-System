package com.fahamutech.doctorapp.database.noSql;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.fahamutech.doctorapp.adapter.CatAdapter;
import com.fahamutech.doctorapp.adapter.TestimonyAdapter;
import com.fahamutech.doctorapp.database.connector.HomeDataSource;
import com.fahamutech.doctorapp.model.Category;
import com.fahamutech.doctorapp.model.Testimony;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeNoSqlDatabase extends NoSqlDatabase implements HomeDataSource {

    private List<Category> categories = new ArrayList<>();
    private List<Testimony> testimonies = new ArrayList<>();

    public HomeNoSqlDatabase(Context context) {
        super(context);
    }


    @Override
    public void getCategory(RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout) {

        swipeRefreshLayout.setRefreshing(true);

        Task<QuerySnapshot> querySnapshotTask = firestore.collection(NoSqlColl.CATEGORY.name()).get();
        querySnapshotTask
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    categories.addAll(queryDocumentSnapshots.toObjects(Category.class));
                    CatAdapter catAdapter = new CatAdapter(categories, context);
                    recyclerView.setLayoutManager(new GridLayoutManager(this.context, 2));
                    recyclerView.setAdapter(catAdapter);
                    swipeRefreshLayout.setRefreshing(false);
                    Log.e("TAG***", " done get categories");

                }).addOnFailureListener(e -> {
                    Log.e("TAG***", "Failed to get category : " + e);
                    swipeRefreshLayout.setRefreshing(false);
                }
        );

    }

    public void getTestimony(RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout) {

        swipeRefreshLayout.setRefreshing(true);

        CollectionReference collection = firestore.collection(NoSqlColl.TESTIMONY.name());
        collection
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    testimonies.addAll(queryDocumentSnapshots.toObjects(Testimony.class));
                    TestimonyAdapter testimonyAdapter=new TestimonyAdapter(this.context,testimonies);
                    recyclerView.setLayoutManager(new GridLayoutManager(this.context,2));
                    recyclerView.setAdapter(testimonyAdapter);
                    swipeRefreshLayout.setRefreshing(false);
                })
                .addOnFailureListener(e -> {
                    Log.e("TAG***", "Failed to get testimonies : " + e);
                    swipeRefreshLayout.setRefreshing(false);
                });
    }
}
