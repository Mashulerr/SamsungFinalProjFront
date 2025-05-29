package com.example.spacex.domain.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CommentEntity {
    @NonNull
    private final String id;

    @NonNull
    private final String articleId;

    @NonNull
    private final String username;

    @Nullable
    private final String photoUrl;

    @NonNull
    private final String content;

    @NonNull
    private final String userId;

    public CommentEntity(
            @NonNull String id,
            @NonNull String articleId,
            @NonNull String username,
            @Nullable String photoUrl,
            @NonNull String content,
            @NonNull String userId
    ) {
        this.id = id;
        this.articleId = articleId;
        this.username = username;
        this.photoUrl = photoUrl;
        this.content = content;
        this.userId = userId;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getArticleId() {
        return articleId;
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
    public String getContent() {
        return content;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }
}
