package com.example.spacex.domain.event;

import com.example.spacex.domain.entity.ItemEventEntity;
import com.example.spacex.domain.entity.Status;

import java.util.List;
import java.util.function.Consumer;

public class GetEventListUseCase {
    private final EventRepository repo;

    public GetEventListUseCase(EventRepository repo) {
        this.repo = repo;
    }

    public void execute(Consumer<Status<List<ItemEventEntity>>> callback){
        repo.getAllEvents(callback);
    }
}
