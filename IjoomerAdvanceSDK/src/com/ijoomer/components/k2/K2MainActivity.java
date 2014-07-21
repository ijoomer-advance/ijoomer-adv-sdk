package com.ijoomer.components.k2;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.widget.ProgressBar;

import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.library.k2.k2MainDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;

/**
 * This Class Contains All Method Related To K2MainActivity.
 * 
 * @author tasol
 * 
 */
public class K2MainActivity extends K2MasterActivity {

	private K2MainNewsFragment newsFragment;
	private K2MainCatalogFragment catalogFragment;
	private K2MainDirectoriesFragment directoriesFragment;
	private K2MainListFragment listFragment;
	private K2MainGridFragment gridFragment;
	private K2MainSingleFragment singleCategoryFragment;
	private k2MainDataProvider provider;

	private String IN_PAGE_LAYOUT;
	private String IN_CATGEORY_TYPE;
	private String IN_MENUID;
	private String ITEMID = "itemid";

	/**
	 * Overrides method
	 */

	@Override
	public int setLayoutId() {
		return R.layout.k2_main;
	}

	@Override
	public void initComponents() {
		getIntentData();
		provider = new k2MainDataProvider(this);
	}

	@Override
	public void prepareViews() {
		luancherFragment();
	}

	@Override
	public void setActionListeners() {

	}

	@Override
	public void onBackPressed() {
		if (singleCategoryFragment != null) {
			if (singleCategoryFragment.categoryStack.size() > 0) {
				singleCategoryFragment.lnrk2SingleCategory.removeAllViews();
				singleCategoryFragment.updateFragment(false, singleCategoryFragment.categoryStack.pop(), false);
			} else {
				super.onBackPressed();
			}
		} else {
			super.onBackPressed();
		}
	}

	/**
	 * Class method
	 */

	private void getIntentData() {
		try {
			IN_PAGE_LAYOUT = ((JSONObject) new JSONObject(getIntent().getStringExtra("IN_OBJ")).getJSONObject("itemdata")).getString(PAGELAYOUT) == null ? ""
					: ((JSONObject) new JSONObject(getIntent().getStringExtra("IN_OBJ")).getJSONObject("itemdata")).getString(PAGELAYOUT);
			IN_MENUID = new JSONObject(getIntent().getStringExtra("IN_OBJ")).getString(ITEMID) == null ? "0" : new JSONObject(getIntent().getStringExtra("IN_OBJ"))
					.getString(ITEMID);
			IN_CATGEORY_TYPE = ((JSONObject) new JSONObject(getIntent().getStringExtra("IN_OBJ")).getJSONObject("itemdata")).getString(CATID) == null ? ""
					: ((JSONObject) new JSONObject(getIntent().getStringExtra("IN_OBJ")).getJSONObject("itemdata")).getString(CATID);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method used to launcher k2 fragment.
	 */
	private void luancherFragment() {

		if (IN_PAGE_LAYOUT.length() > 0 && IN_PAGE_LAYOUT.equalsIgnoreCase(CATALOGDETAIL) || IN_PAGE_LAYOUT.equalsIgnoreCase(NEWSDETAIL)
				|| IN_PAGE_LAYOUT.equalsIgnoreCase(SIMPLEDETAIL)) {
			getItemsFromServer();
		} else if (IN_CATGEORY_TYPE.length() > 0 && IN_CATGEORY_TYPE.split(",").length > 1) {
			if (IN_PAGE_LAYOUT.length() > 0 && IN_PAGE_LAYOUT.equalsIgnoreCase(CATALOG)) {
				catalogFragment = new K2MainCatalogFragment();
				addFragment(R.id.lnrFragment, catalogFragment);
			} else if (IN_PAGE_LAYOUT.length() > 0 && IN_PAGE_LAYOUT.equalsIgnoreCase(NEWS)) {
				newsFragment = new K2MainNewsFragment();
				addFragment(R.id.lnrFragment, newsFragment);
			} else if (IN_PAGE_LAYOUT.length() > 0 && IN_PAGE_LAYOUT.equalsIgnoreCase(DIRECTORY)) {
				directoriesFragment = new K2MainDirectoriesFragment();
				addFragment(R.id.lnrFragment, directoriesFragment);
			} else if (IN_PAGE_LAYOUT.length() > 0 && IN_PAGE_LAYOUT.equalsIgnoreCase(SIMPLELIST)) {
				listFragment = new K2MainListFragment();
				addFragment(R.id.lnrFragment, listFragment);
			} else if (IN_PAGE_LAYOUT.length() > 0 && IN_PAGE_LAYOUT.equalsIgnoreCase(SCROLLINGGRID)) {
				gridFragment = new K2MainGridFragment();
				addFragment(R.id.lnrFragment, gridFragment);
			}

		} else {
			if (IN_PAGE_LAYOUT.length() > 0 && IN_PAGE_LAYOUT.equalsIgnoreCase(SIMPLELIST)) {
				listFragment = new K2MainListFragment();
				addFragment(R.id.lnrFragment, listFragment);
			} else if (IN_PAGE_LAYOUT.length() > 0 && IN_PAGE_LAYOUT.equalsIgnoreCase(SCROLLINGGRID)) {
				gridFragment = new K2MainGridFragment();
				addFragment(R.id.lnrFragment, gridFragment);
			} else {
				singleCategoryFragment = new K2MainSingleFragment();
				addFragment(R.id.lnrFragment, singleCategoryFragment);
			}
		}

	}

	private void getItemsFromServer() {
		provider.getItemsDetail(IN_MENUID, new WebCallListener() {
			ProgressBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				if (responseCode == 200) {
					if (IN_PAGE_LAYOUT.length() > 0 && IN_PAGE_LAYOUT.equalsIgnoreCase(CATALOGDETAIL)) {
						try {
							loadNew(K2CatalogDetailsActivity.class, K2MainActivity.this, true, "IN_MENUID", IN_MENUID, "IN_ITEMS_LIST", data1, "IN_CURRENT_ITEM_SELECTED", 0,
									"IN_OBJ", getIntent().getStringExtra("IN_OBJ"));
						} catch (Throwable e) {
							e.printStackTrace();
						}
					} else if (IN_PAGE_LAYOUT.length() > 0 && IN_PAGE_LAYOUT.equalsIgnoreCase(NEWSDETAIL)) {
						try {
							loadNew(K2NewsDetailsActivity.class, K2MainActivity.this, true, "IN_MENUID", IN_MENUID, "IN_ITEMS_LIST", data1, "IN_CURRENT_ITEM_SELECTED", 0,
									"IN_OBJ", getIntent().getStringExtra("IN_OBJ"));
						} catch (Throwable e) {
							e.printStackTrace();
						}
					} else if (IN_PAGE_LAYOUT.length() > 0 && IN_PAGE_LAYOUT.equalsIgnoreCase(SIMPLEDETAIL)) {
						try {
							loadNew(K2ItemsDetailsActivity.class, K2MainActivity.this, true, "IN_MENUID", IN_MENUID, "IN_ITEMS_LIST", data1, "IN_CURRENT_ITEM_SELECTED", 0,
									"IN_OBJ", getIntent().getStringExtra("IN_OBJ"));
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				} else {
					responseErrorMessageHandler(responseCode, true);
				}
			}

			@Override
			public void onProgressUpdate(int progressCount) {
				proSeekBar.setProgress(progressCount);
			}
		});
	}

	/**
	 * This method used to shown response message.
	 * 
	 * @param responseCode
	 *            represented response code
	 * @param finishActivityOnConnectionProblem
	 *            represented finish activity on connection problem
	 */
	private void responseErrorMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {
		IjoomerUtilities.getCustomOkDialog(getString(R.string.dialog_k2_directories), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())),
				getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

					@Override
					public void NeutralMethod() {

					}
				});
	}

}
