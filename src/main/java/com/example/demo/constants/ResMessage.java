package com.example.demo.constants;

public enum ResMessage {

	SUCCESS(200, "成功"), //
	PLEASE_LOGIN_FIRST(400, "請先登入"),
	LOGIN_ERROR(400, "登入失敗"),
	REGISTRATION_ERROR(400, "註冊失敗"),
	PASSWORD_ERROR(400, "密碼錯誤"),
	PHONE_SIZE_ERROR(400,"電話長度錯誤"),
	CATEGORY_ERROR(400,"大類型的類型錯誤(不存在)"),
	STORE_EXISTS(400,"店家(電話)已存在"),
	STORE_NOT_FOUND(404,"找不到該店家"),
	INPUT_IS_EMPTY(400,"請輸入搜尋關鍵字"),
	USER_NOT_FOUND(404, "找不到此用戶");
	
	
	private int code;

	private String message;

	private ResMessage(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
