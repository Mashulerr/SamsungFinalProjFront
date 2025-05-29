package com.example.spacex.data.source;

import com.example.spacex.data.dto.ArticleDto;
import com.example.spacex.data.dto.ArticleInitDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;

import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ArticleApi {
    @GET("api/news")
    Call<List<ArticleDto>> getAllArticles();

    @POST("api/news")
    Call<ArticleDto> createArticle(@Body ArticleInitDto article);

    @GET("api/news/{id}")
    Call<ArticleDto> getArticleById(@Path("id") String id);

    @PUT("api/news/{id}")
    Call<ArticleDto> update(@Path("id") String id, @Body ArticleDto article);

    @DELETE("api/news/{id}")
    Call<Void> deleteArticle(@Path("id") String id);
}

