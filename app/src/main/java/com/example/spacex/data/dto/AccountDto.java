package com.example.spacex.data.dto;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class AccountDto {
    @NonNull
    @SerializedName("name")
    public String name;

    @NonNull
    @SerializedName("username")
    public String username;

    @NonNull
    @SerializedName("email")
    public String email;

    @NonNull
    @SerializedName("password")
    public String password;

    public AccountDto(
            @NonNull String name,
            @NonNull String username,
            @NonNull String email,
            @NonNull String password
    ) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
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

    @NonNull
    public String getPassword() {
        return password;
    }
}
