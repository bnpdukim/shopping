package com.example.order.service;

import com.example.user.dto.UserDto;
import retrofit2.Call;

/**
 * Created by sajacaros on 2017-04-27.
 */
public interface UserEndPoint {
    Call<UserDto.Response> userProfile(String principalId);
}
