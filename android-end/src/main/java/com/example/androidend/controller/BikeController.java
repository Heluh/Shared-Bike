package com.example.androidend.controller;

import com.example.androidend.annotation.LoginUser;
import com.example.androidend.entity.Bike;
import com.example.androidend.entity.Coordinates;
import com.example.androidend.entity.User;
import com.example.androidend.service.BikeService;
import com.example.androidend.service.RecordService;
import com.example.androidend.utils.R;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.androidend.entity.Record;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/bikes")
public class BikeController {
    @Autowired
    private BikeService bikeService;

    @Autowired
    private RecordService recordService;

    @GetMapping("/findAll")
    public R findAll() {
        List<Bike> bikes = bikeService.findAll();
        return R.ok().put("data", bikes);
    }

    @GetMapping
    public R findByBikeNumber(String bikeNumber) {
        try{
            Bike bike = bikeService.findByBikeNumber(bikeNumber);
            if (bike != null) {
                return R.ok().put("data", bike);
            } else {
                return R.error("未找到该自行车");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.error("查询失败");
        }

    }

    @PostMapping("/unlock")
    public R unlock(@LoginUser User user,@RequestParam String bikeNumber) {
        try{
            // 检查自行车是否可以使用
            Bike bike = bikeService.findByBikeNumber(bikeNumber);

            if (bike == null) {
                return R.error(404, "未找到该自行车");
            }

            if (bike.getStatus() != Bike.STATUS_AVAILABLE) {
                return R.error(409, "自行车正在被使用");
            }

            // 判断用户是否有正在骑行的记录
            Record record = recordService.findByUsernameAndStatus(user.getUsername(), (byte) 0);
            if (record != null) {
                return R.error(409, "用户有正在骑行的记录");
            }

            bikeService.unlock(bikeNumber);

            // 创建新的骑行记录
            Record newRecord = new Record();
            newRecord.setUsername(user.getUsername());
            newRecord.setBikeNum(bikeNumber);
            newRecord.setDistance(0.0);
            newRecord.setTime(new Date());
            newRecord.setDuration(0);
            newRecord.setStatus((byte) 0); // 表示骑行进行中
            newRecord.setContrail("[]"); // 初始化为空的 JSON 数组
            recordService.save(newRecord);
            return R.ok().put("data", bike);
        }catch (Exception e){
            e.printStackTrace();
            return R.error("解锁失败");
        }

    }

    @PostMapping("/lock")
    public R lock(String bikeNumber, Double longitude, Double latitude) {
        try{
            if(bikeService.canReturnBike(bikeNumber, longitude, latitude)){
                bikeService.lock(bikeNumber);

                // 结束骑行记录
                Record record = recordService.findByBikeNumberAndStatus(bikeNumber, (byte) 0);
                if (record != null) {
                    record.setDistance(calculateTotalDistance(record.getContrail()));
                    record.setDuration(calculateDuration(record.getTime(), new Date()));
                    record.setStatus((byte) 1); // 表示骑行已结束
                    recordService.update(record);
                }
                return R.ok();
            }else{
                return R.error(400, "不在指定区域内");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.error("锁车失败");
        }

    }

    private double calculateTotalDistance(String contrailJson) {
        try {
            // 反序列化轨迹数据并计算总距离
            List<Coordinates> contrail = new ObjectMapper().readValue(contrailJson, new TypeReference<List<Coordinates>>() {});
            // 实现计算总距离的逻辑
            double totalDistance = 0.0;
            for (int i = 1; i < contrail.size(); i++) {
                totalDistance += distanceBetween(contrail.get(i - 1), contrail.get(i));
            }
            return totalDistance;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    private double distanceBetween(Coordinates c1, Coordinates c2) {
        // 使用 Haversine 公式计算两个坐标点之间的距离
        final int R = 6371; // 地球半径，单位：千米
        double latDistance = Math.toRadians(c2.getLatitude() - c1.getLatitude());
        double lonDistance = Math.toRadians(c2.getLongitude() - c1.getLongitude());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(c1.getLatitude())) * Math.cos(Math.toRadians(c2.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // 返回距离，单位：千米
    }

    private long calculateDuration(Date startTime, Date endTime) {
        return (endTime.getTime() - startTime.getTime()) / 1000; // 持续时间，单位：秒
    }

}
