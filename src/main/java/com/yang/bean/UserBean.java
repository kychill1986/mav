package com.yang.bean;

import java.io.Serializable;

public class UserBean implements Serializable {

	private static final long serialVersionUID = -5873691974645631359L;

	private String id;

	private String username;

	private String password;

	private Double score;

	public UserBean() {
	}

	public UserBean(String id, String username, String password, Double score) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.score = score;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

}