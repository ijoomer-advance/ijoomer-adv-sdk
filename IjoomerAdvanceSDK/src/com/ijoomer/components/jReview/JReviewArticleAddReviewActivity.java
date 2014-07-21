package com.ijoomer.components.jReview;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerRatingBar;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.jReview.JReviewDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;

public class JReviewArticleAddReviewActivity extends JReviewMasterActivity{

	private boolean ISEDIT;
	private String IN_REVIEWID;
	private String IN_ARTICLEID;
	private String IN_REVIEWTITLE;
	private String IN_REVIEWCOMMENTS;

	private IjoomerTextView txtHeaderTitle;
	private IjoomerEditText edtReviewTitle;
	private IjoomerEditText edtReviewComments;
	private LinearLayout reviewsCriteria;
	private IjoomerButton btnCancel;
	private IjoomerButton btnCreate;
	private LayoutInflater iLayoutInflater;

	private ArrayList<HashMap<String, String>> IN_CRITERIALIST;
	private IjoomerCaching iCaching;
	private JReviewDataProvider dataProvider;

	@Override
	public int setLayoutId() {
		return R.layout.jreview_article_add_review;
	}

	@Override
	public void initComponents() {
		getIntentData();

		iCaching = new IjoomerCaching(this);
		dataProvider = new JReviewDataProvider(this);
		iLayoutInflater = LayoutInflater.from(this);

		txtHeaderTitle = ((IjoomerTextView) getHeaderView().findViewById(R.id.txtHeader));
		edtReviewTitle = (IjoomerEditText) findViewById(R.id.jreviewarticlereviewtitletxt);
		edtReviewComments = (IjoomerEditText) findViewById(R.id.jreviewarticlereviewcommenttxt);
		btnCancel = (IjoomerButton) findViewById(R.id.btnCancel);
		btnCreate = (IjoomerButton) findViewById(R.id.btnCreate);
		reviewsCriteria = (LinearLayout) findViewById(R.id.lnr_reviews_critaria);
	}

	@Override
	public void prepareViews() {
		if(ISEDIT){
			txtHeaderTitle.setText(getString(R.string.jreview_edit_review));
		}else{
			txtHeaderTitle.setText(getString(R.string.jreview_add_review));
		}
		edtReviewTitle.setText(IN_REVIEWTITLE);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
		params.topMargin = 10;

		int len = IN_CRITERIALIST.size();
		if(ISEDIT){
			for (int i = 0; i < len; i++) {
				final HashMap<String, String> field = IN_CRITERIALIST.get(i);
				View ratingView = iLayoutInflater.inflate(R.layout.jreview_dynamic_rating_view_item, null);
				((IjoomerTextView) ratingView.findViewById(R.id.txtLable)).setText(field.get(NAME));
				((IjoomerRatingBar) ratingView.findViewById(R.id.jreviewarticlereviewratingBar)).setStarRating(IjoomerUtilities.parseFloat(field.get(RATINGVALUE)));
				ratingView.setTag(field);
				reviewsCriteria.addView(ratingView, params);
			}
		}else{
			for (int i = 0; i < len; i++) {
				final HashMap<String, String> field = IN_CRITERIALIST.get(i);
				View ratingView = iLayoutInflater.inflate(R.layout.jreview_dynamic_rating_view_item, null);
				if(field.get(REVIEWREQUIRED).equalsIgnoreCase("1")){
					((IjoomerTextView) ratingView.findViewById(R.id.txtLable)).setText(field.get(NAME)+" *");
				}else{
					((IjoomerTextView) ratingView.findViewById(R.id.txtLable)).setText(field.get(NAME));
				}
				ratingView.setTag(field);
				reviewsCriteria.addView(ratingView, params);
			}
		}
		edtReviewComments.setText(IN_REVIEWCOMMENTS);
	}

	@Override
	public void setActionListeners() {
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		btnCreate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideSoftKeyboard();
				saveReview();
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void getIntentData(){
		ISEDIT = getIntent().getBooleanExtra("IS_EDIT", false);
		IN_REVIEWID = getIntent().getStringExtra(REVIEWID) == null ? "0" : getIntent().getStringExtra(REVIEWID);
		IN_ARTICLEID = getIntent().getStringExtra(ARTICLEID) == null ? "0" : getIntent().getStringExtra(ARTICLEID);
		IN_REVIEWTITLE = getIntent().getStringExtra("IN_REVIEWTITLE") == null ? "" : getIntent().getStringExtra("IN_REVIEWTITLE");
		IN_REVIEWCOMMENTS = getIntent().getStringExtra("IN_REVIEWCOMMENTS") == null ? "" : getIntent().getStringExtra("IN_REVIEWCOMMENTS");
		IN_CRITERIALIST = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("IN_CRITERIALIST");
	}

	@SuppressWarnings("unchecked")
	private void saveReview() {
		String ratings = "";

		boolean validationFlag = true;

		if(edtReviewTitle.getText().toString().length() <= 0){
			edtReviewTitle.setError(getString(R.string.validation_value_required));
			validationFlag = false;
		}

		if(edtReviewComments.getText().toString().length() <= 0){
			edtReviewComments.setError(getString(R.string.validation_value_required));
			validationFlag = false;
		}

		StringBuilder ratingsBuider = new StringBuilder();
		int size = reviewsCriteria.getChildCount();
		for (int i = 0; i < size; i++) {
			View v = (LinearLayout) reviewsCriteria.getChildAt(i);
			HashMap<String, String> field = new HashMap<String, String>();
			field.putAll((HashMap<String, String>) v.getTag());
			IjoomerRatingBar ratingbar = (IjoomerRatingBar) v.findViewById(R.id.jreviewarticlereviewratingBar);
			if(field.get(REVIEWREQUIRED).equalsIgnoreCase("1")){
				if(ratingbar.getStarRating() <= 0){
					validationFlag = false;
				}
			}
			ratingsBuider.append(String.valueOf(ratingbar.getStarRating()));
			ratingsBuider.append(",");
		}
		if(ratingsBuider.toString().length() > 0){
			ratings = ratingsBuider.toString().substring(0, ratingsBuider.toString().length()-1);
		}else{
			ratings = "";
		}

		if (validationFlag) {
			final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_addreview));

			dataProvider.addreview(IN_REVIEWID ,IN_ARTICLEID, edtReviewTitle.getText().toString(), edtReviewComments.getText().toString(), ratings, new WebCallListener() {

				@Override
				public void onProgressUpdate(int progressCount) {
					proSeekBar.setProgress(progressCount);
				}

				@Override
				public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
					try {
						if (responseCode == 200) {
							JSONObject result = (JSONObject) data2;

							iCaching.updateTable("UPDATE jreview_articles SET overallAverageRating = '"+result.getString(OVERALLAVERAGERATING)+"' WHERE articleID = "+IN_ARTICLEID);
							iCaching.updateTable("UPDATE jreview_articles SET userRatingCount = '"+result.getString(USERRATINGCOUNT)+"' WHERE articleID = "+IN_ARTICLEID);
							iCaching.updateTable("UPDATE jreview_articles SET averageReviewCriteria = '"+result.getString(AVERAGEREVIEWCRITERIA)+"' WHERE articleID = "+IN_ARTICLEID);
							iCaching.updateTable("UPDATE jreview_articles SET reviews = '"+result.getString(REVIEWS)+"' WHERE articleID = "+IN_ARTICLEID);

							IjoomerUtilities.getCustomOkDialog(getString(R.string.jreview_review), getString(R.string.jreview_review_added_successfully), 
									getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

								@Override
								public void NeutralMethod() {
									getSmartApplication().writeSharedPreferences(SP_RELOADREVIEWS, true);
									getSmartApplication().writeSharedPreferences(SP_RELOADARTICLEDETAILS, true);
									getSmartApplication().writeSharedPreferences(SP_RELOADARTICLES, true);
									finish();
								}
							});
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
	}

}
