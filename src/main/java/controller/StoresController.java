package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import request.StoresReq;
import response.BasicRes;
import service.StoreService;

@CrossOrigin
@RestController
public class StoresController {

	@Autowired
	private StoreService storeService;
	
	@PostMapping("stores/create")
	public BasicRes create(@Valid @RequestBody StoresReq req) throws Exception {
		return storeService.create(req);
	}
}
