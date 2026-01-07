package com.example.demo.request;

import java.util.List;

import com.example.demo.constants.ValidationMsg;
import com.example.demo.vo.FeeDescriptionVo;

import jakarta.validation.constraints.NotBlank;

public class StoresReq {

	@NotBlank(message = ValidationMsg.STORE_NAME_EMPTY)
	private String storesname;

	@NotBlank(message = ValidationMsg.PHONE_EMPTY)
	private String phone;

	@NotBlank(message = ValidationMsg.CATEGORY_EMPTY)
	private String category;

	@NotBlank(message = ValidationMsg.TYPE_EMPTY)
	private String type;

	private String memo;

	private String image;

	private List<FeeDescriptionVo> fee_description;

	private boolean publish;

	public String getStoresname() {
		return storesname;
	}

	public void setStoresname(String storesname) {
		this.storesname = storesname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public List<FeeDescriptionVo> getFee_description() {
		return fee_description;
	}

	public void setFee_description(List<FeeDescriptionVo> fee_description) {
		this.fee_description = fee_description;
	}

	public boolean isPublish() {
		return publish;
	}

	public void setPublish(boolean publish) {
		this.publish = publish;
	}

}
