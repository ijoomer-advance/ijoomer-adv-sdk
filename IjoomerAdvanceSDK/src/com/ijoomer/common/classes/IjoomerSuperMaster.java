package com.ijoomer.common.classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;

import com.androidquery.AQuery;
import com.facebook.Session;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.caching.IjoomerCachingConstants;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.components.jbolochat.JBoloChatManager;
import com.ijoomer.components.jomsocial.JomProfileActivity;
import com.ijoomer.custom.interfaces.IjoomerSharedPreferences;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerCheckBox;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.menubuilder.MenuDrawer;
import com.ijoomer.oauth.IjoomerOauth;
import com.ijoomer.src.IjoomerHomeActivity;
import com.ijoomer.src.IjoomerLoginActivity;
import com.ijoomer.src.IjoomerMenuActivity;
import com.ijoomer.src.IjoomerSplashActivity;
import com.ijoomer.src.R;
import com.ijoomer.weservice.IjoomerWebService;
import com.ijoomer.weservice.WebCallListener;
import com.smart.android.framework.SmartAndroidActivity;
import com.smart.framework.AlertMagnatic;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartApplication;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

/**
 * This Class Contains All Method Related To IjoomerSuperMaster.
 * 
 * @author tasol
 * 
 */
public abstract class IjoomerSuperMaster extends SmartAndroidActivity implements IjoomerSharedPreferences {

	private AQuery androidQuery;

	private ArrayList<SmartListItem> listDataSideMenu = new ArrayList<SmartListItem>();

	private static String screenCaption;
	private final String MENUITEM = "menuitem";
	private final String TAB = "tab";
	private final String TAB_ACTIVE = "tab_active";
	private final String ITEMVIEW = "itemview";
	private final String ITEMDATA = "itemdata";
	private final String ITEMCAPTION = "itemcaption";
	private final String ICON = "icon";
	private String imgPath;
	private static boolean isSideMenuOpen = false;
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private GoogleCloudMessaging gcm;

	/**
	 * Constructor
	 */
	public IjoomerSuperMaster() {
		IjoomerCachingConstants.unNormalizeFields = CoreCachingConstants.getUnnormlizeFields();
		if (getSmartApplication().readSharedPreferences().getString(SP_LOGIN_REQ_OBJECT, "").length() > 0) {
			setOptionMenu(R.menu.ijoomer_menu);
		} else {
			setOptionMenu(0);
		}
		IjoomerUtilities.mSmartAndroidActivity = this;
		setApplicationOrientation(SCREEN_ORIENTATION_PORTRAIT);
		IjoomerApplicationConfiguration.setDefaultConfiguration(this);
		androidQuery = new AQuery(this);
	}

	/**
	 * Overrides method
	 */

	@Override
	public void loadHeaderComponents() {
		try {
			getScreenRootView().getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {

					Rect r = new Rect();
					getScreenRootView().getWindowVisibleDisplayFrame(r);

					final FrameLayout f = (FrameLayout) findViewById(123);
					int heightDiff = getScreenRootView().getRootView().getHeight() - (r.bottom - r.top);
					if (heightDiff > 100) {
						getFooterView().setVisibility(View.GONE);
						getBottomAdvertiseView().setVisibility(View.GONE);
						f.setVisibility(View.VISIBLE);

						f.setOnTouchListener(new OnTouchListener() {

							@Override
							public boolean onTouch(View v, MotionEvent event) {
								if (isKeyboardHideOnOutsideTouch()) {

									hideSoftKeyboard();
									return true;
								} else {
									return false;
								}
							}
						});
					} else {
						if (getFooterView().getVisibility() == View.GONE && f.getVisibility() == View.VISIBLE && !isSideMenuOpen) {
							getBottomAdvertiseView().setVisibility(View.VISIBLE);
							FrameLayout ff = (FrameLayout) findViewById(123);
							ff.setVisibility(View.GONE);
							ff.setOnTouchListener(null);

							try {
								if (IjoomerGlobalConfiguration.hasTabBar(IjoomerSuperMaster.this, IjoomerScreenHolder.aliasScreens.get(getClass().getSimpleName()))) {
									getFooterView().setVisibility(View.VISIBLE);
								} else {
									if (!IjoomerScreenHolder.aliasScreens.containsKey(getClass().getSimpleName()) && IjoomerMenus.getInstance().getTabBarData() != null) {
										getFooterView().setVisibility(View.VISIBLE);
									} else {
										getFooterView().setVisibility(View.GONE);
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			});
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.ic_menu_logout) {
			logout();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
        if(getString(R.string.http_access_allow).equals("true")){
            AQuery.setAuthHeader(getB64Auth(SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(SP_HTTP_ACCESSS_USERNAME,""),SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(SP_HTTP_ACCESSS_PASSWORD,"")));
        }
		IjoomerCachingConstants.unNormalizeFields = CoreCachingConstants.getUnnormlizeFields();
		enableGCM();

		try {
			applySideMenu();
			applyTabMenu();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (IjoomerApplicationConfiguration.isReloadRequired()) {
				IjoomerApplicationConfiguration.setDefaultConfiguration(this);
				IjoomerApplicationConfiguration.setReloadRequired(true);
			} else {
				IjoomerApplicationConfiguration.setDefaultConfiguration(this);
			}
			IjoomerUtilities.mSmartAndroidActivity = this;
			isSideMenuOpen = false;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

    private String getB64Auth (String userName,String password) {
        String source=userName+":"+ password;
        String ret="Basic "+ Base64.encodeToString(source.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
        return ret;
    }

	@Override
	public View setBottomAdvertisement() {
		// return IjoomerAdManager.newInstance().getBottomAdvertisement(this);
		return null;
	}

	@Override
	public View setTopAdvertisement() {
		return null;
	}

	@Override
	public void setOnLoadAdvertisement() {
		IjoomerAdManager.getInstance().getOnLoadAdvertisement(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onBackPressed() {
		final int drawerState = mMenuDrawer.getDrawerState();
		if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
			mMenuDrawer.closeMenu();
			return;
		}
		super.onBackPressed();
	}

	/**
	 * This method used to get screen caption.
	 * 
	 * @return represented {@link String}
	 */
	public String getScreenCaption() {
		return screenCaption;
	}

	/**
	 * This method used to set screen caption.
	 * 
	 * @param screenCaption
	 *            represented screen caption
	 */
	@SuppressWarnings("static-access")
	public void setScreenCaption(String screenCaption) {
		this.screenCaption = screenCaption;
	}

	/**
	 * This method used to set image uri.
	 * 
	 * @return represented {@link Uri}
	 */
	public Uri setImageUri() {
		// Store image in dcim
		File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
		Uri imgUri = Uri.fromFile(file);
		this.imgPath = file.getAbsolutePath();
		return imgUri;
	}

	/**
	 * This method used to get Image path.
	 * 
	 * @return
	 */
	public String getImagePath() {
		return imgPath;
	}

	/**
	 * This method used to get absolute path from uri.
	 * 
	 * @param uri
	 *            represented uri
	 * @return represented {@link String}
	 */
	public String getAbsolutePath(Uri uri) {
		String[] projection = { MediaColumns.DATA };
		@SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else
			return null;
	}

	/**
	 * This method used to decode file from string path.
	 * 
	 * @param path
	 *            represented path
	 * @return represented {@link Bitmap}
	 */
	public Bitmap decodeFile(String path) {
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, o);
			// The new size we want to scale to
			final int REQUIRED_SIZE = 70;

			// Find the correct scale value. It should be the power of 2.
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
				scale *= 2;

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeFile(path, o2);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * This method used to decode file from uri path.
	 * 
	 * @param path
	 *            represented path
	 * @return represented {@link Bitmap}
	 */
	public Bitmap decodeFile(Uri path) {
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;

			BitmapFactory.decodeFile(getAbsolutePath(path), o);

			// The new size we want to scale to
			final int REQUIRED_SIZE = 70;

			// Find the correct scale value. It should be the power of 2.
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
				scale *= 2;

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeFile(getAbsolutePath(path), o2);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * This method used to logout from twitter.
	 */
	public void twitterLogout() {
		getSmartApplication().writeSharedPreferences(SP_TWITTER_TOKEN, null);
		getSmartApplication().writeSharedPreferences(SP_TWITTER_SECRET_TOKEN, null);
	}

	/**
	 * This method used to logout from app.
	 */
	public void logout() {
		IjoomerUtilities.getConfirmDialog(getString(R.string.logout), getString(R.string.logout_message), getString(R.string.yes), getString(R.string.no), true, new AlertMagnatic() {

			@Override
			public void PositiveMethod(DialogInterface dialog, int id) {

				final SeekBar progressBar = IjoomerUtilities.getLoadingDialog(getString(R.string.loging_out));
				IjoomerOauth.getInstance(IjoomerSuperMaster.this).logout(new WebCallListener() {

					@Override
					public void onProgressUpdate(int progressCount) {
						progressBar.setProgress(progressCount);
					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						if (responseCode == 200) {
							getSmartApplication().writeSharedPreferences(SP_PASSWORD, null);
							getSmartApplication().writeSharedPreferences(SP_ISLOGOUT, true);
							getSmartApplication().writeSharedPreferences(SP_LOGIN_REQ_OBJECT, null);
							getSmartApplication().writeSharedPreferences(SP_ISFACEBOOKLOGIN, false);
							
							twitterLogout();

							try {
								Session.getActiveSession().closeAndClearTokenInformation();
								
								if(IjoomerGlobalConfiguration.isEnableJbolo()){
									JBoloChatManager.getInstance(IjoomerSuperMaster.this).stopPolling();
								}
							} catch (Throwable e) {
								e.printStackTrace();
							}
							
							Intent intent = new Intent("clearStackActivity");
							intent.setType("text/plain");
							sendBroadcast(intent);
							IjoomerWebService.cookies = null;
							
							loadNew(IjoomerLoginActivity.class, IjoomerSuperMaster.this, true);
						} else {
							IjoomerUtilities.getCustomOkDialog(getString(R.string.logout), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())),
									getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

								@Override
								public void NeutralMethod() {

								}
							});
						}
					}
				});
			}

			@Override
			public void NegativeMethod(DialogInterface dialog, int id) {

			}
		});

	}

	/**
	 * This method used to hide soft keyboard.
	 */
	public void hideSoftKeyboard() {
		try {
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method used to show soft keyboard.
	 */
	public void showSoftKeyboard() {
		try {
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.toggleSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method used to do ellipsize to textview.
	 * 
	 * @param tv
	 *            represented TextView do ellipsize
	 * @param maxLine
	 *            represented max line to show
	 */
	public void doEllipsize(final IjoomerTextView tv, final int maxLine) {
		ViewTreeObserver vto = tv.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {

				ViewTreeObserver obs = tv.getViewTreeObserver();
				obs.removeGlobalOnLayoutListener(this);
				if (maxLine <= 0) {
					int lineEndIndex = tv.getLayout().getLineEnd(0);
					String text = tv.getText().subSequence(0, lineEndIndex - 3) + "...";
					tv.setText(text);
				} else if (tv.getLineCount() >= maxLine) {
					int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
					String text = tv.getText().subSequence(0, lineEndIndex - 3) + "...";
					tv.setText(text);
				}
			}
		});
	}

	/**
	 * This method used to convert json to map.
	 * 
	 * @param object
	 *            represented json object
	 * @return represented {@link Map<String, String>}
	 * @throws JSONException
	 *             represented {@link JSONException}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, String> jsonToMap(JSONObject object) throws JSONException {
		Map<String, String> map = new HashMap();
		Iterator keys = object.keys();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			map.put(key, fromJson(object.get(key)).toString());
		}
		return map;
	}

	/**
	 * This method used to convert json to Object.
	 * 
	 * @param json
	 *            represented json object
	 * @return represented {@link Object}
	 * @throws JSONException
	 *             represented {@link JSONException}
	 */
	private Object fromJson(Object json) throws JSONException {
		if (json == JSONObject.NULL) {
			return null;
		} else if (json instanceof JSONObject) {
			return jsonToMap((JSONObject) json);
		} else if (json instanceof JSONArray) {
			return toList((JSONArray) json);
		} else {
			return json;
		}
	}

	/**
	 * This method used to convert json array to List.
	 * 
	 * @param array
	 *            represented json array
	 * @return represented {@link List}
	 * @throws JSONException
	 *             represented {@link JSONException}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List toList(JSONArray array) throws JSONException {
		List list = new ArrayList();
		int size = array.length();
		for (int i = 0; i < size; i++) {
			list.add(fromJson(array.get(i)));
		}
		return list;
	}

	/**
	 * This method used to show side menu.
	 */
	public void showSideMenu() {

		ArrayList<HashMap<String, String>> sideMenuData;
		sideMenuData = IjoomerGlobalConfiguration.getSideMenu(this, IjoomerScreenHolder.aliasScreens.get(getClass().getSimpleName()));
		if (sideMenuData == null || sideMenuData.size() <= 0) {
			sideMenuData = IjoomerMenus.getInstance().getSideMenuData();
		}

		IjoomerMenus.getInstance().setSideMenuData(sideMenuData);
		final View sideMenuView = LayoutInflater.from(this).inflate(R.layout.ijoomer_sidemenu, null);

		final ListView sideMenuList = (ListView) sideMenuView.findViewById(R.id.sideMenuList);
		prepareList(sideMenuData);
		final SmartListAdapterWithHolder listAdapter = getSideMenuListAdapter();
		sideMenuList.setAdapter(listAdapter);

		sideMenuList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				JSONObject obj = (JSONObject) listAdapter.getItem(arg2).getValues().get(0);
				launchActivity(obj);
			}
		});
		mMenuDrawer.setMenuView(sideMenuView);
	}

	/**
	 * This method used to show tab bar.
	 */
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
			((ViewGroup) getFooterView().getChildAt(0)).addView(tabbar, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
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
						itemId = null;
					}

					try {
						intentItemId = new JSONObject(getIntent().getStringExtra("IN_OBJ")).getString(ITEMDATA);
					} catch (Exception e) {
						intentItemId = null;
					}

					if (itemId == null && (intentItemId == null || intentItemId.length() <= 0)) {
						itemId = item.getString(ITEMVIEW);
						intentItemId = IjoomerScreenHolder.aliasScreens.get(getClass().getSimpleName()) == null ? IjoomerMenus.getInstance().getSelectedScreenName() : IjoomerScreenHolder.aliasScreens
								.get(getClass().getSimpleName());
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
							androidQuery.id((ImageView) lnrItem.getChildAt(0)).image(IjoomerGlobalConfiguration.getMoreIcon(this).get(0).get(TAB_ACTIVE), true, true, getDeviceWidth(), 0);

						} else {
							androidQuery.id((ImageView) lnrItem.getChildAt(0)).image(IjoomerGlobalConfiguration.getMoreIcon(this).get(0).get(TAB), true, true, getDeviceWidth(), 0);

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
							try {
								androidQuery.id((ImageView) lnrItem.getChildAt(0)).image(item.getString(TAB), true, true, getDeviceWidth(), 0);
							} catch (Exception e2) {
							}
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
						intentItemId = IjoomerScreenHolder.aliasScreens.get(getClass().getSimpleName()) == null ? IjoomerMenus.getInstance().getSelectedScreenName() : IjoomerScreenHolder.aliasScreens
								.get(getClass().getSimpleName());
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

	/**
	 * List adapter for side menu.
	 * 
	 * @return represented {@link SmartListAdapterWithHolder}
	 */
	private SmartListAdapterWithHolder getSideMenuListAdapter() {
		SmartListAdapterWithHolder listAdapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.ijoomer_sidemenu_listitem, listDataSideMenu, new ItemView() {

			@Override
			public View setItemView(int position, View v, SmartListItem item, ViewHolder holder) {
				holder.imgMenuItemicon = (ImageView) v.findViewById(R.id.imgMenuItemicon);
				holder.txtMenuItemCaption = (IjoomerTextView) v.findViewById(R.id.txtMenuItemCaption);

				final JSONObject obj = (JSONObject) item.getValues().get(0);

				if (obj.has("logout")) {
					holder.txtMenuItemCaption.setText(getString(R.string.logout));
					holder.imgMenuItemicon.setImageResource(R.drawable.logout);
				} else {
					try {
						holder.txtMenuItemCaption.setText(obj.getString(ITEMCAPTION));

						if (obj.has(ICON)) {
							androidQuery.id(holder.imgMenuItemicon).image(obj.getString(ICON), true, true, getDeviceWidth(), 0);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				return v;
			}

			@Override
			public View setItemView(int position, View v, SmartListItem item) {
				return null;
			}

		});
		return listAdapterWithHolder;
	}

	/**
	 * This method used to prepare list for side menu.
	 * 
	 * @param data
	 *            represented side menu list
	 */
	private void prepareList(ArrayList<HashMap<String, String>> data) {
		listDataSideMenu.clear();
		int size = data.size();
		if (data != null && size > 0) {
			for (int i = 0; i < size; i++) {
				try {
					JSONArray array = new JSONArray(data.get(i).get(MENUITEM));
					int len = array.length();
					for (int j = 0; j < len; j++) {
						JSONObject objItem = array.getJSONObject(j);

						if (objItem.getString(ITEMVIEW).equals("Login") && (getSmartApplication().readSharedPreferences().getString(SP_LOGIN_REQ_OBJECT, null)) != null) {
							objItem.put("logout", "logout");
						}

						if (!objItem.has(ICON)) {
							ArrayList<HashMap<String, String>> iconData = IjoomerGlobalConfiguration.getSideMenuIcon(this, objItem.getString(ITEMVIEW));
							if (iconData != null && iconData.size() > 0) {
								objItem.put(ICON, iconData.get(0).get(ICON));
							}
						}
						SmartListItem item = new SmartListItem();
						item.setItemLayout(R.layout.ijoomer_sidemenu_listitem);
						ArrayList<Object> obj = new ArrayList<Object>();
						obj.add(objItem);
						item.setValues(obj);
						listDataSideMenu.add(item);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * This method used to apply side menu.
	 */
	public void applySideMenu() {
		try {
			if (IjoomerGlobalConfiguration.hasSideMenu(this, IjoomerScreenHolder.aliasScreens.get(getClass().getSimpleName()))) {
				ArrayList<HashMap<String, String>> sideMenuData;
				sideMenuData = IjoomerGlobalConfiguration.getSideMenu(this, IjoomerScreenHolder.aliasScreens.get(getClass().getSimpleName()));
				if (sideMenuData == null || sideMenuData.size() <= 0) {
					sideMenuData = IjoomerMenus.getInstance().getSideMenuData();
				}

				IjoomerMenus.getInstance().setSideMenuData(sideMenuData);
				((LinearLayout) getHeaderView().findViewById(R.id.lnrSideMenu)).setVisibility(View.VISIBLE);
				((ImageView) getHeaderView().findViewById(R.id.imgSideMenu)).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mMenuDrawer.toggleMenu(true);

					}
				});
				showSideMenu();
			} else {
				if (!IjoomerScreenHolder.aliasScreens.containsKey(getClass().getSimpleName()) && IjoomerMenus.getInstance().getSideMenuData() != null) {
					((LinearLayout) getHeaderView().findViewById(R.id.lnrSideMenu)).setVisibility(View.VISIBLE);
					((ImageView) getHeaderView().findViewById(R.id.imgSideMenu)).setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mMenuDrawer.toggleMenu(true);
						}
					});
					showSideMenu();
				} else {
					((LinearLayout) getHeaderView().findViewById(R.id.lnrSideMenu)).setVisibility(View.GONE);
					mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_NONE);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_NONE);
		}
	}

	public void applySideMenu(String screenName) {
		try {
			if (IjoomerGlobalConfiguration.hasSideMenu(this, IjoomerScreenHolder.aliasScreens.get(screenName))) {
				ArrayList<HashMap<String, String>> sideMenuData;
				sideMenuData = IjoomerGlobalConfiguration.getSideMenu(this, IjoomerScreenHolder.aliasScreens.get(screenName));
				if (sideMenuData == null || sideMenuData.size() <= 0) {
					sideMenuData = IjoomerMenus.getInstance().getSideMenuData();
				}

				IjoomerMenus.getInstance().setSideMenuData(sideMenuData);
				((LinearLayout) getHeaderView().findViewById(R.id.lnrSideMenu)).setVisibility(View.VISIBLE);
				((ImageView) getHeaderView().findViewById(R.id.imgSideMenu)).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mMenuDrawer.toggleMenu(true);

					}
				});
				showSideMenu();
			} else {
				if (!IjoomerScreenHolder.aliasScreens.containsKey(screenName) && IjoomerMenus.getInstance().getSideMenuData() != null) {
					((LinearLayout) getHeaderView().findViewById(R.id.lnrSideMenu)).setVisibility(View.VISIBLE);
					((ImageView) getHeaderView().findViewById(R.id.imgSideMenu)).setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mMenuDrawer.toggleMenu(true);
						}
					});
					showSideMenu();
				} else {
					((LinearLayout) getHeaderView().findViewById(R.id.lnrSideMenu)).setVisibility(View.GONE);
					mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_NONE);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_NONE);
		}
	}

	/**
	 * This method used to apply tab menu.
	 */
	public void applyTabMenu() {
		try {
			if (IjoomerGlobalConfiguration.hasTabBar(this, IjoomerScreenHolder.aliasScreens.get(getClass().getSimpleName()))) {
				showTabBar();
			} else {
				if (!IjoomerScreenHolder.aliasScreens.containsKey(getClass().getSimpleName()) && IjoomerMenus.getInstance().getTabBarData() != null) {
					showTabBar();
				} else {
					getFooterView().setVisibility(View.GONE);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method used to show more pop menu.
	 * 
	 * @param moreData
	 *            represented {@link Object} list
	 * @param v
	 *            represented view
	 */
	@SuppressWarnings("deprecation")
	public void showMorePopup(ArrayList<Object> moreData, View v) {

		ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
		int size = moreData.size();
		for (int j = 0; j < size; j++) {
			SmartListItem item = new SmartListItem();
			item.setItemLayout(R.layout.ijoomer_more_menu_listitem);
			ArrayList<Object> obj = new ArrayList<Object>();
			obj.add(moreData.get(j));
			item.setValues(obj);
			listData.add(item);
		}
		final SmartListAdapterWithHolder listAdapter = getMoreMenuListAdapter(listData);

		Rect r = new Rect();
		v.getDrawingRect(r);

		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = layoutInflater.inflate(R.layout.ijoomer_more_popup, null);
		final ListView lstMore = (ListView) layout.findViewById(R.id.lstMore);

		lstMore.setAdapter(listAdapter);
		final PopupWindow popup = new PopupWindow(this);
		popup.setAnimationStyle(R.style.animation);
		popup.setContentView(layout);
		popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		popup.setWidth(getDeviceWidth() / 2);
		popup.setFocusable(true);
		popup.setBackgroundDrawable(new BitmapDrawable(getResources()));
		popup.showAtLocation(layout, Gravity.RIGHT | Gravity.BOTTOM, 0, r.bottom);
		if (popup.isShowing())

			lstMore.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					popup.dismiss();
					JSONObject obj = (JSONObject) listAdapter.getItem(arg2).getValues().get(0);

					try {
						launchActivity(obj);
						popup.dismiss();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

	}

	/**
	 * List adapter more list
	 * 
	 * @param moreListData
	 *            represented {@link SmartListItem} list
	 * @return represented {@link SmartListAdapterWithHolder}
	 */
	@Override
	public SmartListAdapterWithHolder getMoreMenuListAdapter(ArrayList<SmartListItem> moreListData) {

		SmartListAdapterWithHolder listAdapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.ijoomer_more_menu_listitem, moreListData, new ItemView() {

			@Override
			public View setItemView(int position, View v, SmartListItem item, ViewHolder holder) {
				holder.imgMenuItemicon = (ImageView) v.findViewById(R.id.imgMenuItemicon);
				holder.txtMenuItemCaption = (IjoomerTextView) v.findViewById(R.id.txtMenuItemCaption);

				final JSONObject obj = (JSONObject) item.getValues().get(0);
				String itemId = null;
				String intentItemId = null;

				try {
					itemId = new JSONObject(obj.getString(ITEMDATA)).toString();
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

				try {
					holder.txtMenuItemCaption.setText(obj.getString(ITEMCAPTION));
					if (itemId == null && (intentItemId == null || intentItemId.length() <= 0)) {
						itemId = obj.getString(ITEMVIEW);
						intentItemId = IjoomerScreenHolder.aliasScreens.get(getClass().getSimpleName()) == null ? IjoomerMenus.getInstance().getSelectedScreenName()
								: IjoomerScreenHolder.aliasScreens.get(getClass().getSimpleName());
					} else {

						if (itemId == null) {
							itemId = obj.getString(ITEMVIEW);
						}

						if (intentItemId == null) {
							intentItemId = IjoomerMenus.getInstance().getSelectedScreenName();
						}
					}

					if (itemId.equals(intentItemId)) {
						androidQuery.id(holder.imgMenuItemicon).image(obj.getString(TAB_ACTIVE), true, true, getDeviceWidth(), 0);
					} else {
						androidQuery.id(holder.imgMenuItemicon).image(obj.getString(TAB), true, true, getDeviceWidth(), 0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				return v;
			}

			@Override
			public View setItemView(int position, View v, SmartListItem item) {
				return null;
			}

		});
		return listAdapterWithHolder;
	}

	/**
	 * This method used to get slide menu difference.
	 * 
	 * @param varient
	 *            represented variant
	 * @return represented {@link Integer}
	 */
	public int getSlideDifference(int varient) {
		Resources resources = getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		return ((metrics.densityDpi * varient) / 160);
	}

	/**
	 * This method used to get privacy code from privacy.
	 * 
	 * @param privacy
	 *            represented privacy name
	 * @return represented {@link String}
	 */
	public String getPrivacyCode(String privacy) {

		ArrayList<String> list = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.wall_post_type)));

		if (privacy.equals(list.get(0))) {
			return "0";
		} else if (privacy.equals(list.get(1))) {
			return "20";
		} else if (privacy.equals(list.get(2))) {
			return "30";
		} else if (privacy.equals(list.get(3))) {
			return "40";
		}
		return "0";
	}

	/**
	 * This method used to get privacy from privacy code.
	 * 
	 * @param privacy
	 *            represented privacy code
	 * @return represented {@link String}
	 */
	public String getPrivacyString(String privacy) {

		ArrayList<String> list = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.wall_post_type)));

		if (privacy.equals("0")) {
			return list.get(0);
		} else if (privacy.equals("20")) {
			return list.get(1);
		} else if (privacy.equals("30")) {
			return list.get(2);
		} else if (privacy.equals("40")) {
			return list.get(3);
		}
		return list.get(0);
	}

	/**
	 * This method used to get privacy index from privacy name or code.
	 * 
	 * @param privacy
	 *            represented privacy code or name
	 * @return represented {@link Integer}
	 */
	public int getPrivacyIndex(String privacy) {

		ArrayList<String> list = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.wall_post_type)));

		if (privacy.equals("0") || privacy.equals(list.get(0))) {
			return 0;
		} else if (privacy.equals("20") || privacy.equals(list.get(1))) {
			return 1;
		} else if (privacy.equals("30") || privacy.equals(list.get(2))) {
			return 2;
		} else if (privacy.equals("40") || privacy.equals(list.get(3))) {
			return 3;
		}
		return 0;
	}

	/**
	 * This method used to get privacy from at index.
	 * 
	 * @param privacyAtIndex
	 *            represented privacy index at
	 * @return represented {@link String}
	 */
	public String getPrivacyStringAtIndex(int privacyAtIndex) {

		ArrayList<String> list = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.wall_post_type)));

		return list.get(privacyAtIndex);
	}

	/**
	 * This method used to get privacy code from at index.
	 * 
	 * @param privacyAtIndex
	 *            represented privacy index at
	 * @return represented {@link String}
	 */
	public String getPrivacyCodeAtIndex(int privacyAtIndex) {

		ArrayList<String> list = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.wall_post_type)));

		String privacy = list.get(privacyAtIndex);

		if (privacy.equals(list.get(0))) {
			return "0";
		} else if (privacy.equals(list.get(1))) {
			return "20";
		} else if (privacy.equals(list.get(2))) {
			return "30";
		} else if (privacy.equals(list.get(3))) {
			return "40";
		}
		return "0";
	}

	/**
	 * This method used to show UrlSettingDialog.
	 */
	public void showUrlSettingDialog() {
		final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
		dialog.setContentView(R.layout.ijoomer_urlsetting_dialog);
		final IjoomerEditText edtDomain = (IjoomerEditText) dialog.findViewById(R.id.edtDomain);
		final IjoomerCheckBox chkSsl = (IjoomerCheckBox) dialog.findViewById(R.id.chkSsl);
		final IjoomerButton btnTestConnection = (IjoomerButton) dialog.findViewById(R.id.btnTestConnection);

		final LinearLayout lnr_welcome_first = (LinearLayout) dialog.findViewById(R.id.lnr_welcome_first);
		final LinearLayout lnr_welcome_second = (LinearLayout) dialog.findViewById(R.id.lnr_welcome_second);

		IjoomerButton btnYes = (IjoomerButton) dialog.findViewById(R.id.btnYes);
		IjoomerButton btnNo = (IjoomerButton) dialog.findViewById(R.id.btnNo);

		btnTestConnection.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideSoftKeyboard();

				if (edtDomain.getText().toString().trim().length() > 0) {

					final SeekBar progressBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
					new AsyncTask<Void, Void, Boolean>() {

						String domain = "";

						@Override
						protected void onPreExecute() {
							super.onPreExecute();
							domain = edtDomain.getText().toString();
							if (chkSsl.isChecked()) {
								domain = "https://" + domain + "";
							} else {
								domain = "http://" + domain + "";
							}
							if (!domain.endsWith("/")) {
								domain += "/";
							}
						}

						@Override
						protected Boolean doInBackground(Void... params) {
							final ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
							final NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
							if (netInfo != null && netInfo.isConnected()) {
								try {
									URL url = new URL(domain);
									final HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
									urlc.setRequestProperty("User-Agent", "Android Application");
									urlc.setRequestProperty("Connection", "close");
									urlc.setConnectTimeout(10 * 1000);
									urlc.connect();
									if (urlc.getResponseCode() == 200) {
										return true;
									}
								} catch (Throwable e) {
									e.printStackTrace();
									runOnUiThread(new Runnable() {
										public void run() {
											try {
												progressBar.setProgress(100);
												IjoomerUtilities.getCustomOkDialog("Test failure", "Unknown Host !", getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

													@Override
													public void NeutralMethod() {
													}
												});

											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									});
								}
							} else {
								runOnUiThread(new Runnable() {
									public void run() {
										progressBar.setProgress(100);
										IjoomerUtilities.getCustomOkDialog("Test failure", "No Internet Connection!", getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

											@Override
											public void NeutralMethod() {
											}
										});

									}
								});
							}
							return false;
						}

						@Override
						protected void onPostExecute(Boolean result) {
							super.onPostExecute(result);

							if (result) {
								new IjoomerGlobalConfiguration(IjoomerSuperMaster.this).testUrl(domain, new WebCallListener() {

									@Override
									public void onProgressUpdate(int progressCount) {
										progressBar.setProgress(progressCount);
									}

									@Override
									public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
										if (responseCode == 200) {
											try {
												String[] extentions = getStringArray(((JSONObject) data2).getString("extensions"));
												String str = "Installed Components :\n";
												int size = extentions.length;
												for (int i = 0; i < size - 1; i++) {
													str += extentions[i] + " , ";
												}
												str += extentions[size - 1];
												IjoomerUtilities.getCustomOkDialog("Test Successful", str, getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

													@Override
													public void NeutralMethod() {
														IjoomerApplicationConfiguration.setDomainName(domain);
														new IjoomerCaching(IjoomerSuperMaster.this).resetDataBase();
														getSmartApplication().writeSharedPreferences(SP_ICON_PRELOADER, false);
														goAhed();
													}
												});
											} catch (Exception e) {
												e.printStackTrace();
											}
										} else {
											IjoomerUtilities
											.getCustomOkDialog(
													"Test failure",
													"Sorry <b>Ijoomer Advance </b> not configure on your website, for more info visit :<br> <a href=\"http://www.ijoomer.com\"><b>www.ijoomer.com</b></a> !",
													getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

														@Override
														public void NeutralMethod() {
														}
													});
										}
									}
								});
							}
						}
					}.execute();

				} else {
					edtDomain.setError(getString(R.string.validation_value_required));
				}

			}
		});

		btnNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				dialog.cancel();
				getSmartApplication().writeSharedPreferences(SP_URL_SETTING, true);
				goAhed();
			}
		});
		btnYes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				getSmartApplication().writeSharedPreferences(SP_URL_SETTING, true);
				lnr_welcome_second.setVisibility(View.VISIBLE);
				lnr_welcome_first.setVisibility(View.GONE);
			}
		});
		dialog.show();
	}

	/**
	 * This method used to string array from string with (,) separated.
	 * 
	 * @param value
	 *            represented value
	 * @return represented {@link String} array
	 */
	public String[] getStringArray(final String value) {
		try {
			if (value.length() > 0) {
				final JSONArray temp = new JSONArray(value);
				int length = temp.length();
				if (length > 0) {
					final String[] recipients = new String[length];
					for (int i = 0; i < length; i++) {
						recipients[i] = temp.getString(i).equalsIgnoreCase("null") ? "1" : temp.getString(i);
					}
					return recipients;
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	/**
	 * This method used to get advertisement.
	 * 
	 * @param addId
	 *            represented add id
	 * @return represented {@link View}
	 */
	// public View getAdvertisement(String addId) {
	//
	// if (addId != null && addId.trim().length() > 0) {
	// LinearLayout addLayout = new LinearLayout(this);
	// addLayout.setLayoutParams(new
	// LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
	// LinearLayout.LayoutParams.WRAP_CONTENT));
	// AdView adView = new AdView(this);
	// adView.setAdUnitId(addId);
	// adView.setAdSize(AdSize.BANNER);
	// addLayout.addView(adView);
	// AdRequest.Builder builder = new AdRequest.Builder();
	// builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
	// adView.loadAd(builder.build());
	// return addLayout;
	// } else {
	// return null;
	// }
	//
	// }

	/**
	 * This method used to launch activity.
	 * 
	 * @param obj
	 *            represented json object predefined activity
	 */
	public void launchActivity(JSONObject obj) {
		try {

			setScreenCaption(obj.getString(ITEMCAPTION));
			final String className = IjoomerScreenHolder.originalScreens.get(obj.getString(ITEMVIEW));

			IjoomerMenus.getInstance().setTabBarData(null);
			IjoomerMenus.getInstance().setSideMenuData(null);

			final Intent intent = new Intent();
			if (!(this.getClass().getName().equalsIgnoreCase(className)))
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setClassName(IjoomerSuperMaster.this, className);
			intent.putExtra("IN_USERID", "0");
			if (obj.getString(ITEMVIEW).equals("Login") && (getSmartApplication().readSharedPreferences().getString(SP_LOGIN_REQ_OBJECT, "")).length() > 0) {
				logout();
				return;
			} else if (obj.getString(ITEMVIEW).equals("Registration")) {
				logout();
				return;
			}

			else if (obj.getString(ITEMVIEW).equals("Web")) {
				try {
					intent.setClassName(IjoomerSuperMaster.this, className);
					intent.putExtra("url", new JSONObject(obj.getString(ITEMDATA)).getString("url") + "");
					startActivity(intent);
					return;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			intent.putExtra("IN_OBJ", obj.toString());
			startActivity(intent);
			if ((!(this instanceof IjoomerHomeActivity)) && (!(this instanceof IjoomerMenuActivity))) {
				finish();
			}
		} catch (Exception e) {
			tong(getString(R.string.sdk_error));
			e.printStackTrace();
		}
	}

	/**
	 * This method used to restart app splash activity and clear all activity
	 * from task.
	 */
	private void goAhed() {
		Intent intent = new Intent("clearStackActivity");
		intent.setType("text/plain");
		sendBroadcast(intent);
		IjoomerWebService.cookies = null;
		loadNew(IjoomerSplashActivity.class, IjoomerSuperMaster.this, true);
	}

	/**
	 * This method used to enable GCM.
	 */
	public void enableGCM() {

		try {
			if (checkPlayServices()) {
				gcm = GoogleCloudMessaging.getInstance(this);
				String regid = getRegistrationId();

				if (regid.isEmpty()) {
					registerInBackground();
				}
			} else {
				Log.i("GCM", "No valid Google Play Services APK found.");
			}

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private String getRegistrationId() {

		String registrationId = getSmartApplication().readSharedPreferences().getString(SP_GCM_REGID, "");
		if (registrationId.isEmpty()) {
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = getSmartApplication().readSharedPreferences().getInt(SP_GCM_REGID, Integer.MIN_VALUE);
		int currentVersion = getAppVersion();
		if (registeredVersion != currentVersion) {
			return "";
		}
		return registrationId;
	}

	private int getAppVersion() {
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				if (!getSmartApplication().readSharedPreferences().getBoolean(SP_GCM_ERROR_DIALOG, false)) {
					GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
					getSmartApplication().writeSharedPreferences(SP_GCM_ERROR_DIALOG, true);
				}
			} else {
				Log.i("GCM", "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	private void registerInBackground() {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {

				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(IjoomerSuperMaster.this);
					}
					String regid = gcm.register(IjoomerApplicationConfiguration.getGCMProjectId());

					storeRegistrationId(regid);
				} catch (Exception ex) {

				}
				return null;
			}
		}.execute();
	}

	private void storeRegistrationId(String regId) {
		int appVersion = getAppVersion();
		getSmartApplication().writeSharedPreferences(SP_GCM_REGID, regId);
		getSmartApplication().writeSharedPreferences(SP_GCM_APP_VERSION, appVersion);

	}

	/**
	 * This method used to add fragment to given layout id.
	 * 
	 * @param layoutId
	 *            represented layout id
	 * @param fragment
	 *            represented fragment
	 */
	public void addFragment(int layoutId, Fragment fragment) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(layoutId, fragment);
		ft.commit();
	}

	@Override
	public void initTheme() {
	}

	public Uri getVideoPlayURI(String videoUrl) {

		String video_id = "";
		if (videoUrl != null && videoUrl.trim().length() > 0) {
			System.out.println("VIDEOURL"+videoUrl);
			//String expression = "(?:http|https|)(?::\\/\\/|)(?:www.|)(?:youtu\\.be\\/|youtube\\.com(?:\\/embed\\/|\\/v\\/|\\/watch\\?v=|\\/ytscreeningroom\\?v=|\\/feeds\\/api\\/videos\\/|\\/user\\S*[^\\w\\-\\s]|\\S*[^\\w\\-\\s]))([\\w\\-]{11})[a-z0-9;:@?&%=+\\/\\$_.-]*";
			String expression = "(?:http|https|)(?::\\/\\/|)(?:www.|m.)(?:youtu\\.be\\/|youtube\\.com(?:\\/embed\\/|\\/v\\/|\\/watch\\?v=|\\/ytscreeningroom\\?v=|\\/feeds\\/api\\/videos\\/|\\/user\\S*[^\\w\\-\\s]|\\S*[^\\w\\-\\s]))([\\w\\-]{11})[a-z0-9;:@?&%=+\\/\\$_.-]*";
			CharSequence input = videoUrl;
			Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(input);
			if (matcher.matches()) {
				System.out.println("DATA"+matcher.group(1));
				String groupIndex1 = matcher.group(1);
				if (groupIndex1 != null && groupIndex1.length() == 11)
					video_id = groupIndex1;
			}
		}

		System.out.println("VIDEOID"+video_id);
		if (video_id.trim().length() > 0) {
			return Uri.parse("ytv://" + video_id);
		} else {
			return Uri.parse("mp4://" + videoUrl);
		}
	}

	@SuppressWarnings("resource")
	public void exportDatabse(String databaseName) {
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {
				String currentDBPath = "//data//" + getPackageName() + "//databases//" + databaseName + "";
				String backupDBPath = "backupname.db";
				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(sd, backupDBPath);

				if (currentDB.exists()) {
					FileChannel src = new FileInputStream(currentDB).getChannel();
					FileChannel dst = new FileOutputStream(backupDB).getChannel();
					dst.transferFrom(src, 0, src.size());
					src.close();
					dst.close();
				}
			}
		} catch (Exception e) {

		}
	}

	/**
	 * This method used to goto user profile.
	 * 
	 * @param userID
	 *            represented user id
	 */
	public void gotoProfile(final String userID) {	
		try {
			loadNew(JomProfileActivity.class, this, false, "IN_USERID", userID);
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}
}
