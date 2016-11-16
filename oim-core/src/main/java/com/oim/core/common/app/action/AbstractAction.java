package com.oim.core.common.app.action;

import com.oim.core.app.AppContext;
import com.oim.core.common.app.base.Module;
import com.oim.core.common.app.service.Service;

/**
 * @description:
 * @author XiaHui
 * @date 2014年7月2日 下午12:01:19
 * @version 1.0.0
 */
public abstract class AbstractAction extends Module {

	public AbstractAction(AppContext appContext) {
		super(appContext);
	}

	public <T> T getService(Class<? extends Service> classType) {
		return appContext.getService(classType);
	}

}
