package com.example.androidend.service;

import com.example.androidend.entity.Bike;
import com.example.androidend.mapper.BikeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BikeService {
    @Autowired
    private BikeMapper bikeMapper;


    public List<Bike> findAll() {
        try {
            return bikeMapper.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void unlock(Integer id) {
        try {
            bikeMapper.unlock(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
