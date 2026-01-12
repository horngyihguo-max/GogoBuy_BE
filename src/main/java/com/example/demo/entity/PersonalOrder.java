package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "persona_order")
public class PersonalOrder {

	@Id
	@Column(name = "id")
	private int id;
	

	@Column(name = "events_id")
	private int eventsId;
	
	@Column(name = "user_id")
	private int userId;
	
	@Column(name = "total_weightr")
	private int totalWeight;
	
	@Column(name = "person_fee")
	private int personFee;
	
	@Column(name = "total_sum")
	private int totalSum;
	
	@Column(name = "payment_status")
	private int paymentStatus;
	
	@Column(name = "payment_time")
	private int paymentTime;

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

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(int totalWeight) {
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

	public int getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(int paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public int getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(int paymentTime) {
		this.paymentTime = paymentTime;
	}
	
	
}
