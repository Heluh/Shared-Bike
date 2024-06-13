package com.example.androidend.controller;

import com.example.androidend.entity.Fence;
import com.example.androidend.service.FenceService;
import com.example.androidend.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fence")
public class FenceController {
    @Autowired
    private FenceService fenceService;

    @RequestMapping("/list")
    public R findAll() {
        List<Fence> fences = fenceService.findAll();
        return R.ok().put("data", fences);
    }
}
