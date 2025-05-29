package com.example.spacex.domain.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ItemArticleEntity {

    @NonNull
    private final String id;

    @NonNull
    private final String title;

    @NonNull
    private final String username;

    @Nullable
    private final String photoUrl;

    @NonNull
    private final Integer likes;

    @NonNull
    private final Integer dislikes;

    private final boolean favourite;

    public ItemArticleEntity(
            @NonNull String id,
            @NonNull String title,
            @NonNull String username,
            @Nullable String photoUrl,
            @NonNull Integer likes,
            @NonNull Integer dislikes,
            boolean favourite
    ) {
        this.id = id;
        this.title = title;
        this.username = username;
        this.photoUrl = photoUrl;
        this.likes = likes;
        this.dislikes = dislikes;
        this.favourite = favourite;
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
    public String getUsername() {
        return username;
    }

    @Nullable
    public String getPhotoUrl() {
        return photoUrl;
    }

    @NonNull
    public Integer getLikes() {
        return likes;
    }

    @NonNull
    public Integer getDislikes() {
        return dislikes;
    }

    public boolean isFavourite() {
        return favourite;
    }
}
