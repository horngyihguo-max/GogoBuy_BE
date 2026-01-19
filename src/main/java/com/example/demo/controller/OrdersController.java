package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.request.OredersReq;
import com.example.demo.response.BasicRes;
import com.example.demo.response.GroupbuyEventsRes;
import com.example.demo.service.OrdersService;

import jakarta.validation.Valid;


@CrossOrigin
@RestController
public class OrdersController {

	@Autowired
	private OrdersService ordersService;
	
	//新增訂單
	@PostMapping("gogobuy/addOrders")
	public BasicRes addOrders(@Valid @RequestBody OredersReq req) {
		return ordersService.addOrders(req);
	}
	
	//更新訂單
	@PostMapping("gogobuy/updateOrders")
	public BasicRes updateOrders( @Valid @RequestBody OredersReq req) {
		return ordersService.updateOrders(req.getId(), req);
	}
	
	//查詢跟團者的訂單
	@GetMapping("gogobuy/getEventIdByUserId")
	public GroupbuyEventsRes getEventIdByUserId(@RequestParam(name = "user_id") String userId){
		return ordersService.getEventIdByUserId( userId);
	}
}
