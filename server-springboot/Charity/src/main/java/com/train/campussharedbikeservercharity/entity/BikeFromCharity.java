package com.train.campussharedbikeservercharity.entity;

import lombok.Data;

import java.util.Date;

@Data
public class BikeFromCharity {
    private int id;
    private int user_id;
    private int charity_event_id;
    private int bind_bike_id;
    private int bike_type;
    private Date in_time;
    private Date bind_use_time;
    private String charity_event_name;

    public BikeFromCharity() {
    }
}
