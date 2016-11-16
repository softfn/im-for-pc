package com.only.query.hibernate.callback;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate5.HibernateCallback;

import com.only.query.QueryWrapper;
import com.only.query.hibernate.ResultToBean;
import com.only.query.hibernate.ResultType;

/**
 * @param <T>
 * @Author: XiaHui
 * @Date: 2015年12月25日
 * @ModifyUser: XiaHui
 * @ModifyDate: 2015年12月25日
 */
public final class HQLUniqueResultBack<T> extends BaseCallback implements HibernateCallback<T> {

	private String queryString;
	private QueryWrapper queryWrapper;
	private Class<?> resultClass;
	private List<ResultType> returnTypeList;

	public HQLUniqueResultBack(String queryString, QueryWrapper queryWrapper, Class<T> resultClass, List<ResultType> returnTypeList) {
		this.queryString = queryString;
		this.queryWrapper = queryWrapper;
		this.resultClass = resultClass;
		this.returnTypeList = returnTypeList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T doInHibernate(Session session) throws HibernateException {
		Query query = createHQLQuery(session, queryString);
		setParameter(query, queryWrapper);
		setScalar(query, returnTypeList);

		if (resultClass != null && !this.isPrimitive(resultClass) && !this.isString(resultClass)) {
			if (null == returnTypeList || returnTypeList.isEmpty()) {
				query.setResultTransformer(new ResultToBean(resultClass));
			} else {
				query.setResultTransformer(Transformers.aliasToBean(resultClass));
			}
		}
		Object value = query.uniqueResult();
		return (T) value;
	}
}
