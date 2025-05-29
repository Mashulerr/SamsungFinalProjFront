package com.example.spacex.domain.article;

import androidx.annotation.NonNull;

import com.example.spacex.domain.entity.ItemArticleEntity;
import com.example.spacex.domain.entity.Status;

import java.util.List;
import java.util.function.Consumer;

public class GetArticleListUseCase {


    private final ArticleRepository repo;

    public GetArticleListUseCase(ArticleRepository repo) {
        this.repo = repo;
    }

    public void execute(Consumer<Status<List<ItemArticleEntity>>> callback) {
        repo.getAllArticles(callback);
    }
}
