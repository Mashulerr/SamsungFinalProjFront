package com.example.spacex.data.repository;


import androidx.annotation.NonNull;

import com.example.spacex.data.dto.ReactionInitDto;
import com.example.spacex.data.network.RetrofitFactory;
import com.example.spacex.data.source.ReactionApi;
import com.example.spacex.data.utils.CallToConsumer;
import com.example.spacex.data.utils.mapper.ReactionMapper;
import com.example.spacex.domain.entity.ReactionEntity;
import com.example.spacex.domain.entity.Status;
import com.example.spacex.domain.reaction.ReactionRepository;

import java.util.function.Consumer;


public class ReactionRepositoryImpl implements ReactionRepository {

    private final ReactionApi reactionApi = RetrofitFactory.getInstance().getReactionApi();

    private static ReactionRepositoryImpl INSTANCE;

    private ReactionRepositoryImpl() {
    }

    public static synchronized ReactionRepositoryImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ReactionRepositoryImpl();
        }
        return INSTANCE;
    }


    @Override
    public void addReaction(
            @NonNull String articleId,
            @NonNull String userId,
            @NonNull String type,
            Consumer<Status<Void>> callback
    ) {
        reactionApi.addReaction(new ReactionInitDto(articleId, userId, type)).enqueue(new CallToConsumer<>(
                callback,
                dto -> null
        ));
    }

    @Override
    public void deleteReaction(@NonNull String id, Consumer<Status<Void>> callback) {
        reactionApi.removeReaction(id).enqueue(new CallToConsumer<>(
                callback,
                dto -> null
        ));
    }

    @Override
    public void getReaction(
            @NonNull String userId,
            @NonNull String articleId,
            Consumer<Status<ReactionEntity>> callback
    ) {
        reactionApi.getReactionById(userId, articleId).enqueue(new CallToConsumer<>(
                callback,
                dto -> {
                    if (dto == null) {
                        return null;
                    }
                    return ReactionMapper.toReactionEntity(dto);
                }
        ));
    }

}
