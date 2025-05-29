package com.example.spacex.domain.reaction;

import androidx.annotation.NonNull;

import com.example.spacex.data.repository.ReactionRepositoryImpl;
import com.example.spacex.domain.entity.Status;

import java.util.function.Consumer;

public class AddReactionUseCase {

    private final ReactionRepositoryImpl repo;

    public AddReactionUseCase(ReactionRepositoryImpl repo) {
        this.repo = repo;
    }

    public void execute(
            @NonNull String articleId,
            @NonNull String userId,
            @NonNull String type,
            Consumer<Status<Void>> callback
    ) {
        repo.addReaction(articleId, userId, type, callback);
    }
}
