package com.ijoomer.components.easyblog;

import java.util.HashMap;

/**
 * This Class Contains All Method Related To EasyBlogCachingConstants.
 * 
 * @author tasol
 * 
 */
@SuppressWarnings("serial")
public class EasyBlogCachingConstants {

	public static HashMap<String, String> getUnnormlizeFields() {
		HashMap<String, String> unNormalizeFields = new HashMap<String, String>() {
			{
				put("urls", "");
                put("options", "");
			}
		};
		return unNormalizeFields;
	}
}
