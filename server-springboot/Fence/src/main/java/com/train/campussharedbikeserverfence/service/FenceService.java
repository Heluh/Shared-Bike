package com.train.campussharedbikeserverfence.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.train.campussharedbikeserverfence.entity.Fence;

import java.util.List;
import java.util.Map;

public interface FenceService {
    public void saveFence(Fence fence);

    public void updateFence(Fence fence);

    public void deleteFence(int id);

    public List<Fence> getFenceList(int page, int size);

    public Fence getFence(int id);

    public int getFenceCount();

    public boolean checkInFence(double longitude, double latitude);

    public List<Map<String, Object>> getGraphData();
}
