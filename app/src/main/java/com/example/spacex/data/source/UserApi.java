package com.example.spacex.data.source;

import com.example.spacex.data.dto.AccountDto;
import com.example.spacex.data.dto.UpdatedUserDto;
import com.example.spacex.data.dto.UserDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserApi {

    @GET("/api/person/username/{username}")
    Call<Void> isExist(@Path("username") String username);

    @POST("/api/person/register")
    Call<Void> register(@Body AccountDto dto);

    @GET("/api/person/login")
    Call<UserDto> login();

    @GET("/api/person/{id}")
    Call<UserDto> getUserById(@Path("id") String id);

    @PUT("/api/person/{id}")
    Call<Void> updateUserById(@Path("id") String id, @Body UpdatedUserDto userData);
}
