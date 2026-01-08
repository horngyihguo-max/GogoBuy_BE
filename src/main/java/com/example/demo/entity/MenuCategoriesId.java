package com.example.demo.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MenuCategoriesId implements Serializable{

	private int id;
	
	private int storeId;

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
	
}
