package com.example.spacex.domain.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ItemLaunchEntity {

    @NonNull
    private final String flightNumber;

    @NonNull
    private final String missionName;

    @NonNull
    private final String launchYear;

    @NonNull
    private final String rocketName;

    @Nullable
    private final String missionPatch;

    @NonNull
    private final Boolean isSuccess;

    public ItemLaunchEntity(@NonNull String flightNumber,
                            @NonNull String missionName,
                            @NonNull String launchYear,
                            @NonNull String rocketName,
                            @Nullable String missionPatch,
                            @NonNull Boolean isSuccess
    ) {
        this.flightNumber = flightNumber;
        this.missionName = missionName;
        this.launchYear = launchYear;
        this.rocketName = rocketName;
        this.missionPatch = missionPatch;
        this.isSuccess = isSuccess;
    }

    @NonNull
    public String getFlightNumber() {
        return flightNumber;
    }

    @NonNull
    public String getMissionName() {
        return missionName;
    }

    @NonNull
    public String getLaunchYear() {
        return launchYear;
    }

    @NonNull
    public String getRocketName() {
        return rocketName;
    }

    @Nullable
    public String getMissionPatch() {
        return missionPatch;
    }

    @NonNull
    public Boolean isSuccess() {
        return isSuccess;
    }
}


