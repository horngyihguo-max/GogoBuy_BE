package com.example.demo.vo;

import java.util.List;
import java.util.Map;

public class MenuVo {

private int id;
	
	private int storesId;
	
	private int categoryId;
	
	private String name;
	
	private String description;
	
	private int basePrice;
	
//	URL? BLOB?
	private String image; 
	
	private boolean available;
	
	private Map<String,Object> unusual;

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

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(int basePrice) {
		this.basePrice = basePrice;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	

	

	public Map<String, Object> getUnusual() {
		return unusual;
	}

	public void setUnusual(Map<String, Object> unusual) {
		this.unusual = unusual;
	}

	public MenuVo() {
		super();
	}

	public MenuVo(int id, int storesId, int categoryId, String name, String description, int basePrice, String image,
			boolean available, Map<String, Object> unusual) {
		super();
		this.id = id;
		this.storesId = storesId;
		this.categoryId = categoryId;
		this.name = name;
		this.description = description;
		this.basePrice = basePrice;
		this.image = image;
		this.available = available;
		this.unusual = unusual;
	}



	
	
	
}
