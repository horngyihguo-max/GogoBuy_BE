package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.demo.constants.ResMessage;
import com.example.demo.dao.GroupbuyEventsDao;
import com.example.demo.dao.OrdersDao;
import com.example.demo.dao.StoresSearchDao;
import com.example.demo.dao.UserDao;
import com.example.demo.entity.GroupbuyEvents;
import com.example.demo.entity.Orders;
import com.example.demo.request.OredersReq;
import com.example.demo.response.BasicRes;
import com.example.demo.response.GroupbuyEventsRes;
import com.example.demo.response.OrdersRes;
import com.example.demo.response.ShippingFeeRes;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional
public class OrdersService {

	@Autowired
	private GroupbuyEventsDao groupbuyEventsDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private OrdersDao ordersDao;

	@Autowired
	private StoresSearchDao storesSearchDao;
	
	ObjectMapper mapper = new ObjectMapper();

	private BasicRes checkEvent(OredersReq req) {
		// 檢查所屬團ID
		if (req.getEventsId() == 0) {
			return new BasicRes(ResMessage.EVENTS_ID_ERROR.getCode(), //
					ResMessage.EVENTS_ID_ERROR.getMessage());
		}
		// 檢查有沒有所屬團
		GroupbuyEvents events = groupbuyEventsDao.findById(req.getEventsId());
		if (events == null) {
			return new BasicRes(ResMessage.EVENTS_NOT_FOUND.getCode(), ResMessage.EVENTS_NOT_FOUND.getMessage());
		}
		// 團購是否截止
		if (events.getEndTime().isBefore(LocalDateTime.now())) {
			return new BasicRes(ResMessage.EVENT_CLOSED.getCode(), ResMessage.EVENT_CLOSED.getMessage());
		}
		// 檢查跟團者ID
		if (req.getUserId() == null || userDao.getUserById(req.getUserId()) == null) {
			return new BasicRes(ResMessage.USER_NOT_FOUND.getCode(), ResMessage.USER_NOT_FOUND.getMessage());
		}

		// 檢查菜單品項ID
		if (req.getMenuId() == 0) {
			return new BasicRes(ResMessage.MENU_ID_ERROR.getCode(), ResMessage.MENU_ID_ERROR.getMessage());
		}
		// 從找到的團購事件中取得商店 ID
		int storesId = events.getStoresId();
		// 根據該商店 ID 取得菜單
		List<Map<String, Object>> menuList = storesSearchDao.getMenuByStoreId(storesId);
		if (menuList == null || menuList.isEmpty()) {
			return new BasicRes(ResMessage.MENULIST_NOT_FOUND.getCode(), ResMessage.MENULIST_NOT_FOUND.getMessage());
		}
		// 跟團者傳來的商品 ID是否在菜單清單中
		boolean found = false;
		// 一個一個檢查商店菜單
		for (Map<String, Object> menu : menuList) {
			// 取得資料庫裡的 ID
			String MenuId = String.valueOf(menu.get("id"));
			String getMenuId = String.valueOf(req.getMenuId());

			if (MenuId.equals(getMenuId)) {
				found = true;
				break;
			}
		}
		if (!found) {
			return new BasicRes(404, "該商店無此商品");
		}
		// 檢查跟團者選的菜單是否有在開團者給的
		String hostGiveMenuId = events.getTempMenuList();
		String allowGiveId = String.valueOf(req.getMenuId());

		if (hostGiveMenuId == null || !hostGiveMenuId.contains(allowGiveId)) {
			return new BasicRes(400, "商品 ID: " + allowGiveId + " 不在團長開放的選購名單內");
		}
		// 檢查數量
		if (req.getQuantity() == 0) {
			return new BasicRes(ResMessage.QUANTITY_FOUND.getCode(), ResMessage.QUANTITY_FOUND.getMessage());
		}
		// 檢查訂單創建時間
		if (req.getOrderTime() == null) {
			return new BasicRes(ResMessage.END_TIME_ERROR.getCode(), ResMessage.END_TIME_ERROR.getMessage());
		}
		// 檢查領取狀態
		if (req.getPickupStatus() == null) {
			return new BasicRes(ResMessage.PICKUP_STATUS_ERROR.getCode(), ResMessage.PICKUP_STATUS_ERROR.getMessage());
		}

		// 檢查小計
		if (req.getSubtotal() == 0) {
			return new BasicRes(ResMessage.PICKUP_TIME_ERROR.getCode(), ResMessage.PICKUP_TIME_ERROR.getMessage());
		}
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	// 新增
	public BasicRes addOrders(OredersReq req) {
		if (checkEvent(req).getCode() != ResMessage.SUCCESS.getCode()) {
			return checkEvent(req);
		}
		// 檢查有沒 UserId 跟 EventsId 是否重複
		int checkCount = ordersDao.CheckOrderByUserIdAndEventsId(req.getUserId(), req.getEventsId());
	    if (checkCount > 0) {
	        return new BasicRes(400, "您已參加過此團購，不可重複下單");
	    }
		Orders orders = new Orders();
		try {
			String jsonString = mapper.writeValueAsString(req.getSelectedOptionList());
			orders.setSelectedOption(jsonString);
		} catch (Exception e) {
			return new BasicRes(400, "選項格式轉換失敗");
		}
		orders.setEventsId(req.getEventsId());
		orders.setUserId(req.getUserId());
		orders.setMenuId(req.getMenuId());
		orders.setQuantity(req.getQuantity());
		orders.setPersonalMemo(req.getPersonalMemo());
		orders.setSubtotal(req.getSubtotal());
		orders.setOrderTime(req.getOrderTime());
		orders.setPickupStatus(req.getPickupStatus());
		orders.setPickupTime(req.getPickupTime());
		orders.setWeight(req.getWeight());
		ordersDao.save(orders);
		// 同步到主表的總金額
		updateSubtotal(req.getEventsId());
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	// 更新
	public BasicRes updateOrders(int id, OredersReq req) {
		Orders orders = ordersDao.findById(id);
		if (orders == null) {
			return new BasicRes(404, "找不到該筆訂單");
		}
		if (checkEvent(req).getCode() != ResMessage.SUCCESS.getCode()) {
			return checkEvent(req);
		}
		try {
			String jsonString = mapper.writeValueAsString(req.getSelectedOptionList());
			orders.setSelectedOption(jsonString);
			orders.setQuantity(req.getQuantity());
			orders.setPersonalMemo(req.getPersonalMemo());
			orders.setSubtotal(req.getSubtotal());
			orders.setPickupStatus(req.getPickupStatus());
			orders.setPickupTime(req.getPickupTime());
			orders.setWeight(req.getWeight());
			ordersDao.save(orders);
			updateSubtotal(req.getEventsId());
		} catch (Exception e) {
			return new BasicRes(ResMessage.UPDATE_ERROR.getCode(), ResMessage.UPDATE_ERROR.getMessage());
		}
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	// 回傳的訂單紀錄
	public GroupbuyEventsRes getGroupbuyEventById(String hostId) {
		try {
			if (!StringUtils.hasText(hostId)) {
				return new GroupbuyEventsRes(400, "輸入正確的host_id");
			}
			List<GroupbuyEvents> eventsList = groupbuyEventsDao.getGroupbuyEventById(hostId);
			GroupbuyEventsRes res = new GroupbuyEventsRes(200, "host_id 搜尋成功");
			res.setGroupbuyEvents(eventsList);
			return res;
		} catch (Exception e) {
			return new GroupbuyEventsRes(500, "host_id 搜尋失敗");
		}
	}
	
	// 軟刪除
	public BasicRes deleteOrderByUserIdAndEventsId(String userId, int eventsId) {
		if (!StringUtils.hasText(userId) || eventsId <= 0) {
			return new BasicRes(400, "請提供正確的使用者 ID 與團購 ID");
		}
		int deleteRow = ordersDao.deleteOrderByUserIdAndEventsId(userId, eventsId);
		// 判斷處理結果
		if (deleteRow == 0) {
			return new BasicRes(404, "在此團購中找不到該使用者的有效訂單");
		}
		updateSubtotal(eventsId);
		return new BasicRes(200, "已成功取消該使用者在本次團購中的 " + deleteRow + " 筆訂單");
	}

	// 更新總金額
	private void updateSubtotal(int eventId) {
		// SQL 有 ifnull，沒訂單時總金額就會是 0
		int totalAmount = ordersDao.sumSubtotalByEventId(eventId);
		groupbuyEventsDao.updateTotalAmount(totalAmount, eventId);
	}

	// 計算平分拆帳
	public ShippingFeeRes getShippingFeeByEventId(int eventsId) {
		try {
			int shippingFee = ordersDao.getShippingFeeByEventId(eventsId);
			if (shippingFee == 0) {
				return new ShippingFeeRes(200, "目前免運");
			}
			// 取得目前總人數
			int orderCount = ordersDao.countOrdersByEventId(eventsId);
			// 計算平分金額
			int splitFee = (orderCount > 0) ? (shippingFee / orderCount) : shippingFee;
			ShippingFeeRes res = new ShippingFeeRes(200, "平均運費是 :" + splitFee);
			res.setShippingFee(splitFee);
			return res;
		} catch (Exception e) {
			return new ShippingFeeRes(500, "找不到 ");
		}
	}

	// 查詢跟團者有的開團
	public GroupbuyEventsRes getOrdersByUserId(String userId) {
		try {
			if (!StringUtils.hasText(userId)) {
				return new GroupbuyEventsRes(400, "輸入正確的user_id");
			}
			List<Orders> ordersList = ordersDao.getOrdersByUserId(userId);
			GroupbuyEventsRes res = new GroupbuyEventsRes(200, "user_id 搜尋成功");
			res.setOrders(ordersList);
			return res;
		} catch (Exception e) {
			return new GroupbuyEventsRes(500, "找不到 " + userId + "訂單");
		}
		
	}
	// 查詢跟團者的特定訂單
			public GroupbuyEventsRes getEventIdByUserId(String userId , int eventsId) {
				try {
					if (!StringUtils.hasText(userId) && eventsId == 0) {
						return new GroupbuyEventsRes(400, "輸入正確的user_id 或 events_id");
					}
					List<Orders> orders = ordersDao.getEventIdByUserId(userId,eventsId);
					if(orders == null || orders.isEmpty()) {
						return new GroupbuyEventsRes(404, "找不到此用戶 " + userId + "訂單" + eventsId);
					}
					return new GroupbuyEventsRes(200, "成功找到", orders);
				} catch(Exception e) {
					return new GroupbuyEventsRes(500,"系統錯誤");
				}
			}
}
