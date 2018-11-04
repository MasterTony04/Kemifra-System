package com.fahamu.tech.chat.forumadmin.database;

import com.fahamu.tech.chat.forumadmin.model.ChatMessages;

public interface ChatDataSource {
    void sendMessage(String docId, ChatMessages chatMessages, DataBaseCallback... dataBaseCallbacks);

    void updateDoctorSeen(String docId, boolean flag, DataBaseCallback... dataBaseCallbacks);

    void updateUserSeen(String docId, boolean flag, DataBaseCallback... dataBaseCallbacks);

    void updateThreadTimestamp(String docId, String time, DataBaseCallback... dataBaseCallbacks);

    void getAllMessage(String docId, DataBaseCallback... dataBaseCallbacks);

    Object receiveMessage(String docId, DataBaseCallback... dataBaseCallbacks);

    Object onlineStatus(String userId, DataBaseCallback... dataBaseCallbacks);
}
