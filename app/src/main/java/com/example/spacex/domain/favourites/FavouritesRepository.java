package com.example.spacex.domain.favourites;

import androidx.annotation.NonNull;

import com.example.spacex.domain.entity.ItemArticleEntity;
import com.example.spacex.domain.entity.Status;

import java.util.List;
import java.util.function.Consumer;

public interface FavouritesRepository {

    void getFavouritesList(Consumer<Status<List<ItemArticleEntity>>> callback);

    void addToFavourites(@NonNull String articleId, Consumer<Status<Void>> callback);

    void removeFromFavourites(@NonNull String articleId, Consumer<Status<Void>> callback);
}
