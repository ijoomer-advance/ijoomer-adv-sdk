package com.ijoomer.components.k2;

import java.util.HashMap;

/**
 * This Class Contains UnNormalizeFields K2CachingConstants.
 * 
 * @author tasol
 * 
 */
@SuppressWarnings("serial")
public class K2CachingConstants {

	public static HashMap<String, String> getUnnormlizeFields() {
		HashMap<String, String> unNormalizeFields = new HashMap<String, String>() {
			{
				put("imageGalleries","");
				put("tags","");
				put("comments","");
				put("attachments","");
				put("field","");
				put("extraFields","");
				put("fields","");
				put("ratings","");
			}
		};
		return unNormalizeFields;
	}
}
