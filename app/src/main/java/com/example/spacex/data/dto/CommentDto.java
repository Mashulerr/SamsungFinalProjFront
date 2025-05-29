package com.example.spacex.data.dto;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class CommentDto {

    @Nullable
    @SerializedName("id")
    public String id;

    @Nullable
    @SerializedName("articleId")
    public  String articleId;

    @Nullable
    @SerializedName("username")
    public  String username;

    @Nullable
    @SerializedName("photoUrl")
    public  String photoUrl;

    @Nullable
    @SerializedName("content")
    public  String content;

    @Nullable
    @SerializedName("user_id")
    public  String userId;

    public CommentDto(
            @Nullable String id,
            @Nullable String articleId,
            @Nullable String username,
            @Nullable String photoUrl,
            @Nullable String content,
            @Nullable String userId
    ) {
        this.id = id;
        this.articleId = articleId;
        this.username = username;
        this.photoUrl = photoUrl;
        this.content = content;
        this.userId = userId;
    }
}
