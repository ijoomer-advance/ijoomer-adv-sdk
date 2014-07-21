package com.ijoomer.components.icms;

import com.ijoomer.src.R;

/**
 * This Class Contains All Method Related To IcmsAllCategoryActivity.
 * 
 * @author tasol
 * 
 */
public class IcmsAllCategoryActivity extends IcmsMasterActivity {

	/**
	 * Overrides methods
	 */
	@Override
	public int setLayoutId() {
		return R.layout.icms_category;
	}

	@Override
	public void initComponents() {

	}

	@Override
	public void prepareViews() {
		addFragment(R.id.lnrFragment, new IcmsAllCategoryFragment());

	}

	@Override
	public void setActionListeners() {

	}

}