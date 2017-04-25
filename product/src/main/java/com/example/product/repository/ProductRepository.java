package com.example.product.repository;

import com.example.product.domain.Product;
import org.springframework.data.repository.Repository;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Created by sajacaros on 2017-04-24.
 */
public interface ProductRepository extends Repository<Product, Long> {
    Product save(Product product);

    @Async("jdbcExecutor")
    CompletableFuture<List<Product>> findAllBy();

    Optional<Product> findOne(Long productId);
}
