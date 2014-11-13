package com.ijoomer.components.icms;

import com.ijoomer.src.R;


/**
 * This Class Contains All Method Related To IcmsFeaturedArticlesActivity.
 * 
 * @author tasol
 * 
 */
public class IcmsFeaturedArticlesActivity extends IcmsMasterActivity {

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
		addFragment(R.id.lnrFragment, new IcmsFeaturedArticlesFragment());
	}

	@Override
	public void setActionListeners() {

	}

}