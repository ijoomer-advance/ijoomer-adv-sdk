package com.ijoomer.common.classes;

import android.content.Context;

/**
 * This Class Contains All Method Related To IjoomerPagingProvider.
 * 
 * @author tasol
 * 
 */
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

	/**
	 * This method used to restore page settings.
	 */
	public void restorePagingSettings() {
		pageCounter = 1;
		totalPageCount = 0;
		pageNo = 1;
		pageLimit = 10;
		totalCount = 0;
		hasNextPage = true;
	}

	/**
	 * This method used to get page counter.
	 * 
	 * @return represented {@link Integer}
	 */
	protected int getPageCounter() {
		return pageCounter;
	}

	/**
	 * This method used to set page counter.
	 * 
	 * @param pageCounter
	 *            represented page counter
	 */
	protected void setPageCounter(int pageCounter) {
		this.pageCounter = pageCounter;
	}

	/**
	 * This method used to get page no.
	 * 
	 * @return represented {@link Integer}
	 */
	public int getPageNo() {
		if (!IjoomerUtilities.isNetwokReachable()) {
			hasNextPage = false;
		}
		return pageNo;
	}

	/**
	 * This method used to set page no.
	 * 
	 * @param pageNo
	 *            represented page no.
	 */
	public void setPageNo(int pageNo) {
		if (totalPageCount != 0 && (pageCounter > totalPageCount)) {
			setHasNextPage(false);
		}
		this.pageNo = pageNo;
	}

	/**
	 * This method used to get page limit.
	 * 
	 * @return represented {@link Integer}
	 */
	public int getPageLimit() {
		return pageLimit;
	}

	/**
	 * This method used set page limit.
	 * 
	 * @param pageLimit
	 *            represented page limit
	 */
	public void setPageLimit(int pageLimit) {
		this.pageLimit = pageLimit;
	}

	/**
	 * This method used to get total count.
	 * 
	 * @return represented {@link Integer}
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * This method used to set total count.
	 * 
	 * @param totalCount
	 *            represented total count
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * This method used to check hash next page.
	 * 
	 * @return represented {@link Boolean}
	 */
	public boolean hasNextPage() {
		return hasNextPage;
	}

	/**
	 * This method used to set hash next page.
	 * 
	 * @param hasNextPage
	 *            represented isHashNextPage
	 */
	protected void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	/**
	 * This method used to get total page count.
	 * 
	 * @return represented {@link Integer}
	 */
	public int getTotalPageCount() {
		return totalPageCount;
	}

	/**
	 * This method used to set total page counter.
	 * 
	 * @param totalPageCount
	 *            represented total page counter
	 */
	protected void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}

	/**
	 * This method used to set Paging.
	 * 
	 * @param pageLimit
	 *            represented page limit
	 * @param total
	 *            represented total data
	 */
	protected void setPagingParams(int pageLimit, int total) {
		setTotalCount(total);
		setPageLimit(pageLimit);
		if (total > pageLimit) {
			setTotalPageCount(((total % pageLimit == 0) ? total / pageLimit : (total / pageLimit) + 1));
			setPageCounter(getPageCounter() + 1);
			setPageNo(getPageCounter());
		} else {
			setTotalCount(total);
			setTotalPageCount(1);
			setPageCounter(getPageCounter() + 1);
			setPageNo(getPageCounter());
		}
	}
}
