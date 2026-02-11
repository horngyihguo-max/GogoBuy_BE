package com.example.demo.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.example.demo.constants.SalesStatsType;
import com.example.demo.dao.StoresSearchDao;
import com.example.demo.entity.SalesStats;
import com.example.demo.projection.SalesLeaderboardProjection;
import com.example.demo.repository.SalesStatsRepository;
import com.example.demo.response.SalesStatsRes;

import jakarta.transaction.Transactional;

@Service
public class SalesStatsService {
	@Autowired
    private SalesStatsRepository salesStatsRepository;
	
	@Autowired
    private StoresSearchDao storesSearchDao;

	@Transactional
    public SalesStatsRes addSalesVolume(Integer storeId, Integer menuId, int quantity) {
        try {
        	
            if (!storesSearchDao.existsById(storeId)) {
                return new SalesStatsRes(404, "審核失敗：商店 ID " + storeId + " 不存在喵！");
            }

            if (storesSearchDao.getMenuByMenuId(menuId) == null) {
                return new SalesStatsRes(404, "審核失敗：商品 ID " + menuId + " 不存在喵！");
            }
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
            return new SalesStatsRes(500, "同步更新失敗喵...: " + e.getMessage());
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
    
    public SalesStatsRes getTop10SalesBystore(Integer storeId, SalesStatsType type) {
        try {
            // 將 Enum 轉為 String 以符合 nativeQuery 參數
            String typeStr = (type != null) ? type.name() : SalesStatsType.ALL.name();
            
            List<SalesLeaderboardProjection> details = salesStatsRepository.findTop10WithDetails(storeId, typeStr);
            if (CollectionUtils.isEmpty(details)) {
            	return new SalesStatsRes(404,"目前該店沒有銷售紀錄喵!快去買喵!");
            }
            SalesStatsRes res = new SalesStatsRes(200, "獲取排行成功喵！");
            res.setSalesDetailList(details); 
            return res;
        } catch (Exception e) {
            return new SalesStatsRes(500, "失敗喵...：" + e.getMessage());
        }
    }
    
    public SalesStatsRes getTop10(SalesStatsType type) {
        try {
            String typeStr = (type != null) ? type.name() : SalesStatsType.ALL.name();
            List<SalesLeaderboardProjection> list = salesStatsRepository.findGlobalTop10(typeStr);
            if (CollectionUtils.isEmpty(list)) {
            	return new SalesStatsRes(404,"任何店都沒有銷售紀錄喵!快去買喵!");
            }
            SalesStatsRes res = new SalesStatsRes(200, "獲取全平台排行成功喵！");
            res.setSalesDetailList(list);
            return res;
        } catch (Exception e) {
            return new SalesStatsRes(500, "獲取全平台排行失敗喵：" + e.getMessage());
        }
    }

}
