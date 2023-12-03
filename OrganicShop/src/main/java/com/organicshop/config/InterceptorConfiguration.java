package com.organicshop.config;

import com.organicshop.web.interceptor.ClosedInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    private final ClosedInterceptor closedInterceptor;

    public InterceptorConfiguration(ClosedInterceptor closedInterceptor) {
        this.closedInterceptor = closedInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
        registry.addInterceptor(closedInterceptor);
    }

}
