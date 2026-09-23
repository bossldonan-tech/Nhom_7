package com.ptpmhdv.order.client;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Chuyen tiep header Authorization (JWT) cua nguoi dung dang goi order-service
 * sang cac loi goi Feign toi product-service / payment-service, de cac service
 * do xac dinh dung nguoi dung hien tai (CurrentUser) khi xu ly.
 */
@Configuration
public class FeignAuthConfig {

    @Bean
    public RequestInterceptor authForwardingInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                String authHeader = attrs.getRequest().getHeader("Authorization");
                if (authHeader != null) {
                    requestTemplate.header("Authorization", authHeader);
                }
            }
        };
    }
}
