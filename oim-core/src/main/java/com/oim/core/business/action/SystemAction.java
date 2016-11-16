package com.oim.core.business.action;

import com.oim.core.app.AppContext;
import com.oim.core.common.annotation.ActionMapping;
import com.oim.core.common.annotation.MethodMapping;
import com.oim.core.common.app.action.AbstractAction;

/**
 * 描述：
 * 
 * @author 夏辉
 * @date 2014年6月14日 下午9:31:55
 * @version 0.0.1
 */
@ActionMapping(value = "000")
public class SystemAction extends AbstractAction {

	public SystemAction(AppContext appContext) {
		super(appContext);
	}

	@MethodMapping(value = "0001")
	public void beat() {
		
	}
}
