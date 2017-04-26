package com.example.user.controller;

import com.example.user.dto.UserDto;
import com.example.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

/**
 * Created by sajacaros on 2017-04-26.
 */
@RestController
@RequestMapping("/api/${api.version}")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody UserDto.New newUser) {
        log.info("create user : {}", newUser);
        userService.create(newUser);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public DeferredResult<List<UserDto.Response>> users() {
        DeferredResult<List<UserDto.Response>> deferredResult = new DeferredResult<>();
        userService.users()
            .thenAccept(users->deferredResult.setResult(users));
        return deferredResult;
    }

    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserDto.Response userFor(@PathVariable("id") Long id) {
        return userService.userFor(id);
    }
}
