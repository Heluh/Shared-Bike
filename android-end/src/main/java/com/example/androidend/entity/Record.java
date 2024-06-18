package com.example.androidend.entity;

import lombok.Data;

import java.time.Duration;
import java.util.Date;

@Data
public class Record {
    private int id;
    private String username;
    private double distance;
    private Date time;
    private long duration;
    private byte status;
    private String bikeNum;
    private String contrail;

}
