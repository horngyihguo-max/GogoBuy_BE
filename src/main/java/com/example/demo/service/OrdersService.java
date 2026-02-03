package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.demo.constants.ResMessage;
import com.example.demo.dao.GroupbuyEventsDao;
import com.example.demo.dao.OrdersDao;
import com.example.demo.dao.StoresSearchDao;
import com.example.demo.dao.UserDao;
import com.example.demo.entity.GroupbuyEvents;
import com.example.demo.entity.Menu;
import com.example.demo.entity.Orders;
import com.example.demo.request.OredersReq;
import com.example.demo.response.BasicRes;
import com.example.demo.response.GroupbuyEventsRes;
import com.example.demo.response.OrdersRes;
import com.fasterxml.jackson.core.JsonProcessingException;
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
		// 檢查領取狀態
		if (req.getPickupStatus() == null) {
			return new BasicRes(ResMessage.PICKUP_STATUS_ERROR.getCode(), ResMessage.PICKUP_STATUS_ERROR.getMessage());
		}

		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}
	//
	public BasicRes hardDelete(String userId, int eventsId) {
		int orders = ordersDao.getOrderByUserIdAndEventsId(userId, eventsId);
		if (orders == 0) {
	        return new BasicRes(404, "找不到訂單或已刪除");
	    }
		ordersDao.hardDelete(userId, eventsId);
		return new BasicRes(200, "成功刪除訂單");
	}

	// 計算個人subtotal
	private OrdersRes getSubtotal(OredersReq req) {
		try {
	        // 取得該品項底價 
	        Menu menu = storesSearchDao.getMenuByMenuId(req.getMenuId());
	        if (menu == null) {
	            return new OrdersRes(404, "找不到對應的菜單品項", 0);
	        }
	        // 商品單價
	        int basePrice = menu.getBasePrice(); 
	        int totalExtraPrice = 0; 

	        // 累加所有選中的加價項目
	        List<Map<String, Object>> opListPrice = req.getSelectedOptionList();
	        if (opListPrice != null) {
	            for (Map<String, Object> op : opListPrice) {
	                Object extraPriceObject = op.get("extraPrice");
	                if (extraPriceObject != null) {
	                    totalExtraPrice += Integer.parseInt(extraPriceObject.toString());
	                }
	            }
	        }

	        // 計算單價與小計
	        int unitPrice = basePrice + totalExtraPrice; 
	        int subtotal = unitPrice * req.getQuantity();
	        return new OrdersRes(200, "個人小記計算成功", subtotal);
	    } catch (Exception e) {
	        return new OrdersRes(500, "計算過程發生錯誤: " + e.getMessage(), 0);
	    }
	}

	// 新增
	public BasicRes addOrders(OredersReq req) {
		if (checkEvent(req).getCode() != ResMessage.SUCCESS.getCode()) {
			return checkEvent(req);
		}
		Orders orders = new Orders();
		OrdersRes subtotal = getSubtotal(req);
		if (subtotal.getCode() == 200) {
			orders.setSubtotal(subtotal.getSubtotal());
			String jsonString;
			try {
				jsonString = mapper.writeValueAsString(req.getSelectedOptionList());
				orders.setSelectedOption(jsonString);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		orders.setEventsId(req.getEventsId());
		orders.setUserId(req.getUserId());
		orders.setMenuId(req.getMenuId());
		orders.setQuantity(req.getQuantity());
		orders.setPersonalMemo(req.getPersonalMemo());
		orders.setOrderTime(LocalDateTime.now());
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
		OrdersRes subtotal = getSubtotal(req);
		try {
			String jsonString = mapper.writeValueAsString(req.getSelectedOptionList());
			orders.setSelectedOption(jsonString);
			orders.setQuantity(req.getQuantity());
			orders.setPersonalMemo(req.getPersonalMemo());
			orders.setSubtotal(subtotal.getSubtotal());
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
	public GroupbuyEventsRes getEventIdByUserId(String userId, int eventsId) {
		try {
			if (!StringUtils.hasText(userId) && eventsId == 0) {
				return new GroupbuyEventsRes(400, "輸入正確的user_id 或 events_id");
			}
			List<Orders> orders = ordersDao.getEventIdByUserId(userId, eventsId);
			if (orders == null || orders.isEmpty()) {
				return new GroupbuyEventsRes(404, "找不到此用戶 " + userId + "訂單" + eventsId);
			}
			return new GroupbuyEventsRes(200, "成功找到", orders);
		} catch (Exception e) {
			return new GroupbuyEventsRes(500, "系統錯誤");
		}
	}
	
	public BasicRes deleteCartByOrderId(int orderId) {
		int deletedCount = ordersDao.deleteOrderById(orderId);

		// 如果受影響的筆數大於 0，表示至少有一份問卷被成功軟刪除
		if (deletedCount > 0) {
			return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
		} else {
			// 如果一筆都沒改到，表示傳入的 ID 在資料庫都找不到（或是已被刪除）
			return new BasicRes(ResMessage.ORDER_ERROR.getCode(), ResMessage.ORDER_ERROR.getMessage());
		}
	}
}
