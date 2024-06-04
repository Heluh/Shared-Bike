package com.train.campussharedbikeservercharity.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Bike {
    private int id;
    private String bikeNumber;
    private String status;
    private double longitude;
    private double latitude;
    private Date createTime;
    private Date lastUpdateTime;
    private int fenceId;

    public Bike() {
    }
}
