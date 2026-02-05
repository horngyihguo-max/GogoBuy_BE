package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.response.GoogleMapRes;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

@Service
public class GoogleMapService {
	/*
	 * 在 Google Maps SDK 中，所有對外的請求（如轉地址、算距離、找地點）都不能直接跑出去， 它們必須通通交給
	 * GeoApiContext這個物件來處理。
	 */
	@Autowired
	private GeoApiContext geoApiContext;

	// 跟 google 溝通
	public GoogleMapRes googleMapAddress(String address) {
		try {
			// 呼叫 Google Geocoding API 轉換地址
			// SDK 會自動處理 URL 編碼，你直接傳中文地址即可
			// await(): 程式會在這裡停一下，等 Google 的機器人回覆。
			GeocodingResult[] results = GeocodingApi.geocode(geoApiContext, address).await();

			// Google 回傳的結果可能不只一個，我們取第一個最準確的 results[0]。
			// 取出座標：lng (經度) 和 lat (緯度)
			if (results != null && results.length > 0) {
				double lng = results[0].geometry.location.lng;
				double lat = results[0].geometry.location.lat;
				return new GoogleMapRes(200, "成功拿取經緯度", lng, lat);
			} else {
				return new GoogleMapRes(404, "找不到該地址的座標");
			}
		} catch (Exception e) {
			return new GoogleMapRes(500, "系統錯誤：" + e.getMessage(), 0.0, 0.0);
		}
	}
}
