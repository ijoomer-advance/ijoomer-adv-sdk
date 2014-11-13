package com.ijoomer.components.sobipro;

import com.ijoomer.src.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This Class Contains All Method Related To SobiproEntriesActivity.
 * 
 * @author tasol
 * 
 */

public class SobiproEntriesActivity extends SobiproMasterActivity {
	private String IN_PARENT_ID, IN_SECTION_ID, IN_PAGELAYOUT;
	private int IN_POS;
	private String IN_FEATUREDFIRST = "No";
	private ArrayList<String> pageLayouts;

	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.sobipro_entries;
	}

	@Override
	public void initComponents() {
		pageLayouts = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.sobipro_pageLayout)));
	}

	@Override
	public void prepareViews() {
		getIntentData();
		switch (pageLayouts.indexOf(IN_PAGELAYOUT)) {
		case 1:
			addFragment(R.id.lnrFragment, new SobiproCarEntriesListFragment(IN_SECTION_ID, IN_PARENT_ID, IN_POS, IN_PAGELAYOUT, IN_FEATUREDFIRST));
			break;
		default:
			addFragment(R.id.lnrFragment, new SobiproEntriesListFragment(IN_SECTION_ID, IN_PARENT_ID, IN_POS, IN_PAGELAYOUT, IN_FEATUREDFIRST));
			break;

		}

	}

	@Override
	public void setActionListeners() {

	}

	/**
	 * This method used to get intent data.
	 */

	private void getIntentData() {
		try {
			IN_SECTION_ID = getIntent().getStringExtra("IN_SECTION_ID");
			IN_PARENT_ID = getIntent().getStringExtra("IN_PARENT_ID");
			IN_PAGELAYOUT = getIntent().getStringExtra("IN_PAGELAYOUT");
			IN_POS = getIntent().getIntExtra("IN_POS", 0);
			IN_FEATUREDFIRST = getIntent().getStringExtra("IN_FEATUREDFIRST");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
