package com.train.campussharedbikeserver.controller;


import com.train.campussharedbikeserver.bean.Fence;
import com.train.campussharedbikeserver.server.FenceService;
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

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "显示界面——围栏管理", description = "地图展示界面调用，根据查找条件返回对应的围栏信息列表")
@RestController
@RequestMapping("/fence")
public class FenceController {
    @Autowired
    private FenceService fenceService;

    /**
     * 从数据库中查询并返回全部的围栏信息
     *
     * @return 包含查询结果的Map对象
     */
    @Operation(summary = "查询全部围栏信息")
    @ApiResponse(responseCode = "200", description = "成功查询围栏列表",
            content = @Content(mediaType = "application/json"))
    @GetMapping("/selectAllFence")
    public ResponseEntity<Map<String, Object>> selectAllFence() {
        List<Fence> fenceList = fenceService.selectAllFence();

        Map<String, Object> response = new HashMap<>();
        if (fenceList == null) {
            response.put("code", 0);
            response.put("fenceList", null);
            response.put("msg", "查询失败");
        } else {
            // 按照 Person 对象的 age 属性进行排序
            fenceList.sort(new Comparator<Fence>() {
                @Override
                public int compare(Fence p1, Fence p2) {
                    return Integer.compare(p1.getType(), p2.getType());
                }
            });
            response.put("code", 1);
            response.put("fenceList", fenceList);
            response.put("msg", "查询成功");
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
