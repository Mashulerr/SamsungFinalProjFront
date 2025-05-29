package com.example.spacex.data.utils.mapper;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.spacex.data.dto.CommentDto;
import com.example.spacex.domain.entity.CommentEntity;

import java.util.ArrayList;
import java.util.List;

public class CommentMapper {

    @Nullable
    public static CommentEntity toCommentEntity(@NonNull CommentDto commentDto) {
        final String id = commentDto.id;
        final String articleId = commentDto.articleId;
        final String username = commentDto.username;
        final String photoUrl = commentDto.photoUrl;
        final String content = commentDto.content;
        final String userId = commentDto.userId != null ? commentDto.userId : "";


        if (id != null && articleId != null && username != null && content != null) {
            return new CommentEntity(id, articleId, username, photoUrl, content, userId);
        }
        return null;
    }


    @NonNull
    public static ArrayList<CommentEntity> toCommentEntityList(@NonNull List<CommentDto> commentsDto) {
        ArrayList<CommentEntity> result = new ArrayList<>();

        if (commentsDto.isEmpty()) {
            return result;
        }

        for (CommentDto commentDto : commentsDto) {
            CommentEntity commentEntity = toCommentEntity(commentDto);
            if (commentEntity != null) {
                result.add(commentEntity);
            }
        }
        return result;
    }

    @NonNull
    public static List<CommentDto> toCommentDtoList(@NonNull List<CommentEntity> commentsEntity) {
        List<CommentDto> result = new ArrayList<>(commentsEntity.size());
        for (CommentEntity commentEntity : commentsEntity) {
            result.add(toCommentDto(commentEntity));
        }
        return result;
    }

    @NonNull
    public static CommentDto toCommentDto(@NonNull CommentEntity commentEntity) {
        return new CommentDto(
                commentEntity.getId(),
                commentEntity.getArticleId(),
                commentEntity.getUsername(),
                commentEntity.getPhotoUrl(),
                commentEntity.getContent(),
                commentEntity.getUserId()
        );
    }
}
