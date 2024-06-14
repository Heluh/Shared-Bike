package com.example.androidend.controller;

import com.example.androidend.annotation.LoginUser;
import com.example.androidend.entity.User;
import com.example.androidend.service.RecordService;
import com.example.androidend.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/record")
public class RecordController {
    @Autowired
    private RecordService recordService;

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

}
