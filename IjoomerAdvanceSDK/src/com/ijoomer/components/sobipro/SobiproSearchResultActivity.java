package com.ijoomer.components.sobipro;

import com.ijoomer.src.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To SobiproEntriesActivity.
 * 
 * @author tasol
 * 
 */

public class SobiproSearchResultActivity extends SobiproMasterActivity {

	private String optionCriteria, searchKey, section_id;
	private ArrayList<HashMap<String, String>> searchField;
	private int IN_POS;
	private String IN_FEATUREDFIRST = "Yes";

	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.sobipro_entries;
	}

	@Override
	public void initComponents() {

	}

	@Override
	public void prepareViews() {

		getIntentData();

		addFragment(R.id.lnrFragment, new SobiproSearchResultListFragment(optionCriteria, searchKey, section_id, searchField, IN_POS, IN_FEATUREDFIRST));
	}

	@Override
	public void setActionListeners() {
	}

	/**
	 * This method is used to get intent data
	 */

	@SuppressWarnings("unchecked")
	public void getIntentData() {
		try {
			IN_POS = getIntent().getIntExtra("IN_POS", 0);
			optionCriteria = getIntent().getStringExtra("IN_OPTIONCRITERIA");
			searchKey = getIntent().getStringExtra("IN_SEARCH_KEY");
			section_id = getIntent().getStringExtra("IN_SECTION_ID");
			searchField = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("IN_SEARCHFIELD");
			IN_FEATUREDFIRST = getIntent().getStringExtra("IN_FEATUREDFIRST");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
