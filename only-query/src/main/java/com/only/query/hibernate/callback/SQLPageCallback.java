package com.only.query.hibernate.callback;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate5.HibernateCallback;

import com.only.query.QueryWrapper;
import com.only.query.hibernate.ResultToBean;
import com.only.query.hibernate.ResultType;
import com.only.query.page.DefaultPage;
import com.only.query.page.QueryPage;

/**
 * 描述：
 * 
 * @author 夏辉
 * @param <T>
 * @param <T>
 * @date 2014年3月31日 下午1:01:17 version 0.0.1
 */
public class SQLPageCallback<T> extends BaseCallback implements HibernateCallback<List<T>> {
	private String queryString;
	private QueryWrapper queryWrapper;
	private Class<T> resultClass;
	private List<ResultType> returnTypeList;

	public SQLPageCallback(String queryString, QueryWrapper queryWrapper, Class<T> resultClass, List<ResultType> returnTypeList) {
		this.queryString = queryString;
		this.queryWrapper = queryWrapper;
		this.resultClass = resultClass;
		this.returnTypeList = returnTypeList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> doInHibernate(Session session) throws HibernateException {
		List<T> list = null;
		String queryCountSQL = wrapTotalSql(queryString);
		Query queryCount = createSQLQuery(session, queryCountSQL);
		setParameter(queryCount, queryWrapper);
		QueryPage page = queryWrapper.getPage();
		if (page == null) {
			page = new DefaultPage();
		}
		int totalCount = getCount(queryCount);
		if (0 < totalCount) {
			page.setTotalCount(totalCount);
			Query query = createSQLQuery(session, queryString);
			query.setFirstResult(page.getStartResult()); // 从第0条开始
			query.setMaxResults(page.getPageSize()); // 取出10条
			setParameter(query, queryWrapper);
			if (null != returnTypeList) {
				setScalar(query, returnTypeList);
			}
			if (resultClass != null && !this.isPrimitive(resultClass) && !this.isString(resultClass)) {
				if (null == returnTypeList || returnTypeList.isEmpty()) {
					query.setResultTransformer(new ResultToBean(resultClass));
				} else {
					query.setResultTransformer(Transformers.aliasToBean(resultClass));
				}
			}
			list = query.list();
		}
		if (null == list) {
			list = new ArrayList<T>();
		}
		page.setResultList(list);
		return list;
	}
}
