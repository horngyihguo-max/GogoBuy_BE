package com.example.demo.vo;

public class FeeDescriptionVo {

	private int km;

	private int fee;

	public int getKm() {
		return km;
	}

	public void setKm(int km) {
		this.km = km;
	}

	public int getFee() {
		return fee;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

	public FeeDescriptionVo() {
		super();
	}

	public FeeDescriptionVo(int km, int fee) {
		super();
		this.km = km;
		this.fee = fee;
	}

}
