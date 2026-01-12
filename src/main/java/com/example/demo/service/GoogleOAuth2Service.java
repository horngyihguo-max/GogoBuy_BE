package com.example.demo.service;

import java.util.HashMap;
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
public class GoogleOAuth2Service extends DefaultOAuth2UserService {

	@Autowired
	private UserDao userDao;

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	// Spring Security 登入時會自動跳進來的地方
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

		// 1. 抓取 Google 資料
		OAuth2User oAuth2User = super.loadUser(userRequest);
		String email = oAuth2User.getAttribute("email");
		String nickname = oAuth2User.getAttribute("name");
		String avatarUrl = oAuth2User.getAttribute("picture");
		String password = oAuth2User.getAttribute("sub");
		String phone = "未提供電話";

		// 2. 判斷是否需要自動註冊
		if (userDao.getUser(email) == null) {
			userDao.addGoogleUser(UUID.randomUUID().toString(), email, encoder.encode(password), nickname, phone,
					avatarUrl);
		}
		return oAuth2User;
	}

	public Map<String, Object> loginGoogle( OAuth2User principal) {
		
		Map<String, Object> res = new HashMap<>();
		
		if (principal == null) {
			return Map.of("status", "未登入");
		}
		// 1. 從 Google 資料中取得 Email
		String email = principal.getAttribute("email");
		String nickname = principal.getAttribute("name");
        String avatarUrl = principal.getAttribute("picture");

		// 2. 去資料庫查看看有沒有這個 Email
		User user = userDao.getUser(email);

		if (user != null) {
            res.put("status", "success");
            res.put("nickname", nickname != null ? nickname : user.getNickname()); 
            res.put("email", email);
//            res.put("sub", encoder.encode(password));
            res.put("avatarUrl", avatarUrl);
        } else {
            res.put("status", "processing");
            res.put("message", "帳號建立中，請稍後再試");
        }
        return res;
	}
}
