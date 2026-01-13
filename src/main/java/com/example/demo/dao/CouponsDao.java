package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Coupons;

import jakarta.transaction.Transactional;

public interface CouponsDao extends JpaRepository<Coupons, Integer>{
	
//	新增優惠券
	@Modifying
	@Transactional
	@Query(value = "insert into coupons(user_id, coupon_code, applicable_stores, amount_threshold," //
			+ "discount_max, discount_value, end_date) " //
			+ " values (?1,?2,?3,?4,?5,?6,?7)", nativeQuery = true)
	public int addCoupon(String userId, String couponCode, //
			String applicableStores, int amountThreshold, int discountMax, //
			String discountValue, String endDate);
	
//查詢優惠券(考慮軟刪除)
	@Query(value = "SELECT * FROM coupons WHERE id = ?1 AND is_deleted = false", nativeQuery = true)
    public Coupons getCouponById(int id);
	
//	更新
	@Modifying
    @Transactional
    @Query(value = "UPDATE coupons SET user_id = ?2, coupon_code = ?3, applicable_stores = ?4, amount_threshold = ?5, "//
            + "discount_max = ?6, discount_value = ?7, end_date = ?8, is_used = ?9, is_deleted = ?10 "//
            + "WHERE id = ?1", nativeQuery = true)
	public int updateCoupon(int id,String userId,String couponCode,String applicableStores,Integer amountThreshold,//
			Integer discountMax,String discountValue,String endDate,boolean used,boolean deleted);
	
	//	物理刪除
	 @Modifying
	    @Transactional
	    @Query(value = "DELETE FROM coupons WHERE id = ?1", nativeQuery = true)
	    public void deleteCouponById(int id);
	 //軟刪除	 
	 @Modifying
	    @Transactional
	    @Query(value = "UPDATE coupons SET is_deleted = true WHERE id = ?1", nativeQuery = true)
	    public int softDeleteCouponById(int id);
}
