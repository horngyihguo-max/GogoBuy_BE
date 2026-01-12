package com.example.demo.constants;

public enum PickupStatusEnum {

	NOT_PICKED_UP("未取貨"),
    PICKED_UP("已取貨");
	
	private String message;

	PickupStatusEnum(String string) {
		// TODO Auto-generated constructor stub
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
