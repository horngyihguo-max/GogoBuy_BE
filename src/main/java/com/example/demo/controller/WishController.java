package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.request.UserAddReq;
import com.example.demo.request.WishReq;
import com.example.demo.response.BasicRes;
import com.example.demo.service.WishService;

import jakarta.validation.Valid;

@CrossOrigin
@RestController
public class WishController {
	@Autowired
	private WishService wishService;
	
	@PostMapping("gogobuy/wishes")
	public BasicRes create(@Valid @RequestBody WishReq req) {
		return wishService.addWish(req);
	}
	
	@PostMapping("gogobuy/follow")
	public BasicRes follow(@RequestParam("id") int id, @RequestParam("userId") String userId) {
		return wishService.setfollowers(id, userId);
	}
	
	@PostMapping("gogobuy/delete")
	public BasicRes delete(@RequestParam("id") int id, @RequestParam("userId") String userId) {
		return wishService.delWish(id, userId);
	}
}
