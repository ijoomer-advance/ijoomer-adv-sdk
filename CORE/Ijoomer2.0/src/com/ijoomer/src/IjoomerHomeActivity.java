package com.ijoomer.src;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

import com.ijoomer.common.classes.IjoomerHomeMaster;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.page.indicator.CirclePageIndicator;

public class IjoomerHomeActivity extends IjoomerHomeMaster {

	private ViewPager viewPager;

	private ArrayList<HashMap<String, String>> menuData;
	private HomePageAdapter adapter;
	private CirclePageIndicator indicator;
	private JSONArray menuItems;

	private int itemsPerPage = 9;
	private int normalScreen = 400;

	/**
	 * Overrides method
	 */

	@Override
	public int setLayoutId() {
		return R.layout.ijoomer_home_menu;
	}

	@Override
	public void initComponents() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		adapter = new HomePageAdapter(getSupportFragmentManager());
		menuData = IjoomerGlobalConfiguration.getHomeMenu(this);
		Display display = getWindowManager().getDefaultDisplay();
		int height = display.getHeight();
		if (height > normalScreen)
			itemsPerPage = 12;
		else
			itemsPerPage = 9;
		try {
			menuItems = new JSONArray(menuData.get(0).get("menuitem"));
			if (menuItems.length() < itemsPerPage)
				itemsPerPage = menuItems.length();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void prepareViews() {
		((IjoomerTextView) getHeaderView().findViewById(R.id.txtHeader)).setVisibility(View.GONE);
		((ImageView) getHeaderView().findViewById(R.id.imgHeader)).setVisibility(View.VISIBLE);
		if (menuItems != null && menuItems.length() > 0) {
			viewPager.setAdapter(adapter);
			viewPager.setCurrentItem(0);

			indicator.setPageColor(Color.TRANSPARENT);
			indicator.setStrokeColor(Color.parseColor(getString(R.color.blue)));
			indicator.setStrokeWidth(convertSizeToDeviceDependent(1));
			indicator.setRadius(convertSizeToDeviceDependent(5));
			indicator.setFillColor(Color.parseColor(getString(R.color.blue)));
			indicator.setViewPager(viewPager, 0);
			indicator.setSnap(true);
			if (((menuItems.length() % itemsPerPage) == 0 ? menuItems.length() / itemsPerPage : (menuItems.length() / itemsPerPage + 1)) <= 1) {
				indicator.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void setActionListeners() {

	}

	/**
	 * Custom class
	 */
	private class HomePageAdapter extends FragmentPagerAdapter {

		public HomePageAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int pos) {
			return new IjoomerHomeFragment(pos * itemsPerPage, ((pos * itemsPerPage) + itemsPerPage) >= menuItems.length() ? menuItems.length()
					: ((pos * itemsPerPage) + itemsPerPage), menuItems);
		}

		@Override
		public int getCount() {
			return (menuItems.length() % itemsPerPage) == 0 ? menuItems.length() / itemsPerPage : (menuItems.length() / itemsPerPage + 1);
		}

	}

}
