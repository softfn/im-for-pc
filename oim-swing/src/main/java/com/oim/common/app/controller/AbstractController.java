package com.oim.common.app.controller;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.oim.app.AppContext;


/**
 * @description:
 * @author XiaHui
 * @date 2014年7月2日 下午12:01:19
 * @version 1.0.0
 */
public abstract class AbstractController {

	protected Map<String, Method> methodMap = new HashMap<String, Method>();
	protected AppContext appContext;

	public AbstractController(AppContext appContext) {
		this.appContext = appContext;
	}
}
