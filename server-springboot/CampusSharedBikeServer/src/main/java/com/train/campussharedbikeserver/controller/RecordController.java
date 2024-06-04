package com.train.campussharedbikeserver.controller;

import com.train.campussharedbikeserver.bean.Fence;
import com.train.campussharedbikeserver.bean.Record;
import com.train.campussharedbikeserver.server.RecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "显示界面——轨迹记录管理", description = "提供查询用户轨迹记录的相关接口")
@RestController
@RequestMapping("/record")
public class RecordController {
    @Autowired
    private RecordService recordService;

    /**
     * 获取所有记录
     *
     * @return 包含查询结果的Map对象
     */
    @Operation(summary = "获取所有记录")
    @ApiResponse(responseCode = "200", description = "成功获取所有记录",
            content = @Content(mediaType = "application/json"))
    @GetMapping("/getAllRecord")
    public ResponseEntity<Map<String, Object>> getAllRecord() {
        List<Record> recordList = recordService.getAllRecord();

        Map<String, Object> response = new HashMap<>();
        if (recordList == null) {
            response.put("code", 0);
            response.put("recordList", null);
            response.put("msg", "查询失败");
        } else {
            response.put("code", 1);
            response.put("recordList", recordList);
            response.put("msg", "查询成功");
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
