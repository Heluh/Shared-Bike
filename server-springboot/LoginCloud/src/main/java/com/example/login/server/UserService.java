package com.example.login.server;

import com.example.login.bean.User;
import com.example.login.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper; //注入UserMapper

    /*
     * 登陆检查
     */
    public User login(String username, String password){
        return userMapper.login(username,password);
    }

}