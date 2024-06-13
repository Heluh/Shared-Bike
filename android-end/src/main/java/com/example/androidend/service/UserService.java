package com.example.androidend.service;

import com.example.androidend.entity.User;
import com.example.androidend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public User selectUserById(int id) {
        try {
            return userMapper.selectUserById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public User selectUserByUsername(String username) {
        try {
            return userMapper.selectUserByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void save(User user) {
        try {
            userMapper.insertUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
