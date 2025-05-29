package com.example.spacex.data.dto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ArticleInitDto {

    @NonNull
    @SerializedName("title")
    public String title;

    @NonNull
    @SerializedName("content")
    public String content;


    @NonNull
    @SerializedName("username")
    public String username;

    @Nullable
    @SerializedName("photoUrl")
    public String photoUrl;

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

    public ArticleInitDto(
            @NonNull String title,
            @NonNull String content,
            @NonNull String username,
            @Nullable String photoUrl,
            @Nullable Integer likes,
            @Nullable Integer dislikes,
            @Nullable ArrayList<CommentDto> comments,
            boolean favourite
    ) {
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
