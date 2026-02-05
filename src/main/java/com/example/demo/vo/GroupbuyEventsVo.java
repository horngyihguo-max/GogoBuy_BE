package com.example.demo.vo;

import java.util.List;

public class GroupbuyEventsVo {

	// 對應 temp_menu：紀錄限制的訂購品項
	private List<String> tempMenuList;

	// 對應 recommend：紀錄勾選推薦的品項
	private List<String> recommendList;

	public List<String> getTempMenuList() {
		return tempMenuList;
	}

	public void setTempMenuList(List<String> tempMenuList) {
		this.tempMenuList = tempMenuList;
	}

	public List<String> getRecommendList() {
		return recommendList;
	}

	public void setRecommendList(List<String> recommendList) {
		this.recommendList = recommendList;
	}

}
