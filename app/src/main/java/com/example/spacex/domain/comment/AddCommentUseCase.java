package com.example.spacex.domain.comment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.spacex.data.repository.CommentRepositoryImpl;
import com.example.spacex.domain.entity.Status;

import java.util.function.Consumer;

public class AddCommentUseCase {

    private final CommentRepositoryImpl repo;

    public AddCommentUseCase(CommentRepositoryImpl repo) {
        this.repo = repo;
    }

    public void execute(
            @NonNull String articleId,
            @NonNull String username,
            @Nullable String photoUrl,
            @NonNull String content,
            @NonNull String userId,
            Consumer<Status<Void>> callback){
        repo.addComment(articleId, username, photoUrl, content, userId, callback);

    }
}
