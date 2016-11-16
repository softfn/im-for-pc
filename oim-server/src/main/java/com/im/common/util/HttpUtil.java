package com.im.common.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author Only
 * @date 2016年5月12日 下午5:34:08
 */
public class HttpUtil {

	public static HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}

	public static HttpServletResponse getResponse() {
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		return response;
	}

	public static HttpSession getSession() {
		HttpSession session = null;
		HttpServletRequest request = getRequest();
		if (null != request) {
			session = request.getSession();
		}
		return session;
	}

	public static String getSessionId() {
		String id = null;
		HttpSession session = getSession();
		if (null != session) {
			id = session.getId();
		}
		return id;
	}
}
