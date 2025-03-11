package com.muybaby.food.bean;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * @author MuYbaby
 * @create 2025/3/6
 */

@Data
@Builder
public class User {
    private Integer userId;
    private String username;
    private String passwordHash;
    private String email;
    private String phone;
    private String role;
    @JsonSerialize(using = LocalDateTimeToSecondsSerializer.class)
    private LocalDateTime createdAt;  // 直接存储秒级时间戳
    @JsonSerialize(using = LocalDateTimeToSecondsSerializer.class)
    private LocalDateTime updatedAt;

    // 添加默认构造函数
    public User() {
    }

    public User(Integer userId, String username, String passwordHash, String email, String phone, String role, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}