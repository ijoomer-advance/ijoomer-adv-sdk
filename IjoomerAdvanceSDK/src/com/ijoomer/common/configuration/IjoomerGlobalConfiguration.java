package com.ijoomer.common.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.DisplayMetrics;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerPagingProvider;
import com.ijoomer.common.classes.IjoomerScreenHolder;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.custom.interfaces.IjoomerSharedPreferences;
import com.ijoomer.weservice.IjoomerWebService;
import com.ijoomer.weservice.ProgressListener;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.SmartApplication;

/**
 * This Class Contains All Method Related To IjoomerGlobalConfiguration.
 * 
 * @author tasol
 * 
 */
public class IjoomerGlobalConfiguration extends IjoomerPagingProvider implements IjoomerSharedPreferences {

	private Context mContext;
	private AQuery androidQuery;
	public static final String JOMVERSION_V30 = "3.0";

	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public IjoomerGlobalConfiguration(Context context) {
		super(context);
		mContext = context;
		androidQuery = new AQuery(mContext);
	}

	public static boolean isEnableJbolo() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getBoolean(IjoomerSharedPreferences.SP_ISENABLEJBOLO,
				false);
	}

	public static void setEnableJbolo(boolean isEnableJbolo) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_ISENABLEJBOLO, isEnableJbolo);
	}
	
	public static boolean isEnableJReview() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getBoolean(IjoomerSharedPreferences.SP_ISENABLEJREVIEW,
				false);
	}

	public static void setEnableJReview(boolean isEnableJreview) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_ISENABLEJREVIEW, isEnableJreview);
	}

	public static boolean isEnableCommentK2() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getBoolean(IjoomerSharedPreferences.SP_ISENABLECOMMENTK2,
				false);
	}

	public static void setEnableCommentK2(boolean isEnableCommentK2) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_ISENABLECOMMENTK2, isEnableCommentK2);
	}

	public static boolean isJboloChatGetHistory() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getBoolean(
				IjoomerSharedPreferences.SP_JBOLO_CHAT_GET_HISTORY, false);
	}

	public static void setJboloChatGetHistory(boolean isGetHistory) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_JBOLO_CHAT_GET_HISTORY, isGetHistory);
	}

	public static boolean isJboloChatSendFile() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getBoolean(IjoomerSharedPreferences.SP_JBOLO_CHAT_SEND_FILE,
				false);
	}

	public static void setJboloChatSendFile(boolean isSendFile) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_JBOLO_CHAT_SEND_FILE, isSendFile);
	}

	public static boolean isJboloChatGroupChat() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getBoolean(IjoomerSharedPreferences.SP_JBOLO_CHAT_GROUP_CHAT,
				false);
	}

	public static void setJboloChatGroupChat(boolean isGroupChat) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_JBOLO_CHAT_GROUP_CHAT, isGroupChat);
	}

	public static int getJboloChatSendFileMaxLimit() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getInt(
				IjoomerSharedPreferences.SP_JBOLO_CHAT_SEND_FILE_MAX_LIMIT, 3);
	}

	public static void setJboloChatSendFileMaxLimit(int limit) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_JBOLO_CHAT_SEND_FILE_MAX_LIMIT, limit);
	}

	/**
	 * This method used to is enable voice.
	 * 
	 * @return represented {@link Boolean}
	 */
	public static boolean isEnableVoice() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getBoolean(IjoomerSharedPreferences.SP_ISENABLEVOICE, false);
	}

	/**
	 * This method used to set enable voice.
	 * 
	 * @param isVoiceEnable
	 *            represented enable voice
	 */
	public static void setEnableVoice(boolean isVoiceEnable) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_ISENABLEVOICE, isVoiceEnable);
	}

	/**
	 * This method used to get max audio length.
	 * 
	 * @return represented {@link Integer}
	 */
	public static int getMaxAudioLength() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getInt(IjoomerSharedPreferences.SP_MAXAUDIOLENGTH, 0);
	}

	/**
	 * This method used to set max audio length.
	 * 
	 * @param maxAudioLength
	 *            represented max audio length
	 */
	public static void setMaxAudioLength(int maxAudioLength) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_MAXAUDIOLENGTH, maxAudioLength);
	}

	/**
	 * This method used to set terms object.
	 * 
	 * @param request
	 *            represented request terms
	 */
	public static void setTermsObject(String request) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_TERMSOBJECT, request);
	}

	/**
	 * This method used to get terms object.
	 * 
	 * @return represented {@link String}
	 */
	public static String getTermsObject() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(IjoomerSharedPreferences.SP_TERMSOBJECT, "");
	}

	/**
	 * This method used to set enable terms.
	 * 
	 * @param isEnable
	 *            represented enable terms
	 */
	public static void setEnableTerms(boolean isEnable) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_ISENABLETERMS, isEnable);
	}

	/**
	 * This method used to is enable terms.
	 * 
	 * @return represented {@link Boolean}
	 */
	public static boolean isEnableTerms() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getBoolean(IjoomerSharedPreferences.SP_ISENABLETERMS, false);
	}

	/**
	 * This method used to get photo upload size
	 * 
	 * @return represented {@link Integer}
	 */
	public static int getPhotoUploadSize() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getInt(IjoomerSharedPreferences.SP_PHOTOUPLOADSIZE, 0);
	}

	/**
	 * This method used to set photo upload size
	 * 
	 * @param photoUploadSize
	 *            represented photo upload size
	 */
	public static void setPhotoUploadSize(int photoUploadSize) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_PHOTOUPLOADSIZE, photoUploadSize);
	}

	/**
	 * This method used to get video upload size
	 * 
	 * @return represented {@link Integer}
	 */
	public static int getVideoUploadSize() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getInt(IjoomerSharedPreferences.SP_VIDEOUPLOADSIZE, 0);
	}

	/**
	 * This method used to set video upload size.
	 * 
	 * @param videoUploadSize
	 *            represented video upload size
	 */
	public static void setVideoUploadSize(int videoUploadSize) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_VIDEOUPLOADSIZE, videoUploadSize);
	}

	/**
	 * This method used to is video upload.
	 * 
	 * @return represented {@link Boolean}
	 */
	public static boolean isVideoUpload() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getBoolean(IjoomerSharedPreferences.SP_ISVIDEOUPLOAD, false);
	}

	/**
	 * This method used to set is video upload.
	 * 
	 * @param isVideoUpload
	 *            represented is upload video
	 */
	public static void setIsVideoUpload(boolean isVideoUpload) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_ISVIDEOUPLOAD, isVideoUpload);
	}

	/**
	 * This method used to is event create.
	 * 
	 * @return represented {@link Boolean}
	 */
	public static boolean isEventCreate() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getBoolean(IjoomerSharedPreferences.SP_ISEVENTCREATE, false);
	}

	/**
	 * This method used to set event create.
	 * 
	 * @param isEventCreate
	 *            represented event create
	 */
	public static void setIsEventCreate(boolean isEventCreate) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_ISEVENTCREATE, isEventCreate);
	}

	/**
	 * This method used to is group create
	 * 
	 * @return represented {@link Boolean}
	 */
	public static boolean isGroupCreate() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getBoolean(IjoomerSharedPreferences.SP_ISGROUPCREATE, false);
	}

	/**
	 * This method used to set group create
	 * 
	 * @param isGroupCreate
	 *            represented group create
	 */
	public static void setIsGroupCreate(boolean isGroupCreate) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_ISGROUPCREATE, isGroupCreate);
	}

	/**
	 * This method used to is photo upload
	 * 
	 * @return represented {@link Boolean}
	 */
	public static boolean isPhotoUpload() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getBoolean(IjoomerSharedPreferences.SP_ISPHOTOUPLOAD, false);
	}

	/**
	 * This method used to set photo upload.
	 * 
	 * @param isPhotoUpload
	 *            represented photo upload
	 */
	public static void setisPhotoUpload(boolean isPhotoUpload) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_ISPHOTOUPLOAD, isPhotoUpload);
	}

	/**
	 * This method used to get server time zone.
	 * 
	 * @return represented {@link String}
	 */
	public static String getServerTimeZone() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(IjoomerSharedPreferences.SP_SERVERTIMEZONE, "");
	}

	/**
	 * This method used to set server time zone.
	 * 
	 * @param serverTimeZone
	 *            represented server time zone
	 */
	public static void setServerTimeZone(String serverTimeZone) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_SERVERTIMEZONE, serverTimeZone);
	}

	/**
	 * This method used to set registration with
	 * 
	 * @param registrationWith
	 *            registrationWith registration with
	 */
	public static void setRegistrationWith(String registrationWith) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_REGISTRATIONWITH, registrationWith);
	}

	/**
	 * This method used to get registration with.
	 * 
	 * @return represented {@link String}
	 */
	public static String getRegistrationWith() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(IjoomerSharedPreferences.SP_REGISTRATIONWITH, "");
	}

	/**
	 * This method used to set is login required.
	 * 
	 * @return isLoginRequired represented is login required
	 */
	public void setIsLoginRequired(Boolean isLoginRequired) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_ISLOGINREQUIRED, isLoginRequired);
	}

	/**
	 * This method used to is login required.
	 * 
	 * @return represented {@link Boolean}
	 */
	public static boolean isLoginRequired() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences()
				.getBoolean(IjoomerSharedPreferences.SP_ISLOGINREQUIRED, false);
	}

	/**
	 * This method used to set is allow registration
	 * 
	 * @return allowRegistration represented is allow registration
	 */
	public void setIsAllowRegistration(Boolean allowRegistration) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_ALLOWREGISTRATION, allowRegistration);
	}

	/**
	 * This method used to is allow registration.
	 * 
	 * @return represented {@link Boolean}
	 */
	public static boolean isAllowRegistration() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getBoolean(IjoomerSharedPreferences.SP_ALLOWREGISTRATION,
				false);
	}

	/**
	 * This method used to set is allow theme selection
	 * 
	 * @return allowThemeSelection represented is allow theme selection
	 */
	public void setIsAllowThemeSelection(Boolean allowThemeSelection) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_ALLOWTHEMESELECTION, allowThemeSelection);
	}

	/**
	 * This method used to is allow theme selection
	 * 
	 * @return represented {@link Boolean}
	 */
	public static boolean isAllowThemeSelection() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getBoolean(IjoomerSharedPreferences.SP_ALLOWTHEMESELECTION,
				false);
	}

	/**
	 * This method used to set default avatar
	 * 
	 * @return defaultAvatar represented default avatar
	 */
	public void setDefaultAvatar(String defaultAvatar) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_DEFAULTAVATAR, defaultAvatar);
	}

	/**
	 * This method used to set default avatar
	 * 
	 * @return defaultAvatar represented default avatar
	 */
	public void setDefaultAvatarFemale(String defaultAvatar) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_DEFAULTAVATAR_FEMALE, defaultAvatar);
	}

	/**
	 * This method used to get theme.
	 * 
	 * @return represented {@link String}
	 */
	public static String getDefaultAvatar() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(IjoomerSharedPreferences.SP_DEFAULTAVATAR, "");
	}

	/**
	 * This method used to get theme.
	 * 
	 * @return represented {@link String}
	 */
	public static String getDefaultAvatarFemale() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(IjoomerSharedPreferences.SP_DEFAULTAVATAR_FEMALE,
				"");
	}

	public static void setJomsocialVersion(String version) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_JOM_VERSION, version);
	}

	public static String getJomsocialVersion() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(IjoomerSharedPreferences.SP_JOM_VERSION, "");
	}

	/**
	 * This method used to get theme configuration.
	 * 
	 * @return represented {@link HashMap} list
	 */
	public ArrayList<HashMap<String, String>> getThemeConfiguration() {
		IjoomerCaching ijoomerCaching = new IjoomerCaching(mContext);
		return ijoomerCaching.getDataFromCache("applicationConfig");
	}

	public ArrayList<HashMap<String, String>> getCustomThemeConfiguration() {
		IjoomerCaching ijoomerCaching = new IjoomerCaching(mContext);
		return ijoomerCaching.getDataFromCache("menus");
	}

	/**
	 * This method used to get tab icons.
	 * 
	 * @param componenetName
	 *            represented component name
	 * @return represented {@link HashMap} list
	 */
	public ArrayList<HashMap<String, String>> getTabIcons(String componenetName) {
		IjoomerCaching ijoomerCaching = new IjoomerCaching(mContext);
		return ijoomerCaching.getDataFromCache("applicationConfig", "SELECT tab_active,tab from applicationConfig where extname='"
				+ componenetName + "'");
	}

	/**
	 * This method used to get icons.
	 * 
	 * @param componenetName
	 *            represented component name
	 * @return represented {@link HashMap} list
	 */
	public ArrayList<HashMap<String, String>> getIcons(String componenetName) {
		IjoomerCaching ijoomerCaching = new IjoomerCaching(mContext);
		return ijoomerCaching.getDataFromCache("applicationConfig", "SELECT icon from applicationConfig where extname='" + componenetName
				+ "'");
	}

	/**
	 * This Method is used to get dynamic fields for add entry.
	 *
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void getComponents(final WebCallListener target) {
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam("task", "ping");

				JSONObject taskData = new JSONObject();
				iw.addWSParam(TASKDATA, taskData);
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
						String[] extensions = getStringArray(iw.getJsonObject().getString("extensions"));
						for(int i = 0; i < extensions.length; i++){
							if(extensions[i].toLowerCase().equalsIgnoreCase("jbolo")){
								System.out.println("JBOLOENABLED");
								setEnableJbolo(true);
							} else if(extensions[i].toLowerCase().equalsIgnoreCase("jreviews")){
								System.out.println("JREVIEWENABLED");
								setEnableJReview(true);
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
			}

		}.execute();
	}

	/**
	 * This method used to load global configuration.
	 * 
	 * @param target
	 *            represented {@link WebCallListener}
	 */
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
					case DisplayMetrics.DENSITY_XXHIGH:
						taskData.put("type", "xxhdpi");
						break;
					case DisplayMetrics.DENSITY_TV:
						taskData.put("type", "xxxhdpi");
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
						JSONObject versionInfo = iw.getJsonObject().getJSONObject("configuration").getJSONObject("versioninfo");
						if (versionInfo.getString("jomsocial").startsWith("3")) {
							setJomsocialVersion(JOMVERSION_V30);
						} else {
							setJomsocialVersion(versionInfo.getString("jomsocial"));
						}
					} catch (Exception e) {
						setJomsocialVersion("");
					}
					try {
						JSONObject object = iw.getJsonObject().getJSONObject("configuration").getJSONObject("globalconfig");
						try {
							if (object.getInt("IJOOMER_GC_LOGIN_REQUIRED") == 1) {
								setIsLoginRequired(true);
							} else {
								setIsLoginRequired(false);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

						try {
							if (object.getString("IJOOMER_GC_REGISTRATION").equals("none")) {
								setIsAllowRegistration(false);
							} else {
								setIsAllowRegistration(false);
								setRegistrationWith(object.getString("IJOOMER_GC_REGISTRATION"));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

						try {
							if (object.getInt("IJOOMER_THM_ENABLE_THEME") == 1) {
								setIsAllowThemeSelection(true);
							} else {
								setIsAllowThemeSelection(false);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

						try {
							setServerTimeZone(object.getString("offsetLocation"));
						} catch (Exception e) {
							e.printStackTrace();
						}

						try {
							androidQuery.download(object.getString("defaultAvatar"), new File("/mnt/sdcard/tempMale.png"),
									new AjaxCallback<File>() {
								@Override
								public void callback(String url, File object, AjaxStatus status) {
									super.callback(url, object, status);
									setDefaultAvatar(object.getAbsolutePath());
								}
							});
						} catch (Exception e) {
						}

						try {
							androidQuery.download(object.getString("defaultAvatarFemale"), new File("/mnt/sdcard/tempFemale.png"),
									new AjaxCallback<File>() {
								@Override
								public void callback(String url, File object, AjaxStatus status) {
									super.callback(url, object, status);
									setDefaultAvatarFemale(object.getAbsolutePath());
								}
							});
						} catch (Exception e) {
						}

						try {
							if (object.has("default_landing_screen")) {
								JSONObject defaultScreen = object.getJSONObject("default_landing_screen");
								SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(SP_DEFAULT_LANDING_SCREEN,
										IjoomerScreenHolder.originalScreens.get(defaultScreen.getString("itemview")));

								SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(SP_LAST_ACTIVITY_INTENT,
										defaultScreen.toString());

								((IjoomerSuperMaster) mContext).setScreenCaption(defaultScreen.getString("itemcaption"));

							}
						} catch (Exception e) {
							e.printStackTrace();
						}

						try {
							JSONObject extentionConfig = iw.getJsonObject().getJSONObject("configuration").getJSONObject("extentionconfig");

							try {
								JSONObject jomsocialConfig = extentionConfig.getJSONObject("jomsocial");

								try {
									if (jomsocialConfig.has("videoUploadSize")) {
										setVideoUploadSize(Integer.parseInt(jomsocialConfig.getString("videoUploadSize")));
									} else {
										setVideoUploadSize(8);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								try {
									if (jomsocialConfig.has("isVideoUpload")) {
										setIsVideoUpload(jomsocialConfig.getString("isVideoUpload").equals("1") ? true : false);
									} else {
										setIsVideoUpload(false);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								try {
									if (jomsocialConfig.has("PhotoUploadSize")) {
										setPhotoUploadSize(Integer.parseInt(jomsocialConfig.getString("PhotoUploadSize")));
									} else {
										setPhotoUploadSize(8);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								try {
									if (jomsocialConfig.has("isPhotoUpload")) {
										setisPhotoUpload(jomsocialConfig.getString("isPhotoUpload").equals("1") ? true : false);
									} else {
										setisPhotoUpload(false);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

								try {
									if (jomsocialConfig.has("createEvent")) {
										setIsEventCreate(jomsocialConfig.getString("createEvent").equals("1") ? true : false);
									} else {
										setIsEventCreate(false);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

								try {
									if (jomsocialConfig.has("createGroup")) {
										setIsGroupCreate(jomsocialConfig.getString("createGroup").equals("1") ? true : false);
									} else {
										setIsGroupCreate(false);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

								try {
									if (jomsocialConfig.has("isEnableTerms") && jomsocialConfig.getString("isEnableTerms").equals("1")) {
										setEnableTerms(true);
									} else {
										setEnableTerms(false);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

								try {
									if (jomsocialConfig.has("isEnableVoice") && jomsocialConfig.getString("isEnableVoice").equals("1")) {
										setEnableVoice(true);
									} else {
										setEnableVoice(false);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

								try {
									if (jomsocialConfig.has("termsObject")) {
										setTermsObject(jomsocialConfig.getString("termsObject"));
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							} catch (Throwable e) {
								e.printStackTrace();
							}

							try {
								JSONObject k2Config = extentionConfig.getJSONObject("k2");

								if (k2Config.has("isEnableComment")) {
									setEnableCommentK2(k2Config.getString("isEnableComment").equals("1") ? true : false);
								} else {
									setEnableCommentK2(false);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}

							try {
								JSONObject jboloConfig = extentionConfig.getJSONObject("jbolo");
								try {

									if (jboloConfig.has("chathistory")) {
										setJboloChatGetHistory(jboloConfig.getString("chathistory").equals("1") ? true : false);
									} else {
										setJboloChatGetHistory(false);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

								try {

									try {
										if (jboloConfig.has("sendfile")) {
											setJboloChatSendFile(jboloConfig.getString("sendfile").equals("1") ? true : false);
										} else {
											setJboloChatSendFile(false);
										}
									} catch (Exception e) {
										e.printStackTrace();
									}

									try {

										if (jboloConfig.has("groupchat")) {
											setJboloChatGroupChat(jboloConfig.getString("groupchat").equals("1") ? true : false);
										} else {
											setJboloChatGroupChat(false);
										}
									} catch (Exception e) {
										e.printStackTrace();
									}

									try {

										if (jboloConfig.has("maxSizeLimit")) {
											setJboloChatSendFileMaxLimit(Integer.parseInt(jboloConfig.getString("maxSizeLimit")));
										} else {
											setJboloChatSendFileMaxLimit(3);
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

							} catch (Exception e) {
								e.printStackTrace();
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					setMaxAudioLength(30);
					try {
						IjoomerCaching ic = new IjoomerCaching(mContext);
						ic.cacheData(iw.getJsonObject().getJSONObject("configuration").getJSONArray("theme"), true, "applicationConfig");
						iw.getJsonObject().getJSONObject("configuration").remove("globalconfig");
						iw.getJsonObject().getJSONObject("configuration").remove("versioninfo");
						iw.getJsonObject().getJSONObject("configuration").remove("theme");
						iw.getJsonObject().getJSONObject("configuration").remove("extentionconfig");
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
				if (!SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getBoolean(SP_ICON_PRELOADER, false)) {
					SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(SP_ICON_PRELOADER, true);
					loadAllIcons(target);
				} else {
					target.onProgressUpdate(100);
					target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
				}
			}
		}.execute();
	}

	/**
	 * This method used to get side menu.
	 * 
	 * @param mContext
	 *            represented {@link Context}
	 * @param screenName
	 *            represented screen name
	 * @return represented {@link HashMap} list
	 */
	public static ArrayList<HashMap<String, String>> getSideMenu(Context mContext, String screenName) {

		try {
			return new IjoomerCaching(mContext).getDataFromCache("menus", "select menuitem from menus where screens LIKE '%" + screenName
					+ "%' and menuposition='2'");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * This method used to get side menu.
	 * 
	 * @param mContext
	 *            represented {@link Context}
	 * @param screenName
	 *            represented screen name
	 * @return represented {@link HashMap} list
	 */
	public static ArrayList<HashMap<String, String>> getTopbar(Context mContext, String screenName) {

		try {
			return new IjoomerCaching(mContext).getDataFromCache("menus", "select menuitem from menus where screens LIKE '%" + screenName
					+ "%' and menuposition='3'");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method used to get home menu.
	 * 
	 * @param mContext
	 *            represented {@link Context}
	 * @return represented {@link HashMap} list
	 */
	public static ArrayList<HashMap<String, String>> getHomeMenu(Context mContext) {

		try {
			return new IjoomerCaching(mContext).getDataFromCache("menus", "select menuitem from menus where menuposition='1'");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method used to get tab bar.
	 * 
	 * @param mContext
	 *            represented {@link Context}
	 * @param screenName
	 *            represented screen name
	 * @return represented {@link HashMap} list
	 */
	public static ArrayList<HashMap<String, String>> getTabBar(Context mContext, String screenName) {

		try {
			return new IjoomerCaching(mContext).getDataFromCache("menus", "select menuitem from menus where screens LIKE '%" + screenName
					+ "%' and menuposition='3'");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method used to check hash tab bar.
	 * 
	 * @param mContext
	 *            represented {@link Context}
	 * @param screenName
	 *            represented screen name
	 * @return represented {@link Boolean}
	 */
	public static boolean hasTabBar(Context mContext, String screenName) {
		if (screenName != null) {
			ArrayList<HashMap<String, String>> tabBar = null;
			try {
				tabBar = new IjoomerCaching(mContext).getDataFromCache("menus", "select menuitem from menus where screens LIKE '%"
						+ screenName + "%' and menuposition='3'");
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

	/**
	 * This method used to check hash side menu.
	 * 
	 * @param mContext
	 *            represented {@link Context}
	 * @param screenName
	 *            represented screen name
	 * @return represented {@link Boolean}
	 */
	public static boolean hasSideMenu(Context mContext, String screenName) {
		if (screenName != null) {
			ArrayList<HashMap<String, String>> sideMenu = null;
			try {
				sideMenu = new IjoomerCaching(mContext).getDataFromCache("menus", "select menuitem from menus where screens LIKE '%"
						+ screenName + "%' and menuposition='2'");
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

	/**
	 * This method used to get side menu icon.
	 * 
	 * @param mContext
	 *            represented {@link Context}
	 * @param viewName
	 *            represented view name
	 * @return represented {@link HashMap} list
	 */
	public static ArrayList<HashMap<String, String>> getSideMenuIcon(Context mContext, String viewName) {
		try {
			return new IjoomerCaching(mContext).getDataFromCache("applicationConfig", "select icon from applicationConfig where viewname='"
					+ viewName + "'");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method used to get tab menu icon.
	 * 
	 * @param mContext
	 *            represented {@link Context}
	 * @param viewName
	 *            represented view name
	 * @return represented {@link HashMap} list
	 */
	public static ArrayList<HashMap<String, String>> getTabIcons(Context mContext, String viewName) {
		try {
			return new IjoomerCaching(mContext).getDataFromCache("applicationConfig",
					"select tab,tab_active from applicationConfig where viewname='" + viewName + "'");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method used to get more menu icon.
	 * 
	 * @param mContext
	 *            represented {@link Context}
	 * @return represented {@link HashMap} list
	 */
	public static ArrayList<HashMap<String, String>> getMoreIcon(Context mContext) {
		try {
			return new IjoomerCaching(mContext).getDataFromCache("applicationConfig",
					"select tab,tab_active from applicationConfig where viewname='More'");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method used to ping server component installed.
	 * 
	 * @param testUrl
	 *            represented server url
	 * @param target
	 *            represented {@link WebCallListener}
	 */
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
					IjoomerApplicationConfiguration.setDomainName(testUrl);
					iw.setTestUrl(null);
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

	/**
	 * This method used to load all icons.
	 * 
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	private void loadAllIcons(final WebCallListener target) {
		try {
			ArrayList<HashMap<String, String>> icons = getThemeConfiguration();
			List<String> url = new ArrayList<String>();
			for (HashMap<String, String> hashMap : icons) {
				if (hashMap.containsKey("icon")) {
					url.add(hashMap.get("icon"));
				}
				url.add(hashMap.get("tab"));
				url.add(hashMap.get("tab_active"));
			}
			icons = getCustomThemeConfiguration();
			for (HashMap<String, String> hashMap : icons) {

				try {
					JSONArray arr = new JSONArray(hashMap.get("menuitem"));

					for (int i = 0; i < arr.length(); i++) {

						try {
							JSONObject obj = arr.getJSONObject(i);
							if (obj.has("icon")) {
								url.add(obj.getString("icon"));
							}
							if (obj.has("tab")) {
								url.add(obj.getString("tab"));
							}
							if (obj.has("tab_active")) {
								url.add(obj.getString("tab_active"));
							}
						} catch (Exception e) {
						}
					}
				} catch (Exception e) {
				}
			}

			if (url.size() > 0) {
				startIconPreloader(url, 0, target);
			} else {
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
			}
		} catch (Exception e) {
			target.onProgressUpdate(100);
			target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
		}
	}

	/**
	 * This method used to start icon pre-loader.
	 * 
	 * @param icons
	 *            represented {@link String} list icon
	 * @param index
	 *            represented index
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	private void startIconPreloader(final List<String> icons, final int index, final WebCallListener target) {

		androidQuery.ajax(icons.get(index), Bitmap.class, 0, new AjaxCallback<Bitmap>() {
			@Override
			public void callback(String url, Bitmap object, AjaxStatus status) {
				super.callback(url, object, status);
				if ((icons.size() - 1) == index) {
					target.onProgressUpdate(100);
					target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
				} else {
					startIconPreloader(icons, index + 1, target);
				}
			}
		});
	}

	public void getPushData(final String pushId, final WebCallListener target) {

		new AsyncTask<Void, Void, JSONObject>() {

			@Override
			protected JSONObject doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				iw.addWSParam(TASK, "getPushNotification");

				JSONObject taskData = new JSONObject();
				try {
					taskData.put("id", pushId);
				} catch (Throwable e) {
				}

				iw.addWSParam(TASKDATA, taskData);

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
					return iw.getJsonObject();

				}
				return null;
			}

			@Override
			protected void onPostExecute(JSONObject result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), null, result);
			}
		}.execute();

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
}
