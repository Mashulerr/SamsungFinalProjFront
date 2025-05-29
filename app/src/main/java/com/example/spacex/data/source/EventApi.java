package com.example.spacex.data.source;

import com.example.spacex.data.dto.EventDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface EventApi {
    @GET("history/{id}")
    Call<EventDto> getEventById(@Path("id") String id);

    @GET("history")
    Call<List<EventDto>> getAllEvents();
}
