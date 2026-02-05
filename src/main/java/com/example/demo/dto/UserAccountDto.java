package com.example.demo.dto;

public class UserAccountDto {
	
//	Redis功能所需 暫時用不到

	private String newEmail;
	
	private String otpCode;

	public String getNewEmail() {
		return newEmail;
	}

	public void setNewEmail(String newEmail) {
		this.newEmail = newEmail;
	}

	public String getOtpCode() {
		return otpCode;
	}

	public void setOtpCode(String otpCode) {
		this.otpCode = otpCode;
	}

}
