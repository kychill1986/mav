package com.yang.redis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.SortParameters.Order;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.BulkMapper;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.query.SortQueryBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yang.bean.UserBean;
import com.yang.reidis.RetwisRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class UserNewTest {

	@Resource
	private RetwisRepository retwis;

	@Resource
	private RedisTemplate<String, String> jedisTemplate;
	
	@Resource
	private StringRedisTemplate stringRedisTemplate;

	// @Test
	public void saveUser() {
		for (int i = 0; i < 11; i++) {
			double score = Double.valueOf(1421908495253d);
			UserBean user = new UserBean(i + "", "chill" + i, "pass" + i, score
					+ i);
			retwis.saveUser(i + "", user);

			retwis.saveSortUserId(i + "", score + i);
		}
	}

	// @Test
	public void getZSetId() {
		Set<TypedTuple<String>> tuples = new HashSet<TypedTuple<String>>();
		TypedTuple<String> TypedTuple = null;
		for (int i = 200000; i <= 300000; i++) {
			TypedTuple = new DefaultTypedTuple("a2_" + i, (double) i);
			tuples.add(TypedTuple);
			if (i % 10000 == 0) {
				retwis.getZSetOperations().add("a2", tuples);
				tuples.clear();
			}
		}
	}

//	@Test
	public void getSortSet() {
		SortQuery<String> query = SortQueryBuilder
				.sort("diy:wallpaper:special:follow:user:ad468ef11c4e4c3dad881a1ad366e65e")
				.order(Order.DESC).get("#").limit(0, 9).alphabetical(true)
				.build();
		BulkMapper<String, String> hm = new BulkMapper<String, String>() {
			@Override
			public String mapBulk(List<String> bulk) {
				Map<String, String> map = new LinkedHashMap<String, String>();
				Iterator<String> iterator = bulk.iterator();
				return iterator.next();
			}
		};

		System.out.println(jedisTemplate.sort(query, hm));
	}

	@Test
	public void testPipeline() {
		List<Object> results = stringRedisTemplate
				.executePipelined(new RedisCallback<Object>() {
					public Object doInRedis(RedisConnection connection)
							throws DataAccessException {
						StringRedisConnection stringRedisConn = (StringRedisConnection) connection;
						
						stringRedisConn.zCard("hola:views:1:zset_3");
						stringRedisConn.zCard("hola:views:1:zset_1");
						stringRedisConn.zCard("hola:views:1:zset_2");
						
						return null;
					}
				});
		System.out.println(results);
	}

	// @Test
	public void updateUser() {
		UserBean user = new UserBean();
		user.setPassword("hahahaha111");
		retwis.updateUser("1", user);
	}

}