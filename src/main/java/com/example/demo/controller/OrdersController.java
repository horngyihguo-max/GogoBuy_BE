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
import com.example.demo.response.ShippingFeeRes;
import com.example.demo.service.OrdersService;

import jakarta.validation.Valid;

@CrossOrigin
@RestController
public class OrdersController {

	@Autowired
	private OrdersService ordersService;

	// 新增訂單
	@PostMapping("gogobuy/addOrders")
	public BasicRes addOrders(@Valid @RequestBody OredersReq req) {
		return ordersService.addOrders(req);
	}

	// 更新訂單
	@PostMapping("gogobuy/updateOrders")
	public BasicRes updateOrders(@Valid @RequestBody OredersReq req) {
		return ordersService.updateOrders(req.getId(), req);
	}

	// 查詢跟團者的開團
	@GetMapping("gogobuy/getOrdersIdByUserId")
	public GroupbuyEventsRes getOrdersIdByUserId(@Valid @RequestParam(name = "user_id") String userId) {
		return ordersService.getOrdersByUserId(userId);
	}

	// 查詢跟團者的訂單
	@GetMapping("gogobuy/getEventIdByUserId")
	public GroupbuyEventsRes getEventIdByUserId(@Valid @RequestParam(name = "user_id") String userId,
			@RequestParam(name = "events_id") int eventsId) {
		return ordersService.getEventIdByUserId(userId, eventsId);
	}

	// 回傳計算完的平均運費
	@PostMapping("gogobuy/getShippingFeeByEventId")
	public ShippingFeeRes getShippingFeeByEventId(@RequestParam(name = "events_id") int eventsId) {
		return ordersService.getShippingFeeByEventId(eventsId);
	}

	// 軟刪除
	@PostMapping("gogobuy/deleteOrderByUserIdAndEventsId")
	public BasicRes deleteOrderByUserIdAndEventsId(@RequestParam(name = "user_id") String userId, //
			@RequestParam(name = "events_id") int eventsId) {
		return ordersService.deleteOrderByUserIdAndEventsId(userId, eventsId);
	}
}
