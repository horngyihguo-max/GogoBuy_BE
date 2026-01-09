package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
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

	@Autowired
	private JavaMailSender mailSender;

	/**
	 * Redis發送修改email驗證碼功能 暫時用不到
	 * 
	 * @Autowired private StringRedisTemplate redisTemplate;
	 */

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	public BasicRes addUser(UserAddReq req) {
		String uniqueID = UUID.randomUUID().toString();
		String email = req.getEmail();
		String password = req.getPassword();
		String name = req.getNickname();
		String phone = req.getPhone();
//		String avatarUrl = req.getAvatarUrl();
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

		User user = userDao.getUserByEmail(email);

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
		userDao.userPassword(id, encodePassword);
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
		userDao.userPhone(id, phone);
		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}

//	發送OTP code 並修改Email
	@Transactional
	public BasicRes sendOtpWithoutRedis(String email, String id) {
		// 1. 生成 6 位數驗證碼
		String otpCode = String.format("%06d", new Random().nextInt(1000000));

		// 2. 找到使用者並更新 OTP 資訊
		User user = userDao.getUserById(id);
		user.setOtpCode(otpCode);
		user.setOtpExpiry(LocalDateTime.now().plusMinutes(5)); // 設定 5 分鐘後過期
		userDao.save(user);

		SimpleMailMessage message = new SimpleMailMessage();

		// 從 properties 讀取，或直接寫死發信帳號
		message.setFrom("GogobuyAdmin@gmail.com");
		message.setTo(email);
		message.setSubject("[GoGoBuy] 修改帳號驗證碼");
		message.setText("您好：\n\n您的驗證碼為：" + otpCode + "\n驗證碼將於 5 分鐘後失效，請盡速完成操作。");

		try {
			System.out.println("OTP碼:" + otpCode);
			// 執行發送
			mailSender.send(message);
			return new BasicRes(ResMessage.EMAIL_SUCCESS.getCode(), //
					ResMessage.EMAIL_SUCCESS.getMessage());
		} catch (Exception e) {
			return new BasicRes(ResMessage.EMAIL_ERROR.getCode(), //
					ResMessage.EMAIL_ERROR.getMessage());
		}
	}

	@Transactional
	public BasicRes verifyAndUpdateEmail(String newEmail, String inputCode, String id) {
		User user = userDao.getUserById(id);

		// 1. 檢查驗證碼是否為空
		if (user.getOtpCode() == null || user.getOtpExpiry() == null) {
			return new BasicRes(ResMessage.OTP_EMPTY.getCode(), //
					ResMessage.OTP_EMPTY.getMessage());
		}

		// 2. 檢查是否過期
		if (LocalDateTime.now().isAfter(user.getOtpExpiry())) {
			return new BasicRes(ResMessage.OTP_EXPIRED.getCode(), //
					ResMessage.OTP_EXPIRED.getMessage());
		}

		// 3. 比對驗證碼
		if (!user.getOtpCode().equals(inputCode)) {
			return new BasicRes(ResMessage.OTP_ERROR.getCode(), //
					ResMessage.OTP_ERROR.getMessage());
		}

		// 檢查新信箱是否已被他人佔用
		if (userDao.existsByEmail(newEmail) > 0) { 
			return new BasicRes(ResMessage.EMAIL_EXITS.getCode(), ResMessage.EMAIL_EXITS.getMessage());
		}

		// 4. 驗證通過，執行更新並清空 OTP 資訊
		user.setEmail(newEmail);
		user.setOtpCode(null); // 清除驗證碼，防止重複使用
		user.setOtpExpiry(null);
		userDao.save(user);

		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}
	
	/**
     * 定時清理過期的 OTP。
     * cron 表達式說明：秒 分 時 日 月 週
     * "0 0 3 * * ?"    代表每天凌晨 3 點執行
     *  0 0 12 * * ?    每天中午 12 點
	 *  0 0 12 1 * ?    每個月 1 號中午 12 點
	 *  0/5 0 12 * 1 ?  1 月每天中午 12 點，每 5 秒
     */
//	每小時進行一次清理
    @Scheduled(cron = "*0 0 * * * ?")
    @Transactional
    public void cleanupExpiredOtp() {
        System.out.println("開始清理過期驗證碼...");
        
        // 找到所有已過期且還有 code 的使用者，並清空欄位
        int updatedRows = userDao.clearExpiredOtp(LocalDateTime.now());
        
        System.out.println("清理完成，共影響了 " + updatedRows + " 筆資料。");
    }

	/*
	 * Redis功能區塊 暫時用不到 // 驗證並更新 Email
	 * 
	 * @Transactional(rollbackOn = Exception.class) public BasicRes
	 * verifyAndUpdateEmail(UserAccountDto dto, String id) { User user =
	 * userDao.getUserById(id); String redisKey = "OTP:EMAIL_CHANGE:" + id; String
	 * storedCode = redisTemplate.opsForValue().get(redisKey);
	 * 
	 * // 1. 檢查驗證碼是否存在 if (storedCode == null) { return new
	 * BasicRes(ResMessage.OTP_EXPIRED.getCode(), //
	 * ResMessage.OTP_EXPIRED.getMessage()); }
	 * 
	 * // 2. 比對驗證碼 if (!storedCode.equals(dto.getOtpCode())) { return new
	 * BasicRes(ResMessage.OTP_ERROR.getCode(), //
	 * ResMessage.OTP_ERROR.getMessage()); }
	 * 
	 * // 3. 檢查 Email 是否重複 if (user.getEmail() == (dto.getNewEmail())) { return new
	 * BasicRes(ResMessage.EMAIL_EXITS.getCode(), //
	 * ResMessage.EMAIL_EXITS.getMessage()); }
	 * 
	 * // 4. 更新 user.setEmail(dto.getNewEmail()); userDao.save(user);
	 * 
	 * // 5. 更新成功，刪除驗證碼 redisTemplate.delete(redisKey);
	 * 
	 * return new BasicRes(ResMessage.SUCCESS.getCode(), //
	 * ResMessage.SUCCESS.getMessage()); }
	 */
}
