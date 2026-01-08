package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constants.ResMessage;
import com.example.demo.dto.UserInfoDto;
import com.example.demo.dto.UserPasswordDto;
import com.example.demo.request.UserAddReq;
import com.example.demo.request.UserLoginReq;
import com.example.demo.response.BasicRes;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@CrossOrigin
@RestController
public class UserController {

	@Autowired
	private UserService userService;

//	新增帳戶
	@PostMapping("gogobuy/user/registration")
	public BasicRes create(@Valid @RequestBody UserAddReq req) throws Exception {
		return userService.addUser(req);
	}

//	登入
	@PostMapping("gogobuy/user/login")
	public BasicRes login(@Valid @RequestBody UserLoginReq req, HttpSession session) throws Exception {
		BasicRes res = userService.login(req);
		if (res.getCode() == 200) {
//			setting session's attribute when login success
			session.setAttribute("account", req.getEmail());
		}
		return res;
	}

//	修改大頭貼、暱稱、載具
	@PatchMapping("gogobuy/user/profile")
	public BasicRes updateInfo(@RequestBody UserInfoDto dto, @RequestParam("id") String id) {
		return userService.updateInfo(dto, id);
	}

//	修改密碼
	@PostMapping("gogobuy/user/password")
	public BasicRes changePassword(@RequestBody UserPasswordDto dto, @RequestParam("id") String id) {
		return userService.updatePassword(id, dto);
	}

//	串接電話
	@PostMapping("gogobuy/user/phone")
	public BasicRes userPhone(@RequestBody String phone, @RequestParam("id") String id) {
		return userService.userPhone(phone, id);
	}

//	登出
	@PostMapping("gogobuy/user/logout")
	public BasicRes logout(HttpSession session) {
//		讓session 失效
//		一個 session 的有效時間為 30 分
//		在有效時間內做任何操作，都會再延長30分
		session.invalidate();
		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}

}
