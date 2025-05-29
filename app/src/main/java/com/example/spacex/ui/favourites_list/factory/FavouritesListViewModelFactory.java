package com.example.spacex.ui.favourites_list.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.spacex.ui.favourites_list.FavouritesListViewModel;

public class FavouritesListViewModelFactory implements ViewModelProvider.Factory{
    private final String userId;
    public FavouritesListViewModelFactory(String userId) {
        this.userId = userId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FavouritesListViewModel(userId);
    }
}
