package com.example.demo.constants;

public enum NotifiCategoryEnum {
	PRODUCT("商品"),
	SYSTEM("系統"),
	EVENT("活動");
	
	private String category;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	private NotifiCategoryEnum(String category) {
		this.category = category;
	}
	
}
