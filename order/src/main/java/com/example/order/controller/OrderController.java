package com.example.order.controller;

import com.example.order.dto.OrderDto;
import com.example.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.security.Principal;
import java.util.List;

/**
 * Created by sajacaros on 2017-04-26.
 */
@RestController
@RequestMapping("/api/${api.version}")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createOrder(@RequestBody OrderDto.New newOrder, Principal principal) {
        log.info("create order : {}, principal : {}", newOrder, principal.getName());
        orderService.create(newOrder);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public DeferredResult<List<OrderDto.Details>> orders() {
        DeferredResult<List<OrderDto.Details>> deferredResult = new DeferredResult<>();

        orderService.orders().thenAccept(orders -> deferredResult.setResult(orders));

        return deferredResult;
    }
}
