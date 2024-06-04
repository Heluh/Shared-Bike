package com.example.login.bean;

import lombok.Data;

@Data
public class User {
    private int id;
    private String userAccount;
    private String userPassword;
    private String userName;
    private int userNum;
    private int userState;
    private int userDel;
}
