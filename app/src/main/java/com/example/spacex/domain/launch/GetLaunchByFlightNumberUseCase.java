package com.example.spacex.domain.launch;

import androidx.annotation.NonNull;

import com.example.spacex.domain.entity.FullLaunchEntity;
import com.example.spacex.domain.entity.Status;

import java.util.function.Consumer;

public class GetLaunchByFlightNumberUseCase {

    private final LaunchRepository repo;

    public GetLaunchByFlightNumberUseCase(LaunchRepository repo) {
        this.repo = repo;
    }

    public void execute(@NonNull String flightNumber, Consumer<Status<FullLaunchEntity>> callback) {
        repo.getLaunch(flightNumber, callback);
    }
}
