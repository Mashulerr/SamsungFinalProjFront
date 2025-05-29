package com.example.spacex.domain.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class FullArticleEntity {
    @NonNull
    private final String id;

    @NonNull
    private final String title;

    @NonNull
    private final String content;

    @NonNull
    private final String username;

    @Nullable
    private final String photoUrl;

    @NonNull
    private final Integer likes;

    @NonNull
    private final Integer dislikes;

    @Nullable
    private final List<CommentEntity> comments;

    private final boolean favourite;

    public FullArticleEntity(
            @NonNull String id,
            @NonNull String title,
            @NonNull String content,
            @NonNull String username,
            @Nullable String photoUrl,
            @NonNull Integer likes,
            @NonNull Integer dislikes,
            @Nullable List<CommentEntity> comments,
            boolean favourite
    ) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.username = username;
        this.photoUrl = photoUrl;
        this.likes = likes;
        this.dislikes = dislikes;
        this.comments = comments;
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
    public String getContent() {
        return content;
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

    @Nullable
    public List<CommentEntity> getComments() {
        return comments;
    }

    public boolean isFavourite() {
        return favourite;
    }
}
