package com.ijoomer.common.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.DisplayMetrics;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerResponseValidator;
import com.ijoomer.common.classes.IjoomerScreenHolder;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.custom.interfaces.IjoomerSharedPreferences;
import com.ijoomer.weservice.IjoomerWebService;
import com.ijoomer.weservice.ProgressListener;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.SmartApplication;

public class IjoomerGlobalConfiguration extends IjoomerResponseValidator implements IjoomerSharedPreferences {

	public static boolean isLoginRequired;
	public static boolean allowRegistration;
	public static boolean allowThemeSelection;
	public static String registrationWith;
	public static String theme;
	public static String serverTimeZone;
	public static String defaultUserImage;
	public static int videoUploadSize;
	public static int photoUploadSize;
	public static boolean isVideoUpload;
	public static boolean isEventCreate;
	public static boolean isGroupCreate;
	public static boolean isPhotoUpload;
	private AQuery androidQuery;

	public static int getPhotoUploadSize() {
		return photoUploadSize;
	}

	public static void setPhotoUploadSize(int Size) {
		photoUploadSize = Size;
	}

	public static int getVideoUploadSize() {
		return videoUploadSize;
	}

	public static void setVideoUploadSize(int Size) {
		videoUploadSize = Size;
	}

	public static boolean isVideoUpload() {
		return isVideoUpload;
	}

	public static void setIsVideoUpload(boolean isAllow) {
		isVideoUpload = isAllow;
	}

	public static boolean isEventCreate() {
		return isEventCreate;
	}

	public static void setIsEventCreate(boolean isAllow) {
		isEventCreate = isAllow;
	}

	public static boolean isGroupCreate() {
		return isGroupCreate;
	}

	public static void setIsGroupCreate(boolean isAllow) {
		isGroupCreate = isAllow;
	}

	public static boolean isPhotoUpload() {
		return isPhotoUpload;
	}

	public static void setisPhotoUpload(boolean isAllow) {
		isPhotoUpload = isAllow;
	}

	public static String getServerTimeZone() {
		return serverTimeZone;
	}

	public static void setServerTimeZone(String serverTimeZone) {
		IjoomerGlobalConfiguration.serverTimeZone = serverTimeZone;
	}

	private Context mContext;

	public IjoomerGlobalConfiguration(Context context) {
		super(context);
		mContext = context;
		androidQuery = new AQuery(mContext);
	}

	public static String getRegistrationWith() {
		return registrationWith;
	}

	public static boolean isLoginRequired() {
		return isLoginRequired;
	}

	public static boolean isAllowRegistration() {
		return allowRegistration;
	}

	public static boolean isAllowThemeSelection() {
		return allowThemeSelection;
	}

	public static String getTheme() {
		return theme;
	}

	public ArrayList<HashMap<String, String>> getThemeConfiguration() {
		IjoomerCaching ijoomerCaching = new IjoomerCaching(mContext);
		return ijoomerCaching.getDataFromCache("applicationConfig");
	}

	public ArrayList<HashMap<String, String>> getTabIcons(String componenetName) {
		IjoomerCaching ijoomerCaching = new IjoomerCaching(mContext);
		return ijoomerCaching.getDataFromCache("applicationConfig", "SELECT tab_active,tab from applicationConfig where extname='" + componenetName + "'");
	}

	public ArrayList<HashMap<String, String>> getIcons(String componenetName) {
		IjoomerCaching ijoomerCaching = new IjoomerCaching(mContext);
		return ijoomerCaching.getDataFromCache("applicationConfig", "SELECT icon from applicationConfig where extname='" + componenetName + "'");
	}

	public void loadGlobalConfiguration(final WebCallListener target) {

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				iw.addWSParam("task", "applicationConfig");
				JSONObject taskData = new JSONObject();
				try {
					taskData.put("device", "android");
					switch (mContext.getResources().getDisplayMetrics().densityDpi) {
					case DisplayMetrics.DENSITY_LOW:
						taskData.put("type", "ldpi");
						break;
					case DisplayMetrics.DENSITY_MEDIUM:
						taskData.put("type", "mdpi");
						break;
					case DisplayMetrics.DENSITY_HIGH:
						taskData.put("type", "hdpi");
						break;
					case DisplayMetrics.DENSITY_XHIGH:
						taskData.put("type", "xhdpi");
						break;
					}

				} catch (Throwable e) {
				}
				iw.addWSParam("taskData", taskData);
				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});

				if (validateResponse(iw.getJsonObject())) {
					try {
						JSONObject object = iw.getJsonObject().getJSONObject("configuration").getJSONObject("globalconfig");
						if (object.getInt("IJOOMER_GC_LOGIN_REQUIRED") == 1) {
							isLoginRequired = true;
						} else {
							isLoginRequired = false;
						}

						if (object.getString("IJOOMER_GC_REGISTRATION").equals("none")) {
							allowRegistration = false;
						} else {
							allowRegistration = false;
							registrationWith = object.getString("IJOOMER_GC_REGISTRATION");
						}

						if (object.getInt("IJOOMER_THM_ENABLE_THEME") == 1) {
							allowThemeSelection = true;
						} else {
							allowThemeSelection = false;
						}

						serverTimeZone = object.getString("offsetLocation");
						theme = object.getString("IJOOMER_THM_SELECTED_THEME");

						androidQuery.download(object.getString("defaultAvatar"), new File("/mnt/sdcard/temp.png"), new AjaxCallback<File>() {
							@Override
							public void callback(String url, File object, AjaxStatus status) {
								super.callback(url, object, status);
								defaultUserImage = object.getAbsolutePath();
							}
						});

						if (object.has("default_landing_screen")) {
							JSONObject defaultScreen = object.getJSONObject("default_landing_screen");
							SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(SP_DEFAULT_LANDING_SCREEN,
									IjoomerScreenHolder.originalScreens.get(defaultScreen.getString("itemview")));
							
							try {
								((IjoomerSuperMaster)mContext).setScreenCaption(defaultScreen.getString("itemcaption"));
							} catch (Exception e) {
							}
						}

						JSONObject jomsocialConfig = iw.getJsonObject().getJSONObject("configuration").getJSONObject("extentionconfig").getJSONObject("jomsocial");
						if (jomsocialConfig.has("videoUploadSize")) {
							setVideoUploadSize(Integer.parseInt(jomsocialConfig.getString("videoUploadSize")));
						} else {
							setVideoUploadSize(8);
						}
						if (jomsocialConfig.has("isVideoUpload")) {
							setIsVideoUpload(jomsocialConfig.getString("isVideoUpload").equals("1") ? true : false);
						} else {
							setIsVideoUpload(false);
						}

						if (jomsocialConfig.has("PhotoUploadSize")) {
							setPhotoUploadSize(Integer.parseInt(jomsocialConfig.getString("PhotoUploadSize")));
						} else {
							setPhotoUploadSize(8);
						}
						if (jomsocialConfig.has("isPhotoUpload")) {
							setisPhotoUpload(jomsocialConfig.getString("isPhotoUpload").equals("1") ? true : false);
						} else {
							setisPhotoUpload(false);
						}

						if (jomsocialConfig.has("createEvent")) {
							setIsEventCreate(jomsocialConfig.getString("createEvent").equals("1") ? true : false);
						} else {
							setIsEventCreate(false);
						}

						if (jomsocialConfig.has("createGroup")) {
							setIsGroupCreate(jomsocialConfig.getString("createGroup").equals("1") ? true : false);
						} else {
							setIsGroupCreate(false);
						}

					} catch (Throwable e) {
						e.printStackTrace();
					}
					try {
						IjoomerCaching ic = new IjoomerCaching(mContext);
						ic.cacheData(iw.getJsonObject().getJSONObject("configuration").getJSONArray("theme"), true, "applicationConfig");
						iw.getJsonObject().getJSONObject("configuration").remove("globalconfig");
						iw.getJsonObject().getJSONObject("configuration").remove("theme");
						ic.cacheData(iw.getJsonObject(), true, "menus");
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
			}
		}.execute();

	}

	public static ArrayList<HashMap<String, String>> getSideMenu(Context mContext, String screenName) {

		try {
			return new IjoomerCaching(mContext).getDataFromCache("menus", "select menuitem from menus where screens LIKE '%" + screenName + "%' and menuposition='2'");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<HashMap<String, String>> getHomeMenu(Context mContext) {

		try {
			return new IjoomerCaching(mContext).getDataFromCache("menus", "select menuitem from menus where menuposition='1'");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<HashMap<String, String>> getTabBar(Context mContext, String screenName) {

		try {
			return new IjoomerCaching(mContext).getDataFromCache("menus", "select menuitem from menus where screens LIKE '%" + screenName + "%' and menuposition='3'");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean hasTabBar(Context mContext, String screenName) {
		if (screenName != null) {
			ArrayList<HashMap<String, String>> tabBar = null;
			try {
				tabBar = new IjoomerCaching(mContext).getDataFromCache("menus", "select menuitem from menus where screens LIKE '%" + screenName + "%' and menuposition='3'");
			} catch (Throwable e) {
				e.printStackTrace();
			}

			if (tabBar == null || tabBar.size() <= 0) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	public static boolean hasSideMenu(Context mContext, String screenName) {
		if (screenName != null) {
			ArrayList<HashMap<String, String>> sideMenu = null;
			try {
				sideMenu = new IjoomerCaching(mContext).getDataFromCache("menus", "select menuitem from menus where screens LIKE '%" + screenName + "%' and menuposition='2'");
			} catch (Throwable e) {
				e.printStackTrace();
			}

			if (sideMenu == null || sideMenu.size() <= 0) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	public static ArrayList<HashMap<String, String>> getSideMenuIcon(Context mContext, String viewName) {
		try {
			return new IjoomerCaching(mContext).getDataFromCache("applicationConfig", "select icon from applicationConfig where viewname='" + viewName + "'");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<HashMap<String, String>> getTabIcons(Context mContext, String viewName) {
		try {
			return new IjoomerCaching(mContext).getDataFromCache("applicationConfig", "select tab,tab_active from applicationConfig where viewname='" + viewName + "'");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<HashMap<String, String>> getMoreIcon(Context mContext) {
		try {
			return new IjoomerCaching(mContext).getDataFromCache("applicationConfig", "select tab,tab_active from applicationConfig where viewname='More'");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	public void testUrl(final String testUrl, final WebCallListener target) {
		new AsyncTask<Void, Void, Object>() {

			@Override
			protected Object doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.setTestUrl(testUrl);
				iw.reset();
				iw.addWSParam("task", "ping");

				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});

				if (validateResponse(iw.getJsonObject())) {

				}
				return iw.getJsonObject();
			}

			@Override
			protected void onPostExecute(Object result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), null, result);
			}
		}.execute();
	}

}
