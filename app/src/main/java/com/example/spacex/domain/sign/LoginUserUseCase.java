package com.example.spacex.domain.sign;

import androidx.annotation.NonNull;

import com.example.spacex.domain.entity.Status;
import com.example.spacex.domain.entity.UserEntity;

import java.util.function.Consumer;

public class LoginUserUseCase {

    private final SignRepository repo;

    public LoginUserUseCase(SignRepository repo) {
        this.repo = repo;
    }

    public void execute(
            @NonNull String username,
            @NonNull String password,
            Consumer<Status<UserEntity>> callback) {
        repo.login(username, password, (status) -> {
            if (status.getStatusCode() != 200) repo.logout();
            callback.accept(status);
        });
    }
}
