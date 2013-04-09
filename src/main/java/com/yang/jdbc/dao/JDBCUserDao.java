package com.yang.jdbc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.yang.entity.Users;

/**
 * 
 * . <br>
 * <p>
 * 修改历史: 2013-2-20 上午9:29:39 yichao.yang@chipease.com <br>
 * 修改说明: <br>
 * <p>
 * Copyright: Copyright (c) 2013-2-20 上午9:29:39
 * <p>
 * @author yichao.yang@rayhov.com
 * @version 1.0
 */
@Repository
public class JDBCUserDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Users> getUserInfo() {
		String sql = "select username, password from user";
		final List<Users> list = new ArrayList<Users>();
		jdbcTemplate.query(sql, new Object[] {}, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				Users users = new Users();
				users.setUsername(rs.getString("username"));
				users.setPassword(rs.getString("password"));
				list.add(users);
			}
		});
		
		return list;
	}
}
