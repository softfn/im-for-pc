package com.oim.core.common.app.sender;

import com.oim.core.app.AppContext;
import com.oim.core.common.app.AbstractFactory;

/**
 * 
 * @author XiaHui
 * @date 2015年3月16日 下午2:24:31
 */
public class SenderFactory extends AbstractFactory {

	public SenderFactory(AppContext appContext) {
		super(appContext);
	}

	@SuppressWarnings("unchecked")
	public <T> T getSender(Class<? extends Sender> classType) {
		return (T) super.getObject(classType);
	}
}
