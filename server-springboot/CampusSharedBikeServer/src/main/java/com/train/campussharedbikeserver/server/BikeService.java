package com.train.campussharedbikeserver.server;

import com.train.campussharedbikeserver.bean.Bike;
import com.train.campussharedbikeserver.mapper.BikeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BikeService {
    @Autowired
    private BikeMapper bikeMapper;

    public List<Bike> selectBikeByFenceId(int fenceId){
        return bikeMapper.selectBikeByFenceId(fenceId);
    }

}
