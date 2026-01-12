package com.example.demo.response;

import java.util.List;

public class DelWishRes extends BasicRes{
	private List<String> followersList;

	public List<String> getFollowersList() {
		return followersList;
	}

	public void setFollowersList(List<String> followersList) {
		this.followersList = followersList;
	}

	public DelWishRes() {
		super();
	}

	public DelWishRes(int code, String message) {
		super(code, message);
	}

	public DelWishRes(int code, String message, List<String> followersList) {
		super(code, message);
		this.followersList = followersList;
	}
	
}
