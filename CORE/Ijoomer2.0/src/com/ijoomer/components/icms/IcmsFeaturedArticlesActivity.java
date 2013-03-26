package com.ijoomer.components.icms;

import java.util.ArrayList;
import java.util.HashMap;

import android.text.Html;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.icms.IcmsArticlesDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

/**
 * Activity class for IcmsFeaturedArticle view
 * 
 * @author tasol
 * 
 */
public class IcmsFeaturedArticlesActivity extends IcmsMasterActivity {

	private ListView listArticle;

	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private ArrayList<String> ID_ARRAY;
	private SmartListAdapterWithHolder listAdapterWithHolder;
	private AQuery androidAQuery;

	private IcmsArticlesDataProvider articlesDataProvider = new IcmsArticlesDataProvider(this);

	@Override
	public int setLayoutId() {
		return R.layout.icms_article;
	}

	@Override
	public void initComponents() {
		listArticle = (ListView) findViewById(R.id.icmsListArticle);
		listArticle.setDivider(null);

		ID_ARRAY = new ArrayList<String>();
		androidAQuery = new AQuery(this);

	}

	@Override
	public void prepareViews() {
		((TextView) getHeaderView().findViewById(R.id.txtHeader)).setText(getScreenCaption());
		getFeaturedArticles();
	}

	@Override
	public void setActionListeners() {
		listArticle.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 0) {
					if (!articlesDataProvider.isCalling() && articlesDataProvider.hasNextPage()) {
						articlesDataProvider.getFeaturedArticles(new WebCallListener() {

							@Override
							public void onProgressUpdate(int progressCount) {
							}

							@Override
							public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								if (responseCode == 200) {
									if (data1 != null && data1.size() > 0) {

										if (IjoomerApplicationConfiguration.isCachEnable) {
											prepareList(data1, false);
											listAdapterWithHolder = getListAdapter(listData);
											listArticle.setAdapter(listAdapterWithHolder);
										} else {
											prepareList(data1, true);
										}
									}
								} else {
									IjoomerUtilities.getCustomOkDialog(getString(R.string.articles),
											getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok),
											R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

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
				}
			}
		});
		listArticle.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				try {
					HashMap<String, String> article_extra = (HashMap<String, String>) listAdapterWithHolder.getItem(arg2).getValues().get(0);
//					setScreenCaption(article_extra.get(TITLE));
					loadNew(IcmsArticleDetailActivity.class, IcmsFeaturedArticlesActivity.this, false, "IN_ARTICLE_INDEX", arg2 + "", "IN_ARTICLE_TITLE", article_extra.get(TITLE),
							"IN_ARTICLE_ID_ARRAY", ID_ARRAY);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void getFeaturedArticles() {
		final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.featured_progress_title));

		articlesDataProvider.getFeaturedArticles(new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {

				proSeekBar.setProgress(progressCount);
			}

			@Override
			public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				if (responseCode == 200) {

					prepareList(data1, false);
					listAdapterWithHolder = getListAdapter(listData);
					listArticle.setAdapter(listAdapterWithHolder);
				} else {
					IjoomerUtilities.getCustomOkDialog(getString(R.string.articles), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())),
							getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

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
	 * @param data
	 *            : data from response.
	 * 
	 * 
	 */
	public void prepareList(ArrayList<HashMap<String, String>> data, boolean append) {

		if (data != null) {

			if (!append) {
				listData.clear();
				ID_ARRAY.clear();
			}
			for (HashMap<String, String> hashMap : data) {

				SmartListItem item = new SmartListItem();
				ID_ARRAY.add(hashMap.get(ARTICLEID));
				item.setItemLayout(R.layout.icms_featured_article_listitem);
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
	 * This method is used to get list adapter.
	 * 
	 * @param listData
	 *            :List data prepared by <b>prepareList()</b> Method.
	 * @return smartListAdapter
	 */
	public SmartListAdapterWithHolder getListAdapter(ArrayList<SmartListItem> listData) {

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.icms_featured_article_listitem, listData, new ItemView() {

			@Override
			public View setItemView(int position, View v, SmartListItem item, ViewHolder holder) {

				holder.icmsTxtTitle = (IjoomerTextView) v.findViewById(R.id.icmsTxtTitle);
				holder.icmsTxtIntro = (IjoomerTextView) v.findViewById(R.id.icmsTxtIntro);
				holder.icmsImageThumb = (ImageView) v.findViewById(R.id.icmsImageThumb);
				// holder.icmsImageThumb.setVisibility(View.GONE);

				@SuppressWarnings("unchecked")
				final HashMap<String, String> value = (HashMap<String, String>) item.getValues().get(0);
				holder.icmsImageThumb.setVisibility(View.VISIBLE);
				androidAQuery.id(holder.icmsImageThumb).image(value.get(IMAGE), true, true, getDeviceWidth(), R.drawable.icms_article_default);
				holder.icmsTxtIntro.setText(Html.fromHtml(value.get(INTROTEXT)).toString().trim());
				holder.icmsTxtTitle.setText(value.get(TITLE).trim());
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