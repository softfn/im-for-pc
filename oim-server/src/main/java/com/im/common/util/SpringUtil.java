package com.im.common.util;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;

/**
 * @author Only
 * @date 2016年5月19日 上午11:56:33
 */
public class SpringUtil {

	private static ApplicationContext applicationContext = null;

	@Resource
	public void setApplicationContext(ApplicationContext applicationContext) {
		SpringUtil.applicationContext = applicationContext;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		Object o = null;
		if (null != applicationContext) {
			o = applicationContext.getBean(name);
		}
		return (T) o;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<?> type) {
		Object o = null;
		if (null != applicationContext) {
			o = applicationContext.getBean(type);
		}
		return (T) o;
	}
}
