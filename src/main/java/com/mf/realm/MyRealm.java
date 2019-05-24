package com.mf.realm;


import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.mf.entity.User;
import com.mf.repository.UserRepository;

public class MyRealm extends AuthorizingRealm{
	//这个类主要的作用就是身份认证,把前台和后台获得的密码比对一下
	@Autowired
	public UserRepository userRepository;
	
	//身份认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//从token获取用户名  前台收集到的用户名
		String userName = (String) token.getPrincipal();
		//使用用户名查询user  后台收集到的用户
		User user  = userRepository.findByUserName(userName);
		//判断一下是否根据user查询了一个用户,如果没有.这个user就等于null,抛出异常
		if(user!=null) {
			/**这块对比逻辑是先对比username，但是username肯定是相等的，所以真正对比的是password。
			 * 从这里传入的password（这里是从数据库获取的）和token（filter中登录时生成的）中的password做对比，如果相同就允许登录，不相同就抛出异常。
			 */
			AuthenticationInfo authenticationInfo=new SimpleAuthenticationInfo(user.getUserName(),user.getPassword(),this.getName());
			return authenticationInfo;
		}else {
			throw new UnknownAccountException();
		}
	}
		
	//授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
