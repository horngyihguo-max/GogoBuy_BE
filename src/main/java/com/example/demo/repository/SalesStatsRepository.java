package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.constants.SalesStatsType;
import com.example.demo.entity.SalesStats;

@Repository
public interface SalesStatsRepository extends JpaRepository<SalesStats, Long> {

    // 用於更新銷量時尋找現有紀錄
    Optional<SalesStats> findByStoreIdAndMenuIdAndStatsTypeAndStatsDate(
            Integer storeId, Integer menuId, SalesStatsType statsType, LocalDate statsDate);

    // 查詢某家店、某種週期下的熱銷排名 (前 N 名)
    List<SalesStats> findTop10ByStoreIdAndStatsTypeOrderBySalesVolumeDesc(
            Integer storeId, SalesStatsType statsType);

    // 查詢某週期內「全平台」最火熱的商品 (不限店家)
    @Query("SELECT s FROM SalesStats s WHERE s.statsType = :type " +
           "AND (:date IS NULL OR s.statsDate = :date) " +
           "ORDER BY s.salesVolume DESC")
    List<SalesStats> findGlobalTopSales(@Param("type") SalesStatsType type, 
                                        @Param("date") LocalDate date, 
                                        Pageable pageable);
    
    //清理舊資料 (例如：刪除一年前的每日統計，保持資料表輕量)
    void deleteByStatsTypeAndStatsDateBefore(SalesStatsType type, LocalDate date);
}