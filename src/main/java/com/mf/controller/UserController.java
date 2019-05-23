package com.mf.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mf.entity.User;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@RequestMapping("/login")
	public Map<String,Object> login(User user,HttpSession session){
		//使用shiro技术
		HashMap<String, Object> map = new HashMap<String,Object>();
		//使用shiro里面的方法拿到subject对象
		Subject subject = SecurityUtils.getSubject();
		//用token封装用户和密码
		UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(),user.getPassword());
		//开始登录
		//调用login方法跳到MyRealm身份认证并传递token
		subject.login(token);
		return null;
	}
}
