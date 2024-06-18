package com.example.androidend.mapper;


import com.example.androidend.entity.Bike;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BikeMapper {
    List<Bike> findAll();

    Bike findBikeByNumber(String bikeNumber);

    void unlock(String bikeNumber);

    void lock(String bikeNumber);
}
