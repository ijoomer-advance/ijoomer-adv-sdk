package com.ijoomer.custom.interfaces;

public interface IjoomerSharedPreferences {

	/*
	 * Global Configuration Preferences
	 */

	public static String SP_GC_LOGIN_REQUIRED = "loginRequired";
	public static String SP_GC_REGISTRATION = "registration";
	public static String SP_GC_SELECT_THEME = "selectTheme";
	public static String SP_GC_THEME = "theme";
	public static String SP_DEFAULT_DOWNLOAD_LOCATION = "defaultDownloadLocation";
	public static String SP_URL_SETTING = "urlsetting";
	public static String SP_LAST_ACTIVITY = "lastActivity";
	public static String SP_DEFAULT_LANDING_SCREEN = "defaultLandingScreen";

	/*
	 * User Authentication Preferences
	 */
	public static String SP_USERNAME = "userName";
	public static String SP_PASSWORD = "password";
	public static String SP_CLIENT_DOMAIN = "clientDomain";
	public static String SP_LATITUDE = "latitude";
	public static String SP_LONGITUDE = "longitude";
	public static String SP_REMEMBERME = "rememberMe";
	public static String SP_ISLOGOUT = "isLoggedOut";
	public static String SP_ISFACEBOOKLOGIN = "isFacebookLogin";
	public static String SP_LOGIN_REQ_OBJECT = "loginReqObject";

	/*
	 * Twitter Preferences
	 */

	public static String SP_TWITTER_TOKEN = "token";
	public static String SP_TWITTER_SECRET_TOKEN = "secretToken";

	/*
	 * FaceBook Preferences
	 */

	public static String SP_FACEBOOK_TOKEN = "fb_token";
	public static String SP_FACEBOOK_RESPONSE = "fb_response";

}
