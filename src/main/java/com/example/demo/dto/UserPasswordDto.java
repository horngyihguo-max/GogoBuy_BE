package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserPasswordDto {

	@NotBlank(message = "舊密碼不能為空")
	private String oldPassword;

	@NotBlank(message = "新密碼不能為空")
	@Size(min = 8, message = "新密碼長度至少需要8位")
	private String newPassword;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
