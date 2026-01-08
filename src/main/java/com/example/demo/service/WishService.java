package com.example.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.constants.ResMessage;
import com.example.demo.dao.WishDao;
import com.example.demo.request.WishReq;
import com.example.demo.response.BasicRes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
public class WishService {
	
	@Autowired
	private WishDao wishDao;
	
	@Transactional(rollbackOn = Exception.class)
	public BasicRes addWish(WishReq req) {
		int times=wishDao.getTimes(req.getUserId());
		if(times<=0) {
			return new BasicRes(ResMessage.OUT_OF_TIMES_REMAINING.getCode(), ResMessage.OUT_OF_TIMES_REMAINING.getMessage());
		}
		try {
			wishDao.addWish(req.getUserId(), req.getTitle(), req.isAnonymous(), null, false, req.getType(), req.getLocation());
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
		//許願者不可跟願
		String wishUser=String.valueOf(data[0]);
		if(userId.equals(wishUser)) {
			return new BasicRes(ResMessage.WISH_USER_CAN_NOT_FOLLOW.getCode(), ResMessage.WISH_USER_CAN_NOT_FOLLOW.getMessage());
		}
		//字串轉陣列
		Object rawFollowers = data[1];  //目前為字串"null"
		List<String> followers = new ArrayList<>();
		if (rawFollowers != null) {
		    String followerStr = String.valueOf(rawFollowers);
		    // 確保不是 "null" 字串且格式正確 {}
		    if (!followerStr.equalsIgnoreCase("null") && followerStr.length() > 2) {
		        // 去大括號
		        String content = followerStr.substring(1, followerStr.length() - 1);
		        // 過濾掉空字串，避免 split 產生空元素
		        String[] splitData = content.split(",");
		        for (String s : splitData) {
		            if (s != null && !s.trim().isEmpty()) {
		                followers.add(s.trim());
		            }
		        }
		    }
		}
		// 檢查是否已經跟隨過
		if (followers.contains(userId)) {
			followers.remove(userId);
		}else {
			followers.add(userId);
		}

	    // 轉回字串存回 DB
	    String newFollowersStr = "{" + String.join(",", followers) + "}";
		wishDao.setfollowers(id, newFollowersStr);
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}
	
	public BasicRes delWish(int id, String userId) {
		int result=wishDao.delWish(id, userId);
		if(result<=0) {
			return new BasicRes(ResMessage.WISH_DELETE_ERROR.getCode(), ResMessage.WISH_DELETE_ERROR.getMessage());
		}
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}
}
