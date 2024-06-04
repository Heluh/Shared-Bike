package com.train.campussharedbikeservercharity.controller;

import com.train.campussharedbikeservercharity.entity.BikeFromCharity;
import com.train.campussharedbikeservercharity.entity.CharityEventWithBikeCount;
import com.train.campussharedbikeservercharity.service.CharityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import com.train.campussharedbikeservercharity.entity.CharityEvent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "捐赠车辆管理", description = "有关捐赠车辆的接口")
@RestController
@RequestMapping("/charity/bike")
public class BikeController {
    @Autowired
    private CharityService charityService;

    @GetMapping("/bind-list")
    @Operation(summary = "查询已入库绑定运营列表", description = "传入页码和每页大小，返回卷层信息列表")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功")
    })
    public ResponseEntity<Map<String, Object>> getBikesWithNullId(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<BikeFromCharity> bikeList = charityService.getBikesWithNonNullId(page, size);
            int totalCount = charityService.getBikesWithNonNullIdCount();

            Map<String, Object> data = new HashMap<>();
            data.put("list", bikeList);
            data.put("count", totalCount);

            response.put("code", 0);
            response.put("data", data);
            response.put("msg", "获取自行车已入库绑定运营列表成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 1);
            response.put("data", -1);
            response.put("msg", "获取自行车已入库绑定运营列表失败: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/in-list")
    @Operation(summary = "查询已入库未绑定运营列表", description = "传入页码和每页大小，返回卷层信息列表")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功")
    })
    public ResponseEntity<Map<String, Object>> getBikesWithNotNullId(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<BikeFromCharity> bikeList = charityService.getBikesWithNullId(page, size);
            int totalCount = charityService.getBikesWithNullIdCount();

            Map<String, Object> data = new HashMap<>();
            data.put("list", bikeList);
            data.put("count", totalCount);

            response.put("code", 0);
            response.put("data", data);
            response.put("msg", "获取自行车已入库未绑定运营列表成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 1);
            response.put("data", -1);
            response.put("msg", "获取自行车已入库未绑定运营列表失败: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除车辆", description = "根据车辆ID删除车辆")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功"),
            @ApiResponse(responseCode = "404", description = "车辆不存在"),
            @ApiResponse(responseCode = "400", description = "无效请求")
    })
    public ResponseEntity<Map<String, Object>> deleteCharityBikeById(@RequestParam(defaultValue = "-1") int id) {
        Map<String, Object> response = new HashMap<>();
        try {
            charityService.deleteBike(id);
            response.put("code", 0);
            response.put("data", null);
            response.put("msg", "删除车辆成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 1);
            response.put("data", -1);
            response.put("msg", "删除车辆失败: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/add")
    @Operation(summary = "添加车辆", description = "添加车辆")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功"),
            @ApiResponse(responseCode = "400", description = "无效请求")
    })
    public ResponseEntity<Map<String, Object>> addBikeDonation(@RequestBody BikeFromCharity bike) {
        Map<String, Object> response = new HashMap<>();
        try {
            charityService.addBikeDonation(bike);
            response.put("code", 0);
            response.put("data", null);
            response.put("msg", "捐赠车辆成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 1);
            response.put("data", -1);
            response.put("msg", "捐赠车辆失败: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/bind")
    @Operation(summary = "绑定车辆", description = "绑定车辆到指定的运营区域")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功"),
            @ApiResponse(responseCode = "400", description = "无效请求")
    })
    public ResponseEntity<Map<String, Object>>  bindBikeToFence(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            int bikeId = (int) request.get("bike_id");
            int fenceId = (int) request.get("fence_id");

            charityService.insertBikeAndUpdateBinding(bikeId, fenceId);

            response.put("code", 0);
            response.put("data", null);
            response.put("msg", "捐赠车辆转运营成功");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 1);
            response.put("data", -1);
            response.put("msg", "捐赠车辆转运营失败: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
