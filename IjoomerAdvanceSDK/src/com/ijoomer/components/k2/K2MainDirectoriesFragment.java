package com.ijoomer.components.k2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.IjoomerListView;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.k2.k2MainDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.CacheCallListener;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Fragment Contains All Method Related To K2MainDirectoriesFragment.
 * 
 * @author tasol
 * 
 */
public class K2MainDirectoriesFragment extends SmartFragment implements K2TagHolder {

	private IjoomerListView listView;
	private SeekBar proSeekBar;

	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private k2MainDataProvider provider;
	private SmartListAdapterWithHolder adapter;
	private AQuery androidQuery;

	private String IN_MENUID;
	private String ITEMID = "itemid";

	public K2MainDirectoriesFragment() {
	}

	/**
	 * Overrides method
	 */

	@Override
	public int setLayoutId() {
		return R.layout.k2_main_directories;
	}

	@Override
	public void initComponents(View currentView) {
		listView = (IjoomerListView) currentView.findViewById(R.id.listView);

		androidQuery = new AQuery(getActivity());
		provider = new k2MainDataProvider(getActivity());
	}

	@Override
	public void prepareViews(View currentView) {
		updateFragment();
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void setActionListeners(View currentView) {

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * Class methods
	 */

	public void updateFragment() {
		getIntentData();
		prepareDirectoriesList(true);
	}

	/**
	 * This method used to update fragment.
	 */
	private void getIntentData() {
		try {
			IN_MENUID = getActivity().getIntent().getStringExtra("IN_OBJ") == null ? "0" : new JSONObject(getActivity().getIntent().getStringExtra("IN_OBJ")).getString(ITEMID);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method used to get data from server
	 * 
	 * @param isProgressShow
	 *            represented isProgressShow
	 */
	private void getDataFromServer(final boolean isProgressShow) {

		provider.getCategories(IN_MENUID, new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {
				if (isProgressShow) {
					proSeekBar.setProgress(progressCount);
				}

			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				try {
					if (responseCode == 200) {
						prepareDirectoriesList(false);
					} else {
						responseErrorMessageHandler(responseCode, true);
					}
				} catch (Throwable e) {

				}
			}
		});
	}

	/**
	 * This method used to shown response message.
	 * 
	 * @param responseCode
	 *            represented response code
	 * @param finishActivityOnConnectionProblem
	 *            represented finish activity on connection problem
	 */
	private void responseErrorMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {
		IjoomerUtilities.getCustomOkDialog(getActivity().getString(R.string.dialog_k2_directories), getActivity().getString(getResources().getIdentifier("code" + responseCode, "string", getActivity().getPackageName())),
				getActivity().getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

					@Override
					public void NeutralMethod() {
						
					}
				});
	}

	/**
	 * This method used to prepare list category with items.
	 * 
	 * @param isProgressShow
	 */
	private void prepareDirectoriesList(final boolean isProgressShow) {
		if (isProgressShow) {
			proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
		}
		provider.getMainCategoryByName("0", IN_MENUID, new CacheCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {
				if (isProgressShow) {
					proSeekBar.setProgress(progressCount);
				}
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, final ArrayList<HashMap<String, String>> parentData1, Object data2) {
				try {
					if (responseCode == 200) {
						listData.clear();
						for (final HashMap<String, String> category : parentData1) {
							provider.getItemByName(category.get(ID), IN_MENUID, new CacheCallListener() {

								@Override
								public void onProgressUpdate(int progressCount) {
								}

								@Override
								public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
									SmartListItem item = new SmartListItem();
									item.setItemLayout(R.layout.k2_main_directories_list_item);
									ArrayList<Object> obj = new ArrayList<Object>();
									obj.add(category);
									obj.add(data1);
									item.setValues(obj);
									listData.add(item);
									if (parentData1.size() == listData.size()) {
										if (adapter == null) {
											adapter = getListAdapter();
											listView.setAdapter(adapter);
										} else {
											adapter.notifyDataSetChanged();
										}
									}
								}
							});

						}

					}
					if (provider.hasNextPage()) {
						if (responseCode == 200) {
							getDataFromServer(false);
						} else {
							getDataFromServer(true);
						}
					}
				} catch (Throwable e) {

				}
			}
		});
	}

	/**
	 * List adapter for category with items.
	 * 
	 * @return represented {@link SmartListAdapterWithHolder}
	 */
	private SmartListAdapterWithHolder getListAdapter() {
		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.k2_main_directories_list_item, listData, new ItemView() {
			@SuppressWarnings("unchecked")
			@Override
			public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {

				holder.txtk2DirectoriesCategoryName = (IjoomerTextView) v.findViewById(R.id.txtk2DirectoriesCategoryName);
				holder.hrzk2DirectoriesCategoryItem = (HorizontalScrollView) v.findViewById(R.id.hrzk2DirectoriesCategoryItem);
				holder.txtk2DirectoriesCategoryNoItem = (IjoomerTextView) v.findViewById(R.id.txtk2DirectoriesCategoryNoItem);
				holder.lnrk2DirectoriesScrollable = (LinearLayout) v.findViewById(R.id.lnrk2DirectoriesScrollable);
				holder.hrzk2DirectoriesCategoryItem.setVisibility(View.GONE);
				holder.txtk2DirectoriesCategoryNoItem.setVisibility(View.GONE);

				final HashMap<String, String> categoryData = (HashMap<String, String>) item.getValues().get(0);
				final ArrayList<HashMap<String, String>> categoryItemData = (ArrayList<HashMap<String, String>>) item.getValues().get(1);

				holder.txtk2DirectoriesCategoryName.setText(categoryData.get(NAME));
				if (categoryItemData != null && categoryItemData.size() > 0) {
					holder.hrzk2DirectoriesCategoryItem.setVisibility(View.VISIBLE);
					holder.lnrk2DirectoriesScrollable.removeAllViews();
					for (int i = 0; i < categoryItemData.size(); i++) {
						View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.k2_main_directories_catrgory_item, null);
						ImageView imgk2DirectoriesCategoryItem = (ImageView) itemView.findViewById(R.id.imgk2DirectoriesCategoryItem);
						IjoomerTextView txtk2DirectoriesCategoryItemName = (IjoomerTextView) itemView.findViewById(R.id.txtk2DirectoriesCategoryItemName);
						FrameLayout frmk2DirectoriesCategoryItem = (FrameLayout) itemView.findViewById(R.id.frmk2DirectoriesCategoryItem);
						androidQuery.id(imgk2DirectoriesCategoryItem).image(categoryItemData.get(i).get(IMAGESMALL), true, true, 100, R.drawable.k2_default);
						txtk2DirectoriesCategoryItemName.setText(categoryItemData.get(i).get(TITLE));
						frmk2DirectoriesCategoryItem.setTag(i);
						frmk2DirectoriesCategoryItem.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								try {
									((SmartActivity) getActivity()).loadNew(K2ItemsDetailsActivity.class, getActivity(), false, "IN_MENUID", IN_MENUID, "IN_ITEMS_LIST", categoryItemData, "IN_CURRENT_ITEM_SELECTED",
											Integer.parseInt(v.getTag().toString()));
								} catch (Throwable e) {
									e.printStackTrace();
								}
							}
						});

						holder.lnrk2DirectoriesScrollable.addView(itemView);
					}

				} else {
					holder.txtk2DirectoriesCategoryNoItem.setVisibility(View.VISIBLE);
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

}
