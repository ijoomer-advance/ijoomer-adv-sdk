package com.ijoomer.components.jReview;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.custom.interfaces.ReportListner;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerRatingBar;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.customviews.IjoomerVoiceAndTextMessager;
import com.ijoomer.customviews.IjoomerVoiceAndTextMessager.MessageHandler;
import com.ijoomer.library.jReview.JReviewDataProvider;
import com.ijoomer.src.IjoomerLoginActivity;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertMagnatic;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

public class JReviewArticleReviewsDetailActivity extends JReviewMasterActivity{

	private String IN_CATEGORYID;
	private String IN_ARTICLEID;
	private String IN_CAPTION;
	private String IN_LIKE_CNT;
	private String IN_UNLIKE_CNT;

	private AQuery androidQuery;
	private IjoomerCaching iCaching;

	private ViewGroup reviewDetailsHeaderLayout;
	private ListView lstReviewsComment;
	private ImageView editReview;
	private IjoomerTextView txtUsername, txtReviewTitle, txtReviewIntro, txtReviewDate, txtLikeCount,
	txtunlikeCount, txtcommentCount, txtReviewReport, txtComments, txtAverageReviewCount;
	private IjoomerRatingBar averageReviewRatingbar;
	private ImageView userimg;
	private LinearLayout reviewsRatingLayout;
	private IjoomerVoiceAndTextMessager voiceMessager;

	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder commentAdapter;
	private ArrayList<HashMap<String, String>> IN_CRITERIALIST;
	private ArrayList<HashMap<String, String>> IN_ARTICLE_DETAILS;
	private HashMap<String, String> IN_REVIEW_DETAILS;
	private ArrayList<HashMap<String, String>> IN_REVIEW_COMMENTS;

	private JReviewDataProvider dataProvider;
	private JReviewDataProvider reviewsDataProvider;

	@Override
	public int setLayoutId() {
		return R.layout.jreview_article_reviews_detail;
	}

	@Override
	public void initComponents() {
		getIntentData();

		dataProvider = new JReviewDataProvider(this);
		reviewsDataProvider = new JReviewDataProvider(this);
		androidQuery = new AQuery(this);
		iCaching = new IjoomerCaching(this);

		editReview = ((ImageView) getHeaderView().findViewById(R.id.imgeditReview));

		reviewDetailsHeaderLayout = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.jreview_article_reviews_details_header, null);
		lstReviewsComment = (ListView) findViewById(R.id.lstReviewsComment);
		lstReviewsComment.addHeaderView(reviewDetailsHeaderLayout);
		lstReviewsComment.setAdapter(null);

		//header views
		userimg = (ImageView) reviewDetailsHeaderLayout.findViewById(R.id.jreviewarticlereviewimg);
		txtUsername = (IjoomerTextView) reviewDetailsHeaderLayout.findViewById(R.id.jreviewarticlereviewUsername);
		txtReviewTitle = (IjoomerTextView) reviewDetailsHeaderLayout.findViewById(R.id.jreviewarticlereviewtitle);
		txtReviewIntro = (IjoomerTextView) reviewDetailsHeaderLayout.findViewById(R.id.jreviewarticlereviewdisc);
		txtReviewDate = (IjoomerTextView) reviewDetailsHeaderLayout.findViewById(R.id.jreviewarticlereviewDate);
		txtLikeCount = (IjoomerTextView) reviewDetailsHeaderLayout.findViewById(R.id.jreviewarticlereviewLikeCount);
		txtunlikeCount = (IjoomerTextView) reviewDetailsHeaderLayout.findViewById(R.id.jreviewarticlereviewDislikeCount);
		txtcommentCount = (IjoomerTextView) reviewDetailsHeaderLayout.findViewById(R.id.jreviewarticlereviewCommentCount);
		txtReviewReport = (IjoomerTextView) reviewDetailsHeaderLayout.findViewById(R.id.jreviewarticlereviewReport);
		txtAverageReviewCount = (IjoomerTextView) reviewDetailsHeaderLayout.findViewById(R.id.jreviewarticleaverageratingcount);
		txtComments = (IjoomerTextView) reviewDetailsHeaderLayout.findViewById(R.id.txtComments);
		averageReviewRatingbar = (IjoomerRatingBar) reviewDetailsHeaderLayout.findViewById(R.id.jreviewarticlereviewaverageratingBar);
		reviewsRatingLayout = (LinearLayout) reviewDetailsHeaderLayout.findViewById(R.id.jreviewarticleotherreviewsform);

		voiceMessager = (IjoomerVoiceAndTextMessager) findViewById(R.id.voiceMessager);
	}

	@Override
	protected void onResume() {
		super.onResume();
		applySideMenu("JReviewAllDirectoriesActivity");
	}

	@Override
	public void prepareViews() {
		if(IN_CAPTION!=null && IN_CAPTION.length()>0){
			((TextView) getHeaderView().findViewById(R.id.txtHeader)).setText(IN_CAPTION);
		}else{
			((TextView) getHeaderView().findViewById(R.id.txtHeader)).setText(getString(R.string.jreview_reviews_details));
		}

		if(IN_REVIEW_DETAILS.get(USERNAME).
				equalsIgnoreCase(getSmartApplication().readSharedPreferences().getString(SP_USERNAME, ""))){
			editReview.setVisibility(View.VISIBLE);
		}else{
			editReview.setVisibility(View.GONE);
		}

		if(IN_REVIEW_DETAILS.get(USERAVATAR).length() > 0){
			userimg.setVisibility(View.VISIBLE);
			androidQuery.id(userimg).image(IN_REVIEW_DETAILS.get(USERAVATAR), true, true, 0, R.drawable.jreview_default);
		}else{
			userimg.setVisibility(View.GONE);
		}

		txtUsername.setText(getString(R.string.jreview_reviewd_by)+" "+IN_REVIEW_DETAILS.get(USERNAME));
		txtReviewDate .setText(IN_REVIEW_DETAILS.get(CREATED));
		averageReviewRatingbar.setStarRating(Float.parseFloat(IN_REVIEW_DETAILS.get(AVERAGERATING)));
		txtAverageReviewCount.setText("("+IjoomerUtilities.parseFloat(IN_REVIEW_DETAILS.get(AVERAGERATING))+")");
		txtReviewTitle.setText(IN_REVIEW_DETAILS.get(TITLE));

		if(IN_REVIEW_DETAILS.get(COMMENTS)!=null && IN_REVIEW_DETAILS.get(COMMENTS).length()>0){
			txtReviewIntro.setVisibility(View.VISIBLE);
			txtReviewIntro.setText(IN_REVIEW_DETAILS.get(COMMENTS));
		}else{
			txtReviewIntro.setVisibility(View.GONE);
		}

		txtLikeCount.setText(IN_LIKE_CNT);
		txtunlikeCount.setText(IN_UNLIKE_CNT);
		txtcommentCount.setText(IN_REVIEW_DETAILS.get(COMMENTCOUNT));

		try{
			reviewsRatingLayout.removeAllViews();
			LayoutInflater inflater = LayoutInflater.from(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
			params.topMargin = 10;

			ArrayList<HashMap<String, String>> fields = iCaching.parseData(new JSONArray(IN_REVIEW_DETAILS.get("criteria")));
			int len = fields.size();
			for (int i = 0; i < len; i++) {
				final HashMap<String, String> field = fields.get(i);
				View ratingView = inflater.inflate(R.layout.jreview_dynamic_readonly_rating_view_item, null);
				((IjoomerTextView) ratingView.findViewById(R.id.txtLable)).setText(field.get("name"));
				((IjoomerRatingBar) ratingView.findViewById(R.id.jreviewarticlereviewratingBar)).setStarRating(IjoomerUtilities.parseFloat(field.get(RATINGVALUE)));
				((IjoomerTextView) ratingView.findViewById(R.id.jreviewarticlereviewrating)).setText("("+IjoomerUtilities.parseFloat(field.get(RATINGVALUE))+")");
				reviewsRatingLayout.addView(ratingView, params);
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		//get review comments
		getReviewComments();
	}

	@Override
	public void setActionListeners() {
		editReview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(getSmartApplication().readSharedPreferences().getString(SP_USERNAME, "").length() > 0){
					try{
						loadNew(JReviewArticleAddReviewActivity.class, JReviewArticleReviewsDetailActivity.this, true,
								ARTICLEID , IN_ARTICLEID, REVIEWID, IN_REVIEW_DETAILS.get(REVIEWID),"IN_REVIEWTITLE", IN_REVIEW_DETAILS.get(TITLE), 
								"IN_REVIEWCOMMENTS", IN_REVIEW_DETAILS.get(COMMENTS), 
								"IN_CRITERIALIST", IN_CRITERIALIST, "IS_EDIT", true);
					}catch(Exception e){
						e.printStackTrace();
					}
				}else{
					try{
						getSmartApplication().writeSharedPreferences(SP_DOLOGIN, true);
						loadNew(IjoomerLoginActivity.class, JReviewArticleReviewsDetailActivity.this, false);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		});

		txtLikeCount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (getSmartApplication().readSharedPreferences().getString(SP_USERNAME, "").length() > 0) {
					dataProvider.sendReviewVote("1", IN_REVIEW_DETAILS.get(REVIEWID), IN_ARTICLEID, new WebCallListener() {

						@Override
						public void onProgressUpdate(int progressCount) {

						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if(responseCode == 200){
								try{
									JSONObject result = (JSONObject) data2;
									txtLikeCount.setText(result.getString(LIKECOUNT));
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
						getSmartApplication().writeSharedPreferences(SP_DOLOGIN, true);
						loadNew(IjoomerLoginActivity.class, JReviewArticleReviewsDetailActivity.this, false);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

		txtunlikeCount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (getSmartApplication().readSharedPreferences().getString(SP_USERNAME, "").length() > 0) {
					dataProvider.sendReviewVote("0", IN_REVIEW_DETAILS.get(REVIEWID), IN_ARTICLEID, new WebCallListener() {

						@Override
						public void onProgressUpdate(int progressCount) {

						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if(responseCode == 200){
								try{
									JSONObject result = (JSONObject) data2;
									txtunlikeCount.setText(result.getString(DISLIKECOUNT));
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
						getSmartApplication().writeSharedPreferences(SP_DOLOGIN, true);
						loadNew(IjoomerLoginActivity.class, JReviewArticleReviewsDetailActivity.this, false);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

		voiceMessager.setMessageHandler(new MessageHandler() {

			@Override
			public void onVoiceMessageRecordingComplete(String message, String voiceMessagePath) {

			}

			@Override
			public void onButtonSend(String message) {
				if (getSmartApplication().readSharedPreferences().getString(SP_USERNAME, "").length() > 0) {
					if(message.length() > 0){
						final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_addcomment));
						reviewsDataProvider.addreviewComment(IN_REVIEW_DETAILS.get(REVIEWID) ,IN_ARTICLEID, message, new WebCallListener() {

							@Override
							public void onProgressUpdate(int progressCount) {
								proSeekBar.setProgress(progressCount);
							}

							@Override
							public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								try {
									if (responseCode == 200) {
										JSONObject result = (JSONObject) data2;

										getSmartApplication().writeSharedPreferences(SP_RELOADREVIEWS, true);

										//update reviews and article details related it 
										iCaching.updateTable("UPDATE jreview_articles SET overallAverageRating = '"+result.getString(OVERALLAVERAGERATING)+"' WHERE articleID="+IN_ARTICLEID);
										iCaching.updateTable("UPDATE jreview_articles SET userRatingCount = '"+result.getString(USERRATINGCOUNT)+"' WHERE articleID="+IN_ARTICLEID);
										iCaching.updateTable("UPDATE jreview_articles SET averageReviewCriteria = '"+result.getString(AVERAGEREVIEWCRITERIA)+"' WHERE articleID="+IN_ARTICLEID);
										iCaching.updateTable("UPDATE jreview_articles SET reviews = '"+result.getString(REVIEWS)+"' WHERE articleID="+IN_ARTICLEID);

										//update review comments
										iCaching.updateTable("UPDATE jreview_articles SET reviewComment = '"+result.getString(REVIEWCOMMENT)+"' WHERE articleID="+IN_ARTICLEID);

										//get updated review comments
										getReviewComments();
										
										txtcommentCount.setText(String.valueOf(Integer.parseInt(txtcommentCount.getText().toString()) + 1));
									} else {
										IjoomerUtilities.getCustomOkDialog(getString(R.string.jreview_review), getString(getResources()
												.getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok), R.layout.ijoomer_ok_dialog,
												new CustomAlertNeutral() {

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
				} else {
					try {
						getSmartApplication().writeSharedPreferences(SP_DOLOGIN, true);
						loadNew(IjoomerLoginActivity.class, JReviewArticleReviewsDetailActivity.this, false);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onToggle(int messager) {

			}
		});

		txtReviewReport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				IjoomerUtilities.getJreviewArticleVideosReportDialog(new ReportListner() {

					@Override
					public void onClick(String repotType, String message) {
						tingOnUI("Under Construction!!!!");
					}
				});
			}
		});
	}

	/**
	 * Class methods
	 */

	/**
	 * This method used to get intent data.
	 */
	@SuppressWarnings("unchecked")
	private void getIntentData() {
		IN_CATEGORYID = getIntent().getStringExtra(CATEGORY_ID) == null ? "0" : getIntent().getStringExtra(CATEGORY_ID);
		IN_ARTICLEID = getIntent().getStringExtra(ARTICLEID) == null ? "0" : getIntent().getStringExtra(ARTICLEID);
		IN_CAPTION = getIntent().getStringExtra("IN_CAPTION") == null ? "" : getIntent().getStringExtra("IN_CAPTION");
		IN_LIKE_CNT = getIntent().getStringExtra("IN_REVIEW_LIKE") == null ? "0" : getIntent().getStringExtra("IN_REVIEW_LIKE");
		IN_UNLIKE_CNT = getIntent().getStringExtra("IN_REVIEW_UNLIKE") == null ? "0" : getIntent().getStringExtra("IN_REVIEW_UNLIKE");
		IN_CRITERIALIST = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("IN_CRITERIALIST");
		IN_REVIEW_DETAILS = (HashMap<String, String>) getIntent().getSerializableExtra("IN_REVIEW_DETAILS");
	}

	private void getReviewComments(){
		try{
			//get article review comments
			IN_ARTICLE_DETAILS = dataProvider.getArticleReviewsComments(IN_CATEGORYID, IN_ARTICLEID);
			if(IN_ARTICLE_DETAILS.get(0).get(REVIEWCOMMENT)!=null 
					&& IN_ARTICLE_DETAILS.get(0).get(REVIEWCOMMENT).length()>0){
				IN_REVIEW_COMMENTS = iCaching.parseData(new JSONArray(IN_ARTICLE_DETAILS.get(0).get(REVIEWCOMMENT)));
				if(IN_REVIEW_COMMENTS!=null && IN_REVIEW_COMMENTS.size()>0){
					txtComments.setText(getString(R.string.jreview_comments_title));

					//prepare article reviews comments list
					prepareList(IN_REVIEW_COMMENTS,false);
					commentAdapter = getListAdapter();
					lstReviewsComment.setAdapter(commentAdapter);
				}else{
					txtComments.setText(getString(R.string.jreview_no_comments));
				}
			}else{
				txtComments.setText(getString(R.string.jreview_no_comments));
				lstReviewsComment.setAdapter(null);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * This method used to prepare list review comment.
	 * 
	 * @param data
	 *            represented review comment data
	 * @param append
	 *            represented data append
	 */
	public void prepareList(ArrayList<HashMap<String, String>> data, boolean append) {
		if (data != null) {
			if (!append) {
				listData.clear();
			}
			for (HashMap<String, String> hashMap : data) {
				if(hashMap.get(REVIEWID).equals(IN_REVIEW_DETAILS.get(REVIEWID))){
					SmartListItem item = new SmartListItem();
					item.setItemLayout(R.layout.jreview_article_reviews_comments_listitem);
					ArrayList<Object> obj = new ArrayList<Object>();
					obj.add(hashMap);
					item.setValues(obj);
					if (append) {
						commentAdapter.add(item);
					} else {
						listData.add(item);
					}
				}
			}
		}
	}

	/**
	 * List adapter for review comments.
	 */

	private SmartListAdapterWithHolder getListAdapter() {
		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(JReviewArticleReviewsDetailActivity.this, R.layout.jreview_article_reviews_comments_listitem, listData, new ItemView() {
			@Override
			public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {
				holder.jreviewreviewimgCommentUserAvatar = (ImageView) v.findViewById(R.id.imgCommentUserAvatar);
				holder.jreviewreviewtxtCommentUserName = (IjoomerTextView) v.findViewById(R.id.txtCommentUserName);
				holder.jreviewreviewtxtCommentDate = (IjoomerTextView) v.findViewById(R.id.txtCommentDate);
				holder.jreviewreviewtxtCommentTitle = (IjoomerTextView) v.findViewById(R.id.txtCommentTitle);
				holder.jreviewreviewbtnCommentRemove = (IjoomerButton) v.findViewById(R.id.btnCommentRemove);

				@SuppressWarnings("unchecked")
				final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);
				if(row.get(USERAVATAR).length() > 0){
					holder.jreviewreviewimgCommentUserAvatar.setVisibility(View.VISIBLE);
					androidQuery.id(holder.jreviewreviewimgCommentUserAvatar).image(row.get(USERAVATAR), true, true, getDeviceWidth(), 0);
				}else{
					holder.jreviewreviewimgCommentUserAvatar.setVisibility(View.GONE);
				}
				holder.jreviewreviewtxtCommentUserName.setText(row.get(USERNAME));
				holder.jreviewreviewtxtCommentDate.setText(row.get(CREATED));
				holder.jreviewreviewtxtCommentTitle.setText(row.get(TEXT));

				if(getSmartApplication().readSharedPreferences().getString(SP_USERNAME, "").length() > 0){
					if(getSmartApplication().readSharedPreferences().getString(SP_USERNAME, "").equalsIgnoreCase(row.get(USERNAME))){
						holder.jreviewreviewbtnCommentRemove.setVisibility(View.VISIBLE);
					}else{
						holder.jreviewreviewbtnCommentRemove.setVisibility(View.GONE);
					}
				}

				holder.jreviewreviewbtnCommentRemove.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						IjoomerUtilities.getCustomConfirmDialog(getString(R.string.jreview_review_comment_remove), getString(R.string.are_you_sure), getString(R.string.yes), getString(R.string.no),
								new CustomAlertMagnatic() {

							@Override
							public void PositiveMethod() {
								removeReviewComment(row.get(REVIEWCOMMENTID),row.get(REVIEWID));
							}

							@Override
							public void NegativeMethod() {

							}
						});
					}
				});

				return v;
			}

			@Override
			public View setItemView(int position, View v, SmartListItem item) {
				return null;
			}
		});
		return adapterWithHolder;
	}

	private void removeReviewComment(final String commentID,final String reviewID){
		final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_removecomment));
		reviewsDataProvider.removereviewComment(commentID, reviewID, IN_ARTICLEID, new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {
				proSeekBar.setProgress(progressCount);
			}

			@Override
			public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				try {
					if (responseCode == 200) {
						JSONObject result = (JSONObject) data2;

						getSmartApplication().writeSharedPreferences(SP_RELOADREVIEWS, true);

						//update reviews and article details related it 
						iCaching.updateTable("UPDATE jreview_articles SET overallAverageRating = '"+result.getString(OVERALLAVERAGERATING)+"' WHERE articleID="+IN_ARTICLEID);
						iCaching.updateTable("UPDATE jreview_articles SET userRatingCount = '"+result.getString(USERRATINGCOUNT)+"' WHERE articleID="+IN_ARTICLEID);
						iCaching.updateTable("UPDATE jreview_articles SET averageReviewCriteria = '"+result.getString(AVERAGEREVIEWCRITERIA)+"' WHERE articleID="+IN_ARTICLEID);
						iCaching.updateTable("UPDATE jreview_articles SET reviews = '"+result.getString(REVIEWS)+"' WHERE articleID="+IN_ARTICLEID);

						//update review comments
						iCaching.updateTable("UPDATE jreview_articles SET reviewComment = '"+result.getString(REVIEWCOMMENT)+"' WHERE articleID="+IN_ARTICLEID);

						System.out.println("DATA"+result.getString(REVIEWCOMMENT));
						//get updated review comments
						getReviewComments();
					} else {
						IjoomerUtilities.getCustomOkDialog(getString(R.string.jreview_review), getString(getResources()
								.getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok), R.layout.ijoomer_ok_dialog,
								new CustomAlertNeutral() {

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
	 * This method used to shown response message.
	 * 
	 * @param responseCode
	 *            represented response code
	 * @param finishActivityOnConnectionProblem
	 *            represented finish activity on connection problem
	 */
	private void responseErrorMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {
		IjoomerUtilities.getCustomOkDialog(getString(R.string.jreview_review), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())),
				getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

			@Override
			public void NeutralMethod() {

			}
		});
	}
}
