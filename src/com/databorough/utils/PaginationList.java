package com.databorough.utils;

import java.util.ArrayList;

public class PaginationList<T> extends ArrayList<T>{
	private static final long serialVersionUID = 1L;
	private int totalCount;
	private int pageSize;
	private int startRow;
	public PaginationList(int startRow, int pageSize) {
		this.startRow = startRow;
		this.pageSize = pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getTotalPage() {
    	int n = (totalCount % pageSize);
    	n = n > 0 ? 1 : n;
    	return n + (totalCount / pageSize);
	}

	public int getStartRow() {
		return startRow;
	}
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}