package com.example.demo.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.demo.dao.UserDao;

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
			userDao.addGoogleUser(UUID.randomUUID().toString(), email, encoder.encode(password), name, phone , avatarUrl);
		}
		return oAuth2User;
	}
}
