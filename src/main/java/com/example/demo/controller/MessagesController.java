package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.request.NotifiMesReq;
import com.example.demo.response.BasicRes;
import com.example.demo.response.MessagesRes;
import com.example.demo.service.MessagesService;

import jakarta.validation.Valid;

@CrossOrigin
@RestController
public class MessagesController {
	
	@Autowired
	private MessagesService messagesService;
	
	@PostMapping("gogobuy/messages/create")
	public BasicRes create(@Valid @RequestBody NotifiMesReq req) {
	    try {
	        return messagesService.create(req);
	    } catch (Exception e) {
	        // 捕捉 Service 丟出的 throw new Exception("...")
	        // 回傳自定義的錯誤代碼（例如 400）與 Exception 的 Message
	        return new BasicRes(400, e.getMessage());
	    }
	}
	
	@PostMapping("gogobuy/messages/update")
	public MessagesRes update(@RequestParam("id") int id, @Valid @RequestBody NotifiMesReq req) {
	    try {
	        return messagesService.update(id, req);
	    } catch (Exception e) {
	        return new MessagesRes(400, e.getMessage());
	    }
	}
	
	@PostMapping("gogobuy/messages/userDelete")
	public BasicRes userDelete(@RequestParam("userId") String userId, @RequestParam("notifId") int notifId) {
	    try {
	        return messagesService.deleteByUser(userId, notifId);
	    } catch (Exception e) {
	        return new BasicRes(400, e.getMessage());
	    }
	}
	
	@PostMapping("gogobuy/messages/delete")
	public BasicRes delete(@RequestParam("notifId") int notifId) {
	    try {
	        return messagesService.delete(notifId);
	    } catch (Exception e) {
	        return new BasicRes(400, e.getMessage());
	    }
	}
	
	@GetMapping("gogobuy/messages/searchUser")
	public MessagesRes searchByUser(@RequestParam(name = "userId") String userId) {
		try {
			return messagesService.searchByUser(userId);
		} catch (Exception e) {
			return new MessagesRes(400, e.getMessage());
		}
	}

}
