package com.example.demo.constants;

public enum NotifiCategoryEnum {
	GROUP_BUY("團購"),
	SYSTEM("系統"),
	WISH("許願");
	
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
