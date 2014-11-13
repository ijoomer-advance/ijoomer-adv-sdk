package com.ijoomer.components.sobipro;

import com.ijoomer.src.R;

/**
 * Activity class for SobiproFavouriteActivity view
 * 
 * @author tasol
 * 
 */
public class SobiproFavouriteActivity extends SobiproMasterActivity {

	/**
	 * Overrides methods.
	 */
	@Override
	public int setLayoutId() {
		return R.layout.sobipro_favourite;
	}

	@Override
	public void initComponents() {
	}

	@Override
	public void prepareViews() {
		addFragment(R.id.lnrFragment, new SobiproFavouriteListFragment());
	}

	@Override
	public void setActionListeners() {
	}

}