package com.example.spacex.domain.user;

import androidx.annotation.NonNull;

import com.example.spacex.domain.entity.Status;
import com.example.spacex.domain.entity.UserEntity;

import java.util.function.Consumer;

public class GetUserByIdUseCase {

    private final UserRepository repo;

    public GetUserByIdUseCase(UserRepository repo) {
        this.repo = repo;
    }

    public void execute(@NonNull String id, Consumer<Status<UserEntity>> callback) {
        repo.getUser(id, callback);
    }
}
