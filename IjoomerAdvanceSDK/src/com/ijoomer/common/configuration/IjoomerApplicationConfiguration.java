package com.ijoomer.common.configuration;

import org.json.JSONObject;

import android.graphics.Typeface;

import com.ijoomer.common.classes.Theme;
import com.ijoomer.custom.interfaces.IjoomerSharedPreferences;
import com.smart.framework.SmartApplication;

public abstract class IjoomerApplicationConfiguration implements IjoomerSharedPreferences {
	public static String domainName;
	public static String twitterSecretKey;
	public static String twitterConsumerKey;
	public static int theme;
	public static boolean isCachEnable;
	public static String dateTimeFormat;
	public static String dateFormat;
	public static String timeFormat;
	public static String loginActivityName;
	public static boolean uploadMultiplePhotos;
	public static boolean isReloadRequired;
	public static boolean debugOn;

	public static Typeface fontFace;
	public static String fontNameWithPath;

	public static String getFontNameWithPath() {
		return fontNameWithPath;
	}

	public static void setFontNameWithPath(String fontNameWithPath) {
		IjoomerApplicationConfiguration.fontNameWithPath = fontNameWithPath;
	}

	public static Typeface getFontFace() {
		return fontFace;
	}

	public static void setFontFace(Typeface fontFace) {
		IjoomerApplicationConfiguration.fontFace = fontFace;
	}

	public static boolean isDebugOn() {
		return debugOn;
	}

	public static void setDebugOn(boolean debugOn) {
		IjoomerApplicationConfiguration.debugOn = debugOn;
	}

	public static boolean isReloadRequired() {
		return isReloadRequired;
	}

	public static void setReloadRequired(boolean isReloadRequired) {
		IjoomerApplicationConfiguration.isReloadRequired = isReloadRequired;
	}

	public static boolean isUploadMultiplePhotos() {
		return uploadMultiplePhotos;
	}

	public static void setUploadMultiplePhotos(boolean uploadMultiplePhotos) {
		IjoomerApplicationConfiguration.uploadMultiplePhotos = uploadMultiplePhotos;
	}

	public static String getLoginActivityName() {
		return IjoomerApplicationConfiguration.loginActivityName;
	}

	public static void setLoginActivityName(String loginActivityName) {
		IjoomerApplicationConfiguration.loginActivityName = loginActivityName;
	}

	public static boolean isCachEnable() {
		return isCachEnable;
	}

	public static void setCachEnable(boolean isCachEnable) {
		IjoomerApplicationConfiguration.isCachEnable = isCachEnable;
	}

	public static void setDefaultConfiguration() {
		isReloadRequired = false;
		domainName = getDomainName();
		twitterConsumerKey = "ACGuGZRQI4rASvX4uHgDw";
		twitterSecretKey = "n2zv5dXGbvav3FCb63sk3rIYH8zz74is69dUkINlsgg";

		theme = Theme.RED;
		isCachEnable = true;
		dateFormat = "yyyy-MM-dd";
		dateTimeFormat = "yyyy-MM-dd kk:mm:ss";
		timeFormat = "kk:mm:ss";
		loginActivityName = "com.ijoomer.src.IjoomerLoginActivity";
		uploadMultiplePhotos = true;
		debugOn = true;

		fontNameWithPath = "fonts/Helvetica_LT_55_Roman_0.ttf";

	}

	public static int getTheme() {
		return theme;
	}

	public static void setTheme(int theme) {
		IjoomerApplicationConfiguration.theme = theme;
	}

	public static String getDomainName() {
		// return
		// SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(SP_CLIENT_DOMAIN,
		// "http://192.168.5.156/development/ijoomer2.0/");
		return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(SP_CLIENT_DOMAIN, "http://www.ijoomer.com/ijoomeradv/");

	}

	public static void setDomainName(String domainName) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(SP_CLIENT_DOMAIN, domainName);
		IjoomerApplicationConfiguration.domainName = domainName;
	}

	public static String getTwitterSecretKey() {
		return twitterSecretKey;
	}

	public static void setTwitterSecretKey(String twitterSecretKey) {
		IjoomerApplicationConfiguration.twitterSecretKey = twitterSecretKey;
	}

	public static String getTwitterConsumerKey() {
		return twitterConsumerKey;
	}

	public static void setTwitterConsumerKey(String twitterConsumerKey) {
		IjoomerApplicationConfiguration.twitterConsumerKey = twitterConsumerKey;
	}

}
