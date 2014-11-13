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
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.icms.IcmsArticlesDataProvider;
import com.ijoomer.src.R;
import com.smart.framework.CustomAlertMagnatic;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

/**
 * This Class Contains All Method Related To IcmsFavouriteArticlesFragment.
 * 
 * @author tasol
 * 
 */
public class IcmsFavouriteArticlesFragment extends SmartFragment implements IcmsTagHolder {
	
	private ListView listArticle;

	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private ArrayList<String> ID_ARRAY;
	private SmartListAdapterWithHolder listAdapterWithHolder;
	private IcmsArticlesDataProvider articlesDataProvider;
	private AQuery androidAQuery;

	private final String FAVOURITE = "favourite";

	/**
	 * Overrides methods
	 */
	@Override
	public int setLayoutId() {
		return R.layout.icms_article_fragment;
	}

	@Override
	public void initComponents(View currentView) {
		articlesDataProvider = new IcmsArticlesDataProvider(getActivity());
		listArticle = (ListView) currentView.findViewById(R.id.icmsListArticle);

		androidAQuery = new AQuery(getActivity());
		ID_ARRAY = new ArrayList<String>();
	}

	@Override
	public void prepareViews(View currentView) {

		((TextView) ((SmartActivity) getActivity()).getHeaderView().findViewById(R.id.txtHeader)).setText(((IjoomerSuperMaster) getActivity()).getScreenCaption());
		prepareList(articlesDataProvider.getFavouriteArticles());
		listAdapterWithHolder = getListAdapter(listData);
		if (listData.size() > 0) {
			listArticle.setAdapter(listAdapterWithHolder);
		} else {
			IjoomerUtilities.getCustomOkDialog(getString(R.string.favourite_article), getString(getResources()
					.getIdentifier("code" + 204, "string", getActivity().getPackageName())), getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

				@Override
				public void NeutralMethod() {
				}
			});

		}

	}
	
	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void setActionListeners(View currentView) {

	}
	
	/**
	 * Class methods
	 */

	/**
	 * This method used to prepare list favourite article.
	 * @param data represented article data
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
	 * List adapter for favourite article.
	 * @param listData represented article data
	 * @return represented {@link SmartListAdapterWithHolder}
	 */
	public SmartListAdapterWithHolder getListAdapter(ArrayList<SmartListItem> listData) {

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.icms_article_listitem, listData, new ItemView() {

			@Override
			public View setItemView(final int position, View v, SmartListItem item, ViewHolder holder) {

				holder.icmsTxtTitle = (IjoomerTextView) v.findViewById(R.id.icmsTxtTitle);
				holder.icmsTxtIntro = (IjoomerTextView) v.findViewById(R.id.icmsTxtIntro);
				holder.icmsImageThumb = (ImageView) v.findViewById(R.id.icmsImageThumb);
				holder.icmsArticleRemove = (IjoomerButton) v.findViewById(R.id.icmsBtnArticleRemove);
				holder.icmsLnrArticle = (LinearLayout) v.findViewById(R.id.icmsLnrArticle);

				holder.icmsArticleRemove.setVisibility(View.VISIBLE);
				@SuppressWarnings("unchecked")
				final HashMap<String, String> value = (HashMap<String, String>) item.getValues().get(0);
				holder.icmsArticleRemove.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						IjoomerUtilities.getCustomConfirmDialog(getString(R.string.article_remove), getString(R.string.are_you_sure), getString(R.string.yes),
								getString(R.string.no), new CustomAlertMagnatic() {

									@Override
									public void PositiveMethod() {
										if (new IjoomerCaching(getActivity()).deleteDataFromCache("delete from '" + FAVOURITE + "' where articleid='" + value.get(ARTICLEID) + "'")) {
											listAdapterWithHolder.remove(listAdapterWithHolder.getItem(position));
											ID_ARRAY.remove(position);
											listArticle.invalidate();

										}
									}

									@Override
									public void NegativeMethod() {

									}
								});

					}
				});

				holder.icmsLnrArticle.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							((SmartActivity) getActivity()).loadNew(IcmsArticleDetailActivity.class, getActivity(), false, "IN_ARTICLE_INDEX", position + "", "IN_ARTICLE_TITLE",
									value.get(TITLE), "IN_ARTICLE_ID_ARRAY", ID_ARRAY);
						} catch (Exception e) {
							
							e.printStackTrace();
						}
					}
				});

				androidAQuery.id(holder.icmsImageThumb).image(value.get(IMAGE), true, true, ((SmartActivity) getActivity()).getDeviceWidth(), R.drawable.icms_default);

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