package com.example.maintenance.service;


import com.example.maintenance.bean.Maintenance;
import com.example.maintenance.mapper.MaintenanceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaintenanceService {
    @Autowired
    private MaintenanceMapper maintenanceMapper;

    public List<Maintenance> showMaintenance(){
        return maintenanceMapper.showMaintenance();
    }

    public int deleteMaintenance(Integer id){
        return maintenanceMapper.deleteMaintenance(id);
    }

    public int updateMaintenance(Maintenance maintenance){
        return maintenanceMapper.updateMaintenance(maintenance);
    }

    public int addMaintenance(Maintenance maintenance){
        return maintenanceMapper.addMaintenance(maintenance);
    }

    public List<Maintenance> findMaintenanceByNum(String bikeNumber){
        return maintenanceMapper.findMaintenanceByNum(bikeNumber);
    }
}
