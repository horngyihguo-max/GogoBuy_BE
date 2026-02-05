package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.demo.entity.SystemNotice;
import com.example.demo.request.NoticeReq;
import com.example.demo.response.BasicRes;
import com.example.demo.service.NotificationService;
import com.example.demo.service.SystemNotificationService;

@RestController
@RequestMapping("/api/sse")
@CrossOrigin(origins = "*") // 允許所有來源連線 (開發方便)
public class SseController {

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private SystemNotificationService systemNotificationService;

	// Client 端連線端點: GET /api/sse/subscribe/{userId}
	@GetMapping(value = "/subscribe/{userId}", produces = "text/event-stream")
	public SseEmitter subscribe(@PathVariable("userId") String userId) {
		return notificationService.subscribe(userId);
	}

	// 觸發通知端點 (模擬後台發送): POST /api/sse/send?userId=gaga&message=hello
	@PostMapping("/send")
	public String send(@RequestParam("userId") String userId, @RequestParam("message") String message) {
		notificationService.sendNotification(userId, message);
		return "Message sent to " + userId;
	}

	// 管理員新增公告 api/sse/set-notice
	@PostMapping("/set-notice")
	public BasicRes setNotice(@RequestBody NoticeReq req) {
		// 廣播在線用戶
		notificationService.broadcast(req.getContent());
		return systemNotificationService.setNotice(req);
	}

	// 取得歷史公告
	@GetMapping("/history")
	public List<SystemNotice> getHistory() {
		return systemNotificationService.getHistory();
	}

}
