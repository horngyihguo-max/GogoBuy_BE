package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_option_items")
@IdClass(value = ProductOptionItemsId.class)
public class ProductOptionItems {

	@Id
	@Column(name = "id")
	private int id;

	@Id
	@Column(name = "group_id")
	private int groupId;

	@Column(name = "name")
	private String name;

	@Column(name = "extraPrice")
	private Integer extraPrice;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getExtraPrice() {
		return extraPrice;
	}

	public void setExtraPrice(Integer extraPrice) {
		this.extraPrice = extraPrice;
	}
	
	
}
