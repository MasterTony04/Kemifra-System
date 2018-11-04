package com.fahamutech.doctorappadmin.session;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {

    private SharedPreferences sharedPreferences;
    private static String CAT = "_category_";
    private static String TITLE = "_title_";

    public Session(Context context) {
        sharedPreferences = context.getSharedPreferences("jhsffsaiuda", Context.MODE_PRIVATE);
    }

    public void saveLastCategory(String category) {
        sharedPreferences.edit().putString(CAT, category).apply();
    }

    public void saveLastTitle(String title) {
        sharedPreferences.edit().putString(TITLE, title).apply();
    }

    public String getLastCategory() {
        return sharedPreferences.getString(CAT, "");
    }

    public String getLastTitle() {
        return sharedPreferences.getString(TITLE, "");
    }
}
