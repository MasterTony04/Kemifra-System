package com.fahamu.tech.chat.forumadmin.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fahamu.tech.chat.forumadmin.R;
import com.fahamu.tech.chat.forumadmin.database.PostDataSource;
import com.fahamu.tech.chat.forumadmin.database.PostNoSqlDataBase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllChartFragment extends Fragment {

    private ListenerRegistration listenerRegistration;
    private PostNoSqlDataBase dataSource;

    public AllChartFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.forum_all_chart_fragment, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.all_chart_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dataSource = new PostNoSqlDataBase(getContext());
        listenerRegistration = dataSource.getAllPosts(recyclerView, getContext());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
        dataSource.offline(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        super.onDestroy();
    }
}
