package com.example.demo.response;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.constants.GroupbuyStatusEnum;
import com.example.demo.constants.SplitTypeEnum;
import com.example.demo.entity.GroupsSearchView;

public class GroupsSearchViewRes extends BasicRes{

	private int eventId;

	private String eventName;
	
	private String hostNickname;
	
	private int storeId;
	
	private GroupbuyStatusEnum eventStatus;

	private LocalDateTime endTime;

	private int totalOrderAmount;

	private Integer shippingFee;

	private SplitTypeEnum splitType;

	private String announcement;
	
	private String eventType;

	private String tempMenuList;

	private String recommendList;

	private String recommendDescription;

	private Integer limitation;

	private String storeName;

	private String storeCategory;

	private String hostId;

	private String hostAvatar;

	private boolean deleted;

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getHostNickname() {
		return hostNickname;
	}

	public void setHostNickname(String hostNickname) {
		this.hostNickname = hostNickname;
	}

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public GroupbuyStatusEnum getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(GroupbuyStatusEnum eventStatus) {
		this.eventStatus = eventStatus;
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

	public Integer getShippingFee() {
		return shippingFee;
	}

	public void setShippingFee(Integer shippingFee) {
		this.shippingFee = shippingFee;
	}

	public SplitTypeEnum getSplitType() {
		return splitType;
	}

	public void setSplitType(SplitTypeEnum splitType) {
		this.splitType = splitType;
	}

	public String getAnnouncement() {
		return announcement;
	}

	public void setAnnouncement(String announcement) {
		this.announcement = announcement;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getTempMenuList() {
		return tempMenuList;
	}

	public void setTempMenuList(String tempMenuList) {
		this.tempMenuList = tempMenuList;
	}

	public String getRecommendList() {
		return recommendList;
	}

	public void setRecommendList(String recommendList) {
		this.recommendList = recommendList;
	}

	public String getRecommendDescription() {
		return recommendDescription;
	}

	public void setRecommendDescription(String recommendDescription) {
		this.recommendDescription = recommendDescription;
	}

	public Integer getLimitation() {
		return limitation;
	}

	public void setLimitation(Integer limitation) {
		this.limitation = limitation;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStoreCategory() {
		return storeCategory;
	}

	public void setStoreCategory(String storeCategory) {
		this.storeCategory = storeCategory;
	}

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	public String getHostAvatar() {
		return hostAvatar;
	}

	public void setHostAvatar(String hostAvatar) {
		this.hostAvatar = hostAvatar;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public GroupsSearchViewRes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GroupsSearchViewRes(int code, String message) {
		super(code, message);
		// TODO Auto-generated constructor stub
	}

	public GroupsSearchViewRes(int code, String message, int eventId, String eventName, String hostNickname,
			int storeId, GroupbuyStatusEnum eventStatus, LocalDateTime endTime, int totalOrderAmount,
			Integer shippingFee, SplitTypeEnum splitType, String announcement, String eventType, String tempMenuList,
			String recommendList, String recommendDescription, Integer limitation, String storeName,
			String storeCategory, String hostId, String hostAvatar, boolean deleted) {
		super(code, message);
		this.eventId = eventId;
		this.eventName = eventName;
		this.hostNickname = hostNickname;
		this.storeId = storeId;
		this.eventStatus = eventStatus;
		this.endTime = endTime;
		this.totalOrderAmount = totalOrderAmount;
		this.shippingFee = shippingFee;
		this.splitType = splitType;
		this.announcement = announcement;
		this.eventType = eventType;
		this.tempMenuList = tempMenuList;
		this.recommendList = recommendList;
		this.recommendDescription = recommendDescription;
		this.limitation = limitation;
		this.storeName = storeName;
		this.storeCategory = storeCategory;
		this.hostId = hostId;
		this.hostAvatar = hostAvatar;
		this.deleted = deleted;
	}

	public GroupsSearchViewRes(int i, String string, List<GroupsSearchView> list) {
		// TODO Auto-generated constructor stub
	}
	
	
}
