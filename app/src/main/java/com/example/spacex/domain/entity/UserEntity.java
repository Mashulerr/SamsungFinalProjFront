package com.example.spacex.domain.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UserEntity {

    @NonNull
    private final String id;

    @Nullable
    private final String phone;

    @NonNull
    private final String name;

    @NonNull
    private final String username;

    @NonNull
    private final String email;

    @Nullable
    private final String photoUrl;

    public UserEntity(
            @NonNull String id,
            @Nullable String phone,
            @NonNull String name,
            @NonNull String username,
            @NonNull String email,
            @Nullable String photoUrl
    ) {
        this.id = id;
        this.phone = phone;
        this.name = name;
        this.username = username;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @Nullable
    public String getPhone() {
        return phone;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @Nullable
    public String getPhotoUrl() {
        return photoUrl;
    }
}
