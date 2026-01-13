package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.constants.GroupbuyStatusEnum;
import com.example.demo.constants.ResMessage;
import com.example.demo.dao.GroupbuyEventsDao;
import com.example.demo.dao.StoresSearchDao;
import com.example.demo.dao.UserDao;
import com.example.demo.entity.GroupbuyEvents;
import com.example.demo.entity.Stores;
import com.example.demo.entity.User;
import com.example.demo.request.GroupbuyEventsReq;
import com.example.demo.response.BasicRes;
import com.example.demo.vo.MenuVo;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional
public class GroupbuyEventsService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private StoresSearchDao storesSearchDao;

	@Autowired
	private GroupbuyEventsDao groupbuyEventsDao;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	public BasicRes addEvent(GroupbuyEventsReq req) {
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
		if(stores == null) {
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
		if (req.getAnnouncement() == null) {
			return new BasicRes(ResMessage.ANNOUMCEMENT_ERROR.getCode(), //
					ResMessage.ANNOUMCEMENT_ERROR.getMessage());
		}
		// 檢查總運費
		if (req.getShippingFee() == 0) {
			return new BasicRes(ResMessage.SHIPPING_FEE_ERROR.getCode(), //
					ResMessage.SHIPPING_FEE_ERROR.getMessage());
		}
		// 檢查商家類型
		if (req.getType() == null) {
			return new BasicRes(ResMessage.TYPE_ERROR.getCode(), //
					ResMessage.TYPE_ERROR.getMessage());
		}
		// 金額門檻
		if (req.getLimitation() == 0) {
			return new BasicRes(ResMessage.SPLIT_TYPE_ERROR.getCode(), //
					ResMessage.SPLIT_TYPE_ERROR.getMessage());
		}

		// 檢查推薦品項是否存在
		List<Map<String, Object>> menuList  = storesSearchDao.getMenuByStoreId(req.getStoresId());
		// 前端給的推薦品項
		List<Integer> recommendList = req.getRecommendList();
		if(menuList == null) {
			return new BasicRes(ResMessage.MENU_NOT_FOUND.getCode(), //
					ResMessage.MENU_NOT_FOUND.getMessage());
		}
		List<MenuVo> menuVoList = new ArrayList<>();
		for (Map<String, Object> map : menuList) {
			// 先轉簡單欄位
			Map<String, Object> tempMap = new HashMap<>(map);
			// 先移出unusual
			tempMap.remove("unusual");
			MenuVo mVo = mapper.convertValue(tempMap, MenuVo.class);
			 menuVoList.add(mVo);
		}
		
		// 檢查推薦菜單品項
		Set<Integer> validMenuIds = menuList.stream()
				/*遍歷每一個 Map (m)，從中取出鍵值為 "id" 的物件，並強制轉型為 Integer。
				 * 這時流裡面的東西從「整個選單資訊」變成了「只有 ID」。
				 * */
                .map(m -> (Integer) m.get("id"))
                // .collect : 數據重新打包。
                //toSet : 將這些 ID 存入一個 Set 集合。
                .collect(java.util.stream.Collectors.toSet());
		if (recommendList != null) {
            for (Integer recId : req.getRecommendList()) {
                if (!validMenuIds.contains(recId)) {
                    return new BasicRes(ResMessage.MENU_ITEM_NOT_FOUND.getCode(), ResMessage.MENU_ITEM_NOT_FOUND.getMessage());
                }
            }
        }

		
//		LocalDateTime now = LocalDateTime.now();
//	    if (req.getEndTime().isBefore(now.plusMinutes(30))) {
//	        return new BasicRes(ResMessage.END_TIME_ERROR.getCode(),
//	                           "結單時間至少需設定在 5 分鐘後");
//	    }
//	    
		
		// 新增資料
		GroupbuyEvents event = new GroupbuyEvents();
		event.setHostId(req.getHostId());
	    event.setStoresId(req.getStoresId());
	    event.setStatus(GroupbuyStatusEnum.OPEN); 
	    event.setEndTime(req.getEndTime());
	    event.setSplitType(req.getSplitType());
	    event.setTotalOrderAmount(0);
	    event.setShippingFee(req.getShippingFee() != null ? req.getShippingFee() : 0);
	    event.setLimitation( req.getLimitation());
	    event.setAnnouncement(req.getAnnouncement());
	    event.setType(req.getType());
	    event.setRecommendDescription(req.getRecommendDescription());


	    try {
	        // 1. 將 List 序列化為 JSON 字串
	        String tempMenuJson = "";
	        if (req.getTempMenuList() != null && !req.getTempMenuList().isEmpty()) {
	            tempMenuJson = mapper.writeValueAsString(req.getTempMenuList());
	        }
	        String recommendJson = "";
	        if (req.getRecommendList() != null && !req.getRecommendList().isEmpty()) {
	            recommendJson = mapper.writeValueAsString(req.getRecommendList());
	        }
	        // 2. 將轉好的字串存入 event 物件 
	        event.setTempMenuList(tempMenuJson);
	        event.setRecommendList(recommendJson);

	        // 3.存入資料庫
	        groupbuyEventsDao.addEvent(
	            event.getHostId(),
	            event.getStoresId(),
	            event.getStatus().name(),         
	            event.getEndTime(),
	            event.getTotalOrderAmount(),
	            event.getShippingFee(),
	            event.getSplitType().name(),
	            event.getAnnouncement(),
	            event.getType(),
	            event.getTempMenuList(),   
	            event.getRecommendList(),  
	            event.getRecommendDescription(),
	            event.getLimitation()
	        );
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new BasicRes(ResMessage.EVENT_ERROR.getCode(), ResMessage.EVENT_ERROR.getMessage());
	    }
	    return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
}
}
