package com.ijoomer.common.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.DisplayMetrics;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerPagingProvider;
import com.ijoomer.common.classes.IjoomerScreenHolder;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.custom.interfaces.IjoomerSharedPreferences;
import com.ijoomer.src.R;
import com.ijoomer.weservice.IjoomerWebService;
import com.ijoomer.weservice.ProgressListener;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;
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
	 * Jreview code
	 */
	int count = 0;
	int batchCount = 0;
	int batchs;
	int pages = 36;
	int limit = 6;
	int pagePerBatch = 0;
	boolean wasNetworkProblem = false;
	private WebCallListener local = null;
	private List<String> queue = new ArrayList<String>();
	
	private final String CATEGORY = "category";
	private final String GET_DATA = "getData";
	private final String ISINTIALREQUEST = "isIntialRequest";
	private final String LASTREQUESTTIME = "lastRequestTime";
	
	private final String TABLE_JREVIEW_DIRECTORIES = "jreview_directory";
	private final String TABLE_JREVIEW_CATEGORIES = "jreview_categories";
	private final String TABLE_JREVIEW_ARTICLES = "jreview_articles";
	private final String TABLE_JREVIEW_SEARCH = "jreview_search";
	private boolean isCalling = false;

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
	 * JREVIEW CONFIGS
	 */

	/**
	 * This method used to get photo upload size
	 * 
	 * @return represented {@link Integer}
	 */
	public static int getJreviewArticlePhotoUploadSize() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getInt(IjoomerSharedPreferences.SP_JREVIEWARTICLEPHOTOUPLOADSIZE, 0);
	}

	/**
	 * This method used to set photo upload size
	 * 
	 * @param photoUploadSize
	 *            represented photo upload size
	 */
	public static void setJreviewArticlePhotoUploadSize(int photoUploadSize) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_JREVIEWARTICLEPHOTOUPLOADSIZE, photoUploadSize);
	}


	/**
	 * This method used to get video upload size
	 * 
	 * @return represented {@link Integer}
	 */
	public static int getJreviewArticleVideoUploadSize() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getInt(IjoomerSharedPreferences.SP_JREVIEWARTICLEVIDEOUPLOADSIZE, 0);
	}

	/**
	 * This method used to set video upload size
	 * 
	 * @param videoUploadSize
	 *            represented video upload size
	 */
	public static void setJreviewArticleVideoUploadSize(int videoUploadSize) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_JREVIEWARTICLEVIDEOUPLOADSIZE, videoUploadSize);
	}


	/**
	 * This method used to get audio upload size
	 * 
	 * @return represented {@link Integer}
	 */
	public static int getJreviewArticleAudioUploadSize() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getInt(IjoomerSharedPreferences.SP_JREVIEWARTICLEAUDIOUPLOADSIZE, 0);
	}

	/**
	 * This method used to set audio upload size
	 * 
	 * @param audioUploadSize
	 *            represented audio upload size
	 */
	public static void setJreviewArticleAudioUploadSize(int audioUploadSize) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_JREVIEWARTICLEAUDIOUPLOADSIZE, audioUploadSize);
	}

	/**
	 * This method used to get attachment upload size
	 * 
	 * @return represented {@link Integer}
	 */
	public static int getJreviewArticleAttachmentUploadSize() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getInt(IjoomerSharedPreferences.SP_JREVIEWARTICLEATTACHMENTUPLOADSIZE, 0);
	}

	/**
	 * This method used to set attachment upload size
	 * 
	 * @param attachmentUploadSize
	 *            represented attachment upload size
	 */
	public static void setJreviewArticleAttachmentUploadSize(String attachmentUploadSize) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_JREVIEWARTICLEATTACHMENTUPLOADSIZE, attachmentUploadSize);
	}

	/**
	 * This method used to get photo upload limit
	 * 
	 * @return represented {@link Integer}
	 */
	public static String getJreviewArticlePhotoUploadLimit() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(IjoomerSharedPreferences.SP_JREVIEWARTICLEPHOTOUPLOADLIMIT, "0");
	}

	/**
	 * This method used to set photo upload limit
	 * 
	 * @param photoUploadLimit
	 *            represented photo upload limit
	 */
	public static void setJreviewArticlePhotoUploadLimit(String photoUploadLimit) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_JREVIEWARTICLEPHOTOUPLOADLIMIT, photoUploadLimit);
	}

	/**
	 * This method used to get video upload limit
	 * 
	 * @return represented {@link Integer}
	 */
	public static String getJreviewArticleVideoUploadLimit() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(IjoomerSharedPreferences.SP_JREVIEWARTICLEVIDEOUPLOADLIMIT, "0");
	}

	/**
	 * This method used to set video upload limit
	 * 
	 * @param videoUploadLimit
	 *            represented video upload limit
	 */
	public static void setJreviewArticleVideoUploadLimit(String videoUploadLimit) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_JREVIEWARTICLEVIDEOUPLOADLIMIT, videoUploadLimit);
	}

	/**
	 * This method used to get audio upload limit
	 * 
	 * @return represented {@link Integer}
	 */
	public static String getJreviewArticleAudioUploadLimit() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(IjoomerSharedPreferences.SP_JREVIEWARTICLEAUDIOUPLOADLIMIT, "0");
	}

	/**
	 * This method used to set audio upload limit
	 * 
	 * @param audioUploadLimit
	 *            represented audio upload limit
	 */
	public static void setJreviewArticleAudioUploadLimit(String audioUploadLimit) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_JREVIEWARTICLEAUDIOUPLOADLIMIT, audioUploadLimit);
	}

	/**
	 * This method used to get attachment upload limit
	 * 
	 * @return represented {@link Integer}
	 */
	public static String getJreviewArticleAttachmentUploadLimit() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(IjoomerSharedPreferences.SP_JREVIEWARTICLEATTACHMENTUPLOADLIMIT, "0");
	}

	/**
	 * This method used to set attachment upload limit
	 * 
	 * @param attachmentUploadLimit
	 *            represented attachment upload limit
	 */
	public static void setJreviewArticleAttachmentUploadLimit(String attachmentUploadLimit) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_JREVIEWARTICLEATTACHMENTUPLOADLIMIT, attachmentUploadLimit);
	}

	/**
	 * This method used to set is allow article search by author 
	 * 
	 * @return allowsearchbyauthor represented is allow search by author
	 */
	public void setIsAllowSearchByAuthor(String allowsearchbyauthor) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_JREVIEWALLOWSEARCHBYAUTHOR, allowsearchbyauthor);
	}

	/**
	 * This method used to is allow article search by author 
	 * 
	 * @return represented {@link Boolean}
	 */
	public static boolean isAllowSearchByAuthor() {
		if(SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(IjoomerSharedPreferences.SP_JREVIEWALLOWSEARCHBYAUTHOR,
				"").equalsIgnoreCase("1")){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * This method used to set is photo upload enable
	 * 
	 */
	public void setJreviewPhotoEnable(String jreviewphotoenable) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_JREVIEWPHOTOENABLE, jreviewphotoenable);
	}

	/**
	 * This method used to get is photo upload enable
	 * 
	 * @return represented {@link Boolean}
	 */
	public static boolean isJreviewPhotoEnable() {
		if(SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(IjoomerSharedPreferences.SP_JREVIEWPHOTOENABLE,
				"").equalsIgnoreCase("1")){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * This method used to set video upload enable
	 * 
	 */
	public void setJreviewVideoEnable(String jreviewvideoenable) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_JREVIEWVIDEOENABLE, jreviewvideoenable);
	}

	/**
	 * This method used to get video upload enable
	 * 
	 * @return represented {@link Boolean}
	 */
	public static boolean isJreviewVideoEnable() {
		if(SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(IjoomerSharedPreferences.SP_JREVIEWVIDEOENABLE,
				"").equalsIgnoreCase("1")){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * This method used to set audio upload enable
	 * 
	 */
	public void setJreviewAudioEnable(String jreviewaudioenable) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_JREVIEWAUDIOENABLE, jreviewaudioenable);
	}

	/**
	 * This method used to get is audio upload enable
	 * 
	 * @return represented {@link Boolean}
	 */
	public static boolean isJreviewAudioEnable() {
		if(SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(IjoomerSharedPreferences.SP_JREVIEWAUDIOENABLE,
				"").equalsIgnoreCase("1")){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * This method used to set attachment upload enable 
	 * 
	 */
	public void setJreviewAttachmentEnable(String jreviewattachmentenable) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_JREVIEWATTACHMENTENABLE, jreviewattachmentenable);
	}

	/**
	 * This method used to get is attachment upload enable
	 * 
	 * @return represented {@link Boolean}
	 */
	public static boolean isJreviewAttachmentEnable() {
		if(SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(IjoomerSharedPreferences.SP_JREVIEWATTACHMENTENABLE,
				"").equalsIgnoreCase("1")){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * This method used to set attachment upload enable 
	 * 
	 */
	public void setJreviewFavouriteEnable(String jreviewfavouriteenable) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_JREVIEWFAVOURITEENABLE, jreviewfavouriteenable);
	}

	/**
	 * This method used to get is attachment upload enable
	 * 
	 * @return represented {@link Boolean}
	 */
	public static boolean isJreviewFavouriteEnable() {
		if(SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(IjoomerSharedPreferences.SP_JREVIEWFAVOURITEENABLE,
				"").equalsIgnoreCase("1")){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * This method used to set photo upload enable 
	 * 
	 */
	public void setJreviewPhotoUploadEnable(String jreviewphotouploadenable) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_JREVIEWPHOTOUPLOADENABLE, jreviewphotouploadenable);
	}

	/**
	 * This method used to get is photo upload enable
	 * 
	 * @return represented {@link Boolean}
	 */
	public static String isJreviewPhotoUploadEnable() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(IjoomerSharedPreferences.SP_JREVIEWPHOTOUPLOADENABLE,
				"");
	}

	/**
	 * This method used to set video upload enable 
	 * 
	 */
	public void setJreviewVideoUploadEnable(String jreviewvideouploadenable) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_JREVIEWVIDEOUPLOADENABLE, jreviewvideouploadenable);
	}

	/**
	 * This method used to get is video upload enable
	 * 
	 * @return represented {@link Boolean}
	 */
	public static String isJreviewVideoUploadEnable() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(IjoomerSharedPreferences.SP_JREVIEWVIDEOUPLOADENABLE,
				"");
	}

	/**
	 * This method used to set audio upload enable 
	 * 
	 */
	public void setJreviewAudioUploadEnable(String jreviewaudiouploadenable) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_JREVIEWAUDIOUPLOADENABLE, jreviewaudiouploadenable);
	}

	/**
	 * This method used to get is audio upload enable
	 * 
	 * @return represented {@link Boolean}
	 */
	public static String isJreviewAudioUploadEnable() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(IjoomerSharedPreferences.SP_JREVIEWAUDIOUPLOADENABLE,
				"");
	}

	/**
	 * This method used to set attachment upload enable 
	 * 
	 */
	public void setJreviewAttachmentUploadEnable(String jreviewattachmentuploadenable) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_JREVIEWATTACHMENTUPLOADENABLE, jreviewattachmentuploadenable);
	}

	/**
	 * This method used to get is attachment upload enable
	 * 
	 * @return represented {@link Boolean}
	 */
	public static String isJreviewAttachmentUploadEnable() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(IjoomerSharedPreferences.SP_JREVIEWATTACHMENTUPLOADENABLE,
				"");
	}

	/**
	 * This method used to get jreview page limit
	 * 
	 * @return represented {@link Integer}
	 */
	public static int getJreviewPageLimit() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getInt(IjoomerSharedPreferences.SP_JREVIEWPAGELIMIT, 0);
	}

	/**
	 * This method used to set jreview page limit
	 * 
	 * @param attachmentUploadLimit
	 *            represented attachment upload limit
	 */
	public static void setJreviewPageLimit(int pageLimit) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_JREVIEWPAGELIMIT, pageLimit);
	}

	/**
	 * This method used to get jreview articles total
	 * 
	 * @return represented {@link Integer}
	 */
	public static int getJreviewTotalArticles() {
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getInt(IjoomerSharedPreferences.SP_JREVIEWTOTALARTICLES, 0);
	}

	/**
	 * This method used to set jreview articles total
	 * 
	 * @param attachmentUploadLimit
	 *            represented attachment upload limit
	 */
	public static void setJreviewTotalArticles(int total) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_JREVIEWTOTALARTICLES, total);
	}

	/**
	 * This method used to get jreview add listing allow
	 * 
	 * @return represented {@link Integer}
	 */
	public static Boolean isJreviewAddListingAllow() {
		if(SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(IjoomerSharedPreferences.SP_JREVIEWADDLISTINGENABLE,
				"").equalsIgnoreCase("1")){
			return true;
		}else{
			return false;
		}	
	}

	/**
	 * This method used to set jreview add listing allow
	 * 
	 * @param attachmentUploadLimit
	 *            represented attachment upload limit
	 */
	public static void setJreviewAddListingAllow(String addListing) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_JREVIEWADDLISTINGENABLE, addListing);
	}

	/**
	 * This method used to get jreview add listing allow
	 * 
	 * @return represented {@link Integer}
	 */
	public static Boolean isJreviewEditListingAllow() {
		if(SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(IjoomerSharedPreferences.SP_JREVIEWEDITLISTINGENABLE,
				"").equalsIgnoreCase("1")){
			return true;
		}else{
			return false;
		}	
	}

	/**
	 * This method used to set jreview add listing allow
	 * 
	 * @param attachmentUploadLimit
	 *            represented attachment upload limit
	 */
	public static void setJreviewEditListingAllow(String editListing) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_JREVIEWEDITLISTINGENABLE, editListing);
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
								JSONObject jreviewConfig = extentionConfig.getJSONObject("jreviews");

								try {
									if (jreviewConfig.has("addnewaccess")) {
										setJreviewAddListingAllow(jreviewConfig.getString("addnewaccess"));
									} else {
										setJreviewAddListingAllow("0");
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

								try {
									if (jreviewConfig.has("editaccess")) {
										setJreviewEditListingAllow(jreviewConfig.getString("editaccess"));
									} else {
										setJreviewEditListingAllow("0");
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								try {
									if (jreviewConfig.has("media_access_view_photo_listing")) {
										setJreviewPhotoEnable(jreviewConfig.getString("media_access_view_photo_listing"));
									} else {
										setJreviewPhotoEnable("0");
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

								try {
									if (jreviewConfig.has("media_access_view_video_listing")) {
										setJreviewVideoEnable(jreviewConfig.getString("media_access_view_video_listing"));
									} else {
										setJreviewVideoEnable("0");
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

								try {
									if (jreviewConfig.has("media_access_view_audio_listing")) {
										setJreviewAudioEnable(jreviewConfig.getString("media_access_view_audio_listing"));
									} else {
										setJreviewAudioEnable("0");
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

								try {
									if (jreviewConfig.has("media_access_view_attachment_listing")) {
										setJreviewAttachmentEnable(jreviewConfig.getString("media_access_view_attachment_listing"));
									} else {
										setJreviewAttachmentEnable("0");
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

								try {
									if (jreviewConfig.has("media_access_submit_photo_listing")) {
										setJreviewPhotoUploadEnable(jreviewConfig.getString("media_access_submit_photo_listing"));
									} else {
										setJreviewPhotoUploadEnable("0");
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

								try {
									if (jreviewConfig.has("media_access_submit_video_listing")) {
										setJreviewVideoUploadEnable(jreviewConfig.getString("media_access_submit_video_listing"));
									} else {
										setJreviewVideoUploadEnable("0");
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

								try {
									if (jreviewConfig.has("media_access_submit_audio_listing")) {
										setJreviewAudioUploadEnable(jreviewConfig.getString("media_access_submit_audio_listing"));
									} else {
										setJreviewAudioUploadEnable("0");
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

								try {
									if (jreviewConfig.has("media_access_submit_attachment_listing")) {
										setJreviewAttachmentUploadEnable(jreviewConfig.getString("media_access_submit_attachment_listing"));
									} else {
										setJreviewAttachmentUploadEnable("0");
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

								try {
									if (jreviewConfig.has("favorites_enable")) {
										setJreviewFavouriteEnable(jreviewConfig.getString("favorites_enable"));
									} else {
										setJreviewFavouriteEnable("0");
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

								try {
									if (jreviewConfig.has("search_item_author")) {
										setIsAllowSearchByAuthor(jreviewConfig.getString("search_item_author"));
									} else {
										setIsAllowSearchByAuthor("0");
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

								try {
									if (jreviewConfig.has("media_photo_max_size")) {
										setJreviewArticlePhotoUploadSize(Integer.parseInt(jreviewConfig.getString("media_photo_max_size")));
									} else {
										setJreviewArticlePhotoUploadSize(4);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

								try {
									if (jreviewConfig.has("media_video_max_size")) {
										setJreviewArticleVideoUploadSize(Integer.parseInt(jreviewConfig.getString("media_video_max_size")));
									} else {
										setJreviewArticleVideoUploadSize(8);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

								try {
									if (jreviewConfig.has("media_audio_max_size")) {
										setJreviewArticleAudioUploadSize(Integer.parseInt(jreviewConfig.getString("media_audio_max_size")));
									} else {
										setJreviewArticleAudioUploadSize(0);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

								try {
									if (jreviewConfig.has("media_attachment_max_size")) {
										setJreviewArticleAttachmentUploadSize(jreviewConfig.getString("media_attachment_max_size"));
									} else {
										setJreviewArticleAttachmentUploadSize("0");
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

								try {
									if (jreviewConfig.has("media_photo_max_uploads_listing")) {
										setJreviewArticlePhotoUploadLimit(jreviewConfig.getString("media_photo_max_uploads_listing"));
									} else {
										setJreviewArticlePhotoUploadLimit("0");
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

								try {
									if (jreviewConfig.has("media_video_max_uploads_listing")) {
										setJreviewArticleVideoUploadLimit(jreviewConfig.getString("media_video_max_uploads_listing"));
									} else {
										setJreviewArticleVideoUploadLimit("0");
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

								try {
									if (jreviewConfig.has("media_audio_max_uploads_listing")) {
										setJreviewArticleAudioUploadLimit(jreviewConfig.getString("media_audio_max_uploads_listing"));
									} else {
										setJreviewArticleAudioUploadLimit("0");
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

								try {
									if (jreviewConfig.has("media_attachment_max_uploads_listing")) {
										setJreviewArticleAttachmentUploadLimit(jreviewConfig.getString("media_attachment_max_uploads_listing"));
									} else {
										setJreviewArticleAttachmentUploadLimit("0");
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

								try {
									if (jreviewConfig.has("pageLimit")) {
										setJreviewPageLimit(Integer.parseInt(jreviewConfig.getString("pageLimit")));
									} else {
										setJreviewPageLimit(0);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

								try {
									if (jreviewConfig.has("articleTotal")) {
										setJreviewTotalArticles(Integer.parseInt(jreviewConfig.getString("articleTotal")));
									} else {
										setJreviewTotalArticles(0);
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
	
	
	/** 
	 * Jreview code
	 */
	
	/**
	 * This method used to check provider execute any request call.
	 * 
	 * @return {@link boolean}
	 */
	public boolean isCalling() {
		return isCalling;
	}

	/**
	 * This Method is used to get whole jreview data.
	 * 
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void getData(final String isIntialRequest, final String lastRequestTime, final WebCallListener target) {

		if (hasNextPage()) {
			isCalling = true;
			new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {
				@Override
				protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
					IjoomerWebService iw = new IjoomerWebService();
					iw.reset();
					iw.addWSParam(EXTNAME, JREVIEW);
					iw.addWSParam(EXTVIEW, CATEGORY);
					iw.addWSParam(EXTTASK, GET_DATA);

					JSONObject taskData = new JSONObject();
					try {
						taskData.put(ISINTIALREQUEST, isIntialRequest);
						taskData.put(LASTREQUESTTIME, lastRequestTime);
						taskData.put(PAGENO, "" + getPageNo());
					} catch (Throwable e) {
						e.printStackTrace();
					}
					iw.addWSParam(TASKDATA, taskData);
					iw.WSCallGetIS(new ProgressListener() {

						@Override
						public void transferred(long num) {
							if (num >= 100) {
								target.onProgressUpdate(95);
							} else {
								target.onProgressUpdate((int) num);
							}
						}
					});

					IjoomerCaching ic = new IjoomerCaching(mContext);
					try {
						JSONObject jsonNode = iw.getJsonObject();
						setResponseCode(Integer.parseInt(jsonNode.getString("code")));
						if (Integer.parseInt(jsonNode.getString("code")) == 200) {
							try {
								setPagingParams(Integer.parseInt(jsonNode.getString(PAGELIMIT)), Integer.parseInt(jsonNode.getString(TOTAL)));
							} catch (Exception e) {
							}

							try {
								JSONObject directories = jsonNode.getJSONObject("directories");
								ic.cacheData(directories.getJSONArray("directory"), false, TABLE_JREVIEW_DIRECTORIES);
							} catch (Exception e) {
								e.printStackTrace();
							}

							try {
								JSONObject categories = jsonNode.getJSONObject("categories");
								ic.cacheData(categories.getJSONArray("category"), false, TABLE_JREVIEW_CATEGORIES);
							} catch (Exception e) {
								e.printStackTrace();
							}

							try {
								JSONObject searchCriteria = jsonNode.getJSONObject("searchlistingTypes");
								ic.cacheData(searchCriteria.getJSONArray("searchlistingType"), false, TABLE_JREVIEW_SEARCH);
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							try {
								JSONObject articles = jsonNode.getJSONObject("articles");
								ic.cacheData(articles.getJSONArray("article"), false, TABLE_JREVIEW_ARTICLES);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					} 
					return null;
				}

				@Override
				protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
					super.onPostExecute(result);
					isCalling = false;
					target.onProgressUpdate(100);
					target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
				}

			}.execute();
		} else {
			isCalling = false;
			target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
			target.onProgressUpdate(100);
		}
	}
	
	public void async_post(final String isIntialRequest, final String lastRequestTime, final WebCallListener target) {
		int totalArticles = IjoomerGlobalConfiguration.getJreviewTotalArticles();
		int pageLimit = IjoomerGlobalConfiguration.getJreviewPageLimit();
		pages = totalArticles <= 2000 ? ((totalArticles % pageLimit == 0 ? totalArticles / pageLimit : (totalArticles / pageLimit) + 1)) : ((totalArticles % pageLimit == 0 ? totalArticles / pageLimit : (totalArticles / pageLimit) + 1));
		batchs = pages % limit == 0 ? pages / limit : (pages / limit) + 1;
		pagePerBatch = pages < (limit + 1) ? pages : limit;

		local = new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {
				target.onProgressUpdate(progressCount);
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				if (responseCode == 599) {
					target.onCallComplete(599, "", null, null);
				} else if (responseCode != 1010) {
					if (count == pages) {
						target.onCallComplete(200, null, null, null);
					}else if (batchCount != batchs) {
						startDownloading(count + 1, (count + limit) > pages ? pages : (count + limit), isIntialRequest, lastRequestTime, local);
					}
				} else {
					if (count == pages) {
						target.onCallComplete(200, null, null, null);
					} 
				}
			}
		};

		startDownloading(1, limit > pages ? pages : limit, isIntialRequest, lastRequestTime, local);
	}

	private void startDownloading(final int start, final int end, final String isIntialRequest, final String lastRequestTime, final WebCallListener target) {
		List<String> pageNos = new ArrayList<String>();
		String[] pagesDonloades = null;

		List<String> pathsDownload = new ArrayList<String>();
		String[] pathsDonloades = null;
		if (SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(SP_PAGECOMPLETED, null) != null) {
			pagesDonloades = SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(SP_PAGECOMPLETED, null).split("_");
			pageNos = (List<String>) Arrays.asList(pagesDonloades);
		}

		if (start == 1) {

			if (SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(SP_PATHSDOWNLOADED, null) != null) {
				pathsDonloades = SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(SP_PATHSDOWNLOADED, null).split(";");
				pathsDownload = (List<String>) Arrays.asList(pathsDonloades);
			}

			if (pathsDonloades != null) {
				queue.addAll(pathsDownload);
				startParsingAndStoring(target);
			}
		}

		for (int i = start; i <= end; i++) {

			if (pagesDonloades != null && pageNos.contains("" + i)) {
				count++;
				if (count == end) {
					batchCount++;

					if (batchCount != batchs) {
						target.onCallComplete(200, null, null, null);
						target.onProgressUpdate((count * 100) / pages);
					} else {
					}
				} else {
					target.onProgressUpdate((count * 100) / pages);
				}

				continue;
			}

			final String url = IjoomerApplicationConfiguration.getDomainName() + new IjoomerWebService().domainTail;

			final IjoomerWebService iw = new IjoomerWebService();
			iw.reset();
			iw.addWSParam(EXTNAME, JREVIEW);
			iw.addWSParam(EXTVIEW, CATEGORY);
			iw.addWSParam(EXTTASK, GET_DATA);

			JSONObject taskData = new JSONObject();
			try {
				taskData.put(ISINTIALREQUEST, isIntialRequest);
				taskData.put(LASTREQUESTTIME, lastRequestTime);
				taskData.put(PAGENO, "" + i);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			iw.addWSParam(TASKDATA, taskData);

			final Map<String, Object> params = new HashMap<String, Object>();
			params.put("reqObject", iw.getWSParameter().toString());
			final AQuery aq = new AQuery(mContext);

			System.out.println("WSParam : " + iw.getWSParameter().toString());

			aq.ajax(url, params, String.class, new AjaxCallback<String>() {

				@Override
				public void callback(String url, String json, AjaxStatus status) {

					if (status.getCode() != 200 && !wasNetworkProblem) {
						wasNetworkProblem = true;
						IjoomerUtilities.getCustomOkDialog(mContext.getString(R.string.splash),
								mContext.getString(mContext.getResources().getIdentifier("code" + 599, "string", mContext.getPackageName())), mContext.getString(R.string.ok),
								R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

							@Override
							public void NeutralMethod() {
								target.onCallComplete(599, "", null, null);
							}
						});
					}
					System.out.println("WSResponse:" + json);

					if (json != null) {
						count++;

						if (SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(SP_PAGECOMPLETED, null) == null) {
							try {
								SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(SP_PAGECOMPLETED, iw.getWSParameter().getJSONObject(TASKDATA).getString(PAGENO));
							} catch (JSONException e) {
								e.printStackTrace();
							}
						} else {
							try {
								SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(
										SP_PAGECOMPLETED,
										SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(SP_PAGECOMPLETED, null) + "_"
												+ iw.getWSParameter().getJSONObject(TASKDATA).getString(PAGENO));
							} catch (JSONException e) {
								e.printStackTrace();
							}

						}
						try {
							String path = writeInputStreamToFile(json, iw.getWSParameter().getJSONObject(TASKDATA).getString(PAGENO));
							if (SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(SP_PATHSDOWNLOADED, null) == null) {
								SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(SP_PATHSDOWNLOADED, path);
							} else {
								SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(SP_PATHSDOWNLOADED,
										SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(SP_PATHSDOWNLOADED, null) + ";" + path);
							}
							if (queue.size() <= 0) {
								queue.add(path);
								startParsingAndStoring(target);
							} else {
								queue.add(path);
							}
							if (count == end) {
								batchCount++;

								if (batchCount != batchs) {
									target.onProgressUpdate((count * 100) / pages);
									target.onCallComplete(200, null, null, null);
								} else {
								}
							} else {
								target.onProgressUpdate((count * 100) / pages);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			});
		}
	}

	private String writeInputStreamToFile(String response, String pageNo) {

		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + pageNo;
		try {
			FileWriter file = new FileWriter(path);
			file.write(response);
			file.flush();
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return path;
	}

	private void startParsingAndStoring(final WebCallListener target) {

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				for (int i = 0; i < queue.size(); i++) {
					try {
						IjoomerCaching ic = new IjoomerCaching(mContext);

						File file = new File(queue.get(i));
						@SuppressWarnings("resource")
						FileInputStream fileInputStream = new FileInputStream(file);
						FileChannel fileChannel = fileInputStream.getChannel();
						MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
						JSONObject jsonNode = new JSONObject(Charset.forName(HTTP.UTF_8).decode(mappedByteBuffer).toString());
						setResponseCode(Integer.parseInt(jsonNode.getString("code")));

						if (Integer.parseInt(jsonNode.getString("code")) == 200) {
							try {
								setPagingParams(Integer.parseInt(jsonNode.getString(PAGELIMIT)), Integer.parseInt(jsonNode.getString(TOTAL)));
							} catch (Exception e) {
							}

							String[] pno  =queue.get(i).split("/"); 
							if (pno[pno.length-1].equals("1")) {
								try {
									JSONObject directories = jsonNode.getJSONObject("directories");
									ic.cacheData(directories.getJSONArray("directory"), false, TABLE_JREVIEW_DIRECTORIES);
								} catch (Exception e) {
									e.printStackTrace();
								}

								try {
									JSONObject categories = jsonNode.getJSONObject("categories");
									ic.cacheData(categories.getJSONArray("category"), false, TABLE_JREVIEW_CATEGORIES);
								} catch (Exception e) {
									e.printStackTrace();
								}

								try {
									JSONObject searchCriteria = jsonNode.getJSONObject("searchlistingTypes");
									ic.cacheData(searchCriteria.getJSONArray("searchlistingType"), false, TABLE_JREVIEW_SEARCH);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

							try {
								JSONObject articles = jsonNode.getJSONObject("articles");
								ic.cacheData(articles.getJSONArray("article"), false, TABLE_JREVIEW_ARTICLES);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try {
							new File(queue.get(i)).delete();
						} catch (Exception e2) {
						}
					}
				}
				
				queue.clear();
				
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				target.onCallComplete(1010, getErrorMessage(), null, null);
			}
		}.execute();

	}

}
