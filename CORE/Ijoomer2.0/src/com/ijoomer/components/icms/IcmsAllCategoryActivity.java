package com.ijoomer.components.icms;

import java.util.ArrayList;
import java.util.HashMap;

import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.icms.IcmsCategoryDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

/**
 * Activity class for IcmsAllCategory view
 * 
 * @author tasol
 * 
 */
public class IcmsAllCategoryActivity extends IcmsMasterActivity {

	private ListView list;

	private AQuery androidAQuery;
	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private ArrayList<String> ID_ARRAY;
	private HashMap<String, String> IN_PARENTCATEGORY;
	private SmartListAdapterWithHolder listAdapterWithHolder;

	private IcmsCategoryDataProvider categoryDataProvider = new IcmsCategoryDataProvider(this);

	private int categoryCount;

	@Override
	public int setLayoutId() {
		return R.layout.icms_category;
	}

	@Override
	public void initComponents() {

		list = (ListView) findViewById(R.id.list);

		ID_ARRAY = new ArrayList<String>();
		androidAQuery = new AQuery(this);

		getIntentData();

	}

	@Override
	public void prepareViews() {
		((TextView) getHeaderView().findViewById(R.id.txtHeader)).setText(getScreenCaption());
		getCategories();

	}

	@Override
	public void setActionListeners() {

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub

				@SuppressWarnings("unchecked")
				HashMap<String, String> value = (HashMap<String, String>) listAdapterWithHolder.getItem(arg2).getValues().get(0);
				setScreenCaption(value.get(TITLE));
				if (value.containsKey(TOTALARTICLES)) {
					try {

						loadNew(IcmsCategoryActivity.class, IcmsAllCategoryActivity.this, false, "IN_PARENTCATEGORY", value);

					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					try {

						loadNew(IcmsArticleDetailActivity.class, IcmsAllCategoryActivity.this, false, "IN_ARTICLE_INDEX", arg2 - (categoryCount) + "", "IN_ARTICLE_TITLE",
								value.get(TITLE), "IN_ARTICLE_ID_ARRAY", ID_ARRAY);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		});

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@SuppressWarnings("unchecked")
	private void getIntentData() {
		IN_PARENTCATEGORY = (HashMap<String, String>) getIntent().getSerializableExtra("IN_PARENTCATEGORY");
	}

	private void getCategories() {
		final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.category_progress_title));
		categoryDataProvider.getCategories(IN_PARENTCATEGORY.get(CATEGORY_ID), new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {

				proSeekBar.setProgress(progressCount);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {

				if (responseCode == 200) {
					prepareList((ArrayList<HashMap<String, String>>) data2, data1);
					listAdapterWithHolder = getListAdapter(listData);
					list.setAdapter(listAdapterWithHolder);
				} else {

					IjoomerUtilities.getCustomOkDialog(getString(R.string.categorylist),
							getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok), R.layout.ijoomer_ok_dialog,
							new CustomAlertNeutral() {

								@Override
								public void NeutralMathod() {
									if (responseCode == 599) {
										onBackPressed();
									}

								}
							});
				}
			}
		});
	}

	/**
	 * This method is used to prepare initial list from response data.
	 * 
	 * @param catData
	 *            specify Category Data
	 * @param articleData
	 *            specify Article Data
	 */
	public void prepareList(ArrayList<HashMap<String, String>> catData, ArrayList<HashMap<String, String>> articleData) {

		listData.clear();
		ID_ARRAY.clear();
		categoryCount = catData.size();
		if (catData != null && categoryCount > 0) {

			for (int i = 0; i < categoryCount; i++) {

				if (i == (categoryCount - 1)) {
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
			int size = articleData.size();
			for (int i = 0; i < size; i++) {
				ID_ARRAY.add(articleData.get(i).get(ARTICLEID));
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
	 * This method is used to prepare initial list from response data.
	 * 
	 * @param data
	 *            : data from response.
	 */
	public void prepareList(ArrayList<HashMap<String, String>> articleData) {

		if (articleData != null && articleData.size() > 0) {
			for (HashMap<String, String> hashMap : articleData) {
				ID_ARRAY.add(hashMap.get(ARTICLEID));
				SmartListItem articleitem = new SmartListItem();
				articleitem.setItemLayout(R.layout.icms_category_listitem);
				ArrayList<Object> articlevalues = new ArrayList<Object>();
				articlevalues.add(hashMap);
				articleitem.setValues(articlevalues);

				listData.add(articleitem);

			}
		}
	}

	/**
	 * This method is used to get list adapter.
	 * 
	 * @param listData
	 *            :List data prepared by <b>prepareList()</b> Method.
	 * @return smartListAdapter
	 */

	public SmartListAdapterWithHolder getListAdapter(final ArrayList<SmartListItem> listData) {

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.icms_category_listitem, listData, new ItemView() {

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
					// holder.icmsTxtHeader.setVisibility(View.GONE);
					// holder.icmsTxtFooter.setVisibility(View.GONE);
					holder.icmsLnrCatListItem.setVisibility(View.VISIBLE);
					holder.icmsLnrArtListItem.setVisibility(View.GONE);
					/*
					 * Sub catagory
					 */

					androidAQuery.id(holder.icmsCatImageThumb).image(value.get(IMAGE), true, true, getDeviceWidth(), R.drawable.icms_default);
					try {
						holder.icmsCatTxtTitle.setText(value.get(TITLE));
					} catch (Exception e) {
					}
					try {
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
					// holder.icmsTxtHeader.setVisibility(View.GONE);
					// holder.icmsTxtFooter.setVisibility(View.GONE);
					holder.icmsLnrCatListItem.setVisibility(View.GONE);
					holder.icmsLnrArtListItem.setVisibility(View.VISIBLE);

				}

				/*
				 * article
				 */
				try {
					holder.icmsTxtTitle.setText(value.get(TITLE));
				} catch (Exception e) {
				}
				try {
					holder.icmsTxtIntro.setText(Html.fromHtml(value.get(INTROTEXT)).toString().trim());
				} catch (Exception e) {
				}
				try {
					androidAQuery.id(holder.icmsImageThumb).image(value.get(IMAGE), true, true, getDeviceWidth(), R.drawable.icms_default);
				} catch (Exception e) {
					holder.icmsImageThumb.setVisibility(View.GONE);
				}

				return v;
			}

		});
		return adapterWithHolder;
	}

}