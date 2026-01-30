package com.example.demo.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.demo.constants.ResMessage;
import com.example.demo.dao.CouponsDao;
import com.example.demo.dao.UserDao;
import com.example.demo.entity.Coupons;
import com.example.demo.entity.User;
import com.example.demo.request.CouponReq;
import com.example.demo.response.BasicRes;
import com.example.demo.response.CouponsRes;

@Service
public class CouponService {

	@Autowired
	private CouponsDao couponsDao;

	@Autowired
	private UserDao userDao;

	// 檢查優惠券用戶
	private void checkCouponUserId(CouponReq req) throws Exception {
		User user = userDao.getUserById(req.getUserId());

		if (user == null) {
			throw new Exception("不存在的用戶");
		}
	};

	// 檢查優惠碼
	private void checkCouponCode(CouponReq req) throws Exception {
		// if (req.getCouponCode() == null || req.getCouponCode().trim().isEmpty()) {
		if (!StringUtils.hasText(req.getCouponCode())) {
			throw new Exception("未輸入優惠碼(或為空)");
		}
	}

	// 檢查門檻金額
	private void checkAmountThreshold(CouponReq req) throws Exception {
		Integer threshold = req.getAmountThreshold();

		if (threshold < 0) {
			throw new Exception("門檻金額錯誤(<0)");
		}
	}

	// 檢查折扣金額上限
	private void checkDiscountMax(CouponReq req) throws Exception {
		Integer max = req.getDiscountMax();

		if (max == null) {
			throw new Exception("折抵上限金額不得為空");
		}

		if (max < 0) {
			throw new Exception("折抵上限金額錯誤(<0)");
		}
	}

	// 檢查折扣金額
	private void checkDiscountValue(CouponReq req) throws Exception {
		String str = req.getDiscountValue();

		if (str == null || str.isEmpty()) {
			throw new Exception("折抵數值不得為空");
		}

		// 檢查折扣是否以%為基準
		if (str.endsWith("%")) {
			try {
				String numberPart = str.substring(0, str.length() - 1);
				double percent = Double.parseDouble(numberPart);

				if (percent <= 0 || percent > 100) {
					throw new Exception("百分比折抵必須介於 1~100 之間");
				}
			} catch (NumberFormatException e) {
				throw new Exception("百分比格式錯誤");
			}
		} else {
			// 固定金額格式
			try {
				int amount = Integer.parseInt(str);

				if (amount < 0) {
					throw new Exception("折抵金額不能小於 0");
				}
			} catch (NumberFormatException e) {
				throw new Exception("數值格式錯誤，請輸入數字或百分比(%)");
			}
		}

	}

	// 檢查優惠券截止日期
	private void checkEndDate(CouponReq req) throws Exception {
		// 轉換String>>DateTime
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate endDate = LocalDate.parse(req.getEndDate(), formatter);

			if (endDate.isBefore(LocalDate.now())) {
				throw new Exception("結束時間不得早於現在時間");
			}
		} catch (DateTimeParseException e) {
			throw new Exception("日期格式錯誤，請使用 yyyy-MM-dd");
		}
	}

	// 檢查優惠券是否存在
	private void checkCouponId(int id) throws Exception {
		Coupons existCoupon = couponsDao.getCouponById(id);
		if (existCoupon == null) {
			throw new Exception("沒找到優惠券");
		}
	}

	// 建立優惠券
	public BasicRes createCoupon(CouponReq req) throws Exception {

		// 呼叫檢查function
		checkCouponUserId(req);
		checkCouponCode(req);
		checkAmountThreshold(req);
		checkDiscountMax(req);
		checkDiscountValue(req);
		checkEndDate(req);

		couponsDao.addCoupon(req.getUserId(), req.getCouponCode(), //
				req.getApplicableStores(), req.getAmountThreshold(), //
				req.getDiscountMax(), req.getDiscountValue(), //
				req.getEndDate());

		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	};

	// 修改優惠券
	public BasicRes updateCoupon(int id, CouponReq req) throws Exception {

		// 呼叫檢查function
		checkCouponId(id);
		checkCouponUserId(req);
		checkCouponCode(req);
		checkAmountThreshold(req);
		checkDiscountMax(req);
		checkDiscountValue(req);
		checkEndDate(req);

		couponsDao.updateCoupon(id, req.getUserId(), //
				req.getCouponCode(), req.getApplicableStores(), //
				req.getAmountThreshold(), req.getDiscountMax(), //
				req.getDiscountValue(), req.getEndDate(), //
				req.isUsed(), req.isDeleted());

		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}

	public BasicRes deleteFullCoupon(int id) throws Exception {

		couponsDao.deleteCouponById(id);

		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}

	// 刪除優惠券
	public BasicRes deleteCoupon(int id) throws Exception {

		checkCouponId(id);

		int result = couponsDao.softDeleteCouponById(id);

		if (result <= 0) {
			throw new Exception("找不到該優惠券或已被刪除 (ID: " + id + ")");
		}

		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}

	// 根據用戶id搜尋對應優惠券
	public CouponsRes searchCouponByUser(String userId) {

		if (userId == null || userId.trim().isEmpty()) {
			return new CouponsRes(400, "User ID 不得為空", null);
		}

		User user = userDao.getUserById(userId);
		if (user == null) {
			return new CouponsRes(ResMessage.USER_NOT_FOUND.getCode(), "此使用者不存在", null);
		}

		List<Coupons> coupons = couponsDao.getCouponByUserId(userId);

		if (coupons == null || coupons.isEmpty()) {
			return new CouponsRes(ResMessage.SUCCESS.getCode(), "該使用者目前沒有任何優惠券", new ArrayList<>());
		}

		return new CouponsRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage(), coupons);
	}
}
