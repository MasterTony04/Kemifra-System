package com.fahamutech.adminapp.chat21;

import org.chat21.android.ui.login.activities.ChatSplashActivity;

public class MySplashActivity extends ChatSplashActivity {

    @Override
    protected Class<?> getTargetClass() {
        return ChatMainActivity.class;
    }
}
