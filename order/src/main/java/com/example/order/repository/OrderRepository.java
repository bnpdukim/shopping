package com.example.order.repository;

import com.example.order.domain.Order;
import org.springframework.data.repository.Repository;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by sajacaros on 2017-04-26.
 */
public interface OrderRepository extends Repository<Order, Long> {
    void save(Order order);

    @Async("jdbcExecutor")
    CompletableFuture<List<Order>> findAllBy();
}
