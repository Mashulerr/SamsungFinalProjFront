package com.example.spacex.data.utils.mapper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.spacex.data.dto.ReactionDto;
import com.example.spacex.domain.entity.ReactionEntity;
import com.example.spacex.domain.entity.ReactionType;

public class ReactionMapper {

    @Nullable
    public static ReactionEntity toReactionEntity(@NonNull ReactionDto reactionDto) {
        final String id = reactionDto.id;
        final String articleId = reactionDto.articleId;
        final String userId = reactionDto.userId != null ? reactionDto.userId : "";
        final ReactionType type = parseReactionType(reactionDto.type);

        if (id != null && articleId != null) {
            return new ReactionEntity(id, articleId, userId, type);
        }
        return null;
    }

    @NonNull
    private static ReactionType parseReactionType(@Nullable String rawType) {
        if (rawType == null) return ReactionType.none;
        try {
            return ReactionType.valueOf(rawType.toLowerCase());
        } catch (IllegalArgumentException e) {
            return ReactionType.none;
        }
    }
}
