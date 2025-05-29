package com.example.spacex.domain.entity;

import androidx.annotation.Nullable;

public class Status<T> {
    private final int statusCode;

    @Nullable
    private final T value;

    @Nullable
    private final Throwable error;

    public Status(int statusCode, @Nullable T value, @Nullable Throwable error) {
        this.statusCode = statusCode;
        this.value = value;
        this.error = error;

    }

    public int getStatusCode() {
        return statusCode;
    }

    @Nullable
    public T getValue() {
        return value;
    }

    @Nullable
    public Throwable getError() {
        return error;
    }


    public boolean isSuccess() {
        return statusCode == 200;
    }
}
