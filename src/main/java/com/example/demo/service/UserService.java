package com.example.demo.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.constants.ResMessage;
import com.example.demo.dao.UserDao;
import com.example.demo.entity.User;
import com.example.demo.request.UserAddReq;
import com.example.demo.request.UserLoginReq;
import com.example.demo.response.BasicRes;

@EnableScheduling
@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	public BasicRes addUser(UserAddReq req) {
		String uniqueID = UUID.randomUUID().toString();
		String email = req.getEmail();
		String password = req.getPassword();
		String name = req.getNickname();
		String phone = req.getPhone();
		int res = userDao.addUser(uniqueID, email, encoder.encode(password), name, phone);
		if (res == 1) {
			return new BasicRes(ResMessage.SUCCESS.getCode(), //
					ResMessage.SUCCESS.getMessage());
		} else {
			return new BasicRes(ResMessage.REGISTRATION_ERROR.getCode(), //
					ResMessage.REGISTRATION_ERROR.getMessage());
		}
	}

	public BasicRes login(UserLoginReq req) {
		String email = req.getEmail();
		String password = req.getPassword();
	

		User user = userDao.getUser(email);

		if (user == null) {
			return new BasicRes(ResMessage.USER_NOT_FOUND.getCode(), //
					ResMessage.USER_NOT_FOUND.getMessage());
		}
//		比對密碼:
//		比對輸入的密碼與資料庫中加密過的密碼是否相同
		if (!encoder.matches(password, user.getPassword())) {
			return new BasicRes(ResMessage.PASSWORD_ERROR.getCode(), //
					ResMessage.PASSWORD_ERROR.getMessage());
		}

		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}

//	單位:           秒 分 時 日 月 週
//	@Scheduled(cron = "* * * * * *")
//	public void test() {
//		System.out.println(LocalDateTime.now());	
//	}

}
