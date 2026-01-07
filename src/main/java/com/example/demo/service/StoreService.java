package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.constants.ResMessage;
import com.example.demo.dao.StoresDao;
import com.example.demo.request.StoresReq;
import com.example.demo.response.BasicRes;

@Service
public class StoreService {

	@Autowired
	private StoresDao StoresDao;

	// 回滾
	@Transactional(rollbackFor = Exception.class)
	public BasicRes create(StoresReq req) throws Exception {

		try {
			StoresDao.addStore(req.getStoresname(), req.getPhone(), req.getCategory(), //
					req.getType(), req.getMemo(), req.getMemo(), //
					req.getImage(), req.getFee_description(), req.isPublish());

		} catch (Exception e) {
			throw e;
		}

		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}
}
