package com.oim.core.business.service;

import java.util.List;

import com.oim.core.app.AppContext;
import com.oim.core.bean.Group;
import com.oim.core.bean.GroupCategory;
import com.oim.core.bean.GroupCategoryMember;
import com.oim.core.business.manage.LastManage;
import com.oim.core.business.manage.ListManage;
import com.oim.core.common.app.service.Service;

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

	public void updateGroup(Group group) {
		ListManage listManage = this.appContext.getManage(ListManage.class);
		listManage.updateGroup(group);
		LastManage lastManage = this.appContext.getManage(LastManage.class);
		lastManage.updateLastGroup(group);
	}
}
