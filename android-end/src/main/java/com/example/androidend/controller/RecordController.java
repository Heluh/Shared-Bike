package com.example.androidend.controller;

import com.example.androidend.annotation.LoginUser;
import com.example.androidend.entity.Bike;
import com.example.androidend.entity.BikeLocation;
import com.example.androidend.entity.User;
import com.example.androidend.service.BikeService;
import com.example.androidend.service.RecordService;
import com.example.androidend.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.androidend.entity.Record;

import java.util.List;

@RestController
@RequestMapping("/record")
public class RecordController {
    @Autowired
    private RecordService recordService;

    @Autowired
    private BikeService bikeService;

    /**
     * 根据用户名获取所有记录
     *
     * @return 包含查询结果的Map对象
     */
    @GetMapping
    public R getAllRecord(@LoginUser User user) {
        List<Record> recordList = recordService.findByUserName(user.getUsername());

        if (recordList == null) {
            return R.error("查询失败");
        } else {
            return R.ok().put("data", recordList);
        }
    }

    /**
     * 实时记录用户的轨迹
     */
    @PostMapping("/update")
    public R updateLocation(@RequestBody BikeLocation bikeLocation) {
        try {
            Record record = recordService.findByBikeNumberAndStatus(bikeLocation.getBikeNumber(), (byte) 0);
            if (record == null) {
                return R.error("骑行记录不存在");
            }
            recordService.updateContrail(record.getId(), bikeLocation.getCoordinates());
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("记录骑行轨迹失败");
        }
        return R.ok("记录骑行轨迹成功");
    }

    /**
     * 查询用户还未结束的骑行记录
     */
    @GetMapping("/current")
    public R getCurrentRecord(@LoginUser User user) {
        Record record = recordService.findByUsernameAndStatus(user.getUsername(), (byte) 0);
        if (record == null) {
            return R.error("未找到用户的骑行记录");
        } else {
            Bike bike = bikeService.findByBikeNumber(record.getBikeNum());
            return R.ok().put("data", bike);
        }
    }


}
