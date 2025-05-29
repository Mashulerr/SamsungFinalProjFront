package com.example.spacex.domain.article;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.spacex.domain.entity.CommentEntity;
import com.example.spacex.domain.entity.FullArticleEntity;
import com.example.spacex.domain.entity.Status;

import java.util.ArrayList;
import java.util.function.Consumer;

public class UpdateArticleUseCase {

    private final ArticleRepository repo;

    public UpdateArticleUseCase(ArticleRepository repo) {
        this.repo = repo;
    }

    public void execute(
            @NonNull String id,
            @NonNull String title,
            @NonNull String content,
            @NonNull String username,
            @Nullable String photoUrl,
            @NonNull Integer likes,
            @NonNull Integer dislikes,
            @Nullable ArrayList<CommentEntity> comments,
            boolean favourite,
            Consumer<Status<FullArticleEntity>> callback
    ) {
        repo.update(id, title, content, username, photoUrl, likes, dislikes, comments, favourite, callback);
    }
}
