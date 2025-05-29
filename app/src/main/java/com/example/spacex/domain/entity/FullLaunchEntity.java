package com.example.spacex.domain.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FullLaunchEntity {

    @NonNull
    private final String flightNumber;

    @NonNull
    private final String missionName;

    @NonNull
    private final String launchDateUtc;

    @Nullable
    private final String details;

    @NonNull
    private final String rocketName;

    @NonNull
    private final String rocketType;

    @NonNull
    private final Boolean isSuccess;

    @Nullable
    private final String failureReason;

    @Nullable
    private final String missionPatch;

    @Nullable
    private final String videoLink;

    @Nullable
    private final String wikipediaLink;

    public FullLaunchEntity(@NonNull String flightNumber,
                            @NonNull String missionName,
                            @NonNull String launchDateUtc,
                            @Nullable String details,
                            @NonNull String rocketName,
                            @NonNull String rocketType,
                            @NonNull Boolean isSuccess,
                            @Nullable String failureReason,
                            @Nullable String missionPatch,
                            @Nullable String videoLink,
                            @Nullable String wikipediaLink) {
        this.flightNumber = flightNumber;
        this.missionName = missionName;
        this.launchDateUtc = launchDateUtc;
        this.details = details;
        this.rocketName = rocketName;
        this.rocketType = rocketType;
        this.isSuccess = isSuccess;
        this.failureReason = failureReason;
        this.missionPatch = missionPatch;
        this.videoLink = videoLink;
        this.wikipediaLink = wikipediaLink;
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
    public String getLaunchDateUtc() {
        return launchDateUtc;
    }

    @Nullable
    public String getDetails() {
        return details;
    }

    @NonNull
    public String getRocketName() {
        return rocketName;
    }

    @NonNull
    public String getRocketType() {
        return rocketType;
    }


    @Nullable
    public String getFailureReason() {
        return failureReason;
    }

    @Nullable
    public String getMissionPatch() {
        return missionPatch;
    }

    @Nullable
    public String getVideoLink() {
        return videoLink;
    }

    @Nullable
    public String getWikipediaLink() {
        return wikipediaLink;
    }

    @NonNull
    public Boolean isSuccess() {
        return isSuccess;
    }
}