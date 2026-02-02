package com.example.demo.constants;

public enum GroupbuyStatusEnum {
	OPEN("進行中"), 
	LOCKED("未開始"), 
	FINISHED("已結束"), ;
	
	private String message;

	GroupbuyStatusEnum(String string) {
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
