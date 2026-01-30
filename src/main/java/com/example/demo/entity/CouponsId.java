package com.example.demo.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CouponsId implements Serializable{

	private int id;
	
	private String userId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
}
