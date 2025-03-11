package com.muybaby.food.mapper;

import com.muybaby.food.bean.User;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author MuYbaby
 * @create 2025/3/7
 */
@Mapper
public interface UserMapper {
    User SelectUser(Integer user_id);

    User SelectUserByName(String username);

    int InsertUser(User user);

    User SelectUserByEmail(String email);
}
