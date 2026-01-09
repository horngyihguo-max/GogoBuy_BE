package com.example.demo.vo;

import java.util.List;

public class WishVo {
	private String userId;
	private String followers;
	private boolean deleted;
	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFollowers() {
		return followers;
	}
	public void setFollowers(String followers) {
		this.followers = followers;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	public WishVo() {
		super();
	}
	public WishVo(String userId, String followers, boolean deleted) {
		super();
		this.userId = userId;
		this.followers = followers;
		this.deleted = deleted;
	}
}
