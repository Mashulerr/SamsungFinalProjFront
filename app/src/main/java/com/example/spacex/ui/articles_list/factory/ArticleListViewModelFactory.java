package com.example.spacex.ui.articles_list.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.spacex.ui.articles_list.ArticleListViewModel;

public class ArticleListViewModelFactory implements ViewModelProvider.Factory {
    private final String userId;

    public ArticleListViewModelFactory(String userId) {
        this.userId = userId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ArticleListViewModel(userId);
    }
}

