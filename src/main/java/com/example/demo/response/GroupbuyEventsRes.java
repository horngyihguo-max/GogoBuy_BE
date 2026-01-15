package com.example.demo.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class GroupbuyEventsRes {

	private int id;
    private String hostId;           
    private int storesId;          
    private String status;           
    private LocalDateTime endTime;   
    private int totalOrderAmount;   
    private int shippingFee;        
    private int limitation;          
    private String announcement;     
    private List<Map<String, Object>> tempMenuList; 
    private List<Integer> recommendList;
    private String recommendDescription;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getHostId() {
		return hostId;
	}
	public void setHostId(String hostId) {
		this.hostId = hostId;
	}
	public int getStoresId() {
		return storesId;
	}
	public void setStoresId(int storesId) {
		this.storesId = storesId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public LocalDateTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
	public int getTotalOrderAmount() {
		return totalOrderAmount;
	}
	public void setTotalOrderAmount(int totalOrderAmount) {
		this.totalOrderAmount = totalOrderAmount;
	}
	public int getShippingFee() {
		return shippingFee;
	}
	public void setShippingFee(int shippingFee) {
		this.shippingFee = shippingFee;
	}
	public int getLimitation() {
		return limitation;
	}
	public void setLimitation(int limitation) {
		this.limitation = limitation;
	}
	public String getAnnouncement() {
		return announcement;
	}
	public void setAnnouncement(String announcement) {
		this.announcement = announcement;
	}
	public List<Map<String, Object>> getTempMenuList() {
		return tempMenuList;
	}
	public void setTempMenuList(List<Map<String, Object>> tempMenuList) {
		this.tempMenuList = tempMenuList;
	}
	public List<Integer> getRecommendList() {
		return recommendList;
	}
	public void setRecommendList(List<Integer> recommendList) {
		this.recommendList = recommendList;
	}
	public String getRecommendDescription() {
		return recommendDescription;
	}
	public void setRecommendDescription(String recommendDescription) {
		this.recommendDescription = recommendDescription;
	}
	public GroupbuyEventsRes() {
		super();
		// TODO Auto-generated constructor stub
	}
	public GroupbuyEventsRes(int id, String hostId, int storesId, String status, LocalDateTime endTime,
			int totalOrderAmount, int shippingFee, int limitation, String announcement,
			List<Map<String, Object>> tempMenuList, List<Integer> recommendList, String recommendDescription) {
		super();
		this.id = id;
		this.hostId = hostId;
		this.storesId = storesId;
		this.status = status;
		this.endTime = endTime;
		this.totalOrderAmount = totalOrderAmount;
		this.shippingFee = shippingFee;
		this.limitation = limitation;
		this.announcement = announcement;
		this.tempMenuList = tempMenuList;
		this.recommendList = recommendList;
		this.recommendDescription = recommendDescription;
	}
    
    
}
