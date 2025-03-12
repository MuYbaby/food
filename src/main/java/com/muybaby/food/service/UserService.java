package com.muybaby.food.service;

import com.muybaby.food.bean.User;


/**
 * @author MuYbaby
 * @create 2025/3/7
 */

public interface UserService {
    User getUser(Integer user_id);
    User getUserByName(String username);
    int addUser(User user);
    User getUserByEmail(String email);
}
