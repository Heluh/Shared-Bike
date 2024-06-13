package com.example.androidend.mapper;

import com.example.androidend.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    void insertUser(User user);
    User selectUserById(Integer id);
    User selectUserByUsername(String username);
    void updateUser(User user);
    void deleteUser(Integer id);
}