package com.example.spacex.domain.launch;

import com.example.spacex.domain.entity.ItemLaunchEntity;
import com.example.spacex.domain.entity.Status;

import java.util.List;
import java.util.function.Consumer;

public class GetLaunchListUseCase {

    private final LaunchRepository repo;

    public GetLaunchListUseCase(LaunchRepository repo) {
        this.repo = repo;
    }

    public void execute(Consumer<Status<List<ItemLaunchEntity>>> callback) {
        repo.getAllLaunches(callback);
    }
}
