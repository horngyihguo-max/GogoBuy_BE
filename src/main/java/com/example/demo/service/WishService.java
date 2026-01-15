package com.example.demo.service;

import java.lang.reflect.Array;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.constants.NotifiCategoryEnum;
import com.example.demo.constants.ResMessage;
import com.example.demo.constants.WishTypeEnum;
import com.example.demo.dao.WishDao;
import com.example.demo.entity.NotifiMes;
import com.example.demo.entity.Wishes;
import com.example.demo.repository.NotifiMesRepository;
import com.example.demo.request.WishReq;
import com.example.demo.response.AllWishRes;
import com.example.demo.response.BasicRes;
import com.example.demo.vo.WishVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
public class WishService {
	
	@Autowired
	private WishDao wishDao;
	@Autowired
	private NotifiMesRepository notifiMsgRepository;
	
	public AllWishRes allWish() {
		List<Wishes> data=wishDao.allWish();
		List<WishVo> returnData=new ArrayList<>();
		for(Wishes w:data) {
			WishVo vo=new WishVo();
			vo.setId(w.getId());
			vo.setUser_id(w.getUser_id());
			vo.setTitle(w.getTitle());
			vo.setType(w.getType());
			vo.setBuildDate(w.getBuildDate());
			vo.setLocation(w.getLocation());
			if(w.isAnonymous()) {
				vo.setNickname(wishDao.getNickname(w.getUser_id()));
			}else {
				vo.setNickname(null);
			}
			if (w.getFollowers() != null && !w.getFollowers().isBlank()) {
	            vo.setFollowers(
	                Arrays.asList(w.getFollowers().split(","))
	            );
	        }else {
	        		vo.setFollowers(Collections.emptyList());
	        }
			returnData.add(vo);
		}
		return new AllWishRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), returnData);
	}
	
	@Transactional(rollbackOn = Exception.class)
	public BasicRes addWish(WishReq req) throws Exception{
		int times=wishDao.getTimes(req.getUserId());
		if(times<=0) {
			return new BasicRes(ResMessage.OUT_OF_TIMES_REMAINING.getCode(), ResMessage.OUT_OF_TIMES_REMAINING.getMessage());
		}
		try {
			if(req.getType() != WishTypeEnum.beverage && 
			    req.getType() != WishTypeEnum.restaurant && 
			    req.getType() != WishTypeEnum.groceries ) {
				return new BasicRes(ResMessage.WISH_TYPE_ERROR.getCode(), ResMessage.WISH_TYPE_ERROR.getMessage());
			}
			wishDao.addWish(req.getUserId(), req.getTitle(), req.isAnonymous(), null, false, req.getType().name(), req.getLocation());
			times=times-1;
			wishDao.setTimes(req.getUserId(), times);
		} catch (Exception e) {
			throw e;
		}
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}
	
	public BasicRes setfollowers(int id, String userId) {
		final String userIdPattern="^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
		if(!userId.matches(userIdPattern)) {
			return new BasicRes(ResMessage.USER_NOT_FOUND.getCode(), ResMessage.USER_NOT_FOUND.getMessage());
		}
		List<Object[]> wishData=wishDao.getfollowers(id);
		if (wishData.isEmpty()) {
	        return new BasicRes(ResMessage.WISH_ID_NOT_FOUND.getCode(), ResMessage.WISH_ID_NOT_FOUND.getMessage());
	    }
		Object[] data=wishData.get(0);
		String wishUser=data[0].toString();
		String followersStr=(data[1] != null) ? data[1].toString() : null;
		boolean deleted=((Number)data[2]).intValue()==1;
		if (deleted) {
	        return new BasicRes(ResMessage.WISH_ID_NOT_FOUND.getCode(), ResMessage.WISH_ID_NOT_FOUND.getMessage());
	    }
		//許願者不可跟願
		if(userId.equals(wishUser)) {
			return new BasicRes(ResMessage.WISH_USER_CAN_NOT_FOLLOW.getCode(), ResMessage.WISH_USER_CAN_NOT_FOLLOW.getMessage());
		}
		//物件轉陣列
		List<String> followersList=new ArrayList<>();
		if(followersStr != null && !followersStr.trim().isEmpty()) {
			String[] splitData = followersStr.split(",");  //過濾字串
	        for (String s : splitData) {
	            if (s != null && !s.trim().isEmpty()) {
	                followersList.add(s.trim());
	            }
	        }
		}
		// 檢查是否已經跟隨過
		if (followersList.contains(userId)) {
			followersList.remove(userId);
		}else {
			followersList.add(userId);
		}

	    // 轉回字串存回 DB
		String saveStr = String.join(",", followersList);
		wishDao.setfollowers(id, saveStr);
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}
	
	public BasicRes delWish(int id, String userId) {
		List<Object[]> wishData=wishDao.getfollowers(id);
		if (wishData.isEmpty()) {
	        return new BasicRes(ResMessage.WISH_ID_NOT_FOUND.getCode(), ResMessage.WISH_ID_NOT_FOUND.getMessage());
	    }
		Object[] data=wishData.get(0);
		String wishUser=data[0].toString();
		List<String> followersList = (data[1] != null) 
			    ? new ArrayList<>(Arrays.asList(((String) data[1]).split(","))) 
			    : new ArrayList<>();
		int result=wishDao.delWish(id, userId);
		
		NotifiCategoryEnum wish=NotifiCategoryEnum.WISH;
		NotifiMes msg = new NotifiMes();
		msg.setCategory(wish);
		msg.setTitle("願望被刪除!!");
		msg.setContent("願望的主人把願望刪掉了，請重新許願");
		msg.setTargetUrl("http://localhost:4200/expired_wishes/wishId="+id);
		msg.setUserId(userId);
		msg.setEventId(id);
		notifiMsgRepository.save(msg);  // 新增訊息
		wishDao.addRecipientsBatch(msg.getId(), followersList);
		if(result<=0) {
			return new BasicRes(ResMessage.WISH_DELETE_ERROR.getCode(), ResMessage.WISH_DELETE_ERROR.getMessage());
		}
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}
	
	public void wishTimesReset() {
		wishDao.wishTimesReset(0, 499, 3);
		wishDao.wishTimesReset(500, 999, 5);
	}
	
	@Transactional(rollbackOn = Exception.class)
	public BasicRes wishOverThreeMonth() throws Exception{
		List<Wishes> wishesData=wishDao.checkOverTime();
		if(wishesData.size()<=0) {
			return null;
		}
		try {
			int wishAmount=wishDao.delOverTime();
			if(wishAmount!=wishesData.size()) {
				throw new RuntimeException("刪除數量不符");
			}
			NotifiCategoryEnum wish=NotifiCategoryEnum.WISH;
			NotifiMes msg = new NotifiMes();
			msg.setCategory(wish);
			msg.setTitle("願望已超過3個月嘍!!");
			msg.setContent("這個願望已超過3個月，願望未成功開團，請重新許願");
			for(Wishes w:wishesData) {
				List<String> wishersList=new ArrayList<>();
				wishersList.add(w.getUser_id());
				msg.setTargetUrl("http://localhost:4200/expired_wishes/wishId="+w.getId());
				msg.setEventId(w.getId());
				notifiMsgRepository.save(msg);  // 新增訊息
				if(w.getFollowers()!=null) {
					wishersList.addAll(Arrays.asList(w.getFollowers().split(",")));
				}
				wishDao.addRecipientsBatch(msg.getId(), wishersList);
			}
			return new BasicRes(ResMessage.SUCCESS.getCode(), //
					ResMessage.SUCCESS.getMessage());
		}catch(Exception e){
			throw e;
		}
	}
}
