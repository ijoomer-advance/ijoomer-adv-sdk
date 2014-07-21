package com.ijoomer.components.icms;

import com.ijoomer.src.R;

/**
 * This Class Contains All Method Related To IcmsCategoryActivity.
 * 
 * @author tasol
 * 
 */
public class IcmsCategoryActivity extends IcmsMasterActivity {

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
		addFragment(R.id.lnrFragment, new IcmsCategoryFragment());
	}

	@Override
	public void setActionListeners() {

	}

}