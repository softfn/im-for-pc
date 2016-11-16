package com.oim.business.service;

import java.util.List;

import com.oim.app.AppContext;
import com.oim.bean.Group;
import com.oim.bean.GroupCategory;
import com.oim.bean.GroupCategoryMember;
import com.oim.business.manage.ListManage;
import com.oim.common.app.service.Service;
import com.oim.net.message.data.UserData;

/**
 * 描述：
 * 
 * @author 夏辉
 * @date 2014年3月31日 上午11:45:15 version 0.0.1
 */
public class GroupService extends Service {

	public GroupService(AppContext appContext) {
		super(appContext);
	}

	public void addGroupCategory(GroupCategory groupCategory) {
		ListManage listManage = this.appContext.getManage(ListManage.class);
		listManage.addGroupCategory(groupCategory);
	}

	public void setGroupCategoryWithGroupList(List<GroupCategory> groupCategoryList, List<Group> groupList, List<GroupCategoryMember> groupCategoryMemberList) {
		ListManage listManage = this.appContext.getManage(ListManage.class);
		listManage.setGroupCategoryWithGroupList(groupCategoryList, groupList, groupCategoryMemberList);
	}

	public void updateUserData(UserData userData) {

	}
}
