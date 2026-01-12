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

	//新增開團
	@Transactional
	@Modifying
	@Query(value ="insert into groupbuy_events(host_id, stores_id, status, end_time, total_order_amount, "
			+ "shipping_fee, split_type, announcement, type, temp_menu, recommend, recommend_description, limitation)"
			+ "values(?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12, ?13)", nativeQuery = true)
	public int addEvent( String hostId, int storesId, String status, LocalDateTime endTime, String totalOrderAmount, int shippingFee, String splitType, String announcement, String type, String tempMenu, String recommend, String recommendDescription, int limitation);
}

