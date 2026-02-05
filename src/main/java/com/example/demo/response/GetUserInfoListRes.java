package com.example.demo.response;

import java.util.List;

import com.example.demo.entity.UserInfo;

public class GetUserInfoListRes extends BasicRes {

	private List<UserInfo> userList;

	public List<UserInfo> getUserList() {
		return userList;
	}

	public void setUserList(List<UserInfo> userList) {
		this.userList = userList;
	}

	public GetUserInfoListRes() {
		super();
	}

	public GetUserInfoListRes(int code, String message) {
		super(code, message);
	}

	public GetUserInfoListRes(int code, String message, List<UserInfo> userList) {
		super(code, message);
		this.userList = userList;
	}

}
