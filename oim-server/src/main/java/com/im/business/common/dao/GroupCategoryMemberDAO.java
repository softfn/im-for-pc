package com.im.business.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.im.base.dao.BaseDAO;
import com.im.bean.GroupCategoryMember;

/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2015年3月15日 上午11:01:42
 * @version 0.0.1
 */
@Repository
public class GroupCategoryMemberDAO extends BaseDAO {


	/**
	 * 根据用户id获取所有群和群分组关联的相关信息
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<GroupCategoryMember> getListByUserId(String userId) {
		StringBuilder hql = new StringBuilder();
		hql.append("select gcm from ");
		hql.append(GroupCategoryMember.class.getName());
		hql.append(" gcm where gcm.userId= '");
		hql.append(userId);
		hql.append("'");
		List<GroupCategoryMember> list = (List<GroupCategoryMember>) this.find(hql.toString());
		return list;
	}
}
