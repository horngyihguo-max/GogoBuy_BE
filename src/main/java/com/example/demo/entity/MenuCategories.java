package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "menu_categories")
@IdClass(value = MenuCategoriesId.class)
public class MenuCategories {

	@Id
	@Column(name = "id")
	private int id;
	
	@Id
	@Column(name = "stores_id")
	private int storeId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "price_level")
	private String priceLevel;

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

	public String getPriceLevel() {
		return priceLevel;
	}

	public void setPriceLevel(String priceLevel) {
		this.priceLevel = priceLevel;
	}

	
	
	
	
}
