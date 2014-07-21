package com.ijoomer.components.jomsocial;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView.BufferType;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerShareActivity;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.custom.interfaces.ReportListner;
import com.ijoomer.custom.interfaces.SelectImageDialogListner;
import com.ijoomer.customviews.IjoomerAudioPlayer.AudioListener;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerCheckBox;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerRadioButton;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.customviews.IjoomerVoiceAndTextMessager;
import com.ijoomer.customviews.IjoomerVoiceAndTextMessager.MessageHandler;
import com.ijoomer.customviews.IjoomerVoiceButton;
import com.ijoomer.library.jomsocial.JomEventDataProvider;
import com.ijoomer.library.jomsocial.JomWallDataProvider;
import com.ijoomer.media.player.IjoomerMediaPlayer;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.ijoomer.weservice.WebCallListenerWithCacheInfo;
import com.smart.framework.CustomAlertMagnatic;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

/**
 * This Class Contains All Method Related To JomEventDetailsActivity.
 * 
 * @author tasol
 * 
 */
public class JomEventDetailsActivity_v30 extends JomMasterActivity {

	private LinearLayout lnrEventAttending;
	private LinearLayout lnrPrivateEventAttending;
	private LinearLayout lnrEventHeader;
	private LinearLayout lnrEventWriteComment;
	private LinearLayout lnrConfirmedGuests;
	private LinearLayout lnrScrollable;
	private LinearLayout listFooter;
	private LinearLayout lnrInvitation;
	private LinearLayout lnrPlayRecordComment;
	private ListView lstConfirmedGuests;
	private ListView lstEventComment;
	private ListView lstEventMember;
	private FrameLayout framEventEditAvatar;
	private ImageView imgEventAvatar;
	private ImageView imgEventEditAvatar;
	private ImageView imgEventMap;
	private ImageView imgTagClose;
	private ImageView imgInvitationIcon;
	private IjoomerTextView txtEventDate;
	private IjoomerTextView txtEventTitle;
	private IjoomerTextView txtEventBy;
	private IjoomerTextView txtEventCategory;
	private IjoomerTextView txtEventAvilableSeats;
	private IjoomerTextView txtEventLikeCount;
	private IjoomerTextView txtEventDislikeCount;
	private IjoomerTextView txtEventCommentCount;
	private IjoomerTextView txtEventShare;
	private IjoomerTextView txtEventReport;
	private IjoomerTextView txtEventWhenData;
	private IjoomerTextView txtEventWhereData;
	private IjoomerTextView txtEventDescriptionData;
	private IjoomerTextView txtEventSummeryData;
	private IjoomerTextView txtEventEdit;
	private IjoomerTextView txtEventSendEmail;
	private IjoomerTextView txtEventRemove;
	private IjoomerTextView txtEventIgnor;
	private IjoomerTextView txtViewAll;
	private IjoomerTextView txtPrivateEventAttendingRequest;
	private IjoomerTextView txtPrivateEventApprovalRequestList;
	private IjoomerTextView txtInvitationMessage;
	private IjoomerTextView txtInvitationReject;
	private IjoomerTextView txtPrivateEventAwiting;
	private IjoomerTextView txtPrivateEvent;
	private IjoomerTextView txtRecentActivities;
	private IjoomerTextView txtTotalRecordComment;
	private IjoomerTextView txtRecordUser;
	private IjoomerVoiceAndTextMessager voiceMessager;
	private IjoomerButton btnEventInvite;
	private IjoomerVoiceButton btnPlayAll;
	@SuppressWarnings("unused")
	private RadioGroup rdgEventAttending;
	private IjoomerRadioButton rdbEventAttendingYes;
	private IjoomerRadioButton rdbEventAttendingNo;
	private ViewGroup eventDetailsHeaderLayout;
	private PopupWindow dialog;
	private ProgressBar pbrGuest;
	private ProgressBar pbrEventWaitingMember;

	private ImageView imgCoverPhoto;
	private ImageView imgAddEditCoverPhoto;

	private AQuery androidQuery;
	private ArrayList<SmartListItem> wallListData = new ArrayList<SmartListItem>();
	private ArrayList<SmartListItem> guestListData = new ArrayList<SmartListItem>();
	private ArrayList<SmartListItem> eventWaitingApprovalMemberList = new ArrayList<SmartListItem>();
	private ArrayList<HashMap<String, String>> guestList;
	private HashMap<String, String> IN_EVENT_DETAILS;
	private HashMap<String, String> EVENT_DETAILS;
	private SmartListAdapterWithHolder commentAdapter;
	private SmartListAdapterWithHolder guestAdapter;

	private JomEventDataProvider guestDataProvider;
	private JomEventDataProvider provider;
	private JomWallDataProvider wallDataProvider;
	private JomEventDataProvider providerEventWaitingApprovalMember;
	private JSONObject menu;

	private String IN_USERID;
	private String selectedImagePath;
	final private int PICK_IMAGE = 1;
	final private int CAPTURE_IMAGE = 2;
	private int recordCommentCounter;
	private int recordCommentTotal;
	private int recordCommentLast;
	private boolean isGuestListResuming = false;
	private boolean isSetEventCoverPage;

	/**
	 * Override methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.jom_event_details;
	}

	@Override
	public void initComponents() {

		eventDetailsHeaderLayout = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.jom_event_details_header_v30, null);
		listFooter = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.ijoomer_list_footer, null);
		lstEventComment = (ListView) findViewById(R.id.lstEventComment);
		lstEventComment.addHeaderView(eventDetailsHeaderLayout, null, false);
		lstEventComment.addFooterView(listFooter, null, false);
		lstEventComment.setAdapter(null);
		lnrEventWriteComment = (LinearLayout) findViewById(R.id.lnrEventWriteComment);
		lnrEventHeader = (LinearLayout) findViewById(R.id.lnrEventHeader);
		txtEventEdit = (IjoomerTextView) findViewById(R.id.txtEventEdit);
		txtEventSendEmail = (IjoomerTextView) findViewById(R.id.txtEventSendEmail);
		txtEventRemove = (IjoomerTextView) findViewById(R.id.txtEventRemove);

		imgCoverPhoto = (ImageView) eventDetailsHeaderLayout.findViewById(R.id.imgCoverPhoto);
		imgAddEditCoverPhoto = (ImageView) eventDetailsHeaderLayout.findViewById(R.id.imgAddEditCoverPhoto);

		lnrPlayRecordComment = (LinearLayout) eventDetailsHeaderLayout.findViewById(R.id.lnrPlayRecordComment);
		lnrEventAttending = (LinearLayout) eventDetailsHeaderLayout.findViewById(R.id.lnrEventAttending);
		lnrPrivateEventAttending = (LinearLayout) eventDetailsHeaderLayout.findViewById(R.id.lnrPrivateEventAttending);
		lnrConfirmedGuests = (LinearLayout) eventDetailsHeaderLayout.findViewById(R.id.lnrConfirmedGuests);
		lnrScrollable = (LinearLayout) eventDetailsHeaderLayout.findViewById(R.id.lnrScrollable);
		lnrInvitation = (LinearLayout) eventDetailsHeaderLayout.findViewById(R.id.lnrInvitation);
		framEventEditAvatar = (FrameLayout) eventDetailsHeaderLayout.findViewById(R.id.framEventEditAvatar);
		imgEventAvatar = (ImageView) eventDetailsHeaderLayout.findViewById(R.id.imgEventAvatar);
		imgEventEditAvatar = (ImageView) eventDetailsHeaderLayout.findViewById(R.id.imgEventEditAvatar);
		imgEventMap = (ImageView) eventDetailsHeaderLayout.findViewById(R.id.imgEventMap);
		imgInvitationIcon = (ImageView) eventDetailsHeaderLayout.findViewById(R.id.imgInvitationIcon);
		txtEventDate = (IjoomerTextView) eventDetailsHeaderLayout.findViewById(R.id.txtEventDate);
		txtEventTitle = (IjoomerTextView) eventDetailsHeaderLayout.findViewById(R.id.txtEventTitle);
		txtEventBy = (IjoomerTextView) eventDetailsHeaderLayout.findViewById(R.id.txtEventBy);
		txtEventCategory = (IjoomerTextView) eventDetailsHeaderLayout.findViewById(R.id.txtEventCategory);
		txtEventAvilableSeats = (IjoomerTextView) eventDetailsHeaderLayout.findViewById(R.id.txtEventAvilableSeats);
		txtEventLikeCount = (IjoomerTextView) eventDetailsHeaderLayout.findViewById(R.id.txtEventLikeCount);
		txtEventDislikeCount = (IjoomerTextView) eventDetailsHeaderLayout.findViewById(R.id.txtEventDislikeCount);
		txtEventCommentCount = (IjoomerTextView) eventDetailsHeaderLayout.findViewById(R.id.txtEventCommentCount);
		txtEventShare = (IjoomerTextView) eventDetailsHeaderLayout.findViewById(R.id.txtEventShare);
		txtEventIgnor = (IjoomerTextView) eventDetailsHeaderLayout.findViewById(R.id.txtEventIgnor);
		txtEventReport = (IjoomerTextView) eventDetailsHeaderLayout.findViewById(R.id.txtEventReport);
		txtEventWhenData = (IjoomerTextView) eventDetailsHeaderLayout.findViewById(R.id.txtEventWhenData);
		txtEventWhereData = (IjoomerTextView) eventDetailsHeaderLayout.findViewById(R.id.txtEventWhereData);
		txtEventDescriptionData = (IjoomerTextView) eventDetailsHeaderLayout.findViewById(R.id.txtEventDescriptionData);
		txtEventSummeryData = (IjoomerTextView) eventDetailsHeaderLayout.findViewById(R.id.txtEventSummeryData);
		txtPrivateEventAttendingRequest = (IjoomerTextView) eventDetailsHeaderLayout.findViewById(R.id.txtPrivateEventAttendingRequest);
		txtInvitationMessage = (IjoomerTextView) eventDetailsHeaderLayout.findViewById(R.id.txtInvitationMessage);
		txtInvitationReject = (IjoomerTextView) eventDetailsHeaderLayout.findViewById(R.id.txtInvitationReject);
		txtPrivateEvent = (IjoomerTextView) eventDetailsHeaderLayout.findViewById(R.id.txtPrivateEvent);
		txtPrivateEventAwiting = (IjoomerTextView) eventDetailsHeaderLayout.findViewById(R.id.txtPrivateEventAwiting);
		txtRecentActivities = (IjoomerTextView) eventDetailsHeaderLayout.findViewById(R.id.txtRecentActivities);
		txtPrivateEventApprovalRequestList = (IjoomerTextView) eventDetailsHeaderLayout.findViewById(R.id.txtPrivateEventApprovalRequestList);
		txtViewAll = (IjoomerTextView) eventDetailsHeaderLayout.findViewById(R.id.txtViewAll);
		txtTotalRecordComment = (IjoomerTextView) eventDetailsHeaderLayout.findViewById(R.id.txtTotalRecordComment);
		txtRecordUser = (IjoomerTextView) eventDetailsHeaderLayout.findViewById(R.id.txtRecordUser);
		rdbEventAttendingYes = (IjoomerRadioButton) eventDetailsHeaderLayout.findViewById(R.id.rdbEventAttendingYes);
		rdbEventAttendingNo = (IjoomerRadioButton) eventDetailsHeaderLayout.findViewById(R.id.rdbEventAttendingNo);
		rdgEventAttending = (RadioGroup) eventDetailsHeaderLayout.findViewById(R.id.rdgEventAttending);
		btnEventInvite = (IjoomerButton) eventDetailsHeaderLayout.findViewById(R.id.btnEventInvite);
		btnPlayAll = (IjoomerVoiceButton) eventDetailsHeaderLayout.findViewById(R.id.btnPlayAll);
		btnPlayAll.setReportVoice(false);
		voiceMessager = (IjoomerVoiceAndTextMessager) findViewById(R.id.voiceMessager);

		androidQuery = new AQuery(this);
		provider = new JomEventDataProvider(this);
		wallDataProvider = new JomWallDataProvider(this);

		getIntentData();
		setEventDetails();
	}

	@Override
	public void prepareViews() {
		loadConfirmedGuests();

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isSetEventCoverPage) {
			isSetEventCoverPage = false;
			setEventDetails();
		}
		if (IjoomerApplicationConfiguration.isReloadRequired()) {
			IjoomerApplicationConfiguration.setReloadRequired(false);
			getEventWall();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == PICK_IMAGE) {
				selectedImagePath = getAbsolutePath(data.getData());
				provider.editEventAvatar(selectedImagePath, IN_EVENT_DETAILS.get(ID), new WebCallListener() {
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
							imgEventAvatar.setImageBitmap(decodeFile(selectedImagePath));
							selectedImagePath = null;
						} else {
							responseErrorMessageHandler(responseCode, false);
						}
					}
				});
			} else if (requestCode == CAPTURE_IMAGE) {
				selectedImagePath = getImagePath();
				provider.editEventAvatar(selectedImagePath, IN_EVENT_DETAILS.get(ID), new WebCallListener() {
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
							imgEventAvatar.setImageBitmap(decodeFile(selectedImagePath));
							selectedImagePath = null;
						} else {
							responseErrorMessageHandler(responseCode, false);
						}
					}
				});
			} else {
				super.onActivityResult(requestCode, resultCode, data);
			}
		}

	}

	@Override
	public void setActionListeners() {
		imgAddEditCoverPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				isSetEventCoverPage = true;
				try {
					loadNew(JomAlbumsActivity.class, JomEventDetailsActivity_v30.this, false, "IN_USERID", IN_USERID, "IN_PROFILE_COVER", "3|" + IN_EVENT_DETAILS.get(ID) + "");
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});
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
					for (int i = 0; i < wallListData.size(); i++) {
						if (getAudio(((HashMap<String, String>) wallListData.get(i).getValues().get(0)).get(TITLETAG)) != null) {
							btnPlayAll.setAudioPath(getAudio(((HashMap<String, String>) wallListData.get(i).getValues().get(0)).get(TITLETAG)), false);
							btnPlayAll.setText(getAudioLength(((HashMap<String, String>) wallListData.get(i).getValues().get(0)).get(TITLETAG)));
							txtRecordUser.setText(((HashMap<String, String>) wallListData.get(i).getValues().get(0)).get(USER_NAME));
							recordCommentLast = i;
							break;
						}
						if (getAudio(((HashMap<String, String>) wallListData.get(i).getValues().get(0)).get(CONTENT)) != null) {
							btnPlayAll.setAudioPath(getAudio(((HashMap<String, String>) wallListData.get(i).getValues().get(0)).get(CONTENT)), false);
							btnPlayAll.setText(getAudioLength(((HashMap<String, String>) wallListData.get(i).getValues().get(0)).get(CONTENT)));
							txtRecordUser.setText(((HashMap<String, String>) wallListData.get(i).getValues().get(0)).get(USER_NAME));
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
					txtRecordUser.setVisibility(View.VISIBLE);
					recordCommentCounter += 1;
					txtTotalRecordComment.setText("(" + recordCommentCounter + "/" + recordCommentTotal + ")");
					for (int i = recordCommentLast + 1; i < wallListData.size(); i++) {
						if (getAudio(((HashMap<String, String>) wallListData.get(i).getValues().get(0)).get(TITLETAG)) != null) {
							btnPlayAll.setAudioPath(getAudio(((HashMap<String, String>) wallListData.get(i).getValues().get(0)).get(TITLETAG)), true);
							btnPlayAll.setText(getAudioLength(((HashMap<String, String>) wallListData.get(i).getValues().get(0)).get(TITLETAG)));
							txtRecordUser.setText(((HashMap<String, String>) wallListData.get(i).getValues().get(0)).get(USER_NAME));
							recordCommentLast = i;
							break;
						}
						if (getAudio(((HashMap<String, String>) wallListData.get(i).getValues().get(0)).get(CONTENT)) != null) {
							btnPlayAll.setAudioPath(getAudio(((HashMap<String, String>) wallListData.get(i).getValues().get(0)).get(CONTENT)), true);
							btnPlayAll.setText(getAudioLength(((HashMap<String, String>) wallListData.get(i).getValues().get(0)).get(CONTENT)));
							txtRecordUser.setText(((HashMap<String, String>) wallListData.get(i).getValues().get(0)).get(USER_NAME));
							recordCommentLast = i;
							break;
						}
					}
				}
			}
		});

		txtInvitationReject.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				provider.eventResponse(IN_EVENT_DETAILS.get(ID), "2", new WebCallListener() {
					final ProgressBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

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
							responseErrorMessageHandler(responseCode, false);
						}

					}
				});
			}
		});
		txtPrivateEventApprovalRequestList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showWaitingApprovalMemberDialog();
			}
		});

		txtPrivateEventAttendingRequest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				provider.requestInvitation(IN_EVENT_DETAILS.get(ID), "1", new WebCallListener() {
					final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

					@Override
					public void onProgressUpdate(int progressCount) {
						proSeekBar.setProgress(progressCount);
					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						if (responseCode == 708 || responseCode == 200) {
							if (responseCode == 708) {
								responseErrorMessageHandler(responseCode, false);
							}
							updateHeader(provider.getNotificationData());
							EVENT_DETAILS.put(ISWAITINGAPPROVAL, "1");
							txtPrivateEventAwiting.setVisibility(View.VISIBLE);
							txtPrivateEventAttendingRequest.setVisibility(View.GONE);
						} else {
							responseErrorMessageHandler(responseCode, false);
						}
					}
				});
			}
		});
		lstEventComment.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 2) {

					if (!wallDataProvider.isCalling() && wallDataProvider.hasNextPage()) {
						listFooterVisible();
						wallDataProvider.getWallList(IN_EVENT_DETAILS.get(ID), "event", new WebCallListenerWithCacheInfo() {

							@Override
							public void onProgressUpdate(int progressCount) {
							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2, int pageNo, int pageLimit,
									boolean fromCache) {
								listFooterInvisible();
								if (responseCode == 200) {

									if (!fromCache) {
										updateHeader(wallDataProvider.getNotificationData());
									}
									prepareWallList(data1, true, fromCache, pageNo, pageLimit);
								} else {
									responseErrorMessageHandler(responseCode, false);
								}
							}
						});
					}
				}
			}
		});

		btnEventInvite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					loadNew(JomInviteFriendActivity.class, JomEventDetailsActivity_v30.this, false, "IN_EVENT_ID", IN_EVENT_DETAILS.get(ID));
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});

		imgEventMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (EVENT_DETAILS.get(LAT).toString().trim().length() > 0 && EVENT_DETAILS.get(LONG).toString().trim().length() > 0) {
					try {
						ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
						list.add(EVENT_DETAILS);
						loadNew(JomMapActivity.class, JomEventDetailsActivity_v30.this, false, "IN_MAPLIST", list, "IS_SHOW_BUBBLE", false);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				} else {
					ting(getString(R.string.lat_long_not_found));
				}

			}
		});

		voiceMessager.setMessageHandler(new MessageHandler() {

			@Override
			public void onVoiceMessageRecordingComplete(String message, String voiceMessagePath) {
				provider.addWallPost(IN_EVENT_DETAILS.get(ID), message, voiceMessagePath, new WebCallListener() {
					final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

					@Override
					public void onProgressUpdate(int progressCount) {
						proSeekBar.setProgress(progressCount);
					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						if (responseCode == 200) {
							updateHeader(provider.getNotificationData());
							EVENT_DETAILS.put(COMMENTS, String.valueOf(Integer.parseInt(EVENT_DETAILS.get(COMMENTS)) + 1));
							txtEventCommentCount.setText(EVENT_DETAILS.get(COMMENTS));
							getEventWall();
						} else {
							responseErrorMessageHandler(responseCode, false);
						}
					}
				});
			}

			@Override
			public void onButtonSend(String message) {
				provider.addWallPost(IN_EVENT_DETAILS.get(ID), message, null, new WebCallListener() {
					final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

					@Override
					public void onProgressUpdate(int progressCount) {
						proSeekBar.setProgress(progressCount);
					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						if (responseCode == 200) {
							updateHeader(provider.getNotificationData());
							EVENT_DETAILS.put(COMMENTS, String.valueOf(Integer.parseInt(EVENT_DETAILS.get(COMMENTS)) + 1));
							txtEventCommentCount.setText(EVENT_DETAILS.get(COMMENTS));
							getEventWall();
						} else {
							responseErrorMessageHandler(responseCode, false);
						}
					}
				});
			}

			@Override
			public void onToggle(int messager) {

			}
		});

		txtEventIgnor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				provider.ignoreEvent(IN_EVENT_DETAILS.get(ID), new WebCallListener() {
					final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

					@Override
					public void onProgressUpdate(int progressCount) {
						proSeekBar.setProgress(progressCount);
					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						if (responseCode == 200) {
							updateHeader(provider.getNotificationData());
							if (EVENT_DETAILS.get(MYSTATUS).equals("1")) {
								updateSeatCount(false);
							}
							rdbEventAttendingNo.setChecked(false);
							rdbEventAttendingYes.setChecked(false);
							EVENT_DETAILS.put(MYSTATUS, "5");

							IjoomerApplicationConfiguration.setReloadRequired(true);
							IjoomerUtilities.getCustomOkDialog(getString(R.string.event), getString(R.string.event_ignore_successfully), getString(R.string.ok),
									R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

								@Override
								public void NeutralMethod() {
									finish();
								}
							});
						} else {
							responseErrorMessageHandler(responseCode, false);
						}
					}
				});

			}
		});

		txtEventReport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				IjoomerUtilities.getReportDialog(new ReportListner() {
					@Override
					public void onClick(String repotType, String message) {
						final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
						provider.reportEvent(IN_EVENT_DETAILS.get(ID), repotType + "  " + message, new WebCallListener() {

							@Override
							public void onProgressUpdate(int progressCount) {
								proSeekBar.setProgress(progressCount);
							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								if (responseCode == 200) {
									updateHeader(provider.getNotificationData());
									IjoomerUtilities.getCustomOkDialog(getString(R.string.event), getString(R.string.report_successfully), getString(R.string.ok),
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

		txtEventSendEmail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				getSendMailDialog(new ReportListner() {

					@Override
					public void onClick(String title, String message) {
						final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
						provider.sendMailToAllMember(IN_EVENT_DETAILS.get(ID), title, message, new WebCallListener() {

							@Override
							public void onProgressUpdate(int progressCount) {
								proSeekBar.setProgress(progressCount);
							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								if (responseCode == 200) {
									updateHeader(provider.getNotificationData());
									IjoomerUtilities.getCustomOkDialog(getString(R.string.event), getString(R.string.event_send_mail_successfully), getString(R.string.ok),
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

		txtViewAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showGuestDialog();
			}
		});
		imgEventEditAvatar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				IjoomerUtilities.selectImageDialog(new SelectImageDialogListner() {

					@Override
					public void onPhoneGallery() {
						Intent intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						startActivityForResult(Intent.createChooser(intent, ""), PICK_IMAGE);
					}

					@Override
					public void onCapture() {
						final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
						startActivityForResult(intent, CAPTURE_IMAGE);
					}
				});

			}
		});

		txtEventEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				provider.addOrEditEventFieldList(IN_EVENT_DETAILS.get(ID), "0", new WebCallListener() {
					final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

					@Override
					public void onProgressUpdate(int progressCount) {
						proSeekBar.setProgress(progressCount);
					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						if (responseCode == 200) {
							updateHeader(provider.getNotificationData());
							try {
								loadNew(JomEventCreateActivity.class, JomEventDetailsActivity_v30.this, true, "IN_FIELD_LIST", data1, "IN_EVENT_ID", IN_EVENT_DETAILS.get(ID),
										"IN_GROUP_ID", "0");
							} catch (Throwable e) {
								e.printStackTrace();
							}
						} else {
							responseErrorMessageHandler(responseCode, false);
						}
					}
				});

			}
		});

		txtEventRemove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				IjoomerUtilities.getCustomConfirmDialog(getString(R.string.event_title_remove), getString(R.string.are_you_sure), getString(R.string.yes), getString(R.string.no),
						new CustomAlertMagnatic() {

					@Override
					public void PositiveMethod() {
						final JomEventDataProvider provider = new JomEventDataProvider(JomEventDetailsActivity_v30.this);
						provider.removeEvent(IN_EVENT_DETAILS.get(ID), new WebCallListener() {
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

		txtEventShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					loadNew(IjoomerShareActivity.class, JomEventDetailsActivity_v30.this, false, "IN_SHARE_CAPTION", IN_EVENT_DETAILS.get(TITLE), "IN_SHARE_DESCRIPTION",
							EVENT_DETAILS.get(DESCRIPTION), "IN_SHARE_THUMB", EVENT_DETAILS.get(AVATAR), "IN_SHARE_SHARELINK", EVENT_DETAILS.get(SHARELINK));
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});

		txtEventLikeCount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (EVENT_DETAILS.get(LIKED).equals("1")) {
					txtEventLikeCount.setClickable(false);
					provider.unlikeEvent(IN_EVENT_DETAILS.get(ID), new WebCallListener() {

						@Override
						public void onProgressUpdate(int progressCount) {

						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								updateHeader(provider.getNotificationData());
								IjoomerApplicationConfiguration.setReloadRequired(true);
								EVENT_DETAILS.put(LIKED, "0");
								EVENT_DETAILS.put(LIKES, String.valueOf(Integer.parseInt(EVENT_DETAILS.get(LIKES)) - 1));
								txtEventLikeCount.setText(EVENT_DETAILS.get(LIKES));
							} else {
								responseErrorMessageHandler(responseCode, false);
							}
							txtEventLikeCount.setClickable(true);
						}
					});
				} else {
					txtEventLikeCount.setClickable(false);
					provider.likeEvent(IN_EVENT_DETAILS.get(ID), new WebCallListener() {

						@Override
						public void onProgressUpdate(int progressCount) {

						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								updateHeader(provider.getNotificationData());
								IjoomerApplicationConfiguration.setReloadRequired(true);
								EVENT_DETAILS.put(LIKED, "1");
								EVENT_DETAILS.put(LIKES, String.valueOf(Integer.parseInt(EVENT_DETAILS.get(LIKES)) + 1));
								txtEventLikeCount.setText(EVENT_DETAILS.get(LIKES));
								if (EVENT_DETAILS.get(DISLIKED).equals("1")) {
									EVENT_DETAILS.put(DISLIKES, String.valueOf(Integer.parseInt(EVENT_DETAILS.get(DISLIKES)) - 1));
									EVENT_DETAILS.put(DISLIKED, "0");
									txtEventDislikeCount.setText(EVENT_DETAILS.get(DISLIKES));
								}
							} else {
								responseErrorMessageHandler(responseCode, false);
							}
							txtEventLikeCount.setClickable(true);
						}
					});
				}
			}
		});

		txtEventDislikeCount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (EVENT_DETAILS.get(DISLIKED).equals("1")) {
					txtEventDislikeCount.setClickable(false);
					provider.unlikeEvent(IN_EVENT_DETAILS.get(ID), new WebCallListener() {

						@Override
						public void onProgressUpdate(int progressCount) {

						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								updateHeader(provider.getNotificationData());
								IjoomerApplicationConfiguration.setReloadRequired(true);
								EVENT_DETAILS.put(DISLIKED, "0");
								EVENT_DETAILS.put(DISLIKES, String.valueOf(Integer.parseInt(EVENT_DETAILS.get(DISLIKES)) - 1));
								txtEventDislikeCount.setText(EVENT_DETAILS.get(DISLIKES));
							} else {
								responseErrorMessageHandler(responseCode, false);
							}
							txtEventDislikeCount.setClickable(true);
						}
					});
				} else {
					txtEventDislikeCount.setClickable(false);
					provider.dislikeEvent(IN_EVENT_DETAILS.get(ID), new WebCallListener() {

						@Override
						public void onProgressUpdate(int progressCount) {

						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								updateHeader(provider.getNotificationData());
								IjoomerApplicationConfiguration.setReloadRequired(true);
								EVENT_DETAILS.put(DISLIKED, "1");
								EVENT_DETAILS.put(DISLIKES, String.valueOf(Integer.parseInt(EVENT_DETAILS.get(DISLIKES)) + 1));
								txtEventDislikeCount.setText(EVENT_DETAILS.get(DISLIKES));
								if (EVENT_DETAILS.get(LIKED).equals("1")) {
									EVENT_DETAILS.put(LIKES, String.valueOf(Integer.parseInt(EVENT_DETAILS.get(LIKES)) - 1));
									EVENT_DETAILS.put(LIKED, "0");
									txtEventLikeCount.setText(EVENT_DETAILS.get(LIKES));
								}
							} else {
								responseErrorMessageHandler(responseCode, false);
							}
							txtEventDislikeCount.setClickable(true);
						}
					});
				}
			}
		});

		rdbEventAttendingNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					if (!EVENT_DETAILS.get(MYSTATUS).equals("2")) {
						provider.eventResponse(IN_EVENT_DETAILS.get(ID), "2", new WebCallListener() {
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
									rdbEventAttendingYes.setChecked(true);
									responseErrorMessageHandler(responseCode, false);
								}
							}
						});
					}
				} catch (Exception e) {
				}

			}
		});

		rdbEventAttendingYes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					if (!EVENT_DETAILS.get(MYSTATUS).equals("1")) {
						provider.eventResponse(IN_EVENT_DETAILS.get(ID), "1", new WebCallListener() {
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
									rdbEventAttendingNo.setChecked(true);
									responseErrorMessageHandler(responseCode, false);
								}
							}
						});
					}
				} catch (Exception e) {
				}

			}
		});

	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {

	}

	/**
	 * Class methods
	 */

	/**
	 * This method used to get intent data.
	 */
	@SuppressWarnings("unchecked")
	private void getIntentData() {
		IN_EVENT_DETAILS = ((HashMap<String, String>) getIntent().getSerializableExtra("IN_EVENT_DETAILS")) == null ? new HashMap<String, String>()
				: ((HashMap<String, String>) getIntent().getSerializableExtra("IN_EVENT_DETAILS"));
		IN_USERID = getIntent().getStringExtra("IN_USERID") == null ? "0" : getIntent().getStringExtra("IN_USERID");
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
	 * This method used to set event details.
	 */
	private void setEventDetails() {

		provider.getEventDetails(IN_EVENT_DETAILS.get(ID), new WebCallListener() {
			final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

			@Override
			public void onProgressUpdate(int progressCount) {
				proSeekBar.setProgress(progressCount);
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				if (responseCode == 200) {
					updateHeader(provider.getNotificationData());
					EVENT_DETAILS = data1.get(0);
					if (EVENT_DETAILS.containsKey(MENU)) {
						try {
							menu = new JSONObject(EVENT_DETAILS.get(MENU));
							if (menu.getString(EDITEVENT).equals("1")) {
								txtEventEdit.setVisibility(View.VISIBLE);
							}
							if (menu.getString(DELETEEVENT).equals("1")) {
								txtEventRemove.setVisibility(View.VISIBLE);
								imgAddEditCoverPhoto.setVisibility(View.VISIBLE);
							}

							if (menu.getString(IGNOREEVENT).equals("1")) {
								txtEventIgnor.setVisibility(View.VISIBLE);
							}
							if (menu.getString(SENDMAIL).equals("1")) {
								txtEventSendEmail.setVisibility(View.VISIBLE);
							}
							if (menu.getString(YOURRESPONSE).equals("1")) {
								lnrEventAttending.setVisibility(View.VISIBLE);
							} else {
								lnrEventAttending.setVisibility(View.GONE);
							}

							if (menu.getString(EDITAVATAR).equals("1")) {
								framEventEditAvatar.setVisibility(View.VISIBLE);
							}

							if (menu.getString(ISMAP).equals("1")) {
								imgEventMap.setVisibility(View.VISIBLE);
							} else {
								imgEventMap.setVisibility(View.GONE);
							}
						} catch (Exception e) {
						}

					} else {
						lnrEventAttending.setVisibility(View.GONE);
					}

					if (txtEventEdit.getVisibility() == View.GONE && txtEventRemove.getVisibility() == View.GONE && txtEventSendEmail.getVisibility() == View.GONE) {
						lnrEventHeader.setVisibility(View.GONE);
					}

					if (EVENT_DETAILS.get(ALLOWINVITE).equals("1")) {
						btnEventInvite.setVisibility(View.VISIBLE);
					}
					if (EVENT_DETAILS.get(ISOPEN).equals("0")) {
						txtPrivateEvent.setVisibility(View.VISIBLE);
					}
					if (EVENT_DETAILS.get(ISWAITINGAPPROVAL).equals("1")) {
						txtPrivateEventAwiting.setVisibility(View.VISIBLE);
						lnrEventAttending.setVisibility(View.GONE);
					}
					if (EVENT_DETAILS.containsKey(MEMBERWAITING) && Integer.parseInt(EVENT_DETAILS.get(MEMBERWAITING)) > 0) {
						txtPrivateEventApprovalRequestList.setVisibility(View.VISIBLE);
						txtPrivateEventApprovalRequestList.setText(String.format(getString(R.string.private_approval_request), Integer.parseInt(EVENT_DETAILS.get(MEMBERWAITING))));
					}
					if (EVENT_DETAILS.get(ISINVITATION).equals("0") && EVENT_DETAILS.get(MYSTATUS).equals("0") && EVENT_DETAILS.get(ISOPEN).equals("0")
							&& EVENT_DETAILS.get(ISCOMMUNITYADMIN).equals("0")) {
						lnrEventAttending.setVisibility(View.GONE);
						lnrPrivateEventAttending.setVisibility(View.VISIBLE);
					} else {
						lnrEventAttending.setVisibility(View.VISIBLE);
						if (EVENT_DETAILS.get(MYSTATUS).equals("0")) {
							rdbEventAttendingYes.setChecked(false);
							rdbEventAttendingNo.setChecked(false);
							if (EVENT_DETAILS.get(ISCOMMUNITYADMIN).equals("0")) {
								lnrEventWriteComment.setVisibility(View.GONE);
							} else {
								getEventWall();
							}
						} else if (EVENT_DETAILS.get(MYSTATUS).equals("1")) {
							rdbEventAttendingYes.setChecked(true);
							rdbEventAttendingNo.setChecked(false);
							getEventWall();
						} else {
							rdbEventAttendingYes.setChecked(false);
							rdbEventAttendingNo.setChecked(true);
							getEventWall();
						}

					}

					if (EVENT_DETAILS.containsKey(ISINVITATION) && EVENT_DETAILS.get(ISINVITATION).equals("1")) {
						lnrInvitation.setVisibility(View.VISIBLE);
						androidQuery.id(imgInvitationIcon).image(EVENT_DETAILS.get(INVITATIONICON), true, true, getDeviceWidth(), 0);
						txtInvitationMessage.setText(addClickablePart(Html.fromHtml(EVENT_DETAILS.get(INVITATIONMESSAGE)), EVENT_DETAILS), BufferType.SPANNABLE);
						txtInvitationMessage.setMovementMethod(LinkMovementMethod.getInstance());
					}
					androidQuery.id(imgCoverPhoto).image(EVENT_DETAILS.get(COVER));
					androidQuery.id(imgEventAvatar).image(EVENT_DETAILS.get(AVATAR), true, true, getDeviceWidth(), 0);
					String[] dateFormate = IN_EVENT_DETAILS.get(DATE).toString().split(" ");
					txtEventDate.setText(dateFormate[1] + " " + dateFormate[0]);
					txtEventTitle.setText(IN_EVENT_DETAILS.get(TITLE));
					txtEventBy.setText(String.format(getString(R.string.by), EVENT_DETAILS.get(USER_NAME)));
					txtEventCategory.setText(EVENT_DETAILS.get(CATEGORY));
					if (EVENT_DETAILS.get(AVAILABLE_SEATS).trim().length() > 0 && EVENT_DETAILS.get(TOTAL_SEATS).trim().length() > 0) {
						txtEventAvilableSeats.setText(getString(R.string.event_seats) + "( " + EVENT_DETAILS.get(AVAILABLE_SEATS) + " / " + EVENT_DETAILS.get(TOTAL_SEATS) + " )");
					} else {
						txtEventAvilableSeats.setText(getString(R.string.event_unlimited_seats));
					}

					txtEventWhenData.setText(IN_EVENT_DETAILS.get(STARTDATE) + " - " + IN_EVENT_DETAILS.get(ENDDATE));
					txtEventWhereData.setText(IN_EVENT_DETAILS.get(LOCATION));
					txtEventDescriptionData.setText(Html.fromHtml(EVENT_DETAILS.get(DESCRIPTION)));
					IjoomerUtilities.IjoomerTextViewResizable(txtEventDescriptionData, 3, getString(R.string.see_more));
					txtEventSummeryData.setText(Html.fromHtml(EVENT_DETAILS.get(SUMMARY)));
					IjoomerUtilities.IjoomerTextViewResizable(txtEventSummeryData, 3, getString(R.string.see_more));
					txtEventLikeCount.setText(EVENT_DETAILS.get(LIKES));
					txtEventDislikeCount.setText(EVENT_DETAILS.get(DISLIKES));
					txtEventCommentCount.setText(EVENT_DETAILS.get(COMMENTS));
				} else {
					responseErrorMessageHandler(responseCode, true);
				}
			}
		});

	}

	/**
	 * This method used to get event wall.
	 */
	private void getEventWall() {

		wallDataProvider.restorePagingSettings();
		listFooterVisible();
		wallDataProvider.getWallList(IN_EVENT_DETAILS.get(ID), "event", new WebCallListenerWithCacheInfo() {

			@Override
			public void onProgressUpdate(int progressCount) {
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2, int pageNo, int pageLimit, boolean fromCache) {
				listFooterInvisible();
				txtRecentActivities.setVisibility(View.VISIBLE);
				if (responseCode == 200) {
					txtRecentActivities.setText(getString(R.string.recent_activities));
					if (!fromCache) {
						updateHeader(wallDataProvider.getNotificationData());
					}
					prepareWallList(data1, false, fromCache, pageNo, pageLimit);
					commentAdapter = getListAdapter();
					lstEventComment.setAdapter(commentAdapter);
				} else {
					if (responseCode == 204) {
						txtRecentActivities.setText(getString(R.string.no_recent_activities));
					} else {
						responseErrorMessageHandler(responseCode, false);
					}
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
		IjoomerUtilities.getCustomOkDialog(getString(R.string.event), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())),
				getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

			@Override
			public void NeutralMethod() {
				if (responseCode == 599 && finishActivityOnConnectionProblem) {
					finish();
				}
			}
		});
	}

	/**
	 * This method used to update seats count.
	 * 
	 * @param isIncress
	 *            represented incress count
	 */
	private void updateSeatCount(boolean isIncress) {
		if (EVENT_DETAILS.get(TOTAL_SEATS).trim().length() > 0 && !EVENT_DETAILS.get(TOTAL_SEATS).trim().equals("0")) {
			if (isIncress) {
				EVENT_DETAILS.put(AVAILABLE_SEATS, String.valueOf((Integer.parseInt(EVENT_DETAILS.get(AVAILABLE_SEATS)) - 1)));
				txtEventAvilableSeats.setText(getString(R.string.event_seats) + "( " + EVENT_DETAILS.get(AVAILABLE_SEATS) + " / " + EVENT_DETAILS.get(TOTAL_SEATS) + " )");
			} else {
				EVENT_DETAILS.put(AVAILABLE_SEATS, String.valueOf((Integer.parseInt(EVENT_DETAILS.get(AVAILABLE_SEATS)) + 1)));
				txtEventAvilableSeats.setText(getString(R.string.event_seats) + "( " + EVENT_DETAILS.get(AVAILABLE_SEATS) + " / " + EVENT_DETAILS.get(TOTAL_SEATS) + " )");
			}
		}

	}

	/**
	 * This method used to get send mail dialog.
	 * 
	 * @param target
	 *            represented {@link ReportListner}
	 */
	private void getSendMailDialog(final ReportListner target) {
		final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
		dialog.setContentView(R.layout.jom_event_send_mail_dialog);
		final IjoomerEditText edtEventSendMailTitle = (IjoomerEditText) dialog.findViewById(R.id.edtEventSendMailTitle);
		final IjoomerEditText edtEventSendMailMessage = (IjoomerEditText) dialog.findViewById(R.id.edtEventSendMailMessage);
		IjoomerButton btnCancel = (IjoomerButton) dialog.findViewById(R.id.btnCancel);
		IjoomerButton btnSend = (IjoomerButton) dialog.findViewById(R.id.btnSend);

		btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				hideSoftKeyboard();
				dialog.dismiss();
				boolean validationFlag = true;
				if (edtEventSendMailTitle.getText().toString().trim().length() <= 0) {
					edtEventSendMailTitle.setError(getString(R.string.validation_value_required));
					validationFlag = false;
				}
				if (edtEventSendMailMessage.getText().toString().trim().length() <= 0) {
					edtEventSendMailMessage.setError(getString(R.string.validation_value_required));
					validationFlag = false;
				}
				if (validationFlag) {
					target.onClick(edtEventSendMailTitle.getText().toString().trim(), edtEventSendMailMessage.getText().toString().trim());
				}
			}

		});
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				hideSoftKeyboard();
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/**
	 * This method used to load event confirmed guests.
	 */
	private void loadConfirmedGuests() {
		guestDataProvider = new JomEventDataProvider(this);

		guestDataProvider.getEventGuestList(IN_EVENT_DETAILS.get(ID), new WebCallListener() {
			@Override
			public void onProgressUpdate(int progressCount) {
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				if (responseCode == 200 && data1 != null && data1.size() > 0) {
					updateHeader(guestDataProvider.getNotificationData());
					lnrConfirmedGuests.setVisibility(View.VISIBLE);
					guestList = data1;
					prepareConfirmGuests(data1);
				} else {
					guestList = null;
					lnrConfirmedGuests.setVisibility(View.GONE);
				}
			}
		});
	}

	/**
	 * This method used to prepare list event confirmed guests.
	 * 
	 * @param data
	 */
	private void prepareConfirmGuests(ArrayList<HashMap<String, String>> data) {
		int length = data.size() > 5 ? 5 : data.size();
		lnrScrollable.removeAllViews();
		for (int i = 0; i < length; i++) {
			LinearLayout lnrGuest = new LinearLayout(this);
			lnrGuest.setOrientation(LinearLayout.VERTICAL);
			lnrGuest.setBackgroundColor(Color.parseColor(getString(R.color.jom_bg_color)));
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
			params.setMargins(5, 5, 5, 5);
			lnrGuest.setPadding(2, 2, 2, 2);

			ImageView imgUser = new ImageView(this);
			imgUser.setScaleType(ScaleType.FIT_XY);
			imgUser.setClickable(false);
			LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(convertSizeToDeviceDependent(50), convertSizeToDeviceDependent(50));
			imgParams.gravity = Gravity.CENTER;

			IjoomerTextView txtUserName = new IjoomerTextView(this);
			txtUserName.setGravity(Gravity.CENTER);
			txtUserName.setClickable(false);
			txtUserName.setTextColor(Color.parseColor(getString(R.color.jom_blue)));
			txtUserName.setBackgroundColor(Color.parseColor(getString(R.color.jom_white)));
			LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(convertSizeToDeviceDependent(50), LinearLayout.LayoutParams.MATCH_PARENT);
			txtParams.gravity = Gravity.CENTER;

			androidQuery.id(imgUser).image(data.get(i).get(USER_AVATAR), true, true, getDeviceWidth(), 0);
			txtUserName.setText(data.get(i).get(USER_NAME));
			doEllipsize(txtUserName, 2);

			lnrGuest.addView(imgUser, imgParams);
			lnrGuest.addView(txtUserName, txtParams);

			lnrScrollable.addView(lnrGuest, params);
			lnrGuest.setTag(data.get(i));

			lnrGuest.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					@SuppressWarnings("unchecked")
					final HashMap<String, String> guest = (HashMap<String, String>) v.getTag();

					if (guest.get("user_profile").equalsIgnoreCase("1")) {
						gotoProfile(guest.get("user_id"));
					}
				}
			});

		}

	}

	/**
	 * This method used to show waiting approvak dialog.
	 */
	@SuppressWarnings("deprecation")
	private void showWaitingApprovalMemberDialog() {

		try {
			int popupWidth = getDeviceWidth() - convertSizeToDeviceDependent(50);
			int popupHeight = getDeviceHeight() - convertSizeToDeviceDependent(200);

			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = layoutInflater.inflate(R.layout.jom_event_guest_popup, null);

			isGuestListResuming = false;
			dialog = new PopupWindow(this);
			dialog.setContentView(layout);
			dialog.setWidth(popupWidth);
			dialog.setHeight(popupHeight);
			dialog.setFocusable(true);
			dialog.setBackgroundDrawable(new BitmapDrawable(getResources()));
			dialog.showAtLocation(layout, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);

			final IjoomerTextView txtHeader = (IjoomerTextView) layout.findViewById(R.id.txtHeader);
			imgTagClose = (ImageView) layout.findViewById(R.id.imgTagClose);
			lstEventMember = (ListView) layout.findViewById(R.id.lstConfirmedGuests);
			lstEventMember.addFooterView(listFooter, null, false);
			lstEventMember.setAdapter(null);
			txtHeader.setText(getString(R.string.event_waiting_list));
			pbrEventWaitingMember = (ProgressBar) layout.findViewById(R.id.pbrGuest);
			listFooterInvisible();
			eventWaitingApprovalMemberList();

			imgTagClose.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			lstEventMember.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {

				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
					if (isGuestListResuming) {
						if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 1) {
							if (!providerEventWaitingApprovalMember.isCalling() && providerEventWaitingApprovalMember.hasNextPage()) {
								listFooterVisible();
								isGuestListResuming = false;
								providerEventWaitingApprovalMember.getEventWaitUserList(IN_EVENT_DETAILS.get(ID), new WebCallListener() {
									@Override
									public void onProgressUpdate(int progressCount) {
									}

									@Override
									public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
										listFooterInvisible();
										if (responseCode == 200 && data1 != null && data1.size() > 0) {
											updateHeader(guestDataProvider.getNotificationData());
											prepareWaitingList(true, data1);
											isGuestListResuming = true;
										} else {
											responseErrorMessageHandler(responseCode, false);
										}
									}
								});
							}
						}
					}
				}
			});

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method used to get event waiting approval member list.
	 */
	private void eventWaitingApprovalMemberList() {
		pbrEventWaitingMember.setVisibility(View.VISIBLE);
		providerEventWaitingApprovalMember = new JomEventDataProvider(this);
		providerEventWaitingApprovalMember.getEventWaitUserList(IN_EVENT_DETAILS.get(ID), new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {

			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				pbrEventWaitingMember.setVisibility(View.GONE);
				if (responseCode == 200) {
					prepareWaitingList(false, data1);
					guestAdapter = getGuestListAdapter(true, eventWaitingApprovalMemberList);
					lstEventMember.setAdapter(guestAdapter);
					isGuestListResuming = true;
				} else {
					lstEventMember.setAdapter(null);
					responseErrorMessageHandler(responseCode, false);
				}
			}
		});

	}

	/**
	 * This method used to show event guest dialog.
	 */
	@SuppressWarnings("deprecation")
	private void showGuestDialog() {
		try {
			int popupWidth = getDeviceWidth() - convertSizeToDeviceDependent(50);
			int popupHeight = getDeviceHeight() - convertSizeToDeviceDependent(200);

			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = layoutInflater.inflate(R.layout.jom_event_guest_popup, null);

			isGuestListResuming = false;
			dialog = new PopupWindow(this);
			dialog.setContentView(layout);
			dialog.setWidth(popupWidth);
			dialog.setHeight(popupHeight);
			dialog.setFocusable(true);
			dialog.setBackgroundDrawable(new BitmapDrawable(getResources()));
			dialog.showAtLocation(layout, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);

			imgTagClose = (ImageView) layout.findViewById(R.id.imgTagClose);
			lstConfirmedGuests = (ListView) layout.findViewById(R.id.lstConfirmedGuests);
			lstConfirmedGuests.addFooterView(listFooter, null, false);
			lstConfirmedGuests.setAdapter(null);
			pbrGuest = (ProgressBar) layout.findViewById(R.id.pbrGuest);
			pbrGuest.setVisibility(View.GONE);
			prepareGuestList(false);
			guestAdapter = getGuestListAdapter(false, guestListData);
			lstConfirmedGuests.setAdapter(guestAdapter);
			isGuestListResuming = true;

			imgTagClose.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			lstConfirmedGuests.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {

				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
					if (isGuestListResuming) {
						if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 1) {
							if (!guestDataProvider.isCalling() && guestDataProvider.hasNextPage()) {
								listFooterVisible();
								isGuestListResuming = false;
								guestDataProvider.getEventGuestList(IN_EVENT_DETAILS.get(ID), new WebCallListener() {
									@Override
									public void onProgressUpdate(int progressCount) {
									}

									@Override
									public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
										listFooterInvisible();
										if (responseCode == 200 && data1 != null && data1.size() > 0) {
											updateHeader(guestDataProvider.getNotificationData());
											guestList = data1;
											prepareGuestList(true);
											isGuestListResuming = true;
										} else {
											responseErrorMessageHandler(responseCode, false);
										}
									}
								});
							}
						}
					}
				}
			});

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method used to prepare list event confirmed guests.
	 * 
	 * @param lnrScrollable
	 *            represented scrollable view.
	 * @param data
	 *            represented confirmed guest data
	 */
	private void prepareConfirmGuests(LinearLayout lnrScrollable, final HashMap<String, String> data) {
		JSONArray jsonArray;
		int length = 0;
		try {
			jsonArray = new JSONArray(data.get("photo_data"));
			length = jsonArray.length() > 5 ? 5 : jsonArray.length();
		} catch (JSONException e1) {
			e1.printStackTrace();
			jsonArray = new JSONArray();
		}

		lnrScrollable.removeAllViews();
		for (int i = 0; i < length; i++) {
			LinearLayout lnrGuest = new LinearLayout(this);
			lnrGuest.setTag(i);
			lnrGuest.setOrientation(LinearLayout.VERTICAL);
			lnrGuest.setBackgroundColor(Color.parseColor(getString(R.color.jom_bg_color)));
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
			params.setMargins(5, 5, 5, 5);
			lnrGuest.setPadding(2, 2, 2, 2);

			ImageView imgUser = new ImageView(this);
			imgUser.setScaleType(ScaleType.FIT_XY);
			imgUser.setClickable(false);
			LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(convertSizeToDeviceDependent(50), convertSizeToDeviceDependent(50));
			imgParams.gravity = Gravity.CENTER;

			try {
				androidQuery.id(imgUser).image(jsonArray.getJSONObject(i).getString(THUMB), true, true, getDeviceWidth(), 0);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			lnrGuest.addView(imgUser, imgParams);
			lnrScrollable.addView(lnrGuest, params);
			lnrGuest.setTag(data.get(i));

			lnrGuest.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ArrayList<HashMap<String, String>> photoData = new ArrayList<HashMap<String, String>>();
					JSONArray jsonArray = null;
					try {
						jsonArray = new JSONArray(data.get("photo_data"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					int size = jsonArray.length();
					for (int j = 0; j < size; j++) {
						try {
							photoData.add(new HashMap<String, String>(jsonToMap(jsonArray.getJSONObject(j))));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					try {
						loadNew(JomPhotoDetailsActivity.class, JomEventDetailsActivity_v30.this, false, "IN_PHOTO_LIST", photoData, "IN_SELECTED_INDEX", (Integer) v.getTag(),
								"IN_TOTAL_COUNT", photoData.size(), "IN_ALBUM", new HashMap<String, String>(jsonToMap(new JSONObject(data.get(CONTENT_DATA)))));
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			});

		}
	}

	/**
	 * This method used to prepare list for event confirmed guest.
	 * 
	 * @param append
	 *            represented data append
	 */
	private void prepareGuestList(boolean append) {
		if (guestList != null) {
			if (!append) {
				guestListData.clear();
			}
			for (HashMap<String, String> hashmap : guestList) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.jom_event_guest_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hashmap);
				item.setValues(obj);
				if (append) {
					guestAdapter.add(item);
				} else {
					guestListData.add(item);
				}
			}

		}

	}

	/**
	 * This method used to prepare list for event waiting member .
	 * 
	 * @param append
	 *            represented data append
	 * @param data
	 *            represented waiting member data
	 */
	private void prepareWaitingList(boolean append, ArrayList<HashMap<String, String>> data) {
		if (data != null) {
			if (!append) {
				eventWaitingApprovalMemberList.clear();
			}
			for (HashMap<String, String> hashMap : data) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.jom_event_guest_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hashMap);
				item.setValues(obj);
				if (append) {
					guestAdapter.add(item);
				} else {
					eventWaitingApprovalMemberList.add(item);
				}
			}

		}

	}

	/**
	 * This method used to prepare list for event wall.
	 * 
	 * @param data
	 *            represented wall data
	 * @param append
	 *            represented data append
	 * @param fromCache
	 *            represented data from cache
	 * @param pageno
	 *            represented page no
	 * @param pagelimit
	 *            represented page limit
	 */
	public void prepareWallList(ArrayList<HashMap<String, String>> data, boolean append, boolean fromCache, int pageno, int pagelimit) {
		if (data != null) {
			if (!append) {
				wallListData.clear();
				recordCommentTotal = 0;
			} else {
				if (!fromCache) {
					int startIndex = ((pageno - 1) * pagelimit);
					int endIndex = commentAdapter.getCount();
					if (startIndex > 0) {
						for (int i = endIndex; i >= startIndex; i--) {
							try {
								commentAdapter.remove(commentAdapter.getItem(i));
								wallListData.remove(i);
							} catch (Exception e) {
							}
						}
					}
				}
			}
			for (HashMap<String, String> hashMap : data) {
				if (!fromCache && getAudio(hashMap.get(TITLETAG)) != null || getAudio(hashMap.get(CONTENT)) != null) {
					recordCommentTotal += 1;
				}
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.jom_wall_activity_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hashMap);
				item.setValues(obj);
				if (append) {
					commentAdapter.add(item);
				} else {
					wallListData.add(item);
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
	 * List adapter for event guest.
	 */
	private SmartListAdapterWithHolder getGuestListAdapter(final boolean isWaitingList, final ArrayList<SmartListItem> data) {

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.jom_event_guest_list_item, data, new ItemView() {
			@Override
			public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {
				holder.imgEventGuestAvatar = (ImageView) v.findViewById(R.id.imgEventGuestAvatar);
				holder.txtEventGuestName = (IjoomerTextView) v.findViewById(R.id.txtEventGuestName);
				holder.btnEventGuestRemove = (IjoomerButton) v.findViewById(R.id.btnEventGuestRemove);
				holder.imgEventGuestOnlineStatus = (ImageView) v.findViewById(R.id.imgEventGuestOnlineStatus);
				holder.lnrEventGuestRemove = (LinearLayout) v.findViewById(R.id.lnrEventGuestRemove);
				holder.txtEventGuestRemove = (IjoomerTextView) v.findViewById(R.id.txtEventGuestRemove);
				holder.chbEventGuestBock = (IjoomerCheckBox) v.findViewById(R.id.chbEventGuestBock);
				holder.btnEventGuestYes = (IjoomerButton) v.findViewById(R.id.btnEventGuestYes);
				holder.btnEventGuestNo = (IjoomerButton) v.findViewById(R.id.btnEventGuestNo);
				holder.txtEventGuestSetAsAdmin = (IjoomerTextView) v.findViewById(R.id.txtEventGuestSetAsAdmin);
				holder.txtEventGuestSetAsMember = (IjoomerTextView) v.findViewById(R.id.txtEventGuestSetAsMember);
				holder.txtEventGuestApproval = (IjoomerTextView) v.findViewById(R.id.txtEventGuestApproval);
				holder.imgEventGuestOnlineStatus.setImageDrawable(getResources().getDrawable(R.drawable.jom_friend_member_offline));
				holder.lnrEventGuestRemove.setVisibility(View.GONE);

				if (isWaitingList) {
					holder.txtEventGuestApproval.setVisibility(View.VISIBLE);
				}
				@SuppressWarnings("unchecked")
				final HashMap<String, String> guest = (HashMap<String, String>) item.getValues().get(0);
				holder.txtEventGuestName.setText(guest.get(USER_NAME));
				androidQuery.id(holder.imgEventGuestAvatar).image(guest.get(USER_AVATAR), true, true, getDeviceWidth(), 0);

				if (guest.get("user_online").equalsIgnoreCase("1")) {
					holder.imgEventGuestOnlineStatus.setImageDrawable(getResources().getDrawable(R.drawable.jom_friend_member_online));
				}

				if (guest.get("canRemove").equalsIgnoreCase("1")) {
					holder.btnEventGuestRemove.setVisibility(View.VISIBLE);
				} else {
					holder.btnEventGuestRemove.setVisibility(View.GONE);
				}

				if (guest.get("canAdmin").equalsIgnoreCase("1")) {
					holder.txtEventGuestSetAsAdmin.setVisibility(View.VISIBLE);
				} else {
					holder.txtEventGuestSetAsAdmin.setVisibility(View.GONE);
				}
				if (guest.get("canMember").equalsIgnoreCase("1")) {
					holder.txtEventGuestSetAsMember.setVisibility(View.VISIBLE);
				} else {
					holder.txtEventGuestSetAsMember.setVisibility(View.GONE);
				}

				if (guest.get("user_id").equalsIgnoreCase("0")) {
					holder.txtEventGuestSetAsAdmin.setVisibility(View.GONE);
					holder.txtEventGuestSetAsMember.setVisibility(View.GONE);
				}

				holder.txtEventGuestApproval.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						provider.approvWaitingUser(guest.get(USER_ID), IN_EVENT_DETAILS.get(ID), new WebCallListener() {
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
									EVENT_DETAILS.put(MEMBERWAITING, "" + (Integer.parseInt(EVENT_DETAILS.get(MEMBERWAITING)) - 1));
									if (Integer.parseInt(EVENT_DETAILS.get(MEMBERWAITING)) <= 0) {
										txtPrivateEventApprovalRequestList.setVisibility(View.GONE);
									} else {
										txtPrivateEventApprovalRequestList.setText(String.format(getString(R.string.private_event_approval_awaiting),
												Integer.parseInt(EVENT_DETAILS.get(MEMBERWAITING))));
									}
									loadConfirmedGuests();
									guestAdapter.remove(guestAdapter.getItem(position));
									if (guestAdapter.getCount() <= 0) {
										dialog.dismiss();
									}
								} else {
									responseErrorMessageHandler(responseCode, false);
								}
							}
						});
					}
				});
				holder.txtEventGuestName.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (guest.get("user_profile").equalsIgnoreCase("1")) {
							gotoProfile(guest.get("user_id"));
						}

					}
				});

				holder.btnEventGuestRemove.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						holder.lnrEventGuestRemove.setVisibility(View.VISIBLE);
					}
				});
				holder.btnEventGuestYes.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						provider.removeOrBlockMember(guest.get("user_id"), IN_EVENT_DETAILS.get(ID), holder.chbEventGuestBock.isChecked(), new WebCallListener() {
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
									if (isWaitingList) {
										EVENT_DETAILS.put(MEMBERWAITING, "" + (Integer.parseInt(EVENT_DETAILS.get(MEMBERWAITING)) - 1));
										if (Integer.parseInt(EVENT_DETAILS.get(MEMBERWAITING)) <= 0) {
											txtPrivateEventApprovalRequestList.setVisibility(View.GONE);
										} else {
											txtPrivateEventApprovalRequestList.setText(String.format(getString(R.string.private_event_approval_awaiting),
													Integer.parseInt(EVENT_DETAILS.get(MEMBERWAITING))));
										}
										loadConfirmedGuests();
									} else {
										loadConfirmedGuests();
									}
									guestAdapter.remove(guestAdapter.getItem(position));
									if (guestAdapter.getCount() <= 0) {
										dialog.dismiss();
									}
								} else {
									responseErrorMessageHandler(responseCode, false);
								}
							}
						});
					}
				});
				holder.btnEventGuestNo.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						holder.lnrEventGuestRemove.setVisibility(View.GONE);
					}
				});
				holder.txtEventGuestSetAsAdmin.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						provider.setUserAsEventAdmin(guest.get(USER_ID), IN_EVENT_DETAILS.get(ID), new WebCallListener() {
							final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

							@Override
							public void onProgressUpdate(int progressCount) {
								proSeekBar.setProgress(progressCount);
							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								if (responseCode == 200) {
									updateHeader(provider.getNotificationData());
									if (isWaitingList) {
										EVENT_DETAILS.put(MEMBERWAITING, "" + (Integer.parseInt(EVENT_DETAILS.get(MEMBERWAITING)) - 1));
										if (Integer.parseInt(EVENT_DETAILS.get(MEMBERWAITING)) <= 0) {
											txtPrivateEventApprovalRequestList.setVisibility(View.GONE);
										} else {
											txtPrivateEventApprovalRequestList.setText(String.format(getString(R.string.private_event_approval_awaiting),
													Integer.parseInt(EVENT_DETAILS.get(MEMBERWAITING))));
										}
										guestAdapter.remove(guestAdapter.getItem(position));
									} else {
										holder.txtEventGuestSetAsAdmin.setVisibility(View.GONE);
										holder.txtEventGuestSetAsMember.setVisibility(View.VISIBLE);
										guest.put("canAdmin", "0");
										guest.put("canMember", "1");
									}
								} else {
									responseErrorMessageHandler(responseCode, false);
								}
							}
						});
					}
				});

				holder.txtEventGuestSetAsMember.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						provider.setUserAsEventMember(IN_USERID, IN_EVENT_DETAILS.get(ID), new WebCallListener() {
							final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

							@Override
							public void onProgressUpdate(int progressCount) {
								proSeekBar.setProgress(progressCount);
							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								if (responseCode == 200) {
									updateHeader(provider.getNotificationData());
									holder.txtEventGuestSetAsAdmin.setVisibility(View.VISIBLE);
									holder.txtEventGuestSetAsMember.setVisibility(View.GONE);
									guest.put("canAdmin", "1");
									guest.put("canMember", "0");
								} else {
									responseErrorMessageHandler(responseCode, false);
								}
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

	/**
	 * List adapter for event wall.
	 * 
	 * @return
	 */
	private SmartListAdapterWithHolder getListAdapter() {
		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.jom_wall_activity_list_item, wallListData, new ItemView() {

			@Override
			public View setItemView(final int position, View v, final SmartListItem item, final ViewHolder holder) {

				holder.imgWallOrActvityUserAvatar = (ImageView) v.findViewById(R.id.imgWallOrActvityUserAvatar);
				holder.txtWallOrActvityUserName = (IjoomerTextView) v.findViewById(R.id.txtWallOrActvityUserName);
				holder.txtWallOrActvityDate = (IjoomerTextView) v.findViewById(R.id.txtWallOrActvityDate);
				holder.txtWallOrActvityTitle = (IjoomerTextView) v.findViewById(R.id.txtWallOrActvityTitle);
				holder.txtWallOrActivityLike = (IjoomerTextView) v.findViewById(R.id.txtWallOrActivityLike);
				holder.txtWallOrActvityContent = (IjoomerTextView) v.findViewById(R.id.txtWallOrActvityContent);
				holder.txtWallOrActivityComment = (IjoomerTextView) v.findViewById(R.id.txtWallOrActivityComment);
				holder.txtWallOrActivityLikeCount = (IjoomerTextView) v.findViewById(R.id.txtWallOrActivityLikeCount);
				holder.txtWallOrActivityCommentCount = (IjoomerTextView) v.findViewById(R.id.txtWallOrActivityCommentCount);
				holder.lnrWallOrActivityContentImage = (LinearLayout) v.findViewById(R.id.lnrWallOrActivityContentImage);
				holder.lnrWallOrActivityContentCoverPhoto = (RelativeLayout) v.findViewById(R.id.lnrWallOrActivityContentCoverPhoto);
				holder.imgWallOrActvityCoverPhoto = (ImageView) v.findViewById(R.id.imgWallOrActvityCoverPhoto);
				holder.lnrWallOrActivityContentVideo = (LinearLayout) v.findViewById(R.id.lnrWallOrActivityContentVideo);
				holder.lnrContentImageScrollable = (LinearLayout) v.findViewById(R.id.lnrContentImageScrollable);
				holder.imgWallOrActvityContentVideoImage = (ImageView) v.findViewById(R.id.imgWallOrActvityContentVideoImage);
				holder.lnrWallOrActivityLikeCommnet = (LinearLayout) v.findViewById(R.id.lnrWallOrActivityLikeCommnet);
				holder.btnWallOrActivityRemove = (IjoomerButton) v.findViewById(R.id.btnWallOrActivityRemove);
				holder.btnPlayStopVoice = (IjoomerVoiceButton) v.findViewById(R.id.btnPlayStopVoice);
				holder.btnPlayStopVoice.setVisibility(View.GONE);
				holder.lnrWallOrActivityContentImage.setVisibility(View.GONE);
				holder.lnrWallOrActivityContentCoverPhoto.setVisibility(View.GONE);
				holder.lnrWallOrActivityContentVideo.setVisibility(View.GONE);
				holder.txtWallOrActvityContent.setVisibility(View.GONE);
				holder.btnWallOrActivityRemove.setVisibility(View.GONE);
				@SuppressWarnings("unchecked")
				final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);

				androidQuery.id(holder.imgWallOrActvityUserAvatar).image(row.get(USER_AVATAR), true, true, getDeviceWidth(), 0);
				holder.txtWallOrActvityTitle.setMovementMethod(LinkMovementMethod.getInstance());
				holder.txtWallOrActvityTitle.setText(addClickablePart(Html.fromHtml(getPlainText(row.get(TITLETAG)).replace("\u25ba", "\u25b6")), row, row.get(TYPE), IN_USERID),
						BufferType.SPANNABLE);
				holder.txtWallOrActvityUserName.setText(row.get(USER_NAME));
				holder.txtWallOrActvityDate.setText(row.get(DATE));

				if (row.containsKey(CONTENT) && row.get(CONTENT).toString().trim().length() > 0) {
					holder.txtWallOrActvityContent.setVisibility(View.VISIBLE);
					holder.txtWallOrActvityContent.setMovementMethod(LinkMovementMethod.getInstance());
					row.put(CONTENT, row.get(CONTENT).replace("\n", " "));
					row.put(CONTENT, row.get(CONTENT).replace("\t", " "));
					row.put(CONTENT, row.get(CONTENT).replace("\r", " "));

					holder.txtWallOrActvityContent.setText(addClickablePart(Html.fromHtml(getPlainText(row.get(CONTENT))), row, row.get(TYPE), IN_USERID), BufferType.SPANNABLE);
				}

				if (getAudio(row.get(TITLETAG)) != null) {

					holder.btnPlayStopVoice.setVisibility(View.VISIBLE);
					holder.btnPlayStopVoice.setText(getAudioLength(row.get(TITLETAG)));
					holder.btnPlayStopVoice.setAudioPath(getAudio(row.get(TITLETAG)), false);
					holder.btnPlayStopVoice.setAudioListener(new AudioListener() {

						@Override
						public void onReportClicked() {
							reportVoice(getAudio(row.get(TITLETAG)));
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

				if (getAudio(row.get(CONTENT)) != null) {

					holder.btnPlayStopVoice.setVisibility(View.VISIBLE);
					holder.btnPlayStopVoice.setText(getAudioLength(row.get(CONTENT)));
					holder.btnPlayStopVoice.setAudioPath(getAudio(row.get(CONTENT)), false);
					holder.btnPlayStopVoice.setAudioListener(new AudioListener() {

						@Override
						public void onReportClicked() {
							reportVoice(getAudio(row.get(CONTENT)));
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
				
				if (row.get(TYPE).toString().trim().equals(COVERUPLOAD) || row.get(TYPE).toString().trim().equals(AVATARUPLOAD)
						||  row.get(TYPE).toString().trim().equals(EVENTSAVATARUPLOAD)) {
					holder.lnrWallOrActivityContentCoverPhoto.setVisibility(View.VISIBLE);
					try{
						JSONObject imageData = new JSONObject(row.get(IMAGE_DATA));
						androidQuery.id(holder.imgWallOrActvityCoverPhoto).progress(R.id.coverimgprogress).image(imageData.getString(URL),true,true);
					}catch (JSONException e){
						e.printStackTrace();
					}
				}else if (row.containsKey(TYPE) && row.get(TYPE).toString().trim().equals(PHOTOS)) {
					prepareConfirmGuests(holder.lnrContentImageScrollable, row);
				}else if (row.containsKey(TYPE) && row.get(TYPE).toString().trim().equals(VIDEOS)) {
					holder.lnrWallOrActivityContentVideo.setVisibility(View.VISIBLE);

					try {
						androidQuery.id(holder.imgWallOrActvityContentVideoImage).image(new JSONObject(row.get(CONTENT_DATA)).getString(THUMB), true, true, getDeviceWidth(), 0);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				if (row.containsKey(DELETEALLOWED) && row.get(DELETEALLOWED).toString().trim().equals("0")) {
					holder.btnWallOrActivityRemove.setVisibility(View.GONE);
				} else {
					holder.btnWallOrActivityRemove.setVisibility(View.VISIBLE);
				}
				if (row.containsKey(LIKECOUNT)) {
					holder.txtWallOrActivityLikeCount.setText(row.get(LIKECOUNT));
				} else {
					holder.txtWallOrActivityLikeCount.setText("0");
				}
				if (row.containsKey(COMMENTCOUNT)) {
					holder.txtWallOrActivityCommentCount.setText(row.get(COMMENTCOUNT));
				} else {
					holder.txtWallOrActivityCommentCount.setText("0");
				}

				if (row.get(LIKEALLOWED).toString().trim().equals("0")) {
					holder.txtWallOrActivityLike.setVisibility(View.GONE);
					holder.txtWallOrActivityLikeCount.setVisibility(View.GONE);
				} else {
					holder.txtWallOrActivityLike.setVisibility(View.VISIBLE);
					holder.txtWallOrActivityLikeCount.setVisibility(View.VISIBLE);
				}
				if (row.get(COMMENTALLOWED).toString().trim().equals("0")) {
					holder.txtWallOrActivityComment.setVisibility(View.GONE);
					holder.txtWallOrActivityCommentCount.setVisibility(View.GONE);
				} else {
					holder.txtWallOrActivityComment.setVisibility(View.VISIBLE);
					holder.txtWallOrActivityCommentCount.setVisibility(View.VISIBLE);
				}

				if (row.get(LIKED).toString().trim().equals("0")) {
					holder.txtWallOrActivityLike.setText(getString(R.string.like));
				} else {
					holder.txtWallOrActivityLike.setText(getString(R.string.unlike));
				}

				holder.txtWallOrActvityUserName.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						gotoProfile(row.get(USER_ID));
					}

				});
				holder.imgWallOrActvityUserAvatar.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						gotoProfile(row.get(USER_ID));
					}

				});
				holder.txtWallOrActivityLikeCount.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if (!row.get(LIKECOUNT).toString().equals("0")) {
							try {
								ArrayList<HashMap<String, String>> IN_WALL_DETAILS = new ArrayList<HashMap<String, String>>();
								IN_WALL_DETAILS.add(row);
								loadNew(JomWallOrActivityDetailActivity.class, JomEventDetailsActivity_v30.this, false, "IN_WALL_DETAILS", IN_WALL_DETAILS,
										"IN_WALL_DETAILS_LIST_TYPE", LIKES, "IN_USERID", IN_USERID);
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					}

				});
				holder.txtWallOrActivityCommentCount.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if (!row.get(COMMENTCOUNT).toString().equals("0")) {
							try {
								ArrayList<HashMap<String, String>> IN_WALL_DETAILS = new ArrayList<HashMap<String, String>>();
								IN_WALL_DETAILS.add(row);
								loadNew(JomWallOrActivityDetailActivity.class, JomEventDetailsActivity_v30.this, false, "IN_WALL_DETAILS", IN_WALL_DETAILS,
										"IN_WALL_DETAILS_LIST_TYPE", COMMENTS, "IN_USERID", IN_USERID);
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					}
				});

				holder.txtWallOrActivityLike.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(final View v) {
						String likeID = "0";
						if (row.get(LIKETYPE).toString().trim().equals(VIDEOS)) {
							try {
								likeID = new JSONObject(row.get(CONTENT_DATA)).getString(ID);
							} catch (JSONException e) {
								e.printStackTrace();
								likeID = "0";
							}
						} else {
							likeID = row.get(ID);
						}
						if (row.get(LIKED).toString().trim().equals("1")) {
							holder.txtWallOrActivityLike.setClickable(false);
							wallDataProvider.unlikeWall(likeID, row.get(LIKETYPE).toString().trim(), new WebCallListener() {
								SeekBar skBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

								@Override
								public void onProgressUpdate(int progressCount) {
									skBar.setProgress(progressCount);
								}

								@Override
								public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
									if (responseCode == 200) {
										((IjoomerTextView) v).setText(getString(R.string.like));
										holder.txtWallOrActivityLikeCount.setText("" + (Integer.parseInt(holder.txtWallOrActivityLikeCount.getText().toString()) - 1));
										row.put(LIKED, "0");
										row.put(LIKECOUNT, holder.txtWallOrActivityLikeCount.getText().toString());
									} else {
										responseErrorMessageHandler(responseCode, false);
									}
									holder.txtWallOrActivityLike.setClickable(true);
								}
							});
						} else {
							holder.txtWallOrActivityLike.setClickable(false);
							wallDataProvider.likeWall(likeID, row.get(LIKETYPE).toString().trim(), new WebCallListener() {
								SeekBar skBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

								@Override
								public void onProgressUpdate(int progressCount) {
									skBar.setProgress(progressCount);
								}

								@Override
								public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
									if (responseCode == 200) {
										((IjoomerTextView) v).setText(getString(R.string.unlike));
										holder.txtWallOrActivityLikeCount.setText("" + (Integer.parseInt(holder.txtWallOrActivityLikeCount.getText().toString()) + 1));
										row.put(LIKED, "1");
										row.put(LIKECOUNT, holder.txtWallOrActivityLikeCount.getText().toString());
									} else {
										responseErrorMessageHandler(responseCode, false);
									}
									holder.txtWallOrActivityLike.setClickable(true);
								}
							});
						}
					}
				});

				holder.btnWallOrActivityRemove.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						IjoomerUtilities.getCustomConfirmDialog(getString(R.string.wall_remove), getString(R.string.are_you_sure), getString(R.string.yes), getString(R.string.no),
								new CustomAlertMagnatic() {

							@Override
							public void PositiveMethod() {
								wallDataProvider.removeWall(row.get(ID), row.get(USER_ID), row.get(COMMENTTYPE), new WebCallListener() {

									@Override
									public void onProgressUpdate(int progressCount) {

									}

									@Override
									public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
										if (responseCode == 200) {
											commentAdapter.remove(commentAdapter.getItem(position));
											if (getAudio(row.get(TITLETAG)) != null || getAudio(row.get(CONTENT)) != null) {
												recordCommentTotal -= 1;
												txtTotalRecordComment.setText("(" + recordCommentTotal + ")");
												if (recordCommentTotal == 1) {
													lnrPlayRecordComment.setVisibility(View.GONE);
												}
											}
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

				holder.txtWallOrActivityComment.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							ArrayList<HashMap<String, String>> IN_WALL_DETAILS = new ArrayList<HashMap<String, String>>();
							IN_WALL_DETAILS.add(row);
							loadNew(JomWallOrActivityDetailActivity.class, JomEventDetailsActivity_v30.this, false, "IN_WALL_DETAILS", IN_WALL_DETAILS,
									"IN_WALL_DETAILS_LIST_TYPE", COMMENTS, "IN_USERID", IN_USERID);
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				});

				holder.imgWallOrActvityContentVideoImage.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						try {
							String url = new JSONObject(row.get(CONTENT_DATA)).getString(URL);
							Intent lVideoIntent = new Intent(null, getVideoPlayURI(url), JomEventDetailsActivity_v30.this, IjoomerMediaPlayer.class);
							startActivity(lVideoIntent);
						} catch (Exception e) {
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
