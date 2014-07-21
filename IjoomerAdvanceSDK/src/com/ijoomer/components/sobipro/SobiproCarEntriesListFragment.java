package com.ijoomer.components.sobipro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerListView;
import com.ijoomer.customviews.IjoomerMultiPurposeSelector;
import com.ijoomer.customviews.IjoomerRadioButton;
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

/**
 * This Fragment Contains All Method Related To SobiproEntriesListFragment.
 * 
 * @author tasol
 * 
 */
public class SobiproCarEntriesListFragment extends SmartFragment implements SobiproTagHolder {
	private String IN_PARENT_ID, IN_SECTION_ID, IN_PAGELAYOUT;
	private String IN_FEATUREDFIRST = "No";
	private int IN_POS;
	private IjoomerListView lstEntries;
	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder listAdapterWithHolder;
	private AQuery androidAQuery;
	private boolean isFilterSortEnable;
	private SobiproCategoriesDataProvider categoriesDataProvider;
	private String sortBy = "";
	private String orderBy = "";
	private String filter = "";
	private ImageView imgMap;
	private ImageView imgFilter;
	private ImageView imgFilterSort;
	private ImageView imgAddEntry;
	private String sectionID;

	private List<ArrayList<HashMap<String, String>>> entryListData;
	private View listFooter;
	private ArrayList<String> ID_ARRAY;
	private SeekBar proSeekBar;

	public SobiproCarEntriesListFragment(String IN_SECTION_ID, String IN_PARENT_ID, int IN_POS, String IN_PAGELAYOUT, String IN_FEATUREDFIRST) {
		this.IN_PAGELAYOUT = IN_PAGELAYOUT;
		this.IN_POS = IN_POS;
		this.IN_PARENT_ID = IN_PARENT_ID;
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
	public void initComponents(View currentView) {

		categoriesDataProvider = new SobiproCategoriesDataProvider(getActivity());
		isFilterSortEnable = false;
		lstEntries = (IjoomerListView) currentView.findViewById(R.id.lstEntries);
		listFooter = LayoutInflater.from(getActivity()).inflate(R.layout.ijoomer_list_footer, null);
		lstEntries.addFooterView(listFooter, null, false);
		ID_ARRAY = new ArrayList<String>();
		androidAQuery = new AQuery(getActivity());

	}

	@Override
	public void prepareViews(View currentView) {
		imgMap = ((ImageView) ((SmartActivity) getActivity()).getHeaderView().findViewById(R.id.imgMap));
		imgFilter = ((ImageView) ((SmartActivity) getActivity()).getHeaderView().findViewById(R.id.imgFilter));
		imgFilterSort = ((ImageView) ((SmartActivity) getActivity()).getHeaderView().findViewById(R.id.imgFilterSort));
		imgAddEntry = ((ImageView) ((SmartActivity) getActivity()).getHeaderView().findViewById(R.id.imgAddEntry));
		imgMap.setVisibility(View.GONE);
		imgFilter.setVisibility(View.VISIBLE);
		imgFilterSort.setVisibility(View.VISIBLE);
		imgAddEntry.setVisibility(View.VISIBLE);

		if (IN_PARENT_ID != null) {
			getEntries();
		} else {
			prepareList((List<ArrayList<HashMap<String, String>>>) entryListData, false, false, 1, 10);
			listAdapterWithHolder = getListAdapter(listData);
			lstEntries.setAdapter(listAdapterWithHolder);
		}
	}

	@Override
	public void setActionListeners(View currentView) {

		lstEntries.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 1) {

					if (!categoriesDataProvider.isCalling() && categoriesDataProvider.hasNextPage()) {
						listFooterVisible();
						if (isFilterSortEnable) {
							categoriesDataProvider.getEntries(IN_SECTION_ID, IN_PARENT_ID, sortBy, orderBy, filter, IN_FEATUREDFIRST, new WebCallListener() {

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

							categoriesDataProvider.getEntries(IN_SECTION_ID, IN_PARENT_ID, IN_FEATUREDFIRST, new WebCallListenerWithCacheInfo() {

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
												prepareList((List<ArrayList<HashMap<String, String>>>) data2, true, fromCache, pageNo, pageLimit);
											}
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});

						} else {

							categoriesDataProvider.getEntries(IN_SECTION_ID, IN_PARENT_ID, IN_FEATUREDFIRST, new WebCallListenerWithCacheInfo() {

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
												prepareList((List<ArrayList<HashMap<String, String>>>) data2, true, fromCache, pageNo, pageLimit);
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
			}
		});

		lstEntries.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {

					prepareIDS();
					((SmartActivity) getActivity()).loadNew(SobiproEntryDetailActivity.class, getActivity(), false, "IN_CAT_ID", IN_PARENT_ID, "IN_ENTRY_ID_ARRAY", ID_ARRAY,
							"IN_ENTRY_INDEX", arg2, "IN_TABLE", SOBIPROENTRIES, "IN_POS", IN_POS, "IN_PAGELAYOUT", IN_PAGELAYOUT, "IN_FEATUREDFIRST", IN_FEATUREDFIRST);
				} catch (Exception e) {
					e.printStackTrace();
				}
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
					((SmartActivity) getActivity()).loadNew(SobiproAddEntryActivity.class, getActivity(), false, "IN_SECTION_ID", sectionID, "IN_CAT_ID", IN_PARENT_ID,
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
					((SmartActivity) getActivity()).loadNew(SobiproMapActivity.class, getActivity(), false, "IN_ENTRY_ID_ARRAY", ID_ARRAY, "IN_TABLE", SOBIPROENTRIES, "IN_POS",
							IN_POS);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		imgFilterSort.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showFilterSortPopup(v);
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
		Rect r = locateView(v);
		sortBy = "";
		LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View layout = layoutInflater.inflate(R.layout.sobipro_car_filtersort_popup, null);
		final RadioGroup btnSortingBy = (RadioGroup) layout.findViewById(R.id.btnSortingBy);
		IjoomerRadioButton btnAscending = (IjoomerRadioButton) layout.findViewById(R.id.btnAscending);
		IjoomerRadioButton btnDescending = (IjoomerRadioButton) layout.findViewById(R.id.btnDescending);

		IjoomerMultiPurposeSelector selector = new IjoomerMultiPurposeSelector(getActivity());
		selector.setDefaultDrawableResource(R.drawable.sobipro_ascending);
		selector.setPressedDrawableResource(SobiproMasterActivity.themes[IN_POS].ascendingDrawable);

		btnAscending.setButtonDrawable(selector.getSelector());
		IjoomerMultiPurposeSelector selectorDescending = new IjoomerMultiPurposeSelector(getActivity());
		selectorDescending.setDefaultDrawableResource(R.drawable.sobipro_descending);
		selectorDescending.setPressedDrawableResource(SobiproMasterActivity.themes[IN_POS].descendingDrawable);

		btnDescending.setButtonDrawable(selectorDescending.getSelector());

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

				if (((IjoomerRadioButton) layout.findViewById(btnSortingBy.getCheckedRadioButtonId())) != null) {
					sortBy = ((IjoomerRadioButton) layout.findViewById(btnSortingBy.getCheckedRadioButtonId())).getText().toString().toLowerCase();
				}
				if (((IjoomerRadioButton) layout.findViewById(R.id.btnDescending)).isChecked()) {
					orderBy = "desc";
				} else {
					orderBy = "asc";
				}
				if (popup.isShowing())
					popup.dismiss();
				if (sortBy.length() > 0) {
					proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
					isFilterSortEnable = true;
					categoriesDataProvider.restorePagingSettings();
					categoriesDataProvider.setPageNo(1);
					categoriesDataProvider.getEntries(IN_SECTION_ID, IN_PARENT_ID, sortBy, orderBy, filter, IN_FEATUREDFIRST, new WebCallListener() {

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
					});
				} else {
					isFilterSortEnable = false;
				}

			}
		});

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

		categoriesDataProvider.getEntries(IN_SECTION_ID, IN_PARENT_ID, IN_FEATUREDFIRST, new WebCallListenerWithCacheInfo() {

			@Override
			public void onProgressUpdate(int progressCount) {
				proSeekBar.setProgress(progressCount);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2, int pageNo, int pageLimit,
					boolean fromCache) {

				try {
					if (responseCode == 200) {
						if (data2 != null) {
							entryListData = (List<ArrayList<HashMap<String, String>>>) data2;
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
				item.setItemLayout(R.layout.sobipro_car_entries_item);
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

						if (row.get(LABELID).equalsIgnoreCase("field_first_registration") && row.get(VALUE).length() > 0)
							regYear = row.get(VALUE);

						if (row.get(LABELID).equalsIgnoreCase("field_fuel_type") && row.get(VALUE).length() > 0)
							fuelType = "-" + row.get(VALUE);

						if (row.get(LABELID).equalsIgnoreCase("field_mileage") && row.get(VALUE).length() > 0) {
							mileage = "-" + row.get(VALUE);
							mileageUnit = row.get(UNIT);
						}
						if (row.get(TYPE).equalsIgnoreCase("phone") && row.get(VALUE).length() > 0) {
							holder.imgCall.setTag(row.get(VALUE));
						}
						if (row.get(TYPE).equalsIgnoreCase("email") && row.get(VALUE).length() > 0) {
							holder.imgEmail.setTag(row.get(VALUE));
						}

						if (row.get(LABELID).equalsIgnoreCase("field_gearbox") && row.get(VALUE).length() > 0)
							gearBox = "-" + row.get(VALUE);

						if (row.get(LABELID).equalsIgnoreCase("field_power") && row.get(VALUE).length() > 0) {
							power = "-" + row.get(VALUE);
							powerUnit = row.get(UNIT);
						}

						holder.txtCarDetail.setText(regYear + fuelType + mileage + mileageUnit + gearBox + power + powerUnit);

						if (row.get(LABELID).equalsIgnoreCase("field_price") && row.get(VALUE).length() > 0) {
							holder.txtPrice.setVisibility(View.VISIBLE);
							priceUnit = row.get(UNIT);
							holder.txtPrice.setText(priceUnit + " " + row.get(VALUE));
						}

					}

					try {
						image = ((IjoomerSuperMaster) getActivity()).getStringArray(value.get(0).get(IMG_GALLERIES));
						if (image != null)
							androidAQuery.id(holder.imgEntryIcon).image(image[0], true, true, ((SmartActivity) getActivity()).getDeviceWidth(), R.drawable.sobipro_default_image);

					} catch (Exception e) {
						e.printStackTrace();
					}
					if (holder.imgCall.getTag() == null || holder.imgCall.getTag().toString().trim().length() == 0)
						holder.imgCall.setVisibility(View.INVISIBLE);

					if (holder.imgEmail.getTag() == null || holder.imgEmail.getTag().toString().trim().length() == 0)
						holder.imgEmail.setVisibility(View.INVISIBLE);

					if (value.get(0).get(TITLE) != null && value.get(0).get(TITLE).trim().length() > 0) {
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
