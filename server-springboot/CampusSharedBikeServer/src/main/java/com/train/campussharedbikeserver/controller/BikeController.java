package com.train.campussharedbikeserver.controller;

import com.train.campussharedbikeserver.bean.Bike;
import com.train.campussharedbikeserver.server.BikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "显示界面——单车管理", description = "地图展示界面调用，根据查找条件返回对应的单车信息列表")
@RestController
@RequestMapping("/bike")
public class BikeController {
    @Autowired
    private BikeService bikeService;

    /**
     * 查询某个围栏内的所有单车
     *
     * @param fenceId 围栏ID
     * @return 包含查询结果的Map对象
     */
    @Operation(summary = "根据围栏ID查询单车列表")
    @ApiResponse(responseCode = "200", description = "成功查询单车列表",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Map.class)))
    @GetMapping(value = "/selectBikeByFenceId")
    public ResponseEntity<Map<String, Object>> selectBikeByFenceId(
            @Parameter(description = "围栏ID", required = true, example = "1")
            @RequestParam(value = "fenceId") int fenceId) {
        List<Bike> bikeList = bikeService.selectBikeByFenceId(fenceId);
        Map<String, Object> response = new HashMap<>();
        if (bikeList == null) {
            response.put("code", 0);
            response.put("bikeList", null);
            response.put("msg", "查询失败");
        } else {
            response.put("code", 1);
            response.put("bikeList", bikeList);
            response.put("msg", "查询成功");
        }
        return ResponseEntity.ok(response);
    }
}
