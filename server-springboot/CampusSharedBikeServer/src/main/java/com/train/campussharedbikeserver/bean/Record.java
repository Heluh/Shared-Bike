package com.train.campussharedbikeserver.bean;

import lombok.Data;

@Data
public class Record {
    private int id;
    private String userAccount;
    private String latitude;
    private String longitude;
    private int distance;
    private String time;

    public Record() {
    }
}
