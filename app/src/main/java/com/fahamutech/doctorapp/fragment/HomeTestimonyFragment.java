package com.fahamutech.doctorapp.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fahamutech.doctorapp.R;
import com.fahamutech.doctorapp.database.connector.HomeDataSource;
import com.fahamutech.doctorapp.database.noSql.HomeNoSqlDatabase;

public class HomeTestimonyFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_testimony, container, false);
        return initView(inflate, new HomeNoSqlDatabase(inflate.getContext()));
    }


    private View initView(View view, HomeDataSource homeDataSource) {
        recyclerView = view.findViewById(R.id.testimony_cat_recy);
        swipeRefreshLayout = view.findViewById(R.id.testimony_cat_swipe);
        homeDataSource.getTestimony(recyclerView, swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() ->
                homeDataSource.getTestimony(recyclerView, swipeRefreshLayout));
        return view;
    }
}
