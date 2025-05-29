package com.example.spacex.ui.article_and_comments.comments_list.factory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.spacex.ui.article_and_comments.comments_list.CommentListViewModel;

public class CommentListViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final String articleId;

    public CommentListViewModelFactory(Application application, String articleId) {
        this.application = application;
        this.articleId = articleId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CommentListViewModel.class)) {
            return (T) new CommentListViewModel(application, articleId);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
