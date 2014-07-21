package com.ijoomer.components.icms;

import java.util.ArrayList;
import java.util.HashMap;

import android.text.Html;
import android.view.LayoutInflater;
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
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.icms.IcmsArticlesDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListenerWithCacheInfo;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

/**
 * This Fragment Contains All Method Related To IcmsArchivedArticlesFragment.
 * 
 * @author tasol
 * 
 */
public class IcmsArchivedArticlesFragment extends SmartFragment implements IcmsTagHolder {

	private ListView listArticle;
	private View listFooter;

	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private ArrayList<String> ID_ARRAY;
	private SmartListAdapterWithHolder listAdapterWithHolder;
	private AQuery androidAQuery;

	private IcmsArticlesDataProvider articlesDataProvider;

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
		listFooter = LayoutInflater.from(getActivity()).inflate(R.layout.ijoomer_list_footer, null);
		listArticle.addFooterView(listFooter, null, false);
		ID_ARRAY = new ArrayList<String>();
		androidAQuery = new AQuery(getActivity());
	}

	@Override
	public void prepareViews(View currentView) {
		((TextView) ((SmartActivity) getActivity()).getHeaderView().findViewById(R.id.txtHeader)).setText(((IjoomerSuperMaster) getActivity()).getScreenCaption());
		getArchivedArticles();
	}

	@Override
	public void setActionListeners(View currentView) {
		listArticle.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 0) {

					if (!articlesDataProvider.isCalling() && articlesDataProvider.hasNextPage()) {
						listFooterVisible();
						articlesDataProvider.getArchivedArticles(new WebCallListenerWithCacheInfo() {

							@Override
							public void onProgressUpdate(int progressCount) {
							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2, int pageNo, int pageLimit,
									boolean fromCache) {
								listFooterInvisible();
								if (responseCode == 200) {
									if (data1 != null && data1.size() > 0) {

										prepareList(data1, true, fromCache, pageNo, pageLimit);

									}
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
				try {
					prepareIDS();
					@SuppressWarnings("unchecked")
					HashMap<String, String> article_extra = (HashMap<String, String>) listAdapterWithHolder.getItem(arg2).getValues().get(0);
					((SmartActivity) getActivity()).loadNew(IcmsArticleDetailActivity.class, getActivity(), false, "IN_ARTICLE_INDEX", arg2 + "", "IN_ARTICLE_TITLE",
							article_extra.get(TITLE), "IN_ARTICLE_ID_ARRAY", ID_ARRAY);
				} catch (Exception e) {
					e.printStackTrace();
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
	 * This method used to get archived article list.
	 */
	private void getArchivedArticles() {
		final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.archived_progress_title));

		articlesDataProvider.getArchivedArticles(new WebCallListenerWithCacheInfo() {

			@Override
			public void onProgressUpdate(int progressCount) {

				proSeekBar.setProgress(progressCount);
			}

			@Override
			public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2, int pageNo, int pageLimit,
					boolean fromCache) {
				try {
					if (responseCode == 200) {

						prepareList(data1, false, fromCache, pageNo, pageLimit);
						listAdapterWithHolder = getListAdapter(listData);
						listArticle.setAdapter(listAdapterWithHolder);
					} else {
						IjoomerUtilities.getCustomOkDialog(getString(R.string.articles),
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
	 * This method used to prepare article ids.
	 */
	@SuppressWarnings("unchecked")
	public void prepareIDS() {

		ID_ARRAY.clear();

		for (SmartListItem row : listData) {
			ID_ARRAY.add(((HashMap<String, String>) row.getValues().get(0)).get(ARTICLEID));
		}
	}

	/**
	 * This method used to prepare list archived article.
	 * 
	 * @param data
	 *            represented article data
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
			if (!append) {
				listData.clear();
			} else {
				if (!fromCache) {
					int startIndex = ((pageno - 1) * pagelimit);
					int endIndex = listAdapterWithHolder.getCount();
					if (startIndex > 0) {
						for (int i = endIndex; i >= startIndex; i--) {
							try {
								listAdapterWithHolder.remove(listAdapterWithHolder.getItem(i));
								listData.remove(i);
							} catch (Exception e) {
							}
						}
					}
				}
			}

			for (HashMap<String, String> hashMap : data) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.icms_article_listitem);
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
	 * List adapter for archived article 
	 * @param listData represented article data
	 * @return represented {@link SmartListAdapterWithHolder}
	 */
	public SmartListAdapterWithHolder getListAdapter(ArrayList<SmartListItem> listData) {

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.icms_article_listitem, listData, new ItemView() {

			@Override
			public View setItemView(int position, View v, SmartListItem item, ViewHolder holder) {

				holder.icmsTxtTitle = (IjoomerTextView) v.findViewById(R.id.icmsTxtTitle);
				holder.icmsTxtIntro = (IjoomerTextView) v.findViewById(R.id.icmsTxtIntro);
				holder.icmsImageThumb = (ImageView) v.findViewById(R.id.icmsImageThumb);

				@SuppressWarnings("unchecked")
				HashMap<String, String> value = (HashMap<String, String>) item.getValues().get(0);

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