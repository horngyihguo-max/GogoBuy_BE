package com.example.demo.vo;

import java.util.List;

public class MenuCategoriesVo {
	
	private int id;
	
	private int store;
	
	private String name;
	
	private List<PriceLevelVo> priceLevel;

	public MenuCategoriesVo() {
		super();
	}

	public MenuCategoriesVo(int id, int store, String name, List<PriceLevelVo> priceLevel) {
		super();
		this.id = id;
		this.store = store;
		this.name = name;
		this.priceLevel = priceLevel;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStore() {
		return store;
	}

	public void setStore(int store) {
		this.store = store;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PriceLevelVo> getPriceLevel() {
		return priceLevel;
	}

	public void setPriceLevel(List<PriceLevelVo> priceLevel) {
		this.priceLevel = priceLevel;
	}

 
}
