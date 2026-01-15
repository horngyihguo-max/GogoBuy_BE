package com.example.demo.dao;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.GroupbuyEvents;

@Repository
public interface GroupbuyEventsDao extends JpaRepository<GroupbuyEvents, Integer> {

	// 新增開團
	@Transactional
	@Modifying
	@Query(value = "insert into groupbuy_events(host_id, stores_id, status, end_time, total_order_amount, "
			+ "shipping_fee, split_type, announcement, type, temp_menu, recommend, recommend_description, limitation)"
			+ "values(?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12, ?13)", nativeQuery = true)
	public int addEvent(String hostId, int storesId, String status, LocalDateTime endTime, Integer totalOrderAmount,
			Integer shippingFee, String splitType, String announcement, String type, String temp_menu, String recommend,
			String recommendDescription, Integer limitation);

	// 查所屬團ID
	@Query(value = "select* from groupbuy_events where id = ?", nativeQuery = true)
	public GroupbuyEvents findById(int id);

	// 更新團
	@Transactional
	@Modifying
	@Query(value = "update groupbuy_events set " + "host_id = ?1, stores_id = ?2, status = ?3, end_time = ?4, "
			+ "total_order_amount = ?5, shipping_fee = ?6, split_type = ?7, "
			+ "announcement = ?8, type = ?9, temp_menu = ?10, recommend = ?11, "
			+ "recommend_description = ?12, limitation = ?13 " + "where id = ?14", nativeQuery = true)
	public int updateEvent(String hostId, int storesId, String status, LocalDateTime endTime, Integer totalOrderAmount,
			Integer shippingFee, String splitType, String announcement, String type, String tempMenu, String recommend,
			String recommendDescription, Integer limitation, int id);

	// 更新總金額
	@Transactional
	@Modifying
	@Query(value = "update groupbuy_events set total_order_amount = ?1 where id = ?2 and is_deleted = false ", nativeQuery = true)
	public int updateEventStats(int totalOrderAmount, int id);

	// 軟刪除
	@Transactional
	@Modifying
	@Query(value = "update  orders set is_deleted = ?2 where user_id = ?1", nativeQuery = true)
	public int delete(String userId, boolean delete);
}
