package com.example.demo.response;

import java.util.List;

import com.example.demo.dto.CartDTO;
import com.example.demo.dto.OrdersDTO;
import com.example.demo.entity.GroupsSearchView;
import com.example.demo.entity.Menu;
import com.example.demo.entity.Orders;
import com.example.demo.entity.OrdersSearchView;
import com.example.demo.entity.PersonalOrder;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

//加上這行，null 的欄位就不會出現在 JSON 裡
@JsonInclude(Include.NON_NULL)
public class GroupbuyEventsRes extends BasicRes {

	private List<?> groupbuyEvents;

	private List<Orders> orders;

	private List<PersonalOrder> personalOrder;

	private List<Menu> menuList;

	private List<GroupsSearchView> groupsSearchViewList;
	
	private List<OrdersSearchView> ordersSearchViewList;


	public List<OrdersSearchView> getOrdersSearchViewList() {
		return ordersSearchViewList;
	}

	public void setOrdersSearchViewList(List<OrdersSearchView> ordersSearchViewList) {
		this.ordersSearchViewList = ordersSearchViewList;
	}

	public List<?> getGroupbuyEvents() {
		return groupbuyEvents;
	}

	public void setGroupbuyEvents(List<?> groupbuyEvents) {
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

	public List<GroupsSearchView> getGroupsSearchViewList() {
		return groupsSearchViewList;
	}

	public void setGroupsSearchViewList(List<GroupsSearchView> groupsSearchViewList) {
		this.groupsSearchViewList = groupsSearchViewList;
	}

	public GroupbuyEventsRes() {
		super();
	}

	public GroupbuyEventsRes(int code, String message) {
		super(code, message);
	}
	

	public GroupbuyEventsRes(int code, String message, List<?> groupbuyEvents, List<Orders> orders,
			List<PersonalOrder> personalOrder, List<Menu> menuList, List<GroupsSearchView> groupsSearchViewList,List<OrdersSearchView> ordersSearchViewList) {
		super(code, message);
		this.groupbuyEvents = groupbuyEvents;
		this.orders = orders;
		this.personalOrder = personalOrder;
		this.menuList = menuList;
		this.groupsSearchViewList = groupsSearchViewList;
		this.ordersSearchViewList = ordersSearchViewList;
	}

	public GroupbuyEventsRes(int code, String message, List<Orders> orders) {
		super(code, message);
		this.orders = orders;
	}

	private List<CartDTO> cartData;

    public List<CartDTO> getCartData() {
        return cartData;
    }

    public void setCartData(List<CartDTO> cartData) {
        this.cartData = cartData;
    }
    private OrdersDTO ordersDto;


	public OrdersDTO getOrdersDto() {
		return ordersDto;
	}

	public void setOrdersDto(OrdersDTO ordersDto) {
		this.ordersDto = ordersDto;
	}
	public GroupbuyEventsRes(int code, String message, OrdersDTO responseDto) {
        super(code, message);
        this.ordersDto = responseDto; 
    }
    
}
