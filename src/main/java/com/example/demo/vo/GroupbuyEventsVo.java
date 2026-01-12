package com.example.demo.vo;

import java.util.List;

public class GroupbuyEventsVo {

	// 對應 temp_menu：紀錄限制的訂購品項
    private List<String> tempMenuIds;

    // 對應 recommend：紀錄勾選推薦的品項
    private List<String> recommendIds;

	public List<String> getTempMenuIds() {
		return tempMenuIds;
	}

	public void setTempMenuIds(List<String> tempMenuIds) {
		this.tempMenuIds = tempMenuIds;
	}

	public List<String> getRecommendIds() {
		return recommendIds;
	}

	public void setRecommendIds(List<String> recommendIds) {
		this.recommendIds = recommendIds;
	}

	
	
}
