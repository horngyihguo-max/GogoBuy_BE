package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.request.StoresReq;
import com.example.demo.response.BasicRes;
import com.example.demo.service.StoreService;

import jakarta.validation.Valid;

@CrossOrigin
@RestController
public class StoresController {

	@Autowired
	private StoreService storeService;

	@PostMapping("gogobuy/store/create")
	public BasicRes create(@Valid @RequestBody StoresReq req) {
	    try {
	        return storeService.create(req);
	    } catch (Exception e) {
	        // 捕捉 Service 丟出的 throw new Exception("...")
	        // 回傳自定義的錯誤代碼（例如 400）與 Exception 的 Message
	        return new BasicRes(400, e.getMessage());
	    }
	}
	
	@PostMapping("gogobuy/store/update")
	public BasicRes update(@RequestParam(name = "id") int storeId, @Valid @RequestBody StoresReq req) {
		try {
			return storeService.update(storeId, req);
		} catch (Exception e) {
			// 捕捉 Service 層拋出的驗證失敗或 SQL 錯誤
			return new BasicRes(400, e.getMessage());
		}
	}
	
//	軟刪
	@PostMapping("gogobuy/store/delete")
	public BasicRes delete(@RequestParam(name = "id") int storeId) {
		try {
			return storeService.deleteStore(storeId);
		} catch (Exception e) {
			return new BasicRes(400, "刪除失敗: " + e.getMessage());
		}
	}
	
//	全刪
	@PostMapping("gogobuy/store/fulldelete")
	public BasicRes deleteFull(@RequestParam(name = "id") int storeId) {
		try {
			return storeService.deleteFullStore(storeId);
		} catch (Exception e) {
			return new BasicRes(400, "刪除失敗: " + e.getMessage());
		}
	}
	
//	名字模糊搜尋店家
	@GetMapping("gogobuy/store/searchName")
    public BasicRes searchStores(@RequestParam(name = "name",required = false) String name) {
        return storeService.getStoresByName(name);
    }
	
//	取得指定店家資訊(開團)
	@GetMapping("gogobuy/store/searchId")
    public BasicRes getStoreById(@RequestParam(name = "id") int id) {
        return storeService.getStoreById(id);
    }
	
	
	
}
