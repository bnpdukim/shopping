package com.example.product.service;

import com.example.product.domain.Product;
import com.example.product.dto.ProductDto;
import com.example.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * Created by sajacaros on 2017-04-24.
 */
public interface ProductService {
    void create(ProductDto.New newProduct);

    CompletableFuture<List<ProductDto.Response>> allProducts();

    ProductDto.Response findProduct(Long productId);

    @Service
    @Slf4j
    @Transactional
    class Default implements ProductService {
        @Autowired
        private ProductRepository productRepository;


        @Override
        public void create(ProductDto.New newProduct) {
            Product p = new Product(newProduct.getName(), newProduct.getPrice());
            productRepository.save(p);
        }

        @Override
        public CompletableFuture<List<ProductDto.Response>> allProducts() {
            return productRepository.findAllBy().thenApply(products ->
                products.stream()
                        .map(p->new ProductDto.Response(p.getId(), p.getName(), p.getPrice()))
                        .collect(Collectors.toList())
            );
        }

        @Override
        public ProductDto.Response findProduct(Long productId) {
            return productRepository.findOne(productId)
                .map(p->new ProductDto.Response(p.getId(),p.getName(),p.getPrice()))
                .orElseThrow(()->new RuntimeException("id"+productId+"에 해당하는 product가 존재하지 않음"));
        }
    }
}
