package com.example.spacex.domain.comment;

import androidx.annotation.NonNull;

import com.example.spacex.domain.entity.CommentEntity;
import com.example.spacex.domain.entity.Status;

import java.util.List;
import java.util.function.Consumer;

public class GetCommentListUseCase {

    private final CommentRepository repo;

    public GetCommentListUseCase(CommentRepository repo) {
        this.repo = repo;
    }

    public void execute(@NonNull String articleId, Consumer<Status<List<CommentEntity>>> callback) {
        repo.getAllComments(articleId, callback);
    }
}
