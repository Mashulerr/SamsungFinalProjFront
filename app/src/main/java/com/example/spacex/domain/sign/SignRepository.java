package com.example.spacex.domain.sign;

import androidx.annotation.NonNull;

import com.example.spacex.domain.entity.Status;
import com.example.spacex.domain.entity.UserEntity;

import java.util.function.Consumer;

public interface SignRepository {

    void isUserExist(@NonNull String username, Consumer<Status<Void>> callback);

    void createAccount(
            @NonNull String name,
            @NonNull String username,
            @NonNull String email,
            @NonNull String password,
            Consumer<Status<Void>> callback
    );

    void login(
            @NonNull String username,
            @NonNull String password,
            Consumer<Status<UserEntity>> callback
    );

    void logout();
}
