package com.oim.common.app.manage;

import com.oim.app.AppContext;
import com.oim.common.app.AbstractFactory;

/**
 * 
 * @author XiaHui
 * @date 2015年3月16日 下午2:24:31
 */
public class ManageFactory extends AbstractFactory {

	public ManageFactory(AppContext appContext) {
		super(appContext);
	}

	public <T> T getManage(Class<? extends Manage> classType) {
		return super.getObject(classType);
	}
}
