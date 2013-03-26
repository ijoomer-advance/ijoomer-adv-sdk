package com.ijoomer.common.classes;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
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
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
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
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionDefaultAudience;
import com.facebook.SessionLoginBehavior;
import com.facebook.internal.Utility;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.custom.interfaces.FacebookLoginHandller;
import com.ijoomer.custom.interfaces.IjoomerSharedPreferences;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerCheckBox;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerHorizontalScroll;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.oauth.IjoomerOauth;
import com.ijoomer.src.IjoomerLoginActivity;
import com.ijoomer.src.IjoomerSplashActivity;
import com.ijoomer.src.R;
import com.ijoomer.weservice.IjoomerWebService;
import com.ijoomer.weservice.WebCallListener;
import com.smart.android.framework.SmartAndroidActivity;
import com.smart.framework.AlertMagnatic;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

public abstract class IjoomerSuperMaster extends SmartAndroidActivity implements IjoomerSharedPreferences {

	private static final int SWIPE_MIN_DISTANCE = 50;
	private static final int SWIPE_THRESHOLD_VELOCITY = 100;
	private GestureDetector gDetector;
	private String imgPath;
	private String[] PERMISSIONS = new String[] { "publish_actions", "email" };
	private Session facebookSession;
	private boolean isFacebookShare;
	private boolean isFacebookLogin;
	private FacebookLoginHandller facebookLoginTarget;

	private String facebookSharingName;
	private String facebookSharingCaption;
	private String facebookSharingDescription;
	private String facebookSharingLink;
	private String facebookSharingPicture;
	private String facebookSharingMessage;
	private AQuery androidQuery;
	private static boolean isSideMenuOpen = false;
	private ArrayList<SmartListItem> listDataSideMenu = new ArrayList<SmartListItem>();

	private Session.OpenRequest openRequest = null;
	SeekBar proSeekBar;

	private static final int FB_SHARE = 111;
	private static final int FB_CONNECT = 222;

	private static String screenCaption;

	public String getScreenCaption() {
		return screenCaption;
	}

	public void setScreenCaption(String screenCaption) {
		this.screenCaption = screenCaption;
	}

	public IjoomerSuperMaster() {
		if (getSmartApplication().readSharedPreferences().getString(SP_LOGIN_REQ_OBJECT, "").length() > 0) {
			setOptionMenu(R.menu.ijoomer_menu);
		} else {
			setOptionMenu(0);
		}
		IjoomerUtilities.mSmartIphoneActivity = this;
		setApplicationOrientation(SCREEN_ORIENTATION_PORTRAIT);
		IjoomerApplicationConfiguration.setDefaultConfiguration();
		androidQuery = new AQuery(this);
	}

	public Uri setImageUri() {
		// Store image in dcim
		File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
		Uri imgUri = Uri.fromFile(file);
		this.imgPath = file.getAbsolutePath();
		return imgUri;
	}

	public String getImagePath() {

		return imgPath;
	}

	public String getAbsolutePath(Uri uri) {
		String[] projection = { MediaColumns.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else
			return null;
	}

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
		}
		return null;

	}

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
		}
		return null;

	}

	public void facebookLogout() {
		try {
			Session.getActiveSession().closeAndClearTokenInformation();
			facebookSession = null;
		} catch (Throwable e) {
		}
	}

	public void twitterLogout() {
		getSmartApplication().writeSharedPreferences(SP_TWITTER_TOKEN, null);
		getSmartApplication().writeSharedPreferences(SP_TWITTER_SECRET_TOKEN, null);
	}

	@Override
	public void loadHeaderComponents() {
		applySideMenu();
		try {
			getScreenRootView().getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {

					Rect r = new Rect();
					getScreenRootView().getWindowVisibleDisplayFrame(r);

					final FrameLayout f = (FrameLayout) findViewById(123);
					int heightDiff = getScreenRootView().getRootView().getHeight() - (r.bottom - r.top);
					if (heightDiff > 100) {
						// getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
						// getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
						getFooterView().setVisibility(View.GONE);
						getBottomAdvertiseView().setVisibility(View.GONE);
						f.setVisibility(View.VISIBLE);

						f.setOnTouchListener(new OnTouchListener() {

							@Override
							public boolean onTouch(View v, MotionEvent event) {
								hideSoftKeyboard();
								return true;
							}
						});
					} else {
						if (getFooterView().getVisibility() == View.GONE && f.getVisibility() == View.VISIBLE && !isSideMenuOpen) {
							// getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
							// getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
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
							}
						}
					}
				}
			});
		} catch (Throwable e) {
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.ic_menu_logout) {
			logout();
		}
		return super.onOptionsItemSelected(item);
	}

	public void logout() {
		IjoomerUtilities.getConfirmDialog(getString(R.string.logout), getString(R.string.logout_message), getString(R.string.yes), getString(R.string.no), true,
				new AlertMagnatic() {

					@Override
					public void PositiveMathod(DialogInterface dialog, int id) {
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
									getSmartApplication().writeSharedPreferences(SP_ISLOGOUT, false);
									getSmartApplication().writeSharedPreferences(SP_ISLOGOUT, true);
									getSmartApplication().writeSharedPreferences(SP_LOGIN_REQ_OBJECT, null);
									twitterLogout();
									getSmartApplication().writeSharedPreferences(SP_ISFACEBOOKLOGIN, false);
									facebookLogout();

									Intent intent = new Intent("clearStackActivity");
									intent.setType("text/plain");
									sendBroadcast(intent);
									IjoomerWebService.cookies = null;

									loadNew(IjoomerLoginActivity.class, IjoomerSuperMaster.this, true);
								} else {
									IjoomerUtilities.getCustomOkDialog(getString(R.string.logout),
											getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok),
											R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

												@Override
												public void NeutralMathod() {

												}
											});
								}
							}
						});
					}

					@Override
					public void NegativeMathod(DialogInterface dialog, int id) {

					}
				});

	}

	public void facebookSharing(final String name, final String caption, final String description, final String link, final String picture, final String message) {
		facebookSharingName = name;
		facebookSharingCaption = caption;
		facebookSharingDescription = description;
		facebookSharingLink = link;
		facebookSharingPicture = picture;
		facebookSharingMessage = message;

		isFacebookLogin = false;
		isFacebookShare = true;
		facebookSession = Session.getActiveSession();
		String applicationId = Utility.getMetadataApplicationId(getBaseContext());

		if (facebookSession == null || facebookSession.getState().isClosed()) {
			Session session = new Session.Builder(getBaseContext()).setApplicationId(applicationId).build();
			Session.setActiveSession(session);
			facebookSession = session;
		}

		if (!facebookSession.isOpened()) {
			openRequest = new Session.OpenRequest(this);

			if (openRequest != null) {
				openRequest.setRequestCode(FB_SHARE);
				openRequest.setPermissions(Arrays.asList(PERMISSIONS[0]));
				openRequest.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);
				facebookSession.openForPublish(openRequest);

			}
		} else {
			postData();
		}

	}

	public void loginWithFacebook(final FacebookLoginHandller target) {
		facebookLoginTarget = target;
		isFacebookLogin = true;
		isFacebookShare = false;
		facebookSession = Session.getActiveSession();
		String applicationId = Utility.getMetadataApplicationId(getBaseContext());

		if (facebookSession == null || facebookSession.getState().isClosed()) {
			Session session = new Session.Builder(getBaseContext()).setApplicationId(applicationId).build();
			Session.setActiveSession(session);
			facebookSession = session;
		}

		if (!facebookSession.isOpened()) {
			openRequest = new Session.OpenRequest(this);

			if (openRequest != null) {
				openRequest.setRequestCode(FB_CONNECT);
				openRequest.setDefaultAudience(SessionDefaultAudience.FRIENDS);
				openRequest.setPermissions(Arrays.asList(PERMISSIONS[1]));
				openRequest.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);
				facebookSession.openForRead(openRequest);

			}

		} else {
			facebookUserData();
		}
	}

	private void facebookUserData() {

		Bundle postParams = new Bundle();
		postParams
				.putString(
						"q",
						"select name,about_me,birthday_date,current_address,education,email,first_name,hometown_location,is_minor,last_name,middle_name,pic,pic_big,pic_cover,pic_small,pic_square,political,quotes,relationship_status,religion,sex,sports,status,timezone,tv,uid,username,verified,website,work from user where uid=me()");

		Request.Callback callback = new Request.Callback() {
			public void onCompleted(Response response) {
				hideProgressDialog();
				JSONObject json = null;
				try {
					json = response.getGraphObject().getInnerJSONObject().getJSONArray("data").getJSONObject(0);
				} catch (Throwable e1) {
					e1.printStackTrace();
				}
				FacebookRequestError error = response.getError();
				if (error != null) {
					json = new JSONObject();
					try {
						json.put("errorMessage", error.getErrorMessage());
						try {
							facebookLoginTarget.loginStatus(response.getConnection().getResponseCode(), json);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					try {
						facebookLoginTarget.loginStatus(response.getConnection().getResponseCode(), json);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};

		Request request = new Request(facebookSession, "fql", postParams, HttpMethod.GET, callback);

		RequestAsyncTask task = new RequestAsyncTask(request);
		task.execute();
		showProgressDialog(getString(R.string.facebook_user_data_fetching), this);

	}

	private void postData() {

		Bundle postParams = new Bundle();
		postParams.putString("name", facebookSharingName == null ? "" : facebookSharingName);
		postParams.putString("caption", facebookSharingCaption == null ? "" : facebookSharingCaption);
		postParams.putString("description", facebookSharingDescription == null ? "" : facebookSharingDescription);
		postParams.putString("link", facebookSharingLink == null ? "" : facebookSharingLink);
		postParams.putString("picture", facebookSharingPicture == null ? "" : facebookSharingPicture);
		postParams.putString("message", facebookSharingMessage == null ? "" : facebookSharingMessage);

		Request.Callback callback = new Request.Callback() {

			public void onCompleted(Response response) {
				hideProgressDialog();
				FacebookRequestError error = response.getError();
				if (error != null) {
					IjoomerUtilities.getCustomOkDialog(getString(R.string.facebook_share_title), error.getErrorMessage(), getString(R.string.ok), R.layout.ijoomer_ok_dialog,
							new CustomAlertNeutral() {

								@Override
								public void NeutralMathod() {

								}
							});
				} else {
					IjoomerUtilities.getCustomOkDialog(getString(R.string.facebook_share_title), getString(R.string.facebook_share_success), getString(R.string.ok),
							R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

								@Override
								public void NeutralMathod() {

								}
							});
				}
			}
		};

		Request request = new Request(facebookSession, "me/feed", postParams, HttpMethod.POST, callback);

		RequestAsyncTask task = new RequestAsyncTask(request);
		task.execute();

		showProgressDialog(getString(R.string.facebook_wall_posting), this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (resultCode == RESULT_OK) {
				if (requestCode == FB_CONNECT || requestCode == FB_SHARE) {
					Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
					if (facebookSession.isOpened()) {

						if (isFacebookLogin) {
							facebookUserData();
						}
						if (isFacebookShare) {
							postData();
						}
					}
				}
			} else {
				Session.getActiveSession().closeAndClearTokenInformation();
				facebookSession = null;
			}
		} catch (Exception e) {
		}

	}

	public void hideSoftKeyboard() {
		try {
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		} catch (Exception e) {
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			applyTabMenu();
		} catch (Exception e) {
		}

		try {
			if (IjoomerApplicationConfiguration.isReloadRequired()) {
				IjoomerApplicationConfiguration.setDefaultConfiguration();
				IjoomerApplicationConfiguration.setReloadRequired(true);
			} else {
				IjoomerApplicationConfiguration.setDefaultConfiguration();
			}
			IjoomerUtilities.mSmartIphoneActivity = this;
			isSideMenuOpen = false;
		} catch (Exception e) {
		}

	}

	@Override
	public View setBottomAdvertisement() {

		return null;
	}

	@Override
	public View setTopAdvertisement() {

		return null;
	}

	public void doEllipsize(final IjoomerTextView tv, final int maxLine) {
		ViewTreeObserver vto = tv.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

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

	public Map<String, String> jsonToMap(JSONObject object) throws JSONException {
		Map<String, String> map = new HashMap();
		Iterator keys = object.keys();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			map.put(key, fromJson(object.get(key)).toString());
		}
		return map;
	}

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

	private List toList(JSONArray array) throws JSONException {
		List list = new ArrayList();
		int size = array.length();
		for (int i = 0; i < size; i++) {
			list.add(fromJson(array.get(i)));
		}
		return list;
	}

	public void showSideMenu() {

		gDetector = new GestureDetector(new MyGesture());

		ArrayList<HashMap<String, String>> sideMenuData;
		sideMenuData = IjoomerGlobalConfiguration.getSideMenu(this, IjoomerScreenHolder.aliasScreens.get(getClass().getSimpleName()));
		if (sideMenuData == null || sideMenuData.size() <= 0) {
			sideMenuData = IjoomerMenus.getInstance().getSideMenuData();
		}

		IjoomerMenus.getInstance().setSideMenuData(sideMenuData);
		final LinearLayout sideMenu = (LinearLayout) findViewById(108);
		final IjoomerHorizontalScroll hScrollView = (IjoomerHorizontalScroll) findViewById(100);
		final View sideMenuView = LayoutInflater.from(this).inflate(R.layout.ijoomer_sidemenu, null);

		sideMenu.addView(sideMenuView, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

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
		if (!isSideMenuOpen) {

			hScrollView.post(new Runnable() {

				@Override
				public void run() {
					hScrollView.fullScroll(ScrollView.FOCUS_LEFT);
					FrameLayout f = (FrameLayout) findViewById(123);
					f.setVisibility(View.VISIBLE);
					f.setOnTouchListener(new OnTouchListener() {

						@Override
						public boolean onTouch(View v, MotionEvent event) {

							if (gDetector.onTouchEvent(event)) {

								hScrollView.post(new Runnable() {

									@Override
									public void run() {
										hScrollView.fullScroll(ScrollView.FOCUS_RIGHT);
										FrameLayout f = (FrameLayout) findViewById(123);
										f.setVisibility(View.GONE);
									}
								});
								isSideMenuOpen = false;
							}
							return true;
						}
					});
				}
			});
			isSideMenuOpen = true;
		} else {
			FrameLayout f = (FrameLayout) findViewById(123);
			f.setVisibility(View.GONE);
			hScrollView.post(new Runnable() {

				@Override
				public void run() {
					hScrollView.fullScroll(ScrollView.FOCUS_RIGHT);
				}
			});
			isSideMenuOpen = false;
		}
	}

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
			JSONArray tabItems = new JSONArray(menuData.get(0).get("menuitem"));

			LayoutInflater inflater = LayoutInflater.from(this);

			LinearLayout tabbar = new LinearLayout(this);
			tabbar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			tabbar.setGravity(Gravity.CENTER);
			((ViewGroup) getFooterView().getChildAt(0))
					.addView(tabbar, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
			int tabLength = tabItems.length() > 5 ? 5 : tabItems.length();

			if (tabItems.length() > 5) {
				for (int i = 0; i < tabLength; i++) {
					JSONObject item = tabItems.getJSONObject(i);
					ArrayList<HashMap<String, String>> tabData = IjoomerGlobalConfiguration.getTabIcons(this, item.getString("itemview"));
					if (tabData != null && tabData.size() > 0) {
						item.put("tab", tabData.get(0).get("tab"));
						item.put("tab_active", tabData.get(0).get("tab_active"));
					}

					LinearLayout lnrItem = (LinearLayout) inflater.inflate(R.layout.ijoomer_tab_item, null);
					lnrItem.setId(i);
					lnrItem.setTag(item);
					((IjoomerTextView) lnrItem.getChildAt(1)).setText(item.getString("itemcaption"));

					lnrItem.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							try {
								JSONObject obj = (JSONObject) v.getTag();
								final String className = IjoomerScreenHolder.originalScreens.get(obj.getString("itemview"));

								if (!className.equals(IjoomerSuperMaster.this.getClass().getName())) {
									launchActivity(obj);
								}
							} catch (Exception e) {
								ArrayList<Object> moreData = (ArrayList<Object>) v.getTag();
								showMorePopup(moreData, v);

							}
						}
					});
					tabbar.addView(lnrItem, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));

					String screenName = IjoomerScreenHolder.aliasScreens.get(getClass().getSimpleName());

					if (i == (tabLength - 1)) {
						((IjoomerTextView) lnrItem.getChildAt(1)).setText(getString(R.string.more));
						if (isMoreSelected) {
							androidQuery.id((ImageView) lnrItem.getChildAt(0)).image(IjoomerGlobalConfiguration.getMoreIcon(this).get(0).get("tab_active"), true, true,
									getDeviceWidth(), 0);
						} else {
							androidQuery.id((ImageView) lnrItem.getChildAt(0)).image(IjoomerGlobalConfiguration.getMoreIcon(this).get(0).get("tab"), true, true, getDeviceWidth(),
									0);
						}
						ArrayList<Object> moreData = new ArrayList<Object>();
						int size = tabItems.length();
						for (int j = i; j < size; j++) {
							JSONObject moreItem = tabItems.getJSONObject(j);
							if ((screenName != null && moreItem.getString("itemview").equals(screenName))) {
								IjoomerMenus.getInstance().setSelectedScreenName(moreItem.getString("itemview"));
							} else if (flag && (moreItem.getString("itemview").equals(IjoomerMenus.getInstance().getSelectedScreenName()))) {
								IjoomerMenus.getInstance().setSelectedScreenName(moreItem.getString("itemview"));
							}

							ArrayList<HashMap<String, String>> moreTabData = IjoomerGlobalConfiguration.getTabIcons(this, moreItem.getString("itemview"));
							if (moreTabData != null && moreTabData.size() > 0) {
								moreItem.put("tab", moreTabData.get(0).get("tab"));
								moreItem.put("tab_active", moreTabData.get(0).get("tab_active"));
							}
							moreData.add(moreItem);
						}
						lnrItem.setTag(moreData);
					} else {
						if ((screenName != null && item.getString("itemview").equals(screenName))) {
							IjoomerMenus.getInstance().setSelectedScreenName(item.getString("itemview"));
							androidQuery.id((ImageView) lnrItem.getChildAt(0)).image(item.getString("tab_active"), true, true, getDeviceWidth(), 0);
							isMoreSelected = false;
						} else if (flag && (item.getString("itemview").equals(IjoomerMenus.getInstance().getSelectedScreenName()))) {
							IjoomerMenus.getInstance().setSelectedScreenName(item.getString("itemview"));
							androidQuery.id((ImageView) lnrItem.getChildAt(0)).image(item.getString("tab_active"), true, true, getDeviceWidth(), 0);
							isMoreSelected = false;
						} else {
							androidQuery.id((ImageView) lnrItem.getChildAt(0)).image(item.getString("tab"), true, true, getDeviceWidth(), 0);
						}
					}

				}
			} else {
				for (int i = 0; i < tabLength; i++) {
					JSONObject item = tabItems.getJSONObject(i);
					ArrayList<HashMap<String, String>> tabData = IjoomerGlobalConfiguration.getTabIcons(this, item.getString("itemview"));
					if (tabData != null && tabData.size() > 0) {
						item.put("tab", tabData.get(0).get("tab"));
						item.put("tab_active", tabData.get(0).get("tab_active"));
					}

					LinearLayout lnrItem = (LinearLayout) inflater.inflate(R.layout.ijoomer_tab_item, null);
					lnrItem.setId(i);
					lnrItem.setTag(item);
					((IjoomerTextView) lnrItem.getChildAt(1)).setText(item.getString("itemcaption"));

					lnrItem.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							JSONObject obj = (JSONObject) v.getTag();

							try {
								final String className = IjoomerScreenHolder.originalScreens.get(obj.getString("itemview"));

								if (!className.equals(IjoomerSuperMaster.this.getClass().getName())) {
									launchActivity(obj);
								}
							} catch (Exception e) {
							}
						}
					});
					tabbar.addView(lnrItem, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));

					String screenName = IjoomerScreenHolder.aliasScreens.get(getClass().getSimpleName());
					if ((screenName != null && item.getString("itemview").equals(screenName))) {
						IjoomerMenus.getInstance().setSelectedScreenName(item.getString("itemview"));
						androidQuery.id((ImageView) lnrItem.getChildAt(0)).image(item.getString("tab_active"), true, true, getDeviceWidth(), 0);
					} else if (flag && (item.getString("itemview").equals(IjoomerMenus.getInstance().getSelectedScreenName()))) {
						IjoomerMenus.getInstance().setSelectedScreenName(item.getString("itemview"));
						androidQuery.id((ImageView) lnrItem.getChildAt(0)).image(item.getString("tab_active"), true, true, getDeviceWidth(), 0);
					} else {
						androidQuery.id((ImageView) lnrItem.getChildAt(0)).image(item.getString("tab"), true, true, getDeviceWidth(), 0);
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private SmartListAdapterWithHolder getSideMenuListAdapter() {
		SmartListAdapterWithHolder listAdapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.ijoomer_sidemenu_listitem, listDataSideMenu, new ItemView() {

			@Override
			public View setItemView(int position, View v, SmartListItem item, ViewHolder holder) {
				holder.imgMenuItemicon = (ImageView) v.findViewById(R.id.imgMenuItemicon);
				holder.txtMenuItemCaption = (IjoomerTextView) v.findViewById(R.id.txtMenuItemCaption);

				final JSONObject obj = (JSONObject) item.getValues().get(0);
				try {
					holder.txtMenuItemCaption.setText(obj.getString("itemcaption"));

					if (obj.has("icon")) {
						androidQuery.id(holder.imgMenuItemicon).image(obj.getString("icon"), true, true, getDeviceWidth(), 0);
					}
				} catch (Exception e) {
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

	private void prepareList(ArrayList<HashMap<String, String>> data) {
		listDataSideMenu.clear();
		int size = data.size();
		if (data != null && size > 0) {

			for (int i = 0; i < size; i++) {

				try {
					JSONArray array = new JSONArray(data.get(i).get("menuitem"));
					int len = array.length();
					for (int j = 0; j < len; j++) {
						JSONObject objItem = array.getJSONObject(j);
						ArrayList<HashMap<String, String>> iconData = IjoomerGlobalConfiguration.getSideMenuIcon(this, objItem.getString("itemview"));
						if (iconData != null && iconData.size() > 0) {
							objItem.put("icon", iconData.get(0).get("icon"));
						}
						SmartListItem item = new SmartListItem();
						item.setItemLayout(R.layout.ijoomer_sidemenu_listitem);
						ArrayList<Object> obj = new ArrayList<Object>();
						obj.add(objItem);
						item.setValues(obj);
						listDataSideMenu.add(item);
					}
				} catch (Exception e) {
				}
			}
		}

	}

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
						showSideMenu();

					}
				});
			} else {

				if (!IjoomerScreenHolder.aliasScreens.containsKey(getClass().getSimpleName()) && IjoomerMenus.getInstance().getSideMenuData() != null) {

					((LinearLayout) getHeaderView().findViewById(R.id.lnrSideMenu)).setVisibility(View.VISIBLE);
					((ImageView) getHeaderView().findViewById(R.id.imgSideMenu)).setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							showSideMenu();
						}
					});
				} else {
					((LinearLayout) getHeaderView().findViewById(R.id.lnrSideMenu)).setVisibility(View.GONE);
				}
			}
		} catch (Exception e) {
		}
	}

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
		}

	}

	private void showMorePopup(ArrayList<Object> moreData, View v) {

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
		popup.setWidth(getWindowManager().getDefaultDisplay().getWidth() / 2);
		popup.setFocusable(true);
		popup.setBackgroundDrawable(new BitmapDrawable());
		popup.showAtLocation(layout, Gravity.RIGHT | Gravity.BOTTOM, 0, r.bottom);
		if (popup.isShowing())

			lstMore.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					popup.dismiss();
					JSONObject obj = (JSONObject) listAdapter.getItem(arg2).getValues().get(0);

					try {
						final String className = IjoomerScreenHolder.originalScreens.get(obj.getString("itemview"));

						if (!className.equals(IjoomerSuperMaster.this.getClass().getName())) {
							launchActivity(obj);
						} else {
							popup.dismiss();
						}
					} catch (Exception e) {
					}
				}
			});

	}

	private SmartListAdapterWithHolder getMoreMenuListAdapter(ArrayList<SmartListItem> moreListData) {
		SmartListAdapterWithHolder listAdapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.ijoomer_more_menu_listitem, moreListData, new ItemView() {

			@Override
			public View setItemView(int position, View v, SmartListItem item, ViewHolder holder) {
				holder.imgMenuItemicon = (ImageView) v.findViewById(R.id.imgMenuItemicon);
				holder.txtMenuItemCaption = (IjoomerTextView) v.findViewById(R.id.txtMenuItemCaption);

				final JSONObject obj = (JSONObject) item.getValues().get(0);
				try {
					holder.txtMenuItemCaption.setText(obj.getString("itemcaption"));

					if (obj.getString("itemview").equals(IjoomerMenus.getInstance().getSelectedScreenName())) {
						androidQuery.id(holder.imgMenuItemicon).image(obj.getString("tab_active"), true, true, getDeviceWidth(), 0);
					} else {
						androidQuery.id(holder.imgMenuItemicon).image(obj.getString("tab"), true, true, getDeviceWidth(), 0);
					}

				} catch (Exception e) {
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

	public int getSlideDifference(int varient) {
		Resources resources = getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		return ((metrics.densityDpi * varient) / 160);
	}

	@Override
	public void onBackPressed() {
		if (isSideMenuOpen) {
			showSideMenu();
		} else {
			super.onBackPressed();
		}
	}

	private class MyGesture extends SimpleOnGestureListener {

		@Override
		public boolean onSingleTapUp(MotionEvent e) {

			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

			if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				return true;
			}
			return false;

		}

	}

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

	public String getPrivacyStringAtIndex(int privacyAtIndex) {

		ArrayList<String> list = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.wall_post_type)));

		return list.get(privacyAtIndex);
	}

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

		dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				Intent intent = new Intent("clearStackActivity");
				intent.setType("text/plain");
				sendBroadcast(intent);
				IjoomerWebService.cookies = null;
				loadNew(IjoomerSplashActivity.class, IjoomerSuperMaster.this, true);

			}
		});
		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				Intent intent = new Intent("clearStackActivity");
				intent.setType("text/plain");
				sendBroadcast(intent);
				IjoomerWebService.cookies = null;
				loadNew(IjoomerSplashActivity.class, IjoomerSuperMaster.this, true);

			}
		});

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
									runOnUiThread(new Runnable() {
										public void run() {
											try {
												progressBar.setProgress(100);
												IjoomerUtilities.getCustomOkDialog("Test failure", "Unknown Host !", getString(R.string.ok), R.layout.ijoomer_ok_dialog,
														new CustomAlertNeutral() {

															@Override
															public void NeutralMathod() {
																dialog.dismiss();
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
										IjoomerUtilities.getCustomOkDialog("Test failure", "No Internet Connection!", getString(R.string.ok), R.layout.ijoomer_ok_dialog,
												new CustomAlertNeutral() {

													@Override
													public void NeutralMathod() {
														dialog.dismiss();
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
												String str = "Installd Components :\n";
												int size = extentions.length;
												for (int i = 0; i < size; i++) {
													str += extentions[i] + " , ";
												}
												IjoomerUtilities.getCustomOkDialog("Test Successful", str, getString(R.string.ok), R.layout.ijoomer_ok_dialog,
														new CustomAlertNeutral() {

															@Override
															public void NeutralMathod() {
																IjoomerApplicationConfiguration.setDomainName(domain);
																dialog.dismiss();
															}
														});
											} catch (Exception e) {
											}
										} else {
											IjoomerUtilities
													.getCustomOkDialog(
															"Test failure",
															"Sorry <b>Ijoomer Advance </b> not configure on your website, for more info visit :<br> <a href=\"http://www.ijoomer.com\"><b>www.ijoomer.com</b></a> !",
															getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

																@Override
																public void NeutralMathod() {
																	dialog.dismiss();
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

		// edtDomain.setText(IjoomerApplicationConfiguration.getDomainName());
		btnNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				dialog.dismiss();
				getSmartApplication().writeSharedPreferences(SP_URL_SETTING, true);
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

	public String[] getStringArray(final String value) {
		try {
			final JSONArray temp = new JSONArray(value);
			int length = temp.length();
			if (length > 0) {
				final String[] recipients = new String[length];
				for (int i = 0; i < length; i++) {
					recipients[i] = temp.getString(i).equalsIgnoreCase("null") ? "1" : temp.getString(i);
				}
				return recipients;
			}
		} catch (Exception e) {
		}
		return null;

	}

	public View getAdvertisement(String addId) {

		if (addId != null && addId.trim().length() > 0) {
			LinearLayout addLayout = new LinearLayout(this);
			addLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			AdView adView = new AdView(this, AdSize.SMART_BANNER, addId);
			addLayout.addView(adView);
			AdRequest req = new AdRequest();
			req.addTestDevice(AdRequest.TEST_EMULATOR);
			adView.loadAd(new AdRequest());
			return addLayout;
		} else {
			return null;
		}

	}

	public void launchActivity(JSONObject obj) {
		try {

			setScreenCaption(obj.getString("itemcaption"));
			final String className = IjoomerScreenHolder.originalScreens.get(obj.getString("itemview"));

			IjoomerMenus.getInstance().setTabBarData(null);
			IjoomerMenus.getInstance().setSideMenuData(null);

			final Intent intent = new Intent();
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setClassName(IjoomerSuperMaster.this, className);
			intent.putExtra("IN_USERID", "0");
			if (obj.getString("itemview").equals("Registration")) {
				logout();
			} else if (obj.getString("itemview").equals("IcmsSingleArticle")) {
				HashMap<String, String> value = new HashMap<String, String>();
				try {
					value.put("articleid", new JSONObject(obj.getString("itemdata")).getString("id") + "");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					intent.setClassName(IjoomerSuperMaster.this, className);
					intent.putExtra("IN_ARTICLE_ID", value.get("articleid"));
					startActivity(intent);
				} catch (Throwable e) {
					e.printStackTrace();
				}

			} else if (obj.getString("itemview").equals("IcmsAllCategory")) {
				HashMap<String, String> value = new HashMap<String, String>();
				try {
					value.put("title", getString(R.string.categorylist));
					value.put("categoryid", new JSONObject(obj.getString("itemdata")).getString("id") + "");
				} catch (JSONException e) {
					value.put("categoryid", "0");
					e.printStackTrace();
				}

				try {
					intent.setClassName(IjoomerSuperMaster.this, className);
					intent.putExtra("IN_PARENTCATEGORY", value);
					startActivity(intent);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			} else if (obj.getString("itemview").equals("IcmsSingleCategory")) {
				HashMap<String, String> value = new HashMap<String, String>();
				try {
					value.put("title", getString(R.string.singlecategory));
					value.put("categoryid", new JSONObject(obj.getString("itemdata")).getString("id") + "");
				} catch (JSONException e) {
					value.put("categoryid", "0");
					e.printStackTrace();
				}

				try {

					intent.setClassName(IjoomerSuperMaster.this, className);
					intent.putExtra("IN_PARENTCATEGORY", value);
					startActivity(intent);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			} else if (obj.getString("itemview").equals("IcmsCategoryBlog")) {
				HashMap<String, String> value = new HashMap<String, String>();
				try {
					value.put("categoryblogid", new JSONObject(obj.getString("itemdata")).getString("id") + "");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					intent.setClassName(IjoomerSuperMaster.this, className);
					intent.putExtra("IN_CATEGORYBLOG_ID", value.get("categoryblogid"));
					startActivity(intent);
				} catch (Throwable e) {
					e.printStackTrace();
				}

			} else if (obj.getString("itemview").equals("Web")) {
				try {
					intent.setClassName(IjoomerSuperMaster.this, className);
					intent.putExtra("url", new JSONObject(obj.getString("itemdata")).getString("url") + "");
					startActivity(intent);
				} catch (Exception e) {

				}
			} else {
				intent.setClassName(IjoomerSuperMaster.this, className);
				startActivity(intent);
			}
			finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
