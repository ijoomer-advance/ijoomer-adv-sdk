package com.ijoomer.components.jReview;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import pl.mg6.android.maps.extensions.GoogleMap;
import pl.mg6.android.maps.extensions.GoogleMap.OnMapClickListener;
import pl.mg6.android.maps.extensions.SupportMapFragment;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerShareActivity;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.IjoomerUtilities.URLSpanConverter;
import com.ijoomer.common.classes.IjoomerWebviewClient;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.custom.interfaces.IjoomerSharedPreferences;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerRatingBar;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.jReview.JReviewDataProvider;
import com.ijoomer.src.IjoomerLoginActivity;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;

/**
 * This Fragment Contains All Method Related To JReviewArticleDetailFragment.
 * 
 * @author tasol
 * 
 */
@SuppressLint("ValidFragment")
public class JReviewArticleDetailFragment extends SmartFragment implements JReviewTagHandler, IjoomerSharedPreferences {

	private static String TAG_EXPAND = "expand";
	private static String TAG_COLLAPSE = "collapse";

	private static String ISFAVOURITE = "1";
	private static String ISUNFAVOURITE = "0";

	private IjoomerTextView txtArticleTitle, txtArticleIntro, txtCategory, txtPublishOn, txtPageIndicator, txtUserRatingCount, txtEditorRatingCount, txtUserRating, txtEditorRating, txtFavorite,
	txtPhotos, txtVideos, txtMusics, txtAttachment;
	private ImageView imageFullText, imageShare;
	private IjoomerButton intro_toggle_btn, info_toggle_btn, map_toggle_btn, reviewsBtn;
	private LinearLayout articledetailview, info_layout, articleintroview, map_layout, map_view;
	private IjoomerRatingBar userRatingBar, editorRatingBar;

	private ArrayList<HashMap<String, String>> articledetail;
	private ArrayList<HashMap<String, String>> groups;
	private IjoomerCaching iCaching;
	private JReviewDataProvider dataProvider;
	private AQuery androidQuery;

	private SupportMapFragment mapFragment;
	private GoogleMap googleMap;
	private Timer t = null;

	private String articleId;
	private String inpage;
	private String categoryId;
	private int position, totalPages;

	/**
	 * Constructor
	 * 
	 * @param mContext
	 *            represented {@link Context}
	 * @param articleId
	 *            represented article id
	 * @param categoryId
	 *            represented category id
	 * @param inpage
	 *            represented in page
	 * @param position
	 *            represented article position
	 * @param totalPages
	 *            represented articles total pages
	 */
	public JReviewArticleDetailFragment(Context mContext, String inpage, String categoryId, String articleId, int position, int totalPages) {
		this.articleId = articleId;
		this.categoryId = categoryId;
		this.inpage = inpage;
		this.position = position;
		this.totalPages = totalPages;
	}

	/**
	 * Overrides methods
	 */
	@Override
	public int setLayoutId() {
		return R.layout.jreview_article_detail_fragment;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents(View currentView) {
		iCaching = new IjoomerCaching(getActivity());
		dataProvider = new JReviewDataProvider(getActivity());
		androidQuery = new AQuery(getActivity());

		txtPageIndicator = (IjoomerTextView) currentView.findViewById(R.id.jreviewTxtIndicator);
		userRatingBar = (IjoomerRatingBar) currentView.findViewById(R.id.jreviewarticleuserratingBar);
		txtUserRating = (IjoomerTextView) currentView.findViewById(R.id.jreviewarticleuserrating);
		editorRatingBar = (IjoomerRatingBar) currentView.findViewById(R.id.jreviewarticleeditorratingBar);
		txtEditorRating = (IjoomerTextView) currentView.findViewById(R.id.jreviewarticleeditorrating);
		txtUserRatingCount = (IjoomerTextView) currentView.findViewById(R.id.jreviewarticleuserratingcount);
		txtEditorRatingCount = (IjoomerTextView) currentView.findViewById(R.id.jreviewarticleeditorratingcount);

		txtFavorite = (IjoomerTextView) currentView.findViewById(R.id.jreviewarticletxtFavouriteCount);
		txtPhotos = (IjoomerTextView) currentView.findViewById(R.id.jreviewarticletxtPhotoCount);
		txtVideos = (IjoomerTextView) currentView.findViewById(R.id.jreviewarticletxtVideoCount);
		txtMusics = (IjoomerTextView) currentView.findViewById(R.id.jreviewarticletxtMusicCount);
		txtAttachment = (IjoomerTextView) currentView.findViewById(R.id.jreviewarticletxtAttachmentCount);
		imageShare = (ImageView) currentView.findViewById(R.id.jreviewarticleshare);
		txtCategory = (IjoomerTextView) currentView.findViewById(R.id.jreviewTxtCategory);
		txtArticleTitle = (IjoomerTextView) currentView.findViewById(R.id.jreviewArticleTxtTitle);
		txtArticleIntro = (IjoomerTextView) currentView.findViewById(R.id.jreviewArticleFullIntrotxt);
		txtPublishOn = (IjoomerTextView) currentView.findViewById(R.id.jreviewTxtPublishedOn);
		imageFullText = (ImageView) currentView.findViewById(R.id.jreviewArticleFullimg);
		articledetailview = (LinearLayout) currentView.findViewById(R.id.articledetailview);
		info_layout = (LinearLayout) currentView.findViewById(R.id.lnr_form);
		articleintroview = (LinearLayout) currentView.findViewById(R.id.articleintroview);
		intro_toggle_btn = (IjoomerButton) currentView.findViewById(R.id.jreview_intro_togglebtn);
		intro_toggle_btn.setPadding(10, 0, 0, 0);
		intro_toggle_btn.setTag(TAG_EXPAND);
		info_toggle_btn = (IjoomerButton) currentView.findViewById(R.id.jreview_info_togglebtn);
		info_toggle_btn.setPadding(10, 0, 0, 0);
		info_toggle_btn.setTag(TAG_EXPAND);
		map_view = (LinearLayout) currentView.findViewById(R.id.mapView);
		map_toggle_btn = (IjoomerButton) currentView.findViewById(R.id.jreview_map_togglebtn);
		map_toggle_btn.setPadding(10, 0, 0, 0);
		map_toggle_btn.setTag(TAG_EXPAND);
		map_layout = (LinearLayout) currentView.findViewById(R.id.lnr_map);
		reviewsBtn = (IjoomerButton) currentView.findViewById(R.id.jreview_reviews_btn);
		reviewsBtn.setPadding(10, 0, 0, 0);
	}

	@Override
	public void prepareViews(View currentView) {
		txtPageIndicator.setText(position + " " + getString(R.string.of) + " " + totalPages);

		if(((SmartActivity) getActivity()).getSmartApplication().readSharedPreferences().getString(SP_USERNAME, "").length() > 0){
			isarticleFavourite();
		}

		// get article dynamic info details
		groups = dataProvider.getListingForm(categoryId, articleId);

		// get article details
		//articledetail = dataProvider.getSingleArticle(categoryId, articleId);
		articledetail = dataProvider.getArticleDetails(categoryId, articleId);
		prepareArticleDetail(articledetail);
	}

	@Override
	public void setActionListeners(View currentView) {
		txtFavorite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (((SmartActivity) getActivity()).getSmartApplication().readSharedPreferences().getString(SP_USERNAME, "").length() > 0) {
					if (txtFavorite.getTag() != null) {
						if (txtFavorite.getTag().toString().equalsIgnoreCase("0")) {
							addtofavourite();
						} else {
							removefavourite();
						}
					}
				} else {
					try {
						((SmartActivity) getActivity()).getSmartApplication().writeSharedPreferences(SP_DOLOGIN, true);
						((SmartActivity) getActivity()).loadNew(IjoomerLoginActivity.class, getActivity(), false);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

		txtPhotos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					if (articledetail.get(0).get(PHOTO_COUNT).equalsIgnoreCase("0")) {
						if(IjoomerGlobalConfiguration.isJreviewPhotoUploadEnable().equalsIgnoreCase("1")){
							((SmartActivity) getActivity()).loadNew(JReviewArticleGalleryActivity.class, getActivity(), false,
									CATEGORY_ID, categoryId, ARTICLEID, articleId, ARTICLENAME,
									articledetail.get(0).get(ARTICLENAME), "IN_INDEX", 0);
						}else {
							if(articledetail.get(0).get(USERNAME).
									equalsIgnoreCase(((SmartActivity) getActivity()).getSmartApplication().readSharedPreferences().getString(SP_USERNAME, ""))){
								((SmartActivity) getActivity()).loadNew(JReviewArticleGalleryActivity.class, getActivity(), false,
										CATEGORY_ID, categoryId, ARTICLEID, articleId, ARTICLENAME,
										articledetail.get(0).get(ARTICLENAME), "IN_INDEX", 0);
							}
						}
					}else{
						((SmartActivity) getActivity()).loadNew(JReviewArticleGalleryActivity.class, getActivity(), false,
								CATEGORY_ID, categoryId, ARTICLEID, articleId, ARTICLENAME,
								articledetail.get(0).get(ARTICLENAME), "IN_INDEX", 0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		txtVideos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					if (articledetail.get(0).get(VIDEO_COUNT).equalsIgnoreCase("0")) {
						if(IjoomerGlobalConfiguration.isJreviewVideoUploadEnable().equalsIgnoreCase("1")){
							((SmartActivity) getActivity()).loadNew(JReviewArticleVideosActivity.class, getActivity(), false,
									CATEGORY_ID, categoryId, ARTICLEID, articleId, ARTICLENAME, articledetail.get(0).get(ARTICLENAME));
						}else {
							if(articledetail.get(0).get(USERNAME).
									equalsIgnoreCase(((SmartActivity) getActivity()).getSmartApplication().readSharedPreferences().getString(SP_USERNAME, ""))){
								((SmartActivity) getActivity()).loadNew(JReviewArticleVideosActivity.class, getActivity(), false,
										CATEGORY_ID, categoryId, ARTICLEID, articleId, ARTICLENAME, articledetail.get(0).get(ARTICLENAME));
							}
						}
					}else{
						((SmartActivity) getActivity()).loadNew(JReviewArticleVideosActivity.class, getActivity(), false,
								CATEGORY_ID, categoryId, ARTICLEID, articleId, ARTICLENAME, articledetail.get(0).get(ARTICLENAME));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		txtAttachment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					if (articledetail.get(0).get(ATTACHMENT_COUNT).equalsIgnoreCase("0")) {
						if(IjoomerGlobalConfiguration.isJreviewAttachmentUploadEnable().equalsIgnoreCase("1")){
							((SmartActivity) getActivity()).loadNew(JreviewArticleAttachmentsActivity.class, getActivity(), false, 
									CATEGORY_ID, categoryId, ARTICLEID, articleId, ARTICLENAME,articledetail.get(0).get(ARTICLENAME));
						}else {
							if(articledetail.get(0).get(USERNAME).
									equalsIgnoreCase(((SmartActivity) getActivity()).getSmartApplication().readSharedPreferences().getString(SP_USERNAME, ""))){
								((SmartActivity) getActivity()).loadNew(JreviewArticleAttachmentsActivity.class, getActivity(), false, 
										CATEGORY_ID, categoryId, ARTICLEID, articleId, ARTICLENAME,articledetail.get(0).get(ARTICLENAME));
							}
						}
					}else{
						((SmartActivity) getActivity()).loadNew(JreviewArticleAttachmentsActivity.class, getActivity(), false, 
								CATEGORY_ID, categoryId, ARTICLEID, articleId, ARTICLENAME,articledetail.get(0).get(ARTICLENAME));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		txtMusics.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					if (articledetail.get(0).get(AUDIO_COUNT).equalsIgnoreCase("0")) {
						if(IjoomerGlobalConfiguration.isJreviewAudioUploadEnable().equalsIgnoreCase("1")){
							((SmartActivity) getActivity()).loadNew(JreviewArticleAudiosActivity.class, getActivity(), false,
									CATEGORY_ID, categoryId, ARTICLEID, articleId, ARTICLENAME, articledetail.get(0).get(ARTICLENAME));
						}else {
							if(articledetail.get(0).get(USERNAME).
									equalsIgnoreCase(((SmartActivity) getActivity()).getSmartApplication().readSharedPreferences().getString(SP_USERNAME, ""))){
								((SmartActivity) getActivity()).loadNew(JreviewArticleAudiosActivity.class, getActivity(), false,
										CATEGORY_ID, categoryId, ARTICLEID, articleId, ARTICLENAME, articledetail.get(0).get(ARTICLENAME));
							}
						}
					} else{
						((SmartActivity) getActivity()).loadNew(JreviewArticleAudiosActivity.class, getActivity(), false,
								CATEGORY_ID, categoryId, ARTICLEID, articleId, ARTICLENAME, articledetail.get(0).get(ARTICLENAME));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		imageShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					String[] articleImages = ((IjoomerSuperMaster) getActivity()).getStringArray(articledetail.get(0).get(IMAGES));
					((SmartActivity) getActivity()).loadNew(IjoomerShareActivity.class, getActivity(), false,
							"IN_SHARE_CAPTION", articledetail.get(0).get(ARTICLENAME),
							"IN_SHARE_DESCRIPTION", articledetail.get(0).get(FULLTEXT), 
							"IN_SHARE_THUMB", articleImages == null ? "" : articleImages[0],
									"IN_SHARE_SHARELINK", articledetail.get(0).get(SHARELINK));
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});

		intro_toggle_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (intro_toggle_btn.getTag().toString().equalsIgnoreCase(TAG_EXPAND)) {
					txtArticleIntro.setMaxLines(Integer.MAX_VALUE);
					intro_toggle_btn.setBackgroundResource(R.drawable.jreview_expandable_toggle_button_expanded_bg);
					intro_toggle_btn.setText(getString(R.string.jreview_view_less));
					intro_toggle_btn.setPadding(10, 0, 0, 0);
					intro_toggle_btn.setTag(TAG_COLLAPSE);
				} else {
					txtArticleIntro.setMaxLines(4);
					intro_toggle_btn.setBackgroundResource(R.drawable.jreview_expandable_toggle_button_collapsed_bg);
					intro_toggle_btn.setText(getString(R.string.jreview_view_more));
					intro_toggle_btn.setPadding(10, 0, 0, 0);
					intro_toggle_btn.setTag(TAG_EXPAND);
				}
			}
		});

		info_toggle_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (info_toggle_btn.getTag().toString().equalsIgnoreCase(TAG_EXPAND)) {
					info_layout.setVisibility(View.VISIBLE);
					info_toggle_btn.setBackgroundResource(R.drawable.jreview_expandable_toggle_button_expanded_bg);
					info_toggle_btn.setPadding(10, 0, 0, 0);
					info_toggle_btn.setTag(TAG_COLLAPSE);
				} else {
					info_layout.setVisibility(View.GONE);
					info_toggle_btn.setBackgroundResource(R.drawable.jreview_expandable_toggle_button_collapsed_bg);
					info_toggle_btn.setPadding(10, 0, 0, 0);
					info_toggle_btn.setTag(TAG_EXPAND);
				}
			}
		});

		map_toggle_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (map_toggle_btn.getTag().toString().equalsIgnoreCase(TAG_EXPAND)) {
					map_layout.setVisibility(View.VISIBLE);
					map_toggle_btn.setBackgroundResource(R.drawable.jreview_expandable_toggle_button_expanded_bg);
					map_toggle_btn.setPadding(10, 0, 0, 0);
					map_toggle_btn.setTag(TAG_COLLAPSE);
				} else {
					map_layout.setVisibility(View.GONE);
					map_toggle_btn.setBackgroundResource(R.drawable.jreview_expandable_toggle_button_collapsed_bg);
					map_toggle_btn.setPadding(10, 0, 0, 0);
					map_toggle_btn.setTag(TAG_EXPAND);
				}
			}
		});

		reviewsBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (articledetail.get(0).get(USERRATINGCOUNT).equalsIgnoreCase("0")) {
					if (((SmartActivity) getActivity()).getSmartApplication().readSharedPreferences().getString(SP_USERNAME, "").length() > 0) {
						try {
							((SmartActivity) getActivity()).loadNew(JReviewArticleAddReviewActivity.class, getActivity(), false,
									ARTICLEID, articleId, REVIEWID, "0", "IN_REVIEWTITLE", "", "IN_REVIEWCOMMENTS", "", 
									"IN_CRITERIALIST", iCaching.parseData(new JSONArray(articledetail.get(0).get(AVERAGEREVIEWCRITERIA))),
									"IS_EDIT", false);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						try {
							((SmartActivity) getActivity()).getSmartApplication().writeSharedPreferences(SP_DOLOGIN, true);
							((SmartActivity) getActivity()).loadNew(IjoomerLoginActivity.class, getActivity(), false);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					try {
						((SmartActivity) getActivity()).loadNew(JReviewArticleReviewsActivity.class, getActivity(), false, CATEGORY_ID, categoryId, ARTICLEID, articleId, ARTICLENAME, articledetail
								.get(0).get(ARTICLENAME), "IN_AVERAGERATING", articledetail.get(0).get(OVERALLAVERAGERATING),
								"IN_REVIEWLIST", iCaching.parseData(new JSONArray(articledetail.get(0).get(REVIEWS))), 
								"IN_CRITERIALIST", iCaching.parseData(new JSONArray(articledetail.get(0).get(AVERAGEREVIEWCRITERIA))));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	/**
	 * Class methods
	 */
	private void isarticleFavourite(){
		dataProvider.isfavourite(articleId, new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {
			}

			@Override
			public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				if(responseCode == 200){
					try{
						JSONObject result = (JSONObject) data2;
						if(result.getString(ISFAVORITE).equalsIgnoreCase("1")){
							txtFavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.jreview_favorites_selected, 0, 0, 0);
						}else{
							txtFavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.jreview_favorites, 0, 0, 0);
						}
						txtFavorite.setTag(result.getString(ISFAVORITE));
						iCaching.updateTable("UPDATE jreview_articles SET isFavorite='"+result.getString(ISFAVORITE)+"' WHERE articleID="+articleId);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		});
	}

	private void addtofavourite(){
		final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_addtofavoutrite));

		dataProvider.addtofavourite(articleId, new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {
				proSeekBar.setProgress(progressCount);
			}

			@Override
			public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				if(responseCode == 200){
					try{
						JSONObject result = (JSONObject) data2;
						txtFavorite.setTag(ISFAVOURITE);
						txtFavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.jreview_favorites_selected, 0, 0, 0);
						txtFavorite.setText("("+result.getString(FAVOURITECOUNT)+")");
						iCaching.updateTable("UPDATE jreview_articles SET isFavorite='"+ISFAVOURITE+"' WHERE articleID="+articleId);
						iCaching.updateTable("UPDATE jreview_articles SET totalFavorite='"+result.getString(FAVOURITECOUNT)+"' WHERE articleID="+articleId);
						((SmartActivity) getActivity()).getSmartApplication().writeSharedPreferences(SP_RELOADARTICLES, true);
						Toast.makeText(getActivity(), getString(R.string.jreview_addtofavorite), Toast.LENGTH_SHORT).show();
					}catch(Exception e){
						e.printStackTrace();
					}
				}else {
					IjoomerUtilities.getCustomOkDialog(getString(R.string.jreview_review), getString(getResources()
							.getIdentifier("code" + responseCode, "string", ((SmartActivity) getActivity()).getPackageName())), getString(R.string.ok), R.layout.ijoomer_ok_dialog,
							new CustomAlertNeutral() {

						@Override
						public void NeutralMethod() {

						}
					});
				}
			}
		});
	}

	private void removefavourite(){
		final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_removefromfavourite));

		dataProvider.removefavourite(articleId, new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {
				proSeekBar.setProgress(progressCount);
			}

			@Override
			public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				if(responseCode == 200){
					try{
						JSONObject result = (JSONObject) data2;

						txtFavorite.setTag(ISUNFAVOURITE);
						txtFavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.jreview_favorites, 0, 0, 0);
						txtFavorite.setText("("+result.getString(FAVOURITECOUNT)+")");
						iCaching.updateTable("UPDATE jreview_articles SET isFavorite='"+ISUNFAVOURITE+"' WHERE articleID="+articleId);
						iCaching.updateTable("UPDATE jreview_articles SET totalFavorite ='"+result.getString(FAVOURITECOUNT)+"' WHERE articleID="+articleId);
						((SmartActivity) getActivity()).getSmartApplication().writeSharedPreferences(SP_RELOADARTICLES, true);
						if (inpage.equalsIgnoreCase(PAGEFAVOURITE)) {
							((SmartActivity) getActivity()).getSmartApplication().writeSharedPreferences(SP_RELOADFAVUORITEARTICLES, true);
							getActivity().finish();
						}else{
							Toast.makeText(getActivity(), getString(R.string.jreview_removedfromfavorite), Toast.LENGTH_SHORT).show();
						}

					}catch(Exception e){
						e.printStackTrace();
					}
				}else {
					IjoomerUtilities.getCustomOkDialog(getString(R.string.jreview_review), getString(getResources()
							.getIdentifier("code" + responseCode, "string", ((SmartActivity) getActivity()).getPackageName())), getString(R.string.ok), R.layout.ijoomer_ok_dialog,
							new CustomAlertNeutral() {

						@Override
						public void NeutralMethod() {

						}
					});
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
	@SuppressLint("SimpleDateFormat")
	public void prepareArticleDetail(ArrayList<HashMap<String, String>> data) {
		try {
			final HashMap<String, String> value = (HashMap<String, String>) data.get(0);

			txtCategory.setText(value.get(CATEGORY_NAME));

			if (value.get(PUBLISHUP) != null && data.get(0).get(PUBLISHUP).trim().length() > 0) {
				String dateStr = value.get(PUBLISHUP);

				SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Date dateObj = curFormater.parse(dateStr);
				SimpleDateFormat postFormater = new SimpleDateFormat("MM/dd/yyyy");
				txtPublishOn.setText(postFormater.format(dateObj));

			}
			txtArticleTitle.setText(value.get(ARTICLENAME).trim().toUpperCase());

			//view article images
			if (value.get(IMAGES) != null && value.get(IMAGES).length() > 0) {
				androidQuery.id(imageFullText).image(((IjoomerSuperMaster) getActivity()).getStringArray(value.get(IMAGES))[0], true, true, 
						((SmartActivity) getActivity()).getDeviceWidth(), 0, new BitmapAjaxCallback() {
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
			} else if(value.get(MEDIAIMAGES) != null 
					&& value.get(MEDIAIMAGES).length() > 0){
				androidQuery.id(imageFullText).image(iCaching.parseData(new JSONArray(value.get(MEDIAIMAGES))).get(0).get(ORIGINAL), true, true, ((SmartActivity) getActivity()).getDeviceWidth(), 0, new BitmapAjaxCallback() {
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
			}else {
				imageFullText.setVisibility(View.GONE);
			}

			userRatingBar.setStarRating(IjoomerUtilities.parseFloat(value.get(OVERALLAVERAGERATING)));
			txtUserRating.setText("(" + IjoomerUtilities.parseFloat(value.get(OVERALLAVERAGERATING)) + ")");
			txtUserRatingCount.setText("(" + value.get(USERRATINGCOUNT) + ")");

			editorRatingBar.setStarRating(IjoomerUtilities.parseFloat(value.get(EDITORRATING)));
			txtEditorRating.setText("(" + IjoomerUtilities.parseFloat(value.get(EDITORRATING)) + ")");
			txtEditorRatingCount.setText("(" + value.get(EDITORRATINGCOUNT) + ")");

			//view counts belongs to article(media & favourite)
			if(IjoomerGlobalConfiguration.isJreviewVideoEnable()){
				txtVideos.setVisibility(View.VISIBLE);
				txtVideos.setText("("+value.get(VIDEO_COUNT)+")");
			}else{
				txtVideos.setVisibility(View.GONE);
			}

			if(IjoomerGlobalConfiguration.isJreviewPhotoEnable()){
				txtPhotos.setVisibility(View.VISIBLE);
				txtPhotos.setText("("+value.get(PHOTO_COUNT)+")");
			}else{
				txtPhotos.setVisibility(View.GONE);
			}

			if(IjoomerGlobalConfiguration.isJreviewAudioEnable()){
				txtMusics.setVisibility(View.VISIBLE);
				txtMusics.setText("("+value.get(AUDIO_COUNT)+")");
			}else{
				txtMusics.setVisibility(View.GONE);
			}

			if(IjoomerGlobalConfiguration.isJreviewAttachmentEnable()){
				txtAttachment.setVisibility(View.VISIBLE);
				txtAttachment.setText("("+value.get(ATTACHMENT_COUNT)+")");
			}else{
				txtAttachment.setVisibility(View.GONE);
			}

			if(IjoomerGlobalConfiguration.isJreviewFavouriteEnable()){
				txtFavorite.setVisibility(View.VISIBLE);
				if(value.get(ISFAVORITE).equalsIgnoreCase("1")){
					txtFavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.jreview_favorites_selected, 0, 0, 0);
				}else{
					txtFavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.jreview_favorites, 0, 0, 0);
				}
				txtFavorite.setTag(value.get(ISFAVORITE));
				txtFavorite.setText("("+value.get(TOTALFAVORITE)+")");
			}else{
				txtFavorite.setVisibility(View.GONE);
			}

			if (value.get(FULLTEXT).trim().length() > 0) {
				articleintroview.setVisibility(View.VISIBLE);
				SpannableStringBuilder introTxtSpannable = (SpannableStringBuilder) Html.fromHtml(value.get(FULLTEXT).trim());
				introTxtSpannable = (SpannableStringBuilder) IjoomerUtilities.RichTextUtils.replaceAll(introTxtSpannable, URLSpan.class, new URLSpanConverter());
				txtArticleIntro.setText(introTxtSpannable);
				txtArticleIntro.setMovementMethod(LinkMovementMethod.getInstance());
				if (value.get(FULLTEXT).length() > 200) {
					intro_toggle_btn.setVisibility(View.VISIBLE);
				}
			} else if(value.get(INTROTEXT).trim().length() > 0){
				articleintroview.setVisibility(View.VISIBLE);
				SpannableStringBuilder introTxtSpannable = (SpannableStringBuilder) Html.fromHtml(value.get(INTROTEXT).trim());
				introTxtSpannable = (SpannableStringBuilder) IjoomerUtilities.RichTextUtils.replaceAll(introTxtSpannable, URLSpan.class, new URLSpanConverter());
				txtArticleIntro.setText(introTxtSpannable);
				txtArticleIntro.setMovementMethod(LinkMovementMethod.getInstance());
				if (value.get(INTROTEXT).length() > 200) {
					intro_toggle_btn.setVisibility(View.VISIBLE);
				}
			}

			generateinfo(groups);

			if (value.get(LAT).length() > 0 && value.get(LONG).length() > 0) {
				map_view.setVisibility(View.VISIBLE);
				mapFragment = new SupportMapFragment();
				addFragment(map_layout.getId(), mapFragment);
				t = new Timer();
				t.schedule(new TimerTask() {

					@Override
					public void run() {
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								try {
									googleMap = mapFragment.getExtendedMap();
									mapFragment.getView().setClickable(false);
									if (googleMap != null) {
										googleMap.getUiSettings().setZoomControlsEnabled(false);
										googleMap.getUiSettings().setCompassEnabled(false);
										googleMap.getUiSettings().setZoomGesturesEnabled(false);
										googleMap.getUiSettings().setScrollGesturesEnabled(false);
										googleMap.getUiSettings().setRotateGesturesEnabled(false);
										googleMap.getUiSettings().setTiltGesturesEnabled(false);
										placeMarker();
										t.cancel();
									}
									googleMap.setOnMapClickListener(new OnMapClickListener() {

										@Override
										public void onMapClick(LatLng position) {
											try {
												((SmartActivity) getActivity()).loadNew(JReviewArticleDetailMapActivity.class, getActivity(), false, ARTICLENAME,
														articledetail.get(0).get(ARTICLENAME), "IN_MAPLIST", articledetail);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									});
								} catch (Exception e) {
								}
							}
						});
					}
				}, 0, 500);
			} else {
				map_view.setVisibility(View.GONE);
			}

			if(value.get(AVERAGEREVIEWCRITERIA) != null && value.get(AVERAGEREVIEWCRITERIA).length() > 0){
				reviewsBtn.setText(getString(R.string.jreview_user_reviews) + " (" + value.get(USERRATINGCOUNT) + ")");
			}else{
				reviewsBtn.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void placeMarker() {
		try {
			googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ijoomer_map_pin)).position(
					new LatLng(Double.parseDouble(articledetail.get(0).get(LAT)), Double.parseDouble(articledetail.get(0).get(LONG)))));
			googleMap.animateCamera(CameraUpdateFactory.zoomBy(15.0f));
			googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(articledetail.get(0).get(LAT)), Double.parseDouble(articledetail.get(0).get(LONG)))));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method used to create dynamic artcle information.
	 */
	private void generateinfo(ArrayList<HashMap<String, String>> data) {
		try {
			LayoutInflater inflater = LayoutInflater.from(getActivity());
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
			params.topMargin = 5;

			boolean showDetails = false;
			int size = data.size();
			for (int i = 0; i < size; i++) {
				boolean showGroup = false;

				View groupView = inflater.inflate(R.layout.jreview_dynamic_readonly_view_item, null);
				((IjoomerTextView) groupView.findViewById(R.id.txtGroup)).setText(data.get(i).get(GROUPNAME));
				info_layout.addView(groupView, params);

				final ArrayList<HashMap<String, String>> fields =
						iCaching.parseData(new JSONArray(data.get(i).get(FIELDS)));
				int len = fields.size();
				for (int j = 0; j < len; j++) {
					final HashMap<String, String> field = fields.get(j);

					if(field.get(VALUE).length() > 0){
						showDetails = true;
						showGroup = true;

						final View fieldView = inflater.inflate(R.layout.jreview_dynamic_readonly_view_item, null);
						((LinearLayout) fieldView.findViewById(R.id.lnrReadOnly)).setVisibility(View.VISIBLE);
						((IjoomerTextView) fieldView.findViewById(R.id.txtLable)).setText(field.get(CAPTION));
						final IjoomerTextView txtValue = ((IjoomerTextView) fieldView.findViewById(R.id.txtValue));
						if (field.get(CAPTION).contains(getString(R.string.jreview_website))) {
							Spannable spantxt = new SpannableString(field.get(VALUE));
							spantxt.setSpan(new ForegroundColorSpan(Color.BLUE), 0, field.get(VALUE).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							txtValue.setText(spantxt);
							txtValue.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									Intent intent = new Intent(getActivity(), IjoomerWebviewClient.class);
									intent.putExtra("url", field.get(VALUE));
									getActivity().startActivity(intent);
								}
							});
						} else if(field.get(CAPTION).contains(getString(R.string.jreview_phone))){
							Spannable spantxt = new SpannableString(field.get(VALUE));
							spantxt.setSpan(new ForegroundColorSpan(Color.BLUE), 0, field.get(VALUE).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							txtValue.setText(spantxt);
							txtValue.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									Intent callIntent = new Intent(Intent.ACTION_CALL);
									callIntent.setData(Uri.parse("tel:" + field.get(VALUE)));
									getActivity().startActivity(callIntent);
								}
							});
						} else {
							txtValue.setText(field.get(VALUE));
						}
						info_layout.addView(fieldView, params);
					}
				}

				if(showGroup){
					((LinearLayout) groupView.findViewById(R.id.lnrGroup)).setVisibility(View.VISIBLE);
				}
			}
			if(!showDetails){
				articledetailview.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
