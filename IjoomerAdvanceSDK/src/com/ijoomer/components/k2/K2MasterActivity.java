package com.ijoomer.components.k2;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.androidquery.AQuery;
import com.ijoomer.caching.IjoomerCachingConstants;
import com.ijoomer.common.classes.IjoomerMenus;
import com.ijoomer.common.classes.IjoomerScreenHolder;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.src.R;

/**
 * This Class Contains All Method Related To K2MasterActivity.
 * 
 * @author tasol
 * 
 */
public abstract class K2MasterActivity extends IjoomerSuperMaster implements K2TagHolder {

	private final String MENUITEM = "menuitem";
	private final String TAB = "tab";
	private final String TAB_ACTIVE = "tab_active";
	private final String ITEMVIEW = "itemview";
	private final String ITEMCAPTION = "itemcaption";
	private final String ITEMDATA = "itemdata";
	private AQuery androidQuery;

	public K2MasterActivity() {
		super();
		androidQuery = new AQuery(this);
		IjoomerCachingConstants.unNormalizeFields = K2CachingConstants.getUnnormlizeFields();
	}


    /**
	 * Overrides methods
	 */


	@Override
	public int setFooterLayoutId() {
		return R.layout.k2_footer;
	}

	@Override
	public int setHeaderLayoutId() {
		return R.layout.k2_header;
	}

	@Override
	protected void onResume() {
		super.onResume();
		IjoomerCachingConstants.unNormalizeFields = K2CachingConstants.getUnnormlizeFields();
	}

	@Override
	public int setTabBarDividerResId() {
		return 0;
	}

	@Override
	public int setTabItemLayoutId() {
		return 0;
	}

	@Override
	public String[] setTabItemNames() {
		return null;
	}

	@Override
	public int[] setTabItemOffDrawables() {
		return null;
	}

	@Override
	public int[] setTabItemOnDrawables() {
		return null;
	}

	@Override
	public int[] setTabItemPressDrawables() {
		return null;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public View setTopAdvertisement() {
		return null;
	}

	@Override
	public View setBottomAdvertisement() {
		return null;
	}
	@Override
	public void showTabBar() {


		try {
			boolean flag = false;
			boolean isMoreSelected = true;

			ArrayList<HashMap<String, String>> menuData;
			menuData = IjoomerGlobalConfiguration.getTabBar(this, IjoomerScreenHolder.aliasScreens.get(getClass().getSimpleName()));

			if (menuData == null || menuData.size() <= 0) {
				menuData = IjoomerMenus.getInstance().getTabBarData();
				flag = true;
			}

			IjoomerMenus.getInstance().setTabBarData(menuData);
			JSONArray tabItems = new JSONArray(menuData.get(0).get(MENUITEM));

			LayoutInflater inflater = LayoutInflater.from(this);

			LinearLayout tabbar = new LinearLayout(this);
			tabbar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			tabbar.setGravity(Gravity.CENTER);
			((ViewGroup) getFooterView().getChildAt(0)).removeAllViews();
			((ViewGroup) getFooterView().getChildAt(0)).addView(tabbar, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT));
			int tabLength = tabItems.length() > 5 ? 5 : tabItems.length();

			if (tabLength <= 0) {
				getFooterView().setVisibility(View.GONE);
				return;
			}
			if (tabItems.length() > 5) {
				for (int i = 0; i < tabLength; i++) {
					JSONObject item = tabItems.getJSONObject(i);
					if (!item.has(TAB) && !item.has(TAB_ACTIVE)) {
						ArrayList<HashMap<String, String>> tabData = IjoomerGlobalConfiguration.getTabIcons(this, item.getString(ITEMVIEW));
						if (tabData != null && tabData.size() > 0) {
							item.put(TAB, tabData.get(0).get(TAB));
							item.put(TAB_ACTIVE, tabData.get(0).get(TAB_ACTIVE));
						}
					}

					LinearLayout lnrItem = (LinearLayout) inflater.inflate(R.layout.ijoomer_tab_item, null);
					lnrItem.setId(i);
					lnrItem.setTag(item);
					if (IjoomerApplicationConfiguration.tabbarWithoutCaption)
						((IjoomerTextView) lnrItem.getChildAt(1)).setVisibility(View.GONE);
					if (IjoomerApplicationConfiguration.tabbarWithoutImage)
						((ImageView) lnrItem.getChildAt(0)).setVisibility(View.GONE);
					((IjoomerTextView) lnrItem.getChildAt(1)).setText(item.getString(ITEMCAPTION));

					lnrItem.setOnClickListener(new OnClickListener() {

						@SuppressWarnings("unchecked")
						@Override
						public void onClick(View v) {

							try {
								JSONObject obj = (JSONObject) v.getTag();
								launchActivity(obj);
							} catch (Exception e) {
								ArrayList<Object> moreData = (ArrayList<Object>) v.getTag();
								showMorePopup(moreData, v);

							}
						}
					});
					tabbar.addView(lnrItem, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));

					String itemId = null;
					String intentItemId = null;

					try {
						itemId = new JSONObject(item.getString(ITEMDATA)).toString();
					} catch (Exception e) {
						e.printStackTrace();
						itemId = null;
					}

					try {
						intentItemId = new JSONObject(getIntent().getStringExtra("IN_OBJ")).getString(ITEMDATA);
					} catch (Exception e) {
						e.printStackTrace();
						intentItemId = null;
					}

					if (itemId == null && (intentItemId == null || intentItemId.length() <= 0)) {
						itemId = item.getString(ITEMVIEW);
						intentItemId = IjoomerScreenHolder.aliasScreens.get(getClass().getSimpleName()) == null ? IjoomerMenus.getInstance().getSelectedScreenName()
								: IjoomerScreenHolder.aliasScreens.get(getClass().getSimpleName());
					} else {

						if (itemId == null) {
							itemId = item.getString(ITEMVIEW);
						}

						if (intentItemId == null) {
							intentItemId = IjoomerMenus.getInstance().getSelectedScreenName();
						}
					}
					if (i == (tabLength - 1)) {
						((IjoomerTextView) lnrItem.getChildAt(1)).setText(getString(R.string.more));
						if (isMoreSelected) {
							if (getClass().getSimpleName().toLowerCase().contains("k2")) {
								androidQuery.id((ImageView) lnrItem.getChildAt(0)).image(getResources().getDrawable(R.drawable.k2more_tab_active));
							} else {
								androidQuery.id((ImageView) lnrItem.getChildAt(0)).image(IjoomerGlobalConfiguration.getMoreIcon(this).get(0).get(TAB_ACTIVE), true, true,
										getDeviceWidth(), 0);
							}

						} else {
							if (getClass().getSimpleName().toLowerCase().contains("k2")) {
								androidQuery.id((ImageView) lnrItem.getChildAt(0)).image(getResources().getDrawable(R.drawable.k2more_tab));
							} else {
								androidQuery.id((ImageView) lnrItem.getChildAt(0)).image(IjoomerGlobalConfiguration.getMoreIcon(this).get(0).get(TAB), true, true,
										getDeviceWidth(), 0);
							}

						}
						ArrayList<Object> moreData = new ArrayList<Object>();
						int size = tabItems.length();
						for (int j = i; j < size; j++) {
							JSONObject moreItem = tabItems.getJSONObject(j);

							if ((itemId != null && moreItem.getString(ITEMVIEW).equals(intentItemId))) {
								IjoomerMenus.getInstance().setSelectedScreenName(intentItemId);
							} else if (flag && (moreItem.getString(ITEMVIEW).equals(IjoomerMenus.getInstance().getSelectedScreenName()))) {
								IjoomerMenus.getInstance().setSelectedScreenName(intentItemId);
							}

							if (!moreItem.has(TAB) && !moreItem.has(TAB_ACTIVE)) {
								ArrayList<HashMap<String, String>> moreTabData = IjoomerGlobalConfiguration.getTabIcons(this, moreItem.getString(ITEMVIEW));
								if (moreTabData != null && moreTabData.size() > 0) {
									moreItem.put(TAB, moreTabData.get(0).get(TAB));
									moreItem.put(TAB_ACTIVE, moreTabData.get(0).get(TAB_ACTIVE));
								}
							}
							moreData.add(moreItem);
						}
						lnrItem.setTag(moreData);
					} else {
						try {

							if (intentItemId.equals(itemId)) {
								IjoomerMenus.getInstance().setSelectedScreenName(intentItemId);
								androidQuery.id((ImageView) lnrItem.getChildAt(0)).image(item.getString(TAB_ACTIVE), true, true, getDeviceWidth(), 0);
								isMoreSelected = false;
							} else if (flag && (intentItemId.equals(itemId))) {
								IjoomerMenus.getInstance().setSelectedScreenName(item.getString(ITEMVIEW));
								androidQuery.id((ImageView) lnrItem.getChildAt(0)).image(item.getString(TAB_ACTIVE), true, true, getDeviceWidth(), 0);
								isMoreSelected = false;
							} else {
								androidQuery.id((ImageView) lnrItem.getChildAt(0)).image(item.getString(TAB), true, true, getDeviceWidth(), 0);
							}
						} catch (Exception e) {
							e.printStackTrace();
							androidQuery.id((ImageView) lnrItem.getChildAt(0)).image(item.getString(TAB), true, true, getDeviceWidth(), 0);
						}
					}

				}
			} else {
				for (int i = 0; i < tabLength; i++) {
					JSONObject item = tabItems.getJSONObject(i);
					if (!item.has(TAB) && !item.has(TAB_ACTIVE)) {
						ArrayList<HashMap<String, String>> tabData = IjoomerGlobalConfiguration.getTabIcons(this, item.getString(ITEMVIEW));
						if (tabData != null && tabData.size() > 0) {
							item.put(TAB, tabData.get(0).get(TAB));
							item.put(TAB_ACTIVE, tabData.get(0).get(TAB_ACTIVE));
						}
					}

					LinearLayout lnrItem = (LinearLayout) inflater.inflate(R.layout.ijoomer_tab_item, null);
					lnrItem.setId(i);
					lnrItem.setTag(item);
					if (IjoomerApplicationConfiguration.tabbarWithoutCaption)
						((IjoomerTextView) lnrItem.getChildAt(1)).setVisibility(View.GONE);
					if (IjoomerApplicationConfiguration.tabbarWithoutImage)
						((ImageView) lnrItem.getChildAt(0)).setVisibility(View.GONE);

					((IjoomerTextView) lnrItem.getChildAt(1)).setText(item.getString(ITEMCAPTION));

					String itemId = null;
					String intentItemId = null;

					try {
						itemId = new JSONObject(item.getString(ITEMDATA)).toString();
					} catch (Exception e) {
						e.printStackTrace();
						itemId = null;
					}

					try {
						intentItemId = new JSONObject(getIntent().getStringExtra("IN_OBJ")).getString(ITEMDATA);
					} catch (Exception e) {
						e.printStackTrace();
						intentItemId = null;
					}

					if (itemId == null && (intentItemId == null || intentItemId.length() <= 0)) {
						itemId = item.getString(ITEMVIEW);
						intentItemId = IjoomerScreenHolder.aliasScreens.get(getClass().getSimpleName()) == null ? IjoomerMenus.getInstance().getSelectedScreenName()
								: IjoomerScreenHolder.aliasScreens.get(getClass().getSimpleName());
					} else {

						if (itemId == null) {
							itemId = item.getString(ITEMVIEW);
						}

						if (intentItemId == null) {
							intentItemId = IjoomerMenus.getInstance().getSelectedScreenName();
						}
					}

					lnrItem.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							JSONObject obj = (JSONObject) v.getTag();

							try {
								launchActivity(obj);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					tabbar.addView(lnrItem, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
					try {

						if (intentItemId.equals(itemId)) {
							IjoomerMenus.getInstance().setSelectedScreenName(intentItemId);
							androidQuery.id((ImageView) lnrItem.getChildAt(0)).image(item.getString(TAB_ACTIVE), true, true, getDeviceWidth(), 0);
						} else if (flag && (intentItemId.equals(itemId))) {
							IjoomerMenus.getInstance().setSelectedScreenName(intentItemId);
							androidQuery.id((ImageView) lnrItem.getChildAt(0)).image(item.getString(TAB_ACTIVE), true, true, getDeviceWidth(), 0);
						} else {
							androidQuery.id((ImageView) lnrItem.getChildAt(0)).image(item.getString(TAB), true, true, getDeviceWidth(), 0);
						}
					} catch (Exception e) {
						e.printStackTrace();
						androidQuery.id((ImageView) lnrItem.getChildAt(0)).image(item.getString(TAB), true, true, getDeviceWidth(), 0);
					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	
	}

}
