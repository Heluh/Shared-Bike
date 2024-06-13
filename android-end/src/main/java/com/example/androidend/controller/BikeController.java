package com.example.androidend.controller;

import com.example.androidend.entity.Bike;
import com.example.androidend.service.BikeService;
import com.example.androidend.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bikes")
public class BikeController {
    @Autowired
    private BikeService bikeService;


    @GetMapping("/findAll")
    public R findAll() {
        List<Bike> bikes = bikeService.findAll();
        return R.ok().put("data", bikes);
    }

    @PostMapping("/unlock")
    public R unlock(Integer id) {
        bikeService.unlock(id);
        return R.ok();
    }

    @PostMapping("/lock")
    public R lock(String bikeNumber, Double longitude, Double latitude) {
        if(bikeService.canReturnBike(bikeNumber, longitude, latitude)){
            bikeService.lock(bikeNumber);
            return R.ok();
        }else{
            return R.error("Bike not in fence");
        }
    }

}
