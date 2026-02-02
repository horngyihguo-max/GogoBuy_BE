package com.example.demo.request;

import com.example.demo.constants.ValidationMsg;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class GoogleOAuth2Req {

	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = ValidationMsg.EMAIL_ERROR)
	@NotBlank(message = ValidationMsg.EMAIL_ERROR)
	private String email;

	@NotBlank(message = ValidationMsg.PASSWORD_ERROR)
	private String password;

	@NotNull(message = ValidationMsg.NAME_ERROR)
	private String nickname;

	private String avatarUrl;

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

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public GoogleOAuth2Req() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GoogleOAuth2Req(
			@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = " Email 錯誤 ") @NotBlank(message = " Email 錯誤 ") String email,
			@NotBlank(message = " 密碼錯誤 ") String password, @NotNull(message = " 名字名是空的 ") String nickname,
			String avatarUrl) {
		super();
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.avatarUrl = avatarUrl;
	}

}
