package com.example.demo.constants;

public enum PaymentStatus {

	UNPAID("未付費"),
	PAID("已付費"),
	CONFIRMED("已確認");
	
	private String message;

	PaymentStatus(String string) {
		// TODO Auto-generated constructor stub
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
