package com.oim.common.app.view;

import com.oim.app.AppContext;
import com.oim.common.app.AbstractFactory;

/**
 * 
 * @author XiaHui
 * @date 2015年3月16日 下午2:24:31
 */
public class ViewFactory extends AbstractFactory {

	public ViewFactory(AppContext appContext) {
		super(appContext);
	}

	public <T> T getSingleView(Class<? extends AbstractView> classType) {
		return super.getObject(classType);
	}
}
