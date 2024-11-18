package org.example.user.config;

import org.example.user.IgnoreToken;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class MyInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            Object bean = handlerMethod.getBean();
            if (method.isAnnotationPresent(IgnoreToken.class)) {
                return true;
            }
        }

//        String token = request.getHeader("Authorization");
//        if (token == null) {
//            response.setStatus(401);
//            response.setContentType("application/json;charset=utf-8");
//            response.getWriter().write("{\"error\": \"Invalid token\"}");
//            return false;
//        }

        // 在请求处理之前进行操作
        return true; // 返回 true 继续请求处理，返回 false 终止请求处理
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 在请求处理之后，但在视图渲染之前进行操作
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 在请求处理完成后进行操作
    }
}
