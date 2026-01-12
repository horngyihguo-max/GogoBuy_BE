package com.example.demo.request;

import java.time.LocalDateTime;

import com.example.demo.constants.SplitTypeEnum;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class GroupbuyEventsReq {

	@NotBlank(message = "團長ID必填")
    private String hostId;

    @NotNull(message = "商店ID必填")
    private int storesId;

    @NotNull(message = "結單時間必填")
    @Future(message = "結單時間必須是未來")
    private LocalDateTime endTime;

    private int shippingFee = 0;     
    
    @NotNull(message = "運費分攤方式必填")
    private SplitTypeEnum splitType;

    private String announcement;         
    
    private String type;                  
    
    private String tempMenu;            
    
    private String recommend;             
    
    private String recommendDescription;  

    @Min(value = 1, message = "成團門檻至少1元")
    private int limitation = 0;

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

	public int getShippingFee() {
		return shippingFee;
	}

	public void setShippingFee(int shippingFee) {
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

	public String getTempMenu() {
		return tempMenu;
	}

	public void setTempMenu(String tempMenu) {
		this.tempMenu = tempMenu;
	}

	public String getRecommend() {
		return recommend;
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}

	public String getRecommendDescription() {
		return recommendDescription;
	}

	public void setRecommendDescription(String recommendDescription) {
		this.recommendDescription = recommendDescription;
	}

	public int getLimitation() {
		return limitation;
	}

	public void setLimitation(int limitation) {
		this.limitation = limitation;
	}

	public GroupbuyEventsReq() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GroupbuyEventsReq(@NotBlank(message = "團長ID必填") String hostId, @NotNull(message = "商店ID必填") int storesId,
			@NotNull(message = "結單時間必填") @Future(message = "結單時間必須是未來") LocalDateTime endTime, int shippingFee,
			@NotNull(message = "運費分攤方式必填") SplitTypeEnum splitType, String announcement, String type, String tempMenu,
			String recommend, String recommendDescription, @Min(value = 1, message = "成團門檻至少1元") int limitation) {
		super();
		this.hostId = hostId;
		this.storesId = storesId;
		this.endTime = endTime;
		this.shippingFee = shippingFee;
		this.splitType = splitType;
		this.announcement = announcement;
		this.type = type;
		this.tempMenu = tempMenu;
		this.recommend = recommend;
		this.recommendDescription = recommendDescription;
		this.limitation = limitation;
	}  
    
    
}
