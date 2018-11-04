package com.fahamutech.doctorapp.database.connector;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;

public interface HomeDataSource {

    void getCategory(RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout);

    void getTestimony(RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout);
}
