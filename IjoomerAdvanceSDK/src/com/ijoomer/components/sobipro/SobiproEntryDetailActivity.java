package com.ijoomer.components.sobipro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.SeekBar;

import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.library.sobipro.SobiproCategoriesDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListenerWithCacheInfo;
import com.smart.framework.CustomAlertNeutral;

/**
 * This Class Contains All Method Related To SobiproEntryDetailActivity.
 * 
 * @author tasol
 * 
 */
public class SobiproEntryDetailActivity extends SobiproMasterActivity {
	ArrayList<HashMap<String, String>> entryArrayList;
	private int IN_POS;
	private String IN_PAGELAYOUT;
	private final String PAGENO = "pageNO";
	private ViewPager viewPager;
	private EntryDetailAdapter adapter;
	private SobiproCategoriesDataProvider dataProvider;
	String IN_TABLE;
	ArrayList<String> IN_ENTRY_ID_ARRAY;
	int IN_ENTRY_INDEX;
	private SeekBar proSeekBar;
	private ArrayList<String> pageLayouts;
	private String IN_FEATUREDFIRST = "No";

	/**
	 * Overrides method
	 */
	@Override
	public int setLayoutId() {
		return R.layout.sobipro_entry_detail;
	}

	@Override
	public void initComponents() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		adapter = new EntryDetailAdapter(getSupportFragmentManager());
		pageLayouts = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.sobipro_pageLayout)));
		getIntentData();
	}

	@Override
	public void onResume() {
		super.onResume();

		if (IjoomerApplicationConfiguration.isReloadRequired) {
			final int currentItem = viewPager.getCurrentItem();

			dataProvider = new SobiproCategoriesDataProvider(SobiproEntryDetailActivity.this);
			entryArrayList = dataProvider.getEntriesFromCache(IN_TABLE, IN_ENTRY_ID_ARRAY.get(viewPager.getCurrentItem()));
			dataProvider.restorePagingSettings();
			dataProvider.setPageNo(Integer.parseInt(entryArrayList.get(0).get(PAGENO)));

			switch (pageLayouts.indexOf(IN_PAGELAYOUT)) {
			case 2:
				proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
				dataProvider.getRestaurantEntries(entryArrayList.get(0).get(SECTIONID), getLatitude(), getLongitude(), IN_FEATUREDFIRST, new WebCallListenerWithCacheInfo() {

					@Override
					public void onProgressUpdate(int progressCount) {
						proSeekBar.setProgress(progressCount);
					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2, int pageNo, int pageLimit,
							boolean fromCache) {

						try {
							if (responseCode == 200 && !fromCache) {

								if (IN_TABLE.equalsIgnoreCase(SOBIPROFAVOURITEENTRIES)) {
									entryArrayList = dataProvider.getEntriesFromCache(SOBIPRO_RESTAURANT_ENTRIES, IN_ENTRY_ID_ARRAY.get(viewPager.getCurrentItem()));
									dataProvider.addToFavourite(entryArrayList, IN_PAGELAYOUT);
								}

								viewPager.setAdapter(adapter);
								viewPager.setCurrentItem(currentItem);

							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				});
				IjoomerApplicationConfiguration.setReloadRequired(false);
				break;

			default:
				proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
				dataProvider.getEntries(entryArrayList.get(0).get(SECTIONID), entryArrayList.get(0).get(CATID), IN_FEATUREDFIRST, new WebCallListenerWithCacheInfo() {

					@Override
					public void onProgressUpdate(int progressCount) {
						proSeekBar.setProgress(progressCount);
					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2, int pageNo, int pageLimit,
							boolean fromCache) {
						try {
							if (responseCode == 200 && !fromCache) {
								entryArrayList = dataProvider.getEntriesFromCache(SOBIPROENTRIES, IN_ENTRY_ID_ARRAY.get(viewPager.getCurrentItem()));
								if (IN_TABLE.equalsIgnoreCase(SOBIPROFAVOURITEENTRIES)) {
									entryArrayList = dataProvider.getEntriesFromCache(SOBIPROENTRIES, IN_ENTRY_ID_ARRAY.get(viewPager.getCurrentItem()));
									dataProvider.addToFavourite(entryArrayList, IN_PAGELAYOUT);
								}

								viewPager.setAdapter(adapter);
								viewPager.setCurrentItem(currentItem);

							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						IjoomerApplicationConfiguration.setReloadRequired(false);
					}
				});

				break;

			}

		}

	}

	@Override
	public void prepareViews() {
		if (IN_ENTRY_ID_ARRAY != null) {
			viewPager.setAdapter(adapter);
			viewPager.setCurrentItem(IN_ENTRY_INDEX);
		} else {
			IjoomerUtilities.getCustomOkDialog(getScreenCaption(), getString(getResources().getIdentifier("code204", "string", getPackageName())), getString(R.string.ok),
					R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {
						@Override
						public void NeutralMethod() {

						}
					});
		}
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
			IN_ENTRY_INDEX = getIntent().getIntExtra("IN_ENTRY_INDEX", 0);
			IN_ENTRY_ID_ARRAY = getIntent().getStringArrayListExtra("IN_ENTRY_ID_ARRAY");
			IN_TABLE = getIntent().getStringExtra("IN_TABLE");
			IN_POS = getIntent().getIntExtra("IN_POS", 0);
			IN_PAGELAYOUT = getIntent().getStringExtra("IN_PAGELAYOUT");
			IN_FEATUREDFIRST = getIntent().getStringExtra("IN_FEATUREDFIRST");
		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	/**
	 * Custom class Adapter
	 */
	private class EntryDetailAdapter extends FragmentStatePagerAdapter {

		public EntryDetailAdapter(FragmentManager fm) {
			super(fm);
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public Fragment getItem(int pos) {
			System.gc();
			switch (pageLayouts.indexOf(IN_PAGELAYOUT)) {
			case 1:
				return new SobiproCarEntryDetailFragment(IN_ENTRY_ID_ARRAY.get(pos), IN_TABLE, IN_POS, IN_PAGELAYOUT);
			case 2:
				return new SobiproRestaurantEntryDetailFragment(IN_ENTRY_ID_ARRAY.get(pos), IN_TABLE, IN_PAGELAYOUT);
			default:
				return new SobiproEntryDetailFragment(IN_ENTRY_ID_ARRAY.get(pos), IN_TABLE, IN_POS, IN_PAGELAYOUT);

			}
		}

		@Override
		public int getCount() {
			return IN_ENTRY_ID_ARRAY.size();
		}
	}

}
