package com.ijoomer.components.icms;

import java.util.ArrayList;
import java.util.HashMap;

import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.icms.IcmsArticlesDataProvider;
import com.ijoomer.src.R;
import com.smart.framework.CustomAlertMagnatic;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

/**
 * Activity class for IcmsArchivedArticle view
 * 
 * @author tasol
 * 
 */
public class IcmsFavouriteArticlesActivity extends IcmsMasterActivity {
	private ListView listArticle;

	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private ArrayList<String> ID_ARRAY;
	private SmartListAdapterWithHolder listAdapterWithHolder;
	private AQuery androidAQuery;

	private IcmsArticlesDataProvider articlesDataProvider = new IcmsArticlesDataProvider(this);

	private final String FAVOURITE = "favourite";

	@Override
	public int setLayoutId() {
		return R.layout.icms_article;
	}

	@Override
	public void initComponents() {
		listArticle = (ListView) findViewById(R.id.icmsListArticle);

		androidAQuery = new AQuery(this);
		ID_ARRAY = new ArrayList<String>();

	}

	@Override
	public void prepareViews() {
		((TextView) getHeaderView().findViewById(R.id.txtHeader)).setText(getScreenCaption());
		prepareList(articlesDataProvider.getFavouriteArticles());
		listAdapterWithHolder = getListAdapter(listData);
		if (listData.size() > 0) {
			listArticle.setAdapter(listAdapterWithHolder);
		} else {
			IjoomerUtilities.getCustomOkDialog(getString(R.string.favourite_article), getString(getResources().getIdentifier("code" + 204, "string", getPackageName())),
					getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

						@Override
						public void NeutralMathod() {
						}
					});

		}
	}

	@Override
	public void setActionListeners() {
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * This method is used to prepare initial list from response data.
	 * 
	 * @param data
	 *            : data from response.
	 * 
	 */
	public void prepareList(ArrayList<HashMap<String, String>> data) {

		if (data != null) {

			listData.clear();
			ID_ARRAY.clear();
			for (int i = 0; i < data.size(); i++) {
				ID_ARRAY.add(data.get(i).get(ARTICLEID));
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.icms_article_listitem);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(data.get(i));
				item.setValues(obj);
				listData.add(item);
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

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.icms_article_listitem, listData, new ItemView() {

			@Override
			public View setItemView(final int position, View v, SmartListItem item, ViewHolder holder) {

				holder.icmsTxtTitle = (IjoomerTextView) v.findViewById(R.id.icmsTxtTitle);
				holder.icmsTxtIntro = (IjoomerTextView) v.findViewById(R.id.icmsTxtIntro);
				holder.icmsImageThumb = (ImageView) v.findViewById(R.id.icmsImageThumb);
				holder.icmsArticleRemove = (IjoomerButton) v.findViewById(R.id.icmsBtnArticleRemove);
				holder.icmsLnrArticle = (LinearLayout) v.findViewById(R.id.icmsLnrArticle);

				holder.icmsArticleRemove.setVisibility(View.VISIBLE);
				final HashMap<String, String> value = (HashMap<String, String>) item.getValues().get(0);
				holder.icmsArticleRemove.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						IjoomerUtilities.getCustomConfirmDialog(getString(R.string.article_remove), getString(R.string.are_you_sure), getString(R.string.yes),
								getString(R.string.no), new CustomAlertMagnatic() {

									@Override
									public void PositiveMathod() {
										if (new IjoomerCaching(IcmsFavouriteArticlesActivity.this).deleteDataFromCache("delete from '" + FAVOURITE + "' where articleid='"
												+ value.get(ARTICLEID) + "'")) {
											listAdapterWithHolder.remove(listAdapterWithHolder.getItem(position));
											ID_ARRAY.remove(position);
											listArticle.invalidate();

										}
									}

									@Override
									public void NegativeMathod() {

									}
								});

					}
				});

				holder.icmsLnrArticle.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						try {
//							setScreenCaption(value.get(TITLE));
							loadNew(IcmsArticleDetailActivity.class, IcmsFavouriteArticlesActivity.this, false, "IN_ARTICLE_INDEX", position + "", "IN_ARTICLE_TITLE",
									value.get(TITLE), "IN_ARTICLE_ID_ARRAY", ID_ARRAY);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

				androidAQuery.id(holder.icmsImageThumb).image(value.get(IMAGE), true, true, getDeviceWidth(), R.drawable.icms_default);

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