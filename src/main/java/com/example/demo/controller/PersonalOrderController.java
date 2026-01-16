package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.request.personalOrderReq;
import com.example.demo.response.BasicRes;
import com.example.demo.service.PersonalOrderService;

import jakarta.validation.Valid;

@CrossOrigin
@RestController
public class PersonalOrderController {
	
	@Autowired
	private PersonalOrderService personalOrderService;

	@PostMapping("gogobuy/addPersonalOrder")
	public BasicRes addPersonalOrder(@Valid @RequestBody personalOrderReq req) {
		return personalOrderService.addPersonalOrder(req);
	}
	@PostMapping("gogobuy/updatePersonalOrder")
	public BasicRes updatePersonalOrder(@Valid @RequestBody personalOrderReq req) {
		return personalOrderService.updatePersonalOrder(req);
	}
	
}
