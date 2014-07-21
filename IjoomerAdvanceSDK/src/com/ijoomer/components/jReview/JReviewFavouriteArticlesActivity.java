package com.ijoomer.components.jReview;

import com.ijoomer.src.R;


/**
 * This Class Contains All Method Related To IcmsCategoryBlogActivity.
 * 
 * @author tasol
 * 
 */
public class JReviewFavouriteArticlesActivity extends JReviewMasterActivity {

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
	public void prepareViews() {
		addFragment(R.id.lnrFragment, new JReviewFavouriteArticlesFragment());
	}

	@Override
	public void setActionListeners() {

	}
}