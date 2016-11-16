package com.im.business.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.im.base.dao.BaseDAO;
import com.im.bean.User;
import com.im.bean.UserCategoryMember;

/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2015年3月15日 上午11:01:42
 * @version 0.0.1
 */
@Repository
public class UserCategoryMemberDAO extends BaseDAO {

	@SuppressWarnings("unchecked")
	public List<UserCategoryMember> getListByUserNumber(int userNumber) {
		StringBuilder hql = new StringBuilder();
		hql.append("select ucm from ");
		hql.append(UserCategoryMember.class.getName());
		hql.append(" ucm where ucm.ownUserId= ");
		hql.append("( select u.id from ");
		hql.append(User.class.getName());
		hql.append(" u where u.number = ");
		hql.append(userNumber);
		hql.append(" )");
		List<UserCategoryMember> list = (List<UserCategoryMember>) this.find(hql.toString());
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<UserCategoryMember> getListByUserId(String userId) {
		StringBuilder hql = new StringBuilder();
		hql.append("select ucm from ");
		hql.append(UserCategoryMember.class.getName());
		hql.append(" ucm where ucm.ownUserId= '");
		hql.append(userId);
		hql.append("'");
		List<UserCategoryMember> list = (List<UserCategoryMember>) this.find(hql.toString());
		return list;
	}

	/**
	 * 获取用户在别人的好友列表的集合
	 * 
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserCategoryMember> getInListByUserId(String userId) {
		StringBuilder hql = new StringBuilder();
		hql.append("select ucm from ");
		hql.append(UserCategoryMember.class.getName());
		hql.append(" ucm where ucm.memberUserId= '");
		hql.append(userId);
		hql.append("'");
		List<UserCategoryMember> list = (List<UserCategoryMember>) this.find(hql.toString());
		return list;
	}
}
