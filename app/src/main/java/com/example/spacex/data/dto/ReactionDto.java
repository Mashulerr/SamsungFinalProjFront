package com.example.spacex.data.dto;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class ReactionDto {

    @Nullable
    @SerializedName("id")
    public String id;

    @Nullable
    @SerializedName("articleId")
    public String articleId;

    @Nullable
    @SerializedName("type")
    public String type;

    @Nullable
    @SerializedName("userId")
    public String userId;

}
