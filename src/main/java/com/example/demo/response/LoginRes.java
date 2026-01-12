package com.example.demo.response;

public class LoginRes extends BasicRes {

	private String id;

	private String nickname;

	private String phone;

	private String avatarUrl;

	private int exp;

	private String carrier;

	private int timesRemaining;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public int getTimesRemaining() {
		return timesRemaining;
	}

	public void setTimesRemaining(int timesRemaining) {
		this.timesRemaining = timesRemaining;
	}

	public LoginRes() {
		super();
	}

	public LoginRes(int code, String message) {
		super(code, message);
	}

	public LoginRes(int code, String message, String id, String nickname, String phone, String avatarUrl, int exp,
			String carrier, int timesRemaining) {
		super(code, message);
		this.id = id;
		this.nickname = nickname;
		this.phone = phone;
		this.avatarUrl = avatarUrl;
		this.exp = exp;
		this.carrier = carrier;
		this.timesRemaining = timesRemaining;
	}

}
