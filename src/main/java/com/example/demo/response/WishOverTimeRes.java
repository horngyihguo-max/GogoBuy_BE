package com.example.demo.response;

import java.util.List;

public class WishOverTimeRes extends BasicRes{
	private List<String> userList;

	public WishOverTimeRes() {
		super();
	}

	public WishOverTimeRes(int code, String message) {
		super(code, message);
	}

	public WishOverTimeRes(int code, String message, List<String> userList) {
		super(code, message);
		this.userList = userList;
	}

	public List<String> getUserList() {
		return userList;
	}

	public void setUserList(List<String> userList) {
		this.userList = userList;
	}
	
}
