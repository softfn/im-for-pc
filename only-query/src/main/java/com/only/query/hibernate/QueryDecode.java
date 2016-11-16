package com.only.query.hibernate;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.hibernate.type.Type;
import org.hibernate.type.TypeResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * 解析xml为查询对象
 * 
 * @Author: XiaHui
 * @Date: 2015年12月18日
 * @ModifyUser: XiaHui
 * @ModifyDate: 2015年12月18日
 */
public class QueryDecode {

	protected final Logger logger = LogManager.getLogger(this.getClass());
	private TypeResolver typeResolver = new TypeResolver();

	public void test() {
		try {
			String resourcePath = "classpath:query/*.xml";
			getQueryItemList(resourcePath);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public List<QueryItem> getQueryItemList(String resourcePath) {
		List<QueryItem> queryItemList = new ArrayList<QueryItem>();
		try {
			PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			SAXReader saxReader = new SAXReader();
			Resource[] resources = resolver.getResources(resourcePath);
			if (resources != null && resources.length > 0) {
				for (Resource resource : resources) {
					InputStream input = resource.getInputStream();
					try {
						Document document = saxReader.read(input);
						List<QueryItem> list = getQueryItemList(document);
						queryItemList.addAll(list);
					} catch (Exception e) {
						logger.error("", e);
					} finally {
						input.close();
					}
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return queryItemList;
	}

	@SuppressWarnings("unchecked")
	public List<QueryItem> getQueryItemList(Document document) {
		List<QueryItem> queryItemList = new ArrayList<QueryItem>();
		Element rootElement = document.getRootElement();
		List<Element> list = rootElement.elements();
		if (null != list && !list.isEmpty()) {
			QueryItem item = null;
			for (Element element : list) {

				String spaceName = element.attributeValue("name");

				List<Element> itemList = element.elements();

				for (Element e : itemList) {
					item = new QueryItem();
					String name = e.attributeValue("name");
					String type = e.attributeValue("type");

					Element resultListElement = e.element("resultList");

					Element contentElement = e.element("content");
					String content = contentElement.getText();
					item.setSpaceName((null == spaceName) ? "" : spaceName);
					item.setName(name);
					item.setType(type);
					item.setContent(content);
					if (null != resultListElement) {
						List<Element> resultList = resultListElement.elements();
						if (null != resultList && !resultList.isEmpty()) {
							for (Element r : resultList) {
								ResultType resultType = getResultType(r);
								item.add(resultType);
							}
						}
					}
					queryItemList.add(item);
				}
			}
		}
		return queryItemList;

	}

	public ResultType getResultType(Element element) {
		ResultType resultType = new ResultType();
		String columnName = element.attribute("column").getText().trim();
		String typeName = element.attribute("type").getText();
		String propertyName = (null==element.attribute("name"))?"":element.attribute("name").getText();
		resultType.setColumnName(columnName);
		if (null != typeName && !"".equals(typeName)) {
			String typeNameTemp = typeName.toLowerCase().trim();
			Type type = typeResolver.basic(typeNameTemp);
			resultType.setReturnType(type);
		}
		if (null != propertyName && !"".equals(propertyName)) {
			resultType.setPropertyName(propertyName.trim());
		}
		return resultType;
	}

	public boolean isValuable(String text) {
		if (null == text || "".equals(text)) {
			return false;
		}
		String temp = text.replace("\n", "").replace("\t", "").replace(" ", "").replace("\\n", "").trim();
		return !"".equals(temp);
	}
}
