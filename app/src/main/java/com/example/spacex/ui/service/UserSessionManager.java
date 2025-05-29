package com.example.spacex.ui.service;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.spacex.domain.entity.UserEntity;

public class UserSessionManager {

    private static final String PREFS_NAME = "user_prefs";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_NAME = "name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_PHOTO_URL = "photoUrl";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_PASSWORD = "password";

    private final SharedPreferences sharedPreferences;

    public UserSessionManager(@NonNull Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveUser(@NonNull UserEntity user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_ID, user.getId());
        editor.putString(KEY_NAME, user.getName());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_PHONE, user.getPhone());
        editor.putString(KEY_PHOTO_URL, user.getPhotoUrl());
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    public void saveCredentials(@NonNull String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    @Nullable
    public String getPassword() {
        return sharedPreferences.getString(KEY_PASSWORD, null);
    }

    @Nullable
    public UserEntity getUser() {
        String id = sharedPreferences.getString(KEY_USER_ID, null);
        String name = sharedPreferences.getString(KEY_NAME, null);
        String username = sharedPreferences.getString(KEY_USERNAME, null);
        String email = sharedPreferences.getString(KEY_EMAIL, null);
        String phone = sharedPreferences.getString(KEY_PHONE, null);
        String photoUrl = sharedPreferences.getString(KEY_PHOTO_URL, null);

        if (id == null || username == null) return null;

        return new UserEntity(id, phone, name, username, email, photoUrl);
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }
}
