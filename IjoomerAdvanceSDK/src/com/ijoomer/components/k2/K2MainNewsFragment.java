package com.ijoomer.components.k2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.IjoomerHorizontalAutoScroller;
import com.ijoomer.customviews.IjoomerHorizontalAutoScroller.ItemClickListener;
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
 * This Fragment Contains All Method Related To K2MainNewsFragment.
 * 
 * @author tasol
 * 
 */
public class K2MainNewsFragment extends SmartFragment implements K2TagHolder {

	private ListView listView;
	private IjoomerHorizontalAutoScroller hasLeadItems;
	private SeekBar proSeekBar;
	private ProgressBar pbrNews;

	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private ArrayList<HashMap<String, String>> catItems = new ArrayList<HashMap<String, String>>();

	private ArrayList<HashMap<String, String>> allItems = new ArrayList<HashMap<String, String>>();
	private ArrayList<HashMap<String, String>> leadItems = new ArrayList<HashMap<String, String>>();
	private k2MainDataProvider provider;
	private SmartListAdapterWithHolder adapter;
	private AQuery androidQuery;

	private String IN_LEADLIMIT;
	private String IN_MENUID;
	private String ITEMID = "itemid";

	public K2MainNewsFragment() {
	}

	/**
	 * Overrides method
	 */

	@Override
	public int setLayoutId() {
		return R.layout.k2_main_news;
	}

	@Override
	public void initComponents(View currentView) {
		listView = (ListView) currentView.findViewById(R.id.listView);
		pbrNews = (ProgressBar) currentView.findViewById(R.id.pbrNews);
		hasLeadItems = (IjoomerHorizontalAutoScroller) currentView.findViewById(R.id.hasLeadItems);
		hasLeadItems.setScrollDuration(60);

		androidQuery = new AQuery(getActivity());
		provider = new k2MainDataProvider(getActivity());
	}

	@Override
	public void prepareViews(View currentView) {
		listView.setAdapter(null);
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

		listView.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				if (((HashMap<String, String>) listData.get(pos).getValues().get(0)).containsKey("isCat")) {
					pbrNews.setVisibility(View.VISIBLE);
					provider.getItemById(((HashMap<String, String>) listData.get(pos).getValues().get(0)).get(ID), IN_MENUID, new CacheCallListener() {

						@Override
						public void onProgressUpdate(int progressCount) {
						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							pbrNews.setVisibility(View.GONE);
							try {
								if (responseCode == 200) {
									try {
										((SmartActivity) getActivity()).loadNew(K2NewsDetailsActivity.class, getActivity(), false, "IN_MENUID", IN_MENUID, "IN_ITEMS_LIST", data1, "IN_CURRENT_ITEM_SELECTED", 0);
									} catch (Throwable e) {
										e.printStackTrace();
									}
								} else {
									IjoomerUtilities.getCustomOkDialog(getActivity().getString(R.string.dialog_k2_news), getActivity().getString(R.string.k2_no_items), getActivity().getString(R.string.ok), R.layout.ijoomer_ok_dialog,
											new CustomAlertNeutral() {

												@Override
												public void NeutralMethod() {
												}
											});
								}
							} catch (Throwable e) {
							}
						}
					});

				} else {
					try {
						((SmartActivity) getActivity()).loadNew(K2NewsDetailsActivity.class, getActivity(), false, "IN_MENUID", IN_MENUID, "IN_ITEMS_LIST", allItems, "IN_CURRENT_ITEM_SELECTED", pos - catItems.size());
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			}
		});

		hasLeadItems.setItemClickListener(new ItemClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(View v, int position) {
				try {
					((SmartActivity) getActivity()).loadNew(K2NewsDetailsActivity.class, getActivity(), false, "IN_MENUID", IN_MENUID, "IN_ITEMS_LIST", ((ArrayList<HashMap<String, String>>) hasLeadItems.getTag()), "IN_CURRENT_ITEM_SELECTED",
							position);
				} catch (Throwable e) {
					e.printStackTrace();
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

	/**
	 * This method used update fragment.
	 */
	public void updateFragment() {
		getIntentData();
		prepareCatgoryList(true);
	}

	/**
	 * This method used to get intent data.
	 */
	private void getIntentData() {
		try {
			IN_LEADLIMIT = getActivity().getIntent().getStringExtra("IN_OBJ") == null ? "0" : ((JSONObject) new JSONObject(getActivity().getIntent().getStringExtra("IN_OBJ")).getJSONObject("itemdata")).getString(LEADLIMIT);
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
						if (provider.hasNextPage()) {
							getDataFromServer(isProgressShow);
						} else {
							prepareCatgoryList(false);
						}
					} else {
						pbrNews.setVisibility(View.GONE);
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
		IjoomerUtilities.getCustomOkDialog(getActivity().getString(R.string.dialog_k2_news), getActivity().getString(getResources().getIdentifier("code" + responseCode, "string", getActivity().getPackageName())),
				getActivity().getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

					@Override
					public void NeutralMethod() {
						
					}
				});
	}

	/**
	 * This method used to prepare list category.
	 * 
	 * @param isProgressShow
	 *            represented isProgressShow
	 */
	private void prepareCatgoryList(final boolean isProgressShow) {
		if (isProgressShow) {
			proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
		}
		pbrNews.setVisibility(View.VISIBLE);
		provider.getMainCategoryById("0", IN_MENUID, new CacheCallListener() {

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
						catItems = data1;
						prepareItemsList();
						if (provider.hasNextPage()) {
							getDataFromServer(false);
						}
					} else {
						getDataFromServer(isProgressShow);
					}

				} catch (Throwable e) {

				}

			}
		});
	}

	/**
	 * This method used to prepare list items.
	 */
	private void prepareItemsList() {

		provider.getItemById("0", IN_MENUID, new CacheCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				try {
					listData.clear();
					if (catItems != null) {
						for (int i = 0; i < catItems.size(); i++) {
							SmartListItem item = new SmartListItem();
							item.setItemLayout(R.layout.k2_main_news_list_item);
							ArrayList<Object> obj = new ArrayList<Object>();
							catItems.get(i).put("isCat", "");
							obj.add(catItems.get(i));
							item.setValues(obj);
							listData.add(item);
						}
					}

					try {
						hasLeadItems.removeAllViews();
						leadItems.clear();
					} catch (Throwable e) {
					}

					int totalItems = data1.size();
					allItems.clear();
					allItems.addAll(data1);
					for (int i = 0; i < totalItems; i++) {
						SmartListItem item = new SmartListItem();
						item.setItemLayout(R.layout.k2_main_news_list_item);
						ArrayList<Object> obj = new ArrayList<Object>();
						obj.add(data1.get(i));
						item.setValues(obj);
						listData.add(item);
						if (Integer.parseInt(IN_LEADLIMIT) > i) {
							leadItems.add(data1.get(i));
							ViewGroup view = (ViewGroup) LayoutInflater.from(getActivity()).inflate(R.layout.k2_main_news_header_lead_item, null);
							IjoomerTextView txtk2NewsLeadItemName = (IjoomerTextView) view.findViewById(R.id.txtk2NewsLeadItemName);
							txtk2NewsLeadItemName.setText(data1.get(i).get(TITLE));
							hasLeadItems.addItem(view);
						}
					}
					hasLeadItems.setTag(leadItems);
					hasLeadItems.startAutoScrolling();
					if (adapter == null) {
						adapter = getListAdapter();
						listView.setAdapter(adapter);
					} else {
						adapter.notifyDataSetChanged();
					}
				} catch (Throwable e) {

				}
				if (!provider.hasNextPage()) {
					pbrNews.setVisibility(View.GONE);
				}
			}
		});
	}

	/**
	 * List adapter for items.
	 * 
	 * @return
	 */
	private SmartListAdapterWithHolder getListAdapter() {
		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.k2_main_news_list_item, listData, new ItemView() {
			@SuppressWarnings("unchecked")
			@Override
			public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {

				final HashMap<String, String> itemData = (HashMap<String, String>) item.getValues().get(0);
				holder.lnrK2NewsListItem = (LinearLayout) v.findViewById(R.id.lnrK2NewsListItem);
				holder.lnrCategoryHeader = (LinearLayout) v.findViewById(R.id.lnrCategoryHeader);

				if (!itemData.containsKey("isCat")) {
					holder.lnrCategoryHeader.setVisibility(View.GONE);
					holder.lnrK2NewsListItem.setVisibility(View.VISIBLE);
					holder.imgk2NewsCategoryItem = (ImageView) v.findViewById(R.id.imgk2NewsCategoryItem);
					holder.txtk2NewsCategoryItem = (IjoomerTextView) v.findViewById(R.id.txtk2NewsCategoryItem);
					holder.txtk2NewsCategoryDescription = (IjoomerTextView) v.findViewById(R.id.txtk2NewsCategoryDescription);

					androidQuery.id(holder.imgk2NewsCategoryItem).image(itemData.get(IMAGESMALL), true, true, 130, R.drawable.k2_default);
					holder.txtk2NewsCategoryItem.setText(itemData.get(TITLE));
					holder.txtk2NewsCategoryDescription.setText(itemData.get(INTROTEXT));

				} else {
					holder.lnrCategoryHeader.setVisibility(View.VISIBLE);
					holder.lnrK2NewsListItem.setVisibility(View.GONE);
					holder.txtk2NewsCategoryName = (IjoomerTextView) v.findViewById(R.id.txtk2NewsCategoryName);
					holder.txtk2NewsCategoryName.setText(itemData.get(NAME));
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
