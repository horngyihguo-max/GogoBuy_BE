package com.example.demo.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MenuId implements Serializable{

	private int id;
	
	private int storesId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStoresId() {
		return storesId;
	}

	public void setStoresId(int storesId) {
		this.storesId = storesId;
	}
	
}
