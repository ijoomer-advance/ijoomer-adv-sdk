package com.ijoomer.components.icms;

import com.ijoomer.src.R;


/**
 * This Class Contains All Method Related To IcmsCategoryBlogActivity.
 * 
 * @author tasol
 * 
 */
public class IcmsCategoryBlogActivity extends IcmsMasterActivity {

	/**
	 * Overrides methods
	 */
	@Override
	public int setLayoutId() {
		return R.layout.icms_article;
	}

	@Override
	public void initComponents() {

	}

	@Override
	public void prepareViews() {
		addFragment(R.id.lnrFragment, new IcmsCategoryBlogFragment());

	}

	@Override
	public void setActionListeners() {

	}
}