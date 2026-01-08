package com.example.demo.request;

import com.example.demo.constants.ValidationMsg;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class UserAddReq {
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = ValidationMsg.EMAIL_ERROR)
	@NotBlank(message = ValidationMsg.EMAIL_ERROR)
	private String email;

	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{8,16}$", message = ValidationMsg.PASSWORD_ERROR)
	@NotBlank(message = ValidationMsg.PASSWORD_ERROR)
	private String password;

	@NotNull(message = ValidationMsg.NAME_ERROR)
	private String nickname;

	@Pattern(regexp = "^09\\d{2}[\\s\\-]?\\d{3}[\\s\\-]?\\d{3}$", message = ValidationMsg.PHONE_ERROR)
	@NotNull(message = ValidationMsg.PHONE_ERROR)
	private String phone;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public UserAddReq() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserAddReq(
			@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = " Email 錯誤 ") @NotBlank(message = " Email 錯誤 ") String email,
			@NotBlank(message = " 密碼錯誤 ") String password, @NotNull(message = " 名字名是空的 ") String nickname,
			@Pattern(regexp = "^09\\d{2}[\\s\\-]?\\d{3}[\\s\\-]?\\d{3}$", message = " 電話格式錯誤") @NotNull(message = " 電話格式錯誤") String phone) {
		super();
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.phone = phone;
	}

}
