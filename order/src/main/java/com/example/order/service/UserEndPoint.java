package com.example.order.service;

import com.example.user.dto.UserDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by sajacaros on 2017-04-27.
 */
public interface UserEndPoint {
    @GET("{principalId}")
    Call<UserDto.Response> userProfile(@Path("principalId") String principalId);
}
