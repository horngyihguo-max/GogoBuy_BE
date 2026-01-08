package com.example.demo.service;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.constants.ResMessage;
import com.example.demo.dao.StoresDao;
import com.example.demo.request.StoresReq;
import com.example.demo.response.BasicRes;
import com.example.demo.vo.FeeDescriptionVo;
import com.example.demo.vo.MenuCategoriesVo;
import com.example.demo.vo.MenuVo;
import com.example.demo.vo.PriceLevelVo;
import com.example.demo.vo.ProductOptionGroupsVo;
import com.example.demo.vo.ProductOptionItemsVo;
import com.example.demo.vo.StoreOperatingHoursVo;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class StoreService {

	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private StoresDao StoresDao;

	// 回滾
	@Transactional(rollbackFor = Exception.class)
	public BasicRes create(StoresReq req) throws Exception {
//		店家部分

//		電話長度過長
		if (req.getPhone() == null || req.getPhone().length() > 10) {
			return new BasicRes(ResMessage.PHONE_SIZE_ERROR.getCode(), //
					ResMessage.PHONE_SIZE_ERROR.getMessage());
		}
//	  	category類型驗證
		if (!(req.getCategory().equals("fast") || req.getCategory().equals("slow"))) {
			return new BasicRes(ResMessage.CATEGORY_ERROR.getCode(), //
					ResMessage.CATEGORY_ERROR.getMessage());
		}
//		檢查運費級距
		List<FeeDescriptionVo> FeeDescriptionVoList = req.getFee_description();
		if (FeeDescriptionVoList != null) {
			for (FeeDescriptionVo vo : FeeDescriptionVoList) {
				// 檢查距離級距
				if (vo.getKm() < 0) {
					throw new Exception("距離不可為負");
				}
				// 檢查價格 (不能為負數)
				if (vo.getFee() < 0) {
					throw new Exception("類別 [" + vo.getFee() + "] 的價格不可為負數");
				}
			}
		}

//		時段部分
		List<StoreOperatingHoursVo> operatingHoursvoList = req.getOperatingHoursVoList();
		if (operatingHoursvoList != null) {
			for (StoreOperatingHoursVo vo : operatingHoursvoList) {
				if (vo.getOpenTime() == null || vo.getCloseTime() == null) {
					throw new Exception("營業時間不可為空");
				}
				try {
					// 檢查是否為 1~7
					DayOfWeek.of(vo.getWeek());
					java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter
							.ofPattern("HH:mm");
					java.time.LocalTime.parse(vo.getOpenTime(), timeFormatter);
					java.time.LocalTime.parse(vo.getCloseTime(), timeFormatter);
				} catch (DateTimeParseException e) {
					// 如果格式不對 (例如 25:00 或 12:70) 會進到這裡
					throw new Exception("時間格式錯誤，請使用 HH:mm 格式 (例如 09:00)");
//			子類別
				} catch (DateTimeException e) {
					throw new Exception("無效的星期數值: " + vo.getWeek());
//			父類別
				}
			}
		}

//		菜單部分
		List<MenuVo> menuVoList = req.getMenuVoList();
		for (MenuVo vo : menuVoList) {
			if (vo.getName() == null || vo.getName().trim().isEmpty()) {
				throw new Exception("商品名不可為空");
			}
			if (vo.getBasePrice() < 0) {
				throw new Exception("商品價格<0");
			}
			if (vo.getCategoryId() < 0) {
				throw new Exception("不存在的商品規格(<0)");
			}
		}

//		品項類別部分
		List<MenuCategoriesVo> MenuCategoriesVoList = req.getMenuCategoriesVoList();
		for (MenuCategoriesVo vo : MenuCategoriesVoList) {
			if (vo.getName() == null || vo.getName().trim().isEmpty()) {
				throw new Exception("類別名不可為空");
			}
			List<PriceLevelVo> priceLevels = vo.getPriceLevel();
			if (priceLevels != null) {
				for (PriceLevelVo pVo : priceLevels) {
					// 檢查名稱 (例如 M, L)
					if (pVo.getName() == null || pVo.getName().trim().isEmpty()) {
						throw new Exception("類別 [" + vo.getName() + "] 的價格尺寸名稱不可為空");
					}
					// 檢查價格 (不能為負數)
					if (pVo.getPrice() < 0) {
						throw new Exception("類別 [" + vo.getName() + "] 的價格不可為負數");
					}
				}
			}
		}
//		店家Groups個別設定部分(糖冰尺寸加料)
		List<ProductOptionGroupsVo> ProductOptionGroupsVoList = req.getProductOptionGroupsVoList();
		for (ProductOptionGroupsVo vo : ProductOptionGroupsVoList) {
			if (vo.getName() == null || vo.getName().trim().isEmpty()) {
				throw new Exception("個別設定的規格名稱不可為空 (例如：甜度、尺寸)");
			}
			if (vo.getMaxSelection() < 0) {
				throw new Exception("規格 [" + vo.getName() + "] 的最多可選項數不可為負數");
			}
//			Items 的驗證
			List<ProductOptionItemsVo> itemVoList = vo.getItems();
			if (itemVoList != null) {
				for (ProductOptionItemsVo itemVo : itemVoList) {
					if (itemVo.getName() == null || itemVo.getName().trim().isEmpty()) {
						throw new Exception("規格 [" + vo.getName() + "] 內的選項名稱不可為空");
					}
					// 檢查價格是否為負
					if (itemVo.getExtraPrice() != null && itemVo.getExtraPrice() < 0) {
						throw new Exception("選項 [" + itemVo.getName() + "] 的加價不可為負數");
					}
				}
			}
		}

//		傳店家表取店家ID
		String feeStr = mapper.writeValueAsString(req.getFee_description());
		StoresDao.addStore(req.getStoresname(), req.getPhone(), req.getAddress(), //
				req.getCategory(), req.getType(), req.getMemo(), //
				req.getImage(), feeStr, req.isPublish(), req.getCreatedBy());

		int storeId = StoresDao.getLastInsertId();

//		填入營業時間
		for (StoreOperatingHoursVo vo : operatingHoursvoList) {
			StoresDao.addOperatingHours(storeId, vo.getWeek(), vo.getOpenTime(), vo.getCloseTime());
		}

//		填入菜單品項 
		for (MenuVo vo : menuVoList) {
			String unusualStr = mapper.writeValueAsString(vo.getUnusual());
			StoresDao.addMenu(storeId, vo.getCategoryId(), vo.getName(), vo.getDescription(), //
					vo.getBasePrice(), vo.getImage(), true, unusualStr);
		}

//		填入商品類別群組
		for (MenuCategoriesVo vo : MenuCategoriesVoList) {
			String priceLevelListStr = mapper.writeValueAsString(vo.getPriceLevel());
			StoresDao.addCategory(storeId, vo.getName(), priceLevelListStr);
		}

//		填入選項群組
		for (ProductOptionGroupsVo vo : ProductOptionGroupsVoList) {
			StoresDao.addOptionGroups(storeId, vo.getName(), vo.isRequired(), vo.getMaxSelection());
			// 取得「該群組」剛產生的 ID
			int groupId = StoresDao.getLastInsertId();

			// 取得該群組下的所有選項並寫入
			List<ProductOptionItemsVo> itemVoList = vo.getItems(); // 假設 Vo 裡有這個 list
			if (itemVoList != null) {
				for (ProductOptionItemsVo itemVo : itemVoList) {
					// 使用剛拿到的 groupId
					StoresDao.addOptionItems(groupId, itemVo.getName(), itemVo.getExtraPrice());
				}
			}
		}

////		各項料價錢檢查
//		List<ProductOptionItemsVo> ProductOptionItemsVoList = req.getProductOptionItemsVoList();
//		for (ProductOptionItemsVo vo : ProductOptionItemsVoList) {
//			if (vo.getName() == null || vo.getName().trim().isEmpty()) {
//				throw new Exception("選項名稱不可為空 (例如：半糖、L杯)");
//			}
//			if (vo.getExtraPrice() < 0 && vo.getExtraPrice() != null) {
//				throw new Exception("選項 [" + vo.getName() + "] 不可為負數");
//			}
//		}

////		填入選項項目
//		for (ProductOptionItemsVo vo : ProductOptionItemsVoList) {
//			StoresDao.addOptionItems(vo.getGroupId(), vo.getName(), vo.getExtraPrice());
//		}

		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}
}
