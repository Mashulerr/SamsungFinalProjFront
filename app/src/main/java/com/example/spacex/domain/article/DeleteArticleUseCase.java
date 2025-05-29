package com.example.spacex.domain.article;

import androidx.annotation.NonNull;

import com.example.spacex.domain.entity.Status;

import java.util.function.Consumer;

public class DeleteArticleUseCase {
    private final ArticleRepository repo;

    public DeleteArticleUseCase(ArticleRepository repo) {
        this.repo = repo;
    }

    public void execute(@NonNull String id, Consumer<Status<Void>> callback) {
        repo.deleteArticle(id, callback);
    }
}
