package com.example.demo.constants;

public enum SplitTypeEnum {

	EQUAL("平分拆帳"),
	WEIGHT("權重拆帳");
	
	private String message;

	SplitTypeEnum(String string) {
		// TODO Auto-generated constructor stub
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
