package com.example.demo.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.service.WishService;

@Component
@EnableScheduling
public class WishSchedule {
	@Autowired
	private WishService wishService;
	
	@Scheduled(cron = "0 0 0 1 * ?")
    public void wishTimesReset() {
        wishService.wishTimesReset();
    }
}
