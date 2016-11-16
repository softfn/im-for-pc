package com.oim.core.business.manage;

import com.oim.core.app.AppContext;
import com.oim.core.bean.User;
import com.oim.core.business.sender.GroupSender;
import com.oim.core.business.sender.UserSender;
import com.oim.core.common.app.manage.Manage;
import com.oim.core.common.box.PersonalBox;
import com.oim.core.common.config.ConfigManage;
import com.oim.core.common.data.UserSaveData;
import com.oim.core.common.data.UserSaveDataBox;

/**
 * 程序信息相关的管理
 * 
 * @author XiaHui
 * @date 2015年3月16日 下午1:37:57
 */
public class SystemManage extends Manage {

	public SystemManage(AppContext appContext) {
		super(appContext);
	}

	public void initApp(User user) {
		appContext.setLogin(true);

		UserSender us = appContext.getSender(UserSender.class);
		us.getUserCategoryWithUserList();

		GroupSender gs = appContext.getSender(GroupSender.class);
		gs.getGroupCategoryWithGroupList();

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
}
