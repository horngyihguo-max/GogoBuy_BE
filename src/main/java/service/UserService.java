package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import constants.ResMessage;
import dao.UserDao;
import entity.User;
import request.LoginReq;
import response.BasicRes;


@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	public BasicRes login(LoginReq req) {
		// 1. 檢查輸入是否為空
	    if (!StringUtils.hasText(req.getEmail()) || !StringUtils.hasText(req.getPassword())) {
	        return new BasicRes(ResMessage.LOGIN_ERROR.getCode(), ResMessage.LOGIN_ERROR.getMessage());
	    }
	    
	    // 2. 從資料庫搜尋使用者
	    User user = userDao.getUser(req.getEmail());
	    if (user == null) {
	        return new BasicRes(ResMessage.USER_NOT_FOUND.getCode(), ResMessage.USER_NOT_FOUND.getMessage());
	    }
		return new BasicRes (ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}
	
	
}
