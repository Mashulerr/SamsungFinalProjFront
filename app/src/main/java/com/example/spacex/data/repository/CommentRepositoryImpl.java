package com.example.spacex.data.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.spacex.data.dto.CommentInitDto;
import com.example.spacex.data.network.RetrofitFactory;
import com.example.spacex.data.source.CommentApi;
import com.example.spacex.data.utils.CallToConsumer;
import com.example.spacex.data.utils.mapper.CommentMapper;
import com.example.spacex.domain.comment.CommentRepository;
import com.example.spacex.domain.entity.CommentEntity;
import com.example.spacex.domain.entity.Status;

import java.util.List;
import java.util.function.Consumer;

public class CommentRepositoryImpl implements CommentRepository {

    private static volatile CommentRepositoryImpl INSTANCE;

    private final CommentApi commentApi = RetrofitFactory.getInstance().getCommentApi();

    private CommentRepositoryImpl() {
    }

    public static synchronized CommentRepositoryImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CommentRepositoryImpl();
        }
        return INSTANCE;
    }


    @Override
    public void getAllComments(@NonNull String articleId, Consumer<Status<List<CommentEntity>>> callback) {
        commentApi.getAllComments(articleId).enqueue(new CallToConsumer<>(
                callback,
                CommentMapper::toCommentEntityList
        ));
    }

    @Override
    public void addComment(
            @NonNull String articleId,
            @NonNull String username,
            @Nullable String photoUrl,
            @NonNull String content,
            @NonNull String userId,
            Consumer<Status<Void>> callback
    ) {
        commentApi.addComment(new CommentInitDto(
                articleId,
                username,
                photoUrl,
                content,
                userId
        )).enqueue(new CallToConsumer<>(
                callback,
                dto -> null
        ));
    }
}
