package com.ijoomer.components.jReview;



import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.view.LayoutInflater;
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
import com.ijoomer.custom.interfaces.IjoomerSharedPreferences;
import com.ijoomer.customviews.IjoomerRatingBar;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.jReview.JReviewDataProvider;
import com.ijoomer.src.IjoomerLoginActivity;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
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
public class JReviewArticleReviewsFragment extends SmartFragment implements JReviewTagHandler,IjoomerSharedPreferences {

	private String IN_CAPTION;
	private String IN_CATEGORYID;
	private String IN_ARTICLEID;
	private String IN_AVERAGERATING;

	private boolean showExpandedReview = false;

	private View listHeader;
	private ImageView addReview;
	private ListView listReviews;
	private LinearLayout averageRatingsLayout;
	private ArrayList<HashMap<String, String>> IN_ARTICLE_DETAILS;
	private ArrayList<HashMap<String, String>> IN_CRITERIALIST;
	private ArrayList<HashMap<String, String>> IN_REVIEWLIST;
	private ArrayList<HashMap<String, String>> IN_REVIEWVOTESLIST;
	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder reviewlistAdapterWithHolder;

	private JReviewDataProvider dataProvider;
	private IjoomerCaching iCaching;
	private AQuery androidAQuery;


	/**
	 * Overrides methods
	 */
	@Override
	public int setLayoutId() {
		return R.layout.jreview_article_reviews_fragment;
	}

	@Override
	public void initComponents(View currentView) {
		getIntentData();

		dataProvider = new JReviewDataProvider(getActivity());
		iCaching = new IjoomerCaching(getActivity());
		androidAQuery = new AQuery(getActivity());

		addReview = ((ImageView) ((SmartActivity) getActivity()).getHeaderView().findViewById(R.id.imgaddReview));
		listReviews = (ListView) currentView.findViewById(R.id.jreviewListArticleReviews);
		listHeader = LayoutInflater.from(getActivity()).inflate(R.layout.jreview_article_reviews_header, null);
		averageRatingsLayout = (LinearLayout) listHeader.findViewById(R.id.jreviewarticleaveragereviews);
		listReviews.addHeaderView(listHeader, null, false);
	}

	@Override
	public void onResume() {
		super.onResume();
		((IjoomerSuperMaster) getActivity()).applySideMenu("JReviewAllDirectoriesActivity");
		if(((SmartActivity) getActivity()).getSmartApplication().readSharedPreferences()
				.getBoolean(SP_RELOADREVIEWS, false)){
			((SmartActivity) getActivity()).getSmartApplication().
			writeSharedPreferences(SP_RELOADREVIEWS, false);
			getReviews();
		}
	}

	@Override
	public void prepareViews(View currentView) {
		((TextView) ((IjoomerSuperMaster) getActivity()).getHeaderView()
				.findViewById(R.id.txtHeader)).setText(IN_CAPTION);
		addReview.setVisibility(View.VISIBLE);

		getReviews();
	}

	@Override
	public void setActionListeners(View currentView) {
		addReview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(((SmartActivity)getActivity()).getSmartApplication().readSharedPreferences().getString(SP_USERNAME, "").length() > 0){
					try{
						((SmartActivity) getActivity()).loadNew(JReviewArticleAddReviewActivity.class, getActivity(), false,
								ARTICLEID , IN_ARTICLEID, REVIEWID, "0", "IN_REVIEWTITLE", "", "IN_REVIEWCOMMENTS", "", 
								"IN_CRITERIALIST", IN_CRITERIALIST, "IS_EDIT", false);
					}catch(Exception e){
						e.printStackTrace();
					}
				}else{
					try{
						((SmartActivity)getActivity()).getSmartApplication().writeSharedPreferences(SP_DOLOGIN, true);
						((SmartActivity) getActivity()).loadNew(IjoomerLoginActivity.class, getActivity(), false);
					}catch(Exception e){
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
	 * This method used to get reviews data.
	 */
	private void getReviews(){
		try{
			IN_ARTICLE_DETAILS = dataProvider.getSingleArticle(IN_CATEGORYID, IN_ARTICLEID);
			System.out.println("DATA"+IN_ARTICLE_DETAILS);
			IN_AVERAGERATING = IN_ARTICLE_DETAILS.get(0).get(OVERALLAVERAGERATING);
			IN_CRITERIALIST = iCaching.parseData(new JSONArray(IN_ARTICLE_DETAILS.get(0).get(AVERAGEREVIEWCRITERIA)));
			IN_REVIEWLIST = iCaching.parseData(new JSONArray(IN_ARTICLE_DETAILS.get(0).get(REVIEWS)));

			if(IN_ARTICLE_DETAILS.get(0).get(REVIEWVOTES)!=null 
					&& IN_ARTICLE_DETAILS.get(0).get(REVIEWVOTES).length()>0){
				IN_REVIEWVOTESLIST = iCaching.parseData(new JSONArray(IN_ARTICLE_DETAILS.get(0).get(REVIEWVOTES)));
			}else{
				IN_REVIEWVOTESLIST = null;
			}

			//prepare header view
			prepareHeader();

			//prepare reviews list
			prepareList(IN_REVIEWLIST,false);
			reviewlistAdapterWithHolder = getListAdapter(listData);
			listReviews.setAdapter(reviewlistAdapterWithHolder);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * This method used to get intent data.
	 */
	private void getIntentData() {
		IN_CATEGORYID = getActivity().getIntent().getStringExtra(CATEGORY_ID) == null ? "0" : getActivity().getIntent().getStringExtra(CATEGORY_ID);
		IN_ARTICLEID = getActivity().getIntent().getStringExtra(ARTICLEID) == null ? "0" : getActivity().getIntent().getStringExtra(ARTICLEID);
		IN_CAPTION = getActivity().getIntent().getStringExtra(ARTICLENAME) == null ? "" : getActivity().getIntent().getStringExtra(ARTICLENAME);
	}

	private void prepareHeader(){
		averageRatingsLayout.removeAllViews();
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
		params.topMargin = 5;

		//Overall Rating View
		View overallratingView = inflater.inflate(R.layout.jreview_dynamic_readonly_rating_view_item, null);
		((IjoomerTextView) overallratingView.findViewById(R.id.txtLable)).setText(getString(R.string.jreview_overall_rating));
		((IjoomerRatingBar) overallratingView.findViewById(R.id.jreviewarticlereviewratingBar)).setStarRating(IjoomerUtilities.parseFloat(IN_AVERAGERATING));
		((IjoomerTextView) overallratingView.findViewById(R.id.jreviewarticlereviewrating)).setText("("+IjoomerUtilities.parseFloat(IN_AVERAGERATING)+")");
		averageRatingsLayout.addView(overallratingView, params);

		//Rating Criteria View
		int len = IN_CRITERIALIST.size();
		for (int i = 0; i < len; i++) {
			final HashMap<String, String> field = IN_CRITERIALIST.get(i);
			View ratingView = inflater.inflate(R.layout.jreview_dynamic_readonly_rating_view_item, null);
			((IjoomerTextView) ratingView.findViewById(R.id.txtLable)).setText(field.get(NAME));
			((IjoomerRatingBar) ratingView.findViewById(R.id.jreviewarticlereviewratingBar)).setStarRating(IjoomerUtilities.parseFloat(field.get(RATINGVALUE)));
			((IjoomerTextView) ratingView.findViewById(R.id.jreviewarticlereviewrating)).setText("("+IjoomerUtilities.parseFloat(field.get(RATINGVALUE))+")");
			averageRatingsLayout.addView(ratingView, params);
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
	public void prepareList(ArrayList<HashMap<String, String>> articleData,boolean append) {
		if (!append) {
			listData.clear();
		}
		if (articleData != null && articleData.size() > 0) {
			for (int i = 0; i < articleData.size(); i++) {
				SmartListItem articleitem = new SmartListItem();
				articleitem.setItemLayout(R.layout.jreview_article_reviews_listitem);
				ArrayList<Object> articlevalues = new ArrayList<Object>();
				articlevalues.add(articleData.get(i));
				articleitem.setValues(articlevalues);
				if (append) {
					reviewlistAdapterWithHolder.add(articleitem);
				} else {
					listData.add(articleitem);
				}
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

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.jreview_article_reviews_listitem, listData, new ItemView() {

			@Override
			public View setItemView(int position, View v, SmartListItem item) {
				return null;
			}

			@Override
			public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {
				holder.jreviewTxtArticleReviewsUsernametxt = (IjoomerTextView) v.findViewById(R.id.jreviewarticlereviewUsername);
				holder.jreviewTxtArticleReviewsTitletxt = (IjoomerTextView) v.findViewById(R.id.jreviewarticlereviewtitle);
				holder.jreviewTxtArticleReviewsIntrotxt = (IjoomerTextView) v.findViewById(R.id.jreviewarticlereviewdisc);
				holder.jreviewTxtArticleReviewsDatetxt = (IjoomerTextView) v.findViewById(R.id.jreviewarticlereviewDate);
				holder.jreviewTxtArticleReviewsAverageCount = (IjoomerTextView) v.findViewById(R.id.jreviewarticleaverageratingcount);
				holder.jreviewArticleReviewsRatingbar = (IjoomerRatingBar) v.findViewById(R.id.jreviewarticlereviewaverageratingBar);
				holder.jreviewTxtArticleReviewsViewMoretxt = (IjoomerTextView) v.findViewById(R.id.jreviewarticlereviewviewmoretxt);
				holder.jreviewTxtArticleReviewsCommentCount = (IjoomerTextView) v.findViewById(R.id.jreviewarticlereviewCommentCount);
				holder.jreviewTxtArticleReviewsLikeCount = (IjoomerTextView) v.findViewById(R.id.jreviewarticlereviewLikeCount);
				holder.jreviewTxtArticleReviewsUnlikeCount = (IjoomerTextView) v.findViewById(R.id.jreviewarticlereviewDislikeCount);
				holder.jreviewArticleReviewsImg = (ImageView) v.findViewById(R.id.jreviewarticlereviewimg);
				holder.jreviewArticleReviewsViewMoreToggleImg = (ImageView) v.findViewById(R.id.jreviewarticlereviewtoggleimg);
				holder.jreviewArticleReviewsViewMoreLayout = (LinearLayout) v.findViewById(R.id.jreviewarticleviewmorelayout);
				holder.jreviewArticleReviewsRatingLayout = (LinearLayout) v.findViewById(R.id.jreviewarticleotherreviewsform);

				@SuppressWarnings("unchecked")
				final HashMap<String, String> review = (HashMap<String, String>) item.getValues().get(0);

				try {
					if(review.get(USERAVATAR).length() > 0){
						holder.jreviewArticleReviewsImg.setVisibility(View.VISIBLE);
						androidAQuery.id(holder.jreviewArticleReviewsImg).image(review.get(USERAVATAR), true, true, 0, R.drawable.jreview_default);	
					}else{
						holder.jreviewArticleReviewsImg.setVisibility(View.GONE);
					}
					holder.jreviewTxtArticleReviewsUsernametxt.setText(getString(R.string.jreview_reviewd_by)+" "+review.get(USERNAME));
					holder.jreviewTxtArticleReviewsDatetxt.setText(review.get(CREATED));
					holder.jreviewArticleReviewsRatingbar.setStarRating(Float.parseFloat(review.get(AVERAGERATING)));
					holder.jreviewTxtArticleReviewsAverageCount.setText("("+IjoomerUtilities.parseFloat(review.get(AVERAGERATING))+")");
					holder.jreviewTxtArticleReviewsTitletxt.setText(review.get(TITLE));
					if(review.get(COMMENTS)!=null && review.get(COMMENTS).length() > 0){
						holder.jreviewTxtArticleReviewsIntrotxt.setVisibility(View.VISIBLE);
						holder.jreviewTxtArticleReviewsIntrotxt.setText(review.get(COMMENTS));
					}else{
						holder.jreviewTxtArticleReviewsIntrotxt.setVisibility(View.GONE);
					}
					holder.jreviewTxtArticleReviewsCommentCount.setText(review.get(COMMENTCOUNT));

					holder.jreviewTxtArticleReviewsLikeCount.setTag(review.get(REVIEWID));
					holder.jreviewTxtArticleReviewsUnlikeCount.setTag(review.get(REVIEWID));

					if(IN_REVIEWVOTESLIST!=null && IN_REVIEWVOTESLIST.size()>0){
						for(int i = 0; i < IN_REVIEWVOTESLIST.size(); i++){
							if(review.get(REVIEWID).equalsIgnoreCase(IN_REVIEWVOTESLIST.get(i).get(REVIEWID))){
								holder.jreviewTxtArticleReviewsLikeCount.setText(IN_REVIEWVOTESLIST.get(i).get(REVIEWLIKECOUNT));
								holder.jreviewTxtArticleReviewsUnlikeCount.setText(IN_REVIEWVOTESLIST.get(i).get(REVIEWUNLIKECOUNT));
							}
						}
					}

					if(review.get(RATINGCRITERIA)!=null && review.get(RATINGCRITERIA).length()>0){
						holder.jreviewArticleReviewsViewMoreLayout.setVisibility(View.VISIBLE);	
					}
					holder.jreviewArticleReviewsViewMoreLayout.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if (showExpandedReview) {
								showExpandedReview = false;
							} else {
								showExpandedReview = true;
							}
							reviewlistAdapterWithHolder.notifyDataSetChanged();
							listReviews.setSelection(position);
						}
					});

					holder.jreviewArticleReviewsRatingLayout.removeAllViews();
					LayoutInflater inflater = LayoutInflater.from(getActivity());
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
					params.topMargin = 5;

					final ArrayList<HashMap<String, String>> fields = iCaching.parseData(new JSONArray(review.get(RATINGCRITERIA)));
					int len = fields.size();
					for (int i = 0; i < len; i++) {
						final HashMap<String, String> field = fields.get(i);
						View ratingView = inflater.inflate(R.layout.jreview_dynamic_readonly_rating_view_item, null);
						((IjoomerTextView) ratingView.findViewById(R.id.txtLable)).setText(field.get(NAME));
						((IjoomerRatingBar) ratingView.findViewById(R.id.jreviewarticlereviewratingBar)).setStarRating(IjoomerUtilities.parseFloat(field.get(RATINGVALUE)));
						((IjoomerTextView) ratingView.findViewById(R.id.jreviewarticlereviewrating)).setText("("+IjoomerUtilities.parseFloat(field.get(RATINGVALUE))+")");
						holder.jreviewArticleReviewsRatingLayout.addView(ratingView, params);
					}

					if(showExpandedReview){
						holder.jreviewArticleReviewsRatingLayout.setVisibility(View.VISIBLE);
						holder.jreviewTxtArticleReviewsViewMoretxt.setText(getString(R.string.jreview_view_less));
						holder.jreviewArticleReviewsViewMoreToggleImg.setBackgroundResource(R.drawable.jreview_minimise);
					}else{
						holder.jreviewArticleReviewsRatingLayout.setVisibility(View.GONE);
						holder.jreviewTxtArticleReviewsViewMoretxt.setText(getString(R.string.jreview_view_more));
						holder.jreviewArticleReviewsViewMoreToggleImg.setBackgroundResource(R.drawable.jreview_maximise);
					}

					holder.jreviewTxtArticleReviewsLikeCount.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (((SmartActivity) getActivity()).getSmartApplication().readSharedPreferences().getString(SP_USERNAME, "").length() > 0) {
								dataProvider.sendReviewVote("1", holder.jreviewTxtArticleReviewsLikeCount.getTag().toString(), IN_ARTICLEID, new WebCallListener() {

									@Override
									public void onProgressUpdate(int progressCount) {

									}

									@Override
									public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
										if(responseCode == 200){
											try{
												JSONObject result = (JSONObject) data2;
												holder.jreviewTxtArticleReviewsLikeCount.setText(result.getString(LIKECOUNT));
											}catch(Exception e){
												e.printStackTrace();
											}
										}else{
											responseErrorMessageHandler(responseCode, false);
										}
									}
								});
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

					holder.jreviewTxtArticleReviewsUnlikeCount.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (((SmartActivity) getActivity()).getSmartApplication().readSharedPreferences().getString(SP_USERNAME, "").length() > 0) {
								dataProvider.sendReviewVote("0", holder.jreviewTxtArticleReviewsUnlikeCount.getTag().toString(), IN_ARTICLEID, new WebCallListener() {

									@Override
									public void onProgressUpdate(int progressCount) {

									}

									@Override
									public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
										if(responseCode == 200){
											try{
												JSONObject result = (JSONObject) data2;
												holder.jreviewTxtArticleReviewsUnlikeCount.setText(result.getString(DISLIKECOUNT));
											}catch(Exception e){
												e.printStackTrace();
											}
										}else{
											responseErrorMessageHandler(responseCode, false);
										}
									}
								});
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

					v.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							try{
								((SmartActivity) getActivity()).loadNew(JReviewArticleReviewsDetailActivity.class, getActivity(), 
										false, "IN_CAPTION", review.get(TITLE), CATEGORY_ID, IN_CATEGORYID, ARTICLEID, IN_ARTICLEID,
										"IN_REVIEW_DETAILS", review, "IN_REVIEW_LIKE", holder.jreviewTxtArticleReviewsLikeCount.getText().toString(),
										"IN_REVIEW_UNLIKE", holder.jreviewTxtArticleReviewsUnlikeCount.getText().toString(), 
										"IN_CRITERIALIST", fields);
							}catch(Exception e){
								e.printStackTrace();
							}
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
				return v;
			}
		});
		return adapterWithHolder;
	}

	/**
	 * This method used to shown response message.
	 * 
	 * @param responseCode
	 *            represented response code
	 * @param finishActivityOnConnectionProblem
	 *            represented finish activity on connection problem
	 */
	private void responseErrorMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {
		IjoomerUtilities.getCustomOkDialog(getString(R.string.jreview_review), getString(getResources().getIdentifier("code" + responseCode, "string", ((SmartActivity)getActivity()).getPackageName())),
				getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

			@Override
			public void NeutralMethod() {

			}
		});
	}

}