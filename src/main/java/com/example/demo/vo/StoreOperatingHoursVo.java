package com.example.demo.vo;

public class StoreOperatingHoursVo {

	private int id;

	private int storesId;

	private int week;
	
	private String openTime;

	private String closeTime;

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

	public StoreOperatingHoursVo() {
		super();
	}

	public StoreOperatingHoursVo(int id, int storesId, int week, String openTime, String closeTime, boolean closed) {
		super();
		this.id = id;
		this.storesId = storesId;
		this.week = week;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.closed = closed;
	}

}
