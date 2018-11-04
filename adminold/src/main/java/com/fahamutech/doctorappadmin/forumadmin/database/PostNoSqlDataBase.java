package com.fahamu.tech.chat.forumadmin.database;

import android.content.Context;
import android.net.ConnectivityManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

import com.fahamu.tech.chat.forumadmin.adapter.AllChatFragmentAdapter;
import com.fahamu.tech.chat.forumadmin.adapter.MyChatFragmentAdapter;
import com.fahamu.tech.chat.forumadmin.model.ChatTopic;
import com.fahamu.tech.chat.forumadmin.model.OnlineStatus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import java.util.List;
import java.util.Objects;

public class PostNoSqlDataBase extends NoSqlDatabase implements PostDataSource {

    public PostNoSqlDataBase(Context context) {
        super(context);
    }

    /**
     * create new topic in a chat
     *
     * @param chatTopic topic object
     */
    @Override
    public void createChatTopic(ChatTopic chatTopic) {
        DocumentReference forum_posts = firestore.collection(ForumC.FORUMS.name()).document();
        chatTopic.setDocId(forum_posts.getId());
        forum_posts.set(chatTopic, SetOptions.merge());
    }

    /**
     * listener of the user topics
     * get the list of the topic specific user write
     *
     * @param userId       user id
     * @param recyclerView the container of the view
     * @param context      the activity to show the list
     */
    @Override
    public ListenerRegistration getMyChatPost(String userId, RecyclerView recyclerView, Context context) {

        //update online status
        online(userId);

        //return the listener
        return firestore.collection(ForumC.FORUMS.name())
                .whereEqualTo("userId", userId)
                .orderBy("time", Query.Direction.DESCENDING)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {

                    if (queryDocumentSnapshots != null) {
                        List<ChatTopic> chatTopics = queryDocumentSnapshots.toObjects(ChatTopic.class);
                        MyChatFragmentAdapter adapter = new MyChatFragmentAdapter(chatTopics, context);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                    if (e != null) {
                        Log.e(TAG, "error getting the post, cause : " + e.getLocalizedMessage());
                    }
                });
    }

    /**
     * delete the post of a specific user
     *
     * @param docId the id of the document to delete
     */
    @Override
    public void deletePost(String docId) {
        DocumentReference document = firestore.collection(ForumC.FORUMS.name()).document(docId);
        document.delete()
                .addOnSuccessListener(aVoid -> Log.e(TAG, "delete : " + docId))
                .addOnFailureListener(e -> Log.e(TAG, "not deleted, cause : " + e));
    }

    /**
     * get all the topic available in the database
     *
     * @param recyclerView the list view for the topics
     * @param context      the activity to show the view
     */
    @Override
    public ListenerRegistration getAllPosts(RecyclerView recyclerView, Context context) {
        //set online user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser!=null)online(currentUser.getUid());

        return firestore.collection(ForumC.FORUMS.name())
                .orderBy("time", Query.Direction.DESCENDING)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {

                    if (queryDocumentSnapshots != null) {
                        List<ChatTopic> chatTopics = queryDocumentSnapshots.toObjects(ChatTopic.class);

                        RecyclerView.Adapter adapter;
                        adapter = new AllChatFragmentAdapter(chatTopics, context);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                    if (e != null) {
                        Log.e("POSTNOSQL", "Problem get posts,reason : " + e);
                    }
                });
    }

    private void online(String userId) {
        if (isNetworkConnected()) {
            try {
                firestore.collection(ForumC.FORUM_ONLINE.name())
                        .document(userId)
                        .set(new OnlineStatus("online"), SetOptions.merge());
            } catch (Throwable throwable) {
                Log.e(TAG, throwable.getLocalizedMessage());
            }
        } else {
            offline(userId);
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return Objects.requireNonNull(cm).getActiveNetworkInfo() != null;
    }

    public void offline(String userId) {
        if (userId != null) {
            firestore.collection(ForumC.FORUM_ONLINE.name())
                    .document(userId)
                    .set(new OnlineStatus("offline"), SetOptions.merge());
        }
    }


}
