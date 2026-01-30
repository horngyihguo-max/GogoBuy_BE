package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_notification")
public class UserNotif {
	@Id
	@Column(name = "id")
	private int id;
	@Column(name = "user_id")
	private String userId;
	@Column(name = "notif_id")
	private int notifId;
	@Column(name = "is_check")
	private boolean checked;
	@Column(name = "is_deleted")
	private boolean deleted;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getNotifiId() {
		return notifId;
	}
	public void setNotifiId(int notifId) {
		this.notifId = notifId;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
