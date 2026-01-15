package com.example.demo.entity;

import java.time.LocalDate;

import com.example.demo.constants.NotifiCategoryEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "notification_messages")
@Entity
public class NotifiMes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Enumerated(EnumType.STRING)
	@Column(name = "category")
	private NotifiCategoryEnum category;

	@Column(name = "title")
	private String title;

	@Column(name = "content")
	private String content;

	@Column(name = "target_url")
	private String targetUrl;

	@Column(name = "expired_at")
	private LocalDate expiredAt;

	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDate createdAt;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "event_id")
	private Integer eventId;

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

	public LocalDate getExpiredAt() {
		return expiredAt;
	}

	public void setExpiredAt(LocalDate expiredAt) {
		this.expiredAt = expiredAt;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}
}
