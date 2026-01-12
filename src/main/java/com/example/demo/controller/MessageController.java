package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.response.PersonMesRes;
import com.example.demo.service.MessageService;

@CrossOrigin
@RestController
public class MessageController {
	@Autowired
	private MessageService messageService;
	
	@GetMapping("gogobuy/personal_message")
	public PersonMesRes personMes(String userId) {
		return messageService.personMes(userId);
	}
}
