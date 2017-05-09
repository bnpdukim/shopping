package com.example.order.service;

import com.example.order.domain.Order;
import com.example.order.dto.OrderDto;
import com.example.order.repository.OrderRepository;
import com.example.product.dto.ProductDto;
import com.example.user.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Created by sajacaros on 2017-04-26.
 */
public interface OrderService {
    void create(OrderDto.New newOrder);

    CompletableFuture<List<OrderDto.Details>> orders();

    @Slf4j
    @Service
    @Transactional
    class Default implements OrderService {
        @Autowired
        private OrderRepository orderRepository;

        @Autowired
        private UserEndPoint userEndPoint;
        @Autowired
        private ProductEndPoint productEndPoint;

        @Override
        public void create(OrderDto.New newOrder) {
            orderRepository.save(new Order(newOrder.getPrincipalId(),newOrder.getProductId(),newOrder.getQuantity()));
        }

        @Override
        public CompletableFuture<List<OrderDto.Details>> orders() {
             return orderRepository.findAllBy()
                .thenApply(orders->
                    orders.stream()
                        .map(order->new OrderDto.Details(
                                order.getId(),
                                order.getPrincipalId(),
                                order.getProductId(),
                                order.getQuantity()))
                        .map(orderDto -> userFuture(orderDto))
                        .map(completableOrderDto->completableOrderDto.thenCompose(orderDto->productFuture(orderDto)))
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
                );
        }

        private CompletableFuture<OrderDto.Details> productFuture(final OrderDto.Details orderDto) {
            CompletableFuture<OrderDto.Details> future = new CompletableFuture<>();
            ResponseEntity<ProductDto.Response> responseEntity = productEndPoint.product(orderDto.getOrder().getProductId());
            if( responseEntity.getStatusCode().is2xxSuccessful() ) {
                orderDto.setProduct(responseEntity.getBody());
                future.complete(orderDto);
            } else {
                log.warn("user endpoint repsonse is failed.. productId : {}", orderDto.getOrder().getProductId());
                future.completeExceptionally(new RuntimeException("product 정보 획득 실패"));
            }

            return future;
        }

        private CompletableFuture<OrderDto.Details> userFuture(final OrderDto.Details orderDto) {
            CompletableFuture<OrderDto.Details> future = new CompletableFuture<>();
            ResponseEntity<UserDto.Response> responseEntity = userEndPoint.userProfile(orderDto.getOrder().getPrincipalId());
            if( responseEntity.getStatusCode().is2xxSuccessful() ) {
                orderDto.setUser(responseEntity.getBody());
                future.complete(orderDto);
            } else {
                log.warn("user endpoint repsonse is failed.. principalId : {}", orderDto.getOrder().getPrincipalId());
                future.completeExceptionally(new RuntimeException("user 정보 획득 실패"));
            }
            return future;
        }
    }
}
