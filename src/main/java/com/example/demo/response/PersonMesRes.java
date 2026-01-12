package com.example.demo.response;

import java.util.List;

import com.example.demo.entity.Message;

public class PersonMesRes extends BasicRes{
	private List<Message> messageList;

	public List<Message> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<Message> messageList) {
		this.messageList = messageList;
	}

	public PersonMesRes() {
		super();
	}

	public PersonMesRes(int code, String message) {
		super(code, message);
	}

	public PersonMesRes(int code, String message, List<Message> messageList) {
		super(code, message);
		this.messageList = messageList;
	}
	
}
