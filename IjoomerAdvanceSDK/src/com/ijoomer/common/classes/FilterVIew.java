package com.ijoomer.common.classes;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.ijoomer.custom.interfaces.CustomClickListner;
import com.ijoomer.custom.interfaces.FilterListener;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerCheckBox;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerRadioButton;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.src.R;
import com.smart.framework.ItemView;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

import java.util.ArrayList;

/**
 * This Class Contains All Method Related To FilterVIew.
 * 
 * @author tasol
 * NOTE : currently this class not used,future development
 */
public class FilterVIew {

	private PopupWindow filterPopUp;
	private Context mContext;
	private int filterHeight;
	private int filterWidth;
	private ArrayList<FilterItem> filterItems;
	private ListView lstFilterData;
	private LinearLayout lnrFilterItems;
	private IjoomerButton btnApplyl;
	private IjoomerButton btnReset;
	private LinearLayout lnrMultiSelection;
	private IjoomerButton btnAll;
	private IjoomerButton btnNone;
	private ArrayList<SmartListItem> filterListData;
	private int currentSelectedItemIndex = 0;
	private FilterListener local;
	private LinearLayout lnrList;
	private LinearLayout lnrDate;
	private IjoomerEditText edtStartDate;
	private IjoomerEditText edtEndDate;
	private LinearLayout lnrTime;
	private IjoomerEditText edtStartTime;
	private IjoomerEditText edtEndTime;
	private LinearLayout lnrLocation;

	public static final int DATE = 1;
	public static final int TIME = 2;
	public static final int LOCATION = 3;

	public FilterVIew(Context mContext) {
		this.mContext = mContext;
		filterItems = new ArrayList<FilterItem>();
		filterListData = new ArrayList<SmartListItem>();
		filterHeight = ((SmartActivity) mContext).getDeviceWidth();
		filterWidth = ((SmartActivity) mContext).getDeviceHeight();

	}

	public void setOnFilterListener(FilterListener filterListener) {
		local = filterListener;
	}

	public int getFilterHeight() {
		return filterHeight;
	}

	public void setFilterHeight(int filterHeight) {
		this.filterHeight = filterHeight;
	}

	public int getFilterWidth() {
		return filterWidth;
	}

	public void setFilterWidth(int filterWidth) {
		this.filterWidth = filterWidth;
	}

	public void addFilterItem(String itemCaption, Drawable itemIconNormal, Drawable itemIconActive, String defaultItem, ArrayList<String> dataToFilter, boolean allowMultiSelection) {

		FilterItem filterItem = new FilterItem();
		filterItem.setItemCaption(itemCaption);
		filterItem.setItemIconNormal(itemIconNormal);
		filterItem.setItemIconActive(itemIconActive);
		filterItem.setDefaultItem(defaultItem);
		filterItem.setItemData(dataToFilter);
		ArrayList<String> selected = new ArrayList<String>();
		if (allowMultiSelection) {
			if (defaultItem.trim().length() <= 0) {
				selected.addAll(dataToFilter);
			} else {
				selected.add(defaultItem);
			}
		} else {
			selected.add(defaultItem);
		}
		filterItem.setSelectedItems(selected);
		filterItem.setAllowMultipleSelection(allowMultiSelection);
		filterItem.setHasChange(false);
		filterItems.add(filterItem);
	}

	public void addFilterItem(String itemCaption, Drawable itemIconNormal, Drawable itemIconActive, int type) {
		FilterItem filterItem = new FilterItem();
		filterItem.setItemCaption(itemCaption);
		filterItem.setItemIconNormal(itemIconNormal);
		filterItem.setItemIconActive(itemIconActive);
		filterItem.setHasChange(false);
		filterItem.setType(type);
		filterItems.add(filterItem);
	}

	@SuppressWarnings("deprecation")
	public void showFilter() {

		if (filterItems.size() <= 0) {
			Toast.makeText(mContext, "please add filterItems", Toast.LENGTH_SHORT).show();
		} else {
			try {
				LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View layout = layoutInflater.inflate(R.layout.smart_filter, null);

				filterPopUp = new PopupWindow(mContext);
				filterPopUp.setContentView(layout);
				filterPopUp.setWidth(getFilterWidth());
				filterPopUp.setHeight(getFilterHeight());
				filterPopUp.setFocusable(true);
				filterPopUp.setBackgroundDrawable(new BitmapDrawable(mContext.getResources()));
				filterPopUp.showAtLocation(layout, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);

				lnrLocation = (LinearLayout) layout.findViewById(R.id.lnrLocation);
				lnrList = (LinearLayout) layout.findViewById(R.id.lnrList);
				lstFilterData = (ListView) layout.findViewById(R.id.lstFilterData);
				lnrFilterItems = (LinearLayout) layout.findViewById(R.id.lnrFilterItems);
				btnReset = (IjoomerButton) layout.findViewById(R.id.btnReset);
				btnApplyl = (IjoomerButton) layout.findViewById(R.id.btnApply);
				btnAll = (IjoomerButton) layout.findViewById(R.id.btnAll);
				btnNone = (IjoomerButton) layout.findViewById(R.id.btnNone);
				lnrMultiSelection = (LinearLayout) layout.findViewById(R.id.lnrMultiSelection);

				lnrDate = (LinearLayout) layout.findViewById(R.id.lnrDate);
				edtStartDate = (IjoomerEditText) layout.findViewById(R.id.edtStartDate);
				edtEndDate = (IjoomerEditText) layout.findViewById(R.id.edtEndDate);

				edtEndDate.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(final View v) {
						IjoomerUtilities.getDateDialog(((IjoomerEditText) v).getText().toString(), false, new CustomClickListner() {

							@Override
							public void onClick(String value) {
								((IjoomerEditText) v).setText(value);
							}
						});
					}
				});
				edtStartDate.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(final View v) {
						IjoomerUtilities.getDateDialog(((IjoomerEditText) v).getText().toString(), false, new CustomClickListner() {

							@Override
							public void onClick(String value) {
								((IjoomerEditText) v).setText(value);
							}
						});
					}
				});

				lnrTime = (LinearLayout) layout.findViewById(R.id.lnrTime);
				edtStartTime = (IjoomerEditText) layout.findViewById(R.id.edtStartTime);
				edtEndTime = (IjoomerEditText) layout.findViewById(R.id.edtEndTime);

				edtEndTime.setOnClickListener(new OnClickListener() {

					@SuppressWarnings("unused")
					@Override
					public void onClick(final View v) {
						int h;
						int m;
						if (((IjoomerEditText) v).getText().toString() != null && ((IjoomerEditText) v).getText().toString().length() > 0) {
							h = Integer.parseInt(((IjoomerEditText) v).getText().toString().split(":")[0]);
							m = Integer.parseInt(((IjoomerEditText) v).getText().toString().split(":")[1]);
						}
						IjoomerUtilities.getTimeDialog("", new CustomClickListner() {

							@Override
							public void onClick(String value) {
								((IjoomerEditText) v).setText(value);

							}
						});
					}
				});
				edtStartTime.setOnClickListener(new OnClickListener() {

					@SuppressWarnings("unused")
					@Override
					public void onClick(final View v) {
						int h;
						int m;
						if (((IjoomerEditText) v).getText().toString() != null && ((IjoomerEditText) v).getText().toString().length() > 0) {
							h = Integer.parseInt(((IjoomerEditText) v).getText().toString().split(":")[0]);
							m = Integer.parseInt(((IjoomerEditText) v).getText().toString().split(":")[1]);
						}
						IjoomerUtilities.getTimeDialog("", new CustomClickListner() {

							@Override
							public void onClick(String value) {
								((IjoomerEditText) v).setText(value);

							}
						});
					}
				});

				btnApplyl.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (local != null) {
							local.onFilterApply(filterItems);
						}

					}
				});
				btnAll.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						filterItems.get(currentSelectedItemIndex).getSelectedItems().clear();
						filterItems.get(currentSelectedItemIndex).getSelectedItems().addAll(filterItems.get(currentSelectedItemIndex).getItemData());
						prepareFilterList(filterItems.get(currentSelectedItemIndex));
						lstFilterData.setAdapter(getFilterListAdapter());
						invalidateView();
					}
				});
				btnNone.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						filterItems.get(currentSelectedItemIndex).getSelectedItems().clear();
						prepareFilterList(filterItems.get(currentSelectedItemIndex));
						lstFilterData.setAdapter(getFilterListAdapter());
						invalidateView();
					}
				});

				btnReset.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						filterPopUp.dismiss();
					}
				});

				currentSelectedItemIndex = 0;
				prepareFilterItem(filterItems.get(0));
				int size = filterItems.size();
				for (int i = 0; i < size; i++) {
					View v = layoutInflater.inflate(R.layout.filter_tab_item, null);
					final FilterItem filterItem = filterItems.get(i);
					((IjoomerTextView) v.findViewById(R.id.btnItem)).setText(filterItem.getItemCaption());

					final LinearLayout lnrItem = (LinearLayout) v.findViewById(R.id.lnrItem);
					lnrItem.setTag(i);
					lnrFilterItems.addView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
					lnrItem.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							currentSelectedItemIndex = (Integer) v.getTag();
							prepareFilterItem(filterItems.get(currentSelectedItemIndex));
							invalidateView();
							if (!filterItem.getItemCaption().equalsIgnoreCase(filterItems.get(currentSelectedItemIndex).getItemCaption())) {

							}
						}
					});
				}
				invalidateView();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	private SmartListAdapterWithHolder getFilterListAdapter() {

		final SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(mContext, R.layout.filter_list_item, filterListData, new ItemView() {
			@Override
			public View setItemView(final int position, View v, SmartListItem item, ViewHolder holder) {
				holder.txtFilterString = (IjoomerTextView) v.findViewById(R.id.txtFilterString);
				holder.rdbSelectItem = (IjoomerRadioButton) v.findViewById(R.id.rdbSelectItem);
				holder.chkSelectItem = (IjoomerCheckBox) v.findViewById(R.id.chkSelectItem);

				final String strFilter = item.getValues().get(0).toString();
				final Boolean isSelected = (Boolean) item.getValues().get(1);
				holder.txtFilterString.setText(strFilter);

				if (filterItems.get(currentSelectedItemIndex).isAllowMultipleSelection()) {
					holder.chkSelectItem.setVisibility(View.VISIBLE);
					holder.rdbSelectItem.setVisibility(View.GONE);
					if (isSelected) {
						holder.chkSelectItem.setChecked(true);
					} else {
						holder.chkSelectItem.setChecked(false);
					}
				} else {
					holder.rdbSelectItem.setVisibility(View.VISIBLE);
					holder.chkSelectItem.setVisibility(View.GONE);
					if (isSelected) {
						holder.rdbSelectItem.setChecked(true);
					} else {
						holder.rdbSelectItem.setChecked(false);
					}
				}

				holder.txtFilterString.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						performItemClick(strFilter);
					}
				});
				holder.chkSelectItem.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						performItemClick(strFilter);
					}
				});

				holder.rdbSelectItem.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						performItemClick(strFilter);
					}
				});

				return v;
			}

			@Override
			public View setItemView(int position, View v, SmartListItem item) {
				return null;
			}
		});
		return adapterWithHolder;
	}

	private void prepareFilterList(FilterItem filterItem) {
		filterListData.clear();

		filterItem.setHasChange(false);
		if (filterItem.isAllowMultipleSelection()) {
			if (filterItem.getDefaultItem() == null || filterItem.getDefaultItem().trim().length() <= 0) {
				if ((filterItem.getSelectedItems().size() < filterItem.getItemData().size())) {
					filterItem.setHasChange(true);
				}
			} else if (filterItem.getSelectedItems().size() > 1 || filterItem.getSelectedItems().size() <= 0) {
				filterItem.setHasChange(true);
			} else if (filterItem.getSelectedItems().size() == 1 && !(filterItem.getSelectedItems().contains(filterItem.getDefaultItem()))) {
				filterItem.setHasChange(true);
			}
		} else {
			if (filterItem.getSelectedItems().contains(filterItem.getDefaultItem())) {
				filterItem.setHasChange(false);
			} else {
				filterItem.setHasChange(true);
			}
		}
		int size = filterItem.getItemData().size();
		for (int i = 0; i < size ; i++) {
			SmartListItem item = new SmartListItem();
			item.setItemLayout(R.layout.filter_list_item);
			ArrayList<Object> obj = new ArrayList<Object>();
			obj.add(filterItem.getItemData().get(i));

			if (filterItem.getDefaultItem().trim().length() <= 0 && filterItem.getSelectedItems().size() == filterItem.getItemData().size()) {
				obj.add(true);
			} else {
				if (filterItem.getSelectedItems().contains(filterItem.getItemData().get(i))) {
					obj.add(true);
				} else {
					obj.add(false);
				}
			}
			item.setValues(obj);
			filterListData.add(item);
		}

	}

	private void prepareFilterItem(FilterItem filterItem) {
		if (filterItem.getType() == DATE) {
			lnrDate.setVisibility(View.VISIBLE);
			lnrList.setVisibility(View.GONE);
			lnrTime.setVisibility(View.GONE);
			lnrLocation.setVisibility(View.GONE);
		} else if (filterItem.getType() == TIME) {
			lnrDate.setVisibility(View.GONE);
			lnrList.setVisibility(View.GONE);
			lnrTime.setVisibility(View.VISIBLE);
			lnrLocation.setVisibility(View.GONE);
		} else if (filterItem.getType() == LOCATION) {
			lnrDate.setVisibility(View.GONE);
			lnrList.setVisibility(View.GONE);
			lnrTime.setVisibility(View.GONE);
			lnrLocation.setVisibility(View.VISIBLE);
		} else {
			lnrDate.setVisibility(View.GONE);
			lnrList.setVisibility(View.VISIBLE);
			lnrTime.setVisibility(View.GONE);
			lnrLocation.setVisibility(View.GONE);
			prepareFilterList(filterItem);
			lstFilterData.setAdapter(getFilterListAdapter());
		}
	}

	@SuppressWarnings("deprecation")
	private void invalidateView() {

		if (filterItems.get(currentSelectedItemIndex).isAllowMultipleSelection()) {
			lnrMultiSelection.setVisibility(View.VISIBLE);
		} else {
			lnrMultiSelection.setVisibility(View.GONE);
		}
		int size = filterItems.size();
		for (int j = 0; j < size; j++) {
			if (j == currentSelectedItemIndex) {
				if (filterItems.get(j).isHasChange()) {
					((LinearLayout) lnrFilterItems.getChildAt(j).findViewById(R.id.lnrItem)).setBackgroundDrawable(mContext.getResources().getDrawable(
							R.drawable.filter_selected_haschange));
				} else {
					((LinearLayout) lnrFilterItems.getChildAt(j).findViewById(R.id.lnrItem)).setBackgroundDrawable(mContext.getResources().getDrawable(
							R.drawable.filter_selected_nochange));
				}
				((ImageView) ((LinearLayout) lnrFilterItems.getChildAt(j).findViewById(R.id.lnrItem)).findViewById(R.id.imgItemIcon)).setImageDrawable(filterItems.get(j)
						.getItemIconActive());
			} else {
				if (filterItems.get(j).isHasChange()) {
					((LinearLayout) lnrFilterItems.getChildAt(j).findViewById(R.id.lnrItem)).setBackgroundDrawable(mContext.getResources().getDrawable(
							R.drawable.filter_default_haschange));
				} else {
					((LinearLayout) lnrFilterItems.getChildAt(j).findViewById(R.id.lnrItem)).setBackgroundDrawable(mContext.getResources().getDrawable(
							R.drawable.filter_default_nochange));
				}
				((ImageView) ((LinearLayout) lnrFilterItems.getChildAt(j).findViewById(R.id.lnrItem)).findViewById(R.id.imgItemIcon)).setImageDrawable(filterItems.get(j)
						.getItemIconNormal());
			}
		}

	}

	private void performItemClick(String strFilter) {
		if (filterItems.get(currentSelectedItemIndex).isAllowMultipleSelection()) {
			if (filterItems.get(currentSelectedItemIndex).getSelectedItems().contains(strFilter)) {
				filterItems.get(currentSelectedItemIndex).getSelectedItems().remove(strFilter);
			} else {
				filterItems.get(currentSelectedItemIndex).getSelectedItems().add(strFilter);
			}
			prepareFilterList(filterItems.get(currentSelectedItemIndex));
			lstFilterData.setAdapter(getFilterListAdapter());
		} else {
			filterItems.get(currentSelectedItemIndex).getSelectedItems().clear();
			filterItems.get(currentSelectedItemIndex).getSelectedItems().add(strFilter);
			prepareFilterList(filterItems.get(currentSelectedItemIndex));
			lstFilterData.setAdapter(getFilterListAdapter());
		}
		invalidateView();
	}
}
