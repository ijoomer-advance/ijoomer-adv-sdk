package com.ijoomer.plugins;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ijoomer.src.R;

/**
 * This Class Contains All Method Related To IcmsArticleDetailActivity.
 * 
 * @author tasol
 * 
 */
public class PluginsWeatherActivity extends PluginsMasterActivity {

	private ViewPager viewPager;

	private ArrayList<String> IN_LOCATION_ID_ARRAY;
	private ArticleDetailAdapter adapter;
	private ImageView imgAddLocation;

	private int IN_LOCATION_INDEX;

	/**
	 * Overrides method
	 */
	@Override
	public int setLayoutId() {
		return R.layout.plugins_weather;
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
		imgAddLocation = (ImageView) getHeaderView().findViewById(R.id.imgAdd);
		imgAddLocation.setVisibility(View.VISIBLE);
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(IN_LOCATION_INDEX);
	}

	@Override
	public void setActionListeners() {
		imgAddLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadNew(PluginsWeatherLocationActivity.class, PluginsWeatherActivity.this, false);
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
			IN_LOCATION_INDEX = Integer.parseInt(getIntent().getStringExtra("IN_LOCATION_INDEX"));
			IN_LOCATION_ID_ARRAY = getIntent().getStringArrayListExtra("IN_LOCATION_ID_ARRAY");
		} catch (Exception e) {

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
			return new PluginsWeatherFragment(IN_LOCATION_ID_ARRAY.get(pos));
		}

		@Override
		public int getCount() {
			if (IN_LOCATION_ID_ARRAY != null)
				return IN_LOCATION_ID_ARRAY.size();
			else
				return 0;
		}
	}

}
