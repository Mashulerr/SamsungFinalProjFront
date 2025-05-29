package com.example.spacex.domain.article;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.spacex.domain.entity.CommentEntity;
import com.example.spacex.domain.entity.Status;

import java.util.ArrayList;
import java.util.function.Consumer;

public class CreateArticleUseCase {
    private final ArticleRepository repo;

    public CreateArticleUseCase(ArticleRepository repo) {this.repo = repo;}

    public void execute(
            @NonNull String title,
            @NonNull String content,
            @NonNull String username,
            @Nullable String photoUrl,
            @NonNull Integer likes,
            @NonNull Integer dislikes,
            @Nullable ArrayList<CommentEntity> comments,
            boolean favourite,
            Consumer<Status<Void>> callback
    ) {
        repo.createArticle(title, content, username, photoUrl, likes, dislikes, comments, favourite, callback);
    }
}
