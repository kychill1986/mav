package com.yang.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yang.entity.Users;
import com.yang.reidis.RetwisRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class UserNewTest {

	@Resource
	private RetwisRepository retwis;

	@Test
	public void saveUser() {
		String id = "1";
		Users user = new Users();
		user.setId(id);
		user.setUsername("Chill");
		user.setPassword("1111");
		retwis.saveUserId(id);
		retwis.saveUser(id, user);
	}

	@Test
	public void updateUser() {
		Users user = new Users();
		user.setPassword("hahahaha111");
		retwis.updateUser("1", user);
	}

}