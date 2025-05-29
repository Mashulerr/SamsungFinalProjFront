package com.example.spacex.data.utils.mapper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.spacex.data.dto.LaunchDto;
import com.example.spacex.domain.entity.FullLaunchEntity;
import com.example.spacex.domain.entity.ItemLaunchEntity;

import java.util.ArrayList;
import java.util.List;


public class LaunchMapper {

    @Nullable
    public static ItemLaunchEntity toItemLaunchEntity(@NonNull LaunchDto launchDto) {
        final String flightNumber = launchDto.flightNumber;
        final String missionName = launchDto.missionName;
        final String launchYear = launchDto.launchYear;
        final Boolean isSuccess = launchDto.isSuccess;
        final String rocketName = launchDto.rocket != null ? launchDto.rocket.rocketName : null;
        final String missionPatch = launchDto.links != null ? launchDto.links.missionPatch : null;

        if (
                flightNumber != null
                        && missionName != null
                        && launchYear != null
                        && rocketName != null
                        && isSuccess != null
        ) {
            return new ItemLaunchEntity(
                    flightNumber,
                    missionName,
                    launchYear,
                    rocketName,
                    missionPatch,
                    isSuccess
            );
        }
        return null;
    }

    @NonNull
    public static List<ItemLaunchEntity> toItemLaunchEntityList(@NonNull List<LaunchDto> launchesDto) {
        List<ItemLaunchEntity> result = new ArrayList<>(launchesDto.size());
        for (LaunchDto launch : launchesDto) {
            ItemLaunchEntity item = toItemLaunchEntity(launch);
            if (item != null) {
                result.add(item);
            }
        }
        return result;
    }

    @Nullable
    public static FullLaunchEntity toFullLaunchEntity(@NonNull LaunchDto launchDto) {
        final String flightNumber = launchDto.flightNumber;
        final String missionName = launchDto.missionName;
        final String launchDateUtc = launchDto.launchDateUtc;
        final String details = launchDto.details;
        final Boolean isSuccess = launchDto.isSuccess;
        final String rocketName = launchDto.rocket != null ? launchDto.rocket.rocketName : null;
        final String rocketType = launchDto.rocket != null ? launchDto.rocket.rocketType : null;
        final String failureReason = launchDto.failureDetails != null ? launchDto.failureDetails.reason : null;
        final String missionPatch = launchDto.links != null ? launchDto.links.missionPatch : null;
        final String videoLink = launchDto.links != null ? launchDto.links.videoLink : null;
        final String wikipediaLink = launchDto.links != null ? launchDto.links.wikipediaLink : null;

        if (
                flightNumber != null
                        && missionName != null
                        && launchDateUtc != null
                        && rocketName != null
                        && rocketType != null
                        && isSuccess != null
        ) {
            return new FullLaunchEntity(
                    flightNumber,
                    missionName,
                    launchDateUtc,
                    details,
                    rocketName,
                    rocketType,
                    isSuccess,
                    failureReason,
                    missionPatch,
                    videoLink,
                    wikipediaLink
            );
        }
        return null;
    }
}