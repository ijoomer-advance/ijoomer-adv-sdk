package com.ijoomer.custom.interfaces;

/**
 * This Interface Contains All Method Related To IjoomerSharedPreferences.
 * 
 * @author tasol
 * 
 */
public interface IjoomerSharedPreferences {
	/*
	 * Http access
	 */

	public static String SP_HTTP_ACCESSS_USERNAME = "httpAccessUserName";
	public static String SP_HTTP_ACCESSS_PASSWORD = "httpAccessPassword";
	public static String SP_HTTP_ACCESSS_REMEMBER = "httpAccessIsRemember";

	/*
	 * GCM Preferences
	 */

	public static String SP_GCM_REGID = "gcmRegId";
	public static String SP_GCM_APP_VERSION = "gcmAppVersion";
	public static String SP_GCM_ERROR_DIALOG = "gcmErrorDialog";

	/*
	 * Global Configuration Preferences
	 */

	public static String SP_DEFAULT_DOWNLOAD_LOCATION = "defaultDownloadLocation";
	public static String SP_URL_SETTING = "urlsetting";
	public static String SP_LAST_ACTIVITY = "lastActivity";
	public static String SP_LAST_ACTIVITY_INTENT = "lastActivityIntent";
	public static String SP_DEFAULT_LANDING_SCREEN = "defaultLandingScreen";
	public static String SP_ICON_PRELOADER = "iconPreloader";
	public static String SP_LAST_REQUEST_DATE="lastRequestDate";

	/*
	 * User Authentication Preferences
	 */
	public static String SP_USERNAME = "userName";
	public static String SP_DOLOGIN = "dologin";
	public static String SP_PASSWORD = "password";
	public static String SP_CLIENT_DOMAIN = "clientDomain";
	public static String SP_ISLOGOUT = "isLoggedOut";
	public static String SP_ISFACEBOOKLOGIN = "isFacebookLogin";
	public static String SP_LOGIN_REQ_OBJECT = "loginReqObject";

	/*
	 * Twitter Preferences
	 */

	public static String SP_TWITTER_TOKEN = "token";
	public static String SP_TWITTER_SECRET_TOKEN = "secretToken";

	/**
	 * Application Config
	 */

	public static String SP_ISENABLEJBOLO = "isEnableJbolo";
	public static String SP_ISENABLECOMMENTK2 = "isEnableCommentK2";
	public static String SP_ISENABLEJREVIEW = "isEnableJreview";
	public static String SP_ISENABLEVOICE = "isEnableVoice";
	public static String SP_MAXAUDIOLENGTH = "maxAudioLength";
	public static String SP_TERMSOBJECT = "termsObject";
	public static String SP_ISENABLETERMS = "isEnableTerms";
	public static String SP_PHOTOUPLOADSIZE = "photoUploadSize";
	public static String SP_VIDEOUPLOADSIZE = "videoUploadSize";
	public static String SP_ISVIDEOUPLOAD = "isVideoUpload";
	public static String SP_ISEVENTCREATE = "isEventCreate";
	public static String SP_ISGROUPCREATE = "isGroupCreate";
	public static String SP_ISPHOTOUPLOAD = "isPhotoUpload";
	public static String SP_SERVERTIMEZONE = "serverTimeZone";
	public static String SP_ISLOGINREQUIRED = "isLoginRequired";
	public static String SP_ALLOWREGISTRATION = "allowRegistration";
	public static String SP_ALLOWTHEMESELECTION = "allowThemeSelection";
	public static String SP_REGISTRATIONWITH = "registrationWith";
	public static String SP_DEFAULTAVATAR = "defaultAvatar";
	public static String SP_DEFAULTAVATAR_FEMALE = "defaultAvatarFemale";
	public static String SP_JOM_VERSION = "jomVersion";
	public static String SP_JBOLO_CHAT_GET_HISTORY = "jboloChatGetHistory";
	public static String SP_JBOLO_CHAT_SEND_FILE = "jboloChatSendFile";
	public static String SP_JBOLO_CHAT_SEND_FILE_MAX_LIMIT = "jboloChatSendFileMaxLimit";
	public static String SP_JBOLO_CHAT_GROUP_CHAT = "jboloChatGroupChat";

	/**
	 *Plugins Weahter 
	 */
	public static String SP_LOCATION_ID = "locationID";

	/*
	 * JReview Preferences
	 */
	public static String SP_RELOADREVIEWS = "reloadreviews";
	public static String SP_RELOADARTICLEDETAILS = "reloadarticledetails";
	public static String SP_RELOADARTICLES = "reloadarticles";
	public static String SP_RELOADFAVUORITEARTICLES = "reloadfavouritearticles";
	public static String SP_PAGECOMPLETED = "pageCompleted";
	public static String SP_PATHSDOWNLOADED= "pathsDownloaded";

	/*
	 * JBoloChat Preferences
	 */
	public static String SP_JBOLOCHAT_USER_STATUS = "userStatus";
	public static String SP_JBOLOCHAT_USER_STATUS_MESSAGE = "userStatusMessage";

}
