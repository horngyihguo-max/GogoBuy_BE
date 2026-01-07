package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import request.LoginReq;
import response.BasicRes;
import service.UserService;

@RestController
public class LoginController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("gogobuy/login")
	public BasicRes login(@Valid @RequestBody LoginReq req) {
		return userService.login(req);
	}
}
