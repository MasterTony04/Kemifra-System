package com.fahamu.tech.chat.forumadmin.config;

import androidx.appcompat.app.AppCompatActivity;

public class Config {
    private static Class<? extends AppCompatActivity> mainActivity;

    public Config(Class<? extends AppCompatActivity> mainActivity) {
        Config.mainActivity = mainActivity;
    }

    public static Class<? extends AppCompatActivity> getMainActivity() {
        return mainActivity;
    }

}
