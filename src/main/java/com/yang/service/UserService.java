package com.yang.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yang.entity.Users;
import com.yang.jdbc.dao.JDBCUserDao;
import com.yang.mybatis.dao.UserDao;

@Service
public class UserService {

	@Resource
	private JDBCUserDao jdbcUserDao;

	@Resource
	private UserDao userDao;

	public List<Users> getUserInfo() {
		return jdbcUserDao.getUserInfo();
	}

	public List<Users> getAllUser() {
		return userDao.getAllUser();
	}
}
