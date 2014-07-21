package com.ijoomer.common.configuration;

import android.content.Context;
import android.graphics.Typeface;

import com.ijoomer.common.classes.IjoomerScreenHolder;
import com.ijoomer.custom.interfaces.IjoomerSharedPreferences;
import com.ijoomer.src.R;
import com.smart.framework.SmartApplication;

/**
 * This Class Contains All Method Related To IjoomerApplicationConfiguration.
 * 
 * @author tasol
 * 
 */
public abstract class IjoomerApplicationConfiguration implements IjoomerSharedPreferences {

	private static Context mContext;
	public static Typeface fontFace;
	public static String domainName;
	public static String twitterSecretKey;
	public static String twitterConsumerKey;
	public static String dateTimeFormat;
	public static String dateFormat;
	public static String timeFormat;
	public static String loginActivityName;
	public static String GCMProjectId;
	public static String fontNameWithPath;
	public static boolean uploadMultiplePhotos;
	public static boolean isCachEnable;
	public static boolean isReloadRequired;
	public static boolean debugOn;
	public static boolean tabbarWithoutCaption;
	public static boolean tabbarWithoutImage;
	public static boolean showYoutubeVideoTitle;
	public static boolean showYoutubeVideoDescription;
	public static boolean showYoutubeVideoViews;
	public static boolean isEnableVoiceReport;
    public static boolean isEnableSmiley;
	public static int theme;
	public static boolean enableHttps;

	/**
	 * This method used to set default configuration.
	 * 
	 * @param context
	 *            represented {@link Context}
	 */
	public static void setDefaultConfiguration(Context context) {
		try {
			if (twitterConsumerKey == null) {
				mContext = context;
				isReloadRequired = false;
				domainName = getDomainName();
				twitterConsumerKey = context.getString(R.string.twitter_consumer_key);
				twitterSecretKey = context.getString(R.string.twitter_secret_key);
				isCachEnable = Boolean.parseBoolean(context.getString(R.string.is_cache_enable));
				tabbarWithoutCaption = Boolean.parseBoolean(context.getString(R.string.tabbar_without_caption));
				tabbarWithoutImage = Boolean.parseBoolean(context.getString(R.string.tabbar_without_image));
				dateFormat = context.getString(R.string.date_format);
				dateTimeFormat = context.getString(R.string.date_time_format);
				timeFormat = context.getString(R.string.time_format);
				uploadMultiplePhotos = Boolean.parseBoolean(context.getString(R.string.upload_multiple_photos));
				debugOn = Boolean.parseBoolean(context.getString(R.string.debug_on));
				GCMProjectId = context.getString(R.string.gcm_id);
				fontNameWithPath = context.getString(R.string.font_name_with_path);
				showYoutubeVideoTitle = Boolean.parseBoolean(context.getString(R.string.show_youtube_video_title));
				showYoutubeVideoDescription = Boolean.parseBoolean(context.getString(R.string.show_youtube_video_description));
				showYoutubeVideoViews = Boolean.parseBoolean(context.getString(R.string.show_youtube_video_views));
				setEnableVoiceReport(true);
				setEnableSmiley(true);
				enableHttps=false;

				// Activity set up based on Version.
				if (IjoomerGlobalConfiguration.getJomsocialVersion().equals(IjoomerGlobalConfiguration.JOMVERSION_V30)) {
					IjoomerScreenHolder.originalScreens.put("Registration", "com.ijoomer.src.IjoomerRegistrationStep1Activity_v30");
					IjoomerScreenHolder.aliasScreens.put("IjoomerRegistrationStep1Activity_v30", "Registration");
				}

			}
		} catch (Exception e) {
		}

	}

	/**
	 * This method used to is enable voice report.
	 * 
	 * @return represented {@link Boolean}
	 */
	public static boolean isEnableVoiceReport() {
		return isEnableVoiceReport;
	}

	/**
	 * This method used to set enable voice report.
	 * 
	 * @param isEnableVoiceReport
	 *            represented is enable voice report
	 */
	public static void setEnableVoiceReport(boolean isEnableVoiceReport) {
		IjoomerApplicationConfiguration.isEnableVoiceReport = isEnableVoiceReport;
	}

    /**
     * This method used to set enable voice report.
     *
     * @param isEnableSmiley
     *            represented is enable voice report
     */
    public static void setEnableSmiley(boolean isEnableSmiley) {
        IjoomerApplicationConfiguration.isEnableSmiley = isEnableSmiley;
    }

	/**
	 * This method used to get GCM project id.
	 * 
	 * @return represented {@link String}
	 */
	public static String getGCMProjectId() {
		return GCMProjectId;
	}

	/**
	 * This method used to get font name with path.
	 * 
	 * @return represented {@link String}
	 */
	public static String getFontNameWithPath() {
		return fontNameWithPath;
	}

	/**
	 * This method used to set font with path.
	 * 
	 * @param fontNameWithPath
	 *            represented font name with path
	 */
	public static void setFontNameWithPath(String fontNameWithPath) {
		IjoomerApplicationConfiguration.fontNameWithPath = fontNameWithPath;
	}

	/**
	 * This method used to get font face.
	 * 
	 * @return represented {@link Typeface}
	 */
	public static Typeface getFontFace() {
		return fontFace;
	}

	/**
	 * This method used to set font face.
	 * 
	 * @param fontFace
	 *            represented font face
	 */
	public static void setFontFace(Typeface fontFace) {
		IjoomerApplicationConfiguration.fontFace = fontFace;
	}

	/**
	 * This method used to is debug on.
	 * 
	 * @return represented {@link Boolean}
	 */
	public static boolean isDebugOn() {
		return debugOn;
	}

	/**
	 * This method used to set debug on.
	 * 
	 * @param debugOn
	 *            represented debg on
	 */
	public static void setDebugOn(boolean debugOn) {
		IjoomerApplicationConfiguration.debugOn = debugOn;
	}

	/**
	 * This method used to is reload required.
	 * 
	 * @return represented {@link Boolean}
	 */
	public static boolean isReloadRequired() {
		return isReloadRequired;
	}

	/**
	 * This method used to set is reload required.
	 * 
	 * @param isReloadRequired
	 *            represented reload required
	 */
	public static void setReloadRequired(boolean isReloadRequired) {
		IjoomerApplicationConfiguration.isReloadRequired = isReloadRequired;
	}

	/**
	 * this method used to get is mulit-photos upload .
	 * 
	 * @return represented {@link Boolean}
	 */
	public static boolean isUploadMultiplePhotos() {
		return uploadMultiplePhotos;
	}

	/**
	 * This method used to set is multi-photos upload
	 * 
	 * @param uploadMultiplePhotos
	 *            represented upload mulit-photos
	 */
	public static void setUploadMultiplePhotos(boolean uploadMultiplePhotos) {
		IjoomerApplicationConfiguration.uploadMultiplePhotos = uploadMultiplePhotos;
	}

	/**
	 * This method used to get login activity name.
	 * 
	 * @return represented {@link String}
	 */
	public static String getLoginActivityName() {
		return IjoomerApplicationConfiguration.loginActivityName;
	}

	/**
	 * This method used to set login activity name.
	 * 
	 * @param loginActivityName
	 *            represented login activity namer
	 */
	public static void setLoginActivityName(String loginActivityName) {
		IjoomerApplicationConfiguration.loginActivityName = loginActivityName;
	}

	/**
	 * This method used to is cache enable.
	 * 
	 * @return represented {@link Boolean}
	 */
	public static boolean isCachEnable() {
		return isCachEnable;
	}

	/**
	 * This method used to set cache enable.
	 * 
	 * @param isCachEnable
	 *            represented cache enable
	 */
	public static void setCachEnable(boolean isCachEnable) {
		IjoomerApplicationConfiguration.isCachEnable = isCachEnable;
	}

	/**
	 * This method used to get theme.
	 * 
	 * @return represented {@link Integer}
	 */
	public static int getTheme() {
		return theme;
	}

	/**
	 * This method used to set theme.
	 * 
	 * @param theme
	 *            represented theme
	 */
	public static void setTheme(int theme) {
		IjoomerApplicationConfiguration.theme = theme;
	}

	/**
	 * This method used to get domain name.
	 * 
	 * @return represented {@link String}
	 */
	public static String getDomainName() {
		try {

			return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(SP_CLIENT_DOMAIN,
					mContext.getString(R.string.domain_name));
		} catch (Exception e) {
		}
		return null;

	}

	/**
	 * This method used to set domain name.
	 * 
	 * @param domainName
	 *            represented domain name
	 */
	public static void setDomainName(String domainName) {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(SP_CLIENT_DOMAIN, domainName);
		IjoomerApplicationConfiguration.domainName = domainName;
	}

	/**
	 * This method used to get twitter secret key.
	 * 
	 * @return represented {@link String}
	 */
	public static String getTwitterSecretKey() {
		return twitterSecretKey;
	}

	/**
	 * This method used to set twitter secret key.
	 * 
	 * @param twitterSecretKey
	 *            twitter secret key
	 */
	public static void setTwitterSecretKey(String twitterSecretKey) {
		IjoomerApplicationConfiguration.twitterSecretKey = twitterSecretKey;
	}

	/**
	 * This method used to get twitter consumer key.
	 * 
	 * @return represented {@link String}
	 */
	public static String getTwitterConsumerKey() {
		return twitterConsumerKey;
	}

	/**
	 * This method used to set twitter consumer key.
	 * 
	 * @param twitterConsumerKey
	 *            twitter consumer key
	 */
	public static void setTwitterConsumerKey(String twitterConsumerKey) {
		IjoomerApplicationConfiguration.twitterConsumerKey = twitterConsumerKey;
	}

}
