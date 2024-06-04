package com.train.campussharedbikeserverfence.controller;

import com.train.campussharedbikeserverfence.entity.Fence;
import com.train.campussharedbikeserverfence.service.FenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "围栏管理", description = "有关围栏的相关信息管理")
@RestController
@RequestMapping("/fence")
public class FenceController {
    @Autowired
    private FenceService fenceService;

    @PostMapping("/save")
    @Operation(summary = "保存围栏信息", description = "传入一个新的围栏信息进行保存")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功"),
    })
    public ResponseEntity<Map<String, Object>> saveFence(@RequestBody Fence fence) {
        Map<String, Object> response = new HashMap<>();
        try {
            fenceService.saveFence(fence);
            response.put("code", 0);
            response.put("data", fence);
            response.put("msg", "保存成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 1);
            response.put("data", -1);
            response.put("msg", "保存失败: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/list")
    @Operation(summary = "查询围栏信息列表", description = "传入页码和每页大小，返回围栏信息列表")
    @Parameters({
            @Parameter(name = "page", description = "页码", required = true),
            @Parameter(name = "size", description = "每页大小", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功"),
    })
    public ResponseEntity<Map<String, Object>> fenceList(@RequestParam(defaultValue = "1") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Fence> fenceList = fenceService.getFenceList(page, size);
            int fenceCount = fenceService.getFenceCount();
            Map<String, Object> data = new HashMap<>();
            data.put("list", fenceList);
            data.put("count", fenceCount);
            response.put("code", 0);
            response.put("data", data);
            response.put("msg", "查询成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("code", 1);
            response.put("data", -1);
            response.put("msg", "查询失败: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    @Operation(summary = "更新围栏信息", description = "传入围栏信息进行更新")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功"),
    })
    public ResponseEntity<Map<String, Object>> updateFence(@RequestBody Fence fence) {
        Map<String, Object> response = new HashMap<>();
        try {
            fenceService.updateFence(fence);
            response.put("code", 0);
            response.put("data", fence);
            response.put("msg", "围栏更新成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 1);
            response.put("data", -1);
            response.put("msg", "围栏更新失败: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("delete")
    @Operation(summary = "删除围栏", description = "根据围栏ID删除围栏")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功"),
            @ApiResponse(responseCode = "404", description = "围栏不存在"),
            @ApiResponse(responseCode = "400", description = "无效请求")
    })
    public ResponseEntity<Map<String, Object>> deleteFence(@RequestParam(defaultValue = "-1") int id) {
        // id不存在时返回400 无效请求
        if (id <= 0) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 1);
            response.put("data", null);
            response.put("msg", "参数错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        if (fenceService.getFence(id) != null) {
            Map<String, Object> response = new HashMap<>();
            try {
                fenceService.deleteFence(id);
                response.put("code", 0);
                response.put("data", null);
                response.put("msg", "围栏删除成功");
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                response.put("code", 1);
                response.put("data", -1);
                response.put("msg", "围栏删除失败: " + e.getMessage());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 1);
            response.put("data", null);
            response.put("msg", "围栏不存在");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/check")
    @Operation(summary = "传入位置检查是否在围栏内", description = "根据传入的经纬度检查是否存在某一围栏")
    @Parameters({
            @Parameter(name = "longitude", description = "经度", required = true),
            @Parameter(name = "latitude", description = "纬度", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功"),
    })
    public ResponseEntity<Map<String, Object>> checkFence(@RequestParam(defaultValue = "0") double longitude,
                                                          @RequestParam(defaultValue = "0") double latitude) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean isInFence = fenceService.checkInFence(longitude, latitude);
            Map<String, Object> data = new HashMap<>();
            data.put("isInFence", isInFence);
            response.put("code", 0);
            response.put("data", data);
            response.put("msg", "检查成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 1);
            response.put("data", -1);
            response.put("msg", "检查失败: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/graph-data")
    @Operation(summary = "获取围栏图表数据", description = "获取围栏图表数据")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功"),
    })
    public ResponseEntity<Map<String, Object>> getGraphData() {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("code", 0);
            response.put("data", fenceService.getGraphData());
            response.put("msg", "获取图表数据成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            response.put("code", 1);
            response.put("data", -1);
            response.put("msg", "获取图表数据失败: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
