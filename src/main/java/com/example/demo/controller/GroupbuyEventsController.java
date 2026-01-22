package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.request.GroupbuyEventsReq;
import com.example.demo.response.BasicRes;
import com.example.demo.response.GroupbuyEventsRes;
import com.example.demo.service.GoogleMapService;
import com.example.demo.service.GroupbuyEventsService;

import jakarta.validation.Valid;

@CrossOrigin
@RestController
public class GroupbuyEventsController {

	@Autowired
	private GroupbuyEventsService groupbuyEventsService;
	
	@Autowired
	private GoogleMapService googleMapService;

	// 新增
	@PostMapping("gogobuy/addEvent")
	public BasicRes addEvent(@Valid @RequestBody GroupbuyEventsReq req) {
		return groupbuyEventsService.addEvent(req);
	}

	// 更新
	@PostMapping("gogobuy/updateEvent")
	public BasicRes updateEvent(@Valid @RequestBody GroupbuyEventsReq req) {
		return groupbuyEventsService.updateEvent(req.getId(), req);
	}

	// 查詢開團者的開團紀錄
	@GetMapping("gogobuy/getGroupbuyEventById")
	public GroupbuyEventsRes getGroupbuyEventById(@RequestParam(name = "host_id", required = false) String hostId) {
		return groupbuyEventsService.getGroupbuyEventById(hostId);
	}

	// 透過店家ID取得對應菜單
	@GetMapping("gogobuy/getMenuByStoresId")
	public GroupbuyEventsRes getMenuByStoresId(@RequestParam(name = "stores_id", required = false) int storesId) {
		return groupbuyEventsService.getMenuByStoresId(storesId);
	}

	// 查詢開團菜單
	@GetMapping("gogobuy/getMenuByMenuId")
	public GroupbuyEventsRes getMenuByMenuId(@RequestParam(name = "temp_menu", required = false) List<Integer> menuId) {
		return groupbuyEventsService.getMenuByMenuId(menuId);
	}

	// 透過店家ID取得對應菜單
	@GetMapping("gogobuy/getGroupbuyEventByStoresId")
	public GroupbuyEventsRes getGroupbuyEventByStoresId(
			@RequestParam(name = "stores_id", required = false) int storesId) {
		return groupbuyEventsService.getGroupbuyEventByStoresId(storesId);
	}

	// 查詢全部開團
	@GetMapping("gogobuy/getAll")
	public GroupbuyEventsRes getAll() {
		return groupbuyEventsService.getAll();
	}

	// 暱稱查詢開團紀錄
	@GetMapping("gogobuy/getGroupbuyEventByStoresName")
	public GroupbuyEventsRes getGroupbuyEventByStoresName(
			@RequestParam(name = "host_nickname", required = false) String hostNickname) {
		return groupbuyEventsService.getGroupbuyEventByStoresName(hostNickname);
	}
	
	@GetMapping("gogobuy/googleMapAddress")
	public BasicRes googleMapAddress(@RequestParam(name = "address") String address) {
		return googleMapService.googleMapAddress(address);
	}
}
