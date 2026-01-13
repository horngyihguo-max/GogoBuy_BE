package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.request.GroupbuyEventsReq;
import com.example.demo.response.BasicRes;
import com.example.demo.service.GroupbuyEventsService;

import jakarta.validation.Valid;

@CrossOrigin
@RestController
public class GroupbuyEventsController {
	
	@Autowired
	private GroupbuyEventsService groupbuyEventsService;

	@PostMapping("gogobuy/addEvent")
	public BasicRes addEvent(@Valid @RequestBody GroupbuyEventsReq req) {
		return groupbuyEventsService.addEvent(req);
	}
}
