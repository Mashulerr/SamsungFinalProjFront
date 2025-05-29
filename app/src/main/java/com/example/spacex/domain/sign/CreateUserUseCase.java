package com.example.spacex.domain.sign;

import androidx.annotation.NonNull;

import com.example.spacex.domain.entity.Status;

import java.util.function.Consumer;

public class CreateUserUseCase {

    private final SignRepository repo;

    public CreateUserUseCase(SignRepository repo) {
        this.repo = repo;
    }

    public void execute(
            @NonNull String name,
            @NonNull String username,
            @NonNull String email,
            @NonNull String password,
            Consumer<Status<Void>> callback
    ){
        repo.createAccount(name, username, email, password, callback);
    }
}
