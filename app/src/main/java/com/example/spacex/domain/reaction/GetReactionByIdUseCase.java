package com.example.spacex.domain.reaction;

import androidx.annotation.NonNull;

import com.example.spacex.data.repository.ReactionRepositoryImpl;
import com.example.spacex.domain.entity.ReactionEntity;
import com.example.spacex.domain.entity.Status;

import java.util.function.Consumer;

public class GetReactionByIdUseCase {

    private final ReactionRepositoryImpl repo;

    public GetReactionByIdUseCase(ReactionRepositoryImpl repo) {
        this.repo = repo;
    }

    public void execute(
            @NonNull String userId,
            @NonNull String articleId,
            Consumer<Status<ReactionEntity>> callback
    ){
        repo.getReaction(userId, articleId, callback);
    }
}
