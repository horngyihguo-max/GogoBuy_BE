package com.example.demo.request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.example.demo.constants.GroupbuyStatusEnum;
import com.example.demo.constants.SplitTypeEnum;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class GroupbuyEventsReq {

	private int id;

	@NotBlank(message = "團長ID必填")
	private String hostId;

	@NotNull(message = "商店ID必填")
	private int storesId;

	@NotNull(message = "結單時間必填")
	@Future(message = "結單時間必須是未來")
	private LocalDateTime endTime;

	@NotNull(message = "是否開店")
	private GroupbuyStatusEnum status;

	private Integer shippingFee = 0;

	@NotNull(message = "運費分攤方式必填")
	private SplitTypeEnum splitType;

	private String announcement;

	private String type;

	private List<Map<String, Object>> tempMenuList;

	private List<Integer> recommendList;

	private String recommendDescription;

	private Integer totalOrderAmount;

	@Min(value = 1, message = "成團門檻至少1元")
	private Integer limitation = 0;

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

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public GroupbuyStatusEnum getStatus() {
		return status;
	}

	public void setStatus(GroupbuyStatusEnum status) {
		this.status = status;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public Integer getTotalOrderAmount() {
		return totalOrderAmount;
	}

	public void setTotalOrderAmount(Integer totalOrderAmount) {
		this.totalOrderAmount = totalOrderAmount;
	}

	public Integer getLimitation() {
		return limitation;
	}

	public void setLimitation(Integer limitation) {
		this.limitation = limitation;
	}

}
