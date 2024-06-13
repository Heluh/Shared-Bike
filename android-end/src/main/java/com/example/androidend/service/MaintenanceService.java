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
        try{
            return maintenanceMapper.findById(id);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public List<Maintenance> findAll() {
        try {
            return maintenanceMapper.findAll();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public List<Maintenance> findByUsername(String username) {
        try{
            return maintenanceMapper.findByUsername(username);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }


    public Maintenance save(Maintenance maintenance) {
        try{
            maintenanceMapper.save(maintenance);
            return maintenance;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public void deleteById(Long id) {
        try{
            maintenanceMapper.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}