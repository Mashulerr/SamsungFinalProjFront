package com.example.spacex.data.source;

import com.example.spacex.data.dto.ReactionDto;
import com.example.spacex.data.dto.ReactionInitDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReactionApi {
    @DELETE("/api/reaction/{id}")
    Call<Void> removeReaction(@Path("id") String id);

    @POST("/api/reaction")
    Call<Void> addReaction(@Body ReactionInitDto dto);

    @GET("/api/reaction/user/{userId}/article/{articleId}")
    Call<ReactionDto> getReactionById(@Path("userId") String userId, @Path("articleId") String articleId);
}
