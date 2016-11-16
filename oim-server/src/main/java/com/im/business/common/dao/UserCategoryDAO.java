package com.im.business.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.im.base.dao.BaseDAO;
import com.im.bean.User;
import com.im.bean.UserCategory;

/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2015年3月15日 上午10:58:12
 * @version 0.0.1
 */
@Repository
public class UserCategoryDAO extends BaseDAO {

	@SuppressWarnings("unchecked")
	public List<UserCategory> getListByUserNumber(int userNumber) {
		StringBuilder hql = new StringBuilder();
		hql.append("select uc from ");
		hql.append(UserCategory.class.getName());
		hql.append(" uc where uc.userId= ");
		hql.append("( select u.id from ");
		hql.append(User.class.getName());
		hql.append(" u where u.number = ");
		hql.append(userNumber);
		hql.append(" )");
		List<UserCategory> list = (List<UserCategory>) this.find(hql.toString());
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<UserCategory> getListByUserId(String userId) {
		StringBuilder hql = new StringBuilder();
		hql.append("select uc from ");
		hql.append(UserCategory.class.getName());
		hql.append(" uc where uc.userId= '");
		hql.append(userId);
		hql.append("'");
		List<UserCategory> list = (List<UserCategory>) this.find(hql.toString());
		return list;
	}
}
