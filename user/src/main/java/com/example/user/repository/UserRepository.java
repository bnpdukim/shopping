package com.example.user.repository;

import com.example.user.domain.User;
import com.example.user.dto.UserDto;
import org.springframework.data.repository.Repository;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Created by sajacaros on 2017-04-26.
 */
public interface UserRepository extends Repository<User, Long> {
    void save(User newUser);

    @Async("jdbcExecutor")
    CompletableFuture<List<User>> findAllBy();

    Optional<User> findOne(Long id);
}
