package com.example.spacex.domain.launch;


import androidx.annotation.NonNull;

import com.example.spacex.domain.entity.FullLaunchEntity;
import com.example.spacex.domain.entity.ItemLaunchEntity;
import com.example.spacex.domain.entity.Status;

import java.util.List;
import java.util.function.Consumer;

public interface LaunchRepository {

    void getAllLaunches(Consumer<Status<List<ItemLaunchEntity>>> callback);

    void getLaunch(@NonNull String flightNumber, Consumer<Status<FullLaunchEntity>> callback);
}
