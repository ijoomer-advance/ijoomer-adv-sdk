package com.ijoomer.components.icms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerShareActivity;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.IjoomerWebviewClient;
import com.ijoomer.custom.interfaces.IjoomerSharedPreferences;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.icms.IcmsArticleDetailDataProvider;
import com.ijoomer.media.player.IjoomerMediaPlayer;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;

/**
 * This Fragment Contains All Method Related To IcmsArticleDetailFragment.
 * 
 * @author tasol
 * 
 */
@SuppressLint({ "ValidFragment", "SimpleDateFormat" })
public class IcmsArticleDetailFragment extends SmartFragment implements IcmsTagHolder, IjoomerSharedPreferences {

	private LinearLayout lnrUrls;
	private ListView listArticleDetail;
	private IjoomerTextView txtCategory, txtWrittenBy, txtPublishedOn, txtTitle, txtPageIndicator;
	private ImageView imgFavorite, imgShare;
	private ImageView imageFullText;
	private ProgressBar pbrArticleDetail;
	private WebView webFull;
	private View headerView;

	private ArrayList<HashMap<String, String>> articleDetail;
	private IcmsArticleDetailDataProvider articleDetailDataProvider;
	private AQuery androidQuery;
	private Context mContext;

	private String articleId;
	private String currentId;
	private String FAVOURITE;
	private int position, totalPages;

	/**
	 * Constructor
	 * 
	 * @param mContext
	 *            represented {@link Context}
	 * @param articleId
	 *            represented article id
	 * @param position
	 *            represented article position
	 * @param totalPages
	 *            represented articles total pages
	 */
	public IcmsArticleDetailFragment(Context mContext, String articleId, int position, int totalPages) {
		this.articleId = articleId;
		this.mContext = mContext;
		this.position = position;
		this.totalPages = totalPages;
	}

	/**
	 * Overrides methods
	 */
	@Override
	public int setLayoutId() {
		return R.layout.icms_article_detail_listview;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@SuppressLint("SetJavaScriptEnabled")
	@SuppressWarnings("deprecation")
	@Override
	public void initComponents(View currentView) {

		listArticleDetail = (ListView) currentView.findViewById(R.id.icmsListArticleDetail);
		pbrArticleDetail = (ProgressBar) currentView.findViewById(R.id.icmsPbr);
		LayoutInflater inflater = LayoutInflater.from(getActivity());
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
		webFull.getSettings().setPluginState(PluginState.ON);
		webFull.setInitialScale(99);
		FAVOURITE = "favourite";

	}

	@Override
	public void prepareViews(View currentView) {
		pbrArticleDetail.setVisibility(View.VISIBLE);
		txtPageIndicator.setText(position + " " + getString(R.string.of) + " " + totalPages);
		getArticleDetail(articleId);
		listArticleDetail.addHeaderView(headerView);
		listArticleDetail.setAdapter(null);
		listArticleDetail.setSelectionAfterHeaderView();
		txtPageIndicator.setFocusable(true);

	}

	@Override
	public void setActionListeners(View currentView) {
		imgFavorite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
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
				try {
					((SmartActivity) getActivity()).loadNew(IjoomerShareActivity.class, getActivity(), false, "IN_SHARE_CAPTION", articleDetail.get(0).get(TITLE).toString(),
							"IN_SHARE_DESCRIPTION", articleDetail.get(0).get(INTROTEXT).toString(), "IN_SHARE_THUMB", articleDetail.get(0).get(IMAGEFULLTEXT).toString(),
							"IN_SHARE_SHARELINK", articleDetail.get(0).get(SHARELINK).toString());
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * Class methods
	 */

	/**
	 * This method used to get article details.
	 * 
	 * @param id
	 *            represented article id
	 */
	public void getArticleDetail(String id) {
		currentId = id;
		articleDetailDataProvider.getArticleDetail(id, new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {
			}

			@Override
			public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				try {
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
	 * This method used to prepare article details.
	 * 
	 * @param data
	 *            represented article data
	 */
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
						textUrl.setTextColor(Color.parseColor(getString(R.color.icms_blue)));

						SpannableString spanString = new SpannableString(jsonObject.get("urltext").toString());
						spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
						spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
						spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);
						textUrl.setText(spanString);

						textUrl.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

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

				try {

					androidQuery.id(imageFullText).image(data.get(0).get(IMAGEFULLTEXT), true, true, ((SmartActivity) getActivity()).getDeviceWidth(), 0, new BitmapAjaxCallback() {
						@Override
						protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
							super.callback(url, iv, bm, status);
							if (bm != null) {
								imageFullText.setVisibility(View.VISIBLE);
								imageFullText.setImageBitmap(bm);
							} else {
								imageFullText.setVisibility(View.GONE);
							}
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
				}

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
						super.onPageFinished(view, url);
						listArticleDetail.setSelectionAfterHeaderView();
					}

					@Override
					public boolean shouldOverrideUrlLoading(WebView view, String url) {

						Intent activityintent = IcmsActivityFinder.findActivityFromUrl(mContext, url);
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
