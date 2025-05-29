package com.example.spacex.domain.event;

import androidx.annotation.NonNull;

import com.example.spacex.domain.entity.FullEventEntity;
import com.example.spacex.domain.entity.Status;

import java.util.function.Consumer;

public class GetEventByIdUseCase {
    private final EventRepository repo;

    public GetEventByIdUseCase(EventRepository repo) {
        this.repo = repo;
    }

    public void execute(@NonNull String id, Consumer<Status<FullEventEntity>> callback) {
        repo.getEvent(id, callback);
    }
}
