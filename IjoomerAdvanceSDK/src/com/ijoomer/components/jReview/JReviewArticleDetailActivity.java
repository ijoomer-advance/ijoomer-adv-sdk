package com.ijoomer.components.jReview;

import java.util.ArrayList;

import org.json.JSONObject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.library.jReview.JReviewDataProvider;
import com.ijoomer.src.IjoomerLoginActivity;
import com.ijoomer.src.R;

/**
 * This Class Contains All Method Related To JReviewArticleDetailActivity.
 * 
 * @author tasol
 * 
 */
public class JReviewArticleDetailActivity extends JReviewMasterActivity {

	private ViewPager viewPager;
	private ImageView editArticle;

	private ArrayList<String> IN_ARTICLE_ID_ARRAY;
	private ArrayList<String> IN_CATEGORY_ID_ARRAY;
	private ArticleDetailAdapter adapter;

	private String IN_ARTICLE_ID;
	private String IN_PAGE = "";
	private int IN_ARTICLE_INDEX;
	private JSONObject IN_OBJ;

	private JReviewDataProvider dataProvider;

	/**
	 * Overrides method
	 */
	@Override
	public int setLayoutId() {
		return R.layout.jreview_article_detail;
	}

	@Override
	public void initComponents() {
		getIntentData();
		dataProvider = new JReviewDataProvider(this);

		editArticle = ((ImageView) getHeaderView().findViewById(R.id.imgeditArticle));
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		adapter = new ArticleDetailAdapter(getSupportFragmentManager());
	}

	@Override
	protected void onResume() {
		super.onResume();
		applySideMenu("JReviewAllDirectoriesActivity");
		if(getSmartApplication().readSharedPreferences()
				.getBoolean(SP_RELOADARTICLEDETAILS, false)){
			getSmartApplication().writeSharedPreferences(SP_RELOADARTICLEDETAILS, false);
			adapter.notifyDataSetChanged();
			viewPager.setCurrentItem(viewPager.getCurrentItem());
		}
	}

	@Override
	public void prepareViews() {
		((TextView) getHeaderView().findViewById(R.id.txtHeader)).setText(getIntent().getStringExtra("IN_CAPTION"));
		if(getSmartApplication().readSharedPreferences().getString(SP_USERNAME, "").length() > 0){
			if(IjoomerGlobalConfiguration.isJreviewAddListingAllow()){
				if (IN_PAGE.equalsIgnoreCase(PAGEARTICLES)) {
					editArticle.setVisibility(View.VISIBLE);
				} 
			}
		}
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(IN_ARTICLE_INDEX);
	}

	@Override
	public void setActionListeners() {
		editArticle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (getSmartApplication().readSharedPreferences().getString(SP_USERNAME, "").length() > 0) {
					try {
						loadNew(JReviewCreateListingActivity.class, JReviewArticleDetailActivity.this, false,
								CATEGORY_ID, IN_CATEGORY_ID_ARRAY.get(viewPager.getCurrentItem()), ARTICLEID, IN_ARTICLE_ID_ARRAY.get(viewPager.getCurrentItem()), "IN_ARTICLE_DETAILS",
								dataProvider.getArticleDetails(IN_CATEGORY_ID_ARRAY.get(viewPager.getCurrentItem()), IN_ARTICLE_ID_ARRAY.get(viewPager.getCurrentItem())),
								GROUPS, dataProvider.getListingForm(IN_CATEGORY_ID_ARRAY.get(viewPager.getCurrentItem()), IN_ARTICLE_ID_ARRAY.get(viewPager.getCurrentItem())),
								"IS_EDIT", true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					try {
						getSmartApplication().writeSharedPreferences(SP_DOLOGIN, true);
						loadNew(IjoomerLoginActivity.class, JReviewArticleDetailActivity.this, false);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
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
			IN_ARTICLE_ID = new JSONObject(IN_OBJ.getString(ITEMDATA))
			.getString("id");
		} catch (Exception e) {

		}
		if (IN_ARTICLE_ID == null) {
			try {
				IN_PAGE = getIntent().getStringExtra("IN_PAGE");
				IN_ARTICLE_INDEX = Integer.parseInt(getIntent().getStringExtra(
						"IN_ARTICLE_INDEX"));
				IN_CATEGORY_ID_ARRAY = getIntent().getStringArrayListExtra(
						"IN_CATEGORY_ID_ARRAY");
				IN_ARTICLE_ID_ARRAY = getIntent().getStringArrayListExtra(
						"IN_ARTICLE_ID_ARRAY");
			} catch (Exception e) {
				e.printStackTrace();
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
			return new JReviewArticleDetailFragment(
					JReviewArticleDetailActivity.this, IN_PAGE,
					IN_CATEGORY_ID_ARRAY.get(pos),
					IN_ARTICLE_ID_ARRAY.get(pos), (pos + 1),
					IN_ARTICLE_ID_ARRAY.size());
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
