package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class VerificationService {

	/*
	 * redis功能所需service 暫用不到
	 * 
	 * @Autowired private StringRedisTemplate redisTemplate;
	 * 
	 * private static final String REDIS_PREFIX = "OTP:ACCOUNT:";
	 * 
	 * // 發送驗證碼 public BasicRes sendOtp(String email, String id) { // 產生 6 位隨機數
	 * String otpCode = String.format("%06d", new Random().nextInt(1000000));
	 * 
	 * // 存入 Redis (Key: OTP:ACCOUNT:U001, Value: 123456, 過期: 5分鐘)
	 * redisTemplate.opsForValue().set(REDIS_PREFIX + id, otpCode, 5,
	 * TimeUnit.MINUTES);
	 * 
	 * // 實際開發應呼叫 MailService，這裡先印在 Console 模擬 System.out.println("DEBUG: 寄送驗證碼 " +
	 * otpCode + " 到信箱 " + email);
	 * 
	 * return new BasicRes(ResMessage.SUCCESS.getCode(), //
	 * ResMessage.SUCCESS.getMessage()); }
	 * 
	 */

}
