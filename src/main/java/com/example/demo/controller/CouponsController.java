package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.request.CouponReq;
import com.example.demo.response.BasicRes;
import com.example.demo.service.CouponService;

import jakarta.validation.Valid;

@CrossOrigin
@RestController
public class CouponsController {

	@Autowired
	private CouponService couponService;
	
	@PostMapping("gogobuy/coupon/add")
	public BasicRes addCoupon(@Valid @RequestBody CouponReq req) {
		 try {
		        return couponService.createCoupon(req);
		    } catch (Exception e) {
		        // 捕捉 Service 丟出的 throw new Exception("...")
		        // 回傳自定義的錯誤代碼（例如 400）與 Exception 的 Message
		        return new BasicRes(400, e.getMessage());
		    }
	}
	
	@PostMapping("gogobuy/coupon/update")
	public BasicRes updateCoupon(@RequestParam(name = "id") int id , @RequestBody CouponReq req) {
		try {
			return couponService.updateCoupon(id, req);
		} catch (Exception e) {
			// 捕捉 Service 層拋出的驗證失敗或 SQL 錯誤
			return new BasicRes(400, e.getMessage());
		}
	}
	
//	軟刪
	@PostMapping("gogobuy/coupon/delete")
	public BasicRes delete(@RequestParam(name = "id") int id) {
		try {
			return couponService.deleteCoupon(id);
		} catch (Exception e) {
			return new BasicRes(400, "刪除失敗: " + e.getMessage());
		}
	}
	
//	全刪
	@PostMapping("gogobuy/coupon/fulldelete")
	public BasicRes deleteFull(@RequestParam(name = "id") int id) {
		try {
			return couponService.deleteFullCoupon(id);
		} catch (Exception e) {
			return new BasicRes(400, "刪除失敗: " + e.getMessage());
		}
	}
	
	@GetMapping("gogobuy/coupon/searchUser")
	  public BasicRes getCouponById(@RequestParam(name = "user_id") String userId) {
		return couponService.searchCouponByUser(userId);
	}
}
