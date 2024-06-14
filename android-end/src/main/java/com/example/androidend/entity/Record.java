package com.example.androidend.entity;

import lombok.Data;

import java.time.Duration;

@Data
public class Record {
    private int id;
    private String username;
    private String latitude;
    private String longitude;
    private double distance;
    private String time;
    private long duration;

}
