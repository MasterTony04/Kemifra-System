package com.fahamutech.doctorappadmin.database.noSql;

import android.content.Context;
import android.util.Log;

import com.fahamutech.doctorappadmin.adapter.CatAdapter;
import com.fahamutech.doctorappadmin.model.Category;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.List;

public abstract class NoSqlDatabase {

    FirebaseFirestore firestore;
    protected Context context;

    NoSqlDatabase(Context context) {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setSslEnabled(true)
                .build();
        this.firestore = FirebaseFirestore.getInstance();
        this.firestore.setFirestoreSettings(settings);
        this.context = context;
    }


}
