package com.example.demo.response;

import java.util.List;

import com.example.demo.entity.NotifiMes;
import com.example.demo.entity.UserNotif;

public class MessagesRes extends BasicRes {

	private List<NotifiMes> notifiMesList;
	
	private List<UserNotif> UserNotifList;

	public List<NotifiMes> getNotifiMesList() {
		return notifiMesList;
	}

	public void setNotifiMesList(List<NotifiMes> notifiMesList) {
		this.notifiMesList = notifiMesList;
	}

	public List<UserNotif> getUserNotifList() {
		return UserNotifList;
	}

	public void setUserNotifList(List<UserNotif> userNotifList) {
		UserNotifList = userNotifList;
	}

	public MessagesRes() {
		super();
	}

	public MessagesRes(int code, String message) {
		super(code, message);
	}

	public MessagesRes(int code, String message, List<NotifiMes> notifiMesList, List<UserNotif> userNotifList) {
		super(code, message);
		this.notifiMesList = notifiMesList;
		UserNotifList = userNotifList;
	}
	
	public MessagesRes(int code, String message, List<NotifiMes> notifiMesList) {
		super(code, message);
		this.notifiMesList = notifiMesList;
	}
	
	
}
