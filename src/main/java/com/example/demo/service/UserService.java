package com.example.demo.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.demo.constants.ResMessage;
import com.example.demo.dao.UserDao;
import com.example.demo.dto.UserInfoDto;
import com.example.demo.dto.UserPasswordDto;
import com.example.demo.entity.User;
import com.example.demo.request.UserAddReq;
import com.example.demo.request.UserLoginReq;
import com.example.demo.response.BasicRes;

import jakarta.transaction.Transactional;

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

//	更新暱稱、大頭貼或載具
	@Transactional(rollbackOn = Exception.class)
	public BasicRes updateInfo(UserInfoDto dto, String id) {
		User user = userDao.getUserById(id);
		if (user == null) {
			return new BasicRes(ResMessage.USER_NOT_FOUND.getCode(), //
					ResMessage.USER_NOT_FOUND.getMessage());
		}

		String finalnickname = StringUtils.hasText(dto.getNickname()) ? dto.getNickname() : user.getNickname();

		String finalavatarUrl = (dto.getAvatarUrl() != null) ? dto.getAvatarUrl() : user.getAvatarUrl();

		String finalcarrier = (dto.getCarrier() != null) ? dto.getCarrier() : user.getCarrier();

		userDao.updateProfile(finalnickname, finalavatarUrl, finalcarrier, id);
		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}

//	更新密碼
	@Transactional(rollbackOn = Exception.class)
	public BasicRes updatePassword(String id, UserPasswordDto dto) {
		User user = userDao.getUserById(id);
//		檢查帳戶是否存在
		if (user == null) {
			return new BasicRes(ResMessage.USER_NOT_FOUND.getCode(), //
					ResMessage.USER_NOT_FOUND.getMessage());
		}

//		比對密碼:
//		比對輸入的密碼與資料庫中加密過的密碼是否相同
		if (!encoder.matches(dto.getOldPassword(), user.getPassword())) {
			return new BasicRes(ResMessage.PASSWORD_ERROR.getCode(), //
					ResMessage.PASSWORD_ERROR.getMessage());
		}
		if (dto.getOldPassword().equals(dto.getNewPassword())) {
			return new BasicRes(ResMessage.SAME_PASSWORD_ERROR.getCode(), //
					ResMessage.SAME_PASSWORD_ERROR.getMessage());
		}

//		加密新密碼
		String encodePassword = encoder.encode(dto.getNewPassword());
		user.setPassword(encodePassword);
		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}

//	串接電話
	@Transactional(rollbackOn = Exception.class)
	public BasicRes userPhone(String phone, String id) {
		User user = userDao.getUserById(id);
		if (user == null) {
			return new BasicRes(ResMessage.USER_NOT_FOUND.getCode(), //
					ResMessage.USER_NOT_FOUND.getMessage());
		}
		if (phone == null) {
			return new BasicRes(ResMessage.PHONE_ERROR.getCode(), //
					ResMessage.PHONE_ERROR.getMessage());
		}
		user.setPhone(phone);
		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}
}
