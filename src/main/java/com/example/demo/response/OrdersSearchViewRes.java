package com.example.demo.response;

import java.time.LocalDateTime;

import com.example.demo.constants.PickupStatusEnum;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

public class OrdersSearchViewRes extends BasicRes{

	private int orderId;
	
	private int eventsId;

	private String userId;

	private int menuId;
	
	private int quantity;

	private String selectedOption;

	private String personalMemo;

	private LocalDateTime orderTime;
	
	//資料庫會直接儲存 列舉名稱的字串
	@Enumerated(EnumType.STRING) 
	private PickupStatusEnum pickupStatus;
	
	private LocalDateTime pickupTime;

	private int subtotal;

	private double weight;
	
	private boolean deleted;

	private String menuName;

	private String hostId;

	private String hostNickname;

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
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

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getSelectedOption() {
		return selectedOption;
	}

	public void setSelectedOption(String selectedOption) {
		this.selectedOption = selectedOption;
	}

	public String getPersonalMemo() {
		return personalMemo;
	}

	public void setPersonalMemo(String personalMemo) {
		this.personalMemo = personalMemo;
	}

	public LocalDateTime getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(LocalDateTime orderTime) {
		this.orderTime = orderTime;
	}

	public PickupStatusEnum getPickupStatus() {
		return pickupStatus;
	}

	public void setPickupStatus(PickupStatusEnum pickupStatus) {
		this.pickupStatus = pickupStatus;
	}

	public LocalDateTime getPickupTime() {
		return pickupTime;
	}

	public void setPickupTime(LocalDateTime pickupTime) {
		this.pickupTime = pickupTime;
	}

	public int getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(int subtotal) {
		this.subtotal = subtotal;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	public String getHostNickname() {
		return hostNickname;
	}

	public void setHostNickname(String hostNickname) {
		this.hostNickname = hostNickname;
	}

	public OrdersSearchViewRes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OrdersSearchViewRes(int code, String message) {
		super(code, message);
		// TODO Auto-generated constructor stub
	}

	public OrdersSearchViewRes(int code, String message, int orderId, int eventsId, String userId, int menuId,
			int quantity, String selectedOption, String personalMemo, LocalDateTime orderTime,
			PickupStatusEnum pickupStatus, LocalDateTime pickupTime, int subtotal, double weight, boolean deleted,
			String menuName, String hostId, String hostNickname) {
		super(code, message);
		this.orderId = orderId;
		this.eventsId = eventsId;
		this.userId = userId;
		this.menuId = menuId;
		this.quantity = quantity;
		this.selectedOption = selectedOption;
		this.personalMemo = personalMemo;
		this.orderTime = orderTime;
		this.pickupStatus = pickupStatus;
		this.pickupTime = pickupTime;
		this.subtotal = subtotal;
		this.weight = weight;
		this.deleted = deleted;
		this.menuName = menuName;
		this.hostId = hostId;
		this.hostNickname = hostNickname;
	}

	
}
