package com.only.query.hibernate;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.only.query.QueryWrapper;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.Version;

/**
 * @Author: XiaHui
 * @Date: 2015年12月18日
 * @ModifyUser: XiaHui
 * @ModifyDate: 2015年12月18日
 */
public class QueryContext {
	
	protected final Logger logger = LogManager.getLogger(this.getClass());
	private final QueryDecode qd = new QueryDecode();
	private final Map<String, QueryItem> queryItemMap = new ConcurrentHashMap<String, QueryItem>();

	private final StringTemplateLoader templateLoader = new StringTemplateLoader();
	private final Version version = new Version("2.3.23");
	private final Configuration config = new Configuration(version);

	private String configLocation;

	public QueryContext() {
		config.setTemplateLoader(templateLoader);
		config.setObjectWrapper(new DefaultObjectWrapper(version));
	}

	public String getConfigLocation() {
		return configLocation;
	}

	public void setConfigLocation(String configLocation) {
		this.configLocation = configLocation;
	}

	public void load() throws QueryItemException {
		queryItemMap.clear();
		if (null != configLocation && !"".equals(configLocation)) {
			List<QueryItem> queryItemList = qd.getQueryItemList(configLocation);
			for (QueryItem item : queryItemList) {
				String key = getKey(item);
				if (queryItemMap.containsKey(key)) {
					throw new QueryItemException("命名空间为：" + item.getSpaceName() + "，查询语句名为：" + item.getName() + "，有重复！");
				} else {
					queryItemMap.put(key, item);
					templateLoader.putTemplate(key, item.getContent());
				}
			}
		}
	}

	private String getKey(QueryItem item) {
		StringBuilder key = new StringBuilder();
		key.append(item.getSpaceName());
		key.append(".");
		key.append(item.getName());
		return key.toString();
	}

	public QueryItem getQueryItem(String name) {
		return queryItemMap.get(name);
	}

	public String getQueryContent(String name, Map<String, Object> valueMap) {
		StringWriter writer = new StringWriter();
		Template template;
		try {
			template = config.getTemplate(name, "UTF-8");
			template.process(valueMap, writer);
		} catch (Exception e) {
			logger.error("", e);
		}
		try {
			writer.flush();
			writer.close();
		} catch (IOException e) {
			logger.error("", e);
		}
		String sql = writer.toString();
		if (logger.isDebugEnabled()) {
			logger.debug(sql);
		} else if (logger.isInfoEnabled()) {
			logger.info(sql);
		}
		return sql;
	}

	public String getQueryContent(String name, QueryWrapper queryWrapper) {
		Map<String, Object> valueMap = (null == queryWrapper) ? null : queryWrapper.getParameterMap();
		return getQueryContent(name, valueMap);
	}
}
