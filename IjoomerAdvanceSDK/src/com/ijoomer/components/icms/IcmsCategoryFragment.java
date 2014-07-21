package com.ijoomer.components.icms;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.icms.IcmsCategoryDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListenerWithCacheInfo;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

/**
 * Activity class for IcmsSingleCategory and IcmsCategory view
 * 
 * @author tasol
 * 
 */
public class IcmsCategoryFragment extends SmartFragment implements IcmsTagHolder {

	private ListView list;
	private View listFooter;

	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private ArrayList<String> ID_ARRAY;
	private HashMap<String, String> IN_PARENTCATEGORY;
	private IcmsCategoryDataProvider categoryDataProvider;
	private SmartListAdapterWithHolder listAdapterWithHolder;
	private AQuery androidAQuery;

	private JSONObject IN_OBJ;

	private int categoryCount;

	/**
	 * Overrides methods
	 */
	@Override
	public int setLayoutId() {
		return R.layout.icms_category_fragment;
	}

	@Override
	public void initComponents(View currentView) {
		categoryDataProvider = new IcmsCategoryDataProvider(getActivity());
		list = (ListView) currentView.findViewById(R.id.list);
		listFooter = LayoutInflater.from(getActivity()).inflate(R.layout.ijoomer_list_footer, null);
		list.addFooterView(listFooter, null, false);
		ID_ARRAY = new ArrayList<String>();
		androidAQuery = new AQuery(getActivity());

		getIntentData();
	}

	@Override
	public void prepareViews(View currentView) {
		((TextView) ((SmartActivity) getActivity()).getHeaderView().findViewById(R.id.txtHeader)).setText(((IjoomerSuperMaster) getActivity()).getScreenCaption());
		getCategories();
	}

	@Override
	public void setActionListeners(View currentView) {

		list.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 0) {

					if (!categoryDataProvider.isCalling() && categoryDataProvider.hasNextPage()) {
						listFooterVisible();
						categoryDataProvider.getCategories(IN_PARENTCATEGORY.get(CATEGORY_ID), new WebCallListenerWithCacheInfo() {

							@Override
							public void onProgressUpdate(int progressCount) {

							}

							@Override
							public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2, int pageNo,
									int pageLimit, boolean fromCache) {
								listFooterInvisible();
								if (responseCode == 200) {
									if (data1 != null && data1.size() > 0)
										prepareList(data1, true, fromCache, pageNo, pageLimit);

								}

							}
						});

					}
				}
			}
		});

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				@SuppressWarnings("unchecked")
				HashMap<String, String> value = (HashMap<String, String>) listAdapterWithHolder.getItem(arg2).getValues().get(0);
				if (value.containsKey(TOTALARTICLES)) {
					try {
						((IjoomerSuperMaster) getActivity()).setScreenCaption(value.get(TITLE));
						((SmartActivity) getActivity()).loadNew(IcmsCategoryActivity.class, getActivity(), false, "IN_PARENTCATEGORY", value);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					try {
						prepareIDS();
						((SmartActivity) getActivity()).loadNew(IcmsArticleDetailActivity.class, getActivity(), false, "IN_ARTICLE_INDEX", arg2 - (categoryCount) + "",
								"IN_ARTICLE_TITLE", value.get(TITLE), "IN_ARTICLE_ID_ARRAY", ID_ARRAY);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		});

	}

	@Override
	public View setLayoutView() {
		return null;
	}

	/**
	 * Class methods
	 */

	/**
	 * This method used to visible list footer
	 */
	public void listFooterVisible() {
		listFooter.setVisibility(View.VISIBLE);
	}

	/**
	 * This method used to gone list footer
	 */
	public void listFooterInvisible() {
		listFooter.setVisibility(View.GONE);
	}

	/**
	 * This method used to get intent data.
	 */
	@SuppressWarnings("unchecked")
	private void getIntentData() {

		try {
			IN_OBJ = new JSONObject(getActivity().getIntent().getStringExtra("IN_OBJ"));

			IN_PARENTCATEGORY = new HashMap<String, String>();
			IN_PARENTCATEGORY.put("title", getString(R.string.singlecategory));
			IN_PARENTCATEGORY.put("categoryid", new JSONObject(IN_OBJ.getString(ITEMDATA)).getString("id"));

		} catch (Exception e) {
			IN_PARENTCATEGORY = (HashMap<String, String>) getActivity().getIntent().getSerializableExtra("IN_PARENTCATEGORY");
		}

	}

	/**
	 * This method used to get categories list.
	 */
	private void getCategories() {
		final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.category_progress_title));

		categoryDataProvider.getCategories(IN_PARENTCATEGORY.get(CATEGORY_ID), new WebCallListenerWithCacheInfo() {

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

						prepareList((ArrayList<HashMap<String, String>>) data2, data1);
						listAdapterWithHolder = getListAdapter(listData);
						list.setAdapter(listAdapterWithHolder);
					} else {
						IjoomerUtilities.getCustomOkDialog(getString(R.string.categorylist),
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
	 * This method used to prepare article ids
	 */
	@SuppressWarnings("unchecked")
	public void prepareIDS() {

		ID_ARRAY.clear();
		for (int i = categoryCount; i < listData.size(); i++) {
			ID_ARRAY.add(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(ARTICLEID));
		}
	}

	/**
	 * This method used to prepare list article categories.
	 * 
	 * @param catData
	 *            represented categories data
	 * @param articleData
	 *            represented article data
	 */
	public void prepareList(ArrayList<HashMap<String, String>> catData, ArrayList<HashMap<String, String>> articleData) {

		listData.clear();
		ID_ARRAY.clear();

		if (catData != null && catData.size() > 0) {
			categoryCount = catData.size();
			for (int i = 0; i < catData.size(); i++) {

				if (i == (catData.size() - 1)) {
					SmartListItem catitem = new SmartListItem();
					catitem.setItemLayout(R.layout.icms_category_listitem);
					ArrayList<Object> catvalues = new ArrayList<Object>();
					HashMap<String, String> tmp = catData.get(i);
					tmp.put("shadow", "");
					catvalues.add(tmp);
					catitem.setValues(catvalues);
					listData.add(catitem);

				} else {
					SmartListItem catitem = new SmartListItem();
					catitem.setItemLayout(R.layout.icms_category_listitem);
					ArrayList<Object> catvalues = new ArrayList<Object>();
					catvalues.add(catData.get(i));
					catitem.setValues(catvalues);
					listData.add(catitem);
				}
			}

		}
		if (articleData != null && articleData.size() > 0) {

			for (int i = 0; i < articleData.size(); i++) {
				SmartListItem articleitem = new SmartListItem();
				articleitem.setItemLayout(R.layout.icms_category_listitem);
				ArrayList<Object> articlevalues = new ArrayList<Object>();
				articlevalues.add(articleData.get(i));
				articleitem.setValues(articlevalues);

				listData.add(articleitem);

			}
		}
	}

	/**
	 * This method used to prepare list categories.
	 * 
	 * @param data
	 *            represented categories data
	 * @param append
	 *            represented data append
	 * @param fromCache
	 *            represented data from cache
	 * @param pageno
	 *            represented page no
	 * @param pagelimit
	 *            represented page limit
	 */
	public void prepareList(ArrayList<HashMap<String, String>> data, boolean append, boolean fromCache, int pageno, int pagelimit) {
		if (data != null) {

			if (!fromCache) {
				int startIndex = ((pageno - 1) * pagelimit);
				int endIndex = listAdapterWithHolder.getCount();
				if (startIndex > 0) {
					startIndex += categoryCount;
					for (int i = endIndex; i >= startIndex; i--) {
						try {
							listAdapterWithHolder.remove(listAdapterWithHolder.getItem(i));
							listData.remove(i);
						} catch (Exception e) {
						}
					}
				}

			}

			for (HashMap<String, String> hashMap : data) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.icms_category_listitem);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hashMap);
				item.setValues(obj);
				listAdapterWithHolder.add(item);

			}

		}
	}

	/**
	 * List adapter for article categories.
	 * 
	 * @param listData
	 *            represented categories data
	 * @return represented {@link SmartListAdapterWithHolder}
	 */
	public SmartListAdapterWithHolder getListAdapter(final ArrayList<SmartListItem> listData) {

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.icms_category_listitem, listData, new ItemView() {

			@Override
			public View setItemView(int position, View v, SmartListItem item) {

				return null;
			}

			@Override
			public View setItemView(int position, View v, SmartListItem item, ViewHolder holder) {
				holder.icmsTxtTitle = (IjoomerTextView) v.findViewById(R.id.icmsTxtTitle);
				holder.icmsTxtIntro = (IjoomerTextView) v.findViewById(R.id.icmsTxtIntro);
				holder.icmsImageThumb = (ImageView) v.findViewById(R.id.icmsImageThumb);
				holder.icmsCatImageThumb = (ImageView) v.findViewById(R.id.icmsCatImageThumb);
				holder.icmsCatTxtArticlesCount = (IjoomerTextView) v.findViewById(R.id.icmsCatTxtArticlesCount);
				holder.icmsCatTxtTitle = (IjoomerTextView) v.findViewById(R.id.icmsCatTxtTitle);
				holder.icmsLnrCatListItem = (LinearLayout) v.findViewById(R.id.icmsLnrCatListItem);
				holder.icmsLnrArtListItem = (LinearLayout) v.findViewById(R.id.icmsLnrArtListItem);
				holder.icmsCatArticleSeparator = (ImageView) v.findViewById(R.id.icmsCatArticleSeparator);
				holder.icmsCatDivider = (ImageView) v.findViewById(R.id.icmsCatDivider);
				holder.icmsCatArticleSeparator.setVisibility(View.GONE);
				@SuppressWarnings("unchecked")
				HashMap<String, String> value = (HashMap<String, String>) item.getValues().get(0);

				if (value.containsKey(TOTALARTICLES)) {

					/*
					 * Sub catagory
					 */
					holder.icmsLnrCatListItem.setVisibility(View.VISIBLE);
					holder.icmsLnrArtListItem.setVisibility(View.GONE);

					androidAQuery.id(holder.icmsCatImageThumb).image(value.get(IMAGE), true, true, ((SmartActivity) getActivity()).getDeviceWidth(), R.drawable.icms_default);

					try {
						holder.icmsCatTxtTitle.setText(value.get(TITLE));
						holder.icmsCatTxtArticlesCount.setText("(" + value.get(TOTALARTICLES) + ")");
					} catch (Exception e) {

					}
					if (value.containsKey("shadow")) {
						holder.icmsCatArticleSeparator.setVisibility(View.VISIBLE);
						holder.icmsCatDivider.setVisibility(View.GONE);
					} else {
						holder.icmsCatArticleSeparator.setVisibility(View.GONE);
						holder.icmsCatDivider.setVisibility(View.VISIBLE);
					}

				} else {
					/*
					 * article
					 */
					holder.icmsLnrCatListItem.setVisibility(View.GONE);
					holder.icmsLnrArtListItem.setVisibility(View.VISIBLE);

					try {
						holder.icmsTxtTitle.setText(value.get(TITLE));
						holder.icmsTxtIntro.setText(Html.fromHtml(value.get(INTROTEXT)).toString().trim());
					} catch (Exception e) {

					}

					androidAQuery.id(holder.icmsImageThumb).image(value.get(IMAGE), true, true, ((SmartActivity) getActivity()).getDeviceWidth(), R.drawable.icms_default);

				}

				return v;
			}

		});
		return adapterWithHolder;
	}

}