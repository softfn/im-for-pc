package com.oim.core.business.action;

import java.util.List;

import com.oim.core.app.AppContext;
import com.oim.core.bean.UserCategory;
import com.oim.core.bean.UserCategoryMember;
import com.oim.core.business.service.UserService;
import com.oim.core.common.annotation.ActionMapping;
import com.oim.core.common.annotation.MethodMapping;
import com.oim.core.common.annotation.Parameter;
import com.oim.core.common.app.action.AbstractAction;
import com.oim.core.common.box.UserDataBox;
import com.oim.core.net.message.data.UserData;

/**
 * 描述：
 * 
 * @author 夏辉
 * @date 2014年6月14日 下午9:31:55
 * @version 0.0.1
 */

@ActionMapping(value = "101")
public class UserAction extends AbstractAction {

	public UserAction(AppContext appContext) {
		super(appContext);
	}

	/***
	 * 接受服务器传来好友列表信息
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 */
	@MethodMapping(value = "0004")
	public void setUserCategoryWithUserList(
			@Parameter("userCategoryList") List<UserCategory> userCategoryList,
			@Parameter("userDataList") List<UserData> userDataList,
			@Parameter("userCategoryMemberList") List<UserCategoryMember> userCategoryMemberList) {
		UserService userService = appContext.getService(UserService.class);
		userService.setUserCategoryWithUserList(userCategoryList, userDataList, userCategoryMemberList);
	}

	/***
	 * 接受用户信息更新
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 */
	@MethodMapping(value = "0008")
	public void updateUserStatus(@Parameter("userId")String userId,@Parameter("status")String status) {
		UserData userData=UserDataBox.get(userId);
		if(null!=userData){
			userData.setStatus(status);
			UserService userService = appContext.getService(UserService.class);
			userService.updateUserData(userData);
		}
	}
	
	@MethodMapping(value = "0009")
	public void updateUser(@Parameter("userId")String userId) {
	}
	
	@MethodMapping(value = "0010")
	public void updateUser(@Parameter("userData")UserData userData) {
	}
}
