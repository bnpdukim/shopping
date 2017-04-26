package com.example.order.repository;

import com.example.order.domain.Order;
import org.springframework.data.repository.Repository;

/**
 * Created by sajacaros on 2017-04-26.
 */
public interface OrderRepository extends Repository<Order, Long> {
    void save(Order order);
}
