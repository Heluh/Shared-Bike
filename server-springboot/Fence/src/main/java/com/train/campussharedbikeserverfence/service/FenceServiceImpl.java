package com.train.campussharedbikeserverfence.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.train.campussharedbikeserverfence.entity.Fence;
import com.train.campussharedbikeserverfence.entity.FenceType2MaxCount;
import com.train.campussharedbikeserverfence.entity.FenceTypeCount;
import com.train.campussharedbikeserverfence.mapper.FenceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.train.campussharedbikeserverfence.utils.PolygonUtil.isPointInsidePolygon;

@Service
public class FenceServiceImpl implements FenceService{
    @Autowired
    private FenceMapper fenceMapper;

    @Override
    public void saveFence(Fence fence) {
        // 设置create_date属性为当前时间（根据具体需求进行设置）
        fence.setCreate_date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        fenceMapper.insertFence(fence);
    }

    @Override
    public void updateFence(Fence fence) {
        fence.setCreate_date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        fenceMapper.updateFence(fence);
    }

    @Override
    public void deleteFence(int id) {
        fenceMapper.deleteFence(id);
    }

    @Override
    public List<Fence> getFenceList(int page, int size) {
        return fenceMapper.getFenceList((page - 1) * size, size);
    }

    @Override
    public Fence getFence(int id) {
        return fenceMapper.getFence(id);
    }

    @Override
    public int getFenceCount() {
        return fenceMapper.getFenceCount();
    }

    @Override
    public boolean checkInFence(double longitude, double latitude) {
        List<Fence> fenceList = fenceMapper.getFenceList(0, 1000);
        boolean type1Ok = false;
        boolean type2Ok = false;
        for (Fence fence : fenceList) {
            if (type1Ok && type2Ok) {
                return true;
            }
            if (!type1Ok && fence.getType() == 1) {
                if (isPointInsidePolygon(longitude, latitude, fence.getFence_points())) {
                    type1Ok = true;
                }
            } else if (!type2Ok && fence.getType() == 2) {
                if (isPointInsidePolygon(longitude, latitude, fence.getFence_points())) {
                    type2Ok = true;
                }
            }
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> getGraphData() {
        Map<String, Object> typeCountData = new HashMap<>();
        typeCountData.put("titleText", "围栏类型统计");
        typeCountData.put("titleSubtext", "数据来自全部围栏");
        typeCountData.put("legendData", new String[]{"车辆运营区", "指定停车区"});
        typeCountData.put("seriesName", "围栏类型");

        List<FenceTypeCount> fenceTypeCount = fenceMapper.getFenceTypeCount();
        FenceTypeCount type1Data = fenceTypeCount.get(0);
        FenceTypeCount type2Data = fenceTypeCount.get(1);
        Map<String, Object> type1DataMap = new HashMap<>();
        type1DataMap.put("name", "车辆运营区");
        Map<String, Object> type2DataMap = new HashMap<>();
        type2DataMap.put("name", "指定停车区");
        if (type1Data.getType() == 1) {
            type1DataMap.put("value", type1Data.getCount());
            type2DataMap.put("value", type2Data.getCount());
        } else {
            type1DataMap.put("value", type1Data.getCount());
            type2DataMap.put("value", type2Data.getCount());
        }

        List<Map<String, Object>> seriesData = new java.util.ArrayList<>();
        seriesData.add(type1DataMap);
        seriesData.add(type2DataMap);
        typeCountData.put("seriesData", seriesData);

        Map<String, Object> type2MaxSeatData = new HashMap<>();
        type2MaxSeatData.put("titleText", "指定停车区统计");
        type2MaxSeatData.put("titleSubtext", "数据来自全部围栏");
        type2MaxSeatData.put("seriesName", "指定停车区");

        List<FenceType2MaxCount> type2MaxSeatSeriesData = fenceMapper.getFenceType2MaxCount();
        seriesData = new java.util.ArrayList<>();
        for (FenceType2MaxCount row : type2MaxSeatSeriesData) {
            Map<String, Object> rowMap = new HashMap<>();
            rowMap.put("name", row.getFenceName());
            rowMap.put("value", row.getCapacity());
            seriesData.add(rowMap);
        }
        type2MaxSeatData.put("seriesData", seriesData);

        List<String> legendData = new java.util.ArrayList<>();
        for (FenceType2MaxCount row : type2MaxSeatSeriesData) {
            legendData.add(row.getFenceName());
        }
        type2MaxSeatData.put("legendData", legendData);

        List<Map<String, Object>> result = new java.util.ArrayList<>();
        result.add(typeCountData);
        result.add(type2MaxSeatData);

        return result;
    }
}
