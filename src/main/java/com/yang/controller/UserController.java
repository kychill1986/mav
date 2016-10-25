package com.yang.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.yang.bean.PageResult;
import com.yang.entity.Users;
import com.yang.util.AjaxHelper;
import com.yang.util.JacksonUtil;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yang.service.UserService;

import java.io.IOException;
import java.util.List;

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

	@RequestMapping(value = "/listWithAjax")
	@ResponseBody
	public PageResult getUserListWithAjax(HttpServletResponse response) {
		List<Users> userList = userService.getAllUser();

		PageResult result = new PageResult();
		//rows 和 total返回结果必须有,不然列表不能正常显示
		result.setTotal(userList.size());
		result.setRows(userList);
		return result;
	}

}