package com.ijoomer.components.sobipro;

import com.ijoomer.src.R;

/**
 * Activity class for SobiproGalleryActivity view
 * 
 * @author tasol
 * 
 */
public class SobiproGalleryActivity extends SobiproMasterActivity {

	/**
	 * Overrides methods.
	 */
	@Override
	public int setLayoutId() {
		return R.layout.sobipro_gallery;
	}

	@Override
	public void initComponents() {
	}

	@Override
	public void prepareViews() {
		addFragment(R.id.lnrFragment, new SobiproGalleryFragment());
	}

	@Override
	public void setActionListeners() {
	}

}