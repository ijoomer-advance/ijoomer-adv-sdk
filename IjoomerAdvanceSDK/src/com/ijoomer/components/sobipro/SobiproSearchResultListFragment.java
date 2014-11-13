package com.ijoomer.components.sobipro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerRatingBar;
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

/**
 * This Fragment Contains All Method Related To SobiproEntriesListFragment.
 * 
 * @author tasol
 * 
 */
public class SobiproSearchResultListFragment extends SmartFragment implements SobiproTagHolder {
	private String IN_PAGELAYOUT;
	private String IN_FEATUREDFIRST = "Yes";
	private ListView lstEntries;
	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder listAdapterWithHolder;
	private AQuery androidAQuery;
	private ImageView imgMap;
	private View listFooter;
	private ArrayList<String> ID_ARRAY;
	private SeekBar proSeekBar;
	private SobiproCategoriesDataProvider dataProvider;
	private String optionCriteria, searchKey, section_id;
	private ArrayList<HashMap<String, String>> searchField;
	private int IN_POS;
	private ArrayList<String> pageLayouts;

	public SobiproSearchResultListFragment(String optionCriteria, String searchKey, String section_id, ArrayList<HashMap<String, String>> searchField, int IN_POS,
			String IN_FEATUREDFIRST) {
		this.IN_POS = IN_POS;
		this.searchField = searchField;
		this.optionCriteria = optionCriteria;
		this.searchKey = searchKey;
		this.section_id = section_id;
		this.IN_FEATUREDFIRST = IN_FEATUREDFIRST;
	}

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
		dataProvider = new SobiproCategoriesDataProvider(getActivity());
		lstEntries = (ListView) currentView.findViewById(R.id.lstEntries);
		listFooter = LayoutInflater.from(getActivity()).inflate(R.layout.ijoomer_list_footer, null);
		lstEntries.addFooterView(listFooter, null, false);
		ID_ARRAY = new ArrayList<String>();
		androidAQuery = new AQuery(getActivity());
		pageLayouts = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.sobipro_pageLayout)));

	}

	@Override
	public void prepareViews(View currentView) {
		imgMap = (ImageView) getActivity().findViewById(R.id.imgMap);
		getIntentData();
		getEntries();
	}

	@Override
	public void setActionListeners(View currentView) {

		lstEntries.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 0) {

					if (!dataProvider.isCalling() && dataProvider.hasNextPage()) {
						listFooterVisible();
						dataProvider.getSearchResult(optionCriteria, searchKey, section_id, searchField, IN_FEATUREDFIRST, new WebCallListener() {

							@SuppressWarnings("unchecked")
							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								try {
									listFooterInvisible();
									if (responseCode == 200) {
										if (data2 != null && ((List<ArrayList<HashMap<String, String>>>) data2).size() > 0) {
											prepareList((List<ArrayList<HashMap<String, String>>>) data2, true, true, 0, 0);
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

							@Override
							public void onProgressUpdate(int progressCount) {
							}

						});

					}
				}
			}
		});
		lstEntries.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {
					prepareIDS();
					((SmartActivity) getActivity()).loadNew(SobiproEntryDetailActivity.class, getActivity(), false, "IN_ENTRY_ID_ARRAY", ID_ARRAY, "IN_ENTRY_INDEX", arg2,
							"IN_TABLE", SOBIPROSEARCHRESULT, "IN_POS", IN_POS, "IN_PAGELAYOUT", IN_PAGELAYOUT, "IN_FEATUREDFIRST", IN_FEATUREDFIRST);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		imgMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					prepareIDS();
					((SmartActivity) getActivity()).loadNew(SobiproMapActivity.class, getActivity(), false, "IN_ENTRY_ID_ARRAY", ID_ARRAY, "IN_TABLE", SOBIPROENTRIES, "IN_POS",
							IN_POS);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * This method used to get intent data.
	 */

	private void getIntentData() {
		try {
			IN_POS = getActivity().getIntent().getIntExtra("IN_POS", 0);
			IN_PAGELAYOUT = getActivity().getIntent().getStringExtra("IN_PAGELAYOUT");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is used to make footer visible.
	 */

	public void listFooterVisible() {
		listFooter.setVisibility(View.VISIBLE);
	}

	/**
	 * This method is used to make footer invisible.
	 */

	public void listFooterInvisible() {
		listFooter.setVisibility(View.GONE);
	}

	/**
	 * This method used to get entry list.
	 */
	private void getEntries() {
		proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

		dataProvider.getSearchResult(optionCriteria, searchKey, section_id, searchField, IN_FEATUREDFIRST, new WebCallListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				try {
					if (responseCode == 200) {
						if (data2 != null) {
							prepareList((List<ArrayList<HashMap<String, String>>>) data2, false, true, 0, 0);
							switch (pageLayouts.indexOf(IN_PAGELAYOUT)) {
							case 1:
								listAdapterWithHolder = getCarListAdapter(listData);
								break;

							case 2:
								imgMap.setVisibility(View.VISIBLE);
								listAdapterWithHolder = getRestaurantListAdapter(listData);
								break;

							default:
								imgMap.setVisibility(View.VISIBLE);
								listAdapterWithHolder = getListAdapter(listData);
								break;
							}

							lstEntries.setAdapter(listAdapterWithHolder);
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
						IjoomerUtilities.getCustomOkDialog(((IjoomerSuperMaster) getActivity()).getScreenCaption(),
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

			@Override
			public void onProgressUpdate(int progressCount) {
				proSeekBar.setProgress(progressCount);
			}

		});

	}

	/**
	 * This method is used to prepare initial list from response data.
	 * 
	 * @param data
	 *            : data from response.
	 * 
	 * 
	 */
	public void prepareList(List<ArrayList<HashMap<String, String>>> data, boolean append, boolean fromCache, int pageno, int pagelimit) {
		if (data != null) {
			if (!append) {
				listData.clear();
			} else {
				if (!fromCache) {
					int startIndex = ((pageno - 1) * pagelimit);
					int endIndex = listAdapterWithHolder.getCount();
					if (startIndex > 0) {
						for (int i = endIndex; i >= startIndex; i--) {
							try {
								listAdapterWithHolder.remove(listAdapterWithHolder.getItem(i));
								listData.remove(i);
							} catch (Exception e) {
							}
						}
					}
				}
			}

			for (ArrayList<HashMap<String, String>> hashMap : data) {

				SmartListItem item = new SmartListItem();
				switch (pageLayouts.indexOf(IN_PAGELAYOUT)) {
				case 1:
					item.setItemLayout(R.layout.sobipro_car_entries_item);
					break;
				case 2:
					item.setItemLayout(R.layout.sobipro_restaurant_entry_item);
					break;
				default:
					item.setItemLayout(R.layout.sobipro_entries_item);
					break;
				}

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
	 * List adapter for entry list.
	 * 
	 * @param listData
	 *            represented entry data
	 * @return represented {@link SmartListAdapterWithHolder}
	 */

	public SmartListAdapterWithHolder getListAdapter(ArrayList<SmartListItem> listData) {

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.sobipro_entries_item, listData, new ItemView() {
			@Override
			public View setItemView(int position, View v, SmartListItem item, ViewHolder holder) {

				holder.imgEntryIcon = (ImageView) v.findViewById(R.id.imgEntryIcon);
				holder.txtTitle = (IjoomerTextView) v.findViewById(R.id.txtTitle);
				holder.txtReview = (IjoomerTextView) v.findViewById(R.id.txtReview);
				holder.txtDistance = (IjoomerTextView) v.findViewById(R.id.txtDistance);
				holder.txtPrice = (IjoomerTextView) v.findViewById(R.id.txtPrice);
				holder.txtAddress = (IjoomerTextView) v.findViewById(R.id.txtAddress);
				holder.rtbRating = (IjoomerRatingBar) v.findViewById(R.id.rtbRating);

				@SuppressWarnings("unchecked")
				final ArrayList<HashMap<String, String>> value = (ArrayList<HashMap<String, String>>) item.getValues().get(0);
				try {

					for (HashMap<String, String> row : value) {

						if (row.get(LABELID).equalsIgnoreCase("field_address") && row.get(VALUE).length() > 0) {
							holder.txtAddress.setText(row.get(VALUE));
							holder.txtAddress.setVisibility(View.VISIBLE);
						}

						if (row.get(LABELID).equalsIgnoreCase("field_distance"))
							holder.txtDistance.setText(row.get(VALUE) + " " + getActivity().getString(R.string.sobipro_miles_text));

						if (row.get(LABELID).equalsIgnoreCase("field_company_logo")) {
							androidAQuery.id(holder.imgEntryIcon).image(row.get(VALUE), true, true, ((SmartActivity) getActivity()).getDeviceWidth(),
									R.drawable.sobipro_default_image);
						}
						if (row.get(LABELID).equalsIgnoreCase("field_price")) {
							int price = Integer.parseInt(row.get(VALUE));
							String price_final = "";
							String currency = row.get(UNIT);
							if (price < 5) {
								for (int i = 0; i < price; i++)
									price_final = price_final + currency;

							} else {
								price_final = currency + currency + currency + currency + currency;
							}
							holder.txtPrice.setText(price_final);
						}
					}

					if (value.get(0).get(TITLE).length() > 0) {
						holder.txtTitle.setText(value.get(0).get(TITLE));
						holder.txtTitle.setVisibility(View.VISIBLE);
					}
					if (value.get(0).get(TOTALREVIEWCOUNT) != null && value.get(0).get(TOTALREVIEWCOUNT).length() > 0)
						holder.txtReview.setText(value.get(0).get(TOTALREVIEWCOUNT) + " " + (getActivity().getString(R.string.sobipro_detail_review_text)).toLowerCase());
					if (position % 2 == 0) {
						holder.rtbRating.setStarBgColor(SobiproMasterActivity.themes[IN_POS].getBgColor());
						v.setBackgroundColor(SobiproMasterActivity.themes[IN_POS].getBgColor());
					} else {
						holder.rtbRating.setStarBgColor(SobiproMasterActivity.themes[IN_POS].getBgLightColor());
						v.setBackgroundColor(SobiproMasterActivity.themes[IN_POS].getBgLightColor());
					}
					if (value.get(0).get(AVERAGERATING) != null && value.get(0).get(AVERAGERATING).length() > 0) {
						holder.rtbRating.setFilledStarResourceId(R.drawable.sobipro_rating_transparent);
						holder.rtbRating.setHalfFilledStarResourceId(R.drawable.sobipro_rating_transparent_half);
						holder.rtbRating.setEmptyStarResourceId(R.drawable.sobipro_rating_empty_star);
						holder.rtbRating.setStarRating(Float.parseFloat(value.get(0).get(AVERAGERATING)) / 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

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
	 * List adapter for entry list.
	 * 
	 * @param listData
	 *            represented entry data
	 * @return represented {@link SmartListAdapterWithHolder}
	 */

	public SmartListAdapterWithHolder getCarListAdapter(ArrayList<SmartListItem> listData) {

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.sobipro_car_entries_item, listData, new ItemView() {
			@Override
			public View setItemView(int position, View v, SmartListItem item, ViewHolder holder) {

				String regYear = "", fuelType = "", mileage = "", gearBox = "", power = "";
				String powerUnit = "", priceUnit = "", mileageUnit = "";
				String image[] = new String[1];

				holder.imgEntryIcon = (ImageView) v.findViewById(R.id.imgEntryIcon);
				holder.txtTitle = (IjoomerTextView) v.findViewById(R.id.txtCarTitle);
				holder.txtCarDetail = (IjoomerTextView) v.findViewById(R.id.txtCarDetails);
				holder.txtPrice = (IjoomerTextView) v.findViewById(R.id.txtCarPrice);
				holder.imgCall = (ImageView) v.findViewById(R.id.imgCall);
				holder.imgEmail = (ImageView) v.findViewById(R.id.imgEmail);
				holder.imgCall.setImageResource(SobiproMasterActivity.themes[IN_POS].getImgCall());
				holder.imgEmail.setImageResource(SobiproMasterActivity.themes[IN_POS].getImgEmail());

				@SuppressWarnings("unchecked")
				final ArrayList<HashMap<String, String>> value = (ArrayList<HashMap<String, String>>) item.getValues().get(0);
				try {
					for (HashMap<String, String> row : value) {

						// if (row.get(LABELID).equalsIgnoreCase("field_name"))
						// holder.txtTitle.setText(row.get(VALUE));

						if (row.get(LABELID).equalsIgnoreCase("field_first_registration"))
							regYear = row.get(VALUE);

						if (row.get(LABELID).equalsIgnoreCase("field_fuel_type"))
							fuelType = row.get(VALUE);

						if (row.get(LABELID).equalsIgnoreCase("field_mileage")) {
							mileage = row.get(VALUE);
							mileageUnit = row.get(UNIT);
						}
						if (row.get(TYPE).equalsIgnoreCase("phone")) {
							holder.imgCall.setTag(row.get(VALUE));
						}
						if (row.get(TYPE).equalsIgnoreCase("email")) {
							holder.imgEmail.setTag(row.get(VALUE));
						}

						if (row.get(LABELID).equalsIgnoreCase("field_gearbox"))
							gearBox = row.get(VALUE);

						if (row.get(LABELID).equalsIgnoreCase("field_power")) {
							power = row.get(VALUE);
							powerUnit = row.get(UNIT);
						}

						holder.txtCarDetail.setText(regYear + "-" + fuelType + "-" + mileage + " " + mileageUnit + "-" + gearBox + "-" + power + " " + powerUnit);

						if (row.get(LABELID).equalsIgnoreCase("field_price")) {
							priceUnit = row.get(UNIT);
							holder.txtPrice.setText(priceUnit + " " + row.get(VALUE));
							holder.txtPrice.setVisibility(View.VISIBLE);
						}

					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					image = ((IjoomerSuperMaster) getActivity()).getStringArray(value.get(0).get(IMG_GALLERIES));

					androidAQuery.id(holder.imgEntryIcon).image(image[0], true, true, ((SmartActivity) getActivity()).getDeviceWidth(), R.drawable.sobipro_default_image);

				} catch (Exception e) {
					e.printStackTrace();
				}
				if (holder.imgCall.getTag() == null || holder.imgCall.getTag().toString().trim().length() == 0)
					holder.imgCall.setVisibility(View.INVISIBLE);

				if (holder.imgEmail.getTag() == null || holder.imgEmail.getTag().toString().trim().length() == 0)
					holder.imgEmail.setVisibility(View.INVISIBLE);

				if (value.get(0).get(TITLE).trim().length() > 0) {
					holder.txtTitle.setText(value.get(0).get(TITLE));
				} else {
					holder.txtTitle.setVisibility(View.GONE);
				}

				holder.imgCall.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + v.getTag().toString()));
						startActivity(intent);

					}
				});

				holder.imgEmail.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent email = new Intent(Intent.ACTION_SEND);
						email.putExtra(Intent.EXTRA_EMAIL, new String[] { v.getTag().toString() });
						email.setType("message/rfc822");
						try {
							startActivity(Intent.createChooser(email, getString(R.string.sobipro_choose_email_client)));
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				});
				holder.txtTitle.setTextColor(SobiproMasterActivity.themes[IN_POS].getTextColor());
				holder.txtPrice.setTextColor(SobiproMasterActivity.themes[IN_POS].getTextColor());

				if (position % 2 == 0) {
					v.setBackgroundColor(SobiproMasterActivity.themes[IN_POS].getBgLightColor());
				} else {
					v.setBackgroundColor(SobiproMasterActivity.themes[IN_POS].getBgColor());
				}
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
	 * List adapter for entry list.
	 * 
	 * @param listData
	 *            represented entry data
	 * @return represented {@link SmartListAdapterWithHolder}
	 */

	public SmartListAdapterWithHolder getRestaurantListAdapter(ArrayList<SmartListItem> listData) {

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.sobipro_restaurant_entry_item, listData, new ItemView() {
			@Override
			public View setItemView(int position, View v, SmartListItem item, ViewHolder holder) {

				holder.imgEntryIcon = (ImageView) v.findViewById(R.id.imgEntryIcon);
				holder.txtTitle = (IjoomerTextView) v.findViewById(R.id.txtTitle);
				holder.txtDistance = (IjoomerTextView) v.findViewById(R.id.txtDistance);
				holder.txtPrice = (IjoomerTextView) v.findViewById(R.id.txtPrice);
				holder.txtGoodFor = (IjoomerTextView) v.findViewById(R.id.txtGoodFor);
				holder.rtbRating = (IjoomerRatingBar) v.findViewById(R.id.rtbRating);
				holder.btnFavourite = (IjoomerButton) v.findViewById(R.id.btnFavourite);
				holder.imgShare = (ImageView) v.findViewById(R.id.imgShare);

				@SuppressWarnings("unchecked")
				final ArrayList<HashMap<String, String>> value = (ArrayList<HashMap<String, String>>) item.getValues().get(0);
				try {
					for (HashMap<String, String> row : value) {

						if (row.get(LABELID).equalsIgnoreCase("field_distance") && row.get(VALUE).trim().length() > 0) {
							holder.txtDistance.setText(row.get(VALUE) + " " + getActivity().getString(R.string.sobipro_restaurant_miles_text));
							holder.txtDistance.setVisibility(View.VISIBLE);
						}

						if (row.get(LABELID).equalsIgnoreCase("field_good_for") && row.get(VALUE).trim().length() > 0) {
							holder.txtGoodFor.setText(row.get(VALUE));
							holder.txtGoodFor.setVisibility(View.VISIBLE);
						}

						if (row.get(LABELID).equalsIgnoreCase("field_price") && row.get(VALUE).length() > 0) {
							holder.txtPrice.setVisibility(View.VISIBLE);
							int price = Integer.parseInt(row.get(VALUE));
							String price_final = "";
							String currency = row.get(UNIT);
							if (price < 5) {
								for (int i = 0; i < price; i++)
									price_final = price_final + currency;

							} else {
								price_final = currency + currency + currency + currency + currency;
							}
							holder.txtPrice.setText(price_final);
						}
					}
					if (value.get(0).get(TITLE).length() > 0) {
						holder.txtTitle.setText(value.get(0).get(TITLE));
						holder.txtTitle.setVisibility(View.VISIBLE);
					}
					try {
						String[] image = ((IjoomerSuperMaster) getActivity()).getStringArray(value.get(0).get(IMG_GALLERIES));

						androidAQuery.id(holder.imgEntryIcon).image(image[0], true, true, ((SmartActivity) getActivity()).getDeviceWidth(), R.drawable.sobipro_default_image);

					} catch (Exception e) {
						e.printStackTrace();
					}

					holder.rtbRating.setFilledStarResourceId(R.drawable.sobipro_rating_transparent_entry_detail);
					holder.rtbRating.setHalfFilledStarResourceId(R.drawable.sobipro_rating_transparent_half_entry_detail);
					holder.rtbRating.setEmptyStarResourceId(R.drawable.sobipro_rating_empty_star);
					holder.rtbRating.setStarBgColor(getActivity().getResources().getColor(R.color.sobipro_green));
					holder.rtbRating.setStarRating(Float.parseFloat(value.get(0).get(AVERAGERATING)) / 2);
				} catch (Exception e) {
					e.printStackTrace();
				}
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
