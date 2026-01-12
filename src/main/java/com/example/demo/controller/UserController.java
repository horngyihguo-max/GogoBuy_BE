package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constants.ResMessage;
import com.example.demo.request.UserAddReq;
import com.example.demo.request.UserLoginReq;
import com.example.demo.response.BasicRes;
import com.example.demo.service.GoogleOAuth2Service;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@CrossOrigin
@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
    private GoogleOAuth2Service googleOAuth2Service;

	@PostMapping("gogobuy/addUser")
	public BasicRes create(@Valid @RequestBody UserAddReq req) throws Exception {
		return userService.addUser(req);
	}

	@PostMapping("gogobuy/login")
	public BasicRes login(@Valid @RequestBody UserLoginReq req, HttpSession session) throws Exception {
		BasicRes res = userService.login(req);
		if (res.getCode() == 200) {
//			setting session's attribute when login success
			session.setAttribute("account", req.getEmail());
		}
		return res;
	}

	@PostMapping("gogobuy/logout")
	public BasicRes logout(HttpSession session) {
//		讓session 失效
//		一個 session 的有效時間為 30 分
//		在有效時間內做任何操作，都會再延長30分
		session.invalidate();
		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}
	// http://localhost:8080/oauth2/authorization/google 授權網址
	// 拿Google資料
	@GetMapping("/oauth/user")
    public Map<String, Object> getUser(@AuthenticationPrincipal OAuth2User principal) {
        return googleOAuth2Service.loginGoogle(principal);
    }
	
}
