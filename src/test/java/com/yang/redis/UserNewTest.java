package com.yang.redis;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yang.bean.UserBean;
import com.yang.reidis.RetwisRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class UserNewTest {

	@Resource
	private RetwisRepository retwis;

//	@Test
	public void saveUser() {
		for (int i = 0; i < 11; i++) {
			double score = Double.valueOf(1421908495253d);
			UserBean user = new UserBean(i + "", "chill" + i, "pass" + i, score + i);
			retwis.saveUser(i + "", user);
			
			retwis.saveSortUserId(i + "", score + i);
		}
	}
	
	@Test
	public void getZSetId() {
		System.out.println("----------->>"+retwis.userIdZSet().rangeByScore(1421908495250d, 1421908495256d));
		System.out.println("----------->>"+retwis.userIdZSet().score("0"));
	}

	// @Test
	public void updateUser() {
		UserBean user = new UserBean();
		user.setPassword("hahahaha111");
		retwis.updateUser("1", user);
	}

}