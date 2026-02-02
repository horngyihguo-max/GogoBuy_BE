package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "coupons")
@IdClass(value = CouponsId.class)
public class Coupons {
	
	@Id
	@Column(name = "id")
	private int id;

	@Id
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "coupon_code")
	private String  couponCode;
	
	@Column(name = "applicable_stores")
	private String applicableStores;
	
	@Column(name = "amount_threshold")
	private Integer amountThreshold;
	
	@Column(name = "discount_max")
	private Integer discountMax;
	
	@Column(name = "discount_value")
	private String discountValue;
	
	@Column(name = "end_date")
	private String endDate;
	
	@Column(name = "is_used")
	private boolean used;
	
	@Column(name = "is_deleted")
	private boolean deleted;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getApplicableStores() {
		return applicableStores;
	}

	public void setApplicableStores(String applicableStores) {
		this.applicableStores = applicableStores;
	}

	public Integer getAmountThershold() {
		return amountThreshold;
	}

	public void setAmountThershold(Integer amountThreshold) {
		this.amountThreshold = amountThreshold;
	}

	public Integer getDiscountMax() {
		return discountMax;
	}

	public void setDiscountMax(Integer discountMax) {
		this.discountMax = discountMax;
	}

	public String getDiscountValue() {
		return discountValue;
	}

	public void setDiscountValue(String discountValue) {
		this.discountValue = discountValue;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	
}
