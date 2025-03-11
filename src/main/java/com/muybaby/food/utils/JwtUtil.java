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

    private static final String secret = "MuYbaby";
    private static final long expiration = 86400000;

    public static String generateToken(User user) {
        try {
            return JWT.create()
                    .withSubject(user.getUserId().toString())
                    .withClaim("username", user.getUsername())
                    .withClaim("role", user.getRole()) // 加入角色信息
                    .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                    .sign(Algorithm.HMAC256(secret));
        } catch (JWTCreationException e) {
            throw new RuntimeException("生成JWT失败", e);
        }
    }

    // 新增方法，用于从 JWT 中获取角色信息
    public static String getRoleFromToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(secret))
                    .build()
                    .verify(token)
                    .getClaim("role").asString();
        } catch (Exception e) {
            throw new RuntimeException("JWT验证失败", e);
        }
    }

    public static DecodedJWT verifyToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(secret))
                    .build()
                    .verify(token);
        } catch (JWTVerificationException e) {
            throw new RuntimeException("JWT验证失败", e);
        }
    }

    public static void main(String[] args) {
        try {
            String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI0NiIsInVzZXJuYW1lIjoiMTIzMTIzIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3NDE3NjU2NDB9.p4pl4AYKsiI3zUuefKdwVRIsgLe3uJtM0EBLHSR4gdM";
                if (JwtUtil.getRoleFromToken(token).equals("USER")){
                System.out.println("验证成功");
                }
            System.out.println("demo");
        }catch (Exception e){
//            log.error("e: ", e);
            System.out.println("验证失败"+e);
        }
    }
}