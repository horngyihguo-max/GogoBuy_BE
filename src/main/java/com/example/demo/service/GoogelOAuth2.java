package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.demo.dao.UserDao;
import com.example.demo.request.UserAddReq;

@Service
public class GoogelOAuth2 extends DefaultOAuth2UserService {

	@Autowired
	private UserService userService;

	@Autowired
	private UserDao userDao;

	@Override
	// @Override 意思是：「這是我要覆蓋（重新定義）父類別的方法。」

	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		// 1. 先呼叫父類別，讓 Spring 去跟 Google 把資料要回來
		OAuth2User oAuth2User = super.loadUser(userRequest);

		// 2. 從 Google 資料 (Map) 中取出資訊
		String email = oAuth2User.getAttribute("email");
		String name = oAuth2User.getAttribute("name");
		String pictureUrl = oAuth2User.getAttribute("picture");
		String sub = oAuth2User.getAttribute("sub");

		// 3. 先檢查資料庫
		if (userDao.getUser(email) == null) {
			// 4. 建立一個 UserAddReq 物件，並把資料塞進去
			UserAddReq req = new UserAddReq();
			req.setEmail(email);
			req.setNickname(name);
			// Google 登入者沒有密碼，隨機生一個
			req.setPassword(sub);
			// 電話在 Google 資料裡通常拿不到
			req.setPhone("null");
			// 圖片
			req.setAvatarUrl(pictureUrl);
			// 5. 存入 DB
			userService.addUser(req);
		}
		return oAuth2User;
	}
}
