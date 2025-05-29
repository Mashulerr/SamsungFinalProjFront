package com.example.spacex.data.source;

import com.example.spacex.data.dto.LaunchDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface LaunchApi {

    @GET("launches")
    Call<List<LaunchDto>> getAllLaunches();

    @GET("launches/{flight_number}")
    Call<LaunchDto> getLaunchByFlightNumber(@Path("flight_number") String flightNumber);
}
