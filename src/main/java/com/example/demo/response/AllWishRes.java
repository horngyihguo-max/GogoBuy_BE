package com.example.demo.response;

import java.time.LocalDate;
import java.util.List;

import com.example.demo.vo.WishVo;

public class AllWishRes extends BasicRes{
	private List<WishVo> allWish;

	public AllWishRes() {
		super();
	}

	public AllWishRes(int code, String message) {
		super(code, message);
	}

	public AllWishRes(int code, String message, List<WishVo> allWish) {
		super(code, message);
		this.allWish = allWish;
	}

	public List<WishVo> getAllWish() {
		return allWish;
	}

	public void setAllWish(List<WishVo> allWish) {
		this.allWish = allWish;
	}
}
