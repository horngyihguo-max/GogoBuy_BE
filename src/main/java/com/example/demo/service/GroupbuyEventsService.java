package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.demo.constants.GroupbuyStatusEnum;
import com.example.demo.constants.PaymentStatus;
import com.example.demo.constants.ResMessage;
import com.example.demo.dao.GroupbuyEventsDao;
import com.example.demo.dao.GroupsSearchViewDao;
import com.example.demo.dao.OrdersDao;
import com.example.demo.dao.PersonalOrderDao;
import com.example.demo.dao.StoresSearchDao;
import com.example.demo.dao.UserDao;
import com.example.demo.dto.CartDTO;
import com.example.demo.entity.GroupbuyEvents;
import com.example.demo.entity.GroupsSearchView;
import com.example.demo.entity.Menu;
import com.example.demo.entity.Orders;
import com.example.demo.entity.Stores;
import com.example.demo.entity.User;
import com.example.demo.projection.GroupbuyEventsProjection;
import com.example.demo.request.GroupbuyEventsReq;
import com.example.demo.response.BasicRes;
import com.example.demo.response.GroupbuyEventsRes;
import com.example.demo.response.ShippingFeeRes;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GroupbuyEventsService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private StoresSearchDao storesSearchDao;

	@Autowired
	private GroupbuyEventsDao groupbuyEventsDao;

	@Autowired
	private GroupsSearchViewDao groupsSearchViewDao;

	@Autowired
	private OrdersDao ordersDao;

	@Autowired
	private PersonalOrderDao personalOrderDao;

	@Autowired
	private PersonalOrderService personalOrderService;

	ObjectMapper mapper = new ObjectMapper();

	// 將重複的驗證邏輯提取出來
	private BasicRes checkEvent(GroupbuyEventsReq req) {
		// 檢查團長ID
		if (!StringUtils.hasText(req.getHostId())) {
			return new BasicRes(ResMessage.HOST_ID_ERROR.getCode(), //
					ResMessage.HOST_ID_ERROR.getMessage());
		}
		// 檢查有沒有團長
		User user = userDao.findById(req.getHostId()).orElse(null);
		if (user == null) {
			return new BasicRes(ResMessage.HOST_ID_NOT_FOUND.getCode(), ResMessage.HOST_ID_NOT_FOUND.getMessage());
		}

		// 檢查商店ID
		if (req.getStoresId() == 0) {
			return new BasicRes(ResMessage.STORES_ID_ERROR.getCode(), //
					ResMessage.STORES_ID_ERROR.getMessage());
		}
		// 檢查商家是否存在
		Stores stores = storesSearchDao.getStoreById(req.getStoresId());
		if (stores == null) {
			return new BasicRes(ResMessage.STORES_ID_NULL.getCode(), //
					ResMessage.STORES_ID_NULL.getMessage());
		}
		// 檢查團名
		if (!StringUtils.hasText(req.getEventName())) {
			return new BasicRes(400, "團名必填");
		}

		// 檢查結束時間
		if (req.getEndTime() == null) {
			return new BasicRes(ResMessage.END_TIME_ERROR.getCode(), //
					ResMessage.END_TIME_ERROR.getMessage());
		}
		// 檢查拆帳模式
		if (req.getSplitType() == null) {
			return new BasicRes(ResMessage.SPLIT_TYPE_ERROR.getCode(), //
					ResMessage.SPLIT_TYPE_ERROR.getMessage());
		}
		// 檢查總金額
		if (req.getTotalOrderAmount() < 0) {
			return new BasicRes(ResMessage.TOTALORDERAMOUNT_ERROR.getCode(), //
					ResMessage.TOTALORDERAMOUNT_ERROR.getMessage());
		}
		// 檢查總運費
		if (req.getShippingFee() < 0) {
			return new BasicRes(ResMessage.SHIPPING_FEE_ERROR.getCode(), //
					ResMessage.SHIPPING_FEE_ERROR.getMessage());
		}
		// 檢查商家類型
		if (req.getType() == null) {
			return new BasicRes(ResMessage.TYPE_ERROR.getCode(), //
					ResMessage.TYPE_ERROR.getMessage());
		}
		// 金額門檻
		if (req.getLimitation() < 0) {
			return new BasicRes(ResMessage.SPLIT_TYPE_ERROR.getCode(), //
					ResMessage.SPLIT_TYPE_ERROR.getMessage());
		}
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	// 新增開團
	public BasicRes addEvent(GroupbuyEventsReq req) {
		// 如果 checkEvent(req).getCode() 不等於 SUCCESS.getCode() 就會回傳 錯誤的訊息跟代碼
		if (checkEvent(req).getCode() != ResMessage.SUCCESS.getCode()) {
			return checkEvent(req);
		}
		// 新增資料
		GroupbuyEvents event = new GroupbuyEvents();
		event.setHostId(req.getHostId());
		event.setStoresId(req.getStoresId());
		event.setEventName(req.getEventName());
		event.setStatus(GroupbuyStatusEnum.OPEN);
		event.setEndTime(req.getEndTime());
		event.setSplitType(req.getSplitType());
		event.setTotalOrderAmount(0);
		event.setShippingFee(req.getShippingFee());
		event.setLimitation(req.getLimitation());
		event.setAnnouncement(req.getAnnouncement());
		event.setType(req.getType());
		event.setRecommendDescription(req.getRecommendDescription());

		try {
			// 此店家的全部菜單
			List<Map<String, Object>> Menu = storesSearchDao.getMenuByStoreId(req.getStoresId());
			// 將資料庫菜單 ID 轉成 Set，方便快速比較
			Set<Integer> MenuIds = Menu.stream()
					/*
					 * 一整個 Map，我們只對 m.get("id") 感興趣，要 toString() 是因為 id 有時候是 Long 之類的
					 * Integer.valueOf() 是因為直接轉型 Integer 很容易噴錯，所以先轉成字串再轉回數字
					 */
					.map(m -> Integer.valueOf(m.get("id").toString()))
					/*
					 * 在 .collect 收集起來打包回 MenuIds .toSet 會自動確保裡面不會有重複的 ID
					 */
					.collect(Collectors.toSet());

			// 檢查飲料 ID
			List<Integer> selectedIds = new ArrayList<>();
			if (req.getTempMenuList() != null) {
				for (Integer selectedId : req.getTempMenuList()) {
					// 檢查這個 ID 是否有在商店裡
					/*
					 * .contains(selectedId) (快速比對)：這是 Set 的功能。 會瞬間檢查 selectedId（團長給的菜單的商品 ID）有沒有在
					 * MenuIds 商店菜單裡面。
					 */
					if (!MenuIds.contains(selectedId)) {
						return new BasicRes(400, "品項 ID: " + selectedId + " 不屬於此店家，無法開團");
					}
					selectedIds.add(selectedId);
				}
			}

			// 檢查推薦飲料ID
			List<Integer> recommendIds = new ArrayList<>();
			if (req.getRecommendList() != null) {
				for (Integer recommendId : req.getRecommendList()) {
					// 檢查這個 ID 是否有在商店裡
					/*
					 * .contains(selectedId) (快速比對)：這是 Set 的功能。 會瞬間檢查 selectedId（團長給的菜單的商品 ID）有沒有在
					 * MenuIds 商店菜單裡面。
					 */
					if (!selectedIds.contains(recommendId)) {
						return new BasicRes(400, "推薦品項 ID: " + recommendId + " 不在本次團購的選購名單內");
					}
					recommendIds.add(recommendId);
				}
			}
			// 將 ID 轉成字串
			// mapper 是負責搬運與轉換資料的工具
			// 序列化就是 Object 轉 Json
			String tempMenuJson = mapper.writeValueAsString(selectedIds);
			String recommendJson = mapper.writeValueAsString(recommendIds);

			// 存入物件
			event.setTempMenuList(tempMenuJson);
			event.setRecommendList(recommendJson);

			groupbuyEventsDao.addEvent(event.getHostId(), //
					event.getStoresId(), //
					event.getEventName(), //
					event.getStatus().name(), //
					event.getEndTime(), //
					event.getTotalOrderAmount(), //
					event.getShippingFee(), //
					event.getSplitType().name(), //
					event.getAnnouncement(), //
					event.getType(), //
					event.getTempMenuList(), //
					event.getRecommendList(), //
					event.getRecommendDescription(), //
					event.getLimitation());
		} catch (Exception e) {
			return new BasicRes(ResMessage.EVENT_ERROR.getCode(), ResMessage.EVENT_ERROR.getMessage());
		}
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	// 更新
	public BasicRes updateEvent(int id, GroupbuyEventsReq req) {
		GroupbuyEvents event = groupbuyEventsDao.findById(id);
		if (event == null) {
			return new BasicRes(ResMessage.EVENTS_NOT_FOUND.getCode(), ResMessage.EVENTS_NOT_FOUND.getMessage());
		}
		try {
			// 此店家的全部菜單
			List<Map<String, Object>> Menu = storesSearchDao.getMenuByStoreId(req.getStoresId());
			// 將資料庫菜單 ID 轉成 Set，方便快速比較
			Set<Integer> MenuIds = Menu.stream()
					/*
					 * 一整個 Map，我們只對 m.get("id") 感興趣，要 toString() 是因為 id 有時候是 Long 之類的
					 * Integer.valueOf() 是因為直接轉型 Integer 很容易噴錯，所以先轉成字串再轉回數字
					 */
					.map(m -> Integer.valueOf(m.get("id").toString()))
					/* Set 會自動確保裡面不會有重複的 ID */
					// 在 .collect 收集起來打包回 MenuIds
					.collect(Collectors.toSet());

			// 檢查飲料 ID
			List<Integer> selectedIds = new ArrayList<>();
			if (req.getTempMenuList() != null) {
				for (Integer selectedId : req.getTempMenuList()) {
					// 檢查這個 ID 是否有在商店裡
					/*
					 * .contains(selectedId) (快速比對)：這是 Set 的功能。 會瞬間檢查 selectedId（團長給的菜單的商品 ID）有沒有在
					 * MenuIds 商店菜單裡面。
					 */
					if (!MenuIds.contains(selectedId)) {
						return new BasicRes(400, "品項 ID: " + selectedId + " 不屬於此店家，無法開團");
					}
					selectedIds.add(selectedId);
				}
			}

			// 檢查推薦飲料ID
			List<Integer> recommendIds = new ArrayList<>();
			if (req.getRecommendList() != null) {
				for (Integer recommendId : req.getRecommendList()) {
					// 檢查這個 ID 是否有在商店裡
					/*
					 * .contains(selectedId) (快速比對)：這是 Set 的功能。 會瞬間檢查 recommendId（團長給的推薦商品 ID）有沒有在
					 * selectedIds （團長給的菜單的商品 ID）菜單裡面。
					 */
					if (!selectedIds.contains(recommendId)) {
						return new BasicRes(400, "推薦品項 ID: " + recommendId + " 不在本次團購的選購名單內");
					}
					recommendIds.add(recommendId);
				}
			}

			// 轉成純 ID 的 JSON 字串
			String tempMenuJson = mapper.writeValueAsString(selectedIds);
			String recommendJson = mapper.writeValueAsString(recommendIds);

			groupbuyEventsDao.updateEvent(//
					req.getHostId(), //
					req.getStoresId(), //
					req.getEventName(), //
					req.getStatus().name(), //
					req.getEndTime(), //
					event.getTotalOrderAmount(), //
					req.getShippingFee(), //
					req.getSplitType().name(), //
					req.getAnnouncement(), //
					req.getType(), //
					tempMenuJson, //
					recommendJson, //
					req.getRecommendDescription(), //
					req.getLimitation(), id);
		} catch (Exception e) {
			return new BasicRes(ResMessage.EVENT_ERROR.getCode(), ResMessage.EVENT_ERROR.getMessage());
		}
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	// 團長手動結單
	@Transactional
	public BasicRes HostCloseEvent(int id, String hostId) {
		GroupbuyEvents event = groupbuyEventsDao.findById(id);
		if (event == null) {
			return new BasicRes(404, "找不到該團購活動");
		}
		if (!event.getHostId().equals(hostId)) {
			return new BasicRes(403, "權限不足，只有團長可以結單");
		}
		if (event.getStatus() == GroupbuyStatusEnum.FINISHED) {
			return new BasicRes(400, "此團已經是結單狀態");
		}
		BasicRes autoClose = closeEvent(id, hostId);
		return autoClose;
	}

	// 結單功能
	@Transactional
	public BasicRes closeEvent(int id, String userId) {
		// 手動結單完查詢所屬活動的跟團者做自動生產addPersonOrder資料
		List<Orders> ordersInfoList = ordersDao.getUserAllByEventsId(id);
		// 檢查
		if (ordersInfoList == null || ordersInfoList.isEmpty()) {
			return new BasicRes(400, "查無此 ordersInfoList 資料");
		}
		List<String> userIdList = ordersDao.getUserIdByEventsId(id);
		for (String userIdStr : userIdList) {
			// 計算該用戶個人的數據
			// 此用戶的全部小計
			int userSubtotal = ordersDao.sumSubtotalByEventIdAndUserId(id, userIdStr);
			// 此用戶的全部重量
			Double userWeight = ordersDao.sumWeightByEventIdAndUserId(id, userIdStr);
			// 新增
			personalOrderDao.addPersonalOrder(id, userIdStr, userWeight, 0, userSubtotal, PaymentStatus.UNPAID.name());
		}
		// 此用戶的運費計算
		ShippingFeeRes feeRes = personalOrderService.getShippingFeeByEventId(id, userId);
		if (feeRes.getCode() == 200) {
			// 更新活動狀態為 FINISHED
			groupbuyEventsDao.updateStatus(GroupbuyStatusEnum.FINISHED.name(), id, userId);
			return new BasicRes(200, "結單成功，帳單已產生並完成運費分攤");
		} else {
			return new BasicRes(400, "帳單已產生但運費計算出錯：" + feeRes.getMessage());
		}
	}

	// 排程結單
	@Transactional
	public BasicRes autoCloseEvent(int id, String userId) {
		BasicRes autoClose = closeEvent(id, userId);
		return autoClose;
	}

	// 軟刪除
	@Transactional
	public BasicRes deleteEvent(int eventsId) {
		// 檢查該活動是否存在且尚未被刪除
		GroupbuyEvents event = groupbuyEventsDao.findById(eventsId);
		if (event == null) {
			return new BasicRes(404, "找不到該團購活動或活動已被刪除");
		}
		// 軟刪除主表
		int deletedEvent = groupbuyEventsDao.delete(eventsId);

		if (deletedEvent > 0) {
			// 順便刪除子表
			ordersDao.deleteAllOrdersByEventId(eventsId);
			return new BasicRes(200, "團購活動ID: " + eventsId + "已成功刪除");
		}
		return new BasicRes(500, "刪除活動失敗，請稍後再試");
	}

	// 回傳開團者的開團紀錄
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

	// 回傳團長給的菜單
	public GroupbuyEventsRes getMenuByMenuId(List<Integer> menuList) {
		try {
			// 先去抓商店的菜單ID
			List<Menu> menus = storesSearchDao.getMenuByMenuId(menuList);
			/*
			 * 因為寫 return new GroupbuyEventsRes(200, "menuId 搜尋成功", menus);會有問題 所以改用 set
			 * 將查詢到的 menus 塞到 res 的 menuList
			 */
			GroupbuyEventsRes res = new GroupbuyEventsRes(200, "menuId 搜尋成功");
			res.setMenuList(menus);
			return res;
		} catch (Exception e) {
			return new GroupbuyEventsRes(500, "菜單搜尋失敗");
		}
	}

	// 回傳用商店ID查詢的菜單
	public GroupbuyEventsRes getMenuByStoresId(int storesId) {
		try {
			if (storesId <= 0) {
				return new GroupbuyEventsRes(400, "輸入正確的stores_id");
			}
			List<Menu> menuList = groupbuyEventsDao.getMenuByStoresId(storesId);
			if (menuList == null || menuList.isEmpty()) {
				return new GroupbuyEventsRes(200, "查無此店家的菜單資料");
			}
			GroupbuyEventsRes res = new GroupbuyEventsRes(200, "店家菜單搜尋成功");
			res.setMenuList(menuList);
			return res;
		} catch (Exception e) {
			return new GroupbuyEventsRes(500, "店家菜單搜尋失敗");
		}
	}

	// 回傳用商店ID查詢符合的團
	public GroupbuyEventsRes getGroupbuyEventByStoresId(int storesId) {
		try {
			if (storesId <= 0) {
				return new GroupbuyEventsRes(400, "輸入正確的stores_id");
			}
			List<GroupbuyEvents> eventsList = groupbuyEventsDao.getGroupbuyEventByStoresId(storesId);
			if (eventsList == null || eventsList.isEmpty()) {
				return new GroupbuyEventsRes(200, "查無此店家的菜單資料");
			}
			GroupbuyEventsRes res = new GroupbuyEventsRes(200, "店家菜單搜尋成功");
			res.setGroupbuyEvents(eventsList);
			return res;
		} catch (Exception e) {
			return new GroupbuyEventsRes(500, "店家菜單搜尋失敗");
		}
	}

	// 回傳全部開團的
	public GroupbuyEventsRes getAll() {
		try {
			List<GroupbuyEventsProjection> list = groupbuyEventsDao.getAll();
			if (list == null) {
				return new GroupbuyEventsRes(400, "目前暫無任何開團資料");
			}
			GroupbuyEventsRes res = new GroupbuyEventsRes(200, "搜尋成功");
			res.setGroupbuyEvents(list);
			return res;
		} catch (Exception e) {
			return new GroupbuyEventsRes(500, "查詢失敗");
		}
	}

	// 回傳暱稱有的開團
	public GroupbuyEventsRes getGroupbuyEventByNickname(String hostNickname) {
		List<GroupsSearchView> nicknameEventsList = groupsSearchViewDao.getGroupbuyEventByNickname(hostNickname);
		if (nicknameEventsList == null) {
			return new GroupbuyEventsRes(200, "查無此開團者的開團資料");
		}
		try {
			GroupbuyEventsRes res = new GroupbuyEventsRes(200, "開團者搜尋成功");
			res.setGroupsSearchViewList(nicknameEventsList);
			return res;
		} catch (Exception e) {
			return new GroupbuyEventsRes(500, "這裡失敗?");
		}
	}

	// 回傳eventsId的活動
	public GroupbuyEventsRes getEventsByEventsId(int id) {
		List<GroupbuyEventsProjection> list = groupbuyEventsDao.getEventsByEventsId(id);
		if (list == null || list.isEmpty()) {
			return new GroupbuyEventsRes(404, "查無此資料");
		}
		return new GroupbuyEventsRes(200, "成功查詢資料", list, null, null, null, null);
	}
	
	
	//	刪除(只做為修改資料用 不串接)
	@Transactional
	public BasicRes deleteEventPhysically (int eventId) {
		GroupbuyEvents event = groupbuyEventsDao.findById(eventId);
		if (event == null) {
			return new BasicRes(404, "根本沒有這團喵");
		}
		
		groupbuyEventsDao.deleteEvent(eventId);
		
		
		return new BasicRes(200, "成功刪除團購喵");
	}
	
	

	// 購物車(首頁)
	public GroupbuyEventsRes getCart(String userId) {
	    try {
	        if (!StringUtils.hasText(userId)) {
	            return new GroupbuyEventsRes(400, "UserId 錯誤");
	        }

	        // 取得該用戶所有未刪除的訂單 
	        List<Orders> allOrders = ordersDao.getOrdersByUserId(userId);
	        
	        if (allOrders == null || allOrders.isEmpty()) {
	            return new GroupbuyEventsRes(200, "購物車目前沒有資料");
	        }
	        
	        // 建立 Map 用於分組處理，Key 為 eventsId
	        Map<Integer, CartDTO> cartMap = new HashMap<>();
	        
	        for (Orders order : allOrders) {
	            int eventId = order.getEventsId();
	            
	            // 如果這團還沒被建立過，則初始化這團的共通資訊
	            if (!cartMap.containsKey(eventId)) {
	                CartDTO dto = new CartDTO();
	                dto.setEventsId(eventId);
	                dto.setItems(new ArrayList<>());
	                dto.setTotalAmount(0);
	                
	                // 補充團購活動資訊
	                GroupbuyEvents event = groupbuyEventsDao.findById(eventId);
	                if (event != null) {
	                    dto.setEventName(event.getEventName());
	                    dto.setStatus(event.getStatus().name());
	                    
	                    // 根據狀態判斷是否可修改 (只有 OPEN 狀態才能改)
	                    dto.setCanModify(event.getStatus() == GroupbuyStatusEnum.OPEN);
	                    
	                    // 補充商店資訊 (抓 Logo 和 店名)
	                    Stores store = storesSearchDao.getStoreById(event.getStoresId());
	                    if (store != null) {
	                        dto.setStoreName(store.getName());
	                        dto.setStoreLogo(store.getImage()); 
	                    }
	                }
	                cartMap.put(eventId, dto);
	            }
	            
	            // 把當前訂單塞入對應的群組
	            CartDTO currentGroup = cartMap.get(eventId);
	            currentGroup.getItems().add(order);
	            
	            // 累加總金額與總數量
	            currentGroup.setTotalAmount(currentGroup.getTotalAmount() + order.getSubtotal());
	            currentGroup.setTotalQuantity(currentGroup.getItems().size());
	            
	            // 記錄該團最新的下單時間 (格式化為字串)
	            if (currentGroup.getLatestOrderTime() == null || 
	                order.getOrderTime().toString().compareTo(currentGroup.getLatestOrderTime()) > 0) {
	                currentGroup.setLatestOrderTime(order.getOrderTime().toString());
	            }
	        } 

	        GroupbuyEventsRes res = new GroupbuyEventsRes(200, "購物車查詢成功");
	        res.setCartData(new ArrayList<>(cartMap.values()));
	        return res;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return new GroupbuyEventsRes(500, "查詢失敗：" + e.getMessage());
	    }
	}
	
}
