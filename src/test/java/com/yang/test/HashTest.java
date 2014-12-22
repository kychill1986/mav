package com.yang.test;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.redis.connection.SortParameters.Order;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.query.SortQueryBuilder;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class HashTest {
	
	@Resource
	private RedisTemplate<String, Long> jedisTemplate;
	
	@Test
	public void testHMSet() {
		jedisTemplate.setValueSerializer(new GenericToStringSerializer<Long>(Long.class));
		jedisTemplate.setHashKeySerializer(new GenericToStringSerializer<String>(String.class));
		jedisTemplate.setHashValueSerializer(new GenericToStringSerializer<String>(String.class));
		
		SortQuery<String> sort = SortQueryBuilder.sort("uid").by("user_info_*->level").limit(0, -1).order(Order.DESC).build();
		List<Long> uidList = jedisTemplate.sort(sort);
		HashOperations<String, String, String> hashOperations = null;
		for (Long uid : uidList) {
			hashOperations = jedisTemplate.opsForHash();
//			System.out.println(hashOperations.entries("user_info_" + uid));
			System.out.println("uid----->>" + uid + ", Name===" + hashOperations.get("user_info_" + uid, "name"));
			System.out.println("uid----->>" + uid + ", Level===" + hashOperations.get("user_info_" + uid, "level"));
		}
	}
	
//	@Test
	public void testHash() {
		jedisTemplate.setHashKeySerializer(new GenericToStringSerializer<String>(String.class));
		jedisTemplate.setHashValueSerializer(new GenericToStringSerializer<String>(String.class));
		
		HashOperations<String, String, String> hashOperations = jedisTemplate.opsForHash();
		hashOperations.entries("user_info_1");
	}
}
