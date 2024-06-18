package com.example.androidend.entity;

import lombok.Data;


@Data
public class Bike {

    private Integer id;

    private String bikeNumber;

    private byte status;

    private double longitude;

    private double latitude;

    private String createTime;

    private String lastUpdateTime;

    private int fenceId;

    public static final byte STATUS_AVAILABLE = 0;

    public static final byte STATUS_USING = 1;
}
