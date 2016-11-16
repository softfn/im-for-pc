package com.im.business.server.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.im.business.server.message.Head;
import com.im.business.server.message.Message;
import com.im.business.server.message.data.PageData;
import com.im.business.server.message.data.UserData;
import com.im.business.server.message.data.query.UserQuery;
import com.im.business.server.mq.MessageQueueWriteHandler;
import com.im.business.server.push.UserPush;
import com.im.bean.UserCategory;
import com.im.bean.UserCategoryMember;
import com.im.business.common.service.UserService;
import com.im.business.common.service.api.UserBaseService;
import com.only.action.annotation.ActionMapping;
import com.only.action.annotation.MethodMapping;
import com.only.net.session.SocketSession;
import com.only.parameter.annotation.Parameter;
import com.only.query.page.DefaultPage;

/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2014年6月14日 下午9:31:55
 * @version 0.0.1
 */
@Controller
@ActionMapping(value = "101")
public class UserAction {

	protected final Logger logger = LogManager.getLogger(this.getClass());

	@Resource
	private UserService userService;
	@Resource
	private MessageQueueWriteHandler messageQueueWriteHandler;
	@Resource
	private UserPush userPush;
	@Resource
	private UserBaseService userBaseService;

	/**
	 * 只获取好友分组
	 * 
	 * @param userMessage
	 * @param dataWrite
	 * @return
	 */
	@MethodMapping(value = "0001")
	public Message getUserCategoryList(SocketSession socketSession) {
		String userId = socketSession.getKey();
		List<UserCategory> userCategoryList = userService.getUserCategoryList(userId);
		Message message = new Message();
		message.put("userDataList", userCategoryList);
		return message;
	}

	/**
	 * 获取好友分组成员信息（不包含用户详情）
	 * 
	 * @param userMessage
	 * @param dataWrite
	 * @return
	 */
	@MethodMapping(value = "0002")
	public Message getUserCategoryMemberUserDataList(SocketSession socketSession) {
		String userId = socketSession.getKey();
		List<UserData> userDataList = userService.getUserCategoryMemberUserDataList(userId);
		userService.setUserStatus(userDataList);
		Message message = new Message();
		message.put("userDataList", userDataList);
		return message;
	}

	@MethodMapping(value = "0003")
	public Message getUserCategoryMemberList(SocketSession socketSession) {
		String userId = socketSession.getKey();
		List<UserCategoryMember> userCategoryMemberList = userService.getUserCategoryMemberList(userId);
		Message message = new Message();
		message.put("userCategoryMemberList", userCategoryMemberList);
		return message;
	}

	@MethodMapping(value = "0004")
	public Message getUserCategoryWithUserList(SocketSession socketSession) {
		String userId = socketSession.getKey();
		List<UserCategory> userCategoryList = userService.getUserCategoryList(userId);
		List<UserData> userDataList = userService.getUserCategoryMemberUserDataList(userId);
		List<UserCategoryMember> userCategoryMemberList = userService.getUserCategoryMemberList(userId);
		userService.setUserStatus(userDataList);
		Message message = new Message();
		message.put("userCategoryList", userCategoryList);
		message.put("userDataList", userDataList);
		message.put("userCategoryMemberList", userCategoryMemberList);
		return message;
	}

	/**
	 * 
	 * @param userMessage
	 * @param dataWrite
	 * @return
	 */
	@MethodMapping(value = "0005")
	public Message getUserDataById(@Parameter("userId") String userId) {
		UserData userData = userService.getUserDataById(userId);
		userService.setUserStatus(userData);
		Message message = new Message();
		message.put("userData", userData);
		return message;
	}

	/**
	 * 条件查询用户
	 * 
	 * @param userMessage
	 * @param dataWrite
	 * @return
	 */
	@MethodMapping(value = "0006")
	public Message queryUserDataList(
			@Parameter("userQuery") UserQuery userQuery,
			@Parameter("page") PageData page) {
		
		DefaultPage defaultPage=new DefaultPage();
		defaultPage.setPageNumber(page.getPageNumber());
		defaultPage.setPageSize(page.getPageSize());
		
		List<UserData> userDataList = userService.queryUserDataList(userQuery, defaultPage);
		userService.setUserStatus(userDataList);
		
		page.setTotalCount(defaultPage.getTotalCount());
		page.setTotalPage(defaultPage.getTotalPage());
		
		Message message = new Message();
		message.put("userDataList", userDataList);
		message.put("page", page);
		return message;
	}
	
	
	
	@MethodMapping(value = "0008")
	public void sendUserStatus(SocketSession socketSession, Head head, @Parameter("status") String status) {
		String userId = socketSession.getKey();
		List<UserCategoryMember> memberList = userService.getInUserCategoryMemberList(userId);
		List<UserData> userDataList = userService.getGroupMemberUserDataListByUserId(userId);

		Map<String, String> map = new HashMap<String, String>();
		for (UserCategoryMember ucm : memberList) {
			map.put(ucm.getOwnUserId(), ucm.getOwnUserId());
		}
		for (UserData ud : userDataList) {
			map.put(ud.getId(), ud.getId());
		}
		List<String> keyList = new ArrayList<String>(map.values());
		userPush.pushUserStatus(head, userId, status, keyList);
		userBaseService.putUserStatus(userId, status);
	}

	@MethodMapping(value = "0009")
	public void sendUserUpdate(SocketSession socketSession, Head head) {
		String userId = socketSession.getKey();
		List<UserCategoryMember> memberList = userService.getInUserCategoryMemberList(userId);
		List<UserData> userDataList = userService.getGroupMemberUserDataListByUserId(userId);

		Map<String, String> map = new HashMap<String, String>();
		for (UserCategoryMember ucm : memberList) {
			map.put(ucm.getOwnUserId(), ucm.getOwnUserId());
		}
		for (UserData ud : userDataList) {
			map.put(ud.getId(), ud.getId());
		}
		List<String> keyList = new ArrayList<String>(map.values());
		userPush.pushUserUpdate(head, userId, keyList);
	}
	
	@MethodMapping(value = "0010")
	public void sendUserDataUpdate(SocketSession socketSession, Head head) {
		String userId = socketSession.getKey();
		List<UserCategoryMember> memberList = userService.getInUserCategoryMemberList(userId);
		List<UserData> userDataList = userService.getGroupMemberUserDataListByUserId(userId);

		Map<String, String> map = new HashMap<String, String>();
		for (UserCategoryMember ucm : memberList) {
			map.put(ucm.getOwnUserId(), ucm.getOwnUserId());
		}
		for (UserData ud : userDataList) {
			map.put(ud.getId(), ud.getId());
		}
		List<String> keyList = new ArrayList<String>(map.values());
		UserData userData=userService.getUserDataById(userId);
		userService.setUserStatus(userData);
		userPush.pushUserUpdate(head, userData, keyList);
	}
	
}
