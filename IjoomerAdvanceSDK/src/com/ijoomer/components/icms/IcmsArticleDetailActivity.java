package com.ijoomer.components.icms;

import java.util.ArrayList;

import org.json.JSONObject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.ijoomer.src.R;


/**
 * This Class Contains All Method Related To IcmsArticleDetailActivity.
 * 
 * @author tasol
 * 
 */
public class IcmsArticleDetailActivity extends IcmsMasterActivity {

	private ViewPager viewPager;

	private ArrayList<String> IN_ARTICLE_ID_ARRAY;
	private ArticleDetailAdapter adapter;

	private String IN_ARTICLE_ID;
	private int IN_ARTICLE_INDEX;
	private JSONObject IN_OBJ;

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

	/**
	 * Class methods
	 */

	/**
	 * This method used to get intent data.
	 */
	private void getIntentData() {
		try {
			IN_OBJ = new JSONObject(getIntent().getStringExtra("IN_OBJ"));
			IN_ARTICLE_ID = new JSONObject(IN_OBJ.getString(ITEMDATA)).getString("id");
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
			return new IcmsArticleDetailFragment(IcmsArticleDetailActivity.this, IN_ARTICLE_ID_ARRAY.get(pos), (pos + 1), IN_ARTICLE_ID_ARRAY.size());
		}

		@Override
		public int getCount() {
			if (IN_ARTICLE_ID_ARRAY != null)
				return IN_ARTICLE_ID_ARRAY.size();
			else
				return 0;
		}
	}

}
