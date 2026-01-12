package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.constants.ResMessage;
import com.example.demo.dao.MessageDao;
import com.example.demo.response.BasicRes;
import com.example.demo.response.PersonMesRes;

@Service
public class MessageService {
	@Autowired
	private MessageDao messageDao;
	
	public PersonMesRes personMes(String userId) {
		final String userIdPattern="^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
		if(!userId.matches(userIdPattern)) {
			return new PersonMesRes(ResMessage.USER_NOT_FOUND.getCode(), ResMessage.USER_NOT_FOUND.getMessage());
		}
		return new PersonMesRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), messageDao.allMes(userId));
	}
}
