package com.example.demo.request;

import jakarta.validation.constraints.NotBlank;

public class CouponReq {
	
	
	private int  id;

	@NotBlank(message = "USER_ID_EMPTY")
	private String userId;
	
	@NotBlank(message = "COUPON_CODE_EMPTY")
	private String couponCode;
	
	private String applicableStores;
	
	private Integer amountThreshold;
	
	private Integer discountMax;
	
	private String discountValue;
	
	private String endDate;
	
	private boolean used;
	
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

	public Integer getAmountThreshold() {
		return amountThreshold;
	}

	public void setAmountThreshold(Integer amountThreshold) {
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
