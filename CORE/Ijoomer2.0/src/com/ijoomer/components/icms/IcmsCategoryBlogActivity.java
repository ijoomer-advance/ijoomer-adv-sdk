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
import android.util.Log;
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
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.IjoomerUtilities.URLSpanConverter;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.icms.IcmsCategoryDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

/**
 * Activity class for IcmsCategoryBlog view
 * 
 * @author tasol
 * 
 */
public class IcmsCategoryBlogActivity extends IcmsMasterActivity {
	private ListView list;

	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private ArrayList<String> ID_ARRAY;
	private SmartListAdapterWithHolder listAdapterWithHolder;
	private AQuery androidAQuery;

	private IcmsCategoryDataProvider categoryDataProvider = new IcmsCategoryDataProvider(this);

	private String IN_CATEGORYBLOG_ID;
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

		list.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

				// TODO Auto-generated method stub

				if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 0) {

					if (!categoryDataProvider.isCalling() && categoryDataProvider.hasNextPage()) {

						categoryDataProvider.getCategories(IN_CATEGORYBLOG_ID, new WebCallListener() {

							@Override
							public void onProgressUpdate(int progressCount) {

							}

							@SuppressWarnings("unchecked")
							@Override
							public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {

								if (responseCode == 200) {

									if (IjoomerApplicationConfiguration.isCachEnable) {
										prepareList((ArrayList<HashMap<String, String>>) data2, data1);
										listAdapterWithHolder = getListAdapter(listData);
										list.setAdapter(listAdapterWithHolder);
									} else {
										prepareList(data1);
									}

								} else {
									IjoomerUtilities.getCustomOkDialog(getString(R.string.categorylist),
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
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub

				@SuppressWarnings("unchecked")
				HashMap<String, String> value = (HashMap<String, String>) listAdapterWithHolder.getItem(arg2).getValues().get(0);
//				setScreenCaption(value.get(TITLE));
				if (value.containsKey(TOTALARTICLES)) {
					try {
						setScreenCaption(value.get(TITLE));
						loadNew(IcmsCategoryActivity.class, IcmsCategoryBlogActivity.this, false, "IN_PARENTCATEGORY", listAdapterWithHolder.getItem(arg2).getValues().get(0));
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					try {

						loadNew(IcmsArticleDetailActivity.class, IcmsCategoryBlogActivity.this, false, "IN_ARTICLE_INDEX", arg2 - (categoryCount) + "", "IN_ARTICLE_TITLE",
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

	private void getIntentData() {
		IN_CATEGORYBLOG_ID = getIntent().getStringExtra("IN_CATEGORYBLOG_ID");
	}

	private void getCategories() {

		final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.categoryblog_progress_title));
		categoryDataProvider.getCategories(IN_CATEGORYBLOG_ID, new WebCallListener() {

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
					IjoomerUtilities.getCustomOkDialog(getString(R.string.categoryblog),
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
		if (catData != null && catData.size() > 0) {

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
	 * This method is used to append list from response data of articles.
	 * 
	 * 
	 * @param articleData
	 *            specify Article Data
	 */
	public void prepareList(ArrayList<HashMap<String, String>> articleData) {

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
	 * This method is used to get list adapter.
	 * 
	 * @param listData
	 *            :List data prepared by <b>prepareList()</b> Method.
	 * @return smartListAdapter
	 */

	public SmartListAdapterWithHolder getListAdapter(ArrayList<SmartListItem> listData) {

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.icms_category_blog_listitem, listData, new ItemView() {

			@Override
			public View setItemView(int position, View v, SmartListItem item) {

				return null;
			}

			@Override
			public View setItemView(int position, View v, SmartListItem item, ViewHolder holder) {
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

					/*
					 * Sub catagory
					 */
					holder.icmsLnrCatListItem.setVisibility(View.VISIBLE);
					holder.icmsLnrBlogListItem.setVisibility(View.GONE);
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
								// TODO Auto-generated method stub
								try {
									loadNew(IcmsArticleDetailActivity.class, IcmsCategoryBlogActivity.this, false, "IN_ARTICLE_ID", value.get(ARTICLEID), "IN_ARTICLE_TITLE",
											value.get(TITLE));
								} catch (Exception e) {
									// TODO Auto-generated catch block

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
	 * This method used to get internal links in given Spannable string.
	 * 
	 * @param introTxtSpannable
	 * @return SpannableStringBuilder with Clickable internal span.
	 * @throws JSONException
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
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							try {
								loadNew(IcmsCategoryActivity.class, IcmsCategoryBlogActivity.this, false, "IN_PARENTCATEGORY", value);
							} catch (Throwable e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else if (activity.equalsIgnoreCase("allCategories")) {
							HashMap<String, String> value = new HashMap<String, String>();

							try {
								value.put(CATEGORY_ID, jsonObject.getString("id"));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							try {
								loadNew(IcmsCategoryActivity.class, IcmsCategoryBlogActivity.this, false, "IN_PARENTCATEGORY", value);
							} catch (Throwable e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else if (activity.equalsIgnoreCase("archive")) {
							loadNew(IcmsArchivedArticlesActivity.class, IcmsCategoryBlogActivity.this, false);
						} else if (activity.equalsIgnoreCase("articleDetail")) {

							HashMap<String, String> value = new HashMap<String, String>();
							try {
								value.put(ARTICLEID, jsonObject.getString("id"));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								loadNew(IcmsArticleDetailActivity.class, IcmsCategoryBlogActivity.this, false, "IN_ARTICLE", value);
							} catch (Throwable e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else if (activity.equalsIgnoreCase("singleArticle")) {

							HashMap<String, String> value = new HashMap<String, String>();
							try {
								value.put(ARTICLEID, jsonObject.getString("articleid"));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								loadNew(IcmsArticleDetailActivity.class, IcmsCategoryBlogActivity.this, false, "IN_ARTICLE", value);
							} catch (Throwable e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else if (activity.equalsIgnoreCase("categoryBlog")) {
							loadNew(IcmsCategoryBlogActivity.class, IcmsCategoryBlogActivity.this, true);
						} else if (activity.equalsIgnoreCase("featured")) {
							loadNew(IcmsFeaturedArticlesActivity.class, IcmsCategoryBlogActivity.this, true);
						}

					}
				}, startindex, endindex, 0);

			}

		}
		return introTxtSpannable;
	}

	/**
	 * This method is used to fetch image from image url.
	 * 
	 * @param source
	 * @return Inputstream for image
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public InputStream imageFetch(String source) throws MalformedURLException, IOException {
		URL url = new URL(source);
		Object o = url.getContent();
		InputStream content = (InputStream) o;
		// add delay here (see comment at the end)
		return content;
	}

}