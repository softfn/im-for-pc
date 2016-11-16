package com.oim.core.common.app.service;

import com.oim.core.app.AppContext;
import com.oim.core.common.app.base.Module;
import com.oim.core.common.app.manage.Manage;

/**
 * 当接受到服务信息时：数据流向设计为 net->controller->service->view<br>
 * 当用户从ui操作向服务器发送数据为view->controller->handler->net
 * 
 * @author XiaHui
 * @date 2015年3月16日 下午2:17:40
 */
public abstract class Service extends Module{

	public Service(AppContext appContext) {
		super(appContext);
	}

	public <T> T getManage(Class<? extends Manage> classType) {
		return appContext.getManage(classType);
	}
}
