package com.example.order.service;

import com.example.product.dto.ProductDto;
import retrofit2.Call;

/**
 * Created by sajacaros on 2017-04-27.
 */
public interface ProductEndPoint {
    Call<ProductDto.Response> product(Long productId);
}
