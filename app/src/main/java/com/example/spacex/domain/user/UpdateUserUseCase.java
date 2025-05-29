package com.example.spacex.domain.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.spacex.domain.entity.Status;

import java.util.function.Consumer;

public class UpdateUserUseCase {

    private final UserRepository repo;

    public UpdateUserUseCase(UserRepository repo) {
        this.repo = repo;
    }

    public void execute(
            @NonNull String id,
            @NonNull String name,
            @NonNull String username,
            @NonNull String email,
            @Nullable String phone,
            @Nullable String photoUrl,
            Consumer<Status<Void>> callback
    ) {
        repo.updateUser(id, name, username, email, phone, photoUrl, callback);
    }
}
