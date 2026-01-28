package com.example.demo.response;

import java.util.List;

import com.example.demo.entity.GroupbuyEvents;
import com.example.demo.entity.GroupsSearchView;
import com.example.demo.entity.Menu;
import com.example.demo.entity.Orders;
import com.example.demo.entity.PersonalOrder;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

//加上這行，null 的欄位就不會出現在 JSON 裡
@JsonInclude(Include.NON_NULL)
public class GroupbuyEventsRes extends BasicRes {

	private List<GroupbuyEvents> groupbuyEvents;

	private List<Orders> orders;

	private List<PersonalOrder> personalOrder;

	private List<Menu> menuList;

	private List<GroupsSearchView> groupsSearchViewList;



	public List<GroupbuyEvents> getGroupbuyEvents() {
		return groupbuyEvents;
	}

	public void setGroupbuyEvents(List<GroupbuyEvents> groupbuyEvents) {
		this.groupbuyEvents = groupbuyEvents;
	}

	public List<Orders> getOrders() {
		return orders;
	}

	public void setOrders(List<Orders> orders) {
		this.orders = orders;
	}

	public List<PersonalOrder> getPersonalOrder() {
		return personalOrder;
	}

	public void setPersonalOrder(List<PersonalOrder> personalOrder) {
		this.personalOrder = personalOrder;
	}

	public List<Menu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}

	public GroupbuyEventsRes() {
		super();
	}

	public GroupbuyEventsRes(int code, String message) {
		super(code, message);
	}

	public GroupbuyEventsRes(int code, String message, List<GroupbuyEvents> groupbuyEvents, List<Orders> orders,
			List<PersonalOrder> personalOrder, List<Menu> menuList) {
		super(code, message);
		this.groupbuyEvents = groupbuyEvents;
		this.orders = orders;
		this.personalOrder = personalOrder;
		this.menuList = menuList;
	}

	public List<GroupsSearchView> getGroupsSearchViewList() {
		return groupsSearchViewList;
	}

	public void setGroupsSearchViewList(List<GroupsSearchView> groupsSearchViewList) {
		this.groupsSearchViewList = groupsSearchViewList;
	}

	public GroupbuyEventsRes(int code, String message, List<GroupbuyEvents> groupbuyEvents, List<Orders> orders,
			List<PersonalOrder> personalOrder, List<Menu> menuList, List<GroupsSearchView> groupsSearchViewList) {
		super(code, message);
		this.groupbuyEvents = groupbuyEvents;
		this.orders = orders;
		this.personalOrder = personalOrder;
		this.menuList = menuList;
		this.groupsSearchViewList = groupsSearchViewList;
	}

	public GroupbuyEventsRes(int code, String message, List<Orders> orders) {
	    super(code, message);
	    this.orders = orders;
	}


	

}
