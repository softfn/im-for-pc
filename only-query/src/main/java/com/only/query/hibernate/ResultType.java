package com.only.query.hibernate;

import org.hibernate.type.Type;

/**
 * 查询结果返回类型
 * 
 * @Author: XiaHui
 * @Date: 2015年12月15日
 * @ModifyUser: XiaHui
 * @ModifyDate: 2015年12月15日
 */
public class ResultType {

	private String columnName;// 数据库返回名称
	private String propertyName;//
	private Type returnType;// 返回类型

	public ResultType() {

	}

	public ResultType(String columnName, Type returnType) {
		this.columnName = columnName;
		this.returnType = returnType;
	}

	public ResultType(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Type getReturnType() {
		return returnType;
	}

	public void setReturnType(Type returnType) {
		this.returnType = returnType;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

}
