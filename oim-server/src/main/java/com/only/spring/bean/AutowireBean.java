package com.only.spring.bean;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;

/**
 * @author: XiaHui
 * @date: 2016年9月29日 上午10:19:19
 */
public class AutowireBean {
	/** 
     * 调用spring注入新创建对象的相关属性(根据属性名进行注入) 
     *  
     * @param bean 
     * @param appContext 
     */  
    public static void autowireBean(Object bean, ApplicationContext appContext) {  
        autowireBean(bean, appContext, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME);  
    }  
  
    /** 
     * 调用spring注入新创建对象的相关属性 
     *  
     * @param bean 
     * @param appContext 
     * @param autowireMode 
     */  
    public static void autowireBean(Object bean, ApplicationContext appContext, int autowireMode) {  
        if (bean == null || appContext == null) {  
            return;  
        }  
  
        AutowireCapableBeanFactory factory = appContext.getAutowireCapableBeanFactory();  
        factory.autowireBeanProperties(bean, autowireMode, false);  
  
        String beanName = ClassUtils.getUserClass(bean).getName();  
        factory.initializeBean(bean, beanName);  
    }  
}
