package com.mf.service.impl;

import org.springframework.stereotype.Service;

import com.mf.entity.User;
import com.mf.repository.UserRepository;
import com.mf.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	public UserRepository userRepository;
	@Override
	public User findByUserName(String userName) {
		
		return userRepository.findByUserName(userName);
	}
		
}
