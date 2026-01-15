package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.constants.ResMessage;
import com.example.demo.dao.GroupbuyEventsDao;
import com.example.demo.dao.OrdersDao;
import com.example.demo.dao.StoresSearchDao;
import com.example.demo.dao.UserDao;
import com.example.demo.entity.GroupbuyEvents;
import com.example.demo.entity.Orders;
import com.example.demo.entity.User;
import com.example.demo.request.OredersReq;
import com.example.demo.response.BasicRes;
import com.example.demo.response.OrdersRes;
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

	private ObjectMapper mapper = new ObjectMapper();

//	@Autowired
//	private GroupbuyEvents groupbuyEvents;

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
		// 檢查前端傳來的 menuId 是否真的存在於該菜單中
		boolean isMenuExist = menuList.stream().anyMatch(m -> m.get("id").equals(req.getMenuId()));
		if (!isMenuExist) {
			return new BasicRes(ResMessage.MENU_ID_ERROR.getCode(), "該商店無此商品");
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
		// 檢查領取時間
		if (req.getPickupTime() == null) {
			return new BasicRes(ResMessage.PICKUP_TIME_ERROR.getCode(), ResMessage.PICKUP_TIME_ERROR.getMessage());
		}
		// 檢查小計
		if (req.getSubtotal() == 0) {
			return new BasicRes(ResMessage.PICKUP_TIME_ERROR.getCode(), ResMessage.PICKUP_TIME_ERROR.getMessage());
		}
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	// 新增
	public BasicRes addOrders(OredersReq req) {

		BasicRes checkResult = checkEvent(req);
		if (checkResult.getCode() != ResMessage.SUCCESS.getCode()) {
			return checkResult;
		}

		// 將 List<Map<String, Object>> 轉為 JSON 字串
		Orders orders = new Orders();
		try {
			// List，writeValueAsString 會把它轉成 String
			String jsonString = mapper.writeValueAsString(req.getSelectedOptionList());
			orders.setSelectedOption(jsonString);
		} catch (Exception e) {
			// 如果轉換失敗，可以 log 錯誤或回傳參數錯誤
			e.printStackTrace();
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
		updateSubtotal(req.getEventsId());
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	// 更新
	public BasicRes updateOrders(int id, OredersReq req) {
		Orders orders = ordersDao.findById(id);
		if (orders == null) {
			return new BasicRes(404, "找不到該筆訂單");
		}
		if (req.isDeleted()) { 
	        orders.setDeleted(true);
	        ordersDao.save(orders);
	        updateSubtotal(orders.getEventsId());
	        return new BasicRes(200, "訂單已成功取消（軟刪除）");
	    }
		BasicRes checkResult = checkEvent(req);
		if (checkResult.getCode() != ResMessage.SUCCESS.getCode()) {
			return checkResult;
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
			e.printStackTrace();
			return new BasicRes(ResMessage.UPDATE_ERROR.getCode(), ResMessage.UPDATE_ERROR.getMessage());
		}
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}
	
	// 更新總金額
	private void updateSubtotal (int eventId ) {
//	    int count = ordersDao.countOrdersByEventId(eventId);
	    Integer sum = ordersDao.sumSubtotalByEventId(eventId);
	    // 處理查無資料時的狀況
	    int totalAmount = (sum != null) ? sum : 0;
	    groupbuyEventsDao.updateEventStats(totalAmount, eventId);
	}
	
}
