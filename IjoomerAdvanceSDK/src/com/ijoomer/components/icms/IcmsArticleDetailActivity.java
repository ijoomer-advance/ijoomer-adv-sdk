package com.ijoomer.components.icms;

import java.util.ArrayList;
import java.util.HashMap;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.ijoomer.src.R;

/**
 * Activity class for IcmsArticleDetail view
 * 
 * @author tasol
 * 
 */
public class IcmsArticleDetailActivity extends IcmsMasterActivity {

	private ViewPager viewPager;

	ArrayList<String> IN_ARTICLE_ID_ARRAY;
	private ArticleDetailAdapter adapter;

	String IN_ARTICLE_TITLE, IN_ARTICLE_ID;
	int IN_ARTICLE_INDEX;

	/**
	 * Overrides method
	 */
	@Override
	public int setLayoutId() {
		return R.layout.icms_article_detail;
	}

	@Override
	public void initComponents() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);

		adapter = new ArticleDetailAdapter(getSupportFragmentManager());

		getIntentData();

	}

	@Override
	public void prepareViews() {
		((TextView) getHeaderView().findViewById(R.id.txtHeader)).setText(getScreenCaption());
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(IN_ARTICLE_INDEX);
	}

	@Override
	public void setActionListeners() {

	}

	private void getIntentData() {
		try {
			IN_ARTICLE_ID = getIntent().getStringExtra("IN_ARTICLE_ID");
		} catch (Exception e) {

		}
		if (IN_ARTICLE_ID == null) {
			try {
				IN_ARTICLE_INDEX = Integer.parseInt(getIntent().getStringExtra("IN_ARTICLE_INDEX"));
				IN_ARTICLE_ID_ARRAY = getIntent().getStringArrayListExtra("IN_ARTICLE_ID_ARRAY");
			} catch (Exception e) {

			}
		} else {
			IN_ARTICLE_INDEX = 0;
			IN_ARTICLE_ID_ARRAY = new ArrayList<String>();
			IN_ARTICLE_ID_ARRAY.add(IN_ARTICLE_ID);
		}
	}

	/**
	 * Custom class Adapter
	 */
	private class ArticleDetailAdapter extends FragmentStatePagerAdapter {

		public ArticleDetailAdapter(FragmentManager fm) {
			super(fm);
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public Fragment getItem(int pos) {
			System.gc();
			return new IcmsArticleDetailFragment(IcmsArticleDetailActivity.this, IN_ARTICLE_ID_ARRAY.get(pos), (pos + 1), IN_ARTICLE_ID_ARRAY.size());
		}

		@Override
		public int getCount() {
			return IN_ARTICLE_ID_ARRAY.size();
		}
	}

}
