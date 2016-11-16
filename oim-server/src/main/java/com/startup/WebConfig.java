package com.startup;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.common.interceptor.SessionInterceptor;
import com.im.common.interceptor.WebContextInterceptor;
import com.only.spring.resolver.ParameterResolver;
import com.only.spring.resolver.RequestResolver;

/**
 *
 * WebConfig WebServlet配置类 spring mvc 配置
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.im", useDefaultFilters = false, includeFilters = { @ComponentScan.Filter(value = { Controller.class }) })
@Import({ WebSecurityConfig.class })
public class WebConfig extends WebMvcConfigurerAdapter {

	@Bean
	public RequestMappingHandlerAdapter requestMappingHandlerAdapter(MappingJackson2HttpMessageConverter messageConverter) {
		List<HandlerMethodArgumentResolver> resolvers = new ArrayList<HandlerMethodArgumentResolver>();
		resolvers.add(requestResolver());
		resolvers.add(parameterResolver());
		RequestMappingHandlerAdapter bean = new RequestMappingHandlerAdapter();
		bean.setSynchronizeOnSession(true);
		bean.setCustomArgumentResolvers(resolvers);
		bean.getMessageConverters().add(messageConverter);
		// bean.setMessageConverters(messageConverters);
		return bean;
	}

	@Bean
	public ParameterResolver parameterResolver() {
		return new ParameterResolver();
	}

	@Bean
	public RequestResolver requestResolver() {
		return new RequestResolver();
	}

	@Bean
	public LocalValidatorFactoryBean localValidatorFactoryBean() {
		return new LocalValidatorFactoryBean();
	}

	/**
	 * 视图层解决方案
	 *
	 * @return
	 */
	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver bean = new InternalResourceViewResolver();
		bean.setPrefix("/WEB-INF/page/");
		bean.setSuffix(".jsp");
		bean.setContentType("text/html;charset=UTF-8");
		return bean;
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.mediaType("json", MediaType.APPLICATION_JSON);
	}

	/**
	 * 国际化处理
	 *
	 * @return
	 */
	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource bean = new ResourceBundleMessageSource();
		bean.setBasename("property.message");
		bean.setCacheSeconds(3000);
		bean.setUseCodeAsDefaultMessage(true);
		return bean;
	}

	/**
	 * json消息转换适配器，用于支持RequestBody、ResponseBody
	 *
	 * @return
	 */
	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
		List<MediaType> mediaTypeList = new ArrayList<MediaType>();
		mediaTypeList.add(MediaType.APPLICATION_JSON);
		mediaTypeList.add(MediaType.APPLICATION_JSON_UTF8);
		MappingJackson2HttpMessageConverter bean = new MappingJackson2HttpMessageConverter();
		bean.setObjectMapper(objectMapper);
		bean.setSupportedMediaTypes(mediaTypeList);
		return bean;
	}

	/**
	 * 附件Multipart解决方案
	 *
	 * @return
	 */
	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver bean = new CommonsMultipartResolver();
		bean.setDefaultEncoding("utf-8");
		bean.setMaxUploadSize(102400000);
		return bean;
	}

	@Bean
	public Jackson2ObjectMapperFactoryBean jackson2ObjectMapperFactoryBean() {
		Jackson2ObjectMapperFactoryBean bean = new Jackson2ObjectMapperFactoryBean();
		bean.setIndentOutput(true);
		bean.setSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return bean;
	}

	/**
	 * 添加拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new WebContextInterceptor());
		InterceptorRegistration sessionRegistration = registry.addInterceptor(new SessionInterceptor());
		sessionRegistration.excludePathPatterns("/static/**", "/html/**", "/common/**");
		sessionRegistration.addPathPatterns("/**");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("/static/");
		registry.addResourceHandler("/common/**").addResourceLocations("/common/");
		registry.addResourceHandler("/html/**").addResourceLocations("/html/");
	}
}
