package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.NotifiMes;

public interface NotifiMesRepository extends JpaRepository<NotifiMes, Integer> {
	/*
	 * notificationRepo 是 對應 NotificationMessage Entity 的 DAO（Data Access Object）
	 * 主要功能： CRUD：新增、查詢、更新、刪除資料 
	 * 自動生成 SQL：不用寫 INSERT/UPDATE/SELECT 管理 Entity的狀態
	 * Hibernate 可以追蹤該物件並回填 @GeneratedValue 的 id
	 */
}
