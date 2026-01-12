package com.example.demo.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="wishes")
public class Wishes {
	@Id
	@Column(name="id")
	private int id;
	@Column(name="user_id")
	private String user_id;
	@Column(name="title")
	private String title;
	@Column(name="is_anonymous")
	private boolean anonymous;
	@Column(name="followers")
	private String followers;
	@Column(name="is_deleted")
	private boolean deleted;
	@Column(name="type")
	private String type;
	@Column(name="build_date")
	private LocalDate buildDate;
	@Column(name="location")
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
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
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
}
