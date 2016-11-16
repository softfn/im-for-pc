package com.oim.business.controller;

import java.util.List;

import com.oim.app.AppContext;
import com.oim.bean.UserCategory;
import com.oim.bean.UserCategoryMember;
import com.oim.business.service.UserService;
import com.oim.common.annotation.ActionMapping;
import com.oim.common.annotation.MethodMapping;
import com.oim.common.annotation.Parameter;
import com.oim.common.app.controller.AbstractController;
import com.oim.common.box.UserDataBox;
import com.oim.net.message.data.UserData;

/**
 * 描述：
 * 
 * @author 夏辉
 * @date 2014年6月14日 下午9:31:55
 * @version 0.0.1
 */

@ActionMapping(value = "101")
public class UserController extends AbstractController {

	public UserController(AppContext appContext) {
		super(appContext);
	}

	/***
	 * 接受服务器传来好友列表信息
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param userMessage
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
	 * @param userMessage
	 */
	@MethodMapping(value = "0008")
	public void updateUserStatus(@Parameter("userId")String userId,@Parameter("status")String status) {
		UserData userData=UserDataBox.get(userId);
		if(null!=userData){
			userData.setStatus(status);
			java.awt.EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					UserService userService = appContext.getService(UserService.class);
					userService.updateUserData(userData);
				}
			});
		}
	}
	
	@MethodMapping(value = "0009")
	public void updateUser(@Parameter("userId")String userId) {
	}
	
	@MethodMapping(value = "0010")
	public void updateUser(@Parameter("userData")UserData userData) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				UserService userService = appContext.getService(UserService.class);
				userService.updateUserData(userData);
			}
		});
	}
}
