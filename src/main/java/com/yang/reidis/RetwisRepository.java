package com.yang.reidis;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.hash.DecoratingStringHashMapper;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.JacksonHashMapper;
import org.springframework.data.redis.support.collections.DefaultRedisMap;
import org.springframework.data.redis.support.collections.DefaultRedisSet;
import org.springframework.data.redis.support.collections.DefaultRedisZSet;
import org.springframework.data.redis.support.collections.RedisMap;
import org.springframework.data.redis.support.collections.RedisSet;
import org.springframework.data.redis.support.collections.RedisZSet;
import org.springframework.stereotype.Component;

import com.yang.bean.UserBean;

@Component
public class RetwisRepository {

	@Resource
	private RedisTemplate<String, String> jedisTemplate;

	private final HashMapper<UserBean, String, String> userMapper = new DecoratingStringHashMapper<UserBean>(new JacksonHashMapper<UserBean>(UserBean.class));

	public void saveUserId(String uid) {
		userIdSet().add(uid);
	}

	public void saveSortUserId(String uid, Double score) {
		userIdZSet().add(uid, score);
	}

	public void saveUser(String uid, UserBean user) {
		if (user != null) {
			userInfo(uid).putAll(userMapper.toHash(user));
		}
	}

	public void updateUser(String uid, UserBean user) {
		if (user == null) {
			return;
		}
		if (user.getPassword() != null) {
			userInfo(uid).replace("password", user.getPassword());
		}
	}

	private RedisSet<String> userIdSet() {
		return new DefaultRedisSet<String>(KeyUtils.userIdSet(), jedisTemplate);
	}

	public RedisZSet<String> userIdZSet() {
		return new DefaultRedisZSet<String>(KeyUtils.userIdZSet(), jedisTemplate);
	}

	private RedisMap<String, Object> userInfo(String uid) {
		return new DefaultRedisMap<String, Object>(KeyUtils.userInfo(uid), jedisTemplate);
	}
	
	
}