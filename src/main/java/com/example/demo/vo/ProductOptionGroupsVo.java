package com.example.demo.vo;

public class ProductOptionGroupsVo {
	
	private int id;
	
	private int storeId;
	
	private String name;
	
	private boolean required;
	
	private int maxelection;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public int getMaxelection() {
		return maxelection;
	}

	public void setMaxelection(int maxelection) {
		this.maxelection = maxelection;
	}

	
}
