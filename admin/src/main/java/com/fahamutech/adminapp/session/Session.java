package com.fahamutech.adminapp.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.fahamutech.adminapp.model.Category;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

public class Session {

    public static String PAY_OK = "AkjgaoHFuf687V";
    public static String PAY_NOT_OK = "uy5rhgJFFjh";
    public static String PAY_D = "T74r74jhkjklh";

    private SharedPreferences sharedPreferences;
    private String CAT = "_category_";
    private String TITLE = "_title_";
    private String PATIENT = "_patient_";
    private String PAY = "_payment_";
    private String CATEGORIES="_category_list_";

    public Session(Context context) {
        sharedPreferences = context.getSharedPreferences("jhsffsaiuda", Context.MODE_PRIVATE);
    }

    public void userPay(String pay) {
        sharedPreferences.edit().putString(PAY, pay).apply();
    }

    public String getUserPay() {
        return sharedPreferences.getString(PAY, PAY_D);
    }

//    public void saveUser(Doctor doctor) {
//        SharedPreferences.Editor edit = sharedPreferences.edit();
//        Gson gson = new Gson();
//        String s = gson.toJson(doctor);
//        edit.putString(PATIENT, s).apply();
//    }

    //to be changed
    public void saveCategories(Object snapshots){
        SharedPreferences.Editor edit = sharedPreferences.edit();
        Gson gson = new Gson();
        Type typeOfSrc = new TypeToken <List<Category>>(){}.getType();
        String s = gson.toJson(snapshots);
        edit.putString(CATEGORIES, s).apply();
        Log.e("TAG save categories","done saved");
    }

    //to be changed
    public Object getCategories(){
        String string = sharedPreferences.getString(CATEGORIES, "");
        if (string.isEmpty()) {
            return null;
        } else {
            Gson gson = new Gson();
            return gson.fromJson(string, Object.class);
        }
    }

//    public Doctor getSavedUser() {
//        String string = sharedPreferences.getString(PATIENT, "");
//        if (string.isEmpty()) {
//            return null;
//        } else {
//            Gson gson = new Gson();
//            return gson.fromJson(string, Doctor.class);
//        }
//    }

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
