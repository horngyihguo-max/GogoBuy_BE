package com.example.demo.vo;

import java.time.LocalDate;
import java.util.List;

import com.example.demo.constants.WishTypeEnum;

import jakarta.persistence.Column;

public class WishVo {
	private int id;
	private String user_id;
	private String nickname;
	private String title;
	private List<String> followers;
	private WishTypeEnum type;
	private LocalDate buildDate;
	private String location;
	private boolean finished;
	
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
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<String> getFollowers() {
		return followers;
	}
	public void setFollowers(List<String> followers) {
		this.followers = followers;
	}
	public WishTypeEnum getType() {
		return type;
	}
	public void setType(WishTypeEnum type) {
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
	public boolean isFinished() {
		return finished;
	}
	public void setFinished(boolean finished) {
		this.finished = finished;
	}
}
