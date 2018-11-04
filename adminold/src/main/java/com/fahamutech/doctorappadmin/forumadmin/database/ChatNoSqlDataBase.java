package com.fahamu.tech.chat.forumadmin.database;

import android.content.Context;
import android.util.Log;

import com.fahamu.tech.chat.forumadmin.model.ChatMessages;
import com.fahamu.tech.chat.forumadmin.model.OnlineStatus;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class ChatNoSqlDataBase extends NoSqlDatabase implements ChatDataSource {

    public ChatNoSqlDataBase(Context context) {
        super(context);
    }

    /**
     * send a message to the specific user id
     *
     * @param docId        the id of the document to send
     * @param chatMessages the message body
     */
    @Override
    public void sendMessage(String docId, ChatMessages chatMessages, DataBaseCallback... dataBaseCallbacks) {
        firestore.collection(ForumC.FORUMS.name())
                .document(docId)
                .collection(ForumC.FORUM_MESSAGE.name())
                .document()
                .set(chatMessages, SetOptions.merge())
                .addOnSuccessListener(aVoid -> dataBaseCallbacks[0].then("done"))
                .addOnFailureListener(e -> {
                    dataBaseCallbacks[1].then("failed");
                    Log.e(TAG, "Failed to send message");
                });

        //update the thread

    }

    @Override
    public void updateDoctorSeen(String docId, boolean flag, DataBaseCallback... dataBaseCallbacks) {
        Map<String, Object> data = new HashMap<>();
        data.put("doctorSeen", flag);
        data.put("userSeen", !flag);
        firestore.collection(ForumC.FORUMS.name())
                .document(docId)
                .update(data)
                .addOnSuccessListener(aVoid ->
                        dataBaseCallbacks[0].then("done update doctor flag"))
                .addOnFailureListener(e ->
                        dataBaseCallbacks[1].then("failed to update doctor flag"));
    }

    @Override
    public void updateUserSeen(String docId, boolean flag, DataBaseCallback... dataBaseCallbacks) {
        Map<String, Object> data = new HashMap<>();
        data.put("doctorSeen", !flag);
        data.put("userSeen", flag);
        firestore.collection(ForumC.FORUMS.name())
                .document(docId)
                .update(data)
                .addOnSuccessListener(aVoid ->
                        dataBaseCallbacks[0].then("done update user flag"))
                .addOnFailureListener(e ->
                        dataBaseCallbacks[1].then("failed to update user flag"));
    }

    @Override
    public void updateThreadTimestamp(String docId, String time, DataBaseCallback... dataBaseCallbacks) {
        firestore.collection(ForumC.FORUMS.name()).document(docId)
                .update("time", time)
                .addOnSuccessListener(aVoid ->
                        dataBaseCallbacks[0].then("Done update : " + docId))
                .addOnFailureListener(e ->
                        dataBaseCallbacks[1]
                                .then("Failed to update : " + e.getLocalizedMessage()));
    }

    @Override
    public void getAllMessage(String docId, DataBaseCallback... dataBaseCallbacks) {
        firestore.collection(ForumC.FORUMS.name()).document(docId)
                .collection(ForumC.FORUM_MESSAGE.name())
                .orderBy("time", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null) {
                        List<ChatMessages> chatMessages = queryDocumentSnapshots.toObjects(ChatMessages.class);
                        dataBaseCallbacks[0].then(chatMessages);
                    }
                })
                .addOnFailureListener(e -> dataBaseCallbacks[1].then(e));
    }

    /**
     * first callback is the changed document and the second callback is for all document
     */
    @Override
    public Object receiveMessage(String docId, DataBaseCallback... dataBaseCallbacks) {
        return firestore.collection(ForumC.FORUMS.name()).document(docId)
                .collection(ForumC.FORUM_MESSAGE.name())
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (queryDocumentSnapshots != null) {
                        List<DocumentChange> documentChanges = queryDocumentSnapshots.getDocumentChanges();
                        for (DocumentChange doc : documentChanges) {
                            if (doc.getType() == DocumentChange.Type.ADDED)
                                dataBaseCallbacks[0].then(doc);
                        }
                    }
                });
    }

    @Override
    public Object onlineStatus(String userId, DataBaseCallback... dataBaseCallbacks) {
        return firestore.collection(ForumC.FORUM_ONLINE.name())
                .document(userId)
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (documentSnapshot != null) {
                        OnlineStatus onlineStatus = documentSnapshot.toObject(OnlineStatus.class);
                        dataBaseCallbacks[0].then(onlineStatus);
                    }
                });
    }
}
