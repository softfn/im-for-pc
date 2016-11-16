package com.oim.business.manage;

import com.oim.app.AppContext;
import com.oim.bean.User;
import com.oim.business.handler.GroupHandler;
import com.oim.business.handler.UserHandler;
import com.oim.common.app.manage.Manage;
import com.oim.ui.view.MainView;
import com.oim.ui.view.TrayView;

/**
 * 程序信息相关的管理
 * @author XiaHui
 * @date 2015年3月16日 下午1:37:57
 */
public class AppManage extends Manage {

	public AppManage(AppContext appContext) {
		super(appContext);
	}

	public void initApp(User user) {
		
		appContext.setLogin(true);

		MainView mainView = this.getSingleView(MainView.class);
		mainView.setUser(user);
		mainView.setVisible(true);

		TrayView trayView = this.getSingleView(TrayView.class);
		trayView.showAllMenu(true);
		
		UserHandler uh = appContext.getHandler(UserHandler.class);
		uh.getUserCategoryWithUserList();
		
		GroupHandler gh = appContext.getHandler(GroupHandler.class);
		gh.getGroupCategoryWithGroupList();
	}
}
