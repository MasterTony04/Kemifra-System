package com.fahamutech.adminapp.database.connector;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.fahamutech.adminapp.database.DataBaseCallback;
import com.fahamutech.adminapp.model.ITestimony;

public interface HomeDataSource {

    Object getCategory(RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout);

    Object getTestimony(RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout);

    void addTestimony(ITestimony testimony, DataBaseCallback... callbacks);

    void deleteTestimony(String docId, DataBaseCallback... callbacks);

}
