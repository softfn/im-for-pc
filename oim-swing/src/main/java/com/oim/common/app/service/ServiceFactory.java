package com.oim.common.app.service;

import com.oim.app.AppContext;
import com.oim.common.app.AbstractFactory;

/**
 * 
 * @author XiaHui
 * @date 2015年3月16日 下午2:24:31
 */
public class ServiceFactory extends AbstractFactory {

	public ServiceFactory(AppContext appContext) {
		super(appContext);
	}

	public <T> T getService(Class<? extends Service> classType) {
		return super.getObject(classType);
	}
}
