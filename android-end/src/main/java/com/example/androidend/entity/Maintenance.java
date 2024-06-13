package com.example.androidend.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Maintenance {


    private Long id;

    private String bikeNumber;

    private String username;

    private Date maintenanceTime;

    private String maintenanceType;

    private String maintenanceStatus;




}