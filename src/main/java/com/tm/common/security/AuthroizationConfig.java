package com.tm.common.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthroizationConfig implements WebMvcConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthroizationConfig.class);


    @Autowired
    private AuthorizationRequestInterceptor authorizationRequestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationRequestInterceptor).addPathPatterns("/**");
        LOGGER.info("RequestInterceptor: Incorporating \"Authorization Interceptor\".");
    }
}
