package com.yang.mongo;

import com.yang.mongo.model.User;
import com.yang.mongo.repository.UserDaoImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class MongoTest {

    private static final Log log = LogFactory.getLog(MongoTest.class);

    @Resource
    private UserDaoImpl userDaoImpl;

    private String collectionName = "users";

    @Test
    public void testAdd() {
        List<User> users = new ArrayList<User>();

        int index = 0;
        //添加一百个user
        for (int i = 0; i < 10000000; i++) {
            User user = new User();
            user.setId("" + i);
            user.setAge(i);
            user.setName("zcy" + i);
            user.setPassword("zcy" + i);
            users.add(user);

            index++;
            if(index == 10000){
                userDaoImpl.batchInsert(users, collectionName);
                index = 0;
                users.clear();
            }
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("maxAge", 50);
        List<User> list = userDaoImpl.findAll(params, collectionName);
        System.out.println("user.count()==" + list.size());
    }

}
