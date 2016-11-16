package com.only.spring.resolver;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.only.parameter.annotation.RequestBean;

public class RequestResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(RequestBean.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest request, WebDataBinderFactory binderFactory) throws Exception {

        RequestBean requestBean = parameter.getParameterAnnotation(RequestBean.class);

        if (requestBean != null) {
            String paramName = requestBean.value();
            if ("".equals(paramName) || null == paramName) {
                paramName = parameter.getParameterName();
            }

            Class<?> clazz = parameter.getParameterType();
            Object object = clazz.newInstance();
            HashMap<String, String[]> paramsMap = new HashMap<String, String[]>();
            Iterator<String> i = request.getParameterNames();
            while (i.hasNext()) {
                String webParam = (String) i.next();
                String[] webValue = request.getParameterValues(webParam);
                if (webParam.startsWith(paramName)) {
                    paramsMap.put(webParam, webValue);
                }
            }
            BeanWrapper beanWrapper = new BeanWrapperImpl(object);
            // obj.findCustomEditor(paramClass, paramString)
            beanWrapper.registerCustomEditor(Date.class, null, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));

            for (String propName : paramsMap.keySet()) {
                String[] value = paramsMap.get(propName);
                String[] valueNameArray = propName.split("\\.");
                if (valueNameArray.length == 2) {
                    beanWrapper.setPropertyValue(valueNameArray[1], value);
                } else if (valueNameArray.length == 3) {
                    Object tempObject = beanWrapper.getPropertyValue(valueNameArray[1]);
                    if (tempObject == null) {
                        beanWrapper.setPropertyValue(valueNameArray[1], beanWrapper.getPropertyType(valueNameArray[1]).newInstance());
                    }
                    beanWrapper.setPropertyValue(valueNameArray[1] + "." + valueNameArray[2], value);
                }
            }

            return object;
        } else {
            return new Object();
        }
    }
}
