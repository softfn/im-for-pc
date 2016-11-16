package com.oim.core.business.service;

import java.util.List;

import com.oim.core.app.AppContext;
import com.oim.core.bean.UserCategory;
import com.oim.core.bean.UserCategoryMember;
import com.oim.core.business.manage.LastManage;
import com.oim.core.business.manage.ListManage;
import com.oim.core.business.manage.PromptManage;
import com.oim.core.common.app.service.Service;
import com.oim.core.common.sound.SoundHandler;
import com.oim.core.net.message.data.UserData;

/**
 * 描述：
 * 
 * @author 夏辉
 * @date 2014年3月31日 上午11:45:15 version 0.0.1
 */
public class UserService extends Service {

	public UserService(AppContext appContext) {
		super(appContext);
	}

	public void addUserCategory(UserCategory userCategory) {
		ListManage listManage = this.appContext.getManage(ListManage.class);
		listManage.addUserCategory(userCategory);
	}

	public void setUserCategoryWithUserList(List<UserCategory> userCategoryList, List<UserData> userDataList, List<UserCategoryMember> userCategoryMemberList) {
		ListManage listManage = this.appContext.getManage(ListManage.class);
		listManage.setUserCategoryWithUserList(userCategoryList, userDataList, userCategoryMemberList);
	}

	/***
	 * 更新用户信息
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param userData
	 */
	public void updateUserData(UserData userData) {
		ListManage listManage = this.appContext.getManage(ListManage.class);
		listManage.updateUserData(userData);
		LastManage lastManage = this.appContext.getManage(LastManage.class);
		lastManage.updateLastUserData(userData);
		if (UserData.status_online.equals(userData.getStatus())) {// 如果用户是上线信息，那么播放好友上线提醒声音
			PromptManage pm = this.appContext.getManage(PromptManage.class);
			pm.playSound(SoundHandler.sound_type_status);
		}
	}
	
	public void updateUserStatus(String userId,String status) {
		
	}
}
