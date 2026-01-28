package com.example.demo.response;

import java.time.LocalDateTime;

import com.example.demo.constants.PaymentStatus;

public class PersonalOrdersRes extends BasicRes{

	private int id;
	
	private int eventsId;

	private String userId;

	private Double totalWeight;

	private int personFee;

	private int totalSum;

	private PaymentStatus paymentStatus;

	private LocalDateTime paymentTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEventsId() {
		return eventsId;
	}

	public void setEventsId(int eventsId) {
		this.eventsId = eventsId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Double getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(Double totalWeight) {
		this.totalWeight = totalWeight;
	}

	public int getPersonFee() {
		return personFee;
	}

	public void setPersonFee(int personFee) {
		this.personFee = personFee;
	}

	public int getTotalSum() {
		return totalSum;
	}

	public void setTotalSum(int totalSum) {
		this.totalSum = totalSum;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public LocalDateTime getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(LocalDateTime paymentTime) {
		this.paymentTime = paymentTime;
	}

	public PersonalOrdersRes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PersonalOrdersRes(int code, String message) {
		super(code, message);
		// TODO Auto-generated constructor stub
	}

	public PersonalOrdersRes(int code, String message, int id, int eventsId, String userId, Double totalWeight,
			int personFee, int totalSum, PaymentStatus paymentStatus, LocalDateTime paymentTime) {
		super(code, message);
		this.id = id;
		this.eventsId = eventsId;
		this.userId = userId;
		this.totalWeight = totalWeight;
		this.personFee = personFee;
		this.totalSum = totalSum;
		this.paymentStatus = paymentStatus;
		this.paymentTime = paymentTime;
	}
	
	
}
