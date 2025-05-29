package com.example.spacex.domain.sign;

import androidx.annotation.NonNull;

import com.example.spacex.domain.entity.Status;

import java.util.function.Consumer;

public class IsUserExistUseCase {

    private final SignRepository repo;

    public IsUserExistUseCase(SignRepository repo) {
        this.repo = repo;
    }

    public void execute(@NonNull String username, Consumer<Status<Boolean>> callback) {
        repo.isUserExist(username, status -> {
            boolean isAvailable = status.getStatusCode() == 200 || status.getStatusCode() == 404;
            callback.accept(
                    new Status<>(
                            status.getStatusCode(),
                            isAvailable ? status.getStatusCode() == 200 : null,
                            status.getError()
                    )
            );
        });
    }
}
