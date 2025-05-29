package com.example.spacex.data.dto;

public class ReactionInitDto {

    public String articleId;

    public String userId;

    public String type;

    public ReactionInitDto(String articleId, String userId, String type) {
        this.articleId = articleId;
        this.userId = userId;
        this.type = type;
    }

    public String getArticleId() {
        return articleId;
    }

    public String getUserId() {
        return userId;
    }

    public String getType() {
        return type;
    }
}
