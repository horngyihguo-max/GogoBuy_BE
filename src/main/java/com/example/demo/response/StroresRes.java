package com.example.demo.response;

import java.util.List;

import com.example.demo.entity.Stores;
import com.example.demo.vo.FeeDescriptionVo;
import com.example.demo.vo.MenuCategoriesVo;
import com.example.demo.vo.MenuVo;
import com.example.demo.vo.ProductOptionGroupsVo;
import com.example.demo.vo.StoreOperatingHoursVo;

public class StroresRes  extends BasicRes{

	private List<Stores> storeList;
	
	private List<StoreOperatingHoursVo> operatingHoursVoList;
    private List<MenuVo> menuVoList;
    private List<MenuCategoriesVo> menuCategoriesVoList;
    private List<ProductOptionGroupsVo> productOptionGroupsVoList;
    private List<FeeDescriptionVo> feeDescriptionVoList;
    
    public List<FeeDescriptionVo> getFeeDescriptionVoList() {
		return feeDescriptionVoList;
	}
	public void setFeeDescriptionVoList(List<FeeDescriptionVo> list) { this.feeDescriptionVoList = list; }
	public List<Stores> getStoreList() {
		return storeList;
	}

	public void setStoreList(List<Stores> storeList) {
		this.storeList = storeList;
	}

	public StroresRes() {
		super();
	}

	public StroresRes(int code, String message) {
		super(code, message);
	}

	public StroresRes(int code, String message, List<Stores> storeList) {
		super(code, message);
		this.storeList = storeList;
	}

	public List<StoreOperatingHoursVo> getOperatingHoursVoList() {
		return operatingHoursVoList;
	}

	public void setOperatingHoursVoList(List<StoreOperatingHoursVo> operatingHoursVoList) {
		this.operatingHoursVoList = operatingHoursVoList;
	}

	public List<MenuVo> getMenuVoList() {
		return menuVoList;
	}

	public void setMenuVoList(List<MenuVo> menuVoList) {
		this.menuVoList = menuVoList;
	}

	public List<MenuCategoriesVo> getMenuCategoriesVoList() {
		return menuCategoriesVoList;
	}

	public void setMenuCategoriesVoList(List<MenuCategoriesVo> menuCategoriesVoList) {
		this.menuCategoriesVoList = menuCategoriesVoList;
	}

	public List<ProductOptionGroupsVo> getProductOptionGroupsVoList() {
		return productOptionGroupsVoList;
	}

	public void setProductOptionGroupsVoList(List<ProductOptionGroupsVo> productOptionGroupsVoList) {
		this.productOptionGroupsVoList = productOptionGroupsVoList;
	}

	public StroresRes(int code, String message, List<Stores> storeList,
			List<StoreOperatingHoursVo> operatingHoursVoList, List<MenuVo> menuVoList,
			List<MenuCategoriesVo> menuCategoriesVoList, List<ProductOptionGroupsVo> productOptionGroupsVoList) {
		super(code, message);
		this.storeList = storeList;
		this.operatingHoursVoList = operatingHoursVoList;
		this.menuVoList = menuVoList;
		this.menuCategoriesVoList = menuCategoriesVoList;
		this.productOptionGroupsVoList = productOptionGroupsVoList;
	}
	
	
	
}
