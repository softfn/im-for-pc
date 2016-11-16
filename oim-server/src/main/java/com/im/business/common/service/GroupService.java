package com.im.business.common.service;

import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.im.bean.Group;
import com.im.bean.GroupCategory;
import com.im.bean.GroupCategoryMember;
import com.im.bean.GroupMember;
import com.im.bean.GroupNumber;
import com.im.bean.User;
import com.im.business.common.dao.GroupCategoryDAO;
import com.im.business.common.dao.GroupCategoryMemberDAO;
import com.im.business.common.dao.GroupDAO;
import com.im.business.common.dao.GroupMemberDAO;
import com.im.business.common.dao.NumberDAO;
import com.im.business.server.message.Message;
import com.im.business.server.message.data.query.GroupQuery;
import com.only.common.util.OnlyDateUtil;
import com.only.query.page.QueryPage;

/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2014年3月31日 上午11:45:15 version 0.0.1
 */
@Service
@Transactional
public class GroupService {

	@Resource
	private GroupDAO groupDAO;
	@Resource
	private GroupMemberDAO groupMemberDAO;
	@Resource
	private GroupCategoryDAO groupCategoryDAO;
	@Resource
	private GroupCategoryMemberDAO groupCategoryMemberDAO;
	@Resource
	private NumberDAO numberDAO;

	public Group getGroupById(String id) {
		Group group = groupDAO.get(Group.class, id);
		return group;
	}

	public Group getGroupByNumber(int number) {
		Group group = groupDAO.getGroupByNumber(number);
		return group;
	}

	public List<GroupCategory> getGroupCategoryListByUserId(String userId) {
		List<GroupCategory> list = groupCategoryDAO.getListByUserId(userId);
		return list;
	}

	public List<GroupCategoryMember> getGroupCategoryMemberListByUserId(String userId) {
		List<GroupCategoryMember> list = groupCategoryMemberDAO.getListByUserId(userId);
		return list;
	}

	public List<Group> getGroupCategoryMemberGroupListByUserId(String userId) {
		List<Group> list = groupDAO.getGroupCategoryMemberGroupListByUserId(userId);
		return list;
	}

	public List<GroupMember> getGroupMemberListByGroupId(String groupId) {
		List<GroupMember> list = groupMemberDAO.getListByGroupId(groupId);
		return list;
	}

	public List<Group> queryGroupList(GroupQuery groupQuery, QueryPage queryPage) {
		List<Group> groupList = groupDAO.queryGroupList(groupQuery, queryPage);
		return groupList;
	}

	public Message add(String userId, Group group, GroupCategoryMember gcm) {
		Message message = new Message();

		int i = new Random().nextInt(3);
		i = i + 1;
		GroupNumber groupNumber = new GroupNumber();// 生成数子账号
		groupNumber.setDateTime(OnlyDateUtil.getCurrentDateTime());

		numberDAO.save(groupNumber);

		group.setNumber(groupNumber.getId());
		group.setHead(i + "");
		group.setHeadType(User.head_type_default);

		groupDAO.save(group);

		if (null == gcm) {
			gcm = new GroupCategoryMember();
		}
		gcm.setUserId(userId);
		gcm.setGroupId(group.getId());
		if (null == gcm.getGroupCategoryId() || "".equals(gcm.getGroupCategoryId())) {
			GroupCategory groupCategory = getOrAddDefaultGroupCategory(userId);
			gcm.setGroupCategoryId(groupCategory.getId());
		}

		groupCategoryMemberDAO.save(gcm);

		GroupMember gm = new GroupMember();
		gm.setUserId(userId);
		gm.setGroupId(group.getId());
		gm.setPosition(GroupMember.position_owner);
		groupMemberDAO.save(gm);
		
		message.put("group", group);
		message.put("groupCategoryMember", gcm);
		message.put("groupMember", gm);
		
		return message;
	}

	public Message update(Group group) {
		Message message = new Message();
		groupDAO.update(group);
		message.put("group", group);
		return message;
	}

	private GroupCategory getOrAddDefaultGroupCategory(String userId) {
		GroupCategory groupCategory = groupCategoryDAO.getDefaultGroupCategory(userId);
		if (null == groupCategory) {
			groupCategory = new GroupCategory();// 生成默认分组信息
			groupCategory.setUserId(userId);
			groupCategory.setName("我的聊天群");
			groupCategory.setSort(GroupCategory.sort_default);
			groupCategoryDAO.save(groupCategory);
		}
		return groupCategory;
	}
}
