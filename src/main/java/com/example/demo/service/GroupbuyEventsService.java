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
import com.example.demo.constants.ResMessage;
import com.example.demo.dao.GroupbuyEventsDao;
import com.example.demo.dao.StoresSearchDao;
import com.example.demo.dao.UserDao;
import com.example.demo.entity.GroupbuyEvents;
import com.example.demo.entity.Menu;
import com.example.demo.entity.Stores;
import com.example.demo.entity.User;
import com.example.demo.request.GroupbuyEventsReq;
import com.example.demo.response.BasicRes;
import com.example.demo.response.GroupbuyEventsRes;
import com.example.demo.response.StroresRes;
import com.example.demo.vo.MenuVo;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GroupbuyEventsService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private StoresSearchDao storesSearchDao;

	@Autowired
	private GroupbuyEventsDao groupbuyEventsDao;

	private ObjectMapper mapper = new ObjectMapper();

	// 將重複的驗證邏輯提取出來
	private BasicRes checkEvent(GroupbuyEventsReq req) {
		// 檢查團長ID
		if (req.getHostId() == null || req.getHostId().trim().isEmpty()) {
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
		if (req.getShippingFee() == 0 || req.getShippingFee() < 0) {
			return new BasicRes(ResMessage.SHIPPING_FEE_ERROR.getCode(), //
					ResMessage.SHIPPING_FEE_ERROR.getMessage());
		}
		// 檢查商家類型
		if (req.getType() == null) {
			return new BasicRes(ResMessage.TYPE_ERROR.getCode(), //
					ResMessage.TYPE_ERROR.getMessage());
		}
		// 金額門檻
		if (req.getLimitation() == 0 || req.getLimitation() < 0) {
			return new BasicRes(ResMessage.SPLIT_TYPE_ERROR.getCode(), //
					ResMessage.SPLIT_TYPE_ERROR.getMessage());
		}
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	// 新增開團
	public BasicRes addEvent(GroupbuyEventsReq req) {
		BasicRes checkResult = checkEvent(req);
		// 如果 checkResult.getCode() 不等於 SUCCESS.getCode() 就會回傳 錯誤的訊息跟代碼
		if (checkResult.getCode() != ResMessage.SUCCESS.getCode()) {
			return checkResult;
		}
		// 新增資料
		GroupbuyEvents event = new GroupbuyEvents();
		event.setHostId(req.getHostId());
		event.setStoresId(req.getStoresId());
		event.setStatus(GroupbuyStatusEnum.OPEN);
		event.setEndTime(req.getEndTime());
		event.setSplitType(req.getSplitType());
		event.setTotalOrderAmount(0);
		event.setShippingFee(req.getShippingFee() != null ? req.getShippingFee() : 0);
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
			String tempMenuJson = mapper.writeValueAsString(selectedIds);
			String recommendJson = mapper.writeValueAsString(recommendIds);

			// 存入物件
			event.setTempMenuList(tempMenuJson);
			event.setRecommendList(recommendJson);

			groupbuyEventsDao.addEvent(event.getHostId(), //
					event.getStoresId(), //
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
		if (req.isDeleted()) {
			event.setDeleted(true);
			groupbuyEventsDao.save(event);
			return new BasicRes(200, "已成功取消（軟刪除）");
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

	// 回傳開團者的訂單紀錄
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
			List<Menu> list = storesSearchDao.getMenuByMenuId(menuList);
			// 先建立一個物件
			GroupbuyEventsRes res = new GroupbuyEventsRes(200, "menuId 搜尋成功");
			// Set 回去
			res.setMenuList(list);
			// 然後回傳
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
			List<Menu> eventsList = groupbuyEventsDao.getMenuByStoresId(storesId);
			if (eventsList == null || eventsList.isEmpty()) {
				return new GroupbuyEventsRes(200, "查無此店家的菜單資料");
			}
			GroupbuyEventsRes res = new GroupbuyEventsRes(200, "店家菜單搜尋成功");
			res.setMenuList(eventsList);
			return res;
		} catch (Exception e) {
			return  new GroupbuyEventsRes(500, "店家菜單搜尋失敗");
		}
	}
	
	//回傳全部開團的
	public  GroupbuyEventsRes  getAll() {
		try {
	        List<GroupbuyEvents> list = groupbuyEventsDao.getAll();
	        if (list == null ) {
	            return new GroupbuyEventsRes(200, "目前暫無任何開團資料");
	        }
	        GroupbuyEventsRes res = new GroupbuyEventsRes(200, "搜尋成功");
	        res.setGroupbuyEvents(list);
	        return res;
	    } catch (Exception e) {
	        return new GroupbuyEventsRes(500, "查詢失敗");
	    }
	}
}
