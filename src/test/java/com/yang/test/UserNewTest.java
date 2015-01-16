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
	public void SaveUser() {
		Users user = new Users();
		user.setId("1");
		user.setUsername("Chill");
		user.setPassword("1111");
		retwis.saveUserId("1");
		retwis.saveUser("1", user);
	}
	
	@Test
	public void UpdateUser() {
		Users user = new Users();
		user.setPassword("hahahaha");
		retwis.updateUser("1", user);
	}

}