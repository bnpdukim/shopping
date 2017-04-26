package com.example.user.service;

import com.example.user.domain.User;
import com.example.user.dto.UserDto;
import com.example.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Created by sajacaros on 2017-04-26.
 */
public interface UserService {
    void create(UserDto.New newUser);

    CompletableFuture<List<UserDto.Response>> users();

    UserDto.Response userFor(Long id);

    @Service
    @Slf4j
    @Transactional
    class Default implements UserService {
        @Autowired
        private UserRepository userRepository;

        @Override
        public void create(UserDto.New requestUser) {
            User newUser = new User(requestUser.getPrincipalId(), requestUser.getName(), requestUser.getSex());
            userRepository.save(newUser);
        }

        @Override
        public CompletableFuture<List<UserDto.Response>> users() {
            return userRepository.findAllBy()
                .thenApply(users->
                    users.stream()
                        .map(user->new UserDto.Response(user.getId(), user.getPrincipalId(),user.getName(),user.getSex()))
                        .collect(Collectors.toList())
                );
        }

        @Override
        public UserDto.Response userFor(Long id) {
            return userRepository.findOne(id)
                    .map(u->new UserDto.Response(u.getId(),u.getPrincipalId(),u.getName(),u.getSex()))
                    .orElseThrow( ()->new RuntimeException("user("+id+") not fount") );
        }
    }
}
