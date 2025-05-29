package com.example.spacex.data.network;

import com.example.spacex.data.source.ArticleApi;
import com.example.spacex.data.source.CommentApi;
import com.example.spacex.data.source.CredentialsDataSource;
import com.example.spacex.data.source.EventApi;
import com.example.spacex.data.source.FavouritesApi;
import com.example.spacex.data.source.LaunchApi;
import com.example.spacex.data.source.ReactionApi;
import com.example.spacex.data.source.UserApi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {
    private static RetrofitFactory INSTANCE;

    private RetrofitFactory() {
    }

    public static synchronized RetrofitFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RetrofitFactory();
        }
        return INSTANCE;
    }

    private final OkHttpClient.Builder client = new OkHttpClient.Builder()
            .addInterceptor(chain -> {
                        String authData = CredentialsDataSource.getInstance().getAuthData();
                        if (authData == null) {
                            return chain.proceed(chain.request());
                        } else {
                            Request request = chain.request()
                                    .newBuilder()
                                    .addHeader("Authorization", authData)
                                    .build();
                            return chain.proceed(request);
                        }

                    }
            );

    private final Retrofit retrofitApi = new Retrofit.Builder()
            .baseUrl("https://api.spacexdata.com/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private final Retrofit retrofitUser = new Retrofit.Builder()
            .baseUrl("http://192.168.0.49:8080/")
            .client(client.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public EventApi getEventApi() {
        return retrofitApi.create(EventApi.class);
    }

    public UserApi getUserApi() {
        return retrofitUser.create(UserApi.class);
    }

    public LaunchApi getLaunchApi() {
        return retrofitApi.create(LaunchApi.class);
    }

    public ArticleApi getArticleApi() {
        return retrofitUser.create(ArticleApi.class);
    }

    public CommentApi getCommentApi() {
        return retrofitUser.create(CommentApi.class);
    }

    public FavouritesApi getFavouritesApi() {
        return retrofitUser.create(FavouritesApi.class);
    }

    public ReactionApi getReactionApi() {
        return retrofitUser.create(ReactionApi.class);
    }

}
