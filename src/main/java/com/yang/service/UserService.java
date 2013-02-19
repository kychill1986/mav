package com.yang.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yang.dao.UserDao;

@Service
public class UserService {

	@Resource
	private UserDao userDao;
	
	public void getUserInfo(){
		userDao.getUserInfo();
	}
}
