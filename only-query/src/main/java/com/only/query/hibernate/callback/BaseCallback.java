package com.only.query.hibernate.callback;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.only.query.QueryWrapper;
import com.only.query.hibernate.ResultType;

/**
 * @Author: XiaHui
 * @Date: 2015年12月17日
 * @ModifyUser: XiaHui
 * @ModifyDate: 2015年12月17日
 */
public class BaseCallback {

	/**
	 * 根据查询语句 包装total语句
	 *
	 * @param sql
	 *            泛指可能是sql也可能是hql
	 * @return
	 */
	public String wrapTotalSql(String sql) {
		// 后续可以考虑包装一起
		List<String> keyList = new ArrayList<String>(); // sql关键字 用于检查order
		// by是不是在最后
		keyList.add(")");
		keyList.add("where");
		keyList.add("from");
		int orderByIndex = sql.lastIndexOf("order by"); // 后续考虑order by 放后面
		StringBuilder sb = new StringBuilder("select count(1) count from (");
		if (orderByIndex == -1) {
			return sb.append(sql).append(") totalTable").toString();
		}
		boolean boolOrderLast = true; // order by 是否在最后
		for (String key : keyList) {
			int keyIndex = sql.lastIndexOf(key);
			// 解决order by 后面带有()在查询总数sql中存在后在sqlserver2008报错的问题 简单处理，后面有特殊场景在做处理
			// 修改时间2015年1月9日10:37:09 ys
			if (keyIndex > orderByIndex) {
				if (")".equals(key)) {
					// (所在位置
					int keyIndex2 = sql.lastIndexOf("(", keyIndex);
					// (也在order by 后面
					if (keyIndex2 > orderByIndex) {
						continue;
					}
				}
				boolOrderLast = false;
				break;
			}
		}
		if (boolOrderLast) { // 如果order by 为是最后的话 说明该语句排序了 需要把排序去除
			sql = sql.substring(0, orderByIndex);
		}
		return sb.append(sql).append(") totalTable").toString();
	}

	/**
	 * 设置sql or hql查询条件
	 *
	 * @param query
	 * @param queryWrapper
	 */
	@SuppressWarnings("rawtypes")
	protected void setParameter(Query query, QueryWrapper queryWrapper) {
		// TODO Auto-generated method stub
		if (queryWrapper != null) {
			Map<String, Object> map = queryWrapper.getParameterMap();
			String[] manes = query.getNamedParameters();
			for (String name : manes) {
				Object value = map.get(name);
				if (value instanceof Collection) {
					query.setParameterList(name, (Collection) value);
				} else if (value instanceof Object[]) {
					query.setParameterList(name, (Object[]) value);
				} else {
					query.setParameter(name, value);
				}
			}
			// Iterator<Entry<String, Object>> it = map.entrySet().iterator();
			// while (it.hasNext()) {
			// Entry<String, Object> entry = it.next();
			// String key = entry.getKey();
			// Object value = entry.getValue();
			// if (value instanceof Collection) {
			// query.setParameterList(key, (Collection) value);
			// } else if (value instanceof Object[]) {
			// query.setParameterList(key, (Object[]) value);
			// } else {
			// query.setParameter(key, value);
			// }
			// }
		}
	}

	protected void setScalar(Query query, List<ResultType> returnTypeList) {
		if (returnTypeList != null && returnTypeList.size() > 0) {
			if (query instanceof SQLQuery) {
				SQLQuery tmpQuery = (SQLQuery) query;
				for (ResultType returnType : returnTypeList) {
					if (returnType.getReturnType() != null) {
						tmpQuery.addScalar(returnType.getColumnName(), returnType.getReturnType());
					} else {
						tmpQuery.addScalar(returnType.getColumnName());
					}
				}
			}
		}
	}

	protected int getCount(Query query) {
		Object count = query.uniqueResult();
		if (count instanceof Long) {
			return ((Long) count).intValue();
		} else if (count instanceof BigInteger) {
			return ((BigInteger) count).intValue();
		} else {
			return ((Integer) count);
		}
	}

	protected Query createSQLQuery(Session session, String sql) {
		Query query = session.createSQLQuery(sql);
		query.setCacheable(false);
		return query;
	}

	protected Query createHQLQuery(Session session, String hql) {
		Query query = session.createQuery(hql);
		query.setCacheable(false);
		return query;
	}

	public boolean isPrimitive(Class<?> o) {
		return (o.isPrimitive()) || (o == Integer.class) || (o == Long.class) || (o == Float.class)
				|| (o == Double.class) || (o == Byte.class) || (o == Character.class) || (o == Boolean.class)
				|| (o == Short.class);
	}

	public boolean isString(Class<?> o) {
		return (o == String.class);
	}
}
