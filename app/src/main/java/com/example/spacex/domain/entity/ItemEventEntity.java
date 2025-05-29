package com.example.spacex.domain.entity;

import androidx.annotation.NonNull;

public class ItemEventEntity {

    @NonNull
    private final String id;

    @NonNull
    private final String title;

    @NonNull
    private final String dataUtc;

    public ItemEventEntity(
            @NonNull String id,
            @NonNull String title,
            @NonNull String dataUtc
    ) {
        this.id = id;
        this.title = title;
        this.dataUtc = dataUtc;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getDataUtc() {
        return dataUtc;
    }
}
