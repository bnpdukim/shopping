package com.example.order.service;

import com.example.user.dto.UserDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by sajacaros on 2017-04-27.
 */
@FeignClient("user")
public interface UserEndPoint {
    @RequestMapping(method = RequestMethod.GET, value = "/user/api/v1/{principalId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<UserDto.Response> userProfile(@PathVariable("principalId") String principalId);
}
