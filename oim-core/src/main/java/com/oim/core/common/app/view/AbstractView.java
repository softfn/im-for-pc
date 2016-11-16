package com.oim.core.common.app.view;

import com.oim.core.app.AppContext;
import com.oim.core.common.app.base.Module;

/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2015年3月8日 下午3:11:11
 * @version 0.0.1
 */
public abstract class AbstractView extends Module {

	public AbstractView(AppContext appContext) {
		super(appContext);
	}

	public void showPrompt(String text) {

	}
}
