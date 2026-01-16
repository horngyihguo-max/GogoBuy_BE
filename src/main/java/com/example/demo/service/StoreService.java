package com.example.demo.service;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.demo.constants.ResMessage;
import com.example.demo.dao.ProductOptionGroupsDao;
import com.example.demo.dao.StoresCreateDao;
import com.example.demo.dao.StoresSearchDao;
import com.example.demo.dao.StoresUpdateDao;
import com.example.demo.entity.ProductOptionGroups;
import com.example.demo.entity.Stores;
import com.example.demo.request.StoresReq;
import com.example.demo.response.BasicRes;
import com.example.demo.response.StroresRes;
import com.example.demo.vo.FeeDescriptionVo;
import com.example.demo.vo.MenuCategoriesVo;
import com.example.demo.vo.MenuVo;
import com.example.demo.vo.PriceLevelVo;
import com.example.demo.vo.ProductOptionGroupsVo;
import com.example.demo.vo.ProductOptionItemsVo;
import com.example.demo.vo.StoreOperatingHoursVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class StoreService {

	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private StoresCreateDao storesCreateDao;

	@Autowired
	private StoresUpdateDao storesUpdateDao;

	@Autowired
	private StoresSearchDao storesSearchDao;

	@Autowired
	private ProductOptionGroupsDao productOptionGroupsDao;
	
//	檢查

	// 檢查店家格式
	private void checkStore(StoresReq req) throws Exception {
		// 電話長度過長
		if (req.getPhone() == null || req.getPhone().length() > 10) {
			throw new Exception(ResMessage.PHONE_SIZE_ERROR.getMessage());
		}

		// category類型驗證
		if (!(req.getCategory().equals("fast") || req.getCategory().equals("slow"))) {
			throw new Exception(ResMessage.CATEGORY_ERROR.getMessage());
		}

		// 檢查運費級距
		if (req.getFee_description() != null) {
			for (FeeDescriptionVo vo : req.getFee_description()) {
				if (vo.getKm() < 0 || vo.getFee() < 0)
					throw new Exception("運費或距離不可為負");
			}
		}
	}

	// 檢查店家是否存在
	private void checkStoreExist(int storeId) throws Exception {
		Stores existingStore = storesSearchDao.getStoreById(storeId);

		if (existingStore == null) {
			throw new Exception(ResMessage.STORE_NOT_FOUND.getMessage());
		}
	}

	// 檢查營業時間
	private void checkHours(List<StoreOperatingHoursVo> list) throws Exception {

		if (list == null)
			return;

		java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm");

		for (StoreOperatingHoursVo vo : list) {
			try {
				// 檢查是否為 1~7
				DayOfWeek.of(vo.getWeek());
				java.time.LocalTime.parse(vo.getOpenTime(), timeFormatter);
				java.time.LocalTime.parse(vo.getCloseTime(), timeFormatter);
			} catch (DateTimeParseException e) {
				// 如果格式不對 (例如 25:00 或 12:70) 會進到這裡
				throw new Exception("時間格式錯誤，請使用 HH:mm 格式 (例如 09:00)");
				// 子類別
			} catch (DateTimeException e) {
				throw new Exception("無效的星期數值: " + vo.getWeek());
				// 父類別
			}
		}
	}

	// 檢查品項
	private void checkMenu(List<MenuVo> menuList, List<MenuCategoriesVo> categoriesList) throws Exception {
		if (menuList != null) {
			for (MenuVo vo : menuList) {
				if (vo.getName() == null || vo.getName().trim().isEmpty())
					throw new Exception("商品名不可為空");

				if (vo.getBasePrice() < 0)
					throw new Exception("商品價格不可為負");

				if (vo.getCategoryId() < 0) {
					throw new Exception("不存在的商品規格(<0)");
				}
			}
		}

		// 檢查分類
		if (categoriesList != null) {
			for (MenuCategoriesVo vo : categoriesList) {
				// if (vo.getName() == null || vo.getName().trim().isEmpty())
				if (!StringUtils.hasText(vo.getName()))
					throw new Exception("類別名不可為空");

				if (vo.getPriceLevel() != null) {
					for (PriceLevelVo p : vo.getPriceLevel()) {
						// if (p.getName() == null || p.getName().trim().isEmpty())
						if (!StringUtils.hasText(p.getName()))
							throw new Exception("規格名不可為空");

						if (p.getPrice() < 0)
							throw new Exception("級距價格不可為負");
					}
				}
			}
		}
	}

	// 檢查選項(群組)
	private void checkOptions(List<ProductOptionGroupsVo> groupList) throws Exception {
		if (groupList == null)
			return;

		for (ProductOptionGroupsVo vo : groupList) {
			// if (vo.getName() == null || vo.getName().trim().isEmpty())
			if (!StringUtils.hasText(vo.getName()))
				throw new Exception("選項群組名不可為空");

			if (vo.getMaxSelection() < 0)
				throw new Exception("規格 [" + vo.getName() + "] 的最多可選項數不可為負數");

			if (vo.getItems() != null) {
				for (ProductOptionItemsVo item : vo.getItems()) {
					if (item.getName() == null || item.getName().trim().isEmpty())
						throw new Exception("選項名不可為空");

					if (item.getExtraPrice() != null && item.getExtraPrice() < 0)
						throw new Exception("加價不可為負");
				}
			}
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void saveSubTables(int storeId, StoresReq req) throws JsonProcessingException {
		// 填入營業時間
		List<StoreOperatingHoursVo> operatingHoursvoList = req.getOperatingHoursVoList();

		for (StoreOperatingHoursVo vo : operatingHoursvoList) {
			storesCreateDao.addOperatingHours(storeId, vo.getWeek(), vo.getOpenTime(), vo.getCloseTime());
		}

		// 填入品項
		List<MenuVo> menuVoList = req.getMenuVoList();

		for (MenuVo vo : menuVoList) {
			String unusualStr = mapper.writeValueAsString(vo.getUnusual());
			storesCreateDao.addMenu(storeId, vo.getCategoryId(), vo.getName(), vo.getDescription(), //
					vo.getBasePrice(), vo.getImage(), true, unusualStr);
		}

		// 填入品項類別
		List<MenuCategoriesVo> MenuCategoriesVoList = req.getMenuCategoriesVoList();

		for (MenuCategoriesVo vo : MenuCategoriesVoList) {
			String priceLevelListStr = mapper.writeValueAsString(vo.getPriceLevel());
			storesCreateDao.addCategory(storeId, vo.getName(), priceLevelListStr);
		}

		// 填入選項群組
		List<ProductOptionGroupsVo> ProductOptionGroupsVoList = req.getProductOptionGroupsVoList();

		for (ProductOptionGroupsVo vo : ProductOptionGroupsVoList) {
			ProductOptionGroups group = new ProductOptionGroups();
			group.setStoresId(storeId);
			group.setName(vo.getName());
			group.setRequired(vo.isRequired());
			group.setMaxSelection(vo.getMaxSelection());

			group = productOptionGroupsDao.save(group);
			int groupId = group.getId();

//			storesCreateDao.addOptionGroups(storeId, vo.getName(), vo.isRequired(), vo.getMaxSelection());
//			int groupId = storesCreateDao.getLastInsertId();

			// 填入選項
			List<ProductOptionItemsVo> itemVoList = vo.getItems();

			if (itemVoList != null) {
				for (ProductOptionItemsVo itemVo : itemVoList) {
					storesCreateDao.addOptionItems(groupId, itemVo.getName(), itemVo.getExtraPrice());
				}
			}
		}
	}

	// 建立店家
	@Transactional(rollbackFor = Exception.class)
	public BasicRes create(StoresReq req) throws Exception {

		// 呼叫檢查的function
		checkStore(req);
		checkHours(req.getOperatingHoursVoList());
		checkMenu(req.getMenuVoList(), req.getMenuCategoriesVoList());
		checkOptions(req.getProductOptionGroupsVoList());

		// 店家已存在
		if (storesCreateDao.existsByPhone(req.getPhone()) > 0) {
			return new BasicRes(ResMessage.STORE_EXISTS.getCode(), //
					ResMessage.STORE_EXISTS.getMessage());
		}

		// 傳店家表取店家ID
		Stores store = new Stores();
		String feeStr = mapper.writeValueAsString(req.getFee_description());

		store.setName(req.getStoresname());
		store.setPhone(req.getPhone());
		store.setAddress(req.getAddress());
		store.setCategory(req.getCategory());
		store.setType(req.getType());
		store.setMemo(req.getMemo());
		store.setImage(req.getImage());
		store.setFeeDescription(feeStr);
		store.setPublish(req.isPublish());
		store.setCreatedBy(req.getCreatedBy());

		store = storesCreateDao.save(store);

//		storesCreateDao.addStore(req.getStoresname(), req.getPhone(), req.getAddress(), //
//				req.getCategory(), req.getType(), req.getMemo(), //
//				req.getImage(), feeStr, req.isPublish(), req.getCreatedBy());

		int storeId = store.getId();

		// 填入子表
		saveSubTables(storeId, req);

		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}

	// 修改店家資訊
	@Transactional(rollbackFor = Exception.class)
	public BasicRes update(int storeId, StoresReq req) throws Exception {

		// 呼叫檢查function
		checkStoreExist(storeId);
		checkStore(req);
		checkHours(req.getOperatingHoursVoList());
		checkMenu(req.getMenuVoList(), req.getMenuCategoriesVoList());
		checkOptions(req.getProductOptionGroupsVoList());

		// 檢查電話是否已被其他店家佔用 (排除目前的 storeId)
		if (storesUpdateDao.countByPhoneExcludeSelf(req.getPhone(), storeId) > 0) {
			return new BasicRes(ResMessage.STORE_EXISTS.getCode(), //
					ResMessage.STORE_EXISTS.getMessage());
		}

		// 更新店家主表資訊
		String feeStr = mapper.writeValueAsString(req.getFee_description());
		storesUpdateDao.updateStore(storeId, //
				req.getStoresname(), req.getPhone(), //
				req.getAddress(), req.getCategory(), //
				req.getType(), req.getMemo(), //
				req.getImage(), feeStr, req.isPublish());

		// 清除舊有的子表資料 (先刪除有外鍵關聯的底層資料)
		// 順序：OptionItems -> OptionGroups -> Menu -> Categories -> Hours
		// 			項目選項 		項目群組 	菜單 	菜單分類 	營業時間
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

	// 資料庫刪除
	@Transactional(rollbackFor = Exception.class)
	public BasicRes deleteFullStore(int storeId) throws Exception {

		checkStoreExist(storeId);
		// 刪除最底層的選項細項 (依賴於 groups)
		storesUpdateDao.deleteOptionItemsByStoreId(storeId);

		// 刪除選項群組
		storesUpdateDao.deleteOptionGroupsByStoreId(storeId);

		// 刪除菜單品項
		storesUpdateDao.deleteMenuByStoreId(storeId);

		// 刪除品項類別
		storesUpdateDao.deleteMenuCategoriesByStoreId(storeId);

		// 刪除營業時間
		storesUpdateDao.deleteOperatingHoursByStoreId(storeId);

		// 刪除店家主體
		int result = storesUpdateDao.deleteStoreById(storeId);

		if (result > 0) {
			return new BasicRes(ResMessage.SUCCESS.getCode(), "店家ID" + storeId + "已被物理抹除");
		} else {
			return new BasicRes(ResMessage.STORE_NOT_FOUND.getCode(), ResMessage.STORE_NOT_FOUND.getMessage());
		}
	}

//		軟刪除
	@Transactional(rollbackFor = Exception.class)
	public BasicRes deleteStore(int storeId) throws Exception {
		checkStoreExist(storeId);

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

	public StroresRes getStoresByName(String name) {
		try {
			// 修正點：如果未輸入或只有空白，直接回傳空清單
			// if (name == null || name.trim().isEmpty()) {
			if (!StringUtils.hasText(name)) {
				return new StroresRes(ResMessage.INPUT_IS_EMPTY.getCode(), "請輸入搜尋關鍵字", null);
			}

			List<Stores> storeList = storesSearchDao.findStoresByNameLike(name);

			return new StroresRes(ResMessage.SUCCESS.getCode(), "搜尋成功，共 " + storeList.size() + " 筆", storeList);
		} catch (Exception e) {
			return new StroresRes(500, "搜尋失敗: " + e.getMessage());
		}
	}

	public StroresRes getStoreById(int storesId) {
		try {
			// 查詢店家主表 (確認是否存在且未被軟刪除)
			Stores store = storesSearchDao.getStoreById(storesId);

			if (store == null) {
				return new StroresRes(//
						ResMessage.STORE_NOT_FOUND.getCode(), //
						ResMessage.STORE_NOT_FOUND.getMessage());
			}

			StroresRes res = new StroresRes( //
					ResMessage.SUCCESS.getCode(), //
					ResMessage.SUCCESS.getMessage(), //
					List.of(store));

			// 營業時間
			List<Map<String, Object>> hoursMap = storesSearchDao.getOperatingHoursByStoreId(storesId);
			res.setOperatingHoursVoList(mapper.convertValue(hoursMap, new TypeReference<List<StoreOperatingHoursVo>>() {
			}));

			// 菜單分類
			List<Map<String, Object>> categoriesMap = storesSearchDao.getCategoriesByStoreId(storesId);
			List<MenuCategoriesVo> categoriesVoList = new ArrayList<>();

			for (Map<String, Object> map : categoriesMap) {
				MenuCategoriesVo catVo = new MenuCategoriesVo();
				catVo.setName((String) map.get("name"));
				// 手動將 price_level 字串轉為 List
				String priceLevelStr = (String) map.get("priceLevel");

				if (priceLevelStr != null && !priceLevelStr.isEmpty()) {
					// Json轉java物件 類型參考
					catVo.setPriceLevel(mapper.readValue(priceLevelStr, new TypeReference<List<PriceLevelVo>>() {
					}));
				}
				categoriesVoList.add(catVo);
			}
			res.setMenuCategoriesVoList(categoriesVoList);

			// 菜單品項
			List<Map<String, Object>> menuMap = storesSearchDao.getMenuByStoreId(storesId);
			List<MenuVo> menuVoList = new ArrayList<>();

			for (Map<String, Object> map : menuMap) {
				// 先轉簡單欄位
				Map<String, Object> tempMap = new HashMap<>(map);
				// 先移出unusual
				tempMap.remove("unusual");
				// 生成新map(不含unusual)
				MenuVo mVo = mapper.convertValue(tempMap, MenuVo.class);
				// 手動將 unusual 字串轉為 Object/Map
				String unusualStr = (String) map.get("unusual");

				//				if (unusualStr != null && !unusualStr.isEmpty()) {
				if (StringUtils.hasText(unusualStr)) {
					// 解析為 Map，保留 JSON 的鍵值結構
					mVo.setUnusual(mapper.readValue(unusualStr, new TypeReference<Map<String, Object>>() {
					}));
				}
				menuVoList.add(mVo);
			}
			res.setMenuVoList(menuVoList);

			// 選項群組與細項 (兩層巢狀)
			List<Map<String, Object>> groupsMap = storesSearchDao.getOptionGroupsByStoreId(storesId);
			List<ProductOptionGroupsVo> groupsVo = mapper.convertValue(groupsMap,
					new TypeReference<List<ProductOptionGroupsVo>>() {
					});

			for (ProductOptionGroupsVo group : groupsVo) {
				List<Map<String, Object>> itemsMap = storesSearchDao.getOptionItemsByGroupId(group.getId());
				group.setItems(mapper.convertValue(itemsMap, new TypeReference<List<ProductOptionItemsVo>>() {
				}));
			}
			res.setProductOptionGroupsVoList(groupsVo);

			// 處理主表內的 JSON 字串 (如 FeeDescription)
			// store 沒有 VoList 欄位，但 res 有，解析後塞給 res
			if (store.getFeeDescription() != null) {
				List<FeeDescriptionVo> fees = mapper.readValue(store.getFeeDescription(),
						new TypeReference<List<FeeDescriptionVo>>() {
						});
				res.setFeeDescriptionVoList(fees);
			}

			return res;

		} catch (Exception e) {
			return new StroresRes(500, "取得店家資料失敗: " + e.getMessage(), null);
		}
	}
	
	
//	全部店家
	public StroresRes getAllStores() {
		
		List<Stores>storesList = storesSearchDao.getAllStores();
		return new StroresRes(ResMessage.STORE_NOT_FOUND.getCode(), "共"+storesList.size()+"筆資料" , storesList);
	}
}
