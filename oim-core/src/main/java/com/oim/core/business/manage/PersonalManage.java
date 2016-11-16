package com.oim.core.business.manage;

import com.oim.core.app.AppContext;
import com.oim.core.business.sender.UserSender;
import com.oim.core.common.app.manage.Manage;
import com.oim.core.common.app.view.MainView;
import com.oim.core.common.box.PersonalBox;
import com.oim.core.net.message.data.LoginData;

/**
 * @author XiaHui
 * @date 2015年3月16日 下午1:37:57
 */
public class PersonalManage extends Manage {

	public PersonalManage(AppContext appContext) {
		super(appContext);
	}


	/**
	 * 更新个人在线状态
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param status
	 */
	public void updateStatus(String status) {
		MainView mainView = this.appContext.getSingleView(MainView.class);
		mainView.setStatus(status);
		LoginData loginData = PersonalBox.get(LoginData.class);
		loginData.setStatus(status);
		UserSender us=appContext.getSender(UserSender.class);
		us.sendUpdateStatus(loginData.getStatus());
	}
}
