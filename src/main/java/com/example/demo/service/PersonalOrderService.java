package com.example.demo.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.constants.PaymentStatus;
import com.example.demo.constants.ResMessage;
import com.example.demo.dao.GroupbuyEventsDao;
import com.example.demo.dao.OrdersDao;
import com.example.demo.dao.PersonalOrderDao;
import com.example.demo.dao.UserDao;
import com.example.demo.entity.GroupbuyEvents;
import com.example.demo.entity.PersonalOrder;
import com.example.demo.request.personalOrderReq;
import com.example.demo.response.BasicRes;


@Service
@Transactional
public class PersonalOrderService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private GroupbuyEventsDao groupbuyEventsDao;
	
//	@Autowired
//	private OrdersDao ordersDao;
	
	@Autowired
	private PersonalOrderDao personalOrderDao;
	
	private BasicRes checkEvent(personalOrderReq req) {
		// 檢查有無所屬團ID
		GroupbuyEvents events = groupbuyEventsDao.findById(req.getEventsId());
		if (events == null) {
			return new BasicRes(ResMessage.EVENTS_NOT_FOUND.getCode(), ResMessage.EVENTS_NOT_FOUND.getMessage());
		}
		// 檢查跟團者ID
		if (userDao.getUserById(req.getUserId()) == null) {
		    return new BasicRes(ResMessage.USER_NOT_FOUND.getCode(), ResMessage.USER_NOT_FOUND.getMessage());
		}
		// 總金額
	      if (req.getTotalSum() <= 0) {
              return new BasicRes(400, "訂單總額不可為空或為0");
          }
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}
	// 新增
	public BasicRes addPersonalOrder(personalOrderReq req) {
		BasicRes checkResult = checkEvent(req);
		if (checkResult.getCode() != ResMessage.SUCCESS.getCode()) {
			return checkResult;
		}
		PersonalOrder po = personalOrderDao.findByEventsIdAndUserId(req.getEventsId(), req.getUserId());
	    if (po != null) {
	        return new BasicRes(400, "新增失敗：該用戶在此活動中已存在結算單");
	    }

	    try {
	        PersonalOrder personalOrder = new PersonalOrder();
	        personalOrder.setEventsId(req.getEventsId());
	        personalOrder.setUserId(req.getUserId());
	        personalOrder.setTotalWeight(req.getTotalWeight());
	        personalOrder.setPersonFee(req.getPersonFee());
	        personalOrder.setTotalSum(req.getTotalSum());
	        personalOrder.setPaymentStatus(PaymentStatus.UNPAID);
	        personalOrderDao.save(personalOrder);
	        return new BasicRes(200, "個人結算單建立成功");
	    } catch (Exception e) {
	        return new BasicRes(500, "資料庫寫入異常，請檢查欄位格式");
	    }
	}
	
	//更新
	public BasicRes updatePersonalOrder(personalOrderReq req) {
		try {
	        // 查詢該筆「結算單」是否存在
	        PersonalOrder order = personalOrderDao.findByEventsIdAndUserId(req.getEventsId(), req.getUserId());
	        if (order == null) {
	            return new BasicRes(404, "更新失敗：找不到此結算單資料");
	        }
	        order.setEventsId(req.getEventsId());
	        order.setUserId(req.getUserId());
	        order.setTotalWeight(req.getTotalWeight());
	        order.setPersonFee(req.getPersonFee());
	        order.setTotalSum(req.getTotalSum());
	        
	        // 狀態與時間邏輯處理
	        PaymentStatus newStatus = req.getPaymentStatus();
	        if (newStatus == PaymentStatus.PAID || newStatus == PaymentStatus.CONFIRMED) {
	            // 只有狀態改為支付相關，且原本沒有時間時才填入
	            if (order.getPaymentTime() == null) {
	                order.setPaymentTime(LocalDateTime.now());
	            }
	        } 
	        order.setPaymentStatus(newStatus);
	        personalOrderDao.save(order);
	        return new BasicRes(200, "更新成功");

	    } catch (Exception e) {
	        return new BasicRes(500, "更新系統異常: ");
	    }
}
}