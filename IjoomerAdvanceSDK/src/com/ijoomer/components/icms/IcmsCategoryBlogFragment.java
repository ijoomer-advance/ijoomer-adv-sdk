package com.ijoomer.components.icms;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.ijoomer.common.classes.IjoomerUtilities.URLSpanConverter;
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
 * This Fragment Contains All Method Related To IcmsCategoryBlogFragment.
 * 
 * @author tasol
 * 
 */
public class IcmsCategoryBlogFragment extends SmartFragment implements IcmsTagHolder {

	private ListView list;
	private View listFooter;
	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private ArrayList<String> ID_ARRAY;
	private SmartListAdapterWithHolder listAdapterWithHolder;
	private IcmsCategoryDataProvider categoryDataProvider;
	private AQuery androidAQuery;
	private String IN_CATEGORYBLOG_ID;
	private int categoryCount;
	private JSONObject IN_OBJ;

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
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

				if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 0) {

					if (!categoryDataProvider.isCalling() && categoryDataProvider.hasNextPage()) {
						listFooterVisible();
						categoryDataProvider.getCategories(IN_CATEGORYBLOG_ID, new WebCallListenerWithCacheInfo() {

							@Override
							public void onProgressUpdate(int progressCount) {

							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2, int pageNo, int pageLimit,
									boolean fromCache) {
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
						((SmartActivity) getActivity()).loadNew(IcmsCategoryActivity.class, getActivity(), false, "IN_PARENTCATEGORY", listAdapterWithHolder.getItem(arg2)
								.getValues().get(0));
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
	private void getIntentData() {
		try {
			IN_OBJ = new JSONObject(getActivity().getIntent().getStringExtra("IN_OBJ"));
			IN_CATEGORYBLOG_ID = new JSONObject(IN_OBJ.getString(ITEMDATA)).getString("id");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method used to get blog categories list.
	 */
	private void getCategories() {

		final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.categoryblog_progress_title));
		categoryDataProvider.getCategories(IN_CATEGORYBLOG_ID, new WebCallListenerWithCacheInfo() {

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
						IjoomerUtilities.getCustomOkDialog(getString(R.string.categoryblog),
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
	 * This method used to fetch image.
	 * 
	 * @param source
	 *            represented source
	 * @return represented {@link InputStream}
	 * @throws MalformedURLException
	 *             represented {@link MalformedURLException}
	 * @throws IOException
	 *             represented {@link IOException}
	 */
	public InputStream imageFetch(String source) throws MalformedURLException, IOException {
		URL url = new URL(source);
		Object o = url.getContent();
		InputStream content = (InputStream) o;
		return content;
	}

	/**
	 * This method used to prepare article ids.
	 */
	@SuppressWarnings("unchecked")
	public void prepareIDS() {

		ID_ARRAY.clear();
		for (int i = categoryCount; i < listData.size(); i++) {
			ID_ARRAY.add(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(ARTICLEID));
		}
	}

	/**
	 * This method used to prepare list blog categories article.
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
		if (catData != null && catData.size() > 0) {

			for (int i = 0; i < catData.size(); i++) {

				if (i == (catData.size() - 1)) {
					SmartListItem catitem = new SmartListItem();
					catitem.setItemLayout(R.layout.icms_category_blog_listitem);
					ArrayList<Object> catvalues = new ArrayList<Object>();
					HashMap<String, String> tmp = catData.get(i);
					tmp.put("shadow", "");
					catvalues.add(tmp);
					catitem.setValues(catvalues);
					listData.add(catitem);

				} else {
					SmartListItem catitem = new SmartListItem();
					catitem.setItemLayout(R.layout.icms_category_blog_listitem);
					ArrayList<Object> catvalues = new ArrayList<Object>();
					catvalues.add(catData.get(i));
					catitem.setValues(catvalues);
					listData.add(catitem);
				}
			}

		}
		if (articleData != null && articleData.size() > 0) {

			for (int i = 0; i < articleData.size(); i++) {
				ID_ARRAY.add(articleData.get(i).get(ARTICLEID));
				SmartListItem articleitem = new SmartListItem();
				articleitem.setItemLayout(R.layout.icms_category_blog_listitem);
				ArrayList<Object> articlevalues = new ArrayList<Object>();
				articlevalues.add(articleData.get(i));
				articleitem.setValues(articlevalues);

				listData.add(articleitem);

			}
		}
	}

	/**
	 * This method used to prepare list blog categories.
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
				item.setItemLayout(R.layout.icms_category_blog_listitem);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hashMap);
				item.setValues(obj);
				listAdapterWithHolder.add(item);

			}

		}
	}

	/**
	 * List adapter for blog categories and article.
	 * 
	 * @param listData
	 *            represented categories data
	 * @return represented {@link SmartListAdapterWithHolder}
	 */
	public SmartListAdapterWithHolder getListAdapter(ArrayList<SmartListItem> listData) {

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.icms_category_blog_listitem, listData, new ItemView() {

			@Override
			public View setItemView(int position, View v, SmartListItem item) {

				return null;
			}

			@Override
			public View setItemView(final int position, View v, SmartListItem item, ViewHolder holder) {
				holder.icmsTxtTitle = (IjoomerTextView) v.findViewById(R.id.icmsTxtTitle);
				holder.icmsTxtIntro = (IjoomerTextView) v.findViewById(R.id.icmsTxtIntro);
				holder.icmsCatImageThumb = (ImageView) v.findViewById(R.id.icmsCatImageThumb);
				holder.icmsCatTxtArticlesCount = (IjoomerTextView) v.findViewById(R.id.icmsCatTxtArticlesCount);
				holder.icmsCatTxtTitle = (IjoomerTextView) v.findViewById(R.id.icmsCatTxtTitle);
				holder.icmsLnrCatListItem = (LinearLayout) v.findViewById(R.id.icmsLnrCatListItem);
				holder.icmsLnrBlogListItem = (LinearLayout) v.findViewById(R.id.icmsLnrBlogListItem);
				holder.icmsCatArticleSeparator = (ImageView) v.findViewById(R.id.icmsCatArticleSeparator);
				holder.icmsCatDivider = (ImageView) v.findViewById(R.id.icmsCatDivider);
				holder.icmsCatArticleSeparator.setVisibility(View.GONE);
				@SuppressWarnings("unchecked")
				final HashMap<String, String> value = (HashMap<String, String>) item.getValues().get(0);

				if (value.containsKey("totalarticles")) {
					holder.icmsLnrCatListItem.setVisibility(View.VISIBLE);
					holder.icmsLnrBlogListItem.setVisibility(View.GONE);
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
					/*
					 * article
					 */
					holder.icmsLnrCatListItem.setVisibility(View.GONE);
					holder.icmsLnrBlogListItem.setVisibility(View.VISIBLE);
					try {
						holder.icmsTxtTitle.setText(value.get(TITLE));
						holder.icmsTxtTitle.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								try {
									prepareIDS();
									((SmartActivity) getActivity()).loadNew(IcmsArticleDetailActivity.class, getActivity(), false, "IN_ARTICLE_INDEX", position - (categoryCount)
											+ "", "IN_ARTICLE_TITLE", value.get(TITLE), "IN_ARTICLE_ID_ARRAY", ID_ARRAY);
								} catch (Exception e) {
									e.printStackTrace();
								}

							}
						});

					} catch (Exception e) {
					}
					try {
						SpannableStringBuilder introTxtSpannable = (SpannableStringBuilder) Html.fromHtml(value.get(INTROTEXT).trim(), new ImageGetter() {
							@Override
							public Drawable getDrawable(String source) {
								Drawable d = null;
								try {
									InputStream src = imageFetch(source);
									d = Drawable.createFromStream(src, "src");
									if (d != null) {
										d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
									}
								} catch (MalformedURLException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}

								return d;
							}

						}, null);

						introTxtSpannable = (SpannableStringBuilder) IjoomerUtilities.RichTextUtils.replaceAll(introTxtSpannable, URLSpan.class, new URLSpanConverter());

						introTxtSpannable = getInternalLink(introTxtSpannable);

						holder.icmsTxtIntro.setText(introTxtSpannable);
						holder.icmsTxtIntro.setMovementMethod(LinkMovementMethod.getInstance());

					} catch (Exception e) {
					}

				}

				return v;
			}

		});
		return adapterWithHolder;
	}

	/**
	 * This method used to add clicable on spannable string.
	 * 
	 * @param introTxtSpannable
	 *            represented spannable string
	 * @return represented {@link SpannableStringBuilder}
	 * @throws JSONException
	 *             represented {@link JSONException}
	 */
	public SpannableStringBuilder getInternalLink(SpannableStringBuilder introTxtSpannable) throws JSONException {
		int startindex = introTxtSpannable.toString().indexOf("[link");
		int endindex = 0;

		while (startindex != -1) {
			String introString = introTxtSpannable.toString();
			String startTag = "[link";
			String endTag = "link]";
			startindex = introString.indexOf(startTag, endindex);
			if (startindex != -1) {
				endindex = introString.indexOf(endTag, startindex);
				String linkString = introTxtSpannable.toString().substring(startindex + startTag.length(), endindex);

				final JSONObject jsonObject = new JSONObject(linkString);

				final String activity = jsonObject.getString("view");
				introTxtSpannable.replace(startindex, endindex + 5, jsonObject.get("text").toString());

				endindex = startindex + jsonObject.get("text").toString().length();
				introTxtSpannable.setSpan(new ClickableSpan() {
					@Override
					public void onClick(View widget) {

						if (activity.equalsIgnoreCase("singleCategory")) {

							HashMap<String, String> value = new HashMap<String, String>();

							try {
								value.put(CATEGORY_ID, jsonObject.getString("id"));
							} catch (JSONException e) {

								e.printStackTrace();
							}

							try {
								((SmartActivity) getActivity()).loadNew(IcmsCategoryActivity.class, getActivity(), false, "IN_PARENTCATEGORY", value);
							} catch (Throwable e) {

								e.printStackTrace();
							}

						} else if (activity.equalsIgnoreCase("allCategories")) {
							HashMap<String, String> value = new HashMap<String, String>();

							try {
								value.put(CATEGORY_ID, jsonObject.getString("id"));
							} catch (JSONException e) {

								e.printStackTrace();
							}

							try {
								((SmartActivity) getActivity()).loadNew(IcmsCategoryActivity.class, getActivity(), false, "IN_PARENTCATEGORY", value);
							} catch (Throwable e) {

								e.printStackTrace();
							}
						} else if (activity.equalsIgnoreCase("archive")) {
							((SmartActivity) getActivity()).loadNew(IcmsArchivedArticlesActivity.class, getActivity(), false);
						} else if (activity.equalsIgnoreCase("articleDetail")) {

							HashMap<String, String> value = new HashMap<String, String>();
							try {
								value.put(ARTICLEID, jsonObject.getString("id"));
							} catch (JSONException e) {

								e.printStackTrace();
							}
							try {
								((SmartActivity) getActivity()).loadNew(IcmsArticleDetailActivity.class, getActivity(), false, "IN_ARTICLE", value);
							} catch (Throwable e) {

								e.printStackTrace();
							}

						} else if (activity.equalsIgnoreCase("singleArticle")) {

							HashMap<String, String> value = new HashMap<String, String>();
							try {
								value.put(ARTICLEID, jsonObject.getString("articleid"));
							} catch (JSONException e) {

								e.printStackTrace();
							}
							try {
								((SmartActivity) getActivity()).loadNew(IcmsArticleDetailActivity.class, getActivity(), false, "IN_ARTICLE", value);
							} catch (Throwable e) {

								e.printStackTrace();
							}
						} else if (activity.equalsIgnoreCase("categoryBlog")) {
							((SmartActivity) getActivity()).loadNew(IcmsCategoryBlogFragment.class, getActivity(), true);
						} else if (activity.equalsIgnoreCase("featured")) {
							((SmartActivity) getActivity()).loadNew(IcmsFeaturedArticlesActivity.class, getActivity(), true);
						}

					}
				}, startindex, endindex, 0);

			}

		}
		return introTxtSpannable;
	}

}