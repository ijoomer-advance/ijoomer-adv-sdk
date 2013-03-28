package com.ijoomer.common.classes;

import java.util.HashMap;

public class IjoomerScreenHolder {
	
	
	public static HashMap<String, String> originalScreens = new HashMap<String, String>() {
		{
			put("IcmsFavouriteArticles","com.ijoomer.components.icms.IcmsFavouriteArticlesActivity");
			put("IcmsArchivedArticles","com.ijoomer.components.icms.IcmsArchivedArticlesActivity");
			put("IcmsCategoryBlog","com.ijoomer.components.icms.IcmsCategoryBlogActivity");
			put("IcmsFeaturedArticles","com.ijoomer.components.icms.IcmsFeaturedArticlesActivity");
			put("IcmsAllCategory","com.ijoomer.components.icms.IcmsAllCategoryActivity");
			put("IcmsSingleArticle","com.ijoomer.components.icms.IcmsArticleDetailActivity");
			put("IcmsSingleCategory","com.ijoomer.components.icms.IcmsCategoryActivity");
			put("Login","com.ijoomer.src.IjoomerLoginActivity");
			put("Registration","com.ijoomer.src.IjoomerRegistrationStep1Activity");
			put("Home","com.ijoomer.src.IjoomerHomeActivity");
			put("Web","com.ijoomer.common.classes.IjoomerWebviewClient");
			put("JomAlbums","com.ijoomer.components.jomsocial.JomAlbumsActivity");
			put("JomVideo","com.ijoomer.components.jomsocial.JomVideoActivity");
			put("JomPrivacySetting","com.ijoomer.components.jomsocial.JomPrivacySettingActivity");
			put("JomEvent","com.ijoomer.components.jomsocial.JomEventActivity");
			put("JomFriendList","com.ijoomer.components.jomsocial.JomFriendListActivity");
			put("JomGroup","com.ijoomer.components.jomsocial.JomGroupActivity");
			put("JomMessage","com.ijoomer.components.jomsocial.JomMessageActivity");
			put("JomProfile","com.ijoomer.components.jomsocial.JomProfileActivity");
			put("JomActivities","com.ijoomer.components.jomsocial.JomActivitiesActivity");

		}
	};
	public static HashMap<String, String> aliasScreens = new HashMap<String, String>() {
		{
			put("IcmsFavouriteArticlesActivity","IcmsFavouriteArticles");
			put("IcmsArchivedArticlesActivity","IcmsArchivedArticles");
			put("IcmsCategoryBlogActivity","IcmsCategoryBlog");
			put("IcmsFeaturedArticlesActivity","IcmsFeaturedArticles");
			put("IcmsArticleDetailActivity","IcmsSingleArticle");
			put("IcmsCategoryActivity","IcmsSingleCategory");
			put("IcmsAllCategoryActivity","IcmsAllCategory");
			put("IjoomerWebviewClient","Web");
			put("IjoomerLoginActivity","Login");
			put("IjoomerRegistrationStep1Activity","Registration");
			put("IjoomerHomeActivity","Home");
			put("JomAlbumsActivity","JomAlbums");
			put("JomVideoActivity","JomVideo");
			put("JomPrivacySettingActivity","JomPrivacySetting");
			put("JomEventActivity","JomEvent");
			put("JomFriendListActivity","JomFriendList");
			put("JomGroupActivity","JomGroup");
			put("JomMessageActivity","JomMessage");
			put("JomProfileActivity","JomProfile");
			put("JomActivitiesActivity","JomActivities");
		}
	};
}
