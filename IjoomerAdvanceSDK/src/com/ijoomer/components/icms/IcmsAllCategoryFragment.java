package com.ijoomer.components.icms;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

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
 * This Fragment Contains All Method Related To IcmsAllCategoryFragment.
 * 
 * @author tasol
 * 
 */
public class IcmsAllCategoryFragment extends SmartFragment implements IcmsTagHolder {

	private ListView list;
	private JSONObject IN_OBJ;
	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private HashMap<String, String> IN_PARENTCATEGORY;
	private ArrayList<String> ID_ARRAY;
	private SmartListAdapterWithHolder listAdapterWithHolder;
	private AQuery androidAQuery;

	private IcmsCategoryDataProvider categoryDataProvider;

	private int categoryCount;

	/**
	 * Overrides method
	 */
	@Override
	public int setLayoutId() {
		return R.layout.icms_category_fragment;
	}

	@Override
	public void initComponents(View currentView) {
		categoryDataProvider = new IcmsCategoryDataProvider(getActivity());
		list = (ListView) currentView.findViewById(R.id.list);

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

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				@SuppressWarnings("unchecked")
				HashMap<String, String> value = (HashMap<String, String>) listAdapterWithHolder.getItem(arg2).getValues().get(0);
				((IjoomerSuperMaster) getActivity()).setScreenCaption(value.get(TITLE));
				if (value.containsKey(TOTALARTICLES)) {
					try {

						((SmartActivity) getActivity()).loadNew(IcmsCategoryActivity.class, getActivity(), false, "IN_PARENTCATEGORY", value);

					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					try {

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
	 * This method used to get intent data.
	 */
	@SuppressWarnings("unchecked")
	private void getIntentData() {
		try {
			IN_OBJ = new JSONObject(getActivity().getIntent().getStringExtra("IN_OBJ"));

			IN_PARENTCATEGORY = new HashMap<String, String>();
			try {
				IN_PARENTCATEGORY.put("title", getString(R.string.categorylist));
				IN_PARENTCATEGORY.put("categoryid", new JSONObject(IN_OBJ.getString(ITEMDATA)).getString("id"));
			} catch (JSONException e) {
				IN_PARENTCATEGORY.put("categoryid", "0");
				e.printStackTrace();
			}

		} catch (Exception e) {
			IN_PARENTCATEGORY = (HashMap<String, String>) getActivity().getIntent().getSerializableExtra("IN_PARENTCATEGORY");
		}
	}

	/**
	 * This method used to get article categories list.
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
				} catch (Throwable e) {
				}

			}
		});
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
					holder.icmsLnrCatListItem.setVisibility(View.VISIBLE);
					holder.icmsLnrArtListItem.setVisibility(View.GONE);
					/*
					 * Sub catagory
					 */

					androidAQuery.id(holder.icmsCatImageThumb).image(value.get(IMAGE), true, true, ((SmartActivity) getActivity()).getDeviceWidth(), R.drawable.icms_default);
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
					androidAQuery.id(holder.icmsImageThumb).image(value.get(IMAGE), true, true, ((SmartActivity) getActivity()).getDeviceWidth(), R.drawable.icms_default);
				} catch (Exception e) {
					holder.icmsImageThumb.setVisibility(View.GONE);
				}

				return v;
			}

		});
		return adapterWithHolder;
	}

}