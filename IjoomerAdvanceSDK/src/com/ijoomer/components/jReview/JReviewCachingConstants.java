package com.ijoomer.components.jReview;

import java.util.HashMap;

public class JReviewCachingConstants {

	public static HashMap<String, String> getUnnormlizeFields() {
		@SuppressWarnings("serial")
		HashMap<String, String> unNormalizeFields = new HashMap<String, String>() {
			{
				put("jreviewMediaAttachments", "");
				put("jreviewMediaImage", "");
				put("jreviewMediaVideo", "");
				put("jreviewImages", "");
				put("jreviewThumbImages", "");
				put("jreview_group", "");
				put("fields", "");
				put("options", "");
				put("reviews", "");
				put("reviewVotes", "");
				put("reviewComment", "");
				put("averageReviewCriteria", "");
				put("criteria", "");
				put("pricePlans", "");
				put("plans", "");
			}
		};
		return unNormalizeFields;
	}

}
