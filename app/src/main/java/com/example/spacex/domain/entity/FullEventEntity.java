package com.example.spacex.domain.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FullEventEntity {
    @NonNull
    private final String id;

    @NonNull
    private final String title;

    @NonNull
    private final String eventDateUtc;

    @NonNull
    private final String eventDetails;

    @Nullable
    private final String articleLink;

    @Nullable
    private final String wikipediaLink;

    @Nullable
    private final String redditLink;

    public FullEventEntity(
            @NonNull String id,
            @NonNull String title,
            @NonNull String eventDateUtc,
            @NonNull String eventDetails,
            @Nullable String articleLink,
            @Nullable String wikipediaLink,
            @Nullable String redditLink
    ) {
        this.id = id;
        this.title = title;
        this.eventDateUtc = eventDateUtc;
        this.eventDetails = eventDetails;
        this.articleLink = articleLink;
        this.wikipediaLink = wikipediaLink;
        this.redditLink = redditLink;
    }


    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getEventDateUtc() {
        return eventDateUtc;
    }

    @NonNull
    public String getEventDetails() {
        return eventDetails;
    }

    @Nullable
    public String getRedditLink() {
        return redditLink;
    }

    @Nullable
    public String getArticleLink() {
        return articleLink;
    }

    @Nullable
    public String getWikipediaLink() {
        return wikipediaLink;
    }
}
