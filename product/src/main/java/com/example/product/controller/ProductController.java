package com.example.product.controller;

import com.example.product.dto.ProductDto;
import com.example.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by sajacaros on 2017-04-24.
 */
@RestController
@RequestMapping("/api/${api.version}")
@Slf4j
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductDto.New newProduct) {
        log.info("create product : {}", newProduct);
        productService.create(newProduct);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public DeferredResult<List<ProductDto.Response>> productList() {
        log.info("find all product");
        DeferredResult<List<ProductDto.Response>> deferredResult = new DeferredResult<>();
        productService.allProducts()
            .thenAccept(products->deferredResult.setResult(products));

        return deferredResult;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ProductDto.Response product(@PathVariable("id") Long productId) {
        log.info("product id : {}", productId);
        return productService.findProduct(productId);
    }
}
