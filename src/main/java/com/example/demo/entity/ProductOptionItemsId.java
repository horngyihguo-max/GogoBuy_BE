package com.example.demo.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ProductOptionItemsId implements Serializable{
	
	private int id;

	private int groupId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	
	

}
