package com.ijoomer.components.sobipro;

import java.util.HashMap;

/**
 * This class contains unNormalizeFields SobiproCachingConstants.
 * 
 * @author tasol
 * 
 */
@SuppressWarnings("serial")
public class SobiproCachingConstants {

	public static HashMap<String, String> getUnnormlizeFields() {
		HashMap<String, String> unNormalizeFields = new HashMap<String, String>() {
			{
				put("categories", "");
				put("value", "");
				put("reviewrating", "");
				put("img_galleries", "");
				put("criterionaverage", "");
				put("ratings", "");
				put("options","");
				put("from","");
				put("to","");
				put("location", "");
				put("category_list", "");
				
				
			}
		};
		return unNormalizeFields;
	}
}
