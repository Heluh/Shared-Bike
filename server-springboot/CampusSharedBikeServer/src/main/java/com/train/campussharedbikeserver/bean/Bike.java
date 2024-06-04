package com.train.campussharedbikeserver.bean;
import lombok.Data;

@Data
public class Bike {
    private int id;
    private String bikeNum;
    private String status;
    private double longitude;
    private double latitude;
    private String create_time;
    private String lastUpdateTime;
    private int fenceId;
}
