package com.ijoomer.common.classes;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ijoomer.common.configuration.IjoomerComponentInfo;
import com.ijoomer.components.icms.IcmsArchivedArticlesActivity;
import com.ijoomer.components.icms.IcmsArticleDetailActivity;
import com.ijoomer.components.icms.IcmsCategoryActivity;
import com.ijoomer.components.icms.IcmsCategoryBlogActivity;
import com.ijoomer.components.icms.IcmsFeaturedArticlesActivity;
import com.ijoomer.src.R;

public class IjoomerActivityFinder {

	private static Context mContext;
	private static String mUrl;
	static String component_name;

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

	private static boolean isInternalUrl() {
		// 01-19 12:40:26.533: E/webview_url(666):
		// http://help.joomla.org/proxy/index.php?option=com_help&keyref=Help16:Components_Redirect_Manager

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

	private static String getComponentName() {
		return component_name;
	}

	private static Intent parseUrl(String componentName) {
		int component = IjoomerComponentInfo.installedComponents.get(componentName);
		switch (component) {
		case 1:
			return getIcmsActivity();
		default:
			return null;
		}
	}

	private static Intent getIcmsActivity() {

		// option=com_content&Itemid=435&view=featured
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
				intent.putExtra("IN_ARTICLE_ID", id_title_spit[0]);
				intent.putExtra("IN_ARTICLE_TITLE", id_title_spit[1]);
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
					// TODO Auto-generated catch block
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
					// TODO Auto-generated catch block
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
		// Intent intent = new Intent();
		// intent.setClassName(mContext,
		// IjoomerApplicationConfiguration.getLoginActivityName());
		// return intent;
	}

}
