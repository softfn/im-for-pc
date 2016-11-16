package com.oim.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**属性的注解，表示是否验证此标注属性
 * @author XiaHui
 * @date 2014年12月12日 下午4:09:11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FieldVerify {
	int index();
}
