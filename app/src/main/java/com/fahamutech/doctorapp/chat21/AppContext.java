package com.fahamutech.doctorapp.chat21;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;

import com.google.firebase.database.FirebaseDatabase;

public class AppContext extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this); // add this
        //called once
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (Throwable ignore) {

        }
    }
}
