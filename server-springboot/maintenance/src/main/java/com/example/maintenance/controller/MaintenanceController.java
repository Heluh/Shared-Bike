package com.example.maintenance.controller;

import com.alibaba.fastjson.JSONObject;

import com.example.maintenance.bean.Maintenance;
import com.example.maintenance.service.MaintenanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "MaintenanceController", description = "维修信息管理")
@RestController
@RequestMapping("/maintenance")
public class MaintenanceController {

    @Autowired
    private MaintenanceService maintenanceService;

    //显示所有维修的信息
    @Operation(summary = "显示所有维修的信息")
    @GetMapping(value = "/showMaintenance")
    public JSONObject showMaintenance(){
        JSONObject returnValue = new JSONObject();
        List<Maintenance> maintenanceList = maintenanceService.showMaintenance();
        if (maintenanceList == null){
            returnValue.put("code", 500);
            returnValue.put("maintenanceList", null);
            returnValue.put("msg", "查询失败");
        }else{
            returnValue.put("code", 200);
            returnValue.put("maintenanceList", maintenanceList);
            returnValue.put("msg", "查询成功");
        }
        return returnValue;
    }

    //删除维修信息
    @Operation(summary = "删除维修信息")
    @PostMapping("/deleteMaintenance")
    public JSONObject deleteMaintenance(Integer id) {
        JSONObject returnValue = new JSONObject();
        int result = maintenanceService.deleteMaintenance(id);
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
    @Operation(summary = "更新维修信息")
    @PostMapping("/updateMaintenance")
    public JSONObject updateMaintenance( Maintenance maintenance){
        JSONObject returnValue = new JSONObject();
        int result = maintenanceService.updateMaintenance(maintenance);
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
    @Operation(summary = "添加维修信息")
    @PostMapping("/addMaintenance")
    public JSONObject addMaintenance(Maintenance maintenance){
        JSONObject returnValue = new JSONObject();
        int result = maintenanceService.addMaintenance(maintenance);
        if (result == 0) {
            returnValue.put("code", 500);
            returnValue.put("msg", "添加失败");
        } else {
            returnValue.put("code", 200);
            returnValue.put("msg", "添加成功");
        }
        return returnValue;
    }

    //根据车辆编号查询维修信息
    @Operation(summary = "根据车辆编号查询维修信息")
    @PostMapping("/findMaintenanceByNum")
    @ResponseBody
    public JSONObject findMaintenanceByNum(String bikeNumber){
        JSONObject returnValue = new JSONObject();
        List<Maintenance> maintenanceList = maintenanceService.findMaintenanceByNum(bikeNumber);
        if (maintenanceList == null) {
            returnValue.put("code", 500);
            returnValue.put("maintenanceList",null);
            returnValue.put("msg", "查询失败");
        } else {
            returnValue.put("code", 200);
            returnValue.put("maintenanceList",maintenanceList);
            returnValue.put("msg", "查询成功");
        }
        return returnValue;
    }
}
