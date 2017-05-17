package com.example.order.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtLogFilter extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if (response.getStatus() >= HttpStatus.SC_BAD_REQUEST) {
                log.info("bad status : {}", response.getStatus());
            } else {
                if(null != request.getUserPrincipal()) {
                    log.info("jwt log filter request get name : {}", request.getUserPrincipal().getName());
                } else {
//                    log.info("jwt log not authentication status : {}", ((HttpServletResponse) response).getStatus());
                }

//                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//                Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
//                log.info("authentication : {}", authentication.getAuthorities());
            }
            filterChain.doFilter(request, response);
        } catch (RuntimeException e) {
        }
    }
}
