package cn.ps.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.ps.service.TestService;

@RestController
public class TestController {
	@Autowired
	TestService ts;
	@RequestMapping("/ts")
	public String ts1() {
		return ts.hello("張三");
	}
	
	@RequestMapping("/ts1")
	public Boolean login(String userName,String password) {
		return ts.login(userName, password);
	}
}
