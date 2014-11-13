package com.ijoomer.components.sobipro;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.sobipro.SobiproCategoriesDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Fragment Contains All Method Related To
 * SobiproSectionCategoryGridFragment.
 * 
 * @author tasol
 * 
 */

public class SobiproCarSectionCategoryGridFragment extends SmartFragment implements SobiproTagHolder {

	private GridView gridCateogories;
	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder listAdapterWithHolder;
	private AQuery androidAQuery;
	private String IN_FEATUREDFIRST;
	private ArrayList<HashMap<String, String>> sectionCategory;
	private String IN_SECTION_ID, IN_CAT_ID, IN_PAGELAYOUT;
	private SobiproCategoriesDataProvider categoriesDataProvider;

	public SobiproCarSectionCategoryGridFragment(String IN_SECTION_ID, String IN_CAT_ID, String IN_PAGELAYOUT, String IN_FEATUREDFIRST) {
		this.IN_SECTION_ID = IN_SECTION_ID;
		this.IN_CAT_ID = IN_CAT_ID;
		this.IN_PAGELAYOUT = IN_PAGELAYOUT;
		this.IN_FEATUREDFIRST = IN_FEATUREDFIRST;

	}

	/**
	 * Overrides methods.
	 */
	@Override
	public int setLayoutId() {
		return R.layout.sobipro_section_category_fragment;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents(View currentView) {
		categoriesDataProvider = new SobiproCategoriesDataProvider(getActivity());
		gridCateogories = (GridView) currentView.findViewById(R.id.grdCategories);
		androidAQuery = new AQuery(getActivity());
	}

	@Override
	public void prepareViews(View currentView) {
		getSectionCategories();

	}

	@Override
	public void setActionListeners(View currentView) {
		gridCateogories.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {
					int IN_POS = 0;
					if (arg2 < SobiproMasterActivity.IMAGE_MAX_SIZE)
						IN_POS = arg2;
					else
						IN_POS = arg2 % SobiproMasterActivity.IMAGE_MAX_SIZE;

					((IjoomerSuperMaster) getActivity()).setScreenCaption(sectionCategory.get(arg2).get(NAME).trim());
					((SmartActivity) getActivity()).loadNew(SobiproEntriesActivity.class, getActivity(), false,"IN_SECTION_ID",IN_SECTION_ID, "IN_PARENT_ID", sectionCategory.get(arg2).get(ID), "IN_POS",
							IN_POS, "IN_PAGELAYOUT", IN_PAGELAYOUT, "IN_FEATUREDFIRST", IN_FEATUREDFIRST);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Class methods.
	 */

	/**
	 * This method is used to get section categories.
	 */

	private void getSectionCategories() {
		final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

		categoriesDataProvider.getSectionCategories(IN_SECTION_ID,IN_CAT_ID, new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {
				proSeekBar.setProgress(progressCount);
			}

			@Override
			public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				try {
					if (responseCode == 200) {
						if (data1 != null) {
							sectionCategory = data1;
							prepareList(data1, false);
							listAdapterWithHolder = getListAdapter(listData);
							gridCateogories.setAdapter(listAdapterWithHolder);
						} else {
							IjoomerUtilities.getCustomOkDialog(((IjoomerSuperMaster) getActivity()).getScreenCaption(),
									getString(getResources().getIdentifier("code204", "string", getActivity().getPackageName())), getString(R.string.ok),
									R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {
										@Override
										public void NeutralMethod() {

										}
									});
						}
					} else {
						IjoomerUtilities.getCustomOkDialog(getString(R.string.articles),
								getString(getResources().getIdentifier("code" + responseCode, "string", getActivity().getPackageName())), getString(R.string.ok),
								R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {
									@Override
									public void NeutralMethod() {
										
									}
								});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
	}

	/**
	 * This method is used to prepare initial list from response data.
	 * 
	 * @param data
	 *            represented data from response.
	 */

	public void prepareList(ArrayList<HashMap<String, String>> data, boolean append) {

		if (data != null) {

			if (!append) {
				listData.clear();
			}
			for (HashMap<String, String> hashMap : data) {

				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.sobipro_car_section_category_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hashMap);
				item.setValues(obj);
				if (append) {
					listAdapterWithHolder.add(item);
				} else {
					listData.add(item);
				}
			}
		}
	}

	/**
	 * List adapter for section category list.
	 * 
	 * @param listData
	 *            represented section category data
	 * @return represented {@link SmartListAdapterWithHolder}
	 */

	public SmartListAdapterWithHolder getListAdapter(ArrayList<SmartListItem> listData) {

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.sobipro_car_section_category_item, listData, new ItemView() {
			@Override
			public View setItemView(int position, View v, SmartListItem item, ViewHolder holder) {

				holder.sobiproTxtCategoriesCaption = (IjoomerTextView) v.findViewById(R.id.txtCategoriesitemCaption);
				holder.sobiproImgCategories = (ImageView) v.findViewById(R.id.imgCategoriesItemIcon);
				holder.sobiproGridItemLayout = (LinearLayout) v.findViewById(R.id.gridItemLayout);
				holder.lnrGridBorder = (LinearLayout) v.findViewById(R.id.lnrGridBorder);

				@SuppressWarnings("unchecked")
				HashMap<String, String> value = (HashMap<String, String>) item.getValues().get(0);
				if (position < SobiproMasterActivity.IMAGE_MAX_SIZE) {
					holder.sobiproGridItemLayout.setBackgroundColor(SobiproMasterActivity.themes[position].getBgLightColor());
					holder.lnrGridBorder.setBackgroundColor(SobiproMasterActivity.themes[position].getGridBorderColor());
					holder.sobiproTxtCategoriesCaption.setTextColor(SobiproMasterActivity.themes[position].getTextColor());
				} else {
					holder.sobiproGridItemLayout.setBackgroundColor(SobiproMasterActivity.themes[position
							% SobiproMasterActivity.IMAGE_MAX_SIZE].getBgLightColor());
					holder.lnrGridBorder.setBackgroundColor(SobiproMasterActivity.themes[position % SobiproMasterActivity.IMAGE_MAX_SIZE]
							.getGridBorderColor());
					holder.sobiproTxtCategoriesCaption.setTextColor(SobiproMasterActivity.themes[position
							% SobiproMasterActivity.IMAGE_MAX_SIZE].getTextColor());

				}
				androidAQuery.id(holder.sobiproImgCategories).image(value.get(IMAGE), true, true, ((SmartActivity) getActivity()).getDeviceWidth(),
						R.drawable.sobipro_entry_default);
				holder.sobiproTxtCategoriesCaption.setText(value.get(NAME).trim());

				return v;
			}

			@Override
			public View setItemView(int position, View v, SmartListItem item) {
				return null;
			}
		});
		return adapterWithHolder;
	}
}
