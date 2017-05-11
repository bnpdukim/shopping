package com.example.order.service;

import com.example.product.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by sajacaros on 2017-05-09.
 */
@Slf4j
@Component
public class ProductFallback implements ProductEndPoint{
    @Override
    public ResponseEntity<ProductDto.Response> product(@PathVariable("productId") Long productId) {
        log.info("product fallback");
//        return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new ProductDto.Response((long) -1, "", -1));
    }
}
