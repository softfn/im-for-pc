package com.im.common.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.im.common.util.JsonUtil;
import com.im.common.web.ResultData;

/**
 * 类描述：异常捕获
 * 
 */
@Component
public class ExceptionResolver implements HandlerExceptionResolver {

	protected final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		StackTraceElement[] array = ex.getStackTrace();
		StringBuilder exception = new StringBuilder();
		if (null != array) {
			for (StackTraceElement stackTraceElement : array) {
				exception.append(stackTraceElement);
				exception.append("\n");
			}
		}
		logger.error(ex.getMessage(), ex);

		ResultData rr = ResultData.createFailMessage(exception.toString());
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.println(JsonUtil.objectToJson(rr));
			writer.flush();
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			if (null != writer) {
				writer.close();
			}
		}
		return null;
	}
}
