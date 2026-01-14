package com.example.demo.constants;

public enum WishTypeEnum {
	beverage("飲品"),
	restaurant("餐廳"),
	groceries("生鮮雜貨");
	
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private WishTypeEnum(String type) {
		this.type = type;
	}
}
