package com.oim.common.app.handler;

import com.oim.app.AppContext;
import com.oim.common.app.AbstractFactory;

/**
 * 
 * @author XiaHui
 * @date 2015年3月16日 下午2:24:31
 */
public class SendHandlerFactory extends AbstractFactory {

	public SendHandlerFactory(AppContext appContext) {
		super(appContext);
	}

	@SuppressWarnings("unchecked")
	public <T> T getHandler(Class<? extends SendHandler> classType) {
		return (T) super.getObject(classType);
	}
}
