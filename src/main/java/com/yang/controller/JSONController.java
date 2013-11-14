package com.yang.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yang.entity.Users;
import com.yang.service.UserService;

@Controller
@RequestMapping("/json")
public class JSONController {

	@Resource
	private UserService userService;

	@RequestMapping(method = RequestMethod.GET, value = "/getAllUser")
	/**只是原方不动的输出对象*/
	// @ResponseBody
	/**要使用“http://localhost:8080/mav/json/getAllUser.xml”形式输出xml，注解必须是@ModelAttribute；“http://localhost:8080/mav/json/getAllUser.json”，输出json对象*/
	@ModelAttribute
	public List<Users> getAllUser() {
		List<Users> list = userService.getAllUser();
		return list;
	}
}