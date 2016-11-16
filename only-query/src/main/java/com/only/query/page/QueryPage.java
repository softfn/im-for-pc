package com.only.query.page;

import java.util.List;

/**
 * 
 * 2013-8-27 下午6:12:06
 * 
 * @author XiaHui
 */
public interface QueryPage {

	public void setTotalCount(int totalCount);

	public int getStartResult();

	public int getEndResult();

	public void setEndResult(int endResult);

	public void setPageSize(int pageSize);

	public int getPageSize();

	public void setPageNumber(int pageNumber);

	public int getPageNumber();

	public int getTotalPage();

	public String getPageMenu();

	public <T> List<T> getResultList();
	
	public <T> void setResultList(List<T> list);

}
