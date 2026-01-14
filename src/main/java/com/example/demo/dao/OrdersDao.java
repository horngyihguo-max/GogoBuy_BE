package com.example.demo.dao;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Orders;


@Repository
public interface OrdersDao extends JpaRepository<Orders, Integer> {

	@Transactional
	@Modifying
	@Query(value ="insert into orders(events_id, user_id, menu_id, quantity, selected_option, "
			+ "personal_memo, order_time, pickup_status, pickup_time, subtotal, weight)"
			+ "values(?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11)", nativeQuery = true)
	public int addOrders( int eventsId, String userId, int menuId, int quantity, String selectedOption, String personalMemo, 
			LocalDateTime orderTime, String pickupStatus, LocalDateTime pickupTime, int subtotal, int weight);
	
	
	
}
