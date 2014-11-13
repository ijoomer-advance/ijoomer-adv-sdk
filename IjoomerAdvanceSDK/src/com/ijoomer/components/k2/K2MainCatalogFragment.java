package com.ijoomer.components.k2;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.CoverFlow;
import com.ijoomer.customviews.IjoomerButton;
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

/**
 * This Fragment Contains All Method Related To K2MainCatalogFragment.
 * 
 * @author tasol
 * 
 */
public class K2MainCatalogFragment extends SmartFragment implements K2TagHolder {

	private ListView listView;
	private IjoomerTextView txtk2CatalogCategoryNoItem;
	private IjoomerTextView txtk2CatalogCategoryName;
	private SeekBar proSeekBar;
	private CoverFlow cfCatalog;
	private ViewGroup catalogHeader;

	private ArrayList<SmartListItem> listDataCategories = new ArrayList<SmartListItem>();
	private ArrayList<SmartListItem> listDataItems = new ArrayList<SmartListItem>();
	private ArrayList<HashMap<String, String>> allItems = new ArrayList<HashMap<String, String>>();
	private SmartListAdapterWithHolder categoriesAdapter;
	private k2MainDataProvider provider;
	private AQuery androidQuery;

	private String IN_MENUID;
	private String ITEMID = "itemid";

	public K2MainCatalogFragment() {
	}

	/**
	 * Overrides method
	 */

	@Override
	public int setLayoutId() {
		return R.layout.k2_main_catalog;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initComponents(View currentView) {
		listView = (ListView) currentView.findViewById(R.id.listView);
		txtk2CatalogCategoryNoItem = (IjoomerTextView) currentView.findViewById(R.id.txtk2CatalogCategoryNoItem);
		txtk2CatalogCategoryName = (IjoomerTextView) currentView.findViewById(R.id.txtk2CatalogCategoryName);

		catalogHeader = (ViewGroup) LayoutInflater.from(getActivity()).inflate(R.layout.k2_catalog_coverflow_header, null);
		cfCatalog = (CoverFlow) catalogHeader.findViewById(R.id.cfCatalog);
		cfCatalog.setSpacing(((SmartActivity) getActivity()).convertSizeToDeviceDependent(-50));
		listView.addHeaderView(catalogHeader, null, false);
		androidQuery = new AQuery(getActivity());
		provider = new k2MainDataProvider(getActivity());
	}

	@Override
	public void prepareViews(View currentView) {
		txtk2CatalogCategoryName.setVisibility(View.VISIBLE);
		listView.setAdapter(null);
		updateFragment();
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void setActionListeners(View currentView) {

		cfCatalog.setOnItemSelectedListener(new OnItemSelectedListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onItemSelected(AdapterView<?> adapter, View v, final int pos, long arg3) {
				txtk2CatalogCategoryName.setText(((HashMap<String, String>) listDataCategories.get(pos).getValues().get(0)).get(NAME));
				prepareCatalogItemsList(((HashMap<String, String>) listDataCategories.get(pos).getValues().get(0)).get(ID), pos);
			};

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
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
	 * This method used to update fragment.
	 */
	public void updateFragment() {
		getIntentData();
		prepareCatalogCategoryList(true);
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
						prepareCatalogCategoryList(false);
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
		IjoomerUtilities.getCustomOkDialog(getActivity().getString(R.string.dialog_k2_catalog),
				getActivity().getString(getResources().getIdentifier("code" + responseCode, "string", getActivity().getPackageName())), getActivity().getString(R.string.ok),
				R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

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
	private void prepareCatalogCategoryList(final boolean isProgressShow) {
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

			@SuppressWarnings({ "deprecation", "unchecked" })
			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				try {
					if (responseCode == 200) {
						listDataCategories.clear();
						for (HashMap<String, String> category : data1) {
							SmartListItem item = new SmartListItem();
							item.setItemLayout(R.layout.k2_catalog_item);
							ArrayList<Object> obj = new ArrayList<Object>();
							obj.add(category);
							item.setValues(obj);
							listDataCategories.add(item);
						}
						if (categoriesAdapter == null) {
							categoriesAdapter = getListAdapterCategory();
							cfCatalog.setAdapter(categoriesAdapter);
							cfCatalog.setSelection(0, true);
							cfCatalog.setAnimationDuration(1000);
							txtk2CatalogCategoryName.setText(data1.get(0).get(NAME));
							prepareCatalogItemsList(((HashMap<String, String>) listDataCategories.get(0).getValues().get(0)).get(ID), 0);
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
	 * This method used to prepare list category item.
	 * 
	 * @param catid
	 *            represented category id
	 * @param currentSelectedItem
	 *            represented current selected category index
	 */
	private void prepareCatalogItemsList(String catid, final int currentSelectedItem) {
		provider.getItemByName(catid, IN_MENUID, new CacheCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				try {
					if (responseCode == 200) {
						if (cfCatalog.getSelectedItemPosition() == currentSelectedItem) {
							txtk2CatalogCategoryNoItem.setVisibility(View.GONE);
							listDataItems.clear();
							allItems.clear();
							for (HashMap<String, String> categotyItem : data1) {
								SmartListItem item = new SmartListItem();
								item.setItemLayout(R.layout.k2_main_catalog_items_list_item);
								ArrayList<Object> obj = new ArrayList<Object>();
								obj.add(categotyItem);
								item.setValues(obj);
								listDataItems.add(item);
							}
							allItems.addAll(data1);
							listView.setAdapter(getListAdapterItems());
						} else {
							listView.setAdapter(null);
							txtk2CatalogCategoryNoItem.setVisibility(View.VISIBLE);
						}
					} else {
						listView.setAdapter(null);
						txtk2CatalogCategoryNoItem.setVisibility(View.VISIBLE);
					}
				} catch (Throwable e) {

				}
			}
		});

	}

	/**
	 * List adapter for category.
	 * 
	 * @return represented {@link SmartListAdapterWithHolder}
	 */
	private SmartListAdapterWithHolder getListAdapterCategory() {
		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.k2_catalog_item, listDataCategories, new ItemView() {
			@SuppressWarnings({ "unchecked", "deprecation" })
			@Override
			public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {

				holder.imgk2CatalogCategory = (ImageView) v.findViewById(R.id.imgk2CatalogCategory);

				final HashMap<String, String> categoryData = (HashMap<String, String>) item.getValues().get(0);

				androidQuery.id(holder.imgk2CatalogCategory).image(categoryData.get(IMAGE), true, true, 300, R.drawable.k2_default);

				v.setLayoutParams(new CoverFlow.LayoutParams(((SmartActivity) getActivity()).convertSizeToDeviceDependent(180), ((SmartActivity) getActivity())
						.convertSizeToDeviceDependent(180)));

				v.setPadding(1, 7, 1, 7);
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
	 * List adapter for category items.
	 * 
	 * @return represented {@link SmartListAdapterWithHolder}
	 */
	private SmartListAdapterWithHolder getListAdapterItems() {
		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.k2_main_catalog_items_list_item, listDataItems, new ItemView() {
			@SuppressWarnings("unchecked")
			@Override
			public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {

				holder.lnrK2ListItem = (LinearLayout) v.findViewById(R.id.lnrK2ListItem);
				holder.imgk2CatalogCategoryItem = (ImageView) v.findViewById(R.id.imgk2CatalogCategoryItem);
				holder.txtk2CatalogCategoryItem = (IjoomerTextView) v.findViewById(R.id.txtk2CatalogCategoryItem);
				holder.btnk2CatalogCategoryItemPrice = (IjoomerButton) v.findViewById(R.id.btnk2CatalogCategoryItemPrice);
				holder.btnk2CatalogCategoryItemPrice.setVisibility(View.GONE);

				final HashMap<String, String> itemData = (HashMap<String, String>) item.getValues().get(0);
				androidQuery.id(holder.imgk2CatalogCategoryItem).image(itemData.get(IMAGESMALL), true, true, 130, R.drawable.k2_default);
				holder.txtk2CatalogCategoryItem.setText(itemData.get(TITLE));

				if (position % 2 == 0) {
					holder.lnrK2ListItem.setBackgroundColor(getResources().getColor(R.color.k2_bg_color));
				} else {
					holder.lnrK2ListItem.setBackgroundColor(getResources().getColor(R.color.k2_catalog_item_dark));
				}

				try {
					JSONArray extraFieldsArray = new JSONArray(itemData.get(EXTRAFIELDS));
					if (extraFieldsArray.length() > 0) {
						for (int i = 0; i < extraFieldsArray.length(); i++) {
							JSONObject fieldJson = (JSONObject) extraFieldsArray.get(i);
							try {
								if (fieldJson.getString(NAME).equals(PRICE) && fieldJson.getString(VALUE).trim().length() > 0) {
									holder.btnk2CatalogCategoryItemPrice.setText("$ " + fieldJson.getString(VALUE));
									holder.btnk2CatalogCategoryItemPrice.setVisibility(View.VISIBLE);
								}
							} catch (Throwable e1) {
								e1.printStackTrace();
							}
						}
					}
				} catch (Throwable e) {
				}

				holder.lnrK2ListItem.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						try {
							((SmartActivity) getActivity()).loadNew(K2CatalogDetailsActivity.class, getActivity(), false, "IN_MENUID", IN_MENUID, "IN_ITEMS_LIST", allItems,
									"IN_CURRENT_ITEM_SELECTED", position);
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
