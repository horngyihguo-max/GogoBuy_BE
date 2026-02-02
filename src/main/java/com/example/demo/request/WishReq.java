package com.example.demo.request;

import java.time.LocalDate;


import com.example.demo.constants.ValidationMsg;
import com.example.demo.constants.WishTypeEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class WishReq {
	@Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$" //
			, message = ValidationMsg.USER_ID_ERROR)
	@NotBlank(message = ValidationMsg.USER_ID_EMPTY)
	private String userId;
	
	@NotBlank(message = ValidationMsg.TITLE_EMPTY)
	private String title;
	
	@NotNull(message = ValidationMsg.ANONYMOUS_EMPTY)
	private boolean anonymous;
	
	@NotNull(message = ValidationMsg.TYPE_EMPTY)
	private WishTypeEnum type;
	
	@NotBlank(message = ValidationMsg.LOCATION_EMPTY)
	private String location;
	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isAnonymous() {
		return anonymous;
	}
	public void setAnonymous(boolean anonymous) {
		this.anonymous = anonymous;
	}
	public WishTypeEnum getType() {
		return type;
	}
	public void setType(WishTypeEnum type) {
		this.type = type;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
}
