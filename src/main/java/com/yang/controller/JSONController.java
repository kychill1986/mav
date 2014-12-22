package com.yang.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yang.entity.Users;
import com.yang.service.UserService;

@Controller
@RequestMapping("/json")
public class JSONController {

	@Resource
	private UserService userService;

	@RequestMapping(method = RequestMethod.GET, value = "/getAllUser")
	@ResponseBody
	@ModelAttribute
	public List<Users> getAllUser() {
		List<Users> list = userService.getAllUser();
		return list;
	}
}