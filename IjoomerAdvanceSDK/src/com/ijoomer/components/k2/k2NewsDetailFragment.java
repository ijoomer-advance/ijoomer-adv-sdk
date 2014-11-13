package com.ijoomer.components.k2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ijoomer.common.classes.IJoomerGooglePlusShareActivity;
import com.ijoomer.common.classes.IJoomerMailShareActivity;
import com.ijoomer.common.classes.IJoomerTwitterShareActivity;
import com.ijoomer.common.classes.IjoomerFacebookSharingActivity;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.IjoomerWebviewClient;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.custom.interfaces.IjoomerSharedPreferences;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerListView;
import com.ijoomer.customviews.IjoomerRadioButton;
import com.ijoomer.customviews.IjoomerRatingBar;
import com.ijoomer.customviews.IjoomerRatingBar.RatingHandler;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.k2.k2MainDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Fragment Contains All Method Related To k2NewsDetailFragment.
 * 
 * @author tasol
 * 
 */
@SuppressLint("ValidFragment")
public class k2NewsDetailFragment extends SmartFragment implements K2TagHolder, IjoomerSharedPreferences {

	private LinearLayout lnrItemShare;
	private IjoomerListView lstItemComment;
	private IjoomerTextView txtItemTitle;
	private IjoomerTextView txtItemCreatedBy;
	private IjoomerTextView txtItemDescription;
	private IjoomerTextView txtItemNoComment;
	private IjoomerTextView txtItemRatingVote;
	private IjoomerEditText edtCommentMessage;
	private IjoomerEditText edtCommentUserName;
	private IjoomerEditText edtCommentEmail;
	private IjoomerEditText edtCommentSiteUrl;
	private IjoomerButton btnCommentSubmit;
	private RadioGroup rdgShareComment;
	private IjoomerRadioButton rdbtItemShare;
	private IjoomerRatingBar rtbItem;
	private ProgressBar pbrItemDetail;
	private ImageView imgSubmitRating;
	private ImageView imgCancelRating;
	private ImageView imgItem;
	private ImageView imgItemShareFacebook;
	private ImageView imgItemShareTwitter;
	private ImageView imgItemShareGoogleplus;
	private ImageView imgItemShareMail;
	private ViewGroup commentHeader;
	private LinearLayout lnrWriteComment;
    private ProgressBar pbrImage;

	private HashMap<String, String> itemData;
	private ArrayList<SmartListItem> listData;
	private k2MainDataProvider provider;
	private AQuery androidQuery;
	private SmartListAdapterWithHolder commentAdapterWithHolder;
	private String IN_MENUID;

	/**
	 * Constructor
	 * 
	 * @param mContext
	 *            represented {@link Context}
	 * @param itemData
	 *            represented item detail data
	 */
	public k2NewsDetailFragment(Context mContext, HashMap<String, String> itemData) {
		this.itemData = itemData;
	}

	/**
	 * Overrides methods
	 */
	@Override
	public int setLayoutId() {
		return R.layout.k2_news_details_fragment;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents(View currentView) {

		commentHeader = (ViewGroup) LayoutInflater.from(getActivity()).inflate(R.layout.k2_news_item_comment_list_header, null);
		lnrItemShare = (LinearLayout) commentHeader.findViewById(R.id.lnrItemShare);
		lstItemComment = (IjoomerListView) currentView.findViewById(R.id.lstItemComment);
		lstItemComment.addHeaderView(commentHeader);
		lstItemComment.setAdapter(null);

		lnrWriteComment = (LinearLayout) commentHeader.findViewById(R.id.lnrWriteComment);
		txtItemTitle = (IjoomerTextView) commentHeader.findViewById(R.id.txtItemTitle);
		txtItemCreatedBy = (IjoomerTextView) commentHeader.findViewById(R.id.txtItemCreatedBy);
		txtItemNoComment = (IjoomerTextView) commentHeader.findViewById(R.id.txtItemNoComment);
		txtItemDescription = (IjoomerTextView) commentHeader.findViewById(R.id.txtItemDescription);
		txtItemRatingVote = (IjoomerTextView) commentHeader.findViewById(R.id.txtItemRatingVote);
		edtCommentEmail = (IjoomerEditText) commentHeader.findViewById(R.id.edtCommentEmail);
		edtCommentMessage = (IjoomerEditText) commentHeader.findViewById(R.id.edtCommentMessage);
		edtCommentSiteUrl = (IjoomerEditText) commentHeader.findViewById(R.id.edtCommentSiteUrl);
		edtCommentUserName = (IjoomerEditText) commentHeader.findViewById(R.id.edtCommentUserName);
		btnCommentSubmit = (IjoomerButton) commentHeader.findViewById(R.id.btnCommentSubmit);
		rtbItem = (IjoomerRatingBar) commentHeader.findViewById(R.id.rtbItem);
		rdbtItemShare = (IjoomerRadioButton) commentHeader.findViewById(R.id.rdbtItemShare);
		rdgShareComment = (RadioGroup) commentHeader.findViewById(R.id.rdgShareComment);
		imgItem = (ImageView) commentHeader.findViewById(R.id.imgItem);
		imgCancelRating = (ImageView) commentHeader.findViewById(R.id.imgCancelRating);
		imgSubmitRating = (ImageView) commentHeader.findViewById(R.id.imgSubmitRating);
		imgItemShareFacebook = (ImageView) commentHeader.findViewById(R.id.imgItemShareFacebook);
		imgItemShareTwitter = (ImageView) commentHeader.findViewById(R.id.imgItemShareTwitter);
		imgItemShareGoogleplus = (ImageView) commentHeader.findViewById(R.id.imgItemShareGoogleplus);
		imgItemShareMail = (ImageView) commentHeader.findViewById(R.id.imgItemShareMail);
		pbrItemDetail = (ProgressBar) currentView.findViewById(R.id.pbrItemDetail);
        pbrImage = (ProgressBar) commentHeader.findViewById(R.id.pbrImage);

		listData = new ArrayList<SmartListItem>();
		androidQuery = new AQuery(getActivity());
		provider = new k2MainDataProvider(getActivity());
		getIntentData();
	}

	public void prepareViews(View currentView) {
		rdbtItemShare.setChecked(true);
        rtbItem.setStarBgColor(getResources().getColor(R.color.k2_orange));
		edtCommentEmail.setText("");
		edtCommentMessage.setText("");
		edtCommentSiteUrl.setText("");
		edtCommentUserName.setText("");
		edtCommentEmail.setError(null);
		edtCommentMessage.setError(null);
		edtCommentUserName.setError(null);
		commentAdapterWithHolder = getListAdapter();
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

		rdgShareComment.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int id) {
				if (R.id.rdbtItemShare == id) {
					lnrItemShare.setVisibility(View.VISIBLE);
					lnrWriteComment.setVisibility(View.GONE);
					txtItemNoComment.setVisibility(View.GONE);
					lstItemComment.setAdapter(null);

				} else {
					lstItemComment.setAdapter(commentAdapterWithHolder);
					lstItemComment.setSelection(1);
					lnrItemShare.setVisibility(View.GONE);
					if (IjoomerGlobalConfiguration.isEnableCommentK2()) {
						lnrWriteComment.setVisibility(View.VISIBLE);
					} else {
						lnrWriteComment.setVisibility(View.GONE);
					}
					if (listData.size() <= 0) {
						if (IjoomerGlobalConfiguration.isEnableCommentK2()) {
							txtItemNoComment.setVisibility(View.VISIBLE);
							txtItemNoComment.setText(getActivity().getString(R.string.k2_be_the_first_to_comment));
						} else {
							txtItemNoComment.setVisibility(View.VISIBLE);
							txtItemNoComment.setText(getActivity().getString(R.string.k2_no_comment));
						}
					}
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
											txtItemRatingVote.setText(String.format(getActivity().getString(R.string.k2_rating_votes),
													Integer.parseInt(rating.getString(RATINGCOUNT))));
										} else {
											rtbItem.setStarRating(Float.parseFloat(rating.getString(RATINGSUM)));
											txtItemRatingVote.setText(String.format(getActivity().getString(R.string.k2_rating_vote),
													Integer.parseInt(rating.getString(RATINGCOUNT))));
										}
									} catch (Throwable e) {
										txtItemRatingVote.setText(String.format(getActivity().getString(R.string.k2_rating_vote), 0));
										rtbItem.setStarRating(0);
									}

								} else {
									IjoomerUtilities.getCustomOkDialog(getActivity().getString(R.string.dialog_k2_item_details), errorMessage,
											getActivity().getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

												@Override
												public void NeutralMethod() {
													try {
														JSONObject rating = new JSONObject(itemData.get(RATINGS));
														if (Integer.parseInt(rating.getString(RATINGCOUNT)) > 1) {
															rtbItem.setStarRating((Float.parseFloat(rating.getString(RATINGSUM)) / Float.parseFloat(rating.getString(RATINGCOUNT))));
															txtItemRatingVote.setText(String.format(getActivity().getString(R.string.k2_rating_votes),
																	Integer.parseInt(rating.getString(RATINGCOUNT))));
														} else {
															rtbItem.setStarRating(Float.parseFloat(rating.getString(RATINGSUM)));
															txtItemRatingVote.setText(String.format(getActivity().getString(R.string.k2_rating_votes),
																	Integer.parseInt(rating.getString(RATINGCOUNT))));
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

		btnCommentSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				boolean validation = true;
				if (edtCommentEmail.getText().toString().length() <= 0) {
					edtCommentEmail.setError(getActivity().getString(R.string.validation_value_required));
					validation = false;
				} else if (!IjoomerUtilities.emailValidator(edtCommentEmail.getText().toString())) {
					edtCommentEmail.setError(getActivity().getString(R.string.validation_invalid_email));
					validation = false;
				}
				if (edtCommentMessage.getText().toString().length() <= 0) {
					edtCommentMessage.setError(getActivity().getString(R.string.validation_value_required));
					validation = false;
				}
				if (edtCommentUserName.getText().toString().length() <= 0) {
					edtCommentUserName.setError(getActivity().getString(R.string.validation_value_required));
					validation = false;
				}
				if (validation) {
					final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
					provider.addOrPostComment(itemData.get(ID), IN_MENUID, edtCommentUserName.getText().toString().trim(), edtCommentMessage.getText().toString().trim(),
							edtCommentEmail.getText().toString().trim(), edtCommentSiteUrl.getText().toString().trim(), new WebCallListener() {

								@Override
								public void onProgressUpdate(int progressCount) {
									proSeekBar.setProgress(progressCount);
								}

								@Override
								public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, final Object data2) {
									try {
										if (responseCode == 200) {
											IjoomerUtilities.getCustomOkDialog(getActivity().getString(R.string.dialog_k2_item_details), errorMessage,
													getActivity().getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

														@Override
														public void NeutralMethod() {
															prepareList((JSONArray) data2);
															itemData.put(COMMENTS, ((JSONArray) data2).toString());
															commentAdapterWithHolder.notifyDataSetChanged();
															edtCommentEmail.setText("");
															edtCommentMessage.setText("");
															edtCommentSiteUrl.setText("");
															edtCommentUserName.setText("");
															if (listData.size() > 0) {
																txtItemNoComment.setVisibility(View.GONE);
															}
														}
													});
										} else {
											responseErrorMessageHandler(responseCode, true);
										}
									} catch (Throwable e) {

									}
								}
							});
				}
			}
		});

		imgItemShareFacebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
                try {
                    ((SmartActivity) getActivity()).loadNew(IjoomerFacebookSharingActivity.class, getActivity(), false, "IN_CAPTION", itemData.get(TITLE), "IN_NAME", itemData.get(ALIAS), "IN_DESCRIPTION", itemData.get(INTROTEXT), "IN_LINK", itemData.get(SHARELINK),
                            "IN_PICTURE", itemData.get(IMAGESMALL), "IN_MESSAGE", "");
                } catch (Throwable e) {
                    e.printStackTrace();
                }
			}
		});

		imgItemShareTwitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					((SmartActivity) getActivity()).loadNew(IJoomerTwitterShareActivity.class, getActivity(), false, "IN_TWIT_MESSAGE", itemData.get(SHARELINK), "IN_TWIT_IMAGE",
							itemData.get(IMAGESMALL));
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});

		imgItemShareMail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					((SmartActivity) getActivity()).loadNewResult(IJoomerMailShareActivity.class, getActivity(), 0, "IN_SHARE_CAPTION", itemData.get(TITLE), "IN_SHARE_DESCRIPTION",
							itemData.get(INTROTEXT), "IN_SHARE_SHARELINK", itemData.get(SHARELINK));
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});

		imgItemShareGoogleplus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					((SmartActivity) getActivity()).loadNew(IJoomerGooglePlusShareActivity.class, getActivity(), false, "IN_SHARE_LINK", itemData.get(SHARELINK));
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
	 * This method used to get intent data.
	 */
	private void getIntentData() {
		IN_MENUID = getActivity().getIntent().getStringExtra("IN_MENUID") == null ? "0" : getActivity().getIntent().getStringExtra("IN_MENUID");
	}

	/**
	 * This method used to prepare iteme details.
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
		txtItemNoComment.setVisibility(View.GONE);
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

		try {
			JSONArray commentArray = new JSONArray(itemData.get(COMMENTS));
			if (commentArray.length() > 0) {
				prepareList(commentArray);

			} else {
				lstItemComment.setAdapter(null);
			}

		} catch (Throwable e) {
			lstItemComment.setAdapter(null);
		}
		pbrItemDetail.setVisibility(View.GONE);

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
		IjoomerUtilities.getCustomOkDialog(getActivity().getString(R.string.dialog_k2_item_details),
				getActivity().getString(getResources().getIdentifier("code" + responseCode, "string", getActivity().getPackageName())), getActivity().getString(R.string.ok),
				R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

					@Override
					public void NeutralMethod() {
						
					}
				});
	}

	/**
	 * This method used to prepare list item comment.
	 * 
	 * @param data
	 *            represented comment data
	 */
	public void prepareList(JSONArray data) {
		if (data != null) {
			listData.clear();
			for (int i = 0; i < data.length(); i++) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.k2_item_details_comment_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				try {
					if (data.getJSONObject(i).getString(PUBLISHED).equals("1")) {
						obj.add(data.get(i));
						item.setValues(obj);
						listData.add(item);
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}

			}

		}
	}

	/**
	 * List adapter for item comment.
	 */
	private SmartListAdapterWithHolder getListAdapter() {
		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.k2_item_details_comment_list_item, listData, new ItemView() {
			@Override
			public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {

				holder.imgK2CommentUserAvatar = (ImageView) v.findViewById(R.id.imgK2CommentUserAvatar);
				holder.txtK2CommentUserName = (IjoomerTextView) v.findViewById(R.id.txtK2CommentUserName);
				holder.txtK2CommentDate = (IjoomerTextView) v.findViewById(R.id.txtK2CommentDate);
				holder.txtK2CommentTitle = (IjoomerTextView) v.findViewById(R.id.txtK2CommentTitle);
				holder.txtK2CommentUrl = (IjoomerTextView) v.findViewById(R.id.txtK2CommentUrl);

				final JSONObject row = (JSONObject) item.getValues().get(0);
				androidQuery.id(holder.imgK2CommentUserAvatar).image(IjoomerGlobalConfiguration.getDefaultAvatar(), true, true, 60, R.drawable.k2_default);
				try {
					holder.txtK2CommentTitle.setText(row.getString(COMMENTTEXT));
					holder.txtK2CommentUserName.setText(row.getString(USERNAME));
					holder.txtK2CommentDate.setText(row.getString(COMMENTDATE));
					holder.txtK2CommentUrl.setText(row.getString(COMMENTURL));
					holder.txtK2CommentUrl.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							try {
								((SmartActivity) getActivity()).loadNew(IjoomerWebviewClient.class, getActivity(), false, "url", row.getString(COMMENTURL));
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					});
				} catch (Throwable e) {
					e.printStackTrace();
				}

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
