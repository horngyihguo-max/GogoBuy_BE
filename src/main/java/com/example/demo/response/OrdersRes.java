package com.example.demo.response;

import java.util.List;
import java.util.Map;

public class OrdersRes extends BasicRes{
	
    private int id;                
    private String userId;     
    private int eventsId;        
    private int menuId;      
    private int quantity;         
    private List<Map<String, Object>> selectedOptionList; 
    private int subtotal;        
    private String personalMemo;   
    private Integer weight;     
    private String pickupStatus;   
    private String pickupTime;    
    private String orderTime;
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
	public int getEventsId() {
		return eventsId;
	}
	public void setEventsId(int eventsId) {
		this.eventsId = eventsId;
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
	public List<Map<String, Object>> getSelectedOptionList() {
		return selectedOptionList;
	}
	public void setSelectedOptionList(List<Map<String, Object>> selectedOptionList) {
		this.selectedOptionList = selectedOptionList;
	}
	public int getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(int subtotal) {
		this.subtotal = subtotal;
	}
	public String getPersonalMemo() {
		return personalMemo;
	}
	public void setPersonalMemo(String personalMemo) {
		this.personalMemo = personalMemo;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public String getPickupStatus() {
		return pickupStatus;
	}
	public void setPickupStatus(String pickupStatus) {
		this.pickupStatus = pickupStatus;
	}
	public String getPickupTime() {
		return pickupTime;
	}
	public void setPickupTime(String pickupTime) {
		this.pickupTime = pickupTime;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public OrdersRes() {
		super();
		// TODO Auto-generated constructor stub
	}
	public OrdersRes(int id, String userId, int eventsId, int menuId, int quantity,
			List<Map<String, Object>> selectedOptionList, int subtotal, String personalMemo, Integer weight,
			String pickupStatus, String pickupTime, String orderTime) {
		super();
		this.id = id;
		this.userId = userId;
		this.eventsId = eventsId;
		this.menuId = menuId;
		this.quantity = quantity;
		this.selectedOptionList = selectedOptionList;
		this.subtotal = subtotal;
		this.personalMemo = personalMemo;
		this.weight = weight;
		this.pickupStatus = pickupStatus;
		this.pickupTime = pickupTime;
		this.orderTime = orderTime;
	}     

    
}
