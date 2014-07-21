package com.ijoomer.components.easyblog;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.TextView;

import com.ijoomer.customviews.IjoomerViewPager;
import com.ijoomer.src.R;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This Class Contains All Method Related To EasyBlogEntryDetailActivity.
 * 
 * @author tasol
 * 
 */
public class EasyBlogEntryDetailActivity extends EasyBlogMasterActivity {

	private IjoomerViewPager viewPager;

	private ArrayList<String> IN_ID_ARRAY;
	private JSONObject IN_OBJ;
	private ArticleDetailAdapter adapter;

	private String IN_ID;
	private int IN_INDEX;

	/**
	 * Overrides method
	 */
	@Override
	public int setLayoutId() {
		return R.layout.easyblog_entry_detail;
	}

	@Override
	public void initComponents() {
		viewPager = (IjoomerViewPager) findViewById(R.id.viewpager);
		adapter = new ArticleDetailAdapter(getSupportFragmentManager());
		getIntentData();
	}

	@Override
	public void prepareViews() {
		((TextView) getHeaderView().findViewById(R.id.txtHeader)).setText(getString(R.string.easy_blog_detail));
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(IN_INDEX);
	}

	@Override
	public void setActionListeners() {

	}

	/**
	 * Class methods
	 */

	/**
	 * This method used to get intent data.
	 */
	private void getIntentData() {
		try {
			IN_OBJ = new JSONObject(getIntent().getStringExtra("IN_OBJ"));
			IN_ID = new JSONObject(IN_OBJ.getString(ITEMDATA)).getString("id");
		} catch (Exception e) {

		}
		if (IN_ID == null) {
			try {
				IN_INDEX = Integer.parseInt(getIntent().getStringExtra("IN_INDEX"));
				IN_ID_ARRAY = getIntent().getStringArrayListExtra("IN_ID_ARRAY");
			} catch (Exception e) {

			}
		} else {
			IN_INDEX = 0;
			IN_ID_ARRAY = new ArrayList<String>();
			IN_ID_ARRAY.add(IN_ID);
		}
	}

	/**
	 * Inner class
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
			return new EasyBlogEntryDetailFragment(EasyBlogEntryDetailActivity.this, IN_ID_ARRAY.get(pos), (pos + 1), IN_ID_ARRAY.size());
		}

		@Override
		public int getCount() {
			if (IN_ID_ARRAY != null)
				return IN_ID_ARRAY.size();
			else
				return 0;
		}
	}
}
