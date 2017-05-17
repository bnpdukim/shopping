package com.example.order.config;

import com.example.order.security.OAuth2FeignRequestInterceptor;
import feign.Contract;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

/**
 * Created by sajacaros on 2017-05-16.
 */
@Configuration
@Slf4j
public class FeignConfiguration {

    @Value("${uaa.client.id}")
    private String oAuth2ClientId;

    @Value("${uaa.client.client-secret}")
    private String oAuth2ClientSecret;

    @Value("${uaa.client.access-token-uri}")
    private String accessTokenUri;


    @Bean
    @ConfigurationProperties("uaa.client")
    @Primary
    OAuth2ProtectedResourceDetails uaa() {
        return new ClientCredentialsResourceDetails();
    }

    @Primary
    @Bean
    public OAuth2ClientContext singletonClientContext() {
        return new DefaultOAuth2ClientContext();
    }

    @Bean
    @ConditionalOnBean(OAuth2ClientContext.class)
    public RequestInterceptor oauth2FeignRequestInterceptor(OAuth2ClientContext oauth2ClientContext){
        return new OAuth2FeignRequestInterceptor(oauth2ClientContext, uaa());
    }

}
