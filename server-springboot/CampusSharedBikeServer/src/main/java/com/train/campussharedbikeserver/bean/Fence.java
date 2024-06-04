package com.train.campussharedbikeserver.bean;

import lombok.Data;

@Data
public class Fence {
    private int id;
    private String fence_name;
    private int capacity;
    private int current_num;
    private String area;
    private String fence_points;
    private String create_date;
    private int type;

    public Fence() {
    }
}
