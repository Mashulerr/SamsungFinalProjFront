package com.example.spacex.domain.entity;

import androidx.annotation.NonNull;

public class ReactionEntity {

    @NonNull
    private final String id;

    @NonNull
    private final String articleId;

    @NonNull
    private final String userId;

    @NonNull
    private final ReactionType type;

    public ReactionEntity(
            @NonNull String id,
            @NonNull String articleId,
            @NonNull String userId,
            @NonNull ReactionType type
    ) {
        this.id = id;
        this.articleId = articleId;
        this.userId = userId;
        this.type = type;
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
    public String getUserId() {
        return userId;
    }

    @NonNull
    public ReactionType getType() {
        return type;
    }
}
