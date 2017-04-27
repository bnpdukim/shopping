package com.example.order.service;

import com.example.product.dto.ProductDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by sajacaros on 2017-04-27.
 */
public interface ProductEndPoint {
    @GET("{productId}")
    Call<ProductDto.Response> product(@Path("productId") Long productId);
}
