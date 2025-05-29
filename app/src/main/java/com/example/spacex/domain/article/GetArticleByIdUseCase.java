package com.example.spacex.domain.article;

import androidx.annotation.NonNull;

import com.example.spacex.domain.entity.FullArticleEntity;
import com.example.spacex.domain.entity.Status;

import java.util.function.Consumer;

public class GetArticleByIdUseCase {

    private final ArticleRepository repo;

    public GetArticleByIdUseCase(ArticleRepository repo) {
        this.repo = repo;
    }

    public void execute(@NonNull String id, Consumer<Status<FullArticleEntity>> callback) {
        repo.getArticle(id, callback);
    }
}
