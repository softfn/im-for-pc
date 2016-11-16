package com.im.common.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Author: XiaHui
 * @Date: 2015年12月21日
 * @ModifyUser: XiaHui
 * @ModifyDate: 2015年12月21日
 */
@Component// 声明这是一个切面Bean
@Aspect// @Aspect放在类头上，把这个类作为一个切面，但是这个类一定要显式的注册在Spring容器中。
@Order(1)// 多个aop作用于同一个贴入点时的先后顺序，数值越小越靠前
public class LogAspect {
	
	protected final Logger logger = LogManager.getLogger(this.getClass());
	
	@Pointcut("execution (* com.im.*.action.*.*(..))")
	public void aspect() {
	}

	/*
	 * 配置前置通知,使用在方法aspect()上注册的切入点 同时接受JoinPoint切入点对象,可以没有该参数
	 */
	@Before("aspect()")
	public void before(JoinPoint point) {
	}

	// 配置后置通知,使用在方法aspect()上注册的切入点
	@After("aspect()")
	public void after(JoinPoint point) {
		if(logger.isDebugEnabled()){
			logger.debug(point.getTarget().getClass()+":123");
		}
	}

	// 配置环绕通知,使用在方法aspect()上注册的切入点 决定是否真实执行
	//@Around("aspect()")
	public void around(JoinPoint point) {
	}

	// 配置后置返回通知,使用在方法aspect()上注册的切入点
	@AfterReturning("aspect()")
	public void afterReturn(JoinPoint point) {
	}

	// 配置抛出异常后通知,使用在方法aspect()上注册的切入点
	@AfterThrowing(pointcut = "aspect()", throwing = "ex")
	public void afterThrow(JoinPoint point, Exception ex) {
	}
}
