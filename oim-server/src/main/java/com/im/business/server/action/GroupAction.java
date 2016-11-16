package com.im.business.server.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.im.business.server.message.Message;
import com.im.business.server.message.data.PageData;
import com.im.business.server.message.data.UserData;
import com.im.business.server.message.data.query.GroupQuery;
import com.im.business.server.mq.MessageQueueWriteHandler;
import com.im.bean.Group;
import com.im.bean.GroupCategory;
import com.im.bean.GroupCategoryMember;
import com.im.bean.GroupMember;
import com.im.business.common.service.GroupService;
import com.im.business.common.service.UserService;
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
@ActionMapping(value = "200")
public class GroupAction{

	@Resource
	private GroupService groupService;
	@Resource
	private UserService userService;
	@Resource
	MessageQueueWriteHandler messageQueueWriteHandler;

	@MethodMapping(value = "0001")
	public Message getGrouCategoryList(SocketSession socketSession) {
		Message message=new Message();
		String userId = socketSession.getKey();
		List<GroupCategory> groupCategoryList = groupService.getGroupCategoryListByUserId(userId);
		message.put("groupCategoryList", groupCategoryList);
		return message;
	}

	@MethodMapping(value = "0002")
	public Message getGroupCategoryMemberGroupList(SocketSession socketSession) {
		Message message=new Message();
		String userId = socketSession.getKey();
		List<Group> groupList = groupService.getGroupCategoryMemberGroupListByUserId(userId);
		message.put("groupList", groupList);
		return message;
	}

	@MethodMapping(value = "0003")
	public Message getGroupCategoryMemberList(SocketSession socketSession) {
		Message message=new Message();
		String userId = socketSession.getKey();
		List<GroupCategoryMember> groupCategoryMemberList = groupService.getGroupCategoryMemberListByUserId(userId);
		message.put("groupCategoryMemberList", groupCategoryMemberList);
		return message;
	}

	@MethodMapping(value = "0004")
	public Message getGroupCategoryWithGroupList(SocketSession socketSession) {
		Message message=new Message();
		String userId = socketSession.getKey();
		List<GroupCategory> groupCategoryList = groupService.getGroupCategoryListByUserId(userId);
		List<GroupCategoryMember> groupCategoryMemberList = groupService.getGroupCategoryMemberListByUserId(userId);
		List<Group> groupList = groupService.getGroupCategoryMemberGroupListByUserId(userId);
		
		
		message.put("groupCategoryMemberList", groupCategoryMemberList);
		message.put("groupCategoryList", groupCategoryList);
		message.put("groupList", groupList);
		return message;
	}

	/**
	 * 
	 * @param groupMessage
	 * @param dataWrite
	 * @return
	 */
	@MethodMapping(value = "0005")
	public Message getGroupById(SocketSession socketSession,
			@Parameter("groupId") String groupId) {
		Message message=new Message();
		Group group = groupService.getGroupById(groupId);
		message.put("group", group);
		return message;
	}

	/**
	 * 条件查询用户
	 * 
	 * @param groupMessage
	 * @param dataWrite
	 * @return
	 */
	@MethodMapping(value = "0006")
	public Message queryGroupDataList(
			@Parameter("groupQuery") GroupQuery groupQuery,
			@Parameter("page")  PageData page ) {
		Message message=new Message();
		DefaultPage defaultPage=new DefaultPage();
		defaultPage.setPageNumber(page.getPageNumber());
		defaultPage.setPageSize(page.getPageSize());
		
		List<Group> groupList =groupService.queryGroupList(groupQuery,defaultPage);
		
		page.setTotalCount(defaultPage.getTotalCount());
		page.setTotalPage(defaultPage.getTotalPage());
		
		message.put("groupList", groupList);
		message.put("page", page);
		return message;
	}

	@MethodMapping(value = "0008")
	public Message addGroup(SocketSession socketSession,
			@Parameter("group")Group group, 
			@Parameter("groupCategoryMember")GroupCategoryMember groupCategoryMember) {
		String userId = socketSession.getKey();
		
		return groupService.add(userId,group, groupCategoryMember);
	}

	@MethodMapping(value = "0009")
	public Message update(SocketSession socketSession,
			@Parameter("group")Group group) {
		return groupService.update(group);
	}

	@MethodMapping(value = "0010")
	public Message getGroupMemberListWithUserDataList(@Parameter("groupId") String groupId) {
		List<GroupMember> groupMemberList=groupService.getGroupMemberListByGroupId( groupId);
		List<UserData> userDataList=userService.getGroupMemberUserDataListByGroupId( groupId);
		userService.setUserStatus(userDataList);
		
		Message message=new Message();
		message.put("groupMemberList", groupMemberList);
		message.put("userDataList", userDataList);
		return message;
	}

}
