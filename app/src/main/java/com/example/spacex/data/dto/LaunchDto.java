package com.example.spacex.data.dto;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class LaunchDto {

    @Nullable
    @SerializedName("flight_number")
    public String flightNumber;

    @SerializedName("mission_name")
    @Nullable
    public String missionName;

    @SerializedName("launch_year")
    @Nullable
    public String launchYear;

    @SerializedName("launch_date_utc")
    @Nullable
    public String launchDateUtc;

    @SerializedName("details")
    @Nullable
    public String details;

    @SerializedName("launch_success")
    @Nullable
    public Boolean isSuccess;

    @SerializedName("rocket")
    @Nullable
    public RocketDto rocket;

    @SerializedName("launch_failure_details")
    @Nullable
    public FailureDetailsDto failureDetails;

    @SerializedName("links")
    public LinksDto links;

    public static class RocketDto {
        @SerializedName("rocket_name")
        @Nullable
        public String rocketName;

        @SerializedName("rocket_type")
        @Nullable
        public String rocketType;
    }

    public static class FailureDetailsDto {
        @SerializedName("reason")
        @Nullable
        public String reason;
    }

    public static class LinksDto {
        @SerializedName("mission_patch_small")
        @Nullable
        public String missionPatch;

        @SerializedName("video_link")
        @Nullable
        public String videoLink;

        @SerializedName("wikipedia")
        @Nullable
        public String wikipediaLink;
    }
}