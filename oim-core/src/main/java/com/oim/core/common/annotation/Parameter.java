package com.oim.core.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Parameter {
	
	String value() default "";
	
	String type() default "json";
	
	public static final String TYPE_JSON="json";
	public static final String TYPE_BEAN="bean";
}
