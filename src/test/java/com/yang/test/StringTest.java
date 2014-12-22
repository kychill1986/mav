package com.yang.test;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.redis.connection.SortParameters.Order;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.query.SortQueryBuilder;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class StringTest {

	@Resource
	private RedisTemplate<String, String> jedisTemplate;

	// @Test
	public void testGetListFromRedisWithSort() {
		jedisTemplate.setValueSerializer(new GenericToStringSerializer<Long>(Long.class));
		SortQuery<String> sort = SortQueryBuilder.sort("user:test").build();
		System.out.println(jedisTemplate.sort(sort));
	}

	// @Test
	public void testGetFromRedisWithSort() {
		jedisTemplate.setValueSerializer(new GenericToStringSerializer<String>(String.class));
		SortQuery<String> sort = SortQueryBuilder.sort("uid").limit(0, -1).order(Order.DESC).build();
		List<String> uidList = jedisTemplate.sort(sort);
		ValueOperations<String, String> valueOperations = null;
		for (String uid : uidList) {
			valueOperations = jedisTemplate.opsForValue();
			System.out.println("uid----->>" + uid + "===" + valueOperations.get("user_name_" + uid));
		}
	}

	//	@Test
	public void testGetFromRedisWithBy() {
		jedisTemplate.setValueSerializer(new GenericToStringSerializer<String>(String.class));
		SortQuery<String> sort = SortQueryBuilder.sort("uid").by("user_level_*").limit(0, -1).order(Order.DESC).build();
		List<String> uidList = jedisTemplate.sort(sort);
		ValueOperations<String, String> valueOperations = null;
		for (String uid : uidList) {
			valueOperations = jedisTemplate.opsForValue();
			System.out.println("uid----->>" + uid + "===" + valueOperations.get("user_name_" + uid));
		}
	}

	@Test
	public void testHMSet() {
		jedisTemplate.setValueSerializer(new GenericToStringSerializer<String>(String.class));
		jedisTemplate.setHashKeySerializer(new GenericToStringSerializer<String>(String.class));
		jedisTemplate.setHashValueSerializer(new GenericToStringSerializer<String>(String.class));
		
		SortQuery<String> sort = SortQueryBuilder.sort("uid").by("user_info_*->level").limit(0, -1).order(Order.DESC).build();
		List<String> uidList = jedisTemplate.sort(sort);
		HashOperations<String, String, String> hashOperations = null;
		for (String uid : uidList) {
			hashOperations = jedisTemplate.opsForHash();
//			System.out.println(hashOperations.entries("user_info_" + uid));
			System.out.println("uid----->>" + uid + ", Name===" + hashOperations.get("user_info_" + uid, "name"));
			System.out.println("uid----->>" + uid + ", Level===" + hashOperations.get("user_info_" + uid, "level"));
		}
	}
	
//	@Test
	public void queryFromHash() {
		jedisTemplate.setHashKeySerializer(new GenericToStringSerializer<String>(String.class));
		jedisTemplate.setHashValueSerializer(new GenericToStringSerializer<String>(String.class));
		
		HashOperations<String, String, String> hashOperations = jedisTemplate.opsForHash();
		System.out.println(hashOperations.values("user_info_1"));
		System.out.println(hashOperations.entries("user_info_1").get("name"));
	}

}
