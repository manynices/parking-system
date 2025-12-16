package com.experiment.parkingsystem.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取请求头中的 Authorization
        String authHeader = request.getHeader("Authorization");

        // 2. 校验格式是否为 "Bearer {token}"
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // 放行登录、注册接口 (根据你的实际路径调整)
            String uri = request.getRequestURI();
            if (uri.contains("/login") || uri.contains("/register")) {
                return true;
            }

            // 其他接口拦截并返回 401
            response.setStatus(401);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write("{\"success\":false,\"message\":\"未登录或Token无效\",\"code\":401}");
            return false;
        }

        // 3. 提取 Token 内容
        String token = authHeader.substring(7); // 去掉 "Bearer "

        // 4. 解析 Token (模拟逻辑：USER_JWT_TOKEN_1001 -> 1001)
        // 实际项目中这里应该使用 JWT 工具类校验签名
        try {
            if (token.startsWith("USER_JWT_TOKEN_")) {
                String userIdStr = token.replace("USER_JWT_TOKEN_", "");
                Long userId = Long.parseLong(userIdStr);

                // 5. 存入上下文
                UserContext.setUserId(userId);
                return true;
            }
        } catch (Exception e) {
            // 解析失败
        }

        // Token 无效
        response.setStatus(401);
        response.getWriter().write("{\"success\":false,\"message\":\"Token已失效\",\"code\":401}");
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求结束后清理 ThreadLocal，防止内存泄漏
        UserContext.remove();
    }
}