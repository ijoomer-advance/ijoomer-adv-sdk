package com.ijoomer.common.classes;

import android.content.Context;

public class IjoomerPagingProvider extends IjoomerResponseValidator {
	private int pageCounter = 1;
	private int totalPageCount = 0;
	private int pageNo = 1;
	private int pageLimit = 10;
	private int totalCount = 0;
	private boolean hasNextPage = true;

	public IjoomerPagingProvider(Context mContext) {
		super(mContext);
	}

	public void restorePagingSettings() {
		pageCounter = 1;
		totalPageCount = 0;
		pageNo = 1;
		pageLimit = 10;
		totalCount = 0;
		hasNextPage = true;
	}

	protected int getPageCounter() {
		return pageCounter;
	}

	protected void setPageCounter(int pageCounter) {
		this.pageCounter = pageCounter;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		if (totalPageCount != 0 && (pageCounter > totalPageCount)) {
			setHasNextPage(false);
		}
		this.pageNo = pageNo;
	}

	public int getPageLimit() {
		return pageLimit;
	}

	public void setPageLimit(int pageLimit) {
		this.pageLimit = pageLimit;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public boolean hasNextPage() {
		return hasNextPage;
	}

	private void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	public int getTotalPageCount() {
		return totalPageCount;
	}

	protected void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}
}
