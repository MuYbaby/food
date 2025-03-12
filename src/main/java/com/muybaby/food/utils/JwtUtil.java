package com.muybaby.food.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.muybaby.food.bean.User;
import lombok.extern.slf4j.Slf4j;


import java.util.Date;

@Slf4j
public class JwtUtil {

    private static final String SECRET = "MuYbaby";
    private static final long EXPIRATION = 86400000; // 24 hours in milliseconds

    public static String generateToken(User user) {
        try {
            return JWT.create()
                    .withSubject(user.getUserId().toString())
                    .withClaim("username", user.getUsername())
                    .withClaim("role", user.getRole() != null ? user.getRole() : "USER") // Default to USER role
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION))
                    .sign(Algorithm.HMAC256(SECRET));
        } catch (JWTCreationException e) {
            log.error("JWT token generation failed", e);
            throw new RuntimeException("生成认证令牌失败", e);
        }
    }

    public static String getRoleFromToken(String token) {
        try {
            DecodedJWT jwt = verifyToken(token);
            return jwt.getClaim("role").asString();
        } catch (Exception e) {
            log.error("Getting role from token failed", e);
            throw new RuntimeException("从令牌获取角色信息失败", e);
        }
    }

    public static String getUsernameFromToken(String token) {
        try {
            DecodedJWT jwt = verifyToken(token);
            return jwt.getClaim("username").asString();
        } catch (Exception e) {
            log.error("Getting username from token failed", e);
            throw new RuntimeException("从令牌获取用户名失败", e);
        }
    }

    public static Integer getUserIdFromToken(String token) {
        try {
            DecodedJWT jwt = verifyToken(token);
            return Integer.valueOf(jwt.getSubject());
        } catch (Exception e) {
            log.error("Getting user ID from token failed", e);
            throw new RuntimeException("从令牌获取用户ID失败", e);
        }
    }

    public static DecodedJWT verifyToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(SECRET))
                    .build()
                    .verify(token);
        } catch (JWTVerificationException e) {
            log.error("JWT verification failed", e);
            throw new RuntimeException("令牌验证失败", e);
        }
    }

    public static boolean isTokenExpired(String token) {
        try {
            DecodedJWT jwt = verifyToken(token);
            return jwt.getExpiresAt().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}