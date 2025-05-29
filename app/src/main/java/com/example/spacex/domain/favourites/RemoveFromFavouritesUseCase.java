package com.example.spacex.domain.favourites;

import androidx.annotation.NonNull;

import com.example.spacex.data.repository.FavouritesRepositoryImpl;
import com.example.spacex.domain.entity.Status;

import java.util.function.Consumer;

public class RemoveFromFavouritesUseCase {

    private final FavouritesRepositoryImpl repo;

    public RemoveFromFavouritesUseCase(FavouritesRepositoryImpl repo) {
        this.repo = repo;
    }

    public void execute(@NonNull String articleId, Consumer<Status<Void>> callback) {
        repo.removeFromFavourites(articleId, callback);
    }
}
