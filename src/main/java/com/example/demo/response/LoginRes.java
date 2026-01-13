package com.example.demo.response;

public class LoginRes extends BasicRes {

	private String id;

	public LoginRes() {
		super();
	}

	public LoginRes(int code, String message) {
		super(code, message);
	}

	public LoginRes(int code, String message, String id) {
		super(code, message);
		this.id = id;
	}

}
