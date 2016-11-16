package com.only.query.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.only.query.QueryWrapper;
import com.only.query.hibernate.QueryContext;
import com.only.query.hibernate.QueryItem;
import com.only.query.hibernate.ResultType;
import com.only.query.hibernate.callback.SQLCallback;
import com.only.query.hibernate.callback.SQLExecuteBack;
import com.only.query.hibernate.callback.SQLPageCallback;
import com.only.query.hibernate.callback.SQLUniqueResultBack;

/**
 * 描述：
 * 
 * @author 夏辉
 * @date 2014年3月29日 下午2:08:59 version 0.0.1
 */
public abstract class AbstractDAO extends HibernateDaoSupport {

	protected QueryContext queryContext;

	public void setQueryContext(QueryContext queryContext) {
		this.queryContext = queryContext;
	}

	/**
	 * 根据主键id获取实体对象
	 * 
	 * @param entityClass
	 * @param id
	 * @return
	 */
	public <T> T get(Class<T> entityClass, Serializable id) {
		return this.getHibernateTemplate().get(entityClass, id);
	}

	/**
	 * 新增数据
	 * 
	 * @param t
	 * @return
	 */
	public <T> Serializable save(T t) {
		return this.getHibernateTemplate().save(t);
	}

	/**
	 * 修改对象
	 * 
	 * @param t
	 */
	public <T> void update(T t) {
		this.getHibernateTemplate().update(t);
	}

	/**
	 * 根据有无主键执行新增或者修改
	 * 
	 * @param t
	 */
	public <T> void saveOrUpdate(T t) {
		this.getHibernateTemplate().saveOrUpdate(t);
	}

	/**
	 * 删除数据
	 * 
	 * @param t
	 */
	public <T> void delete(T t) {
		this.getHibernateTemplate().delete(t);
	}

	/**
	 * 根据类名删除数据
	 * 
	 * @param entityName
	 * @param entity
	 */
	public void delete(String entityName, Object entity) {
		this.getHibernateTemplate().delete(entityName, entity);
	}

	/**
	 * 批量删除实体数据
	 * 
	 * @param entities
	 */
	public void deleteAll(Collection<?> entities) {
		this.getHibernateTemplate().deleteAll(entities);
	}

	/**
	 * 根据类和id删除对象
	 * 
	 * @param entityClass
	 * @param id
	 */
	public void deleteById(Class<?> entityClass, Serializable id) {
		Object o = this.get(entityClass, id);
		this.delete(o);
	}

	public <T> void saveList(final List<T> list) {
		if (null != list) {
			this.getHibernateTemplate().execute(new HibernateCallback<List<T>>() {
				@Override
				public List<T> doInHibernate(Session session) throws HibernateException {
					int batch = 0;
					for (Object o : list) {
						session.save(o);
						batch++;
						if (batch == 50) {// 每50条批量提交一次。
							session.flush();
							session.clear();
							batch = 0;
						}
					}
					return list;
				}
			});
		}
	}

	public <T> void updateList(final List<T> list) {
		if (null != list) {
			this.getHibernateTemplate().execute(new HibernateCallback<List<T>>() {
				@Override
				public List<T> doInHibernate(Session session) throws HibernateException {
					int batch = 0;
					for (Object o : list) {
						session.update(o);
						batch++;
						if (batch == 50) {// 每50条批量提交一次。
							session.flush();
							session.clear();
							batch = 0;
						}
					}
					return list;
				}
			});
		}
	}

	/**
	 * 批量根据有无主键执行新增或者修改
	 * 
	 * @param list
	 */
	public <T> void saveOrUpdateList(final List<T> list) {
		if (null != list) {
			this.getHibernateTemplate().execute(new HibernateCallback<List<T>>() {
				@Override
				public List<T> doInHibernate(Session session) throws HibernateException {
					int batch = 0;
					for (Object o : list) {
						session.saveOrUpdate(o);
						batch++;
						if (batch == 50) {// 每50条批量提交一次。
							session.flush();
							session.clear();
							batch = 0;
						}
					}
					return list;
				}
			});
		}
	}

	public List<?> find(String queryString, Object... values) {
		return this.getHibernateTemplate().find(queryString, values);
	}

	
	/*****************************************************************************************/
	/**
	 * 执行sql语句
	 * 
	 * @param sql
	 * @return
	 */
	public int executeSQL(String sql) {
		return executeSQL(sql, null);
	}

	/**
	 * 执行带传入数据的sql
	 * 
	 * @param sql
	 * @param queryWrapper
	 * @return
	 */
	public int executeSQL(String sql, QueryWrapper queryWrapper) {
		return getHibernateTemplate().execute(new SQLExecuteBack(sql, queryWrapper));
	}

	/**
	 * 执行sql查询，返回单个对象
	 * 
	 * @param sql
	 * @param queryWrapper
	 * @param resultClass
	 * @param returnTypeList
	 * @return
	 */
	public <T> T queryUniqueResult(String sql, QueryWrapper queryWrapper, Class<T> resultClass, List<ResultType> returnTypeList) {
		return this.getHibernateTemplate().execute(new SQLUniqueResultBack<T>(sql, queryWrapper, resultClass, returnTypeList));
	}

	/**
	 * 执行sql返回list对象
	 * 
	 * @param sql
	 * @param queryWrapper
	 * @param resultClass
	 * @param returnTypeList
	 * @return
	 */
	public <T> List<T> queryList(String sql, QueryWrapper queryWrapper, Class<T> resultClass, List<ResultType> returnTypeList) {
		return this.getHibernateTemplate().execute(new SQLCallback<T>(sql, queryWrapper, resultClass, returnTypeList));
	}

	/**
	 * 执行sql返回分页后的对象list
	 * 
	 * @param sql
	 * @param queryWrapper
	 * @param resultClass
	 * @param returnTypeList
	 * @return
	 */
	public <T> List<T> queryPageList(String sql, QueryWrapper queryWrapper, Class<T> resultClass, List<ResultType> returnTypeList) {
		return this.getHibernateTemplate().execute(new SQLPageCallback<T>(sql, queryWrapper, resultClass, returnTypeList));
	}

	/**
	 * 根据xml配置的name执行sql
	 * 
	 * @param name
	 * @param queryWrapper
	 * @return
	 */
	public int executeSQLByName(String name, QueryWrapper queryWrapper) {
		String sql = queryContext.getQueryContent(name, queryWrapper);
		return executeSQL(sql, queryWrapper);
	}

	/**
	 * 根据xml配置的name执行sql，返回单个对象
	 * 
	 * @param name
	 * @param queryWrapper
	 * @param resultClass
	 * @return
	 */
	public <T> T queryUniqueResultByName(String name, QueryWrapper queryWrapper, Class<T> resultClass) {
		QueryItem queryItem = queryContext.getQueryItem(name);
		String sql = queryContext.getQueryContent(name, queryWrapper);
		List<ResultType> resultTypeList = queryItem.getResultTypeList();
		return this.queryUniqueResult(sql, queryWrapper, resultClass, resultTypeList);
	}

	/**
	 * 根据xml配置的name执行sql，返回list对象
	 * 
	 * @param name
	 * @param queryWrapper
	 * @param resultClass
	 * @return
	 */
	public <T> List<T> queryListByName(String name, QueryWrapper queryWrapper, Class<T> resultClass) {
		QueryItem queryItem = queryContext.getQueryItem(name);
		String sql = queryContext.getQueryContent(name, queryWrapper);
		List<ResultType> resultTypeList = queryItem.getResultTypeList();
		return queryList(sql, queryWrapper, resultClass, resultTypeList);
	}

	/**
	 * 根据xml配置的name执行sql，返回分页后的list对象
	 * 
	 * @param <T>
	 * 
	 * @param name
	 * @param queryWrapper
	 * @param resultClass
	 * @return
	 */
	public <T> List<T> queryPageListByName(String name, QueryWrapper queryWrapper, Class<T> resultClass) {
		QueryItem queryItem = queryContext.getQueryItem(name);
		String sql = queryContext.getQueryContent(name, queryWrapper);
		List<ResultType> resultTypeList = queryItem.getResultTypeList();
		return queryPageList(sql, queryWrapper, resultClass, resultTypeList);
	}
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////
}
