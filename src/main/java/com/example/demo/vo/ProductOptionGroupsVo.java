package com.example.demo.vo;

import java.util.List;

public class ProductOptionGroupsVo {
	
	private int id;
	
	private int storesId;
	
	private String name;
	
	private boolean required;
	
	private int maxSelection;
	
	private List<ProductOptionItemsVo> items;

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

	public int getMaxSelection() {
		return maxSelection;
	}

	public void setMaxSelection(int maxSelection) {
		this.maxSelection = maxSelection;
	}

	public List<ProductOptionItemsVo> getItems() {
		return items;
	}

	public void setItems(List<ProductOptionItemsVo> items) {
		this.items = items;
	}

	

	
}
