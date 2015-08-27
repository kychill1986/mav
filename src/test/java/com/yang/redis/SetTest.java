package com.yang.redis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class SetTest {

	@Resource
	private RedisTemplate<String, String> jedisTemplate;

	@Test
	public void oper() {
		// ģ��ͻ��������id
		Integer[] idArray = { 1, 2, 3, 77 };

		Map<String, Integer> codeAndShowCount = new HashMap<String, Integer>();
		Map<String, Set<String>> codeAndId = new HashMap<String, Set<String>>();

		Integer showCount = 0;
		Set<String> idSet = null;
		Set<String> codeSet = null;

		String maxCode = "";
		Integer maxCount = 0;
		// ѭ��ȡ���ͻ��������id
		for (Integer id : idArray) {
			// id�µ�tagcode
			codeSet = getSetOperations().members("set_" + id);
			for (String code : codeSet) {
				// ����
				showCount = codeAndShowCount.get(code);
				showCount = showCount == null ? 0 : showCount;
				if (showCount != 0) {
					codeAndShowCount.put(code, ++showCount);
				} else {
					codeAndShowCount.put(code, 1);
				}

				if (maxCode == null || (!code.equals(maxCode) && maxCount < showCount)) {
					maxCode = code;
					maxCount = showCount;
				}

				// ���ɿͻ��������id��tagcode�£�����������
				idSet = codeAndId.get(code);
				if (idSet == null) {
					idSet = new HashSet<String>();
				}
				idSet.add(id + "");
				codeAndId.put(code, idSet);
			}
		}

		System.out.println(codeAndShowCount);
		// �ͻ��������id��������Щtagcode
		System.out.println(codeAndId);
		System.out.println("���ִ�������code��" + maxCode + "������Ϊ��" + maxCount);
	}

	private SetOperations<String, String> getSetOperations() {
		return jedisTemplate.opsForSet();
	}

}