package com.yang.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.query.SortQueryBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yang.entity.Users;
import com.yang.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class UserTest {

	@Resource
	private UserService userService;

	@Resource
	private RedisTemplate<String, Users> jedisTemplate;

	// @Test
	public void testHasMatchUser() {
		System.out.println(userService.getUserInfo());
	}

	/*********************************** Insert ************************************/

	// @Test
	public void testAddToListRedis() {
		ListOperations<String, Users> listOps = jedisTemplate.opsForList();

		Users users4 = new Users();
		users4.setId("4");
		users4.setUsername("List4");
		users4.setPassword("List4");
		listOps.rightPush("user:list", users4);

		Users users = new Users();
		users.setId("1");
		users.setUsername("List1");
		users.setPassword("List1");
		listOps.rightPush("user:list", users);

		Users users3 = new Users();
		users3.setId("3");
		users3.setUsername("List3");
		users3.setPassword("List3");
		listOps.rightPush("user:list", users3);

		Users users2 = new Users();
		users2.setId("2");
		users2.setUsername("List2");
		users2.setPassword("List2");
		listOps.rightPush("user:list", users2);

	}

	// @Test
	public void testAddToSetRedis() {
		SetOperations<String, Users> setOps = jedisTemplate.opsForSet();
		Users users = new Users();
		users.setId("1");
		users.setUsername("Set1");
		users.setPassword("Set1");

		Users users2 = new Users();
		users2.setId("2");
		users2.setUsername("Set2");
		users2.setPassword("Set2");

		setOps.add("user:set1", users, users2);

		Users users3 = new Users();
		users3.setId("3");
		users3.setUsername("Set3");
		users3.setPassword("Set4");

		Users users4 = new Users();
		users4.setId("4");
		users4.setUsername("Set4");
		users4.setPassword("Set4");

		setOps.add("user:set2", users3, users4);
	}

	// @Test
	public void testAddToZSetRedis() {
		ZSetOperations<String, Users> zsetOps = jedisTemplate.opsForZSet();
		Users users = new Users();
		users.setId("1");
		users.setUsername("ZSet1");
		users.setPassword("ZSet1");

		Users users2 = new Users();
		users2.setId("2");
		users2.setUsername("ZSet2");
		users2.setPassword("ZSet2");

		Users users3 = new Users();
		users3.setId("3");
		users3.setUsername("ZSet3");
		users3.setPassword("ZSet3");

		Users users4 = new Users();
		users4.setId("4");
		users4.setUsername("ZSet4");
		users4.setPassword("ZSet4");

		zsetOps.add("user:zset", users, 1);
		zsetOps.add("user:zset", users3, 3);
		zsetOps.add("user:zset", users4, 4);
		zsetOps.add("user:zset", users2, 2);
	}

	// @Test
	public void testAddToHashRedis() {
		HashOperations<String, String, Users> hashOps = jedisTemplate.opsForHash();
		Users users = new Users();
		users.setId("1");
		users.setUsername("Hash1");
		users.setPassword("Hash1");

		Users users2 = new Users();
		users2.setId("2");
		users2.setUsername("Hash2");
		users2.setPassword("Hash2");

		Map<String, Users> map = new HashMap<String, Users>();
		map.put(users.getId(), users);
		map.put(users2.getId(), users2);

		hashOps.putAll("user:hash", map);
	}

	/*********************************** Get ************************************/

	// @Test
	public void testGetListFromRedisWithPage() {
		ListOperations<String, Users> listOps = jedisTemplate.opsForList();
		List<Users> users = listOps.range("user:list", 0, listOps.size("user:list") - 1);
		for (Users users2 : users) {
			System.out.println("----------->>" + users2.getUsername());
		}
	}
	
	@Test
	public void testGetListFromRedisWithSort() {
		SortQuery<String> sort = SortQueryBuilder.sort("user:list").alphabetical(true).build();
		System.out.println(jedisTemplate.sort(sort));
	}

	// @Test
	public void testGetListOneFromRedis() {
		ListOperations<String, Users> listOps = jedisTemplate.opsForList();
		Users users = new Users();
		users.setId("1");
		users.setUsername("List1a");
		users.setPassword("List1a");
		listOps.set("user:list", 0, users);

		System.out.println(listOps.index("user:list", 0));
	}

	// @Test
	public void testGetSetFromRedis() {
		SetOperations<String, Users> setOps = jedisTemplate.opsForSet();
		Set<Users> users = setOps.intersect("user:set1", "user:set2");
		System.out.println(users);
		for (Users users2 : users) {
			System.out.println("----------->>" + users2.getUsername());
		}
	}

	// @Test
	public void testGetZSetFromRedis() {
		ZSetOperations<String, Users> zsetOps = jedisTemplate.opsForZSet();
		Set<Users> users = zsetOps.rangeByScore("user:zset", 0, zsetOps.size("user:zset"));
		System.out.println(users);
		for (Users users2 : users) {
			System.out.println("----------->>" + users2.getUsername());
		}
	}

	// @Test
	public void testGetHashFromRedis() {
		HashOperations<String, String, Users> hashOps = jedisTemplate.opsForHash();
		System.out.println(hashOps.values("user:hash"));
	}

}