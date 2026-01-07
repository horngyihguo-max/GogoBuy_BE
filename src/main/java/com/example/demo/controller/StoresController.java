package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.request.StoresReq;
import com.example.demo.response.BasicRes;
import com.example.demo.service.StoreService;

import jakarta.validation.Valid;

@CrossOrigin
@RestController
public class StoresController {

	@Autowired
	private StoreService storeService;

	@PostMapping("gogobuy/store/create")
	public BasicRes create(@Valid @RequestBody StoresReq req) throws Exception {
		return storeService.create(req);
	}
}
