package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "store_operating_hours")
@IdClass(value = StoreOperatingHoursId.class)
public class StoreOperatingHours {

	@Id
	@Column(name = "id")
	private int id;
	
	@Id
	@Column(name = "stores_id")
	private int storesId;
	
	@Column(name = "week")
	private int week;
	
	@Column(name = "open_time")
	private String openTime;
	
	@Column(name = "close_time")
	private String closeTime;
	
	@Column(name = "is_closed")
	private boolean closed;

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

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public String getOpenTime() {
		return openTime;
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}

	public String getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}
	
}
