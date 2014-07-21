package com.ijoomer.common.classes;

import java.util.HashMap;

/**
 * This Class Contains All Method Related To IjoomerScreenHolder.
 * 
 * @author tasol
 * 
 */
public class IjoomerScreenHolder {

	@SuppressWarnings("serial")
	public static HashMap<String, String> originalScreens = new HashMap<String, String>() {
		{
			put("IcmsFavouriteArticles", "com.ijoomer.components.icms.IcmsFavouriteArticlesActivity");
			put("IcmsArchivedArticles", "com.ijoomer.components.icms.IcmsArchivedArticlesActivity");
			put("IcmsCategoryBlog", "com.ijoomer.components.icms.IcmsCategoryBlogActivity");
			put("IcmsFeaturedArticles", "com.ijoomer.components.icms.IcmsFeaturedArticlesActivity");
			put("IcmsAllCategory", "com.ijoomer.components.icms.IcmsAllCategoryActivity");
			put("IcmsSingleArticle", "com.ijoomer.components.icms.IcmsArticleDetailActivity");
			put("IcmsSingleCategory", "com.ijoomer.components.icms.IcmsCategoryActivity");
			put("Login", "com.ijoomer.src.IjoomerLoginActivity");
			put("Registration", "com.ijoomer.src.IjoomerRegistrationStep1Activity");
			put("Home", "com.ijoomer.src.IjoomerHomeActivity");
			put("Web", "com.ijoomer.common.classes.IjoomerWebviewClient");
			put("JomAlbums", "com.ijoomer.components.jomsocial.JomAlbumsActivity");
			put("JomVideo", "com.ijoomer.components.jomsocial.JomVideoActivity");
			put("JomPrivacySetting", "com.ijoomer.components.jomsocial.JomPrivacySettingActivity");
			put("JomEvent", "com.ijoomer.components.jomsocial.JomEventActivity");
			put("JomFriendList", "com.ijoomer.components.jomsocial.JomFriendListActivity");
			put("JomGroup", "com.ijoomer.components.jomsocial.JomGroupActivity");
			put("JomMessage", "com.ijoomer.components.jomsocial.JomMessageActivity");
			put("JomProfile", "com.ijoomer.components.jomsocial.JomProfileActivity");
			put("JomActivities", "com.ijoomer.components.jomsocial.JomActivitiesActivity");
			put("JomAdvanceSearch", "com.ijoomer.components.jomsocial.JomAdvanceSearchActivity");
			put("PluginsContactUs", "com.ijoomer.plugins.PluginsContactUsActivity");
			put("PluginsYoutubePlaylist", "com.ijoomer.plugins.PluginsYoutubePlaylistActivity");
			put("PluginsWeather", "com.ijoomer.plugins.PluginsWeatherLocationActivity");
			put("SobiproCategories", "com.ijoomer.components.sobipro.SobiproSectionCategoryActivity");
			put("SobiproFavourite", "com.ijoomer.components.sobipro.SobiproFavouriteActivity");
			put("SobiproAddEntry", "com.ijoomer.components.sobipro.SobiproAddEntryActivity");
			put("PluginsFacebookNearByVenues", "com.ijoomer.plugins.PluginsFacebookCheckinActivity");
			put("PluginsVimeoPlaylist", "com.ijoomer.plugins.PluginsVimeoPlaylistActivity");
			put("K2Categories", "com.ijoomer.components.k2.K2MainActivity");
			put("K2Tag", "com.ijoomer.components.k2.K2MainActivity");
			put("K2UserPage", "com.ijoomer.components.k2.K2MainActivity");
			put("K2LatestItems", "com.ijoomer.components.k2.K2MainActivity");
			put("K2Items", "com.ijoomer.components.k2.K2MainActivity");
			put("CometChat", "com.ijoomer.common.classes.IjoomerCometChat");
			put("Jbolochat", "com.ijoomer.components.jbolochat.JboloOnlineUserListActivity");
			put("DisplayAsMenu", "com.ijoomer.src.IjoomerMenuActivity");
			put("easyBlogCategory", "com.ijoomer.components.easyblog.EasyBlogAllCategoryActivity");
			put("easyBlogAllItems", "com.ijoomer.components.easyblog.EasyBlogEntriesActivity");
			put("AllDirectories", "com.ijoomer.components.jReview.JReviewAllDirectoriesActivity");
			put("JreviewsFavourite", "com.ijoomer.components.jReview.JReviewFavouriteArticlesActivity");
			put("JreviewsSearch", "com.ijoomer.components.jReview.JReviewSearchActivity");
		}
	};
	@SuppressWarnings("serial")
	public static HashMap<String, String> aliasScreens = new HashMap<String, String>() {
		{
			put("IjoomerMenuActivity", "DisplayAsMenu");
			put("IcmsFavouriteArticlesActivity", "IcmsFavouriteArticles");
			put("IcmsArchivedArticlesActivity", "IcmsArchivedArticles");
			put("IcmsCategoryBlogActivity", "IcmsCategoryBlog");
			put("IcmsFeaturedArticlesActivity", "IcmsFeaturedArticles");
			put("IcmsArticleDetailActivity", "IcmsSingleArticle");
			put("IcmsCategoryActivity", "IcmsSingleCategory");
			put("IcmsAllCategoryActivity", "IcmsAllCategory");
			put("IjoomerWebviewClient", "Web");
			put("IjoomerLoginActivity", "Login");
			put("IjoomerRegistrationStep1Activity", "Registration");
			put("IjoomerHomeActivity", "Home");
			put("JomAlbumsActivity", "JomAlbums");
			put("JomVideoActivity", "JomVideo");
			put("JomPrivacySettingActivity", "JomPrivacySetting");
			put("JomEventActivity", "JomEvent");
			put("JomFriendListActivity", "JomFriendList");
			put("JomGroupActivity", "JomGroup");
			put("JomMessageActivity", "JomMessage");
			put("JomProfileActivity", "JomProfile");
			put("JomActivitiesActivity", "JomActivities");
			put("JomAdvanceSearchActivity", "JomAdvanceSearch");
			put("PluginsContactUsActivity", "PluginsContactUs");
			put("PluginsYoutubePlaylistActivity", "PluginsYoutubePlaylist");
			put("PluginsWeatherLocationActivity", "PluginsWeather");
			put("SobiproSectionCategoryActivity", "SobiproCategories");
			put("SobiproFavouriteActivity", "SobiproFavourite");
			put("SobiproAddEntryActivity", "SobiproAddEntry");
			put("PluginsFacebookCheckinActivity", "PluginsFacebookNearByVenues");
			put("PluginsVimeoPlaylistActivity", "PluginsVimeoPlaylistActivity");
			put("K2MainActivity", "K2Categories");
			put("K2MainActivity", "K2Tag");
			put("K2MainActivity", "K2UserPage");
			put("K2MainActivity", "K2LatestItems");
			put("K2MainActivity", "K2Items");
			put("IjoomerCometChat", "CometChat");
			put("JboloOnlineUserListActivity", "Jbolochat");
			put("EasyBlogAllCategoryActivity", "easyBlogCategory");
			put("EasyBlogEntriesActivity", "easyBlogAllItems");
			put("JReviewAllDirectoriesActivity", "AllDirectories");
			put("JReviewFavouriteArticlesActivity", "JreviewsFavourite");
			put("JReviewSearchActivity", "JreviewsSearch");
		}
	};
}
