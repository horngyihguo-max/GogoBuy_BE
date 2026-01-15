package com.example.demo.dao;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.GroupbuyEvents;
import com.example.demo.entity.Orders;

@Repository
public interface OrdersDao extends JpaRepository<Orders, Integer> {

	// 新增
	@Transactional
	@Modifying
	@Query(value = "insert into orders(events_id, user_id, menu_id, quantity, selected_option, "
			+ "personal_memo, order_time, pickup_status, pickup_time, subtotal, weight) "
			+ "values(?1, ?2, ?3, ?4, ?5, ?6, CURRENT_TIMESTAMP, ?7, ?8, ?9, ?10)", nativeQuery = true)
	public int addOrders(int eventsId, String userId, int menuId, int quantity, String selectedOption,
			String personalMemo, String pickupStatus, LocalDateTime pickupTime, int subtotal, int weight);

	// 查詢ordersId
	@Query(value = "select* from orders where id = ?1", nativeQuery = true)
	public Orders findById(int id);

	// 更新
	@Transactional
	@Modifying
	@Query(value = "update orders set " + "events_id = :eventsId, " + "user_id = :userId, " + "menu_id = :menuId, "
			+ "quantity = :quantity, " + "selected_option = :selectedOption, " + "personal_memo = :personalMemo, "
			+ "pickup_status = :pickupStatus, " + "pickup_time = :pickupTime, " + "subtotal = :subtotal, "
			+ "weight = :weight " + "where id = :id", nativeQuery = true)
	public int updateOrders(
			@Param("eventsId") int eventsId, 
			@Param("userId") String userId,
			@Param("menuId") int menuId, 
			@Param("quantity") int quantity,
			@Param("selectedOption") String selectedOption, 
			@Param("personalMemo") String personalMemo,
			@Param("pickupStatus") String pickupStatus, 
			@Param("pickupTime") LocalDateTime pickupTime,
			@Param("subtotal") int subtotal, 
			@Param("weight") int weight, 
			@Param("id") int id);

	// 統計人數 
    @Query(value = "select count (*) from orders where events_id = ?1 and is_deleted = false", nativeQuery = true)
    int countOrdersByEventId( int eventId );

	// 統計總金額
	@Query(value = "select sum(subtotal) from orders where events_id = ?1 and is_deleted = false", nativeQuery = true)
	Integer sumSubtotalByEventId(int eventId);
	
	// 查訂單ID
	@Query(value = "select* from orders where id = ?", nativeQuery = true)
	public Orders finById(int id);

	// 查詢userId在該團的總重量 
	@Query(value = "select sum(weight) from orders where events_id = ?1 AND user_id = ?2 and is_deleted = false", nativeQuery = true)
	Double sumWeightByEventAndUser(int eventId, String userId);

	// 查詢該團所有人的總重量 
	@Query(value = "select sum(weight) from orders where events_id = ?1 and is_deleted = false", nativeQuery = true)
	Double sumTotalWeightByEvent(int eventId);
	
	// 查詢userId在該團的商品小計總額
	@Query(value = "select sum(subtotal) from orders where events_id = ?1 and user_id = ?2 and is_deleted = false", nativeQuery = true)
	Integer sumSubtotalByEventAndUser(int eventId, String userId);
	
	// 軟刪除
	@Transactional
	@Modifying
	@Query(value = "update  orders set is_deleted = ?2 where user_id = ?1", nativeQuery = true)
	public int delete(String userId, boolean delete);
}
