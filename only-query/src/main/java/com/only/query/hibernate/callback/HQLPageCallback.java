package com.only.query.hibernate.callback;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate5.HibernateCallback;

import com.only.query.QueryWrapper;
import com.only.query.page.DefaultPage;
import com.only.query.page.QueryPage;

/**
 * 描述：
 * 
 * @author 夏辉
 * @param <T>
 * @date 2014年3月31日 下午1:01:17 version 0.0.1
 */
public class HQLPageCallback<T> extends BaseCallback implements HibernateCallback<T> {
	private String queryString;
	private QueryWrapper queryWrapper;
	private Class<?> resultClass;

	public HQLPageCallback(String queryString, QueryWrapper queryWrapper, Class<?> resultClass) {
		this.queryString = queryString;
		this.queryWrapper = queryWrapper;
		this.resultClass = resultClass;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T doInHibernate(Session session) throws HibernateException {
		List<T> list = null;
		String queryCountSQL = wrapTotalSql(queryString);
		Query queryCount = createHQLQuery(session, queryCountSQL);
		setParameter(queryCount, queryWrapper);
		QueryPage page = queryWrapper.getPage();
		if (page == null) {
			page = new DefaultPage();
		}
		int totalCount = getCount(queryCount);
		if (0 < totalCount) {
			page.setTotalCount(totalCount);
			Query query = createHQLQuery(session, queryString);
			query.setFirstResult(page.getStartResult()); // 从第0条开始
			query.setMaxResults(page.getPageSize()); // 取出10条
			setParameter(query, queryWrapper);
			if (resultClass != null) {
				query.setResultTransformer(Transformers.aliasToBean(resultClass));
			}
			list = query.list();
		}
		if (null == list) {
			list = new ArrayList<>();
		}
		return (T) list;
	}
}
