package org.example.gateway;

import cn.hutool.core.collection.CollectionUtil;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.common.JwtUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@ConfigurationProperties("mycloud")
@RequiredArgsConstructor
@Setter
@Slf4j
public class MyGlobalFilter implements GlobalFilter , Ordered {
    private List<String> whiteList;
    private final JwtUtil jwtUtil;
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                log.info("##################gateway:start:deviceIp: {}",exchange.getRequest().getRemoteAddress());
                //1.获取request
                ServerHttpRequest request = exchange.getRequest();
                //2.判断是否在白名单
                if (isWhitelist(request.getPath().toString())){
                    //白名单放行
                    return chain.filter(exchange);
                }
                //3.获取token
                List<String> header = request.getHeaders().get("Authorization");
                String token = null;
                if (header != null && CollectionUtil.isNotEmpty(header)) {
                    token = header.get(0);
                }

                final String userId;
                try {
                    //4.校验并解析token
                    userId = jwtUtil.parseToken(token);
                } catch ( Exception e) {
                    log.error("token异常",e);
                    //5.失败返回401
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return response.setComplete();
                }
                //6.请求头中放入用户信息
        ServerWebExchange build = exchange.mutate().request(a->a.header("user-info",userId)).build();

        //正常放行
                return chain.filter(build);
    }

    /**
     * 判断路径是否在白名单中
     * @param path
     * @return
     */
    private boolean isWhitelist(String path) {
        for (String w : whiteList) {
            if (antPathMatcher.match(w,path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
