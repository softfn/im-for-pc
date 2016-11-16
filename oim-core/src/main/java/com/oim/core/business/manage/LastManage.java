package com.oim.core.business.manage;

import com.oim.core.app.AppContext;
import com.oim.core.bean.Group;
import com.oim.core.common.app.manage.Manage;
import com.oim.core.common.app.view.MainView;
import com.oim.core.net.message.data.UserData;

/**
 * 描述：对主界面的记录列表的管理
 * 
 * @author XiaHui
 * @date 2015年4月12日 上午10:18:18
 * @version 0.0.1
 */
public class LastManage extends Manage {

	public LastManage(AppContext appContext) {
		super(appContext);
		initEvent();
	}

	private void initEvent() {
	}

	/**
	 * 将与用户聊天插入历史列表
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param userData
	 */
	public void addLastUserData(UserData userData) {
		MainView mainView = this.appContext.getSingleView(MainView.class);
		mainView.addLastUserData(userData);
	}

	public void updateLastUserData(UserData userData) {
		MainView mainView = this.appContext.getSingleView(MainView.class);
		mainView.updateLastUserData(userData);
	}

	public void addLastGroup(Group group) {
		MainView mainView = this.appContext.getSingleView(MainView.class);
		mainView.addLastGroup(group);
	}

	public void updateLastGroup(Group group) {
		MainView mainView = this.appContext.getSingleView(MainView.class);
		mainView.updateLastGroup(group);
	}

}
