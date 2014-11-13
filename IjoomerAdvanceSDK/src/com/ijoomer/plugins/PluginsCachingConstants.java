package com.ijoomer.plugins;

import java.util.HashMap;

/**
 * This Class Contains UnNormalizeFields PluginsCachingConstants.
 * 
 * @author tasol
 * 
 */
@SuppressWarnings("serial")
public class PluginsCachingConstants {
	public static HashMap<String, String> getUnnormlizeFields() {
		HashMap<String, String> unNormalizeFields = new HashMap<String, String>() {
			{
				put("player", "");// plugin
				put("content", "");// plugin
				put("contentRating", "");// plugin
				put("video", "");// plugin
				put("links", "");// plugin
				put("location", "");// plugin facebook
				put("category_list", "");// plugin facebook
				put("weatherDesc", "");// plugin Weather
				put("weatherIconUrl", "");// plugin Weather
			}
		};
		return unNormalizeFields;
	}
}
