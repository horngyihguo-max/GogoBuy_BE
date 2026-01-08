package com.example.demo.service;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.demo.dao.UserDao;
import com.example.demo.entity.User;

@Service
public class GoogelOAuth2Service extends DefaultOAuth2UserService {

	@Autowired
	private UserDao userDao;

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	// Spring Security 登入時會自動跳進來的地方
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

		// 1. 抓取 Google 資料
		OAuth2User oAuth2User = super.loadUser(userRequest);
		String email = oAuth2User.getAttribute("email");
		String name = oAuth2User.getAttribute("name");
		String avatarUrl = oAuth2User.getAttribute("picture");
		String password = oAuth2User.getAttribute("sub");
		String phone = "09xxxxxxxx";

		// 2. 判斷是否需要自動註冊
		if (userDao.getUser(email) == null) {
			userDao.addGoogleUser(UUID.randomUUID().toString(), email, encoder.encode(password), name, phone,
					avatarUrl);
		}
		return oAuth2User;
	}

	public Map<String, Object> loginGoogle(@AuthenticationPrincipal OAuth2User principal) {
		if (principal == null) {
			return Map.of("status", "未登入");
		}
		// 1. 從 Google 資料中取得 Email
		String email = principal.getAttribute("email");

		// 2. 去資料庫查看看有沒有這個 Email
		User user = userDao.getUser(email);

		if (user != null) {
			// 資料庫已有此用戶
			return Map.of("status", "登入成功 "
//	                "nickname", user.getNickname(), // 從資料庫拿暱稱
//	                "email",user.getEmail(),
//	                "avatarUrl", principal.getAttribute("picture"),
//	         		 "password", principal.getAttribute("sub")
			);
		} else {
			// Google 驗證通過，但資料庫沒有這個 Email (新使用者
			return Map.of("status", "處理中"
					//"message", "帳號同步中"
					);
		}
	}
}
