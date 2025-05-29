package com.example.spacex.domain.article;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.spacex.domain.entity.CommentEntity;
import com.example.spacex.domain.entity.FullArticleEntity;
import com.example.spacex.domain.entity.ItemArticleEntity;
import com.example.spacex.domain.entity.Status;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public interface ArticleRepository {

    void getAllArticles(Consumer<Status<List<ItemArticleEntity>>> callback);

    void createArticle(
            @NonNull String title,
            @NonNull String content,
            @NonNull String userNickname,
            @Nullable String userAvatar,
            @NonNull Integer likes,
            @NonNull Integer dislikes,
            @Nullable ArrayList<CommentEntity> comments,
            boolean favourite,
            Consumer<Status<Void>> callback
    );

    void deleteArticle(@NonNull String id, Consumer<Status<Void>> callback);

    void update(
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
    );

    void getArticle(@NonNull String id, Consumer<Status<FullArticleEntity>> callback);
}
