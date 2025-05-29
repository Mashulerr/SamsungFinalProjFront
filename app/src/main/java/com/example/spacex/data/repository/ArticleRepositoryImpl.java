package com.example.spacex.data.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.spacex.data.dto.ArticleDto;
import com.example.spacex.data.dto.ArticleInitDto;
import com.example.spacex.data.dto.CommentDto;
import com.example.spacex.data.network.RetrofitFactory;
import com.example.spacex.data.source.ArticleApi;
import com.example.spacex.data.utils.CallToConsumer;
import com.example.spacex.data.utils.mapper.ArticleMapper;
import com.example.spacex.data.utils.mapper.CommentMapper;
import com.example.spacex.domain.article.ArticleRepository;
import com.example.spacex.domain.entity.CommentEntity;
import com.example.spacex.domain.entity.FullArticleEntity;
import com.example.spacex.domain.entity.ItemArticleEntity;
import com.example.spacex.domain.entity.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class ArticleRepositoryImpl implements ArticleRepository {

    private final ArticleApi articleApi = RetrofitFactory.getInstance().getArticleApi();

    private static ArticleRepositoryImpl INSTANCE;

    private ArticleRepositoryImpl() {
    }

    public static synchronized ArticleRepositoryImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ArticleRepositoryImpl();
        }
        return INSTANCE;
    }

    @Override
    public void getAllArticles(Consumer<Status<List<ItemArticleEntity>>> callback) {
        articleApi.getAllArticles().enqueue(new CallToConsumer<>(
                callback,
                ArticleMapper::toItemArticleEntityList
        ));
    }

    @Override
    public void createArticle(
            @NonNull String title,
            @NonNull String content,
            @NonNull String userNickname,
            @Nullable String userAvatar,
            @NonNull Integer likes,
            @NonNull Integer dislikes,
            @Nullable ArrayList<CommentEntity> comments,
            boolean favourite,
            Consumer<Status<Void>> callback
    ) {
        articleApi.createArticle(new ArticleInitDto(
                title,
                content,
                userNickname,
                userAvatar,
                0,
                0,
                new ArrayList<>(),
                false
        )).enqueue(new CallToConsumer<>(
                        callback,
                        dto -> null
                )
        );

    }

    @Override
    public void deleteArticle(@NonNull String id, Consumer<Status<Void>> callback) {
        articleApi.deleteArticle(id).enqueue(new CallToConsumer<>(
                callback,
                dto -> null
        ));
    }

    @Override
    public void update(
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
        ArrayList<CommentDto> commentsDto = null;
        if (comments != null) {
            commentsDto = new ArrayList<>(CommentMapper.toCommentDtoList(comments));
        }

        articleApi.update(
                id, new ArticleDto(id, title, content, username, photoUrl, likes, dislikes, commentsDto, favourite)
        ).enqueue(new CallToConsumer<>(callback, ArticleMapper::toFullArticleEntity));
    }

    @Override
    public void getArticle(@NonNull String id, Consumer<Status<FullArticleEntity>> callback) {
        articleApi.getArticleById(id).enqueue(new CallToConsumer<>(
                callback,
                ArticleMapper::toFullArticleEntity
        ));
    }

}
