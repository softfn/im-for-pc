package com.oim.business.manage;

import com.oim.app.AppContext;
import com.oim.bean.User;
import com.oim.business.handler.UserHandler;
import com.oim.common.app.manage.Manage;
import com.oim.common.box.PersonalBox;
import com.oim.common.config.ConfigManage;
import com.oim.common.data.UserSaveData;
import com.oim.common.data.UserSaveDataBox;
import com.oim.ui.view.MainView;

/**
 * @author XiaHui
 * @date 2015年3月16日 下午1:37:57
 */
public class PersonalManage extends Manage {

	public PersonalManage(AppContext appContext) {
		super(appContext);
	}

	/***
	 * 登录成功后，初始化个人信息
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param user
	 */
	public void initPersonal(User user) {
		PersonalBox.put(User.class, user);
		UserSaveDataBox usdb = (UserSaveDataBox) ConfigManage.get(UserSaveDataBox.path, UserSaveDataBox.class);
		int size = usdb.getSize();// 获取登录保存的用户账号数量
		if (size > 20) {// 默认只保存20个用户的数量，多于20个，则删除多余的。
			usdb.remove((size - 20));
		}
		UserSaveData usd = new UserSaveData();
		usd.setAccount(user.getAccount());
		usd.setHead(user.getHead());
		usdb.put(user.getAccount(), usd);
		ConfigManage.addOrUpdate(UserSaveDataBox.path, usdb);
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
		UserHandler uh = this.appContext.getHandler(UserHandler.class);
		uh.sendUpdateStatus(status);
		MainView mainView = this.getSingleView(MainView.class);
		mainView.updateStatus(status);
	}
}
