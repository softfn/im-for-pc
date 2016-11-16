package com.oim.core.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * action类的注解
 * @author XiaHui
 * @date 2014年9月15日 下午2:03:19
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ActionMapping {

	String value();//mapping映射值
}
