package com.example.demo.response;

public class GetUserInfoRes extends BasicRes {

	private String id;

	private String nickname;

	private String email;

	private String phone;

	private String avatarUrl;

	private String carrier;

	private int exp;

	private int timesRemaining;

	private String provider;

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getTimesRemaining() {
		return timesRemaining;
	}

	public void setTimesRemaining(int timesRemaining) {
		this.timesRemaining = timesRemaining;
	}

	public GetUserInfoRes() {
		super();
	}

	public GetUserInfoRes(int code, String message) {
		super(code, message);
	}

	public GetUserInfoRes(int code, String message, String id, String nickname, String email, String phone,
			String avatarUrl, String carrier, int exp, int timesRemaining, String provider) {
		super(code, message);
		this.id = id;
		this.nickname = nickname;
		this.email = email;
		this.phone = phone;
		this.avatarUrl = avatarUrl;
		this.carrier = carrier;
		this.exp = exp;
		this.timesRemaining = timesRemaining;
		this.provider = provider;
	}

}
