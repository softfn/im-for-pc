package com.im.common.converter;

import java.nio.charset.Charset;

import org.springframework.http.converter.StringHttpMessageConverter;

/**
 * 描述：
 * 
 * @author 夏辉
 * @date 2014年4月2日 下午12:08:53 version 0.0.1
 */
public class OnlyStringHttpMessageConverter extends StringHttpMessageConverter {

	public static final Charset UTF8 = Charset.forName("UTF-8");

	public OnlyStringHttpMessageConverter() {
		super(UTF8);
	}

	public OnlyStringHttpMessageConverter(Charset defaultCharset) {
		super(defaultCharset);
	}

}
