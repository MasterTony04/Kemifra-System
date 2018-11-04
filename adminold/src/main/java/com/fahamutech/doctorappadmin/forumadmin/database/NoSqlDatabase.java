package com.fahamu.tech.chat.forumadmin.database;

import android.content.Context;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public abstract class NoSqlDatabase {

    protected Context context;
    FirebaseFirestore firestore;
    static String TAG = "NOSQLDATA";

    public NoSqlDatabase(Context context) {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setSslEnabled(true)
                .build();
        firestore = FirebaseFirestore.getInstance();
        firestore.setFirestoreSettings(settings);
        this.context = context;
    }

}
