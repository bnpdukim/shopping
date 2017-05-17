package com.example.product.config;

import com.example.product.filter.JwtLogFilter;
import com.example.product.security.CustomAuthenticationEntryPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

@Configuration
@EnableAspectJAutoProxy
@EnableResourceServer
@Slf4j
public class ResourceConfig extends ResourceServerConfigurerAdapter{

    @Autowired
    private JwtLogFilter jwtLogFilter;

    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() throws Exception {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        KeyPair keyPair = new KeyStoreKeyFactory(
                new ClassPathResource("keystore.jks"), "foobar".toCharArray())
                .getKeyPair("test");
        converter.setKeyPair(keyPair);
        converter.afterPropertiesSet();
        
        return converter;
    }

    @Bean
    public TokenStore tokenStore() throws Exception {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }
    
    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http

                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
//                .and()
//                    .requestMatchers()
//                        .antMatchers("/**")
                .and()
                    .authorizeRequests()
                        .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .antMatchers(HttpMethod.GET, "/**").authenticated()
                        .antMatchers(HttpMethod.PATCH, "/**").permitAll()
                        .antMatchers(HttpMethod.POST, "/**").authenticated()
                        .antMatchers(HttpMethod.PUT, "/**").permitAll()
                        .antMatchers(HttpMethod.DELETE, "/**").permitAll()
                .and()
                    .exceptionHandling()
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                    .headers().frameOptions().disable()
                .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .csrf().disable();

        // @formatter:on
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("dummy").tokenStore(tokenStore());
    }

}

