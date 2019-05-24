package com.mf.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mf.entity.Menu;
import com.mf.entity.Role;
import com.mf.entity.User;
import com.mf.service.MenuService;
import com.mf.service.RoleService;
import com.mf.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	public UserService userService;
	
	@Autowired
	public RoleService roleService;
	
	@Autowired
	public MenuService menuService;
	
	@ResponseBody
	@RequestMapping("/login")
	public Map<String,Object> login(User user,HttpSession session){
		//使用shiro技术
		Map<String, Object> map = new HashMap<String,Object>();
		//使用shiro里面的方法拿到subject对象
		Subject subject = SecurityUtils.getSubject();		
		//用token封装用户和密码
		UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(),user.getPassword());
		//开始登录
		//调用login方法跳到MyRealm身份认证并传递token
		try{
			
			subject.login(token);
			//根据用户名查询用户
			User currentUser=userService.findByUserName(user.getUserName());
			session.setAttribute("currentUser", currentUser);
			//根据用户id查询用户的角色
			  List<Role> roleList=roleService.findByUserId(currentUser.getId());
			//查询成功用map封装返回一个true,这个用户的角色和这个用户角色的身份个数
			map.put("success", true);
			map.put("roleList", roleList);
			map.put("roleSize",roleList.size());
			
		}catch(Exception e) {
			//登录失败
			e.printStackTrace();
			map.put("success", false);
			map.put("errorInfo", "用户名或密码错误");
		}
		return map;
	}
	
	//保存角色信息
	@ResponseBody
	@RequestMapping("/saveRole")
	public Map<String,Object> saveRole(Integer roleId,HttpSession session) {
		Map<String,Object> 	map=new HashMap<String,Object>();
		Role currentRole=roleService.findById(roleId);
		session.setAttribute("currentRole", currentRole);
		map.put("success", true);
		return map;
	}
	
	
	
	
	public String loadUserInfo(HttpSession session) {
		//获取 用户名 角色
	  User user=(User)session.getAttribute("currentUser");
	  Role role=(Role)session.getAttribute("currentRole");
	  return "欢迎您"+user.getTrueName()+"&nbsp;&nbsp;"+role.getName();
		
		
	}
	
	
	
	//加载权限菜单
	@ResponseBody
	@RequestMapping("/loadMenuInfo")
	public String loadMenuInfo(Integer parentId,HttpSession session) {
		  Role currentrole=(Role)session.getAttribute("currentRole");
		  return getAllMenuByParentId(parentId,currentrole.getId());
		
	}

	//获取所有菜单的信息
	private String getAllMenuByParentId(Integer parentId, Integer id) {
		JsonArray jsonArray= this.getMenuByParentId(parentId,id);
		return null;
	}

	//根据父节点和用户角色id查询菜单
	private JsonArray getMenuByParentId(Integer parentId, Integer roleId) {
		List<Menu> menuList=menuService.findByParentIdAndRoleId(parentId, roleId);
		//list转化json
		JsonArray jsonArray=new JsonArray();
		for(Menu menu:menuList) {
			JsonObject jsonObject=new JsonObject();
			jsonObject.addProperty("id", menu.getId());//节点Id
			jsonObject.addProperty("text", menu.getName());//节点名称
			if(menu.getState()==1) {
				jsonObject.addProperty("state", "closed");//根节点			
			}else {
				jsonObject.addProperty("state", "open");//叶子节点	
			}
			jsonObject.addProperty("iconCls", menu.getIcon());//节点图标
			
			//扩展属性
			JsonObject attributeObject=new JsonObject();
			attributeObject.addProperty("url", menu.getUrl());//菜单请求地址
			
			jsonObject.add("attributes", attributeObject);
			
			jsonArray.add(jsonObject);
			
		}
		
		return jsonArray;
	}
}
