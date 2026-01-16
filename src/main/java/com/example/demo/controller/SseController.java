package com.example.demo.controller;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.demo.service.NotificationService;
import com.example.demo.service.SystemNotificationServer;

@RestController
@RequestMapping("/api/sse")
@CrossOrigin(origins = "*") // 允許所有來源連線 (開發方便)
public class SseController {
//   private final NotificationService notificationService;
	private final NotificationService notificationService;
    private final SystemNotificationServer systemNotificationServer;
   
    public SseController(NotificationService notificationService, SystemNotificationServer systemNotificationServer) {
        this.notificationService = notificationService;
        this.systemNotificationServer = systemNotificationServer;
    }
   
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
   
   // 管理員新增公告  api/sse/set-notice
   @PostMapping("/set-notice") 
   public String setNotice(//
		   @RequestParam String msg,
	        @RequestParam(required = false) Integer minutes,
	        @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime time) {
       LocalDateTime expiry ;
       
       
       // 判斷模式(基於現在多久後/指定時間)      
       if (time != null) {
           // 使用絕對時間
           expiry = time;
       } else if (minutes != null) {
           // 使用相對時間
           expiry = LocalDateTime.now().plusMinutes(minutes);
       } else {
           return "錯誤：必須提供 minutes 或 time 其中一個參數。";
       }
       
       //  時空旅人防呆
       if (expiry.isBefore(LocalDateTime.now())) {
           return "錯誤：失效時間不能是過去的時間。";
       }
       
       
       
       // 存入公告中心(給新登入用戶看)
       systemNotificationServer.setNotice(msg, expiry);
       
       //廣播在線用戶
        notificationService.broadcast(msg); 
       
        return String.format("公告已設定！內容：%s，失效時間：%s", msg, expiry);
   }
   

}
