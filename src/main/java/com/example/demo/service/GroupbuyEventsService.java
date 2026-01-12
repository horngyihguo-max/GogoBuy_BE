package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.constants.ResMessage;
import com.example.demo.request.GroupbuyEventsReq;
import com.example.demo.response.BasicRes;

@Service
@Transactional
public class GroupbuyEventsService {

	public BasicRes addEvent(GroupbuyEventsReq req) {
		// 檢查團長ID
		if (req.getHostId() == null || req.getHostId().trim().isEmpty()) {
			return new BasicRes(ResMessage.USER_NOT_FOUND.getCode(), //
					ResMessage.USER_NOT_FOUND.getMessage());
		}
		// 檢查商店ID
		if (req.getStoresId() == 0) {
			return new BasicRes(ResMessage.USER_NOT_FOUND.getCode(), //
					ResMessage.USER_NOT_FOUND.getMessage());
		}
		// 檢查結束時間
		if (req.getEndTime() == null) {
			return new BasicRes(ResMessage.USER_NOT_FOUND.getCode(), //
					ResMessage.USER_NOT_FOUND.getMessage());
		}
		// 檢查拆帳模式
		if (req.getSplitType() == null) {
			return new BasicRes(ResMessage.USER_NOT_FOUND.getCode(), //
					ResMessage.USER_NOT_FOUND.getMessage());
		}
		return null;
	}
}
