package com.example.login.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.login.bean.User;
import com.example.login.server.UserService;
import com.example.login.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Tag(name = "用户管理", description = "涉及登陆检查、和用户信息的增删改查工作")
@RestController
@RequestMapping("/account")
public class UserController {

    @Autowired
    private UserService userService; //注入UserService

    private final String serviceName = "UserService"; // 需要调用的服务名

    // 登陆检查
    @Operation(summary = "登陆检查", description = "检查用户名和密码是否正确")
    @Parameters({
                    @Parameter(name = "username", description = "用户名", required = true, example = "admin"),
                    @Parameter(name = "password", description = "密码", required = true, example = "123456"),
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "登陆成功"),
            @ApiResponse(responseCode = "400", description = "登陆失败")
    })
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        System.out.println("username: " + username);
        System.out.println("password: " + password);
        User user = userService.login(username,password);
        if (user == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", -1);
            response.put("data", new User());
            response.put("msg", "用户不存在或密码错误");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        String token = JwtUtil.createJWT(UUID.randomUUID().toString(), user.getUserAccount(), null);
        Map<String, Object> data = new HashMap<>();
        data.put("username", user.getUserAccount());
        data.put("password", user.getUserPassword());
        data.put("token", token);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("msg", "成功");
        response.put("data", data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
