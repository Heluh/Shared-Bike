package com.train.campussharedbikeservercharity.entity;

import lombok.Data;
import java.util.Date;

@Data
public class CharityEvent {
    private int id;
    private String name;
    private Date start_date;
    private Date end_date;
    private boolean last_forever;
    private String receive_loc_name;
    private String description;

    public CharityEvent() {
    }
}
