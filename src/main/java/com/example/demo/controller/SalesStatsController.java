package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.request.SalesStatsReq;
import com.example.demo.service.SalesStatsService;

@RestController
@RequestMapping("/gogobuy/salesStats")
public class SalesStatsController {

	@Autowired
    private SalesStatsService salesStatsService;

    /**
     * 模擬手動計入銷量 (測試用)
     */
    @PostMapping("/add")
    public String addSales(@RequestBody SalesStatsReq req) {
        try {
            // 呼叫 Service 執行 DAILY 與 ALL 的更新
            salesStatsService.addSalesVolume(
                req.getStoreId(), 
                req.getMenuId(), 
                req.getQuantity()
            );
            return "銷量累計成功喵！店鋪:" + req.getStoreId() + ", 品項:" + req.getMenuId();
        } catch (Exception e) {
            return "累計失敗喵... 錯誤: " + e.getMessage();
        }
    }
}
