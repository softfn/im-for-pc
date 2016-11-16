package com.oim.core.common.app.base;

import com.oim.core.app.AppContext;

/**
 * @author: XiaHui
 * @date: 2016年9月28日 下午3:35:40
 */
public abstract class Module {
	
	protected AppContext appContext;

	public Module(AppContext appContext) {
		this.appContext = appContext;
	}
}
