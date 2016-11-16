package com.oim.core.net.message.different;

import com.oim.core.net.message.common.Page;

public class PageImpl implements Page {

	private int pageSize = 30;// 页码大小
	private int startResult = 0;// 起始记录数
	private int endResult = 0;// 起始记录数
	private int totalCount = 0;
	private int pageNumber = 1;
	private String pageMenu;
	private int totalPage;

	public int getTotalCount() {
		return totalCount;
	}

	@Override
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	@Override
	public int getPageSize() {
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
		return totalPage;
	}

	@Override
	public String getPageMenu() {
		return pageMenu;
	}

	@Override
	public int getStartResult() {
		return startResult;
	}

	@Override
	public void setEndResult(int endResult) {
		this.endResult = endResult;
	}

	@Override
	public int getEndResult() {
		return endResult;
	}

	@Override
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	@Override
	public int getPageNumber() {
		return pageNumber;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

}
