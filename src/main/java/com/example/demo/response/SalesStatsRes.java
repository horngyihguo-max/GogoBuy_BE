package com.example.demo.response;

import java.util.List;
import java.util.Map;

import com.example.demo.entity.SalesStats;

public class SalesStatsRes extends BasicRes{

	private Integer menuId;
    private Integer salesVolume;
    private String statsType;
    private List<SalesStats> salesList; // 用於返回排行榜
    private List<Map<String, Object>> salesDetailList;
	public Integer getMenuId() {
		return menuId;
	}
	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}
	public Integer getSalesVolume() {
		return salesVolume;
	}
	public void setSalesVolume(Integer salesVolume) {
		this.salesVolume = salesVolume;
	}
	public String getStatsType() {
		return statsType;
	}
	public void setStatsType(String statsType) {
		this.statsType = statsType;
	}
	public List<SalesStats> getSalesList() {
		return salesList;
	}
	public void setSalesList(List<SalesStats> salesList) {
		this.salesList = salesList;
	}
	public List<Map<String, Object>> getSalesDetailList() {
		return salesDetailList;
	}
	public void setSalesDetailList(List<Map<String, Object>> salesDetailList) {
		this.salesDetailList = salesDetailList;
	}
	public SalesStatsRes() {
		super();
	}
	public SalesStatsRes(int code, String message) {
		super(code, message);
	}
	
    
    
}
