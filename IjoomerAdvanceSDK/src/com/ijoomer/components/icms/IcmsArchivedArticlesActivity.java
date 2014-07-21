package com.ijoomer.components.icms;

import com.ijoomer.src.R;


/**
 * This Class Contains All Method Related To IcmsArchivedArticlesActivity.
 * 
 * @author tasol
 * 
 */
public class IcmsArchivedArticlesActivity extends IcmsMasterActivity {

	/**
	 * Overrides method
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
		addFragment(R.id.lnrFragment, new IcmsArchivedArticlesFragment());

	}

	@Override
	public void setActionListeners() {

	}

}