package com.example.demo.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.constants.NotifiCategoryEnum;
import com.example.demo.constants.ResMessage;
import com.example.demo.dao.GroupbuyEventsDao;
import com.example.demo.dao.NotifiMesDao;
import com.example.demo.dao.UserDao;
import com.example.demo.dao.UserNotifDao;
import com.example.demo.entity.NotifiMes;
import com.example.demo.entity.User;
import com.example.demo.entity.UserNotif;
import com.example.demo.request.NotifiMesReq;
import com.example.demo.response.BasicRes;
import com.example.demo.response.MessagesRes;
import com.example.demo.vo.UserNotificationVo;

import jakarta.transaction.Transactional;

@Service
public class MessagesService {

	@Autowired
	private NotifiMesDao notifiMesDao;

	@Autowired
	private UserNotifDao userNotifDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private GroupbuyEventsDao groupbuyEventsDao;

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	
	private void checkNotifiMes(NotifiMesReq req) throws Exception {
//		檢查類別
		try {
			NotifiCategoryEnum category = req.getCategory();
		} catch (IllegalArgumentException e) {
			throw new Exception("不存在的類別喵");
		}
//		檢查標題
		if (req.getTitle() == null || req.getTitle().trim().isEmpty()) {
			throw new Exception("標題不可為空喵");
		}
//		檢查訊息內容
		if (req.getContent() == null || req.getContent().trim().isEmpty()) {
			throw new Exception("內容不可為空喵");
		}
//		檢查URL
		if (req.getTargetUrl() == null || req.getTargetUrl().trim().isEmpty()) {
			throw new Exception("URL不可為空喵");
		}
//		檢查時效
		try {

			LocalDate expiredAt = LocalDate.parse(req.getExpiredAt(), FORMATTER);

			LocalDate now = LocalDate.now();

			if (expiredAt != null && expiredAt.isBefore(now)) {
				throw new Exception("時效期限不可早於現在時間喵");
			}
		} catch (DateTimeParseException e) {
			// 格式不正確的處理
			throw new IllegalArgumentException("無效的時間格式喵: " + req.getExpiredAt());
		}

//		檢查使用者id
		String user = req.getUserId();
		if (user != null) {
			if (user.trim().isEmpty()) {
				throw new Exception("使用者id不能是空白喵");
			}
			User checkUser = userDao.getUserById(user);
			if (checkUser == null) {
				throw new Exception("使用者id不存在喵");
			}
		}
//		檢查活動id
		Integer eventId = req.getEventId();
		if (eventId != null) {
			NotifiCategoryEnum category = req.getCategory();
			if (NotifiCategoryEnum.GROUP_BUY.equals(category)) {
				if (groupbuyEventsDao.findById(eventId) .isEmpty()) {
					throw new Exception("團購不存在喵");
				}
				
			}
		}
	}
	
	private void checkUserNotif(List<UserNotificationVo> voList)throws Exception {
		if (voList==null) {
			throw new Exception("沒有收件者喵(沒有List喵)");
		}
		else {
			for(UserNotificationVo vo:voList) {
				String user = vo.getUserId();
				if(user==null||user.trim().isEmpty()) {
					throw new Exception("沒有收件者喵(收件者為空喵)");
				}
				User checkUser = userDao.getUserById(user);
				if (checkUser == null) {
					throw new Exception("收件者id不存在喵");
				}
			}
		}
	}

	@Transactional
	public BasicRes create(NotifiMesReq req) throws Exception {
		checkNotifiMes(req);
		checkUserNotif(req.getUserNotificationVoList());
		
		NotifiMes mes = new NotifiMes();
	    mes.setCategory(req.getCategory());
	    mes.setTitle(req.getTitle());
	    mes.setContent(req.getContent());
	    mes.setTargetUrl(req.getTargetUrl());
	    
	    mes.setExpiredAt(LocalDate.parse(req.getExpiredAt(), FORMATTER));
	    
	    mes.setUserId(req.getUserId());
	    mes.setEventId(req.getEventId());
	    mes = notifiMesDao.save(mes);
//	    上行作用取代下行 避免ID撞車問題
//		notifiMesDao.createNotifiMes(req.getCategory().name(), req.getTitle(), req.getContent(), req.getTargetUrl(), //
//				req.getExpiredAt(), req.getUserId(), req.getEventId());
	    int nofitId = mes.getId();
		List<UserNotificationVo> UserNotificationVoList = req.getUserNotificationVoList();
		for (UserNotificationVo vo : UserNotificationVoList) {
			userNotifDao.createUserNotifi(vo.getUserId(), nofitId);
		}

		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}
	
	public MessagesRes update(int id, NotifiMesReq req) throws Exception{
//		檢查
		checkNotifiMes(req);
	    checkUserNotif(req.getUserNotificationVoList());
	    NotifiMes mes = notifiMesDao.findById(id).orElseThrow(() -> //
	    new Exception("找不到該筆訊息 ID: " + id + " 喵"));
	    
	    mes.setCategory(req.getCategory());
	    mes.setTitle(req.getTitle());
	    mes.setContent(req.getContent());
	    mes.setTargetUrl(req.getTargetUrl());
	    mes.setExpiredAt(LocalDate.parse(req.getExpiredAt(), FORMATTER));
	    mes.setUserId(req.getUserId());
	    mes.setEventId(req.getEventId());

	    mes = notifiMesDao.save(mes);

	    //	先刪	    
	    userNotifDao.deleteByNotifId(id);
	    
	    //後加	    
	    List<UserNotificationVo> voList = req.getUserNotificationVoList();
	    for (UserNotificationVo vo : voList) {
	        userNotifDao.createUserNotifi(vo.getUserId(), id);
	    }
	    
	    List<UserNotif> updatedUserNotifList = userNotifDao.findByNotifId(id);
		
		return new MessagesRes(
		        ResMessage.SUCCESS.getCode(), 
		        ResMessage.SUCCESS.getMessage()
		       , 
		        List.of(mes), 
		        updatedUserNotifList
		    );
	}
	
	public BasicRes deleteByUser(String userId, int notifId) throws Exception{
		UserNotif userNotif = userNotifDao.findByUserIdAndNotifId(userId, notifId)
	            .orElseThrow(() -> new Exception("找不到該使用者的通知紀錄喵"));
		userNotif.setDeleted(true); 
	    userNotifDao.save(userNotif);
	    
	    return new BasicRes(
	            ResMessage.SUCCESS.getCode(), 
	            "使用者 " + userId + " 的個人通知已刪除"
	        );
	    
	}
	
	public BasicRes delete(int notifId) throws Exception{
//		檢查
		NotifiMes mes = notifiMesDao.findById(notifId)
	            .orElseThrow(() -> new Exception("找不到該筆訊息 ID: " + notifId + "，刪除失敗喵"));
		String title = mes.getTitle();
//		砍子表
		userNotifDao.deleteByNotifId(notifId);
//		砍主表
		notifiMesDao.deleteById(notifId);
		return new BasicRes(ResMessage.SUCCESS.getCode(), 
                "訊息標題:["+title+"]已經被砍光了喵");
	}
	
	

}
