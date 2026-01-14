package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.request.GroupbuyEventsReq;
import com.example.demo.request.OredersReq;
import com.example.demo.response.BasicRes;
import com.example.demo.service.OrdersService;

import jakarta.validation.Valid;


@CrossOrigin
@RestController
public class OrdersController {

	@Autowired
	private OrdersService ordersService;
	
	@PostMapping("gogobuy/addOrders")
	public BasicRes addOrders(@Valid @RequestBody OredersReq req) {
		return ordersService.addOrders(req);
	}
}
