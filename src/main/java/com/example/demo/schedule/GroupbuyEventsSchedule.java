package com.example.demo.schedule;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.constants.GroupbuyStatusEnum;
import com.example.demo.dao.GroupbuyEventsDao;

@Service
public class GroupbuyEventsSchedule {
	
	@Autowired
    private GroupbuyEventsDao groupbuyEventsDao;
	
    // "0 * * * * *" 代表「每分鐘的第一秒」執行一次
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void autoUpdateEventsStatus() {
        // 在 DAO (SQL) 裡面做比較，找到「時間小於現在」且「狀態還是 OPEN」的團
        int count = groupbuyEventsDao.autoUpdateEventsStatus(
            GroupbuyStatusEnum.FINISHED.name(), 
            LocalDateTime.now(), 
            GroupbuyStatusEnum.OPEN.name()
        );
        if (count > 0) {
            System.out.println("已成功自動結單 " + count + " 筆。");
        }
    }
}
