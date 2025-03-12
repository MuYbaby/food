package com.muybaby.food.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muybaby.food.bean.Result;
import com.muybaby.food.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        log.info("Request URI: {}", uri);
        // 检查 Authorization 头
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            handleUnauthorized(response, "未提供有效的身份验证令牌");
            return false;
        }

        String token = authorization.substring(7);

        try {
            // 验证 JWT 令牌
            JwtUtil.verifyToken(token);

            // 获取用户角色
            String role = JwtUtil.getRoleFromToken(token);

            // 路径权限检查
            if (uri.startsWith("/api/admin") && !"ADMIN".equals(role)) {
                handleForbidden(response, "无权访问此资源");
                return false;
            }

            return true;
        } catch (Exception e) {
            log.error("Token verification failed", e);
            handleUnauthorized(response, "无效的身份验证令牌");
            return false;
        }
    }

    private void handleUnauthorized(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Result<Object> result = Result.error(HttpStatus.UNAUTHORIZED, message);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }

    private void handleForbidden(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Result<Object> result = Result.error(HttpStatus.FORBIDDEN, message);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}