package com.yang.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.yang.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Resource
	private UserService userService;

	@RequestMapping(value = "/list")
	public ModelAndView getUserList() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("user/list");
		mav.addObject("list", userService.getAllUser());
		// mav.addObject("list",userService.getUserInfo());
		return mav;
	}

}