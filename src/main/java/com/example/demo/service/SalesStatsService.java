package com.example.demo.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.constants.SalesStatsType;
import com.example.demo.entity.SalesStats;
import com.example.demo.repository.SalesStatsRepository;
import com.example.demo.response.SalesStatsRes;

import jakarta.transaction.Transactional;

@Service
public class SalesStatsService {
	@Autowired
    private SalesStatsRepository salesStatsRepository;

	@Transactional
    public SalesStatsRes addSalesVolume(Integer storeId, Integer menuId, int quantity) {
        try {
            LocalDate today = LocalDate.now();

            // 今日 (DAILY) - 基準日：今天
            updateStatsLogic(storeId, menuId, SalesStatsType.DAILY, today, quantity);

            // 本週 (WEEKLY) - 基準日：本週週一
            LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            updateStatsLogic(storeId, menuId, SalesStatsType.WEEKLY, startOfWeek, quantity);

            // 本月 (MONTHLY) - 基準日：本月 1 號
            LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
            updateStatsLogic(storeId, menuId, SalesStatsType.MONTHLY, startOfMonth, quantity);

            // 年度 (YEAR) - 基準日：本年 1 月 1 號
            LocalDate startOfYear = today.with(TemporalAdjusters.firstDayOfYear());
            updateStatsLogic(storeId, menuId, SalesStatsType.YEAR, startOfYear, quantity);

            // 總累計 (ALL) - 基準日：null
            updateStatsLogic(storeId, menuId, SalesStatsType.ALL, null, quantity);

            return new SalesStatsRes(200, "全週期銷量同步更新成功喵！");
        } catch (Exception e) {
            return new SalesStatsRes(500, "同步更新失敗喵: " + e.getMessage());
        }
    }

    private void updateStatsLogic(Integer storeId, Integer menuId, SalesStatsType type, LocalDate date, int quantity) {
        SalesStats stats = salesStatsRepository
            .findByStoreIdAndMenuIdAndStatsTypeAndStatsDate(storeId, menuId, type, date)
            // 有資料拿資料 沒資料建資料           
            .orElseGet(() -> {
                SalesStats s = new SalesStats();
                s.setStoreId(storeId);
                s.setMenuId(menuId);
                s.setStatsType(type);
                s.setStatsDate(date);
                s.setSalesVolume(0);
                return s;
            });
        stats.setSalesVolume(stats.getSalesVolume() + quantity);
        salesStatsRepository.save(stats);
    }

}
