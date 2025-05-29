package com.example.spacex.domain.reaction;

import androidx.annotation.NonNull;

import com.example.spacex.data.repository.ReactionRepositoryImpl;
import com.example.spacex.domain.entity.Status;

import java.util.function.Consumer;

public class DeleteReactionUseCase {

    private final ReactionRepositoryImpl repo;

    public DeleteReactionUseCase(ReactionRepositoryImpl repo) {
        this.repo = repo;
    }

    public void execute(@NonNull String id, Consumer<Status<Void>> callback){
        repo.deleteReaction(id, callback);
    }
}
