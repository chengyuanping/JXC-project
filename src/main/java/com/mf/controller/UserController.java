package com.mf.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@RequestMapping("/login")
	public Map<String,Object> login(String nameName,String password,HttpSession session){
		//使用shiro技术
		
		return null;
	}
}
