package com.fahamu.tech.chat.forumadmin.database;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.View;

import com.fahamu.tech.chat.forumadmin.model.Patient;

public interface UserDataSource {
    void createUser(Patient patient);

    void getUser(String userId, SwipeRefreshLayout swipeRefreshLayout, DataBaseCallback... dataBaseCallback);

    void getUserSubscription(String docId, SwipeRefreshLayout swipeRefreshLayout, DataBaseCallback... dataBaseCallback);

    void updateUser(String userId, SwipeRefreshLayout swipeRefreshLayout, View... views);

    void getReceipts(String docId,SwipeRefreshLayout swipeRefreshLayout,DataBaseCallback... dataBaseCallbacks);
}
