package com.ijoomer.components.icms;

import com.ijoomer.src.R;


/**
 * This Class Contains All Method Related To IcmsFavouriteArticlesActivity.
 * 
 * @author tasol
 * 
 */
public class IcmsFavouriteArticlesActivity extends IcmsMasterActivity {

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
		addFragment(R.id.lnrFragment, new IcmsFavouriteArticlesFragment());
	}

	@Override
	public void setActionListeners() {

	}
}