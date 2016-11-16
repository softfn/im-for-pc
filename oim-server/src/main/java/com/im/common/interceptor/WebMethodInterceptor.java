package com.im.common.interceptor;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

/**
 * @Author: XiaHui
 * @Date: 2015年12月25日
 * @ModifyUser: XiaHui
 * @ModifyDate: 2015年12月25日
 */
public class WebMethodInterceptor implements MethodInterceptor {

	@Override
	public Object intercept(Object object, Method method, Object[] array, MethodProxy proxy) throws Throwable {
		System.out.println(object.getClass()+"Method:"+method.getName());
		return null;
	}

}
