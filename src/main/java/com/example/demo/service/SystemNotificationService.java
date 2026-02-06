package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.constants.ResMessage;
import com.example.demo.dao.SystemNoticeDao;
import com.example.demo.entity.SystemNotice;
import com.example.demo.request.NoticeReq;
import com.example.demo.response.BasicRes;

import jakarta.transaction.Transactional;

@Service
public class SystemNotificationService {

	// volatile>>確保資料修改後，所有執行緒都能立刻看到最新值

	// 存放當前公告內容
	private volatile String noticeMsg = null;
	// 存放公告到期時間
	private volatile LocalDateTime expiryTime = null;

	@Autowired
	private SystemNoticeDao systemNoticeDao;

	// 提供給管理員設定公告的方法
	@Transactional(rollbackOn = Exception.class)
	public BasicRes setNotice(NoticeReq req) {
		String content = req.getContent();
		LocalDateTime createdAt = req.getCreatedAt();
		if (createdAt == null) {
			createdAt = LocalDateTime.now();
		}
		LocalDateTime expiredAt = req.getExpiredAt();
		int res = systemNoticeDao.addNotice(content, createdAt, expiredAt);
		if (res == 1) {
			return new BasicRes(ResMessage.SUCCESS.getCode(), //
					ResMessage.SUCCESS.getMessage());
		} else {
			return new BasicRes(ResMessage.NOTICE_ERROR.getCode(), //
					ResMessage.NOTICE_ERROR.getMessage());
		}
	}

	// 提供訂閱系統查詢
	public Optional<String> getValidNotice() {
		if (noticeMsg != null && expiryTime != null && LocalDateTime.now().isBefore(expiryTime)) {
			return Optional.of(noticeMsg);
		}
		return Optional.empty();
	}

	// 取得歷史公告
	public List<SystemNotice> getHistory() {
		return systemNoticeDao.findAllNotice();
	}

	// 每分鐘檢查一次，如果過期了就釋放資源
	@Scheduled(fixedRate = 60000)
	public void autoCleanup() {
		if (expiryTime != null && LocalDateTime.now().isAfter(expiryTime)) {
			System.out.println("偵測到公告已過期，執行自動清理...");
			this.noticeMsg = null;
			this.expiryTime = null;
		}
	}

	// 手動清理
	public void clearNoticeManually() {
		this.noticeMsg = null;
		this.expiryTime = null;
	}

}
