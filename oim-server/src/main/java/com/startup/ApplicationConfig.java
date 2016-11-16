package com.startup;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * ApplicationConfig 相当于Spring的applicationContext.xml
 *
 * @author XiaHui
 * @date 2015年12月22日 下午11:29:27
 * @version 0.0.1
 */
@Configuration
@ComponentScan(basePackages = "com.im", excludeFilters = {@ComponentScan.Filter(value = {Controller.class})})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Import({DataSourceConfig.class, HibernateConfig.class, BeanConfig.class})// 导入其他配置
@EnableTransactionManagement
public class ApplicationConfig {

   
}
