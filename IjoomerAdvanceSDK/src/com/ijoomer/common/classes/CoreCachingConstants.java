package com.ijoomer.common.classes;

import java.util.HashMap;

/**
 * This Class Contains UnNormalizeFields CoreCachingConstants.
 * 
 * @author tasol
 * 
 */
@SuppressWarnings("serial")
public class CoreCachingConstants {

	public static HashMap<String, String> getUnnormlizeFields() {
		HashMap<String, String> unNormalizeFields = new HashMap<String, String>() {
			{
				put("menu", "");// core
				put("menuitem", "");// core
				put("options", "");// core
				put("privacy", "");// core
				put("screens", "");
			}
		};
		return unNormalizeFields;
	}
}
