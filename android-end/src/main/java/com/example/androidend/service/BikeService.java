package com.example.androidend.service;

import com.example.androidend.entity.Bike;
import com.example.androidend.entity.Fence;
import com.example.androidend.mapper.BikeMapper;
import com.example.androidend.mapper.FenceMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BikeService {
    @Autowired
    private BikeMapper bikeMapper;

    @Autowired
    private FenceMapper fenceMapper;


    public List<Bike> findAll() {
        try {
            return bikeMapper.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bike findByBikeNumber(String bikeNumber) {
        try {
            return bikeMapper.findBikeByNumber(bikeNumber);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void unlock(String bikeNumber) {
        try {
            bikeMapper.unlock(bikeNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void lock(String bikeNumber) {
        try {
            bikeMapper.lock(bikeNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean canReturnBike(String bikeNumber, Double longitude, Double latitude) {
        // 根据bikeNumber查找对应的车辆和它的所属区域
        Bike bike = bikeMapper.findBikeByNumber(bikeNumber);
        if (bike == null) {
            throw new RuntimeException("Bike not found");
        }

        // 根据车辆所属区域查找对应的围栏
        Fence fence = fenceMapper.getFenceById(bike.getFenceId());

        // 解析围栏的点
        List<double[]> polygon = parseFencePoints(fence.getFence_points());

        // 判断车辆是否在围栏内
        return isPointInPolygon(longitude, latitude, polygon);
    }


    public List<double[]> parseFencePoints(String fencePoints) {
        List<double[]> polygon = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            // 解析 JSON 字符串为 List<Map<String, Double>>
            List<Map<String, Double>> points = mapper.readValue(fencePoints, new TypeReference<List<Map<String, Double>>>() {});
            // 将每个点的经度和纬度添加到 polygon 列表中
            for (Map<String, Double> point : points) {
                double longitude = point.get("lng");
                double latitude = point.get("lat");
                polygon.add(new double[]{longitude, latitude});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return polygon;
    }

    public boolean isPointInPolygon(double px, double py, List<double[]> polygon) {
        int count = 0;
        int n = polygon.size();
        double[] p1, p2;

        for (int i = 0, j = n - 1; i < n; j = i++) {
            p1 = polygon.get(i);
            p2 = polygon.get(j);
            if (p1[1] < py && p2[1] >= py || p2[1] < py && p1[1] >= py) {
                if (p1[0] + (py - p1[1]) / (p2[1] - p1[1]) * (p2[0] - p1[0]) < px) {
                    count++;
                }
            }
        }
        return count % 2 == 1;
    }
}
