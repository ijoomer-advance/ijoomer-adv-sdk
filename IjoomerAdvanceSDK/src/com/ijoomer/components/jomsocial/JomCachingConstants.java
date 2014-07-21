package com.ijoomer.components.jomsocial;

import java.util.HashMap;

/**
 * This class contains unNormalizeFields JomCachingConstants.
 * 
 * @author tasol
 * 
 */
@SuppressWarnings("serial")
public class JomCachingConstants {

	public static HashMap<String, String> getUnnormlizeFields() {
		HashMap<String, String> unNormalizeFields = new HashMap<String, String>() {
			{
				put("menu", "");// jom
				put("adminMenu", "");// jom
				put("options", "");// jom
				put("option", "");// jom
				put("group_data", "");// jom
				put("event_data", "");// jom
				put("image_data", "");// jom
				put("privacy", "");// jom
				put("profile_video", "");// jom
				put("condition", "");// jom
				put("content_data", "");// jom
			}
		};
		return unNormalizeFields;
	}
	public static HashMap<String, String> getUnRepetedField() {
		HashMap<String, String> unNormalizeFields = new HashMap<String, String>() {
			{
				put("content_data", "");// jom
				put("image_data", "");// jom
				put("group_data", "");// jom
				put("event_data", "");// jom

			}
		};
		return unNormalizeFields;
	}
}
