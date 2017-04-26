package com.example.order.service;

import com.example.order.domain.Order;
import com.example.order.dto.OrderDto;
import com.example.order.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by sajacaros on 2017-04-26.
 */
public interface OrderService {
    void create(OrderDto.New newOrder);

    @Slf4j
    @Service
    @Transactional
    class Default implements OrderService {
        @Autowired
        private OrderRepository orderRepository;

        @Override
        public void create(OrderDto.New newOrder) {
            orderRepository.save(new Order(newOrder.getPrincipalId(),newOrder.getProductId(),newOrder.getQuantity()));
        }
    }
}
