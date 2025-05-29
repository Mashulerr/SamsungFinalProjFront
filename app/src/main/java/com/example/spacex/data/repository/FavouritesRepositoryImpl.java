package com.example.spacex.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.spacex.data.dto.ArticleDto;
import com.example.spacex.data.network.RetrofitFactory;
import com.example.spacex.data.source.FavouritesApi;
import com.example.spacex.data.utils.CallToConsumer;
import com.example.spacex.data.utils.mapper.ArticleMapper;
import com.example.spacex.domain.entity.ItemArticleEntity;
import com.example.spacex.domain.entity.Status;
import com.example.spacex.domain.favourites.FavouritesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouritesRepositoryImpl implements FavouritesRepository {

    private final FavouritesApi favouritesApi = RetrofitFactory.getInstance().getFavouritesApi();

    private static FavouritesRepositoryImpl INSTANCE;

    private FavouritesRepositoryImpl() {
    }

    public static synchronized FavouritesRepositoryImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FavouritesRepositoryImpl();
        }
        return INSTANCE;
    }


    @Override
    public void getFavouritesList(Consumer<Status<List<ItemArticleEntity>>> callback) {
        favouritesApi.getFavouritesList().enqueue(new CallToConsumer<>(
                callback,
                ArticleMapper::toItemArticleEntityList
        ));
    }

    @Override
    public void addToFavourites(@NonNull String articleId, Consumer<Status<Void>> callback) {
        favouritesApi.addToFavourites(articleId).enqueue(new CallToConsumer<>(
                callback,
                dto -> null
        ));

    }

    @Override
    public void removeFromFavourites(@NonNull String articleId, Consumer<Status<Void>> callback) {
        favouritesApi.removeFromFavourites(articleId).enqueue(new CallToConsumer<>(
                callback,
                dto -> null
        ));
    }
}
