package com.example.androidend.entity;

import lombok.Data;


@Data
public class Bike {

    private Integer id;

    private String bikeNumber;

    private String status;

    private double longitude;

    private double latitude;

    private String createTime;

    private String lastUpdateTime;

    private int fenceId;
}
