package com.example.order.service;

import com.example.product.dto.ProductDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by sajacaros on 2017-04-27.
 */
@FeignClient(value = "product")
public interface ProductEndPoint {
    @RequestMapping(method = RequestMethod.GET, value = "/${product.context-path}/${product.version}/{productId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<ProductDto.Response> product(@PathVariable("productId") Long productId);
}
