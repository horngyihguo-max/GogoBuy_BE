package com.example.demo.response;

public class ShippingFeeRes extends BasicRes {

	private double shippingFee;

	public double getShippingFee() {
		return shippingFee;
	}

	public void setShippingFee(double shippingFee) {
		this.shippingFee = shippingFee;
	}

	public ShippingFeeRes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ShippingFeeRes(int code, String message) {
		super(code, message);
		// TODO Auto-generated constructor stub
	}

	public ShippingFeeRes(int code, String message, double shippingFee) {
		super(code, message);
		this.shippingFee = shippingFee;
	}

}
