package com.only.query.hibernate.callback;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate5.HibernateCallback;

import com.only.query.QueryWrapper;

/**
 * @param <T>
 * @Author: XiaHui
 * @Date: 2015年12月25日
 * @ModifyUser: XiaHui
 * @ModifyDate: 2015年12月25日
 */
public class SQLExecuteBack extends BaseCallback implements HibernateCallback<Integer> {
	
	private String queryString;
	private QueryWrapper queryWrapper;
	

	public SQLExecuteBack(String queryString, QueryWrapper queryWrapper) {
		this.queryString = queryString;
		this.queryWrapper = queryWrapper;
	}

	@Override
	public Integer doInHibernate(Session session) throws HibernateException {
		Query query = createSQLQuery(session, queryString);
		setParameter(query, queryWrapper);
		int count=query.executeUpdate();
		return count;
	}
}

