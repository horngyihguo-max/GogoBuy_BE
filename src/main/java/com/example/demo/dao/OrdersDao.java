package com.example.demo.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
	@Query(value = "update orders set events_id = ?1, user_id =?2, menu_id = ?3, quantity = ?4, selected_option = ?5,"
			+ "personal_memo = ?6, pickup_status = ?7, pickup_time = ?8, subtotal = ?9, "
			+ "weight = ?10 where id =?11 ", nativeQuery = true)
	public int updateOrders(int eventsId, String userId, int menuId, int quantity, String selectedOption,
			String personalMemo, String pickupStatus, LocalDateTime pickupTime, int subtotal, int weight, int id);

	// 統計人數
	@Query(value = "select count(*) from orders where events_id = ?1 and is_deleted = false", nativeQuery = true)
	public int countOrdersByEventId(int eventId);

	// 統計總金額
	@Transactional
	// ifnull 如果是null 就會寫 0
	@Query(value = "select ifnull(sum(subtotal),0) from orders where events_id = ?1 and is_deleted = false", nativeQuery = true)
	public int sumSubtotalByEventId(int eventId);

	// 查詢userId在該團的總重量
	@Query(value = "select weight from orders where events_id = ?1 AND user_id = ?2 and is_deleted = false", nativeQuery = true)
	public Double sumWeightByEventIdAndUserId(int eventId, String userId);

	// 查詢該團所有人的總重量
	@Query(value = "select sum(weight) from orders where events_id = ?1 and is_deleted = false", nativeQuery = true)
	public Double sumTotalWeightByEventId(int eventId);

	// 查詢userId在該團的商品小計總額
	@Query(value = "select subtotal from orders where events_id = ?1 and user_id = ?2 and is_deleted = false", nativeQuery = true)
	public Integer sumSubtotalByEventIdAndUserId(int eventId, String userId);

	// userId 檢索 order 所有的跟團紀錄
	@Query(value = "select * from orders where user_id = ?1 and is_deleted = false ", nativeQuery = true)
	public List<Orders> getOrdersByUserId(String userId);
	
	// userId 和 eventsId 找訂單所有商品
	@Query(value = "select * from orders where user_id = ?1 and events_id = ?2 and is_deleted = false ", nativeQuery = true)
	public List<Orders> getEventIdByUserId(String userId , int eventsId);

	// userId 和 eventsId 找訂單所有商品
		@Query(value = "select * from orders where user_id = ?1 and events_id = ?2 and is_deleted = false ", nativeQuery = true)
		public int getOrderByUserIdAndEventsId(String userId , int eventsId);
		
	// 檢查 user_id 是否已在特定團購下過單
	@Query(value = "select count(*) from orders where user_id = ?1 and events_id = ?2 and is_deleted = false", nativeQuery = true)
	public int CheckOrderByUserIdAndEventsId(String userId, int eventsId);

	// 根據 eventsId 去查詢 shippingFee
	@Query(value = "select shipping_fee from groupbuy_events where id = ?1 ", nativeQuery = true)
	public Integer getShippingFeeByEventId(int id);

	// 軟刪除 eventId跟userId找的特定欄位
	@Transactional
	@Modifying
	@Query(value = "update orders set is_deleted = true where user_id = ?1 and events_id = ?2 and is_deleted = false", nativeQuery = true)
	public int deleteOrderByUserIdAndEventsId(String userId, int eventsId);

	// 軟刪除全部子表
	@Transactional
	@Modifying
	@Query(value = "update orders set is_deleted = true where events_id = ?1 and is_deleted = false", nativeQuery = true)
	public void deleteAllOrdersByEventId(int eventsId);
	
	//物理刪除order
	@Transactional
	@Modifying
	@Query(value = "delete from orders where user_id = ?1 and events_id = ?2 ", nativeQuery = true)
	public int hardDelete(String userId, int eventsId);

	// 更新領取狀態
	@Transactional
	@Modifying
	@Query(value = "update orders set pickup_status = 'PICKED_UP',pickup_time = now() where events_id = ?1  and user_id = ?2 and is_deleted = false", nativeQuery = true)
	public void updateStatusByEventAndUser(int eventsId, String userId);
	
	// 用 eventsId  查詢 subTotal 
	@Query(value = "select subtotal from orders where events_id = ?1 and is_deleted = false", nativeQuery = true)
	public Integer getSubTotalByEventsId(int eventsId);
	
	//找有誰是這個eventsId的不重複跟團者
	@Query(value = "select user_id as userId from orders where events_id = ?1 and is_deleted = false  group by user_id", nativeQuery = true)
	public List<String> getUserIdByEventsId(int eventsId);
	
	//找這個eventsId的跟團者的資料
	//拿來做自動生產
	@Query(value = "select * from orders where events_id =?1 and is_deleted = false ", nativeQuery = true)
	public List<Orders> getUserAllByEventsId(int eventsId);
	
	//用 userId 查詢 selectedOption 
	@Query(value = "select selected_option from orders where user_id =?1 and is_deleted = false ", nativeQuery = true)
	public List<Orders> getselectedOptionByUserId(int userId);
	
}
