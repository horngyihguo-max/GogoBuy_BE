package com.example.demo.vo;

import java.util.ArrayList;
import java.util.List;

public class MenuCategoriesVo {
	
	private int id;
	
	private int storesId;
	
	private String name;
	
	private List<PriceLevelVo> priceLevel = new ArrayList<>();
	
	private List<MenuVo> menuVo = new ArrayList<>();

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

	public List<PriceLevelVo> getPriceLevel() {
		return priceLevel;
	}

	public void setPriceLevel(List<PriceLevelVo> priceLevel) {
		this.priceLevel = priceLevel;
	}

	public List<MenuVo> getMenuVo() {
		return menuVo;
	}

	public void setMenuVo(List<MenuVo> menuVo) {
		this.menuVo = menuVo;
	}

	public MenuCategoriesVo() {
		super();
	}

	public MenuCategoriesVo(int id, int storesId, String name, List<PriceLevelVo> priceLevel, List<MenuVo> menuVo) {
		super();
		this.id = id;
		this.storesId = storesId;
		this.name = name;
		this.priceLevel = priceLevel;
		this.menuVo = menuVo;
	}

	



 
}
