package com.example.order.service;

import com.example.order.domain.Order;
import com.example.order.dto.OrderDto;
import com.example.order.repository.OrderRepository;
import com.example.product.dto.ProductDto;
import com.example.user.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
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
            productEndPoint.product(orderDto.getOrder().getProductId())
                .enqueue(new Callback<ProductDto.Response>() {
                    @Override
                    public void onResponse(Call<ProductDto.Response> call, Response<ProductDto.Response> response) {
                        if(response.isSuccessful()) {
                            orderDto.setProduct(response.body());
                            future.complete(orderDto);
                        } else {
                            log.warn("user endpoint repsonse is failed.. productId : {}", orderDto.getOrder().getProductId());
                            future.completeExceptionally(new RuntimeException("product 정보 획득 실패"));
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductDto.Response> call, Throwable t) {
                        log.warn("user endpoint repsonse is error.. productId : {}", orderDto.getOrder().getProductId());
                        future.completeExceptionally(new RuntimeException("product 정보 연결 실패"));
                    }
                });
            return future;
        }

        private CompletableFuture<OrderDto.Details> userFuture(final OrderDto.Details orderDto) {
            CompletableFuture<OrderDto.Details> future = new CompletableFuture<>();
            userEndPoint.userProfile(orderDto.getOrder().getPrincipalId())
                .enqueue(new Callback<UserDto.Response>() {
                    @Override
                    public void onResponse(Call<UserDto.Response> call, Response<UserDto.Response> response) {
                        if (response.isSuccessful()) {
                            log.info("!!!!!!!!!!!!!!!!!!!!!!!!");
                            orderDto.setUser(response.body());
                            future.complete(orderDto);
                        } else {
                            log.warn("user endpoint repsonse is failed.. principalId : {}", orderDto.getOrder().getPrincipalId());
                            future.completeExceptionally(new RuntimeException("user 정보 획득 실패"));
                        }
                    }

                    @Override
                    public void onFailure(Call<UserDto.Response> call, Throwable t) {
                        log.warn("user endpoint repsonse is error.. principalId : {}", orderDto.getOrder().getPrincipalId());
                        future.completeExceptionally(new RuntimeException("user 정보 연결 실패"));
                    }
                });
            return future;
        }
    }
}
