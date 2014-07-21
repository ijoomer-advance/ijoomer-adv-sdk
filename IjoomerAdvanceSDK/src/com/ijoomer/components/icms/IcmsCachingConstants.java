package com.ijoomer.components.icms;

import java.util.HashMap;

/**
 * This Class Contains All Method Related To IcmsCachingConstants.
 * 
 * @author tasol
 * 
 */
@SuppressWarnings("serial")
public class IcmsCachingConstants {

	public static HashMap<String, String> getUnnormlizeFields() {
		HashMap<String, String> unNormalizeFields = new HashMap<String, String>() {
			{
				put("urls", "");// icms
			}
		};
		return unNormalizeFields;
	}
}
