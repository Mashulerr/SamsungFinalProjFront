package com.example.spacex.data.dto;


import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ArticleDto {

    @Nullable
    @SerializedName("id")
    public String id;

    @Nullable
    @SerializedName("title")
    public String title;

    @Nullable
    @SerializedName("content")
    public String content;

    @Nullable
    @SerializedName("photoUrl")
    public String photoUrl;

    @Nullable
    @SerializedName("username")
    public String username;

    @Nullable
    @SerializedName("likes")
    public Integer likes;

    @Nullable
    @SerializedName("dislikes")
    public Integer dislikes;

    @Nullable
    @SerializedName("comments")
    public ArrayList<CommentDto> comments;

    @SerializedName("favorite")
    public boolean favourite;


    public ArticleDto(
            @Nullable String id,
            @Nullable String title,
            @Nullable String content,
            @Nullable String photoUrl,
            @Nullable String username,
            @Nullable Integer likes,
            @Nullable Integer dislikes,
            @Nullable ArrayList<CommentDto> comments,
            boolean favourite
    ) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.photoUrl = photoUrl;
        this.username = username;
        this.likes = likes;
        this.dislikes = dislikes;
        this.comments = comments;
        this.favourite = favourite;
    }

    @Nullable
    public String getId() {
        return id;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getContent() {
        return content;
    }

    @Nullable
    public String getPhotoUrl() {
        return photoUrl;
    }

    @Nullable
    public String getUsername() {
        return username;
    }

    @Nullable
    public Integer getLikes() {
        return likes;
    }

    @Nullable
    public Integer getDislikes() {
        return dislikes;
    }

    @Nullable
    public ArrayList<CommentDto> getComments() {
        return comments;
    }

    public boolean isFavourite() {
        return favourite;
    }
}

