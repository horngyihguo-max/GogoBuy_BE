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
import com.example.demo.dao.UserDao;
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
	@Autowired
    private UserDao userDao;

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
	
	@GetMapping("/gogobuy/OAlogin")
    public Map<String, Object> loginGoogle(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return Map.of(
            		"status", "未登入", 
            		"message", "請先去 /login 登入"
            		);
        }
     // 1. 從 Google 資料中取得 Email
        String email = principal.getAttribute("email");

        // 2. 去資料庫查看看有沒有這個 Email
        User user = userDao.getUser(email);

        if (user != null) {
            // 資料庫已有此用戶
            return Map.of(
                "status", "登入成功 "
//                "nickname", user.getNickname(), // 從資料庫拿暱稱
//                "email",user.getEmail(),
//                "avatarUrl", principal.getAttribute("picture"),
//         		 "password", principal.getAttribute("sub")
            );
        } else {
            // Google 驗證通過，但你的資料庫還沒這封 Email (新使用者
        // 登入成功後，你會在這裡看到你的 Google 資料
        return Map.of(
            "status", "註冊成功"
//            "nickname", principal.getAttribute("name"),
//            "email", principal.getAttribute("email"),
//           "avatarUrl", principal.getAttribute("picture"),
//           "password", principal.getAttribute("sub")
        );
	}
}
}
