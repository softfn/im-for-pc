package com.im.test.spring;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * @author: XiaHui
 * @date: 2016年8月19日 下午4:13:31
 */
public class PropertyPlaceholderConfigurerTest {

	public static void main(String[] args) {
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource resource = resolver.getResource("classpath:config/config.properties");
		PropertyPlaceholderConfigurer ppc=new PropertyPlaceholderConfigurer();
		ppc.setLocation(resource);
		//ppc.setProperties(properties);
	}

}
