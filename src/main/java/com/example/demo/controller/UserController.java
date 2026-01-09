package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constants.ResMessage;
import com.example.demo.dao.UserDao;
import com.example.demo.dto.UserAccountDto;
import com.example.demo.dto.UserInfoDto;
import com.example.demo.dto.UserPasswordDto;
import com.example.demo.entity.User;
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

//	@Autowired
//	private VerificationService verificationService;

	@Autowired
	private UserDao userDao;

	@GetMapping("/user/login/google")
	public Map<String, Object> googleLogin(@AuthenticationPrincipal OAuth2User principal) {
		if (principal == null) {
			return Map.of("status", "未登入", "message", "請先去 /login 登入");
		}
		// 1. 從 Google 資料中取得 Email
		String email = principal.getAttribute("email");

		// 2. 去資料庫查看看有沒有這個 Email
		// 這裡建議你的 UserDao 要有一個 findByEmail 的方法
		User user = userDao.getUserByEmail(email);

		if (user != null) {
			// 資料庫已有此用戶
			return Map.of("status", "登入成功 (現有會員)", "nickname", user.getNickname(), // 從資料庫拿暱稱
					"email", user.getEmail(), "avatarUrl", principal.getAttribute("picture")
//                "password", principal.getAttribute("sub")
			);
		} else {
			// Google 驗證通過，但你的資料庫還沒這封 Email (新使用者
			// 登入成功後，你會在這裡看到你的 Google 資料
			return Map.of("status", "登入成功 (新會員)"
//            "nickname", principal.getAttribute("name"),
//            "email", principal.getAttribute("email"),
//           "avatarUrl", principal.getAttribute("picture"),
//           "password", principal.getAttribute("sub")
			);
		}
	}

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

	public record SendOtpRequest(String email) {
	}

//	發送OTP驗證碼
	@PostMapping("gogobuy/user/send-otp")
	public BasicRes sendOtpWithoutRedis(@RequestBody SendOtpRequest req, @RequestParam("id") String id) {
		return userService.sendOtpWithoutRedis(req.email(), id);
	}

//	確認OTP驗證碼並更改email
	@PutMapping("gogobuy/user/email-verify")
	public BasicRes verifyAndUpdateEmail(@RequestBody UserAccountDto dto, @RequestParam("id") String id) {
		return userService.verifyAndUpdateEmail(dto.getNewEmail(), dto.getOtpCode(), id);
	}

	/**
	 * Redis發送修改email驗證碼功能 暫時用不到
	 * 
	 * // 1. 請求發送驗證碼 @PostMapping("gogobuy/account/send-otp") public BasicRes
	 * sendOtp(@RequestBody EmailOtpDto dto, @RequestParam("id") String id) { return
	 * verificationService.sendOtp(dto.getEmail(), id); }
	 * 
	 * // 2. 提交更改 (使用 PUT) @PutMapping("gogobuy/account/verify") public BasicRes
	 * updateAccount(@RequestBody UserAccountDto dto, @RequestParam("id") String id)
	 * { return userService.verifyAndUpdateEmail(dto, id); }
	 * 
	 */

}
