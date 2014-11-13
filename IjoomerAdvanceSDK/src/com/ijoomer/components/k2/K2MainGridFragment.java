package com.ijoomer.components.k2;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
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
 * This Fragment Contains All Method Related To K2MainGridFragment.
 * 
 * @author tasol
 * 
 */
public class K2MainGridFragment extends SmartFragment implements K2TagHolder {

	private GridView gridView;
	private ProgressBar pbrItemGrid;
	private SeekBar proSeekBar;

	private ArrayList<SmartListItem> listDataItems = new ArrayList<SmartListItem>();
	private ArrayList<HashMap<String, String>> allItems = new ArrayList<HashMap<String, String>>();
	private k2MainDataProvider provider;
	private SmartListAdapterWithHolder adapter;
	private AQuery androidQuery;

	private String IN_MENUID;
	private String ITEMID = "itemid";

	public K2MainGridFragment() {
	}

	/**
	 * Overrides method
	 */

	@Override
	public int setLayoutId() {
		return R.layout.k2_main_grid;
	}

	@Override
	public void initComponents(View currentView) {
		gridView = (GridView) currentView.findViewById(R.id.gridView);
		pbrItemGrid = (ProgressBar) currentView.findViewById(R.id.pbrItemGrid);

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
		gridView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
			}

			@Override
			public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 1) {
					if (!provider.isCalling() && provider.hasNextPage()) {
						pbrItemGrid.setVisibility(View.VISIBLE);
						getDataFromServer(false);
					}
				}

			}
		});

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
		prepareCatalogItemsList(true);
	}

	/**
	 * This method used to get intent data.
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
						prepareCatalogItemsList(false);
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
		IjoomerUtilities.getCustomOkDialog(getActivity().getString(R.string.dialog_k2_scrolling_grid), getActivity().getString(getResources().getIdentifier("code" + responseCode, "string", getActivity().getPackageName())),
				getActivity().getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

					@Override
					public void NeutralMethod() {
						
					}
				});
	}

	/**
	 * This method used to prepare list category items.
	 * 
	 * @param isProgressShow
	 *            represented isProgressShow
	 */
	private void prepareCatalogItemsList(final boolean isProgressShow) {
		if (isProgressShow) {
			proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
		}

		provider.getItemByName("0", IN_MENUID, new CacheCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {
				if (isProgressShow) {
					proSeekBar.setProgress(progressCount);
				}
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				try {
					listDataItems.clear();
					allItems.clear();
					if (pbrItemGrid.getVisibility() == View.VISIBLE) {
						pbrItemGrid.setVisibility(View.GONE);
					}
					if (responseCode == 200) {
						allItems.addAll(data1);
						int totalItems = data1.size();
						for (int i = 0; i < totalItems; i++) {
							SmartListItem item = new SmartListItem();
							item.setItemLayout(R.layout.k2_main_grid_item);
							ArrayList<Object> obj = new ArrayList<Object>();
							obj.add(data1.get(i));
							item.setValues(obj);
							listDataItems.add(item);
						}
						if (adapter == null) {
							adapter = getListAdapterItems();
							gridView.setAdapter(adapter);
							getDataFromServer(false);
						} else {
							adapter.notifyDataSetChanged();
						}
					} else {
						getDataFromServer(true);
					}
				} catch (Throwable e) {

				}
			}
		});

	}

	/**
	 * List adapter for items.
	 * 
	 * @return represented {@link SmartListAdapterWithHolder}
	 */
	private SmartListAdapterWithHolder getListAdapterItems() {
		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.k2_main_grid_item, listDataItems, new ItemView() {
			@SuppressWarnings("unchecked")
			@Override
			public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {

				holder.lnrK2Grid = (LinearLayout) v.findViewById(R.id.lnrK2Grid);
				holder.imgk2GridCategoryItem = (ImageView) v.findViewById(R.id.imgk2GridCategoryItem);
				holder.txtk2GridCategoryItemName = (IjoomerTextView) v.findViewById(R.id.txtk2GridCategoryItemName);

				final HashMap<String, String> itemData = (HashMap<String, String>) item.getValues().get(0);
				androidQuery.id(holder.imgk2GridCategoryItem).image(itemData.get(IMAGESMALL), true, true, 130, R.drawable.k2_default);
				holder.txtk2GridCategoryItemName.setText(itemData.get(TITLE));

				holder.lnrK2Grid.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							((SmartActivity) getActivity()).loadNew(K2ItemsDetailsActivity.class, getActivity(), false, "IN_MENUID", IN_MENUID, "IN_ITEMS_LIST", allItems, "IN_CURRENT_ITEM_SELECTED", position);
						} catch (Throwable e) {
							e.printStackTrace();
						}
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

}
