package com.oim.core.business.action;

import java.util.List;

import com.oim.core.app.AppContext;
import com.oim.core.bean.Group;
import com.oim.core.bean.GroupCategory;
import com.oim.core.bean.GroupCategoryMember;
import com.oim.core.business.manage.ChatManage;
import com.oim.core.business.service.GroupService;
import com.oim.core.common.annotation.ActionMapping;
import com.oim.core.common.annotation.MethodMapping;
import com.oim.core.common.annotation.Parameter;
import com.oim.core.common.app.action.AbstractAction;

/**
 * 描述：
 * 
 * @author 夏辉
 * @date 2014年6月14日 下午9:31:55
 * @version 0.0.1
 */

@ActionMapping(value = "200")
public class GroupAction extends AbstractAction {

	public GroupAction(AppContext appContext) {
		super(appContext);
	}

	/**
	 * 接受到服务器传来的群分组、列表等群信息
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 */
	@MethodMapping(value = "0004")
	public void setGroupCategoryWithGroupList(
			@Parameter("groupCategoryList") List<GroupCategory> groupCategoryList,
			@Parameter("groupList") List<Group> groupList,
			@Parameter("groupCategoryMemberList") List<GroupCategoryMember> groupCategoryMemberList) {
		GroupService groupService=this.getService(GroupService.class);
		groupService.setGroupCategoryWithGroupList(groupCategoryList,groupList,groupCategoryMemberList);
	}

	/**
	 * 接受服务器推送的群信息更新
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 */
	@MethodMapping(value = "0009")
	public void updateGroup(@Parameter("group") Group group) {
		GroupService groupService=this.getService(GroupService.class);
		groupService.updateGroup(group);
	}
	
	@MethodMapping(value = "0011")
	public void updateGroup(@Parameter("groupId") String groupId,@Parameter("userId") String userId) {
		ChatManage chatManage = appContext.getManage(ChatManage.class);
		chatManage.updateGroupUserList(groupId);
	}
}
