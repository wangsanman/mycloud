package org.example.user.config;

import cn.hutool.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyInterceptor())
                .addPathPatterns("/**") // 配置拦截路径
                .excludePathPatterns("/doc.html",         // Swagger UI 主页面
                        "/swagger-resources/**",    // Swagger 资源路径
                        "/v3/api-docs/**",          // OpenAPI 3.0 文档路径
                        "/v2/api-docs",             // Swagger 2 文档路径
                        "/webjars/**"               // Swagger 静态资源路径
                );
        // 配置不拦截路径
    }

}