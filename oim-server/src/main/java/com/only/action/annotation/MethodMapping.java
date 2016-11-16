package com.only.action.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 方法注解
 * @author XiaHui
 * @date 2014年9月15日 下午2:04:12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MethodMapping {

	String value();
	/**
	 * 默认true为拦截方法，需要有权限才能访问，false表示都可以访问
	 * @author: XiaHui
	 * @createDate: 2016年8月25日 下午2:04:13
	 * @update: XiaHui
	 * @updateDate: 2016年8月25日 下午2:04:13
	 */
	boolean isIntercept() default true;//默认拦截
}
