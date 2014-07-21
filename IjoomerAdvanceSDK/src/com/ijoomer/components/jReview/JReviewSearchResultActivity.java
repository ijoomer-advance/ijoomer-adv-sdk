package com.ijoomer.components.jReview;

import com.ijoomer.src.R;

/**
 * This Class Contains All Method Related To SobiproEntriesActivity.
 * 
 * @author tasol
 * 
 */

public class JReviewSearchResultActivity extends JReviewMasterActivity {
	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.jreview_articles_view;
	}

	@Override
	public void initComponents() {

	}

	@Override
	protected void onResume() {
		super.onResume();
		applySideMenu("JReviewAllDirectoriesActivity");
	}

	@Override
	public void prepareViews() {
		addFragment(R.id.lnrFragment, new JReviewSearchResultListFragment());
	}

	@Override
	public void setActionListeners() {
	}

}
