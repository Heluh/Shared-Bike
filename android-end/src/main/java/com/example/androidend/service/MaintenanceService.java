package com.example.androidend.service;

import com.example.androidend.entity.Maintenance;
import com.example.androidend.mapper.MaintenanceMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaintenanceService {

    @Resource
    private MaintenanceMapper maintenanceMapper;


    public Maintenance findById(Long id) {
        return maintenanceMapper.findById(id);
    }


    public List<Maintenance> findAll() {
        return maintenanceMapper.findAll();
    }


    public Maintenance save(Maintenance maintenance) {
        maintenanceMapper.save(maintenance);
        return maintenance;
    }


    public void deleteById(Long id) {
        maintenanceMapper.deleteById(id);
    }
}