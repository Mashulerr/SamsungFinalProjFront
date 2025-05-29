package com.example.spacex.domain.favourites;

import com.example.spacex.data.repository.FavouritesRepositoryImpl;
import com.example.spacex.domain.entity.ItemArticleEntity;
import com.example.spacex.domain.entity.Status;

import java.util.List;
import java.util.function.Consumer;

public class GetFavouritesListUseCase {

    private final FavouritesRepositoryImpl repo;

    public GetFavouritesListUseCase(FavouritesRepositoryImpl repo) {
        this.repo = repo;
    }

    public void execute(Consumer<Status<List<ItemArticleEntity>>> callback){
        repo.getFavouritesList(callback);
    }
}
