package com.ijoomer.src;



import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.widget.SeekBar;

import com.ijoomer.common.classes.IjoomerMenuDataProvider;
import com.ijoomer.common.classes.IjoomerMenuMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;

/**
 * This Class Contains All Method Related To IcmsCategoryActivity.
 * 
 * @author tasol
 * 
 */
public class IjoomerMenuActivity extends IjoomerMenuMaster implements IjoomerMenuTagHolder {
	private ArrayList<HashMap<String, String>> menuData;
	private JSONArray menuItems;
	private String menuID;
	private String menuDisplay;
	private JSONObject IN_ITEMDATA;
	private IjoomerMenuDataProvider dataProvider;

	/**
	 * Overrides methods
	 */
	@Override
	public int setLayoutId() {
		return R.layout.ijoomer_menu;
	}

	@Override
	public void initComponents() {
		dataProvider = new IjoomerMenuDataProvider(IjoomerMenuActivity.this);
		getIntentData();

	}

	@Override
	public void prepareViews() {
		try {
			if (menuID != null) {
				final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
				dataProvider.getSubMenu(menuID, new WebCallListener() {

					@Override
					public void onProgressUpdate(int progressCount) {
						if (proSeekBar != null) {
							proSeekBar.setProgress(progressCount);
						}
					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						try {
							if (responseCode == 200) {
								menuData = data1;
								menuItems = new JSONArray(menuData.get(0).get("menuitem"));
								if (menuDisplay.equalsIgnoreCase(GRID))
									addFragment(R.id.lnrFragment, new IjoomerMenuGridFragment(menuItems));
								else
									addFragment(R.id.lnrFragment, new IjoomerMenuListFragment(menuItems));
							} else {

								IjoomerUtilities.getCustomOkDialog(getString(R.string.articles),
										getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok),
										R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

											@Override
											public void NeutralMethod() {

											}
										});
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setActionListeners() {

	}

	private void getIntentData() {
		try {
			JSONObject IN_OBJ = new JSONObject(getIntent().getStringExtra("IN_OBJ"));
			IN_ITEMDATA = new JSONObject(IN_OBJ.getString(ITEMDATA));
			menuID = IN_ITEMDATA.getString(MENUID);
			menuDisplay = IN_ITEMDATA.getString(DISPLAY);

		} catch (Exception e) {

		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("ON resume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		System.out.println("ON pause");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("ON destroy");
	}
}