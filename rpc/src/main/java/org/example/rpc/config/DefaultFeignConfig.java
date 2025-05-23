package org.example.rpc.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.example.common.UserContext;
import org.example.rpc.client.fallback.UserApiFallbackFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.context.annotation.Bean;

public class DefaultFeignConfig {
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                if (UserContext.getUserId() != null) {
                    template.header("user-info", UserContext.getUserId().toString());
                }
            }
        };
    }

    @Bean
    public UserApiFallbackFactory fallbackFactory() {
        return new UserApiFallbackFactory();
    }
}
