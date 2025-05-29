package com.example.spacex.domain.reaction;

import androidx.annotation.NonNull;

import com.example.spacex.domain.entity.ReactionEntity;
import com.example.spacex.domain.entity.Status;

import java.util.function.Consumer;

public interface ReactionRepository {
    void addReaction(
            @NonNull String articleId,
            @NonNull String userId,
            @NonNull String type,
            Consumer<Status<Void>> callback
    );

    void deleteReaction(@NonNull String id, Consumer<Status<Void>> callback);

    void getReaction(@NonNull String userId,
                     @NonNull String articleId,
                     Consumer<Status<ReactionEntity>> callback
    );
}
