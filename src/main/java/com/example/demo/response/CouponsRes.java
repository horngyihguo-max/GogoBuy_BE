package com.example.demo.response;

import java.util.List;

import com.example.demo.entity.Coupons;

public class CouponsRes extends BasicRes{

	private List<Coupons> couponsList;

	public List<Coupons> getCouponsList() {
		return couponsList;
	}

	public void setCouponsList(List<Coupons> couponsList) {
		this.couponsList = couponsList;
	}

	public CouponsRes() {
		super();
	}

	public CouponsRes(int code, String message) {
		super(code, message);
	}

	public CouponsRes(int code, String message, List<Coupons> couponsList) {
		super(code, message);
		this.couponsList = couponsList;
	}
	
	
}
