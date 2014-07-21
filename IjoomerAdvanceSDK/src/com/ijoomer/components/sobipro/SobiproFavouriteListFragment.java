package com.ijoomer.components.sobipro;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidquery.AQuery;
import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerListView;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.sobipro.SobiproCategoriesDataProvider;
import com.ijoomer.src.R;
import com.smart.framework.CustomAlertMagnatic;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This Fragment Contains All Method Related To SobiproFavouriteListFragment.
 * 
 * @author tasol
 * 
 */

public class SobiproFavouriteListFragment extends SmartFragment implements SobiproTagHolder {
	private IjoomerListView lstEntries;
	private List<ArrayList<HashMap<String, String>>> favouriteEntries;
	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder listAdapterWithHolder;
	private AQuery androidAQuery;
	private SobiproCategoriesDataProvider categoriesDataProvider;
	private final String FAVOURITE = "SobiproFavouriteEntries";
	private ArrayList<String> ID_ARRAY;

	/**
	 * Overrides methods.
	 */
	@Override
	public int setLayoutId() {
		return R.layout.sobipro_entries_fragment;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents(View currentView) {
		categoriesDataProvider = new SobiproCategoriesDataProvider(getActivity());
		lstEntries = (IjoomerListView) currentView.findViewById(R.id.lstEntries);
		androidAQuery = new AQuery(getActivity());
		ID_ARRAY = new ArrayList<String>();

	}

	@Override
	public void prepareViews(View currentView) {
		getEntries();
	}

	@Override
	public void setActionListeners(View currentView) {
	}
	
	/**
	 * Class methods
	 */

	/**
	 * This method is used to get entry list.
	 */
	private void getEntries() {
		try {
			if (categoriesDataProvider.getFavouriteEntries() != null && categoriesDataProvider.getFavouriteEntries().size() > 0) {
				favouriteEntries = categoriesDataProvider.getFavouriteEntries();
				prepareList(favouriteEntries, false);
				listAdapterWithHolder = getListAdapter(listData);
				lstEntries.setAdapter(listAdapterWithHolder);
			} else {
				IjoomerUtilities.getCustomOkDialog(((IjoomerSuperMaster) getActivity()).getScreenCaption(),
						getString(getResources().getIdentifier("code204", "string", getActivity().getPackageName())), getString(R.string.ok), R.layout.ijoomer_ok_dialog,
						new CustomAlertNeutral() {
							@Override
							public void NeutralMethod() {

							}
						});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is used to prepare initial list from response data.
	 * 
	 * @param data
	 *            represented data from response.
	 */

	public void prepareList(List<ArrayList<HashMap<String, String>>> data, boolean append) {

		if (data != null) {

			if (!append) {
				listData.clear();
			}
			for (ArrayList<HashMap<String, String>> hashMap : data) {

				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.sobipro_favourite_entry_item);
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
	 * List adapter for favourite list.
	 * 
	 * @param listData
	 *            represented favourite data
	 * @return represented {@link SmartListAdapterWithHolder}
	 */

	public SmartListAdapterWithHolder getListAdapter(ArrayList<SmartListItem> listData) {

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.sobipro_favourite_entry_item, listData, new ItemView() {
			@Override
			public View setItemView(final int position, View v, SmartListItem item, ViewHolder holder) {

				holder.imgEntryIcon = (ImageView) v.findViewById(R.id.imgEntryIcon);
				holder.txtTitle = (IjoomerTextView) v.findViewById(R.id.txtTitle);
				holder.txtDescription = (IjoomerTextView) v.findViewById(R.id.txtDescription);
				holder.btnEntryRemove = (IjoomerButton) v.findViewById(R.id.btnEntryRemove);
				holder.lnrEntry = (LinearLayout) v.findViewById(R.id.lnrEntry);
				holder.btnEntryRemove.setVisibility(View.VISIBLE);

				@SuppressWarnings("unchecked")
				final ArrayList<HashMap<String, String>> value = (ArrayList<HashMap<String, String>>) item.getValues().get(0);
				try {
					String Description = "";
					for (HashMap<String, String> row : value) {

						if ((row.get(LABELID).equalsIgnoreCase("field_description") || row.get(LABELID).equalsIgnoreCase("field_full_description")) && row.get(VALUE).length() > 0) {
							Description = row.get(VALUE).toString();
						} else if (row.get(LABELID).equalsIgnoreCase("field_good_for")) {
							Description = row.get(VALUE).toString();
						}

					}
					holder.txtTitle.setText(value.get(0).get(TITLE));
					holder.txtDescription.setText(Description);

					String image[] = ((IjoomerSuperMaster) getActivity()).getStringArray(value.get(0).get(IMG_GALLERIES));
					if (image != null && image.length > 0)
						androidAQuery.id(holder.imgEntryIcon).image(image[0], true, true, ((SmartActivity) getActivity()).getDeviceWidth(), R.drawable.sobipro_entry_default);

				} catch (Exception e) {
					e.printStackTrace();
				}

				holder.btnEntryRemove.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						IjoomerUtilities.getCustomConfirmDialog(getString(R.string.sobipro_entry_remove), getString(R.string.are_you_sure), getString(R.string.yes),
								getString(R.string.no), new CustomAlertMagnatic() {

									@Override
									public void PositiveMethod() {
										if (new IjoomerCaching(getActivity()).deleteDataFromCache("delete from '" + FAVOURITE + "' where id='" + value.get(0).get(ID) + "'")) {
											listAdapterWithHolder.remove(listAdapterWithHolder.getItem(position));
											lstEntries.invalidate();

										}
									}

									@Override
									public void NegativeMethod() {

									}
								});

					}
				});
				holder.lnrEntry.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							ID_ARRAY.clear();
							ID_ARRAY.add(value.get(position).get(ID));

							((SmartActivity) getActivity()).loadNew(SobiproEntryDetailActivity.class, getActivity(), false, "IN_ENTRY_ID_ARRAY", ID_ARRAY, "IN_ENTRY_INDEX", 0,
									"IN_TABLE", SOBIPROFAVOURITEENTRIES, "IN_POS", 0, "IN_PAGELAYOUT", value.get(position).get(PAGELAYOUT), "IN_FEATUREDFIRST", "No");
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				});

				if (position % 2 == 0)
					v.setBackgroundColor(getResources().getColor(R.color.sobipro_orange));
				else
					v.setBackgroundColor(getResources().getColor(R.color.sobipro_lightorange));

				return v;
			}

			@Override
			public View setItemView(int position, View v, SmartListItem item) {
				return null;
			}
		});
		return adapterWithHolder;
	}

	/**
	 * This method is used to prepare ID to get the entry's id which will used
	 * in entry detail.
	 */

	@SuppressWarnings("unchecked")
	public void prepareIDS() {
		ID_ARRAY.clear();
		for (SmartListItem row : listData) {
			ID_ARRAY.add(((ArrayList<HashMap<String, String>>) row.getValues().get(0)).get(0).get(ID));
		}
	}
}
