package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SystemNotificationServer {

	//	volatile>>確保資料修改後，所有執行緒都能立刻看到最新值
	
    // 存放當前公告內容
    private volatile String noticeMsg = null;
    // 存放公告到期時間 (例如維護開始時間)
    private volatile LocalDateTime expiryTime = null;

    private SystemNoticeRepository systemNoticeRepository;

    public SystemNotificationServer(SystemNoticeRepository systemNoticeRepository) {
        this.systemNoticeRepository = systemNoticeRepository;
    }

    // 提供給管理員設定公告的方法
    public void setNotice(String message, LocalDateTime end) {
        this.noticeMsg = message;
        this.expiryTime = end;
        
        // 存入資料庫留底
        SystemNotice notice = new SystemNotice();
        notice.setContent(message);
        notice.setCreatedAt(LocalDateTime.now());
        notice.setExpiredAt(end);
        systemNoticeRepository.save(notice);
    }

    // 提供訂閱系統查詢
    public Optional<String> getValidNotice() {
        if (noticeMsg != null && expiryTime != null && LocalDateTime.now().isBefore(expiryTime)) {
            return Optional.of(noticeMsg);
        }
        return Optional.empty();
    }
    
    // 取得歷史公告
    public java.util.List<com.example.demo.entity.SystemNotice> getHistory() {
        return systemNoticeRepository.findAllByOrderByCreatedAtDesc();
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
