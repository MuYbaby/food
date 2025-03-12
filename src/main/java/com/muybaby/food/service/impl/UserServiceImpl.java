package com.muybaby.food.service.impl;


import com.muybaby.food.mapper.UserMapper;
import com.muybaby.food.bean.User;
import com.muybaby.food.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author MuYbaby
 * @create 2025/3/7
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Override
    public User getUser(Integer user_id) {
        return userMapper.SelectUser(user_id);
    }

    @Override
    public User getUserByName(String username) {
        return  userMapper.SelectUserByName(username);
    }

    @Override
    public int addUser(User user) {
        return userMapper.InsertUser(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userMapper.SelectUserByEmail(email);
    }
}
