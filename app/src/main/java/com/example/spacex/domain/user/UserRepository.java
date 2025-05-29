package com.example.spacex.domain.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.spacex.domain.entity.Status;
import com.example.spacex.domain.entity.UserEntity;

import java.util.function.Consumer;

public interface UserRepository {

    void getUser(@NonNull String id, Consumer<Status<UserEntity>> callback);

    void updateUser(@NonNull String id,
                    @NonNull String name,
                    @NonNull String username,
                    @NonNull String email,
                    @Nullable String phone,
                    @Nullable String photoUrl,
                    Consumer<Status<Void>> callback);
}
