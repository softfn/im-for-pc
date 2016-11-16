package com.im.business.common.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.im.bean.UserCategory;
import com.im.bean.UserCategoryMember;
import com.im.business.common.dao.NumberDAO;
import com.im.business.common.dao.UserCategoryDAO;
import com.im.business.common.dao.UserCategoryMemberDAO;
import com.im.business.common.dao.UserDAO;
import com.im.business.server.message.Message;

/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2016年1月8日 下午8:12:31
 * @version 0.0.1
 */
@Service
@Transactional
public class UserCategoryService {

	@Resource
	private UserDAO userDAO;
	@Resource
	private NumberDAO numberDAO;
	@Resource
	private UserCategoryDAO userCategoryDAO;
	@Resource
	private UserCategoryMemberDAO userCategoryMemberDAO;

	public Message addUserCategory(UserCategory userCategory) {
		Message m = new Message();
		if (null != userCategory) {
			try {
				userCategory.setSort(UserCategory.sort_custom);
				userCategoryDAO.save(userCategory);
				m.put("userCategory", userCategory);
				m.setInfoCode(Message.code_success);
			} catch (Exception e) {
				m.setInfoCode(Message.code_fail);
			}
		} else {
			m.setInfoCode(Message.code_fail);
		}
		return m;
	}

	public Message addUserCategoryMember(UserCategoryMember userCategoryMember) {

		Message m = new Message();
		if (null != userCategoryMember && null != userCategoryMember.getMemberUserId()) {
			try {
				userCategoryMemberDAO.save(userCategoryMember);
				m.put("userCategoryMember", userCategoryMember);
				m.setInfoCode(Message.code_success);
			} catch (Exception e) {
				m.setInfoCode(Message.code_fail);
			}
		} else {
			m.setInfoCode(Message.code_fail);
		}
		return m;
	}
}
