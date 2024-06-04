package com.example.login.mapper;

import com.example.login.bean.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    /*
    * 登陆检查
    */
    User login(String userAccount, String userPassword);
}

