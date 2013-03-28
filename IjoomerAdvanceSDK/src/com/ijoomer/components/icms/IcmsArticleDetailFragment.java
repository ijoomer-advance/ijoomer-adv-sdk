package com.ijoomer.components.icms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IJoomerTwitterShareActivity;
import com.ijoomer.common.classes.IjoomerActivityFinder;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.IjoomerWebviewClient;
import com.ijoomer.custom.interfaces.IjoomerSharedPreferences;
import com.ijoomer.custom.interfaces.ShareListner;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.icms.IcmsArticleDetailDataProvider;
import com.ijoomer.media.player.IjoomerMediaPlayer;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.SmartActivity;

public class IcmsArticleDetailFragment extends Fragment implements IcmsTagHolder, IjoomerSharedPreferences {
	private ProgressBar pbrArticleDetail;
	private ListView listArticleDetail;
	private LinearLayout lnrUrls;
	private WebView webFull;
	private IjoomerTextView txtCategory, txtWrittenBy, txtPublishedOn, txtTitle, txtPageIndicator;
	private ImageView imgFavorite, imgShare;
	private ImageView imageFullText;
	private View headerView;

	private AQuery androidQuery;
	private IcmsArticleDetailDataProvider articleDetailDataProvider;
	private ArrayList<HashMap<String, String>> articleDetail;

	private Context mContext;
	private int position, totalPages;
	private String articleId;
	private String currentId;
	private String FAVOURITE;

	/**
	 * Constructor
	 * 
	 * @param mContext
	 * @param articleId
	 *            : specify id of article
	 * @param position
	 *            : specify index of selected article
	 * @param totalPages
	 *            : specify total number of articles
	 */
	public IcmsArticleDetailFragment(Context mContext, String articleId, int position, int totalPages) {
		this.articleId = articleId;
		this.mContext = mContext;
		this.position = position;
		this.totalPages = totalPages;
	}

	/**
	 * Overrides method
	 */

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.icms_article_detail_listview, null);
		listArticleDetail = (ListView) v.findViewById(R.id.icmsListArticleDetail);
		pbrArticleDetail = (ProgressBar) v.findViewById(R.id.icmsPbr);
		headerView = inflater.inflate(R.layout.icms_article_detail_header, null, false);

		articleDetailDataProvider = new IcmsArticleDetailDataProvider(mContext);
		txtPageIndicator = (IjoomerTextView) headerView.findViewById(R.id.icmsTxtIndicator);
		imgFavorite = (ImageView) headerView.findViewById(R.id.icmsImageFavorite);
		imgShare = (ImageView) headerView.findViewById(R.id.icmsImageShare);
		lnrUrls = (LinearLayout) headerView.findViewById(R.id.icmsLnrUrls);
		txtCategory = (IjoomerTextView) headerView.findViewById(R.id.icmsTxtCategory);
		txtPublishedOn = (IjoomerTextView) headerView.findViewById(R.id.icmsTxtPublishedOn);
		txtWrittenBy = (IjoomerTextView) headerView.findViewById(R.id.icmsTxtWriitenBy);
		txtTitle = (IjoomerTextView) headerView.findViewById(R.id.icmsTxtTitle);
		imageFullText = (ImageView) headerView.findViewById(R.id.icmsImageFullText);
		androidQuery = new AQuery(mContext);
		webFull = (WebView) headerView.findViewById(R.id.icmsWebViewFull);
		webFull.setBackgroundColor(0);
		webFull.getSettings().setJavaScriptEnabled(true);
		webFull.getSettings().setPluginsEnabled(true);

		FAVOURITE = "favourite";
		pbrArticleDetail.setVisibility(View.VISIBLE);
		txtPageIndicator.setText(position + " " + getString(R.string.of) + " " + totalPages);
		getArticleDetail(articleId);
		imgFavorite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				imgFavorite.setVisibility(View.GONE);
				ArrayList<HashMap<String, String>> favouriteDataArray = new ArrayList<HashMap<String, String>>();
				HashMap<String, String> favouriteData = new HashMap<String, String>();
				favouriteData.put(ARTICLEID, articleDetail.get(0).get("id"));
				favouriteData.put(TITLE, articleDetail.get(0).get(TITLE));
				favouriteData.put(INTROTEXT, articleDetail.get(0).get(INTROTEXT));
				favouriteData.put(IMAGE, articleDetail.get(0).get("image_intro"));
				favouriteDataArray.add(favouriteData);
				new IjoomerCaching(mContext).createTable(favouriteDataArray, FAVOURITE);

				Toast.makeText(mContext, getString(R.string.addtofavorite), Toast.LENGTH_SHORT).show();

			}
		});

		imgShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				IjoomerUtilities.getShareDialog(new ShareListner() {

					@Override
					public void onClick(String shareOn, Object value, String message) {
						// TODO Auto-generated method stub
						if (shareOn.equals("email")) {
							String[] to = value.toString().split(",");
							Intent i = new Intent(Intent.ACTION_SEND);
							i.setType("text/html");
							i.putExtra(Intent.EXTRA_EMAIL, to);
							i.putExtra(Intent.EXTRA_SUBJECT, String.format(getString(R.string.share_email_subject), articleDetail.get(0).get(TITLE)));
							i.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(IjoomerUtilities.prepareEmailBody(message == null ? "" : message, ((SmartActivity) getActivity())
									.getSmartApplication().readSharedPreferences().getString(SP_USERNAME, "")
									+ " " + getString(R.string.saw_this_story_on_the) + " " + getString(R.string.app_name) + " " + getString(R.string.thought_you_should_see_it),
									articleDetail.get(0).get(TITLE), Html.fromHtml(articleDetail.get(0).get(INTROTEXT)).toString(), articleDetail.get(0).get(SHARELINK),
									getString(R.string.try_ijoomeradvance), getString(R.string.site_url))));
							try {
								startActivity(Intent.createChooser(i, "Send mail..."));
							} catch (android.content.ActivityNotFoundException ex) {
								((SmartActivity) getActivity()).ting(getString(R.string.share_email_no_client));
							}
						} else if (shareOn.equals("facebook")) {

							((IjoomerSuperMaster) getActivity()).facebookSharing(articleDetail.get(0).get(TITLE), articleDetail.get(0).get(TITLE),
									Html.fromHtml(articleDetail.get(0).get(INTROTEXT)).toString(), articleDetail.get(0).get(SHARELINK), articleDetail.get(0).get(IMAGEFULLTEXT),
									message == null ? "" : message);
						} else if (shareOn.equals("twitter")) {
							try {
								((SmartActivity) getActivity()).loadNew(IJoomerTwitterShareActivity.class, ((SmartActivity) getActivity()), false, "IN_TWIT_MESSAGE",
										message == null ? "" : message + "\n" + articleDetail.get(0).get(SHARELINK), "IN_TWIT_IMAGE", articleDetail.get(0).get(IMAGEFULLTEXT));
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					}
				});

			}
		});
		System.gc();
		listArticleDetail.addHeaderView(headerView);
		listArticleDetail.setAdapter(null);
		listArticleDetail.setSelectionAfterHeaderView();

		return v;
	}

	public void getArticleDetail(String id) {
		currentId = id;
		articleDetailDataProvider.getArticleDetail(id, new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {

				// TODO Auto-generated method stub

				if (responseCode == 200) {
					if (data1.get(0).get("id").equalsIgnoreCase(currentId)) {
						articleDetail = data1;
						prepareArticleDetail(data1);
						pbrArticleDetail.setVisibility(View.GONE);
					}
				} else {
					pbrArticleDetail.setVisibility(View.GONE);
					IjoomerUtilities.getCustomOkDialog(getString(R.string.articles),
							getString(getResources().getIdentifier("code" + responseCode, "string", mContext.getPackageName())), getString(R.string.ok),
							R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

								@Override
								public void NeutralMathod() {
									if (responseCode == 599) {
										getActivity().onBackPressed();
									}
								}
							});
				}

			}
		});
	}

	public void prepareArticleDetail(ArrayList<HashMap<String, String>> data) {

		if (data != null) {

			try {

				if (data.get(0).get("urls") != null && data.get(0).get("urls").length() > 0) {
					lnrUrls.removeAllViews();
					JSONArray jsonArrayUrls = new JSONArray(data.get(0).get("urls"));
					for (int i = 0; i < jsonArrayUrls.length(); i++) {
						JSONObject jsonObject = jsonArrayUrls.getJSONObject(i);
						IjoomerTextView textUrl = new IjoomerTextView(mContext);

						textUrl.setTag(jsonObject.get("url").toString());
						textUrl.setTextColor(Color.parseColor(getString(R.color.blue)));

						SpannableString spanString = new SpannableString(jsonObject.get("urltext").toString());
						spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
						spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
						spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);
						textUrl.setText(spanString);

						textUrl.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub

								String url = (String) v.getTag();
								Intent intent = new Intent(mContext, IjoomerWebviewClient.class);
								intent.putExtra("url", url);
								startActivity(intent);

							}
						});

						lnrUrls.addView(textUrl);

					}

				}

				txtTitle.setText(data.get(0).get(TITLE));

				androidQuery.id(imageFullText).image(data.get(0).get(IMAGEFULLTEXT), true, true, ((SmartActivity) getActivity()).getDeviceWidth(), R.drawable.icms_article_default);

				if (data.get(0).get(PUBLISH_ON) != null && data.get(0).get(PUBLISH_ON).trim().length() > 0) {
					String dateStr = data.get(0).get(PUBLISH_ON);

					SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					Date dateObj = curFormater.parse(dateStr);
					SimpleDateFormat postFormater = new SimpleDateFormat("MM/dd/yyyy");
					txtPublishedOn.setText(postFormater.format(dateObj));

				}

				if (data.get(0).get(CRETEDBY) != null && data.get(0).get(CRETEDBY).trim().length() > 0) {
					txtWrittenBy.setText(getString(R.string.writtenby) + " " + data.get(0).get(CRETEDBY));

				}

				if (data.get(0).get(CATEGORY_TITLE) != null && data.get(0).get(CATEGORY_TITLE).trim().length() > 0) {
					txtCategory.setText(data.get(0).get(CATEGORY_TITLE));

				}

				webFull.setWebViewClient(new WebViewClient() {
					
					@Override
					public void onPageFinished(WebView view, String url) {
						// TODO Auto-generated method stub
						super.onPageFinished(view, url);
						listArticleDetail.setSelectionAfterHeaderView();
					}
					@Override
					public boolean shouldOverrideUrlLoading(WebView view, String url) {
						// TODO Auto-generated method stub
						
						Intent activityintent = IjoomerActivityFinder.findActivityFromUrl(mContext, url);
						if (activityintent == null) {

							Intent intent = new Intent(mContext, IjoomerWebviewClient.class);
							intent.putExtra("url", url);
							startActivity(intent);
						} else {
							startActivity(activityintent);
						}
						return true;
					}

					@Override
					public void onLoadResource(WebView view, String url) {
						try {

							if (url.contains("&video_id=")) {

								url = url.substring(url.indexOf("video_id="));
								url = url.substring(0, url.indexOf("&"));

								String video_id = url.split("=")[1];
								Intent lVideoIntent = new Intent(null, Uri.parse("ytv://" + video_id + ""), mContext, IjoomerMediaPlayer.class);
								startActivity(lVideoIntent);

								StringBuilder sb = new StringBuilder(); // StringBuilder();
								sb.append("<HTML><HEAD><link rel=\"stylesheet\" type=\"text/css\" href=\"weblayout.css\" /></HEAD><body>");
								String str = articleDetail.get(0).get(FULLTEXT).toString().trim();
								str = str.replaceAll("<iframe width=\"[0-9]*", "<iframe width=\"100\\%");
								str = str.replaceAll("<img[\\w]*", "<img height=\"auto\" style=\"max-width:100\\%\";");
								sb.append(str);
								sb.append("</body></HTML>");
								webFull.loadDataWithBaseURL("file:///android_asset/css/", sb.toString(), "text/html", "utf-8", null);

							}
						} catch (Exception e) {

						}

						super.onLoadResource(view, url);
					}

				});

				StringBuilder sb = new StringBuilder(); // StringBuilder();
				sb.append("<HTML><HEAD><link rel=\"stylesheet\" type=\"text/css\" href=\"weblayout.css\" /></HEAD><body>");
				String str = data.get(0).get(FULLTEXT).toString().trim();
				str = str.replaceAll("<iframe width=\"[0-9]*", "<iframe width=\"100\\%");
				str = str.replaceAll("<img[\\w]*", "<img height=\"auto\" style=\"max-width:100\\%\";");
				sb.append(str);
				sb.append("</body></HTML>");
				webFull.loadDataWithBaseURL("file:///android_asset/css/", sb.toString(), "text/html", "utf-8", null);

			} catch (Throwable e) {
				e.printStackTrace();
			}
			
		}
	}

}
