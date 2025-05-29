package com.example.spacex.data.dto;

public class CommentInitDto {

    public  String articleId;

    public  String username;

    public  String photoUrl;

    public  String content;

    public  String userId;


    public CommentInitDto(
            String articleId,
            String username,
            String photoUrl,
            String content,
            String userId
    ) {
        this.articleId = articleId;
        this.username = username;
        this.photoUrl = photoUrl;
        this.content = content;
        this.userId = userId;
    }

    public String getArticleId() {
        return articleId;
    }

    public String getUsername() {
        return username;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getContent() {
        return content;
    }

    public String getUserId() {
        return userId;
    }
}
