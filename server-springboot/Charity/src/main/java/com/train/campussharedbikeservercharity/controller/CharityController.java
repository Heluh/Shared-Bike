package com.train.campussharedbikeservercharity.controller;

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

@Tag(name = "捐赠活动管理", description = "有关捐赠活动的接口")
@RestController
@RequestMapping("/charity/activity")
public class CharityController {
    @Autowired
    private CharityService charityService;

    @GetMapping("/all-list")
    @Operation(summary = "查询全部捐赠信息列表", description = "传入页码和每页大小，返回卷层信息列表")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功")
    })
    public ResponseEntity<Map<String, Object>> getAllCharityEvents(@RequestParam(defaultValue = "1") int page,
                                                                   @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<CharityEventWithBikeCount> charityEvents = charityService.getAllCharityEvents(page, size);
            int totalCount = charityService.getCharityEventCount();

            Map<String, Object> data = new HashMap<>();
            data.put("list", charityEvents);
            data.put("count", totalCount);

            response.put("code", 0);
            response.put("data", data);
            response.put("msg", "获取捐赠活动列表成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 1);
            response.put("data", -1);
            response.put("msg", "获取捐赠活动列表失败: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // 获取当前开放的捐赠活动
    @GetMapping("/open-list")
    @Operation(summary = "查询当前开放的捐赠信息列表", description = "传入页码和每页大小，返回卷层信息列表")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功")
    })
    public ResponseEntity<Map<String, Object>> getOpenCharityEvents(@RequestParam(defaultValue = "1") int page,
                                                                    @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<CharityEventWithBikeCount> openEvents = charityService.getOpenCharityEvents(page, size);
            int totalCount = charityService.getOpenCharityEventCount();

            Map<String, Object> data = new HashMap<>();
            data.put("list", openEvents);
            data.put("count", totalCount);

            response.put("code", 0);
            response.put("data", data);
            response.put("msg", "获取当前开放的捐赠活动成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 1);
            response.put("data", null);
            response.put("msg", "获取当前开放的捐赠活动失败: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/one")
    @Operation(summary = "获取捐赠活动详情", description = "根据捐赠活动ID获取捐赠活动详情")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功"),
            @ApiResponse(responseCode = "404", description = "捐赠活动不存在")
    })
    public ResponseEntity<Map<String, Object>> getCharityEventById(@RequestParam(defaultValue = "-1") int id) {
        Map<String, Object> response = new HashMap<>();
        if (id == -1) {
            response.put("code", 1);
            response.put("data", null);
            response.put("msg", "捐赠活动ID不能为空");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            CharityEvent charityEvent = charityService.getCharityEventById(id);
            if (charityEvent != null) {
                response.put("code", 0);
                response.put("data", charityEvent);
                response.put("msg", "获取捐赠活动详情成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("code", 1);
                response.put("data", null);
                response.put("msg", "捐赠活动不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("code", 1);
            response.put("data", -1);
            response.put("msg", "获取捐赠活动详情失败: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除捐赠活动", description = "根据捐赠活动ID删除捐赠活动")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功"),
            @ApiResponse(responseCode = "404", description = "捐赠活动不存在"),
            @ApiResponse(responseCode = "400", description = "无效请求")
    })
    public ResponseEntity<Map<String, Object>> deleteCharityEventById(@RequestParam(defaultValue = "-1") int id) {
        Map<String, Object> response = new HashMap<>();
        try {
            charityService.deleteCharityEventById(id);
            response.put("code", 0);
            response.put("data", null);
            response.put("msg", "删除捐赠活动成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 1);
            response.put("data", -1);
            response.put("msg", "删除捐赠活动失败: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/save")
    @Operation(summary = "保存捐赠活动", description = "传入一个新的捐赠活动进行保存")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功")
    })
    public ResponseEntity<Map<String, Object>> insertCharityEvent(@RequestBody CharityEvent charityEvent) {
        Map<String, Object> response = new HashMap<>();
        try {
            charityService.insertCharityEvent(charityEvent);
            response.put("code", 0);
            response.put("data", charityEvent);
            response.put("msg", "保存捐赠活动成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 1);
            response.put("data", -1);
            response.put("msg", "保存捐赠活动失败: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    @Operation(summary = "更新捐赠活动", description = "根据捐赠活动ID更新捐赠活动")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功"),
            @ApiResponse(responseCode = "404", description = "捐赠活动不存在"),
            @ApiResponse(responseCode = "400", description = "无效请求")
    })
    public ResponseEntity<Map<String, Object>> updateCharityEvent(@RequestBody CharityEvent charityEvent) {
        Map<String, Object> response = new HashMap<>();
        try {
            charityService.updateCharityEvent(charityEvent);
            response.put("code", 0);
            response.put("data", charityEvent);
            response.put("msg", "更新捐赠活动成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 1);
            response.put("data", -1);
            response.put("msg", "更新捐赠活动失败: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
