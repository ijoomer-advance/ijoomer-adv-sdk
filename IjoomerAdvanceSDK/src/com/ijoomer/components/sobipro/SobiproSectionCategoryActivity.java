package com.ijoomer.components.sobipro;

import com.ijoomer.src.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Activity class for SobiproSectionCategoryActivity view
 * 
 * @author tasol
 * 
 */

public class SobiproSectionCategoryActivity extends SobiproMasterActivity implements SobiproTagHolder {
	private String IN_SECTION_ID;
	private String IN_CAT_ID = "0";
	private JSONObject IN_OBJ;
	private String IN_PAGELAYOUT;
	private String IN_FEATUREDFIRST = "No";

	private ArrayList<String> pageLayouts;

	@Override
	public int setLayoutId() {
		return R.layout.sobipro_section_category;
	}

	@Override
	public void initComponents() {
		getIntentData();
		pageLayouts = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.sobipro_pageLayout)));

	}

	@Override
	public void prepareViews() {
		try {
			switch (pageLayouts.indexOf(IN_PAGELAYOUT)) {
			case 1:
				if ((IN_SECTION_ID != null && IN_SECTION_ID.length() > 0) && Integer.parseInt(IN_CAT_ID) == 0)
					addFragment(R.id.lnrFragment, new SobiproCarSectionCategoryGridFragment(IN_SECTION_ID, IN_CAT_ID, IN_PAGELAYOUT, IN_FEATUREDFIRST));
				else if ((IN_SECTION_ID != null && IN_SECTION_ID.length() > 0) && Integer.parseInt(IN_CAT_ID) > 0)
					addFragment(R.id.lnrFragment, new SobiproCarEntriesListFragment(IN_SECTION_ID,IN_CAT_ID, 0, IN_PAGELAYOUT, IN_FEATUREDFIRST));
				break;
			case 2:
				if ((IN_SECTION_ID != null && IN_SECTION_ID.length() > 0))
					addFragment(R.id.lnrFragment, new SobiproRestaurantEntriesListFragment(IN_SECTION_ID, IN_PAGELAYOUT, IN_FEATUREDFIRST));
				break;
			default:
				if ((IN_SECTION_ID != null && IN_SECTION_ID.length() > 0) && Integer.parseInt(IN_CAT_ID) == 0)
					addFragment(R.id.lnrFragment, new SobiproSectionCategoryGridFragment(IN_SECTION_ID, IN_CAT_ID, pageLayouts.get(0), IN_FEATUREDFIRST));
				else if ((IN_SECTION_ID != null && IN_SECTION_ID.length() > 0) && Integer.parseInt(IN_CAT_ID) > 0)
					addFragment(R.id.lnrFragment, new SobiproEntriesListFragment(IN_SECTION_ID,IN_CAT_ID, 0, IN_PAGELAYOUT, IN_FEATUREDFIRST));
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setActionListeners() {
	}

	@Override
	public void initTheme() {
		switch (pageLayouts.indexOf(IN_PAGELAYOUT)) {
		case 1:
			themes = new SobiproTheme[1];
			IMAGE_MAX_SIZE = 1;
			themes[0] = new SobiproTheme();
			themes[0].setCarTheme(getResources().getColor(R.color.sobipro_car_theme_dark_color), getResources().getColor(R.color.sobipro_car_theme_light_color), getResources()
					.getColor(R.color.sobipro_car_theme_text_color), getResources().getColor(R.color.sobipro_car_theme_grid_border_color), R.drawable.sobipro_cartheme_call_icon,
					R.drawable.sobipro_cartheme_email_icon, R.drawable.sobipro_ascending_yellow, R.drawable.sobipro_descending_yellow);

			break;

		case 2:
			themes = new SobiproTheme[1];
			IMAGE_MAX_SIZE = 1;
			themes[0] = new SobiproTheme();
			break;

		default:
			IMAGE_MAX_SIZE = 12;
			themes = new SobiproTheme[IMAGE_MAX_SIZE];
			themes[0] = new SobiproTheme(getResources().getColor(R.color.sobipro_orange), getResources().getColor(R.color.sobipro_lightorange), R.drawable.sobipro_detailpage_selector_orange_btn,
					R.drawable.sobipro_add_favourite_orange_btn, R.drawable.sobipro_map_orange_icon, R.drawable.sobipro_ascending_orange, R.drawable.sobipro_descending_orange,
					R.drawable.sobipro_like_orange_icon, R.drawable.sobipro_dislike_orange_icon, R.drawable.sobipro_map_popup_orange, R.drawable.sobipro_map_custom_marker_orange);
			themes[1] = new SobiproTheme(getResources().getColor(R.color.sobipro_pink), getResources().getColor(R.color.sobipro_lightpink), R.drawable.sobipro_detailpage_selector_pink_btn,
					R.drawable.sobipro_add_favourite_pink_btn, R.drawable.sobipro_map_pink_icon, R.drawable.sobipro_ascending_pink, R.drawable.sobipro_descending_pink,
					R.drawable.sobipro_like_pink_icon, R.drawable.sobipro_dislike_pink_icon, R.drawable.sobipro_map_popup_pink, R.drawable.sobipro_map_custom_marker_pink);
			themes[2] = new SobiproTheme(getResources().getColor(R.color.sobipro_orange), getResources().getColor(R.color.sobipro_lightorange), R.drawable.sobipro_detailpage_selector_orange_btn,
					R.drawable.sobipro_add_favourite_orange_btn, R.drawable.sobipro_map_orange_icon, R.drawable.sobipro_ascending_orange, R.drawable.sobipro_descending_orange,
					R.drawable.sobipro_like_orange_icon, R.drawable.sobipro_dislike_orange_icon, R.drawable.sobipro_map_popup_orange, R.drawable.sobipro_map_custom_marker_orange);
			themes[3] = new SobiproTheme(getResources().getColor(R.color.sobipro_green), getResources().getColor(R.color.sobipro_lightgreen), R.drawable.sobipro_detailpage_selector_green_btn,
					R.drawable.sobipro_add_favourite_green_btn, R.drawable.sobipro_map_green_icon, R.drawable.sobipro_ascending_green, R.drawable.sobipro_descending_green,
					R.drawable.sobipro_like_green_icon, R.drawable.sobipro_dislike_green_icon, R.drawable.sobipro_map_popup_green, R.drawable.sobipro_map_custom_marker_green);
			themes[4] = new SobiproTheme(getResources().getColor(R.color.sobipro_brown), getResources().getColor(R.color.sobipro_lightbrown),
					R.drawable.sobipro_detailpage_selector_brown_btn, R.drawable.sobipro_add_favourite_brown_btn, R.drawable.sobipro_map_brown_icon,
					R.drawable.sobipro_ascending_brown, R.drawable.sobipro_descending_brown, R.drawable.sobipro_like_brown_icon, R.drawable.sobipro_dislike_brown_icon,
					R.drawable.sobipro_map_popup_brown, R.drawable.sobipro_map_custom_marker_brown);
			themes[5] = new SobiproTheme(getResources().getColor(R.color.sobipro_pink), getResources().getColor(R.color.sobipro_lightpink), R.drawable.sobipro_detailpage_selector_pink_btn,
					R.drawable.sobipro_add_favourite_pink_btn, R.drawable.sobipro_map_pink_icon, R.drawable.sobipro_ascending_pink, R.drawable.sobipro_descending_pink,
					R.drawable.sobipro_like_pink_icon, R.drawable.sobipro_dislike_pink_icon, R.drawable.sobipro_map_popup_pink, R.drawable.sobipro_map_custom_marker_pink);
			themes[6] = new SobiproTheme(getResources().getColor(R.color.sobipro_brown), getResources().getColor(R.color.sobipro_lightbrown),
					R.drawable.sobipro_detailpage_selector_brown_btn, R.drawable.sobipro_add_favourite_brown_btn, R.drawable.sobipro_map_brown_icon,
					R.drawable.sobipro_ascending_brown, R.drawable.sobipro_descending_brown, R.drawable.sobipro_like_brown_icon, R.drawable.sobipro_dislike_brown_icon,
					R.drawable.sobipro_map_popup_brown, R.drawable.sobipro_map_custom_marker_brown);
			themes[7] = new SobiproTheme(getResources().getColor(R.color.sobipro_green), getResources().getColor(R.color.sobipro_lightgreen), R.drawable.sobipro_detailpage_selector_green_btn,
					R.drawable.sobipro_add_favourite_green_btn, R.drawable.sobipro_map_green_icon, R.drawable.sobipro_ascending_green, R.drawable.sobipro_descending_green,
					R.drawable.sobipro_like_green_icon, R.drawable.sobipro_dislike_green_icon, R.drawable.sobipro_map_popup_green, R.drawable.sobipro_map_custom_marker_green);
			themes[8] = new SobiproTheme(getResources().getColor(R.color.sobipro_orange), getResources().getColor(R.color.sobipro_lightorange), R.drawable.sobipro_detailpage_selector_orange_btn,
					R.drawable.sobipro_add_favourite_orange_btn, R.drawable.sobipro_map_orange_icon, R.drawable.sobipro_ascending_orange, R.drawable.sobipro_descending_orange,
					R.drawable.sobipro_like_orange_icon, R.drawable.sobipro_dislike_orange_icon, R.drawable.sobipro_map_popup_orange, R.drawable.sobipro_map_custom_marker_orange);
			themes[9] = new SobiproTheme(getResources().getColor(R.color.sobipro_brown), getResources().getColor(R.color.sobipro_lightbrown),
					R.drawable.sobipro_detailpage_selector_brown_btn, R.drawable.sobipro_add_favourite_brown_btn, R.drawable.sobipro_map_brown_icon,
					R.drawable.sobipro_ascending_brown, R.drawable.sobipro_descending_brown, R.drawable.sobipro_like_brown_icon, R.drawable.sobipro_dislike_brown_icon,
					R.drawable.sobipro_map_popup_brown, R.drawable.sobipro_map_custom_marker_brown);
			themes[10] = new SobiproTheme(getResources().getColor(R.color.sobipro_pink), getResources().getColor(R.color.sobipro_lightpink), R.drawable.sobipro_detailpage_selector_pink_btn,
					R.drawable.sobipro_add_favourite_pink_btn, R.drawable.sobipro_map_pink_icon, R.drawable.sobipro_ascending_pink, R.drawable.sobipro_descending_pink,
					R.drawable.sobipro_like_pink_icon, R.drawable.sobipro_dislike_pink_icon, R.drawable.sobipro_map_popup_pink, R.drawable.sobipro_map_custom_marker_pink);
			themes[11] = new SobiproTheme(getResources().getColor(R.color.sobipro_green), getResources().getColor(R.color.sobipro_lightgreen), R.drawable.sobipro_detailpage_selector_green_btn,
					R.drawable.sobipro_add_favourite_green_btn, R.drawable.sobipro_map_green_icon, R.drawable.sobipro_ascending_green, R.drawable.sobipro_descending_green,
					R.drawable.sobipro_like_green_icon, R.drawable.sobipro_dislike_green_icon, R.drawable.sobipro_map_popup_green, R.drawable.sobipro_map_custom_marker_green);

			break;
		}

	}
	
	/**
	 * Class methods.
	 */

	/**
	 * This method is used to get intent data.
	 */
	private void getIntentData() {
		try {
			IN_OBJ = new JSONObject(new JSONObject(getIntent().getStringExtra("IN_OBJ")).getString(ITEMDATA));
			IN_PAGELAYOUT = IN_OBJ.getString(PAGELAYOUT);
			IN_FEATUREDFIRST = IN_OBJ.getString(FEATUREDFIRST);
			IN_SECTION_ID = IN_OBJ.getString(SECTION_ID);
			IN_CAT_ID = IN_OBJ.getString(CAT_ID);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
