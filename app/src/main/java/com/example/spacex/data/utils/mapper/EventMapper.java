package com.example.spacex.data.utils.mapper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.spacex.data.dto.EventDto;
import com.example.spacex.domain.entity.FullEventEntity;
import com.example.spacex.domain.entity.ItemEventEntity;

import java.util.ArrayList;
import java.util.List;

public class EventMapper {

    @Nullable
    public static ItemEventEntity toItemEventEntity(@NonNull EventDto eventDto) {
        final String id = eventDto.id;
        final String title = eventDto.title;
        final String dateUtc = eventDto.eventDateUtc;
        if (id != null && title != null && dateUtc != null) {
            return new ItemEventEntity(id, title, dateUtc);
        }
        return null;
    }

    @NonNull
    public static List<ItemEventEntity> toItemEventEntityList(@NonNull List<EventDto> eventsDto) {
        List<ItemEventEntity> result = new ArrayList<>(eventsDto.size());
        for (EventDto event : eventsDto) {
            ItemEventEntity item = toItemEventEntity(event);
            if (item != null) {
                result.add(item);
            }
        }
        return result;
    }

    @Nullable
    public static FullEventEntity toFullEventEntity(@NonNull EventDto eventDto) {
        final String id = eventDto.id;
        final String title = eventDto.title;
        final String eventDateUtc = eventDto.eventDateUtc;
        final String eventDetails = eventDto.eventDetails;
        final String articleLink = eventDto.links != null ? eventDto.links.article : null;
        final String wikipediaLink = eventDto.links != null ? eventDto.links.wikipedia : null;
        final String redditLink = eventDto.links != null ? eventDto.links.reddit : null;

        if (id != null && title != null && eventDateUtc != null && eventDetails != null) {
            return new FullEventEntity(
                    id,
                    title,
                    eventDateUtc,
                    eventDetails,
                    articleLink,
                    wikipediaLink,
                    redditLink
            );
        }
        return null;
    }
}
