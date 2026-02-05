package com.example.demo.service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/*關於多併發
 * 現在在locolHost測試只會是http://localhost(http1)
 * 會有連線數6的限制
 * 但如果實裝在http2(https)就可以不用考慮此問題
 * */
@Service
public class NotificationService {

	@Autowired
	private SystemNotificationService systemNotificationService;

	private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

	// 1. 用戶訂閱 (建立連線)
	public SseEmitter subscribe(String userId) {
		if (emitters.containsKey(userId)) {
			// complete() 會觸發舊連線的終止邏輯，確保資源釋放
			emitters.get(userId).complete();
		}
		// 設定超時時間，0 表示無限 (或設定例如 30分鐘: 1800000L)
		SseEmitter emitter = new SseEmitter(1_800_000L);
		// 存入 Map
		emitters.put(userId, emitter);
		// 連線結束或超時時，從 Map 中移除
		// 關閉瀏覽器
		emitter.onCompletion(() -> emitters.remove(userId));
		// 超時
		emitter.onTimeout(() -> emitters.remove(userId));
		// 斷網
		emitter.onError((e) -> emitters.remove(userId));
		try {
			// 發送一個初始事件，確認連線成功
			emitter.send(SseEmitter.event().name("INIT").data("Connected successfully"));
			// 檢查>>如果有 發送系統公告訊息(例:30分鐘後維護) notice>>公告
			systemNotificationService.getValidNotice().ifPresent(notice -> {
				try {
					emitter.send(SseEmitter.event().name("SYSTEM_NOTICE").data(notice));
				} catch (IOException e) {
					// 靜默處理，等待清理機制
				}
			});
		} catch (IOException e) {
			emitters.remove(userId);
		}
		return emitter;
	}

	// 2. 發送通知給特定用戶
	public void sendNotification(String userId, String message) {
		SseEmitter emitter = emitters.get(userId);
		if (emitter != null) {
			try {
				emitter.send(SseEmitter.event().name("message") // 前端監聽的事件名稱
						.data(message));
			} catch (IOException e) {
				// 發送失敗通常代表連線已斷，移除該 Emitter
				emitters.remove(userId);
			}
		}
	}

	// 在線廣播
	public void broadcast(String message) {
		emitters.forEach((userId, emitter) -> {
			try {
				emitter.send(SseEmitter.event().name("SYSTEM_NOTICE").data(message));
			} catch (IOException e) {
				emitters.remove(userId); // 發現斷線則清理
			}
		});
	}
}
