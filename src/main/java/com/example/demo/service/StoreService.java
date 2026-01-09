package com.example.demo.service;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.constants.ResMessage;
import com.example.demo.dao.StoresCreateDao;
import com.example.demo.dao.StoresUpdateDao;
import com.example.demo.request.StoresReq;
import com.example.demo.response.BasicRes;
import com.example.demo.vo.FeeDescriptionVo;
import com.example.demo.vo.MenuCategoriesVo;
import com.example.demo.vo.MenuVo;
import com.example.demo.vo.PriceLevelVo;
import com.example.demo.vo.ProductOptionGroupsVo;
import com.example.demo.vo.ProductOptionItemsVo;
import com.example.demo.vo.StoreOperatingHoursVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class StoreService {

	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private StoresCreateDao storesCreateDao;
		
	@Autowired
	private StoresUpdateDao storesUpdateDao;
	
//	店家
	private void checkStore(StoresReq req) throws Exception {
//		電話長度過長
	    if (req.getPhone() == null || req.getPhone().length() > 10) {
	        throw new Exception(ResMessage.PHONE_SIZE_ERROR.getMessage());
	    }
//	  	category類型驗證
	    if (!(req.getCategory().equals("fast") || req.getCategory().equals("slow"))) {
	        throw new Exception(ResMessage.CATEGORY_ERROR.getMessage());
	    }
//		檢查運費級距
	    if (req.getFee_description() != null) {
	        for (FeeDescriptionVo vo : req.getFee_description()) {
	            if (vo.getKm() < 0 || vo.getFee() < 0) throw new Exception("運費或距離不可為負");
	        }
	    }
	}
//時刻
	private void checkHours(List<StoreOperatingHoursVo> list) throws Exception {
	    if (list == null) return;
	    java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter
	    		.ofPattern("HH:mm");
	    for (StoreOperatingHoursVo vo : list) {
	    	try {
				// 檢查是否為 1~7
				DayOfWeek.of(vo.getWeek());
				java.time.LocalTime.parse(vo.getOpenTime(), timeFormatter);
				java.time.LocalTime.parse(vo.getCloseTime(), timeFormatter);
			} catch (DateTimeParseException e) {
				// 如果格式不對 (例如 25:00 或 12:70) 會進到這裡
				throw new Exception("時間格式錯誤，請使用 HH:mm 格式 (例如 09:00)");
//		子類別
			} catch (DateTimeException e) {
				throw new Exception("無效的星期數值: " + vo.getWeek());
//		父類別
			}
	    }
	}
//品項
	private void checkMenu(List<MenuVo> menuList, List<MenuCategoriesVo> categoriesList) throws Exception {
	    if (menuList != null) {
	        for (MenuVo vo : menuList) {
	            if (vo.getName() == null || vo.getName().trim().isEmpty()) throw new Exception("商品名不可為空");
	            if (vo.getBasePrice() < 0) throw new Exception("商品價格不可為負");
	            if (vo.getCategoryId() < 0) {throw new Exception("不存在的商品規格(<0)");
				}
	        }
	    }
	    if (categoriesList != null) {
	        for (MenuCategoriesVo vo : categoriesList) {
	            if (vo.getName() == null || vo.getName().trim().isEmpty()) throw new Exception("類別名不可為空");
	            if (vo.getPriceLevel() != null) {
	                for (PriceLevelVo p : vo.getPriceLevel()) {
	                	if (p.getName() == null||p.getName().trim().isEmpty())throw new Exception("規格名不可為空");
	                    if (p.getPrice() < 0) throw new Exception("級距價格不可為負");
	                }
	            }
	        }
	    }
	}
//選項(群組)
	private void checkOptions(List<ProductOptionGroupsVo> groupList) throws Exception {
	    if (groupList == null) return;
	    for (ProductOptionGroupsVo vo : groupList) {
	        if (vo.getName() == null || vo.getName().trim().isEmpty()) throw new Exception("選項群組名不可為空");
	        if (vo.getMaxSelection() < 0) throw new Exception("規格 [" + vo.getName() + "] 的最多可選項數不可為負數");
	        if (vo.getItems() != null) {
	            for (ProductOptionItemsVo item : vo.getItems()) {
	                if (item.getName() == null || item.getName().trim().isEmpty()) throw new Exception("選項名不可為空");
	                if (item.getExtraPrice() != null && item.getExtraPrice() < 0) throw new Exception("加價不可為負");
	            }
	        }
	    }
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void saveSubTables(int storeId, StoresReq req) throws JsonProcessingException {
//		填入營業時間
		List<StoreOperatingHoursVo> operatingHoursvoList = req.getOperatingHoursVoList();
		for (StoreOperatingHoursVo vo : operatingHoursvoList) {
			storesCreateDao.addOperatingHours(storeId, vo.getWeek(), vo.getOpenTime(), vo.getCloseTime());
		}
//		填入品項
		List<MenuVo> menuVoList = req.getMenuVoList();
		for (MenuVo vo : menuVoList) {
			String unusualStr = mapper.writeValueAsString(vo.getUnusual());
			storesCreateDao.addMenu(storeId, vo.getCategoryId(), vo.getName(), vo.getDescription(), //
					vo.getBasePrice(), vo.getImage(), true, unusualStr);
		}
//		填入品項類別
		List<MenuCategoriesVo> MenuCategoriesVoList = req.getMenuCategoriesVoList();
		for (MenuCategoriesVo vo : MenuCategoriesVoList) {
			String priceLevelListStr = mapper.writeValueAsString(vo.getPriceLevel());
			storesCreateDao.addCategory(storeId, vo.getName(), priceLevelListStr);
		}
//		填入選項群組
		List<ProductOptionGroupsVo> ProductOptionGroupsVoList = req.getProductOptionGroupsVoList();
		for (ProductOptionGroupsVo vo : ProductOptionGroupsVoList) {
			storesCreateDao.addOptionGroups(storeId, vo.getName(), vo.isRequired(), vo.getMaxSelection());
			int groupId = storesCreateDao.getLastInsertId();
//		填入選項
			List<ProductOptionItemsVo> itemVoList = vo.getItems();
			if (itemVoList != null) {
				for (ProductOptionItemsVo itemVo : itemVoList) {
					storesCreateDao.addOptionItems(groupId, itemVo.getName(), itemVo.getExtraPrice());
				}
			}
		}
	}
	// 回滾
	@Transactional(rollbackFor = Exception.class)
	public BasicRes create(StoresReq req) throws Exception {
		
		checkStore(req);
	    checkHours(req.getOperatingHoursVoList());
	    checkMenu(req.getMenuVoList(), req.getMenuCategoriesVoList());
	    checkOptions(req.getProductOptionGroupsVoList());

//    店家已存在
		if (storesCreateDao.existsByPhone(req.getPhone())>0) {
		    return new BasicRes(ResMessage.STORE_EXISTS.getCode(), //
					ResMessage.STORE_EXISTS.getMessage());
		}


//		傳店家表取店家ID
		String feeStr = mapper.writeValueAsString(req.getFee_description());
		storesCreateDao.addStore(req.getStoresname(), req.getPhone(), req.getAddress(), //
				req.getCategory(), req.getType(), req.getMemo(), //
				req.getImage(), feeStr, req.isPublish(), req.getCreatedBy());

		int storeId = storesCreateDao.getLastInsertId();
		
//		填入子表
		saveSubTables(storeId, req);

		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}

	// 店家修改
		@Transactional(rollbackFor = Exception.class)
		public BasicRes update(int storeId, StoresReq req) throws Exception {
			// 檢查
			checkStore(req);
			checkHours(req.getOperatingHoursVoList());
			checkMenu(req.getMenuVoList(), req.getMenuCategoriesVoList());
			checkOptions(req.getProductOptionGroupsVoList());

			// 檢查電話是否已被其他店家佔用 (排除目前的 storeId)
			if (storesUpdateDao.countByPhoneExcludeSelf(req.getPhone(), storeId) > 0) {
				return new BasicRes(ResMessage.STORE_EXISTS.getCode(), //
						ResMessage.STORE_EXISTS.getMessage());
			}

			//  更新店家主表資訊
			String feeStr = mapper.writeValueAsString(req.getFee_description());
			storesUpdateDao.updateStore(storeId, req.getStoresname(), req.getPhone(), req.getAddress(), //
					req.getCategory(), req.getType(), req.getMemo(), //
					req.getImage(), feeStr, req.isPublish());

			// 清除舊有的子表資料 (先刪除有外鍵關聯的底層資料)
			// 順序：OptionItems -> OptionGroups -> Menu -> Categories -> Hours
			storesUpdateDao.deleteOptionItemsByStoreId(storeId);
			storesUpdateDao.deleteOptionGroupsByStoreId(storeId);
			storesUpdateDao.deleteMenuByStoreId(storeId);
			storesUpdateDao.deleteMenuCategoriesByStoreId(storeId);
			storesUpdateDao.deleteOperatingHoursByStoreId(storeId);

			// 重新寫入子表資料
			saveSubTables(storeId, req);

			return new BasicRes(ResMessage.SUCCESS.getCode(), //
					ResMessage.SUCCESS.getMessage());
		}
	
//		物理全刪
		@Transactional(rollbackFor = Exception.class)
		public BasicRes deleteFullStore(int storeId) {
		    //刪除最底層的選項細項 (依賴於 groups)
		    storesUpdateDao.deleteOptionItemsByStoreId(storeId);
		    
		    //刪除選項群組
		    storesUpdateDao.deleteOptionGroupsByStoreId(storeId);
		    
		    //刪除菜單品項
		    storesUpdateDao.deleteMenuByStoreId(storeId);
		    
		    //刪除品項類別
		    storesUpdateDao.deleteMenuCategoriesByStoreId(storeId);
		    
		    //刪除營業時間
		    storesUpdateDao.deleteOperatingHoursByStoreId(storeId);
		    
		    //刪除店家主體
		    int result = storesUpdateDao.deleteStoreById(storeId);
		    
		    if (result > 0) {
		        return new BasicRes(ResMessage.SUCCESS.getCode(), "店家ID"+storeId+"已被物理抹除");
		    } else {
		        return new BasicRes(ResMessage.STORE_NOT_FOUND.getCode(), ResMessage.STORE_NOT_FOUND.getMessage());
		    }
		}
		
//		軟刪除
		@Transactional(rollbackFor = Exception.class)
		public BasicRes deleteStore(int storeId) throws Exception {
		    
		    // 刪除所有子表資料
		    storesUpdateDao.deleteOptionItemsByStoreId(storeId);
		    storesUpdateDao.deleteOptionGroupsByStoreId(storeId);
		    storesUpdateDao.deleteMenuByStoreId(storeId);
		    storesUpdateDao.deleteMenuCategoriesByStoreId(storeId);
		    storesUpdateDao.deleteOperatingHoursByStoreId(storeId);
		    
		    // 主表執行「軟刪除」 (將 is_deleted 改為 true)
		    int result = storesUpdateDao.softDeleteStoreById(storeId);
		    
		    if (result <= 0) {
		        throw new Exception("找不到該店家或已被刪除 (ID: " + storeId + ")");
		    }
		    
		    return new BasicRes(ResMessage.SUCCESS.getCode(), "店家 ID " + storeId + " 已成功軟刪除");
		}
}
