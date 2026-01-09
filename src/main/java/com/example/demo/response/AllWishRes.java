package com.example.demo.response;

import java.time.LocalDate;

import jakarta.persistence.Column;

public class AllWishRes extends BasicRes{
	private int id;
	private String user_id;
	private String title;
	private boolean anonymous;
	private String followers;
	private String type;
	private LocalDate buildDate;
	private String location;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isAnonymous() {
		return anonymous;
	}
	public void setAnonymous(boolean anonymous) {
		this.anonymous = anonymous;
	}
	public String getFollowers() {
		return followers;
	}
	public void setFollowers(String followers) {
		this.followers = followers;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public LocalDate getBuildDate() {
		return buildDate;
	}
	public void setBuildDate(LocalDate buildDate) {
		this.buildDate = buildDate;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	public AllWishRes() {
		super();
	}
	public AllWishRes(int code, String message) {
		super(code, message);
	}
	public AllWishRes(int code, String message, int id, String user_id, String title, boolean anonymous,
			String followers, String type, LocalDate buildDate, String location) {
		super(code, message);
		this.id = id;
		this.user_id = user_id;
		this.title = title;
		this.anonymous = anonymous;
		this.followers = followers;
		this.type = type;
		this.buildDate = buildDate;
		this.location = location;
	}
}
