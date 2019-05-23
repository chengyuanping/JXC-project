package com.mf.service;

import com.mf.entity.User;

public interface UserService {
	
	public User findByUserName(String userName);
}
