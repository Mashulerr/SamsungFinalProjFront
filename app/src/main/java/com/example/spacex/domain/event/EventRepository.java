package com.example.spacex.domain.event;

import androidx.annotation.NonNull;

import com.example.spacex.domain.entity.FullEventEntity;
import com.example.spacex.domain.entity.ItemEventEntity;
import com.example.spacex.domain.entity.Status;

import java.util.List;
import java.util.function.Consumer;

public interface EventRepository {

    void getAllEvents(Consumer<Status<List<ItemEventEntity>>> callback);
    void getEvent(@NonNull String id, Consumer<Status<FullEventEntity>> callback);
}
