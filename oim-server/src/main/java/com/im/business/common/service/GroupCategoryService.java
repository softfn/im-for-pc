package com.im.business.common.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.im.bean.GroupCategory;
import com.im.bean.GroupCategoryMember;
import com.im.bean.GroupMember;
import com.im.business.common.dao.GroupCategoryDAO;
import com.im.business.common.dao.GroupCategoryMemberDAO;
import com.im.business.common.dao.GroupMemberDAO;
import com.im.business.server.message.Message;
import com.im.business.server.push.GroupPush;

/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2016年1月8日 下午8:12:31
 * @version 0.0.1
 */
@Service
@Transactional
public class GroupCategoryService {

	@Resource
	private GroupCategoryDAO groupCategoryDAO;
	@Resource
	private GroupCategoryMemberDAO groupCategoryMemberDAO;
	@Resource
	private GroupMemberDAO groupMemberDAO;
	@Resource
	private GroupPush groupPush;

	public Message addGroupCategory(GroupCategory groupCategory) {
		Message message = new Message();
		if (null != groupCategory) {
			try {
				groupCategory.setSort(GroupCategory.sort_custom);
				groupCategoryDAO.save(groupCategory);
				message.put("groupCategory", groupCategory);
				message.setInfoCode(Message.code_success);
			} catch (Exception e) {
				message.setInfoCode(Message.code_fail);
			}
		} else {
			message.setInfoCode(Message.code_fail);
		}
		return message;
	}

	public Message addGroupCategoryMember(GroupCategoryMember groupCategoryMember) {
		Message m = new Message();
		if (null != groupCategoryMember && null != groupCategoryMember.getGroupId()) {
			try {
				String groupId = groupCategoryMember.getGroupId();
				String userId = groupCategoryMember.getUserId();
				groupCategoryMemberDAO.save(groupCategoryMember);
				GroupMember gm = new GroupMember();
				gm.setGroupId(groupCategoryMember.getGroupId());
				gm.setUserId(groupCategoryMember.getUserId());
				gm.setPosition(GroupMember.position_normal);
				groupMemberDAO.save(gm);
				m.put("groupCategoryMember", groupCategoryMember);
				m.put("groupMember", gm);
				m.setInfoCode(Message.code_success);
				
				List<String> userIdList=new ArrayList<String>();
				List<GroupMember> list = groupMemberDAO.getListByGroupId(groupId);
				for(GroupMember member:list){
					userIdList.add(member.getUserId());
				}
				groupPush.pushAddUser(groupId, userId, userIdList);
			} catch (Exception e) {
			}
		} else {
			m.setInfoCode(Message.code_fail);
		}
		return m;
	}
}
