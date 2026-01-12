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
	OUT_OF_TIMES_REMAINING(400, "超過許願次數"),
	WISH_ID_NOT_FOUND(400, "願望不存在"),
    SAME_PASSWORD_ERROR(400, "新密碼與舊密碼相同"),
    EMAIL_ERROR(400, "信箱發送失敗"),
    OTP_EMPTY(400, "請先取得驗證碼"),    
    OTP_EXPIRED(400, "驗證碼已過期"),    
    OTP_ERROR(400, "驗證碼錯誤"),    
    EMAIL_EXITS(400, "該信箱已被使用"),
    EMAIL_SUCCESS(200, "認證信已寄出"),
    PHONE_ERROR(400, "請輸入手機號碼"),
	WISH_USER_CAN_NOT_FOLLOW(400, "許願者不可跟願"),
	WISH_DELETE_ERROR(400, "願望刪除失敗"),
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
