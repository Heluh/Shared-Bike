package com.example.bike.service;


import com.example.bike.bean.Bike;
import com.example.bike.mapper.BikeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BikeService {
    @Autowired
    private BikeMapper bikeMapper;

    public List<Bike> showBike(){
        return bikeMapper.showBike();
    }

    public int deleteBike(Integer id){
        return bikeMapper.deleteBike(id);
    }

    public int updateBike(Bike bike){
        return bikeMapper.updateBike(bike);
    }

    public int addBike(Bike bike){
        return bikeMapper.addBike(bike);
    }

    public List<Bike> findBikeByNum(String bikeNumber){
        return bikeMapper.findBikeByNum(bikeNumber);
    }

}
