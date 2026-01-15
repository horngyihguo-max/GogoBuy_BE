package com.example.demo.request;

import java.util.List;

import com.example.demo.constants.NotifiCategoryEnum;
import com.example.demo.vo.UserNotificationVo;

public class NotifiMesReq {

	private int id;
	private NotifiCategoryEnum category;
	private String title;
	private String content;
	private String targetUrl;
	private String expiredAt;
	private String createdAt;
	private String userId;
	private Integer eventId;
	private List<UserNotificationVo>UserNotificationVoList;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public NotifiCategoryEnum getCategory() {
		return category;
	}
	public void setCategory(NotifiCategoryEnum category) {
		this.category = category;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTargetUrl() {
		return targetUrl;
	}
	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}
	public String getExpiredAt() {
		return expiredAt;
	}
	public void setExpiredAt(String expiredAt) {
		this.expiredAt = expiredAt;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getEventId() {
		return eventId;
	}
	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}
	public List<UserNotificationVo> getUserNotificationVoList() {
		return UserNotificationVoList;
	}
	public void setUserNotificationVoList(List<UserNotificationVo> userNotificationVoList) {
		UserNotificationVoList = userNotificationVoList;
	}

	

	
}
