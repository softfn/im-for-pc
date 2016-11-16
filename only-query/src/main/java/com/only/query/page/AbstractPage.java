package com.only.query.page;

import java.util.List;

public abstract class AbstractPage implements QueryPage {

	private int pageSize = 30;// 页码大小
	private int startResult = 0;// 起始记录数
	private int endResult = 0;// 起始记录数
	private int totalCount = 0;
	private int pageNumber = 1;
	private String pageMenu;
	private int totalPage;
	private List<?> resultList;

	public int getTotalCount() {
		return totalCount;
	}

	@Override
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	@Override
	public int getPageSize() {
		if (this.getTotalCount() < pageSize || pageSize < 0) {
			return this.getTotalCount();
		}
		return pageSize;
	}

	@Override
	public void setPageSize(int pageSize) {
		if (pageSize > 0) {
			this.pageSize = pageSize;
		}
	}

	public void setStartResult(int startResult) {
		this.startResult = startResult;
	}

	@Override
	public int getTotalPage() {
		if (this.getTotalCount() > 0) {
			totalPage = this.getTotalCount() / this.getPageSize();
			if (this.getTotalCount() % this.getPageSize() > 0) {
				totalPage = totalPage + 1;
			}
		}
		return totalPage;
	}

	@Override
	public int getStartResult() {
		if (this.getTotalCount() < this.getPageSize() || this.getPageSize() < 0 || this.getPageNumber() < 0) {
			startResult = 0;
		}else if(startResult>0){
			if (startResult > this.getTotalCount()) {
				startResult = this.getTotalCount();
			}
		} else {
			startResult = (this.getPageNumber() - 1) * this.getPageSize();
			if (startResult > this.getTotalCount()) {
				startResult = this.getTotalCount();
			}
			if (startResult < 0) {
				startResult = 0;
			}
		}
		return startResult;
	}

	@Override
	public void setEndResult(int endResult) {
		if (endResult > this.getTotalCount()) {
			this.endResult = this.getTotalCount();
		} else {
			this.endResult = endResult;
		}
	}

	@Override
	public int getEndResult() {
		endResult = (this.getPageNumber() * this.getPageSize());
		if (endResult > this.getTotalCount()) {
			endResult = this.getTotalCount();
		}
		return endResult;
	}

	@Override
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	@Override
	public int getPageNumber() {
		if (getTotalPage() <= 0) {
			return getTotalPage();
		}
		return pageNumber;
	}

	@Override
	public String getPageMenu() {
		return pageMenu;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getResultList() {
		return ((List<T>) resultList);
	}

	@Override
	public <T> void setResultList(List<T> list) {
		this.resultList = list;
	}
}
