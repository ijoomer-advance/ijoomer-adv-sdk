package com.ijoomer.components.jomsocial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.content.Intent;
import android.location.Address;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView.BufferType;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerShareActivity;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.custom.interfaces.ReportListner;
import com.ijoomer.customviews.IjoomerAudioPlayer.AudioListener;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.customviews.IjoomerVoiceAndTextMessager;
import com.ijoomer.customviews.IjoomerVoiceAndTextMessager.MessageHandler;
import com.ijoomer.customviews.IjoomerVoiceButton;
import com.ijoomer.library.jomsocial.JomGalleryDataProvider;
import com.ijoomer.media.player.IjoomerMediaPlayer;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertMagnatic;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

/**
 * This Class Contains All Method Related To JomVideoDetailsActivity.
 * 
 * @author tasol
 * 
 */
public class JomVideoDetailsActivity extends JomMasterActivity {

	private LinearLayout listFooter;
	private LinearLayout lnrHeader;
	private LinearLayout lnrCreateVideo;
	private LinearLayout lnrVideo;
	private LinearLayout lnrPlayRecordComment;
	private ListView lstVideoComment;
	private IjoomerTextView txtVideoEdit;
	private IjoomerTextView txtVideoRemove;
	private IjoomerTextView txtVideoSetAsProfileVideo;
	private IjoomerTextView txtVideoTitle;
	private IjoomerTextView txtVideoBy;
	private IjoomerTextView txtVideoCategorie;
	private IjoomerTextView txtVideoPriavcy;
	private IjoomerTextView txtVideoDateLocation;
	private IjoomerTextView txtVideoLikeCount;
	private IjoomerTextView txtVideoDislikeCount;
	private IjoomerTextView txtVideoCommentCount;
	private IjoomerTextView txtVideoDesciption;
	private IjoomerTextView txtVideoShare;
	private IjoomerTextView txtVideoReport;
	private IjoomerTextView txtVideoTag;
	private IjoomerTextView txtTotalRecordComment;
	private IjoomerTextView txtRecordUser;
	private IjoomerEditText edtVideoTitle;
	private IjoomerEditText edtVideoDescription;
	private IjoomerEditText edtVideoLocation;
	private ImageView imgVideoAvatar;
	private Spinner spnVideoCategory;
	private Spinner spnWhoCanSee;
	private IjoomerButton btnCancle;
	private IjoomerButton btnSave;
	private IjoomerVoiceButton btnPlayAll;
	private IjoomerVoiceAndTextMessager voiceMessager;
	private ViewGroup videoDetailsHeaderLayout;

	private AQuery androidQuery;
	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private ArrayList<HashMap<String, String>> CATEGORY_LIST;
	private ArrayList<String> CATEGORY_ARRAY;
	private HashMap<String, String> IN_VIDEO_DETAILS;
	private SmartListAdapterWithHolder commentAdapter;

	private JomGalleryDataProvider providerComment;
	private JomGalleryDataProvider provider;

    private final int VIDEO_TAG=1;
    public static int videoTagCount=0;

    private String IN_USERID;
	private String IN_GROUPID;

	private int recordCommentCounter;
	private int recordCommentTotal;
	private int recordCommentLast;

	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.jom_video_details;
	}

	@Override
	public void initComponents() {

		videoDetailsHeaderLayout = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.jom_video_detail_header, null);
		listFooter = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.ijoomer_list_footer, null);
		lstVideoComment = (ListView) findViewById(R.id.lstVideoComment);
		lstVideoComment.addHeaderView(videoDetailsHeaderLayout);
		lstVideoComment.addFooterView(listFooter, null, false);
		lstVideoComment.setAdapter(null);
		lnrHeader = (LinearLayout) findViewById(R.id.lnrHeader);
		lnrPlayRecordComment = (LinearLayout) videoDetailsHeaderLayout.findViewById(R.id.lnrPlayRecordComment);
		lnrCreateVideo = (LinearLayout) videoDetailsHeaderLayout.findViewById(R.id.lnrCreateVideo);
		lnrVideo = (LinearLayout) videoDetailsHeaderLayout.findViewById(R.id.lnrVideo);
		txtVideoEdit = (IjoomerTextView) findViewById(R.id.txtVideoEdit);
		txtVideoRemove = (IjoomerTextView) findViewById(R.id.txtVideoRemove);
		txtVideoSetAsProfileVideo = (IjoomerTextView) findViewById(R.id.txtVideoSetAsProfileVideo);
		txtVideoTitle = (IjoomerTextView) videoDetailsHeaderLayout.findViewById(R.id.txtVideoTitle);
		txtVideoLikeCount = (IjoomerTextView) videoDetailsHeaderLayout.findViewById(R.id.txtVideoLikeCount);
		txtVideoDislikeCount = (IjoomerTextView) videoDetailsHeaderLayout.findViewById(R.id.txtVideoDislikeCount);
		txtVideoCommentCount = (IjoomerTextView) videoDetailsHeaderLayout.findViewById(R.id.txtVideoCommentCount);
		txtVideoBy = (IjoomerTextView) videoDetailsHeaderLayout.findViewById(R.id.txtVideoBy);
		txtVideoDateLocation = (IjoomerTextView) videoDetailsHeaderLayout.findViewById(R.id.txtVideoDateLocation);
		txtVideoDesciption = (IjoomerTextView) videoDetailsHeaderLayout.findViewById(R.id.txtVideoDesciption);
		txtVideoCategorie = (IjoomerTextView) videoDetailsHeaderLayout.findViewById(R.id.txtVideoCategorie);
		txtVideoPriavcy = (IjoomerTextView) videoDetailsHeaderLayout.findViewById(R.id.txtVideoPriavcy);
		txtVideoShare = (IjoomerTextView) videoDetailsHeaderLayout.findViewById(R.id.txtVideoShare);
		txtVideoReport = (IjoomerTextView) videoDetailsHeaderLayout.findViewById(R.id.txtVideoReport);
		txtVideoTag = (IjoomerTextView) videoDetailsHeaderLayout.findViewById(R.id.txtVideoTag);
		txtTotalRecordComment = (IjoomerTextView) videoDetailsHeaderLayout.findViewById(R.id.txtTotalRecordComment);
		txtRecordUser = (IjoomerTextView) videoDetailsHeaderLayout.findViewById(R.id.txtRecordUser);
		edtVideoTitle = (IjoomerEditText) videoDetailsHeaderLayout.findViewById(R.id.edtVideoTitle);
		edtVideoLocation = (IjoomerEditText) videoDetailsHeaderLayout.findViewById(R.id.edtVideoLocation);
		edtVideoDescription = (IjoomerEditText) videoDetailsHeaderLayout.findViewById(R.id.edtVideoDescription);
		imgVideoAvatar = (ImageView) videoDetailsHeaderLayout.findViewById(R.id.imgVideoAvatar);
		btnCancle = (IjoomerButton) videoDetailsHeaderLayout.findViewById(R.id.btnCancle);
		btnSave = (IjoomerButton) videoDetailsHeaderLayout.findViewById(R.id.btnSave);
		btnPlayAll = (IjoomerVoiceButton) videoDetailsHeaderLayout.findViewById(R.id.btnPlayAll);
		btnPlayAll.setReportVoice(false);
		voiceMessager = (IjoomerVoiceAndTextMessager) findViewById(R.id.voiceMessager);
		spnWhoCanSee = (Spinner) videoDetailsHeaderLayout.findViewById(R.id.spnWhoCanSee);
		spnVideoCategory = (Spinner) videoDetailsHeaderLayout.findViewById(R.id.spnVideoCategory);

		androidQuery = new AQuery(this);
		provider = new JomGalleryDataProvider(this);
		providerComment = new JomGalleryDataProvider(this);

		getIntentData();
		getComment(false);
	}

	@Override
	public void prepareViews() {
        videoTagCount=0;
		CATEGORY_ARRAY = new ArrayList<String>();
		getVideoCategory();
		spnWhoCanSee.setAdapter(new IjoomerUtilities.MyCustomAdapter(this, new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.wall_post_type)))));

	}

	@Override
	public void setActionListeners() {

		btnPlayAll.setAudioListener(new AudioListener() {

			@Override
			public void onReportClicked() {

			}

			@Override
			public void onPrepared() {

			}

			@SuppressWarnings("unchecked")
			@Override
			public void onPlayClicked(boolean isplaying) {
				if (!isplaying) {
					txtRecordUser.setVisibility(View.VISIBLE);
					recordCommentCounter = 0;
					recordCommentLast = 0;
					recordCommentCounter += 1;
					txtTotalRecordComment.setText("(" + recordCommentCounter + "/" + recordCommentTotal + ")");
					for (int i = 0; i < listData.size(); i++) {
						if (getAudio(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(COMMENT)) != null) {
							btnPlayAll.setAudioPath(getAudio(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(COMMENT)), false);
							btnPlayAll.setText(getAudioLength(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(COMMENT)));
							txtRecordUser.setText(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(USER_NAME));
							recordCommentLast = i;
							break;
						}
					}
				} else {
					btnPlayAll.setCustomText(getString(R.string.play_all));
					txtTotalRecordComment.setText("(" + recordCommentTotal + ")");
					txtRecordUser.setVisibility(View.GONE);
					recordCommentCounter = recordCommentTotal;
				}
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onComplete() {
				if (recordCommentCounter == recordCommentTotal) {
					btnPlayAll.setCustomText(getString(R.string.play_all));
					txtTotalRecordComment.setText("(" + recordCommentTotal + ")");
					txtRecordUser.setVisibility(View.GONE);
				} else {
					recordCommentCounter += 1;
					txtTotalRecordComment.setText("(" + recordCommentCounter + "/" + recordCommentTotal + ")");
					for (int i = recordCommentLast + 1; i < listData.size(); i++) {
						if (getAudio(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(COMMENT)) != null) {
							btnPlayAll.setAudioPath(getAudio(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(COMMENT)), true);
							btnPlayAll.setText(getAudioLength(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(COMMENT)));
							txtRecordUser.setText(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(USER_NAME));
							recordCommentLast = i;
							break;
						}
					}
				}
			}
		});

		lstVideoComment.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 2) {

					if (!providerComment.isCalling() && providerComment.hasNextPage()) {
						listFooterVisible();
						providerComment.getVideoCommentList(IN_VIDEO_DETAILS.get(ID).toString(), new WebCallListener() {

							@Override
							public void onProgressUpdate(int progressCount) {

							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								listFooterInvisible();
								if (responseCode == 200) {
									updateHeader(providerComment.getNotificationData());
									prepareList(data1, true);
								}
							}
						});
					}
				}
			}
		});
		txtVideoSetAsProfileVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				provider.setAsProfileVideo(IN_VIDEO_DETAILS.get(ID).toString(), new WebCallListener() {
					final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

					@Override
					public void onProgressUpdate(int progressCount) {
						proSeekBar.setProgress(progressCount);
					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						if (responseCode == 200) {
							updateHeader(provider.getNotificationData());
							IjoomerUtilities.getCustomOkDialog(getString(R.string.video), getString(R.string.photo_set_as_profile_video_successfully), getString(R.string.ok),
									R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

										@Override
										public void NeutralMethod() {

										}
									});

						} else {
							responseErrorMessageHandler(responseCode, false);
						}
					}
				});

			}
		});

		imgVideoAvatar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
                try {
                    Intent lVideoIntent = new Intent(null,getVideoPlayURI(IN_VIDEO_DETAILS.get(URL).toString()), JomVideoDetailsActivity.this, IjoomerMediaPlayer.class);
                    startActivity(lVideoIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
			}
		});

		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (edtVideoTitle.getText().toString().trim().length() > 0) {
					if (isVideoDataChanged()) {
						final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
						Address address = IjoomerUtilities.getLatLongFromAddress(edtVideoLocation.getText().toString().trim());
						provider.editVideo(IN_VIDEO_DETAILS.get(ID), IN_GROUPID, edtVideoTitle.getText().toString().trim(), edtVideoDescription.getText().toString().trim(),
								address != null ? address.getLatitude() : 0, address != null ? address.getLongitude() : 0,
								getCategoryId(spnVideoCategory.getSelectedItemPosition()), getPrivacyCode(spnWhoCanSee.getSelectedItem().toString().trim()).toString(),
								new WebCallListener() {

									@Override
									public void onProgressUpdate(int progressCount) {
										proSeekBar.setProgress(progressCount);
									}

									@Override
									public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {

										if (responseCode == 200) {
											updateHeader(provider.getNotificationData());
											IjoomerUtilities.getCustomOkDialog(getString(R.string.video), getString(R.string.video_edited_successfully), getString(R.string.ok),
													R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

														@Override
														public void NeutralMethod() {
															IjoomerApplicationConfiguration.setReloadRequired(true);
															lnrCreateVideo.setVisibility(View.GONE);
															lnrVideo.setVisibility(View.VISIBLE);
															IN_VIDEO_DETAILS.put(CAPTION, edtVideoTitle.getText().toString().trim());
															IN_VIDEO_DETAILS.put(DESCRIPTION, edtVideoDescription.getText().toString().trim());
															IN_VIDEO_DETAILS.put(LOCATION, edtVideoLocation.getText().toString().trim());
															IN_VIDEO_DETAILS.put(PERMISSION, getPrivacyCode(spnWhoCanSee.getSelectedItem().toString().trim()));
															IN_VIDEO_DETAILS.put(CATEGORYID, getCategoryId(spnVideoCategory.getSelectedItemPosition()));

															setVideoDetails();
														}
													});

										} else {
											IjoomerUtilities.getCustomOkDialog(getString(R.string.video),
													getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok),
													R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

														@Override
														public void NeutralMethod() {
															lnrVideo.setVisibility(View.VISIBLE);
															lnrCreateVideo.setVisibility(View.GONE);
														}
													});
										}
									}

								});

					} else {
						lnrCreateVideo.setVisibility(View.GONE);
						lnrVideo.setVisibility(View.VISIBLE);
					}
				} else {
					edtVideoTitle.setError(getString(R.string.validation_value_required));
				}
			}
		});
		txtVideoTag.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
                try {
                    loadNewResult(JomTagPhotoVideoAddRemoveActivity.class,JomVideoDetailsActivity.this,VIDEO_TAG,"IN_TYPE",VIDEO,"IN_VIDEO_ID",IN_VIDEO_DETAILS.get(ID).toString());
                } catch (Throwable e) {
                    e.printStackTrace();
                }
			}
		});
		txtVideoEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (lnrCreateVideo.getVisibility() == View.VISIBLE) {
					lnrVideo.setVisibility(View.VISIBLE);
					lnrCreateVideo.setVisibility(View.GONE);
				} else {

					lnrVideo.setVisibility(View.GONE);
					lnrCreateVideo.setVisibility(View.VISIBLE);
					setVideoDetailsForEdit();
				}
			}
		});

		btnCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				lnrVideo.setVisibility(View.VISIBLE);
				lnrCreateVideo.setVisibility(View.GONE);
			}
		});
		txtVideoRemove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				IjoomerUtilities.getCustomConfirmDialog(getString(R.string.video_title_remove), getString(R.string.are_you_sure), getString(R.string.yes), getString(R.string.no),
						new CustomAlertMagnatic() {

							@Override
							public void PositiveMethod() {
								provider.removeVideo(IN_VIDEO_DETAILS.get(ID).toString(), new WebCallListener() {
									final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

									@Override
									public void onProgressUpdate(int progressCount) {
										proSeekBar.setProgress(progressCount);
									}

									@Override
									public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
										if (responseCode == 200) {
											updateHeader(provider.getNotificationData());
											IjoomerApplicationConfiguration.setReloadRequired(true);
											finish();
										} else {
											IjoomerUtilities.getCustomOkDialog(getString(R.string.video),
													getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok),
													R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

														@Override
														public void NeutralMethod() {
														}
													});
										}
									}
								});
							}

							@Override
							public void NegativeMethod() {

							}
						});

			}
		});

		voiceMessager.setMessageHandler(new MessageHandler() {

			@Override
			public void onVoiceMessageRecordingComplete(String message, String voiceMessagePath) {
				provider.addVideoComment(IN_VIDEO_DETAILS.get(ID).toString(), message, voiceMessagePath, new WebCallListener() {
					final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

					@Override
					public void onProgressUpdate(int progressCount) {
						proSeekBar.setProgress(progressCount);
					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						if (responseCode == 200) {
							updateHeader(provider.getNotificationData());
							IjoomerApplicationConfiguration.setReloadRequired(true);
							IN_VIDEO_DETAILS.put(COMMENTCOUNT, String.valueOf(Integer.parseInt(IN_VIDEO_DETAILS.get(COMMENTCOUNT).toString()) + 1));
							txtVideoCommentCount.setText(IN_VIDEO_DETAILS.get(COMMENTCOUNT).toString());
							getComment(true);
						} else {
							IjoomerUtilities.getCustomOkDialog(getString(R.string.video),
									getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok), R.layout.ijoomer_ok_dialog,
									new CustomAlertNeutral() {

										@Override
										public void NeutralMethod() {
										}
									});
						}
					}
				});
			}

			@Override
			public void onButtonSend(String message) {
				provider.addVideoComment(IN_VIDEO_DETAILS.get(ID).toString(), message, null, new WebCallListener() {
					final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

					@Override
					public void onProgressUpdate(int progressCount) {
						proSeekBar.setProgress(progressCount);
					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						if (responseCode == 200) {
							updateHeader(provider.getNotificationData());
							IjoomerApplicationConfiguration.setReloadRequired(true);
							IN_VIDEO_DETAILS.put(COMMENTCOUNT, String.valueOf(Integer.parseInt(IN_VIDEO_DETAILS.get(COMMENTCOUNT).toString()) + 1));
							txtVideoCommentCount.setText(IN_VIDEO_DETAILS.get(COMMENTCOUNT).toString());
							getComment(true);
						} else {
							IjoomerUtilities.getCustomOkDialog(getString(R.string.video),
									getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok), R.layout.ijoomer_ok_dialog,
									new CustomAlertNeutral() {

										@Override
										public void NeutralMethod() {
										}
									});
						}
					}
				});
			}

			@Override
			public void onToggle(int messager) {

			}
		});
		txtVideoLikeCount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (IN_VIDEO_DETAILS.get(LIKED).equals("1")) {
					txtVideoLikeCount.setClickable(false);
					provider.unlikeVideo(IN_VIDEO_DETAILS.get(ID).toString(), new WebCallListener() {

						@Override
						public void onProgressUpdate(int progressCount) {

						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								updateHeader(provider.getNotificationData());
								IjoomerApplicationConfiguration.setReloadRequired(true);
								IN_VIDEO_DETAILS.put(LIKED, "0");
								IN_VIDEO_DETAILS.put(LIKES, String.valueOf(Integer.parseInt(IN_VIDEO_DETAILS.get(LIKES).toString()) - 1));
								txtVideoLikeCount.setText(IN_VIDEO_DETAILS.get(LIKES).toString());
							} else {
								responseErrorMessageHandler(responseCode, false);
							}
							txtVideoLikeCount.setClickable(true);
						}
					});
				} else {
					txtVideoLikeCount.setClickable(false);
					provider.likeVideo(IN_VIDEO_DETAILS.get(ID).toString(), new WebCallListener() {

						@Override
						public void onProgressUpdate(int progressCount) {

						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								updateHeader(provider.getNotificationData());
								IjoomerApplicationConfiguration.setReloadRequired(true);
								IN_VIDEO_DETAILS.put(LIKED, "1");
								IN_VIDEO_DETAILS.put(LIKES, String.valueOf(Integer.parseInt(IN_VIDEO_DETAILS.get(LIKES).toString()) + 1));
								txtVideoLikeCount.setText(IN_VIDEO_DETAILS.get(LIKES).toString());
								if (IN_VIDEO_DETAILS.get(DISLIKED).equals("1")) {
									IN_VIDEO_DETAILS.put(DISLIKES, String.valueOf(Integer.parseInt(IN_VIDEO_DETAILS.get(DISLIKES).toString()) - 1));
									IN_VIDEO_DETAILS.put(DISLIKED, "0");
									txtVideoDislikeCount.setText(IN_VIDEO_DETAILS.get(DISLIKES).toString());
								}
							} else {
								responseErrorMessageHandler(responseCode, false);
							}
							txtVideoLikeCount.setClickable(true);
						}
					});
				}
			}
		});

		txtVideoDislikeCount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (IN_VIDEO_DETAILS.get(DISLIKED).equals("1")) {
					txtVideoDislikeCount.setClickable(false);
					provider.unlikeVideo(IN_VIDEO_DETAILS.get(ID).toString(), new WebCallListener() {

						@Override
						public void onProgressUpdate(int progressCount) {

						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								updateHeader(provider.getNotificationData());
								IjoomerApplicationConfiguration.setReloadRequired(true);
								IN_VIDEO_DETAILS.put(DISLIKED, "0");
								IN_VIDEO_DETAILS.put(DISLIKES, String.valueOf(Integer.parseInt(IN_VIDEO_DETAILS.get(DISLIKES).toString()) - 1));
								txtVideoDislikeCount.setText(IN_VIDEO_DETAILS.get(DISLIKES).toString());
							} else {
								responseErrorMessageHandler(responseCode, false);
							}
							txtVideoDislikeCount.setClickable(true);
						}
					});
				} else {
					txtVideoDislikeCount.setClickable(false);
					provider.dislikeVideo(IN_VIDEO_DETAILS.get(ID).toString(), new WebCallListener() {

						@Override
						public void onProgressUpdate(int progressCount) {

						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								updateHeader(provider.getNotificationData());
								IjoomerApplicationConfiguration.setReloadRequired(true);
								IN_VIDEO_DETAILS.put(DISLIKED, "1");
								IN_VIDEO_DETAILS.put(DISLIKES, String.valueOf(Integer.parseInt(IN_VIDEO_DETAILS.get(DISLIKES).toString()) + 1));
								txtVideoDislikeCount.setText(IN_VIDEO_DETAILS.get(DISLIKES).toString());
								if (IN_VIDEO_DETAILS.get(LIKED).equals("1")) {
									IN_VIDEO_DETAILS.put(LIKES, String.valueOf(Integer.parseInt(IN_VIDEO_DETAILS.get(LIKES).toString()) - 1));
									IN_VIDEO_DETAILS.put(LIKED, "0");
									txtVideoLikeCount.setText(IN_VIDEO_DETAILS.get(LIKES).toString());
								}
							} else {
								responseErrorMessageHandler(responseCode, false);
							}
							txtVideoDislikeCount.setClickable(true);
						}
					});
				}
			}
		});

		txtVideoShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					loadNew(IjoomerShareActivity.class, JomVideoDetailsActivity.this, false, "IN_SHARE_CAPTION", IN_VIDEO_DETAILS.get(CAPTION).toString(), "IN_SHARE_DESCRIPTION",
							IN_VIDEO_DETAILS.get(DESCRIPTION).toString(), "IN_SHARE_THUMB", IN_VIDEO_DETAILS.get(THUMB).toString(), "IN_SHARE_SHARELINK",
							IN_VIDEO_DETAILS.get(SHARELINK).toString());
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});

		txtVideoReport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				IjoomerUtilities.getReportDialog(new ReportListner() {

					@Override
					public void onClick(String repotType, String message) {
						provider.reportVideo(IN_USERID, IN_VIDEO_DETAILS.get(ID).toString(), repotType + "  " + message, new WebCallListener() {

							@Override
							public void onProgressUpdate(int progressCount) {

							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								if (responseCode == 200) {
									updateHeader(provider.getNotificationData());
									IjoomerUtilities.getCustomOkDialog(getString(R.string.video), getString(R.string.report_successfully), getString(R.string.ok),
											R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

												@Override
												public void NeutralMethod() {

												}
											});
								} else {
									responseErrorMessageHandler(responseCode, false);
								}
							}

						});
					}
				});
			}
		});

	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==VIDEO_TAG){
            txtVideoTag.setText(String.valueOf(videoTagCount));
        }
    }

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {

	}

	/**
	 * Class methods
	 */

	/**
	 * This method used to get video category.
	 */
	private void getVideoCategory() {
		provider.getVideoCategoryList(new WebCallListener() {
			final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

			@Override
			public void onProgressUpdate(int progressCount) {
				proSeekBar.setProgress(progressCount);
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				if (responseCode == 200) {
					updateHeader(provider.getNotificationData());
					CATEGORY_LIST = data1;
					for (HashMap<String, String> hashMap : CATEGORY_LIST) {
						CATEGORY_ARRAY.add(hashMap.get(NAME));
					}
					setVideoDetails();
					spnVideoCategory.setAdapter(new IjoomerUtilities.MyCustomAdapter(JomVideoDetailsActivity.this, CATEGORY_ARRAY));
				} else {
					responseErrorMessageHandler(responseCode, true);
				}

			}
		});
	}

	/**
	 * This method used to get intent data.
	 */
	@SuppressWarnings("unchecked")
	private void getIntentData() {
		IN_USERID = getIntent().getStringExtra("IN_USERID") == null ? "0" : getIntent().getStringExtra("IN_USERID");
		IN_VIDEO_DETAILS = (HashMap<String, String>) getIntent().getSerializableExtra("IN_VIDEO_DETAILS") == null ? new HashMap<String, String>()
				: (HashMap<String, String>) getIntent().getSerializableExtra("IN_VIDEO_DETAILS");
		IN_GROUPID = getIntent().getStringExtra("IN_GROUPID") == null ? "0" : getIntent().getStringExtra("IN_GROUPID");

		if (IN_VIDEO_DETAILS.containsKey(DELETEALLOWED) && IN_VIDEO_DETAILS.get(DELETEALLOWED).equals("1")) {
			lnrHeader.setVisibility(View.VISIBLE);
			if (IN_VIDEO_DETAILS.get(USER_ID).equals("0")) {
				txtVideoSetAsProfileVideo.setVisibility(View.VISIBLE);
			} else {
				txtVideoSetAsProfileVideo.setVisibility(View.GONE);
			}
		}

		androidQuery.id(imgVideoAvatar).image(IN_VIDEO_DETAILS.get(THUMB), true, true, getDeviceWidth(), 0);
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
		IjoomerUtilities.getCustomOkDialog(getString(R.string.video), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())),
				getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

					@Override
					public void NeutralMethod() {
						
					}
				});
	}

	/**
	 * This method used to set video details.
	 */
	private void setVideoDetails() {
		txtVideoTitle.setText(IN_VIDEO_DETAILS.get(CAPTION).toString());
		txtVideoTitle.setMovementMethod(new ScrollingMovementMethod());
		txtVideoBy.setText(IN_VIDEO_DETAILS.get(USER_NAME).toString());
		txtVideoLikeCount.setText(IN_VIDEO_DETAILS.get(LIKES).toString());
		txtVideoDislikeCount.setText(IN_VIDEO_DETAILS.get(DISLIKES).toString());
		txtVideoCommentCount.setText(IN_VIDEO_DETAILS.get(COMMENTCOUNT).toString());
        videoTagCount+=Integer.parseInt(IN_VIDEO_DETAILS.get(TAGS).toString());
        txtVideoTag.setText(String.valueOf(videoTagCount));
		txtVideoPriavcy.setText(getPrivacyString(IN_VIDEO_DETAILS.get(PERMISSIONS).toString()));
		int size = CATEGORY_LIST.size();
		for (int i = 0; i < size; i++) {
			if (CATEGORY_LIST.get(i).get(ID).equals(IN_VIDEO_DETAILS.get(CATEGORYID).toString())) {
				txtVideoCategorie.setText(CATEGORY_LIST.get(i).get(NAME));
				break;
			}
		}
		txtVideoBy.setMovementMethod(LinkMovementMethod.getInstance());
		txtVideoBy.setText(addClickablePart(Html.fromHtml(IN_VIDEO_DETAILS.get(USER_NAME)), IN_VIDEO_DETAILS), BufferType.SPANNABLE);

		txtVideoDesciption.setText(IN_VIDEO_DETAILS.get(DESCRIPTION).toString());

		if (IN_VIDEO_DETAILS.get(LOCATION).toString().trim().length() <= 0) {
			txtVideoDateLocation.setText(IN_VIDEO_DETAILS.get(DATE).toString());
		} else {
			txtVideoDateLocation.setText(IN_VIDEO_DETAILS.get(DATE).toString() + " @ " + IN_VIDEO_DETAILS.get(LOCATION).toString());
		}

		if (IN_VIDEO_DETAILS.get(DELETEALLOWED).equals("1")) {
			lnrHeader.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * This method used to get video comment.
	 * 
	 * @param isNextPage
	 *            represented is next page
	 */
	public void getComment(boolean isNextPage) {

		if (isNextPage) {
			providerComment.restorePagingSettings();
		}
		if (IN_VIDEO_DETAILS.containsKey(COMMENTCOUNT) && Integer.parseInt(IN_VIDEO_DETAILS.get(COMMENTCOUNT).toString()) > 0) {
			providerComment.getVideoCommentList(IN_VIDEO_DETAILS.get(ID).toString(), new WebCallListener() {

				@Override
				public void onProgressUpdate(int progressCount) {

				}

				@Override
				public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
					if (responseCode == 200) {
						updateHeader(providerComment.getNotificationData());
						prepareList(data1, false);
						commentAdapter = getListAdapter();
						lstVideoComment.setAdapter(commentAdapter);
					} else {
						lstVideoComment.setAdapter(null);
						responseErrorMessageHandler(responseCode, false);
					}

				}
			});
		} else {
			lstVideoComment.setAdapter(null);
		}
	}

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
	 * This method used to set edit video details
	 */
	private void setVideoDetailsForEdit() {
		edtVideoTitle.setText(IN_VIDEO_DETAILS.get(CAPTION).toString());
		edtVideoDescription.setText(IN_VIDEO_DETAILS.get(DESCRIPTION).toString());
		edtVideoLocation.setText(IN_VIDEO_DETAILS.get(LOCATION).toString());
		spnWhoCanSee.setSelection(getPrivacyIndex(IN_VIDEO_DETAILS.get(PERMISSIONS).toString()));
		int size = CATEGORY_LIST.size();
		for (int i = 0; i < size; i++) {
			if (CATEGORY_LIST.get(i).get(ID).equals(IN_VIDEO_DETAILS.get(CATEGORYID))) {
				spnVideoCategory.setSelection(i);
				break;
			}
		}

	}

	/**
	 * This method used to is video details changed.
	 * 
	 * @return represented {@link Boolean}
	 */
	private boolean isVideoDataChanged() {
		boolean isChanged = false;
		if (!(edtVideoTitle.getText().toString().equals(IN_VIDEO_DETAILS.get(CAPTION)) && edtVideoDescription.getText().toString().equals(IN_VIDEO_DETAILS.get(DESCRIPTION))
				&& edtVideoLocation.getText().toString().equals(IN_VIDEO_DETAILS.get(LOCATION))
				&& getPrivacyCode(spnWhoCanSee.getSelectedItem().toString()).equals(IN_VIDEO_DETAILS.get(PERMISSION)) && getCategoryId(spnVideoCategory.getSelectedItemPosition())
				.equals(IN_VIDEO_DETAILS.get(CATEGORYID)))) {
			isChanged = true;
		}
		return isChanged;
	}

	/**
	 * This method used to get category id.
	 * 
	 * @param categoryIndex
	 *            represented category index
	 * @return represented {@link String}
	 */
	private String getCategoryId(int categoryIndex) {
		return CATEGORY_LIST.get(categoryIndex).get(ID);
	}


	/**
	 * This method used to prepare list video comment.
	 * 
	 * @param data
	 *            represented video comment data
	 * @param append
	 *            represented data append
	 */
	public void prepareList(ArrayList<HashMap<String, String>> data, boolean append) {
		if (data != null) {
			if (!append) {
				listData.clear();
				recordCommentTotal = 0;
			}
			for (HashMap<String, String> hashMap : data) {
				if (getAudio(hashMap.get(COMMENT)) != null) {
					recordCommentTotal += 1;
				}
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.jom_comment_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hashMap);
				item.setValues(obj);
				if (append) {
					commentAdapter.add(item);
				} else {
					listData.add(item);
				}
			}
			if (recordCommentTotal > 1) {
				lnrPlayRecordComment.setVisibility(View.VISIBLE);
				btnPlayAll.setCustomText(getString(R.string.play_all));
				txtTotalRecordComment.setText("(" + recordCommentTotal + ")");
			}
		}
	}

	/**
	 * List adapter for video comment.
	 */

	private SmartListAdapterWithHolder getListAdapter() {
		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(JomVideoDetailsActivity.this, R.layout.jom_comment_list_item, listData, new ItemView() {
			@Override
			public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {
				holder.imgCommentUserAvatar = (ImageView) v.findViewById(R.id.imgCommentUserAvatar);
				holder.txtCommentUserName = (IjoomerTextView) v.findViewById(R.id.txtCommentUserName);
				holder.txtCommentDate = (IjoomerTextView) v.findViewById(R.id.txtCommentDate);
				holder.txtCommentTitle = (IjoomerTextView) v.findViewById(R.id.txtCommentTitle);
				holder.btnCommentRemove = (IjoomerButton) v.findViewById(R.id.btnCommentRemove);
				holder.btnPlayStopVoice = (IjoomerVoiceButton) v.findViewById(R.id.btnPlayStopVoice);
				holder.btnPlayStopVoice.setVisibility(View.GONE);
				holder.btnCommentRemove.setVisibility(View.GONE);
				@SuppressWarnings("unchecked")
				final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);

				androidQuery.id(holder.imgCommentUserAvatar).image(row.get(USER_AVATAR), true, true, getDeviceWidth(), 0);
				holder.txtCommentTitle.setText(getPlainText(row.get(COMMENT)));
				holder.txtCommentUserName.setText(row.get(USER_NAME));
				holder.txtCommentDate.setText(row.get(DATE));
				if (row.containsKey(DELETEALLOWED) && row.get(DELETEALLOWED).equals("1")) {
					holder.btnCommentRemove.setVisibility(View.VISIBLE);
				}

				if (getAudio(row.get(COMMENT)) != null) {

					holder.btnPlayStopVoice.setVisibility(View.VISIBLE);
					holder.btnPlayStopVoice.setText(getAudioLength(row.get(COMMENT)));
					holder.btnPlayStopVoice.setAudioPath(getAudio(row.get(COMMENT)), false);
					holder.btnPlayStopVoice.setAudioListener(new AudioListener() {

						@Override
						public void onReportClicked() {
							reportVoice(getAudio(row.get(COMMENT)));
						}

						@Override
						public void onPrepared() {
						}

						@Override
						public void onPlayClicked(boolean isplaying) {
							btnPlayAll.setCustomText(getString(R.string.play_all));
							txtTotalRecordComment.setText("(" + recordCommentTotal + ")");
							txtRecordUser.setVisibility(View.GONE);
							recordCommentCounter = recordCommentTotal;
						}

						@Override
						public void onComplete() {
						}
					});

				}

				holder.btnCommentRemove.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						IjoomerUtilities.getCustomConfirmDialog(getString(R.string.video), getString(R.string.are_you_sure), getString(R.string.yes), getString(R.string.no),
								new CustomAlertMagnatic() {

									@Override
									public void PositiveMethod() {
										provider.removeVideoComment(row.get(ID), new WebCallListener() {
											final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

											@Override
											public void onProgressUpdate(int progressCount) {
												proSeekBar.setProgress(progressCount);
											}

											@Override
											public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
												if (responseCode == 200) {
													updateHeader(provider.getNotificationData());
													IjoomerApplicationConfiguration.setReloadRequired(true);
													commentAdapter.remove(commentAdapter.getItem(position));
													if (getAudio(row.get(COMMENT)) != null) {
														recordCommentTotal -= 1;
														txtTotalRecordComment.setText("(" + recordCommentTotal + ")");
														if (recordCommentTotal == 1) {
															lnrPlayRecordComment.setVisibility(View.GONE);
														}
													}
													IN_VIDEO_DETAILS.put(COMMENTCOUNT, String.valueOf(Integer.parseInt(IN_VIDEO_DETAILS.get(COMMENTCOUNT).toString()) - 1));
													txtVideoCommentCount.setText(IN_VIDEO_DETAILS.get(COMMENTCOUNT).toString());
												} else {
													responseErrorMessageHandler(responseCode, false);
												}

											}
										});
									}

									@Override
									public void NegativeMethod() {

									}
								});

					}
				});

				holder.imgCommentUserAvatar.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						if (row.get(USER_PROFILE).equals("1")) {
							gotoProfile(row.get(USER_ID));
						}
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

}
