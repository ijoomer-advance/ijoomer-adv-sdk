package com.ijoomer.components.easyblog;

import com.ijoomer.src.R;

/**
 * This Class Contains All Method Related To IcmsAllCategoryActivity.
 * 
 * @author tasol
 * 
 */
public class EasyBlogAllCategoryActivity extends EasyBlogMasterActivity {

	/**
	 * Overrides methods
	 */
	@Override
	public int setLayoutId() {
		return R.layout.easyblog_category;
	}

	@Override
	public void initComponents() {

	}

	@Override
	public void prepareViews() {
		addFragment(R.id.lnrFragment, new EasyBlogAllCategoryFragment());

	}

	@Override
	public void setActionListeners() {

	}

}