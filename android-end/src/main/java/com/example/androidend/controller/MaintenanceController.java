package com.example.androidend.controller;

import com.example.androidend.annotation.LoginUser;
import com.example.androidend.entity.Maintenance;
import com.example.androidend.entity.User;
import com.example.androidend.service.MaintenanceService;
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
    public ResponseEntity<Maintenance> findById(@PathVariable Long id) {
        Maintenance maintenance = maintenanceService.findById(id);
        if (maintenance != null) {
            return ResponseEntity.ok(maintenance);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Maintenance>> findAll() {
        List<Maintenance> maintenances = maintenanceService.findAll();
        return ResponseEntity.ok(maintenances);
    }

    @PostMapping
    public ResponseEntity<Maintenance> save(@RequestBody Maintenance maintenance,
                                            @LoginUser User user) {
        maintenance.setUsername(user.getUsername());
        Maintenance savedMaintenance = maintenanceService.save(maintenance);
        return ResponseEntity.ok(savedMaintenance);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        maintenanceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}