package com.ijoomer.components.k2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ijoomer.common.classes.IjoomerShareActivity;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.custom.interfaces.IjoomerSharedPreferences;
import com.ijoomer.customviews.IjoomerRatingBar;
import com.ijoomer.customviews.IjoomerRatingBar.RatingHandler;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.k2.k2MainDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Fragment Contains All Method Related To k2ItemsDetailFragment.
 * 
 * @author tasol
 * 
 */
@SuppressLint("ValidFragment")
public class k2ItemsDetailFragment extends SmartFragment implements K2TagHolder, IjoomerSharedPreferences {

	private IjoomerTextView txtItemTitle;
	private IjoomerTextView txtItemCreatedBy;
	private IjoomerTextView txtItemDescription;
	private IjoomerTextView txtItemRatingVote;
	private IjoomerRatingBar rtbItem;
	private ImageView imgSubmitRating;
	private ImageView imgCancelRating;
	private ImageView imgItem;
	private ImageView imgItemShare;
	private ProgressBar pbrItemDetail;
    private ProgressBar pbrImage;

	private HashMap<String, String> itemData;
	private k2MainDataProvider provider;
	private AQuery androidQuery;

	private String IN_MENUID;

	/**
	 * Constructor
	 * 
	 * @param mContext
	 *            represented {@link Context}
	 * @param itemData
	 *            represented item detail data
	 */
	public k2ItemsDetailFragment(Context mContext, HashMap<String, String> itemData) {
		this.itemData = itemData;
	}

	/**
	 * Overrides methods
	 */
	@Override
	public int setLayoutId() {
		return R.layout.k2_item_details_fragment;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents(View currentView) {
		txtItemTitle = (IjoomerTextView) currentView.findViewById(R.id.txtItemTitle);
		txtItemCreatedBy = (IjoomerTextView) currentView.findViewById(R.id.txtItemCreatedBy);
		txtItemDescription = (IjoomerTextView) currentView.findViewById(R.id.txtItemDescription);
		txtItemRatingVote = (IjoomerTextView) currentView.findViewById(R.id.txtItemRatingVote);
		rtbItem = (IjoomerRatingBar) currentView.findViewById(R.id.rtbItem);
		imgItem = (ImageView) currentView.findViewById(R.id.imgItem);
		imgCancelRating = (ImageView) currentView.findViewById(R.id.imgCancelRating);
		imgSubmitRating = (ImageView) currentView.findViewById(R.id.imgSubmitRating);
		imgItemShare = (ImageView) currentView.findViewById(R.id.imgItemShare);
		pbrItemDetail = (ProgressBar) currentView.findViewById(R.id.pbrItemDetail);
        pbrImage = (ProgressBar) currentView.findViewById(R.id.pbrImage);

		androidQuery = new AQuery(getActivity());
		provider = new k2MainDataProvider(getActivity());
		getIntentData();
	}

	private void getIntentData() {
		IN_MENUID = getActivity().getIntent().getStringExtra("IN_MENUID") == null ? "0" : getActivity().getIntent().getStringExtra("IN_MENUID");
	}

	@Override
	public void prepareViews(View currentView) {
        rtbItem.setStarBgColor(getResources().getColor(R.color.k2_orange));
		prepareItemDetail();

	}

	@Override
	public void setActionListeners(View currentView) {

		imgItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					@SuppressWarnings("unused")
					JSONArray imageGallerries = new JSONArray(itemData.get(IMAGEGALLERIES));
					((SmartActivity) getActivity()).loadNew(K2GalleryActivity.class, getActivity(), false, "IN_PHOTOS_PATHS", itemData.get(IMAGEGALLERIES));
				} catch (Throwable e) {

				}
			}
		});

		rtbItem.setRatingHandler(new RatingHandler() {

			@Override
			public void onRatingChangedListener(float rating) {
				imgCancelRating.setVisibility(View.VISIBLE);
				imgSubmitRating.setVisibility(View.VISIBLE);
				txtItemRatingVote.setVisibility(View.GONE);

			}
		});

		imgCancelRating.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imgCancelRating.setVisibility(View.GONE);
				imgSubmitRating.setVisibility(View.GONE);
				txtItemRatingVote.setVisibility(View.VISIBLE);
				try {
					JSONObject rating = new JSONObject(itemData.get(RATINGS));
					if (Integer.parseInt(rating.getString(RATINGCOUNT)) > 1) {
						rtbItem.setStarRating((Float.parseFloat(rating.getString(RATINGSUM)) / Float.parseFloat(rating.getString(RATINGCOUNT))));
						txtItemRatingVote.setText(String.format(getActivity().getString(R.string.k2_rating_votes), Integer.parseInt(rating.getString(RATINGCOUNT))));
					} else {
						rtbItem.setStarRating(Float.parseFloat(rating.getString(RATINGSUM)));
						txtItemRatingVote.setText(String.format(getActivity().getString(R.string.k2_rating_vote), Integer.parseInt(rating.getString(RATINGCOUNT))));
					}
				} catch (Throwable e) {
					txtItemRatingVote.setText(String.format(getActivity().getString(R.string.k2_rating_vote), 0));
					rtbItem.setStarRating(0);
				}

			}
		});

		imgSubmitRating.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (rtbItem.getStarRating() > 0) {
					final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
					provider.rating(String.valueOf(rtbItem.getStarRating()), IN_MENUID, itemData.get(ID), new WebCallListener() {

						@Override
						public void onProgressUpdate(int progressCount) {
							proSeekBar.setProgress(progressCount);
						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							try {
								if (responseCode == 200) {
									try {
										JSONObject rating = (JSONObject) data2;
										itemData.put(RATINGS, rating.toString());
										if (Integer.parseInt(rating.getString(RATINGCOUNT)) > 1) {
											rtbItem.setStarRating((Float.parseFloat(rating.getString(RATINGSUM)) / Float.parseFloat(rating.getString(RATINGCOUNT))));
											txtItemRatingVote.setText(String.format(getActivity().getString(R.string.k2_rating_votes), Integer.parseInt(rating.getString(RATINGCOUNT))));
										} else {
											rtbItem.setStarRating(Float.parseFloat(rating.getString(RATINGSUM)));
											txtItemRatingVote.setText(String.format(getActivity().getString(R.string.k2_rating_vote), Integer.parseInt(rating.getString(RATINGCOUNT))));
										}
									} catch (Throwable e) {
										txtItemRatingVote.setText(String.format(getActivity().getString(R.string.k2_rating_vote), 0));
										rtbItem.setStarRating(0);
									}

								} else {
									IjoomerUtilities.getCustomOkDialog(getActivity().getString(R.string.dialog_k2_item_details), errorMessage, getActivity().getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

										@Override
										public void NeutralMethod() {
											try {
												JSONObject rating = new JSONObject(itemData.get(RATINGS));
												if (Integer.parseInt(rating.getString(RATINGCOUNT)) > 1) {
													rtbItem.setStarRating((Float.parseFloat(rating.getString(RATINGSUM)) / Float.parseFloat(rating.getString(RATINGCOUNT))));
													txtItemRatingVote.setText(String.format(getActivity().getString(R.string.k2_rating_votes), Integer.parseInt(rating.getString(RATINGCOUNT))));
												} else {
													rtbItem.setStarRating(Float.parseFloat(rating.getString(RATINGSUM)));
													rtbItem.setStarRating((Float.parseFloat(rating.getString(RATINGSUM)) / Float.parseFloat(rating.getString(RATINGCOUNT))));
												}
											} catch (Throwable e) {
												txtItemRatingVote.setText(String.format(getActivity().getString(R.string.k2_rating_vote), 0));
												rtbItem.setStarRating(0);
											}
										}
									});
								}
							} catch (Throwable e) {

							}

						}
					});
				}

				imgCancelRating.setVisibility(View.GONE);
				imgSubmitRating.setVisibility(View.GONE);
				txtItemRatingVote.setVisibility(View.VISIBLE);

			}
		});

		imgItemShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {

					((SmartActivity) getActivity()).loadNewResult(IjoomerShareActivity.class, getActivity(), 0, "IN_SHARE_CAPTION", itemData.get(TITLE), "IN_SHARE_DESCRIPTION", itemData.get(INTROTEXT), "IN_SHARE_THUMB", itemData.get(IMAGESMALL),
							"IN_SHARE_SHARELINK", itemData.get(SHARELINK));
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
	 * This method used to prepare item details.
	 */
	private void prepareItemDetail() {
		pbrItemDetail.setVisibility(View.VISIBLE);

        pbrImage.setVisibility(View.VISIBLE);
        androidQuery.ajax(itemData.get(IMAGESLARGE), Bitmap.class, 0,
                new AjaxCallback<Bitmap>() {
                    @Override
                    public void callback(String url, Bitmap object,
                                         AjaxStatus status) {
                        super.callback(url, object, status);
                        if(status.getCode()==200){
                            imgItem.setImageBitmap(object);
                        }else{
                            imgItem.setImageDrawable(getResources().getDrawable(R.drawable.k2_default));
                        }
                        pbrImage.setVisibility(View.GONE);
                    }
                });
		txtItemTitle.setText(itemData.get(TITLE));
		txtItemCreatedBy.setText(String.format(getActivity().getString(R.string.by), ": " + itemData.get(CREATEDBYNAME)));
		txtItemDescription.setText(Html.fromHtml(itemData.get(INTROTEXT)));
		try {
			JSONObject rating = new JSONObject(itemData.get(RATINGS));
			if (Integer.parseInt(rating.getString(RATINGCOUNT)) > 1) {
				rtbItem.setStarRating((Float.parseFloat(rating.getString(RATINGSUM)) / Float.parseFloat(rating.getString(RATINGCOUNT))));
				txtItemRatingVote.setText(String.format(getActivity().getString(R.string.k2_rating_votes), Integer.parseInt(rating.getString(RATINGCOUNT))));
			} else {
				rtbItem.setStarRating(Float.parseFloat(rating.getString(RATINGSUM)));
				txtItemRatingVote.setText(String.format(getActivity().getString(R.string.k2_rating_vote), Integer.parseInt(rating.getString(RATINGCOUNT))));
			}
		} catch (Throwable e) {
			txtItemRatingVote.setText(String.format(getActivity().getString(R.string.k2_rating_vote), 0));
			rtbItem.setStarRating(0);
		}

		pbrItemDetail.setVisibility(View.GONE);

	}

}
