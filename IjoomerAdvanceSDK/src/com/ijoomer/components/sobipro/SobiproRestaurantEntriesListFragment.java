package com.ijoomer.components.sobipro;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerShareActivity;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerCheckBox;
import com.ijoomer.customviews.IjoomerListView;
import com.ijoomer.customviews.IjoomerRatingBar;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.sobipro.SobiproCategoriesDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.ijoomer.weservice.WebCallListenerWithCacheInfo;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This Fragment Contains All Method Related To SobiproEntriesListFragment.
 * 
 * @author tasol
 * 
 */

public class SobiproRestaurantEntriesListFragment extends SmartFragment implements SobiproTagHolder {
	private String IN_SECTION_ID, IN_PAGELAYOUT;
	private int IN_POS;
	private IjoomerListView lstEntries;
	private View headerView;
	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder listAdapterWithHolder;
	private AQuery androidAQuery;
	private SobiproCategoriesDataProvider dataProvider;
	private String sortBy = "";
	private String orderBy = "";
	private String filter = "";
	private ImageView imgMap;
	private ImageView imgFilter;
	private ImageView imgFilterSort;
	private ImageView imgAddEntry;
	private List<ArrayList<HashMap<String, String>>> entryListData;
	private View listFooter;
	private ArrayList<String> ID_ARRAY;
	private SeekBar proSeekBar;
	private ViewPager viewPager;
	private DealFragmentAdapter dealFragmentAdapter;
	private String categories = "";
	private JSONArray jsonArrayCategories;
	private String IN_FEATUREDFIRST = "";
	private String sectionID;

	private boolean fromDB = false;

	/**
	 * Constructor
	 * @param IN_SECTION_ID represents the section id.
	 * @param IN_PAGELAYOUT represents the page layout to identify the view.
	 * @param IN_FEATUREDFIRST represents boolean value to verify to get featured entries first.
	 */

	public SobiproRestaurantEntriesListFragment(String IN_SECTION_ID, String IN_PAGELAYOUT, String IN_FEATUREDFIRST) {
		this.IN_PAGELAYOUT = IN_PAGELAYOUT;
		this.IN_SECTION_ID = IN_SECTION_ID;
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
	public void onResume() {
		super.onResume();
		try {
			if (IjoomerApplicationConfiguration.isReloadRequired()) {
				IjoomerApplicationConfiguration.setReloadRequired(false);
				dataProvider.restorePagingSettings();
				getEntries();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initComponents(View currentView) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		headerView = inflater.inflate(R.layout.sobipro_restaurant_entry_list_header, null, false);
		dataProvider = new SobiproCategoriesDataProvider(getActivity());
		viewPager = (ViewPager) headerView.findViewById(R.id.viewpager);
		lstEntries = (IjoomerListView) currentView.findViewById(R.id.lstEntries);
		listFooter = LayoutInflater.from(getActivity()).inflate(R.layout.ijoomer_list_footer, null);
		lstEntries.addFooterView(listFooter, null, false);
		ID_ARRAY = new ArrayList<String>();
		androidAQuery = new AQuery(getActivity());
	}

	@Override
	public void prepareViews(View currentView) {
		imgMap = (ImageView) getActivity().findViewById(R.id.imgMap);
		imgFilter = (ImageView) getActivity().findViewById(R.id.imgFilter);
		imgFilterSort = (ImageView) getActivity().findViewById(R.id.imgFilterSort);
		imgAddEntry = (ImageView) getActivity().findViewById(R.id.imgAddEntry);
		imgMap.setVisibility(View.VISIBLE);
		imgFilter.setVisibility(View.VISIBLE);
		imgFilterSort.setVisibility(View.VISIBLE);
		imgAddEntry.setVisibility(View.VISIBLE);

		lstEntries.addHeaderView(headerView);
		lstEntries.setAdapter(null);
		prepareHeader();
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
				if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 2) {

					if (!dataProvider.isCalling() && dataProvider.hasNextPage()) {
						listFooterVisible();
						dataProvider.getRestaurantEntries(IN_SECTION_ID, ((SmartActivity) getActivity()).getLatitude(), ((SmartActivity) getActivity()).getLongitude(),
								IN_FEATUREDFIRST, new WebCallListenerWithCacheInfo() {

							@Override
							public void onProgressUpdate(int progressCount) {
							}

							@SuppressWarnings("unchecked")
							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2, int pageNo,
									int pageLimit, boolean fromCache) {
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
						});
					}
				}
			}
		});

		imgFilterSort.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showFilterSortPopup(v);
			}
		});

		imgFilter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {

					((SmartActivity) getActivity()).loadNew(SobiproSearchActivity.class, getActivity(), false, "IN_SECTION_ID", sectionID, "IN_PAGELAYOUT", IN_PAGELAYOUT,
							"IN_FEATUREDFIRST", IN_FEATUREDFIRST);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		imgAddEntry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					((SmartActivity) getActivity()).loadNew(SobiproAddEntryActivity.class, getActivity(), false, "IN_SECTION_ID", sectionID, "IN_CAT_ID", sectionID,
							"IN_PAGELAYOUT", IN_PAGELAYOUT);
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
					((SmartActivity) getActivity()).loadNew(SobiproMapActivity.class, getActivity(), false, "IN_ENTRY_ID_ARRAY", ID_ARRAY, "IN_TABLE", SOBIPRO_RESTAURANT_ENTRIES,
							"IN_POS", IN_POS, "IN_PAGELAYOUT", IN_PAGELAYOUT);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * Class Methods.
	 */

	/**
	 * This method is used to show Sorting and Filtering popup window.
	 * 
	 * @param v
	 *            represented a view of button from where this method will be
	 *            called.
	 */
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void showFilterSortPopup(View v) {
		final ArrayList<Integer> filterBy;
		try {
			if (categories.length() > 0) {
				filterBy = new ArrayList<Integer>();
				jsonArrayCategories = new JSONArray(categories);
				Rect r = locateView(v);
				sortBy = "";
				filter = "";
				LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				final View layout = layoutInflater.inflate(R.layout.sobipro_restaurant_filtersort_popup, null);
				LinearLayout lnrFilterOptions = (LinearLayout) layout.findViewById(R.id.lnrFilterOptions);
				IjoomerButton btnCancel = (IjoomerButton) layout.findViewById(R.id.btnCancel);
				IjoomerButton btnApply = (IjoomerButton) layout.findViewById(R.id.btnApply);
				final PopupWindow popup = new PopupWindow(getActivity());
				popup.setAnimationStyle(R.style.animation);
				popup.setContentView(layout);
				popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
				popup.setWidth(getActivity().getWindowManager().getDefaultDisplay().getWidth() / 2);
				popup.setFocusable(true);
				popup.setBackgroundDrawable(new BitmapDrawable());
				popup.showAtLocation(layout, Gravity.TOP | Gravity.LEFT, r.right, r.bottom);
				lnrFilterOptions.removeAllViews();
				for (int i = 0; i < jsonArrayCategories.length(); i++) {
					JSONObject jsonObject = (JSONObject) jsonArrayCategories.get(i);
					IjoomerCheckBox checkBox = new IjoomerCheckBox(getActivity());
					checkBox.setText(jsonObject.get(NAME).toString());
					checkBox.setButtonDrawable(R.drawable.sobipro_checkbox_selector);
					checkBox.setTag(jsonObject.get(ID).toString());
					checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							if (isChecked) {
								filterBy.add(Integer.parseInt(buttonView.getTag().toString()));
							} else {
								filterBy.remove(Integer.parseInt(buttonView.getTag().toString()));
							}
						}
					});
					lnrFilterOptions.addView(checkBox);

				}

				btnCancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (popup.isShowing())
							popup.dismiss();

					}
				});

				btnApply.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						for (int i = 0; i < filterBy.size(); i++) {
							if (i == filterBy.size() - 1) {
								filter = filter + filterBy.get(i);
							} else {
								filter = filter + filterBy.get(i) + ",";
							}
						}

						if (popup.isShowing())
							popup.dismiss();
						if (sortBy.length() > 0 || filter.length() > 0) {
							proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
							dataProvider.restorePagingSettings();
							dataProvider.setPageNo(1);
							dataProvider.getRestaurantEntries(IN_SECTION_ID, ((SmartActivity) getActivity()).getLatitude(), ((SmartActivity) getActivity()).getLongitude(), sortBy,
									orderBy, filter, IN_FEATUREDFIRST, new WebCallListener() {

								@Override
								public void onProgressUpdate(int progressCount) {
									proSeekBar.setProgress(progressCount);

								}

								@SuppressWarnings("unchecked")
								@Override
								public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
									try {
										if (responseCode == 200) {
											if (data2 != null) {
												entryListData = (List<ArrayList<HashMap<String, String>>>) data2;
												prepareList((List<ArrayList<HashMap<String, String>>>) data2, false, true, 0, 0);
												listAdapterWithHolder = getListAdapter(listData);
												lstEntries.setAdapter(listAdapterWithHolder);
											} else {
												IjoomerUtilities.getCustomOkDialog(((IjoomerSuperMaster) getActivity()).getScreenCaption(), getString(getResources()
														.getIdentifier("code204", "string", getActivity().getPackageName())), getString(R.string.ok),
														R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {
													@Override
													public void NeutralMethod() {

													}
												});
											}
										} else {
											IjoomerUtilities.getCustomOkDialog(((IjoomerSuperMaster) getActivity()).getScreenCaption(), getString(getResources()
													.getIdentifier("code" + responseCode, "string", getActivity().getPackageName())), getString(R.string.ok),
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
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is used to draw popup rectangle for sorting and filtering.
	 * 
	 * @param v
	 *            represented the view from where to click popup will be appear.
	 * @return represented the return Rect type after drawing the rectangle.
	 */
	public static Rect locateView(View v) {
		int[] loc_int = new int[2];
		if (v == null)
			return null;
		try {
			v.getLocationOnScreen(loc_int);
		} catch (NullPointerException npe) {
			return null;
		}
		Rect location = new Rect();
		location.left = loc_int[0];
		location.top = loc_int[1];
		location.right = loc_int[0] + v.getWidth();
		location.bottom = loc_int[1] + v.getHeight();
		return location;
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

		dataProvider.getRestaurantEntries(IN_SECTION_ID, ((SmartActivity) getActivity()).getLatitude(), ((SmartActivity) getActivity()).getLongitude(),
				IN_FEATUREDFIRST, new WebCallListenerWithCacheInfo() {

			@Override
			public void onProgressUpdate(int progressCount) {
				proSeekBar.setProgress(progressCount);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2, int pageNo, int pageLimit,
					boolean fromCache) {

				try {
					fromDB = fromCache;
					if (responseCode == 200) {

						if (data2 != null) {
							entryListData = (List<ArrayList<HashMap<String, String>>>) data2;
							categories = entryListData.get(0).get(0).get(CATEGORIES);
							sectionID = entryListData.get(0).get(0).get(SECTIONID);
							prepareList((List<ArrayList<HashMap<String, String>>>) data2, false, fromCache, pageNo, pageLimit);
							listAdapterWithHolder = getListAdapter(listData);
							lstEntries.setAdapter(listAdapterWithHolder);
						} else {
							IjoomerUtilities.getCustomOkDialog(((IjoomerSuperMaster) getActivity()).getScreenCaption(),
									getString(getResources().getIdentifier("code204", "string", getActivity().getPackageName())), getString(R.string.ok),
									R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {
								@Override
								public void NeutralMethod() {
								}
							});
							sectionID = data1.get(0).get(SECTIONID);
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
		});

	}

	/**
	 * This method used to prepare header view.
	 */

	public void prepareHeader() {
		dataProvider.getDeal(IN_SECTION_ID, ((SmartActivity) getActivity()).getLatitude(), ((SmartActivity) getActivity()).getLongitude(), new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {

				try {
					if (responseCode == 200 && data1 != null && data1.size() > 0) {
						dealFragmentAdapter = new DealFragmentAdapter(getFragmentManager(), data1);
						viewPager.setAdapter(dealFragmentAdapter);
						viewPager.setCurrentItem(0);
					} else {
						lstEntries.removeHeaderView(headerView);

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
	 *            : data from response.
	 * 
	 * 
	 */
	public void prepareList(List<ArrayList<HashMap<String, String>>> data, boolean append, boolean fromCache, int pageno, int pagelimit) {
		if (data != null) {
			if (!append) {
				listData.clear();
			}

			for (ArrayList<HashMap<String, String>> hashMap : data) {

				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.sobipro_restaurant_entry_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hashMap);
				if (dataProvider.isFavourite(hashMap.get(0).get(ID), IN_PAGELAYOUT)) {
					obj.add("");
					dataProvider.addToFavourite(hashMap, IN_PAGELAYOUT);
				}
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

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.sobipro_restaurant_entry_item, listData, new ItemView() {
			@Override
			public View setItemView(final int position, View v, final SmartListItem item, final ViewHolder holder) {

				holder.imgEntryIcon = (ImageView) v.findViewById(R.id.imgEntryIcon);
				holder.txtTitle = (IjoomerTextView) v.findViewById(R.id.txtTitle);
				holder.txtDistance = (IjoomerTextView) v.findViewById(R.id.txtDistance);
				holder.txtPrice = (IjoomerTextView) v.findViewById(R.id.txtPrice);
				holder.txtGoodFor = (IjoomerTextView) v.findViewById(R.id.txtGoodFor);
				holder.rtbRating = (IjoomerRatingBar) v.findViewById(R.id.rtbRating);
				holder.btnFavourite = (IjoomerButton) v.findViewById(R.id.btnFavourite);
				holder.imgShare = (ImageView) v.findViewById(R.id.imgShare);
				holder.lnrEntry = (LinearLayout) v.findViewById(R.id.lnrEntry);

				@SuppressWarnings("unchecked")
				final ArrayList<HashMap<String, String>> value = (ArrayList<HashMap<String, String>>) item.getValues().get(0);
				try {
					for (HashMap<String, String> row : value) {

						if (row.get(LABELID).equalsIgnoreCase("field_good_for") && row.get(VALUE).trim().length() > 0) {
							holder.txtGoodFor.setText(row.get(VALUE));
							holder.txtGoodFor.setVisibility(View.VISIBLE);
							holder.txtGoodFor.setTag(row.get(VALUE));
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
					if (value.get(0).get(TITLE) != null && value.get(0).get(TITLE).length() > 0) {
						holder.txtTitle.setText(value.get(0).get(TITLE));
						holder.txtTitle.setVisibility(View.VISIBLE);
					}
					try {
						double dist = 0;
						if (!fromDB) {
							dist = IjoomerUtilities.convertDistance(value.get(0).get("distance"), IjoomerUtilities.MILE, IjoomerUtilities.MILE);
							holder.txtDistance.setText(dist + " " + getActivity().getString(R.string.sobipro_restaurant_miles_text));
							holder.txtDistance.setVisibility(View.VISIBLE);
						} else {
							dist = IjoomerUtilities.convertDistance(value.get(0).get("distance"), IjoomerUtilities.DEGREE, IjoomerUtilities.MILE);
							holder.txtDistance.setText(dist + " " + getActivity().getString(R.string.sobipro_restaurant_miles_text));
							holder.txtDistance.setVisibility(View.VISIBLE);
						}
					} catch (Exception e) {

					}

					try {
						String[] image = ((IjoomerSuperMaster) getActivity()).getStringArray(value.get(0).get(IMG_GALLERIES));
						if (image != null && image.length > 0) {
							androidAQuery.id(holder.imgEntryIcon).image(image[0], true, true, ((SmartActivity) getActivity()).getDeviceWidth(), R.drawable.sobipro_default_image);
							holder.imgEntryIcon.setTag(image[0]);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					holder.rtbRating.setFilledStarResourceId(R.drawable.sobipro_rating_transparent_entry_detail);
					holder.rtbRating.setHalfFilledStarResourceId(R.drawable.sobipro_rating_transparent_half_entry_detail);
					holder.rtbRating.setEmptyStarResourceId(R.drawable.sobipro_rating_empty_star);
					holder.rtbRating.setStarBgColor(getActivity().getResources().getColor(R.color.sobipro_green));
					holder.rtbRating.setStarRating(Float.parseFloat(value.get(0).get(AVERAGERATING)) / 2);
					holder.lnrEntry.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							try {
								prepareIDS();

								((SmartActivity) getActivity()).loadNew(SobiproEntryDetailActivity.class, getActivity(), false, "IN_ENTRY_ID_ARRAY", ID_ARRAY, "IN_ENTRY_INDEX",
										position, "IN_TABLE", SOBIPRO_RESTAURANT_ENTRIES, "IN_PAGELAYOUT", IN_PAGELAYOUT, "IN_FEATUREDFIRST", IN_FEATUREDFIRST);
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					});
					holder.imgEntryIcon.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							try {
								prepareIDS();

								((SmartActivity) getActivity()).loadNew(SobiproEntryDetailActivity.class, getActivity(), false, "IN_ENTRY_ID_ARRAY", ID_ARRAY, "IN_ENTRY_INDEX",
										position, "IN_TABLE", SOBIPRO_RESTAURANT_ENTRIES, "IN_PAGELAYOUT", IN_PAGELAYOUT);
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					});
					holder.imgShare.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							try {
								String imageThumb = "";
								String goodFor = "";
								String shareLink = "";
								if (value.get(0).get(SHARELINK) != null && value.get(0).get(SHARELINK).trim().length() > 0) {
									shareLink = value.get(0).get(SHARELINK);
								}
								if (holder.txtGoodFor.getTag() != null && holder.txtGoodFor.getTag().toString().trim().length() > 0) {
									goodFor = holder.txtGoodFor.getTag().toString();
								}
								if (holder.imgEntryIcon.getTag() != null && holder.imgEntryIcon.getTag().toString().trim().length() > 0) {
									imageThumb = holder.imgEntryIcon.getTag().toString();

								}

								((SmartActivity) getActivity()).loadNew(IjoomerShareActivity.class, getActivity(), false, "IN_SHARE_CAPTION", value.get(0).get("title"),
										"IN_SHARE_DESCRIPTION", goodFor, "IN_SHARE_THUMB", imageThumb, "IN_SHARE_SHARELINK", shareLink);
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					});
					if (item.getValues().size() > 1) {
						holder.btnFavourite.setEnabled(false);
						holder.btnFavourite.setBackgroundResource(R.drawable.sobipro_favourite_desabled);
					} else {
						holder.btnFavourite.setEnabled(true);
						holder.btnFavourite.setBackgroundResource(R.drawable.sobipro_add_favourite_green_btn);

					}

					holder.btnFavourite.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							try {
								dataProvider.addToFavourite(value, IN_PAGELAYOUT);
								item.getValues().add("");
								Toast.makeText(getActivity(), getString(R.string.sobipro_addtofavorite), Toast.LENGTH_SHORT).show();
								listAdapterWithHolder.notifyDataSetChanged();
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					});

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
		try {
			for (SmartListItem row : listData) {
				ID_ARRAY.add(((ArrayList<HashMap<String, String>>) row.getValues().get(0)).get(0).get(ID));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Custom class Adapter
	 */
	private class DealFragmentAdapter extends FragmentStatePagerAdapter {

		ArrayList<HashMap<String, String>> dealArray;

		public DealFragmentAdapter(FragmentManager fm, ArrayList<HashMap<String, String>> dealArray) {
			super(fm);
			this.dealArray = dealArray;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public Fragment getItem(int pos) {
			System.gc();

			return new SobiproRestaurantDealFragment(dealArray.get(pos));

		}

		@Override
		public int getCount() {
			return dealArray.size();
		}
	}

}
