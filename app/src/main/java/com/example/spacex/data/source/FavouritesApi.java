package com.example.spacex.data.source;

import com.example.spacex.data.dto.ArticleDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FavouritesApi {

    @GET("/api/favorites")
    Call<List<ArticleDto>> getFavouritesList();

    @DELETE("/api/favorites/{articleId}")
    Call<Void> removeFromFavourites(@Path("articleId") String articleId);

    @POST("/api/favorites/{articleId}")
    Call<Void> addToFavourites(@Path("articleId") String articleId);
}
