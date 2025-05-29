package com.example.spacex.data.repository;

import androidx.annotation.NonNull;

import com.example.spacex.data.network.RetrofitFactory;
import com.example.spacex.data.source.EventApi;
import com.example.spacex.data.utils.CallToConsumer;
import com.example.spacex.data.utils.mapper.EventMapper;
import com.example.spacex.domain.event.EventRepository;
import com.example.spacex.domain.entity.FullEventEntity;
import com.example.spacex.domain.entity.ItemEventEntity;
import com.example.spacex.domain.entity.Status;

import java.util.List;
import java.util.function.Consumer;

public class EventRepositoryImpl implements EventRepository {

    private EventApi eventApi = RetrofitFactory.getInstance().getEventApi();

    private static EventRepositoryImpl INSTANCE;

    private EventRepositoryImpl() {}

    public static synchronized EventRepositoryImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EventRepositoryImpl();
        }
        return INSTANCE;
    }

    @Override
    public void getAllEvents(Consumer<Status<List<ItemEventEntity>>> callback) {
        eventApi.getAllEvents().enqueue(new CallToConsumer<>(
                callback,
                EventMapper::toItemEventEntityList
        ));
    }

    @Override
    public void getEvent(@NonNull String id, Consumer<Status<FullEventEntity>> callback) {
        eventApi.getEventById(id).enqueue(new CallToConsumer<>(
                callback,
                EventMapper::toFullEventEntity
        ));
    }
}
