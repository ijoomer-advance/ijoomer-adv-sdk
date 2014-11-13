package com.ijoomer.components.k2;

import java.util.ArrayList;
import java.util.HashMap;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.ijoomer.src.R;

/**
 * This Class Contains All Method Related To K2CatalogDetailsActivity.
 * 
 * @author tasol
 * 
 */
public class K2CatalogDetailsActivity extends K2MasterActivity {

	private ViewPager viewPager;
	

	private ArrayList<HashMap<String, String>> IN_ITEMS_LIST;
	private CatalogDetailAdapter adapter;

	private int IN_CURRENT_ITEM_SELECTED;

	/**
	 * Overrides method
	 */
	@Override
	public int setLayoutId() {
		return R.layout.k2_article_detail;
	}

	@Override
	public void initComponents() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);

		getIntentData();
		adapter = new CatalogDetailAdapter(getSupportFragmentManager());

	}

	@Override
	public void prepareViews() {
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(IN_CURRENT_ITEM_SELECTED, true);
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
	@SuppressWarnings("unchecked")
	private void getIntentData() {
		IN_ITEMS_LIST = (ArrayList<HashMap<String, String>>) (getIntent().getSerializableExtra("IN_ITEMS_LIST") != null ? getIntent().getSerializableExtra("IN_ITEMS_LIST") : new ArrayList<HashMap<String, String>>());
		IN_CURRENT_ITEM_SELECTED = getIntent().getIntExtra("IN_CURRENT_ITEM_SELECTED", 0);
	}

	/**
	 * Inner class
	 */
	private class CatalogDetailAdapter extends FragmentStatePagerAdapter {

		public CatalogDetailAdapter(FragmentManager fm) {
			super(fm);
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public Fragment getItem(int pos) {
			System.gc();
			return new k2CatalogDetailFragment(K2CatalogDetailsActivity.this, IN_ITEMS_LIST.get(pos));
		}

		@Override
		public int getCount() {
			return IN_ITEMS_LIST.size();
		}
	}

}
