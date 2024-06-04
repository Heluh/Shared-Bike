package com.example.bike.controller;

import com.alibaba.fastjson.JSONObject;

import com.example.bike.bean.Bike;
import com.example.bike.service.BikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "BikeController", description = "车辆信息管理")
@RestController
@RequestMapping("/bike")
public class BikeController {

    @Autowired
    private BikeService bikeService;

    //显示所有车辆的信息
    @Operation(summary = "显示所有车辆的信息")
    @GetMapping(value = "/showBike")
    public JSONObject showBike(){
        JSONObject returnValue = new JSONObject();
        List<Bike> bikeList = bikeService.showBike();
        if (bikeList == null){
            returnValue.put("code", 500);
            returnValue.put("bikeList", null);
            returnValue.put("msg", "查询失败");
        }else{
            returnValue.put("code", 200);
            returnValue.put("bikeList", bikeList);
            returnValue.put("msg", "查询成功");
        }
        return returnValue;
    }

    //删除车辆信息
    @Operation(summary = "删除车辆信息")
    @PostMapping("/deleteBike")
    public JSONObject deleteBike(Integer id) {
        JSONObject returnValue = new JSONObject();
        int result = bikeService.deleteBike(id);
        if (result == 0) {
            returnValue.put("code", 500);
            returnValue.put("msg", "删除失败");
        } else {
            returnValue.put("code", 200);
            returnValue.put("msg", "删除成功");
        }
        return returnValue;
    }

    //更新操作
    @Operation(summary = "更新车辆信息")
    @PostMapping("/updateBike")
    public JSONObject updateBike( Bike bike){
        JSONObject returnValue = new JSONObject();
        int result = bikeService.updateBike(bike);
        if (result == 0) {
            returnValue.put("code", 500);
            returnValue.put("msg", "更新失败");
        } else {
            returnValue.put("code", 200);
            returnValue.put("msg", "更新成功");
        }
        return returnValue;
    }

    //添加操作
    @Operation(summary = "添加车辆信息")
    @PostMapping("/addBike")
    public JSONObject addBike(Bike bike){
        JSONObject returnValue = new JSONObject();
        int result = bikeService.addBike(bike);
        if (result == 0) {
            returnValue.put("code", 500);
            returnValue.put("msg", "添加失败");
        } else {
            returnValue.put("code", 200);
            returnValue.put("msg", "添加成功");
        }
        return returnValue;
    }

    //根据车辆编号查询车辆信息
    @Operation(summary = "根据车辆编号查询车辆信息")
    @PostMapping("/findBikeByNum")
    @ResponseBody
    public JSONObject findBikeByNum(String bikeNumber){
        JSONObject returnValue = new JSONObject();
        List<Bike> bike = bikeService.findBikeByNum(bikeNumber);
        if (bike==null) {
            returnValue.put("code", 500);
            returnValue.put("bike",null);
            returnValue.put("msg", "查询失败");
        } else {
            returnValue.put("code", 200);
            returnValue.put("bike",bike);
            returnValue.put("msg", "查询成功");
        }
        return returnValue;
    }

}


