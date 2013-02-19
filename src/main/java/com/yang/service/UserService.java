package com.yang.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yang.dao.UserDao;
import com.yang.entity.Users;

@Service
public class UserService {

	@Resource
	private UserDao userDao;

	public List<Users> getUserInfo() {
		return userDao.getUserInfo();
	}
}
