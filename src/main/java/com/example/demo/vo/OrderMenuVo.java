package com.example.demo.vo;

import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotNull;

public class OrderMenuVo {

	  @NotNull(message = "菜單品項ID必填")
	  private int menuId;
	  
	  @NotNull(message = "數量必填")
      private int quantity;
      
      private String specName;
      
      private List<Map<String, Object>> selectedOptionList;

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

	  public String getSpecName() {
		  return specName;
	  }

	  public void setSpecName(String specName) {
		  this.specName = specName;
	  }

	  public List<Map<String, Object>> getSelectedOptionList() {
		  return selectedOptionList;
	  }

	  public void setSelectedOptionList(List<Map<String, Object>> selectedOptionList) {
		  this.selectedOptionList = selectedOptionList;
	  }
      

      

}
