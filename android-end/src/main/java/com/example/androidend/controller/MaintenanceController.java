package com.example.androidend.controller;

import com.example.androidend.annotation.LoginUser;
import com.example.androidend.entity.Maintenance;
import com.example.androidend.entity.User;
import com.example.androidend.service.MaintenanceService;
import com.example.androidend.utils.R;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/maintenance")
public class MaintenanceController {

    @Resource
    MaintenanceService maintenanceService;


    @GetMapping("/{id}")
    public R findById(@PathVariable Long id) {
        Maintenance maintenance = maintenanceService.findById(id);
        if (maintenance != null) {
            return R.ok().put("data", maintenance);
        } else {
            return R.error("Maintenance not found");
        }
    }

    @GetMapping("/all")
    public R findAll() {
        List<Maintenance> maintenances = maintenanceService.findAll();
        return R.ok().put("data", maintenances);
    }

    @GetMapping
    public R findByUsername( @LoginUser User user) {
        String username = user.getUsername();
        List<Maintenance> maintenances = maintenanceService.findByUsername(username);
        return R.ok().put("data", maintenances);
    }

    @PostMapping
    public R save(@RequestBody Maintenance maintenance,
                  @LoginUser User user) {
        maintenance.setUsername(user.getUsername());
        Maintenance savedMaintenance = maintenanceService.save(maintenance);
        return R.ok().put("data", savedMaintenance);
    }

    @DeleteMapping("/{id}")
    public R deleteById(@PathVariable Long id) {
        maintenanceService.deleteById(id);
        return R.ok();
    }
}