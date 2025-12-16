package com.experiment.parkingsystem.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor())
                .addPathPatterns("/**") // 拦截所有接口
                .excludePathPatterns(          // 排除登录注册
                        "/users/login",
                        "/users/register",
                        "/users/forgot-password"
                );
    }
}