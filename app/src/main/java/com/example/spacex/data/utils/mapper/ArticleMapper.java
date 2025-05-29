package com.example.spacex.data.utils.mapper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.spacex.data.dto.ArticleDto;
import com.example.spacex.domain.entity.CommentEntity;
import com.example.spacex.domain.entity.FullArticleEntity;
import com.example.spacex.domain.entity.ItemArticleEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArticleMapper {

    @Nullable
    public static ItemArticleEntity toItemArticleEntity(@NonNull ArticleDto articleDto) {
        final String id = articleDto.getId();
        final String title = articleDto.getTitle();
        final String username = articleDto.getUsername();
        final String photoUrl = articleDto.getPhotoUrl();
        final Integer likes = articleDto.getLikes();
        final Integer dislikes = articleDto.getDislikes();
        final boolean favourite = articleDto.isFavourite();

        if (id != null && title != null && username != null && likes != null && dislikes != null) {
            return new ItemArticleEntity(id, title, username, photoUrl, likes, dislikes, favourite);
        }
        return null;
    }

    @NonNull
    public static List<ItemArticleEntity> toItemArticleEntityList(@Nullable List<ArticleDto> articlesDto) {
        if (articlesDto == null) return Collections.emptyList();

        List<ItemArticleEntity> result = new ArrayList<>(articlesDto.size());
        for (ArticleDto article : articlesDto) {
            ItemArticleEntity item = toItemArticleEntity(article);
            if (item != null) {
                result.add(item);
            }
        }
        return result;
    }


    @Nullable
    public static FullArticleEntity toFullArticleEntity(@NonNull ArticleDto articleDto) {
        final String id = articleDto.getId();
        final String title = articleDto.getTitle();
        final String content = articleDto.getContent();
        final String username = articleDto.getUsername();
        final String photoUrl = articleDto.getPhotoUrl();
        final Integer likes = articleDto.getLikes();
        final Integer dislikes = articleDto.getDislikes();
        final List<CommentEntity> comments = articleDto.getComments() != null
                ? CommentMapper.toCommentEntityList(articleDto.getComments()) : null;
        final boolean favourite = articleDto.isFavourite();

        if (id != null && title != null && content != null && username != null && likes != null && dislikes != null) {
            return new FullArticleEntity(id, title, content, username, photoUrl, likes, dislikes, comments, favourite);
        }
        return null;
    }
}
