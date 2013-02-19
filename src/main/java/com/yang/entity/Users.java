package com.yang.entity;

import java.io.Serializable;

public class Users implements Serializable {
	/**   */
	private static final long serialVersionUID = -5873691974645631359L;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private Integer id;
	private String username;
	private String password;
}