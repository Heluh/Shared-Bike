package com.train.campussharedbikeserver.bean;

import lombok.Data;

import java.util.Date;

@Data
public class Record {
    private int id;
    private String username;
    private String latitude;
    private String longitude;
    private int distance;
    private Date time;
    long duration;
    private byte status;
    private String bikeNumber;
}
