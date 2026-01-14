package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.NotifiMes;

public interface NotifiMesRepository extends JpaRepository<NotifiMes, Integer>{
	/* notificationRepo 是 對應 NotificationMessage Entity 的 DAO（Data Access Object）
	   主要功能：
		   CRUD：新增、查詢、更新、刪除資料
		   自動生成 SQL：你不用自己寫 INSERT/UPDATE/SELECT
		   管理 Entity 的狀態：Hibernate 可以追蹤這個物件，幫你回填 @GeneratedValue 的 id */
}
