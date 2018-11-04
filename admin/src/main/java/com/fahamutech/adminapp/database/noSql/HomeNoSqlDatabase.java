package com.fahamutech.adminapp.database.noSql;

import android.content.Context;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

import com.fahamutech.adminapp.adapter.CatAdapter;
import com.fahamutech.adminapp.adapter.TestimonyAdapter;
import com.fahamutech.adminapp.database.DataBaseCallback;
import com.fahamutech.adminapp.database.connector.HomeDataSource;
import com.fahamutech.adminapp.model.Category;
import com.fahamutech.adminapp.model.ITestimony;
import com.fahamutech.adminapp.model.Testimony;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeNoSqlDatabase extends NoSqlDatabase implements HomeDataSource {

    private List<Category> cat = new ArrayList<>();

    public HomeNoSqlDatabase(Context context) {
        super(context);
    }

    @Override
    public Object getCategory(RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setRefreshing(true);
        return firestore.collection(NoSqlColl.CATEGORY.name())
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (queryDocumentSnapshots != null) {
                        List<Category> categories = queryDocumentSnapshots.toObjects(Category.class);
                        //save this list of category for later use
                        // MainActivity.categoryList = categories;
                        //new Session(context).saveCategories(categories);
                        CatAdapter catAdapter = new CatAdapter(categories, context);
                        recyclerView.setLayoutManager(new LinearLayoutManager(this.context));
                        recyclerView.setAdapter(catAdapter);
                        swipeRefreshLayout.setRefreshing(false);
                        Log.e("TAG***", " done get categories");
                    }
                    if (e != null) {
                        Log.e("NO SQL ", "fail to listen to the database");
                    }
                });
    }

    public void getAllCategory(DataBaseCallback callbacks) {

        firestore.collection(NoSqlColl.CATEGORY.name())
                .addSnapshotListener((QuerySnapshot snapshots, FirebaseFirestoreException e) -> {
                    if (callbacks != null) callbacks.then(snapshots);
                });
    }

    public Object getTestimony(RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout) {

        swipeRefreshLayout.setRefreshing(true);
        return firestore.collection(NoSqlColl.TESTIMONY.name())
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (queryDocumentSnapshots != null) {
                        List<Testimony> testimonies = queryDocumentSnapshots.toObjects(Testimony.class);
                        TestimonyAdapter testimonyAdapter = new TestimonyAdapter(this.context, testimonies);
                        recyclerView.setLayoutManager(new GridLayoutManager(this.context, 2));
                        recyclerView.setAdapter(testimonyAdapter);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    if (e != null) {
                        Log.e("get testimony", "failed " + e.getLocalizedMessage());
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    @Override
    public void addTestimony(ITestimony testimony, DataBaseCallback... callbacks) {
        DocumentReference testimony1 = firestore.collection("TESTIMONY").document();
        testimony.setId(testimony1.getId());

        testimony1.set(testimony)
                .addOnSuccessListener(aVoid -> {
                    if (callbacks[0] != null) callbacks[0].then("done");
                })
                .addOnFailureListener(e -> {
                    if (callbacks[1] != null) callbacks[1].then("fail " + e.getLocalizedMessage());
                });
    }

    @Override
    public void deleteTestimony(String docId, DataBaseCallback... callbacks) {
        firestore.collection("TESTIMONY")
                .document(docId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    if (callbacks[0] != null) callbacks[0].then("done");
                })
                .addOnFailureListener(e -> {
                    if (callbacks[1] != null) callbacks[1].then("fail " + e.getLocalizedMessage());
                });
    }

}
