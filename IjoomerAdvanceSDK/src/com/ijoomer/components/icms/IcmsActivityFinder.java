package com.ijoomer.components.icms;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ijoomer.common.configuration.IjoomerComponentInfo;
import com.ijoomer.src.R;

/**
 * This Class Contains All Method Related To IcmsActivityFinder.
 * 
 * @author tasol
 * 
 */
public class IcmsActivityFinder {

	private static Context mContext;
	private static String mUrl;
	private static String component_name;

	/**
	 * This method used to find activity from url.
	 * 
	 * @param context
	 *            represented {@link Context}
	 * @param url
	 *            represented url
	 * @return represented {@link Intent}
	 */
	public static Intent findActivityFromUrl(Context context, String url) {
		mContext = context;
		mUrl = url;

		if (isInternalUrl()) {

			if (IjoomerComponentInfo.installedComponents.containsKey(getComponentName())) {
				return parseUrl(getComponentName());
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * This method used to check is internal url.
	 * 
	 * @return represented {@link Boolean}
	 */
	private static boolean isInternalUrl() {
		if (mUrl.contains("option=")) {
			mUrl = mUrl.substring(mUrl.indexOf("option="));
			int startIndex = mUrl.indexOf("option=");
			int endIndex = mUrl.indexOf("&", startIndex);

			component_name = mUrl.substring(startIndex + 7, endIndex);
			Log.e("component_name", component_name + " " + startIndex + " " + endIndex);

			if (IjoomerComponentInfo.installedComponents.containsKey(component_name))
				return true;

		}
		return false;
	}

	/**
	 * This method used to get component name.
	 * 
	 * @return represented {@link String}
	 */
	private static String getComponentName() {
		return component_name;
	}

	/**
	 * This method used to parse url.
	 * 
	 * @param componentName
	 *            represented component name
	 * @return represented {@link Intent}
	 */
	private static Intent parseUrl(String componentName) {
		int component = IjoomerComponentInfo.installedComponents.get(componentName);
		switch (component) {
		case 1:
			return getIcmsActivity();
		default:
			return null;
		}
	}

	/**
	 * This method used to get icms activity.
	 * 
	 * @return represented {@link Intent}
	 */
	private static Intent getIcmsActivity() {

		HashMap<String, String> urlvalues = new HashMap<String, String>();
		String[] urlSpit = mUrl.split("&");
		int size = urlSpit.length;
		for (int i = 0; i < size; i++) {
			String[] spitstr = urlSpit[i].split("=");
			urlvalues.put(spitstr[0], spitstr[1]);
		}
		Intent intent;
		String view = urlvalues.get("view");

		if (view.equalsIgnoreCase("featured")) {

			intent = new Intent(mContext, IcmsFeaturedArticlesActivity.class);
			return intent;

		} else if (view.equalsIgnoreCase("article")) {

			intent = new Intent(mContext, IcmsArticleDetailActivity.class);
			String[] id_title_spit = urlvalues.get("id").split(":");
			try {
				intent.putExtra("IN_ARTICLE_INDEX", "0");
				ArrayList<String> IN_ARTICLE_ID_ARRAY = new ArrayList<String>();
				IN_ARTICLE_ID_ARRAY.add(id_title_spit[0]);
				intent.putExtra("IN_ARTICLE_ID_ARRAY", IN_ARTICLE_ID_ARRAY);
			} catch (Exception e) {

			}
			return intent;

		} else if (view.equalsIgnoreCase("category")) {
			if (urlvalues.containsKey("layout")) {
				intent = new Intent(mContext, IcmsCategoryBlogActivity.class);
				intent.putExtra("IN_CATEGORYBLOG_ID", urlvalues.get("id"));
				return intent;
			} else {

				HashMap<String, String> value = new HashMap<String, String>();

				try {
					if (urlvalues.get("id") != null) {
						value.put("categoryid", urlvalues.get("id"));
					} else {
						value.put("categoryid", "0");
					}

				} catch (Exception e) {

					value.put("categoryid", "0");
					e.printStackTrace();
				}
				try {
					if (urlvalues.get("title") != null) {
						value.put("title", urlvalues.get("title"));
					} else {
						value.put("title", mContext.getString(R.string.category));
					}
				} catch (Exception e) {

					value.put("title", mContext.getString(R.string.category));
					e.printStackTrace();
				}

				intent = new Intent(mContext, IcmsCategoryActivity.class);
				intent.putExtra("IN_PARENTCATEGORY", value);
				return intent;
			}

		} else if (view.equalsIgnoreCase("archive")) {
			intent = new Intent(mContext, IcmsArchivedArticlesActivity.class);
			return intent;

		} else if (view.equalsIgnoreCase("categories")) {
			intent = new Intent(mContext, IcmsCategoryActivity.class);
			intent.putExtra("parentCategory", urlvalues.get("id"));
			return intent;
		}

		return null;
	}

}
