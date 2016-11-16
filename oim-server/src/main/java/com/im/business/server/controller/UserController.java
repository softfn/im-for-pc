package com.im.business.server.controller;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.im.common.web.ResultData;
import com.im.bean.User;
import com.im.business.common.service.PersonalService;
import com.only.parameter.annotation.Parameter;

@Controller
@RequestMapping("/user")
public class UserController {

	protected final Logger logger = LogManager.getLogger(this.getClass());

	@Resource
	private PersonalService personalService;

	@ResponseBody
	@RequestMapping("/register.do")
	public Object register(@Parameter("user") User user) {
		ResultData result = new ResultData();
		try {
			personalService.register(user);
			result.put("user", user);
			result.setCode(ResultData.code_success);
		} catch (Exception e) {
			result.setCode(ResultData.code_fail);
		}
		return result;
	}
}
