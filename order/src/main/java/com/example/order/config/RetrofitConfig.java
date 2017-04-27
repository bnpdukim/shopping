package com.example.order.config;

import com.example.order.service.ProductEndPoint;
import com.example.order.service.UserEndPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by sajacaros on 2017-04-27.
 */
@Configuration
public class RetrofitConfig {
    @Value("${user.uri}")
    private String userUri;
    @Value("${user.port}")
    private int userPort;
    @Value("${user.context-path}")
    private String userContextPath;
    @Value("${user.version}")
    private String userVersion;

    @Value("${product.uri}")
    private String productUri;
    @Value("${product.port}")
    private int productPort;
    @Value("${product.context-path}")
    private String productContextPath;
    @Value("${product.version}")
    private String productVersion;

    @Bean
    public Retrofit userRetrofit() {
        StringBuilder builder = new StringBuilder();
        String userUri = builder
                .append("http://")
                .append(this.userUri)
                .append(":")
                .append(this.userPort)
                .append(this.userContextPath)
                .append("/api/")
                .append(this.userVersion)
                .append("/")
                .toString();
        return new Retrofit.Builder()
                .baseUrl(userUri)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    @Bean
    public UserEndPoint userEndpoint() {
        return userRetrofit().create(UserEndPoint.class);
    }

    @Bean
    public Retrofit productRetrofit() {
        StringBuilder builder = new StringBuilder();
        String productUri = builder
                .append("http://")
                .append(this.productUri)
                .append(":")
                .append(this.productPort)
                .append(this.productContextPath)
                .append("/api/")
                .append(this.productVersion)
                .append("/")
                .toString();
        return new Retrofit.Builder()
                .baseUrl(productUri)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    @Bean
    public ProductEndPoint rmEndpoint() {
        return productRetrofit().create(ProductEndPoint.class);
    }
}
