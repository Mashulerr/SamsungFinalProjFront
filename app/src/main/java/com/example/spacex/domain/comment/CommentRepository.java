package com.example.spacex.domain.comment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.spacex.domain.entity.CommentEntity;
import com.example.spacex.domain.entity.Status;

import java.util.List;
import java.util.function.Consumer;

public interface CommentRepository {

    void getAllComments(@NonNull String articleId, Consumer<Status<List<CommentEntity>>> callback);

    void addComment(@NonNull String articleId,
                    @NonNull String username,
                    @Nullable String photoUrl,
                    @NonNull String content,
                    @NonNull String userId,
                    Consumer<Status<Void>> callback);
}
