package com.ijoomer.components.k2;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.IjoomerGridView;
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
import java.util.Stack;

/**
 * This Fragment Contains All Method Related To K2MainSingleFragment.
 * 
 * @author tasol
 * 
 */
@SuppressLint("NewApi")
public class K2MainSingleFragment extends SmartFragment implements K2TagHolder {

	public LinearLayout lnrk2SingleCategory;
	private ListView listView;
	private IjoomerTextView txtk2CatalogCategoryNoItem;
	private IjoomerGridView gridView;
	private ViewGroup header;
	private ViewGroup footer;
	private SeekBar proSeekBar;
	private ProgressBar pbrSingleCategory;

	private ArrayList<SmartListItem> listDataItems = new ArrayList<SmartListItem>();
	private ArrayList<HashMap<String, String>> allItems = new ArrayList<HashMap<String, String>>();
	public Stack<String> categoryStack = new Stack<String>();
	private SmartListAdapterWithHolder adapter;
	private k2MainDataProvider provider;
	private AQuery androidQuery;

	private String IN_MENUID;
	private String ITEMID = "itemid";

	public K2MainSingleFragment() {
	}

	/**
	 * Overrides method
	 */

	@Override
	public int setLayoutId() {
		return R.layout.k2_main_catalog;
	}

	@Override
	public void initComponents(View currentView) {
		header = (ViewGroup) LayoutInflater.from(getActivity()).inflate(R.layout.k2_main_single_header, null);
		footer = (ViewGroup) LayoutInflater.from(getActivity()).inflate(R.layout.k2_main_grid, null);
		listView = (ListView) currentView.findViewById(R.id.listView);
		pbrSingleCategory = (ProgressBar) currentView.findViewById(R.id.pbrSingleCategory);
		listView.setDividerHeight(0);
		lnrk2SingleCategory = (LinearLayout) header.findViewById(R.id.lnrk2SingleCategory);
		gridView = (IjoomerGridView) footer.findViewById(R.id.gridView);
		txtk2CatalogCategoryNoItem = (IjoomerTextView) currentView.findViewById(R.id.txtk2CatalogCategoryNoItem);
		listView.addHeaderView(header);
		listView.addFooterView(footer);
		listView.setAdapter(null);

		androidQuery = new AQuery(getActivity());
		provider = new k2MainDataProvider(getActivity());
	}

	@Override
	public void prepareViews(View currentView) {
		gridView.setExpandFully(true);
		listView.setAdapter(null);
		updateFragment(true, "0", true);
	}

	@Override
	public View setLayoutView() {
		return null;
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

	public void updateFragment(boolean isShowProgress, String categoryId, boolean isWebCall) {
		getIntentData();
		prepareCategoryList(isShowProgress, categoryId, isWebCall);
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
	 * @param categoryId
	 *            represented category id
	 */
	private void getDataFromServer(final boolean isProgressShow, final String categoryId) {

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
					lnrk2SingleCategory.removeAllViews();
					if (responseCode == 200) {
						if (provider.hasNextPage()) {
							getDataFromServer(isProgressShow, categoryId);
						} else {
							lnrk2SingleCategory.removeAllViews();
							prepareCategoryList(false, categoryId, false);
						}
					} else {
						pbrSingleCategory.setVisibility(View.GONE);
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
		IjoomerUtilities.getCustomOkDialog(getActivity().getString(R.string.dialog_k2_single_category), getActivity().getString(getResources().getIdentifier("code" + responseCode, "string", getActivity().getPackageName())),
				getActivity().getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

					@Override
					public void NeutralMethod() {
						
					}
				});
	}

	/**
	 * This method used to prepare category list.
	 * 
	 * @param isProgressShow
	 *            represented isProgressShow
	 * @param categoryId
	 *            represented category id
	 * @param isWebcall
	 *            represented isWebCall
	 */
	private void prepareCategoryList(final boolean isProgressShow, final String categoryId, final boolean isWebcall) {
		if (isProgressShow) {
			proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
		}
		pbrSingleCategory.setVisibility(View.VISIBLE);

		if (categoryId.equals("0")) {
			provider.getMainCategoryByName(categoryId, IN_MENUID, new CacheCallListener() {

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
							for (final HashMap<String, String> category : data1) {
								ViewGroup view = (ViewGroup) LayoutInflater.from(getActivity()).inflate(R.layout.k2_main_news_header_category_item, null);
								IjoomerTextView txtk2NewsCategoryName = (IjoomerTextView) view.findViewById(R.id.txtk2NewsCategoryName);
								txtk2NewsCategoryName.setText(category.get(NAME));
								view.setBackgroundColor(getResources().getColor(R.color.k2_orange));
								if (lnrk2SingleCategory.getChildCount() <= 0) {
									lnrk2SingleCategory.addView(view);
								}
								provider.getSubCategoryByName(category.get(ID), IN_MENUID, new CacheCallListener() {

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
												for (HashMap<String, String> subCategory : data1) {
													ViewGroup view = (ViewGroup) LayoutInflater.from(getActivity()).inflate(R.layout.k2_main_news_header_category_item, null);
													IjoomerTextView txtk2NewsCategoryName = (IjoomerTextView) view.findViewById(R.id.txtk2NewsCategoryName);
													txtk2NewsCategoryName.setText(subCategory.get(NAME));
													txtk2NewsCategoryName.setTextColor(getActivity().getResources().getColor(R.color.k2_text_color));
													view.setTag(subCategory);
													view.setOnClickListener(new OnClickListener() {

														@SuppressWarnings("unchecked")
														@Override
														public void onClick(final View v) {
															categoryStack.push(categoryId);
															lnrk2SingleCategory.removeAllViews();
															updateFragment(false, ((HashMap<String, String>) v.getTag()).get(ID), false);
														}
													});
													if (lnrk2SingleCategory.getChildCount() <= data1.size()) {
														lnrk2SingleCategory.addView(view);
													}
												}
											}

											provider.getItemByName(category.get(ID), IN_MENUID, new CacheCallListener() {

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
														if (responseCode == 200) {
															allItems.addAll(data1);
															for (HashMap<String, String> items : data1) {
																SmartListItem item = new SmartListItem();
																item.setItemLayout(R.layout.k2_main_grid_item);
																ArrayList<Object> obj = new ArrayList<Object>();
																obj.add(items);
																item.setValues(obj);
																listDataItems.add(item);
															}
															adapter = getListAdapterItems();
															gridView.setAdapter(adapter);
                                                            gridView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
															txtk2CatalogCategoryNoItem.setVisibility(View.GONE);
														} else {
															gridView.setAdapter(null);
															gridView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
															txtk2CatalogCategoryNoItem.setVisibility(View.VISIBLE);
														}
														if (!provider.hasNextPage()) {
															pbrSingleCategory.setVisibility(View.GONE);
														}
													} catch (Throwable e) {

													}

												}
											});
										} catch (Throwable e) {

										}
									}

								});
							}

						}

						if (isWebcall) {
							getDataFromServer(isProgressShow, categoryId);
						}
					} catch (Throwable e) {

					}
				}
			});
		} else {
			provider.getCategoryDetail(categoryId, IN_MENUID, new CacheCallListener() {

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
							for (final HashMap<String, String> category : data1) {
								ViewGroup view = (ViewGroup) LayoutInflater.from(getActivity()).inflate(R.layout.k2_main_news_header_category_item, null);
								IjoomerTextView txtk2NewsCategoryName = (IjoomerTextView) view.findViewById(R.id.txtk2NewsCategoryName);
								txtk2NewsCategoryName.setText(category.get(NAME));
								view.setBackgroundColor(getResources().getColor(R.color.k2_orange));
								lnrk2SingleCategory.addView(view);
								provider.getSubCategoryByName(category.get(ID), IN_MENUID, new CacheCallListener() {

									@Override
									public void onProgressUpdate(int progressCount) {
									}

									@Override
									public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
										if (responseCode == 200) {
											for (HashMap<String, String> subCategory : data1) {
												ViewGroup view = (ViewGroup) LayoutInflater.from(getActivity()).inflate(R.layout.k2_main_news_header_category_item, null);
												IjoomerTextView txtk2NewsCategoryName = (IjoomerTextView) view.findViewById(R.id.txtk2NewsCategoryName);
												txtk2NewsCategoryName.setText(subCategory.get(NAME));
												txtk2NewsCategoryName.setTextColor(getActivity().getResources().getColor(R.color.k2_text_color));
												view.setTag(subCategory);
												view.setOnClickListener(new OnClickListener() {

													@SuppressWarnings("unchecked")
													@Override
													public void onClick(final View v) {
														categoryStack.push(categoryId);
														lnrk2SingleCategory.removeAllViews();
														updateFragment(false, ((HashMap<String, String>) v.getTag()).get(ID), false);
													}
												});
												lnrk2SingleCategory.addView(view);
											}
										}
										provider.getItemByName(category.get(ID), IN_MENUID, new CacheCallListener() {

											@Override
											public void onProgressUpdate(int progressCount) {
											}

											@Override
											public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
												listDataItems.clear();
												allItems.clear();
												if (responseCode == 200) {
													allItems.addAll(data1);
													for (HashMap<String, String> items : data1) {
														SmartListItem item = new SmartListItem();
														item.setItemLayout(R.layout.k2_main_grid_item);
														ArrayList<Object> obj = new ArrayList<Object>();
														obj.add(items);
														item.setValues(obj);
														listDataItems.add(item);
													}
													txtk2CatalogCategoryNoItem.setVisibility(View.GONE);
													adapter = getListAdapterItems();
													gridView.setAdapter(adapter);
                                                    gridView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

												} else {
													gridView.setAdapter(null);
													gridView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
													txtk2CatalogCategoryNoItem.setVisibility(View.VISIBLE);
												}
												if (!provider.hasNextPage()) {
													pbrSingleCategory.setVisibility(View.GONE);
												}
											}
										});
									}
								});
							}

						}
						if (isWebcall) {
							getDataFromServer(isProgressShow, categoryId);
						}
					} catch (Throwable e) {

					}
				}
			});
		}
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
