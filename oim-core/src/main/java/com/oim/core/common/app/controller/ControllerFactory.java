package com.oim.core.common.app.controller;

import com.oim.core.app.AppContext;
import com.oim.core.common.app.AbstractFactory;

/**
 * 
 * @author XiaHui
 * @date 2015年3月16日 下午2:24:31
 */
public class ControllerFactory extends AbstractFactory {

	public ControllerFactory(AppContext appContext) {
		super(appContext);
	}

	@SuppressWarnings("unchecked")
	public <T> T getController(Class<? extends BaseController> classType) {
		return (T) super.getObject(classType);
	}
}
