package org.example.user.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@RequiredArgsConstructor
public class MyInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        if (handler instanceof HandlerMethod) {
//            HandlerMethod handlerMethod = (HandlerMethod) handler;
//            Method method = handlerMethod.getMethod();
//            Object bean = handlerMethod.getBean();
//            if (method.isAnnotationPresent(IgnoreToken.class)) {
//                return true;
//            }
//        }
//
//        String token = request.getHeader("Authorization");
//        if (token == null) {
//            response.setStatus(401);
//            response.setContentType("application/json;charset=utf-8");
//            response.getWriter().write("{\"error\": \"Invalid token\"}");
//            return false;
//        }
//
//        //把用户信息存入线程上下文
//        UserContext.setUserId(Integer.valueOf(jwtUtil.getUserId(token)));

        // 在请求处理之前进行操作
        return true; // 返回 true 继续请求处理，返回 false 终止请求处理
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 在请求处理完成后进行操作
        UserContext.removeUserId();
    }
}
