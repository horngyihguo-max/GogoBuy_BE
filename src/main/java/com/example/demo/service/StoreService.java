package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.constants.ResMessage;
import com.example.demo.dao.StoresDao;
import com.example.demo.request.StoresReq;
import com.example.demo.response.BasicRes;
import com.example.demo.vo.StoreOperatingHoursVo;

@Service
public class StoreService {

	@Autowired
	private StoresDao StoresDao;

	// 回滾
	@Transactional(rollbackFor = Exception.class)
	public BasicRes create(StoresReq req) throws Exception {
//		店家部分
//		電話長度過長
		if (req.getPhone().length() > 10) {
			return new BasicRes(ResMessage.PHONE_SIZE_ERROR.getCode(), //
					ResMessage.PHONE_SIZE_ERROR.getMessage());
		}
//	  	category類型驗證
		if (!(req.getCategory().equals("fast") || req.getAddress().equals("slow"))) {
			return new BasicRes(ResMessage.CATEGORY_ERROR.getCode(), //
					ResMessage.CATEGORY_ERROR.getMessage());
		}
//		時段部分
		List<StoreOperatingHoursVo> voList = req.getOperatingHoursVoList();
		for (StoreOperatingHoursVo vo : voList) {
			if(vo.getStoresId()) {
				return new BasicRes(ResMessage.CATEGORY_ERROR.getCode(), //
						ResMessage.CATEGORY_ERROR.getMessage());
			}
			/*還沒寫*/
		}
		
//		菜單部分
		
//		品項類別部分
		
//		店家個別設定部分(糖冰尺寸加料)
		
//		各項料價錢
		try {
			StoresDao.addStore(req.getStoresname(), req.getPhone(), req.getAddress(), //
					req.getCategory(), req.getType(), req.getMemo(), //
					req.getImage(), req.getFee_description(), req.isPublish());

		} catch (Exception e) {
			throw e;
		}

		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}
}
