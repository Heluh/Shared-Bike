package com.example.androidend.mapper;


import com.example.androidend.entity.Bike;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BikeMapper {
    List<Bike> findAll();

    void unlock(Integer id);

    void lock(Integer id);
}
