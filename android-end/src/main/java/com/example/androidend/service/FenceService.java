package com.example.androidend.service;

import com.example.androidend.entity.Fence;
import com.example.androidend.mapper.FenceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FenceService {
    @Autowired
    private FenceMapper fenceMapper;

    public List<Fence> findAll() {
        try{
            return fenceMapper.getFenceList();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
