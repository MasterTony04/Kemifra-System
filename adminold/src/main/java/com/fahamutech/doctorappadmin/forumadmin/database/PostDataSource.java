package com.fahamu.tech.chat.forumadmin.database;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import com.fahamu.tech.chat.forumadmin.model.ChatTopic;

public interface PostDataSource {
    Object getMyChatPost(String id, RecyclerView recyclerView, Context context);

    void deletePost(String docId);

    void createChatTopic(ChatTopic chatTopic);

    Object getAllPosts(RecyclerView recyclerView, Context context);
}
