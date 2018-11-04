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

/**
 * A simple {@link Fragment} subclass.
 */
public class MyChatFragment extends Fragment {

    private ListenerRegistration listenerRegistration;
    private PostNoSqlDataBase dataSource;
    private String uid;

    public MyChatFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //user id
        uid = FirebaseAuth.getInstance().getUid();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.forum_my_chart_fragment, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.my_chart_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //add listener to my chart fragment
        dataSource = new PostNoSqlDataBase(getContext());
        listenerRegistration = dataSource.getMyChatPost(uid, recyclerView, getContext());
        return view;
    }

    @Override
    public void onDestroy() {
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
        dataSource.offline(uid);
        super.onDestroy();
    }
}
