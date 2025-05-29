package com.example.spacex.data.repository;

import androidx.annotation.NonNull;

import com.example.spacex.data.network.RetrofitFactory;
import com.example.spacex.data.source.LaunchApi;
import com.example.spacex.data.utils.CallToConsumer;
import com.example.spacex.data.utils.mapper.LaunchMapper;
import com.example.spacex.domain.entity.FullLaunchEntity;
import com.example.spacex.domain.entity.ItemLaunchEntity;
import com.example.spacex.domain.entity.Status;
import com.example.spacex.domain.launch.LaunchRepository;

import java.util.List;
import java.util.function.Consumer;

public class LaunchRepositoryImpl implements LaunchRepository {

    private static volatile LaunchRepositoryImpl INSTANCE;

    private final LaunchApi launchApi = RetrofitFactory.getInstance().getLaunchApi();

    private LaunchRepositoryImpl() {
    }

    public static LaunchRepositoryImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (LaunchRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LaunchRepositoryImpl();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAllLaunches(@NonNull Consumer<Status<List<ItemLaunchEntity>>> callback) {
        launchApi.getAllLaunches().enqueue(new CallToConsumer<>(
                callback,
                LaunchMapper::toItemLaunchEntityList
        ));
    }

    @Override
    public void getLaunch(@NonNull String flightNumber, @NonNull Consumer<Status<FullLaunchEntity>> callback) {
        launchApi.getLaunchByFlightNumber(flightNumber).enqueue(new CallToConsumer<>(
                callback,
                LaunchMapper::toFullLaunchEntity
        ));
    }
}
