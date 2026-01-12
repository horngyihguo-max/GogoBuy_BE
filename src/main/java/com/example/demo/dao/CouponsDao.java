package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Coupons;

import jakarta.transaction.Transactional;

public interface CouponsDao extends JpaRepository<Coupons, Integer>{
	
//	新增店家
	@Modifying
	@Transactional
	@Query(value = "insert into coupons(id, user_id, coupon_code, applicable_stores, amount_theshold," //
			+ "discount_max, discount_value, end_date, is_used, is_deleted) " //
			+ " values (?1,?2,?3,?4,?5,?6,?7,?8,?9,?10)", nativeQuery = true)
	public int addCoupon(int id, String userId, String couponCode, //
			String applicableStores, int amount_theshold, int discountMax, //
			String discountValue, String endDate, boolean used, boolean deleted);

}
