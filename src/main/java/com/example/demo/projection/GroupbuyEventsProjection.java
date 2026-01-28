package com.example.demo.projection;

import java.time.LocalDateTime;

public interface GroupbuyEventsProjection {
	// 列出在 JSON 中需要的欄位
	Integer getId();

	String getHostId();

	Integer getStoresId();

	String getEventName();

	String getStatus();

	LocalDateTime getEndTime();

	Integer getTotalOrderAmount();

	Integer getShippingFee();

	String getSplitType();

	String getAnnouncement();

	String getType();

	String getTempMenuList();

	String getRecommendList();

	String getRecommendDescription();

	Integer getLimitation();

	Boolean getDeleted();

	String getNickname();
}
