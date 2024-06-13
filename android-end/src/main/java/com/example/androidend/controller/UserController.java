package com.example.androidend.controller;

import com.example.androidend.annotation.IgnoreAuth;
import com.example.androidend.annotation.LoginUser;
import com.example.androidend.entity.User;
import com.example.androidend.service.UserService;
import com.example.androidend.utils.JwtUtil;
import com.example.androidend.utils.R;
import com.example.androidend.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户 后端接口
 */
@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private RedisUtil redisUtil;

	/**
	 * 登录
	 */
	@IgnoreAuth
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public R login(@RequestParam String username, @RequestParam String password) {
		System.out.println("username: " + username);
		User user = userService.selectUserByUsername(username);
		if (user == null) {
			return R.error("账号不存在");
		}

		// 创建BCryptPasswordEncoder对象
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		// 验证密码
		if (!passwordEncoder.matches(password, user.getPassword())) {
			return R.error("密码不正确");
		}

		String token = JwtUtil.generateToken(user.getId(), username, "users", "用户");
		redisUtil.set(token, user.getId(), 1, TimeUnit.HOURS);

		Map<String, Object> data = new HashMap<>();
		data.put("token", token);

		return R.ok().put("data", data);
	}

	/**
	 * 注册
	 */
	@IgnoreAuth
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public R register(@RequestBody User user) {
		User user1 = userService.selectUserByUsername(user.getUsername());
		if (user1 != null) {
			return R.error("注册用户已存在");
		}

		// 对密码进行加密
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);

		userService.save(user);
		return R.ok();
	}

	@GetMapping("/profile")
	public String userProfile(@LoginUser User user) {
		return "User: " + user.getUsername();
	}





}
