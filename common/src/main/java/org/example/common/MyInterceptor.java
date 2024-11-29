package org.example.common;

import cn.hutool.core.util.StrUtil;
import org.springframework.web.servlet.HandlerInterceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String userInfo = request.getHeader("user-info");
        //有用户信息就存到线程上下文
        if (StrUtil.isNotBlank(userInfo)){
            UserContext.setUserId(Long.valueOf(userInfo));
        }
        return true; // 返回 true 继续请求处理，返回 false 终止请求处理
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 在请求处理完成后进行操作
        UserContext.removeUserId();
    }
}
