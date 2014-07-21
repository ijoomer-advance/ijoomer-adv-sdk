package com.ijoomer.components.jomsocial;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
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
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ijoomer.common.classes.IjoomerFileChooserActivity;
import com.ijoomer.common.classes.IjoomerShareActivity;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.custom.interfaces.AnnouncementAndDiscussionListner;
import com.ijoomer.custom.interfaces.ReportListner;
import com.ijoomer.custom.interfaces.SelectImageDialogListner;
import com.ijoomer.customviews.IjoomerAudioPlayer.AudioListener;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.customviews.IjoomerVoiceAndTextMessager;
import com.ijoomer.customviews.IjoomerVoiceAndTextMessager.MessageHandler;
import com.ijoomer.customviews.IjoomerVoiceButton;
import com.ijoomer.library.jomsocial.JomGroupDataProvider;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To JomGroupDetailsActivity.
 * 
 * @author tasol
 * 
 */
public class JomGroupDetailsActivity_v30 extends JomMasterActivity {

	private LinearLayout lnrGroupCreateDiscussion;
	private LinearLayout lnrGroupHeader;
	private LinearLayout lnrGroupActivitiesAnnouncementDiscussionList;
	private LinearLayout lnrGroupOptionsList;
	private LinearLayout lnrGroupWriteComment;
	private LinearLayout lnrGroupCreateAnnouncement;
	private LinearLayout listFooter;
	private LinearLayout lnrInvitation;
	private LinearLayout lnrPlayRecordComment;
	private ListView lstGroupDetails;
	private ListView lstGroupMember;
	private ListView lstGroupFiles;
	private IjoomerTextView txtGroupTitle;
	private IjoomerTextView txtGroupCreatedBy;
	private IjoomerTextView txtGroupCreatedOn;
	private IjoomerTextView txtGroupNoRecentActivities;
	private IjoomerTextView txtGroupNoDiscussion;
	private IjoomerTextView txtGroupNoAnnouncement;
	private IjoomerTextView txtGroupCategory;
	private IjoomerTextView txtGroupDetails;
	private IjoomerTextView txtGroupLikeCount;
	private IjoomerTextView txtGroupDislikeCount;
	private IjoomerTextView txtGroupFileCount;
	private IjoomerTextView txtGroupShare;
	private IjoomerTextView txtGroupReport;
	private IjoomerTextView txtGroupActivities;
	private IjoomerTextView txtGroupAnnouncement;
	private IjoomerTextView txtGroupDiscussion;
	private IjoomerTextView txtGroupMember;
	private IjoomerTextView txtGroupPhoto;
	private IjoomerTextView txtGroupVideo;
	private IjoomerTextView txtGroupEvent;
	private IjoomerTextView txtGroupEdit;
	private IjoomerTextView txtGroupSendEmail;
	private IjoomerTextView txtGroupRemove;
	private IjoomerTextView txtGroupUnpublish;
	private IjoomerTextView txtGroupAllMember;
	private IjoomerTextView txtGroupBanMember;
	private IjoomerTextView txtInvitationMessage;
	private IjoomerTextView txtInvitationAccept;
	private IjoomerTextView txtInvitationReject;
	private IjoomerTextView txtPrivateGroupApprovalRequestList;
	private IjoomerTextView txtPrivateGroupAwiting;
	private IjoomerTextView txtPrivateGroup;
	private IjoomerTextView txtTotalRecordComment;
	private IjoomerTextView txtRecordUser;
	private IjoomerButton btnGroupJoin;
	private IjoomerButton btnGroupLeave;
	private IjoomerButton btnGroupInviteFriend;
	private IjoomerButton btnGroupCreateAnnouncement;
	private IjoomerButton btnGroupCreateDiscussion;
	private IjoomerVoiceButton btnPlayAll;
	private IjoomerVoiceAndTextMessager voiceMessager;
	private ImageView imgGroupAvatar;
	private ImageView imgTagClose;
	private ImageView imgGroupEditAvatar;
	private ImageView imgInvitationIcon;
	private FrameLayout framGroupEditAvatar;
	private PopupWindow dialog;
	private ProgressBar pbrGroupMember;
	private ProgressBar pbrGroupFiles;
	private Dialog dialogAnnouncementOrDiscussion = null;
	private ViewGroup groupDetailsHeaderLayout;

	private ImageView imgCoverPhoto;
	private ImageView imgAddEditCoverPhoto;

	private AQuery androidQuery;
	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private ArrayList<SmartListItem> guestListData = new ArrayList<SmartListItem>();
	private ArrayList<SmartListItem> fileListData = new ArrayList<SmartListItem>();
	private ArrayList<HashMap<String, String>> fileList;
	private ArrayList<HashMap<String, String>> activitiesList;
	private ArrayList<HashMap<String, String>> announcementList;
	private ArrayList<HashMap<String, String>> discussionList;
	private ArrayList<HashMap<String, String>> groupAllMemberList;
	private ArrayList<HashMap<String, String>> groupBanMemberList;
	private ArrayList<HashMap<String, String>> groupWaitingApprovalMemberList;
	private HashMap<String, String> IN_GROUP_DETAILS;
	private HashMap<String, String> GROUP_DETAILS;
	private JSONObject adminMenu;
	private JSONObject menu;
	private JSONObject option;
	private SmartListAdapterWithHolder wallListAdapterWithHolder;
	private SmartListAdapterWithHolder announcementListAdapterWithHolder;
	private SmartListAdapterWithHolder discussionListAdapterWithHolder;
	private SmartListAdapterWithHolder guestAdapter;
	private SmartListAdapterWithHolder fileListAdapter;

	private JomGroupDataProvider provider;
	private JomGroupDataProvider providerAnnoucement;
	private JomGroupDataProvider providerDiscussion;
	private JomWallDataProvider wallDataProvider;
	private JomGroupDataProvider providerGroupAllMember;
	private JomGroupDataProvider providerGroupBanMember;
	private JomGroupDataProvider providerGroupWaitingApprovalMember;
	private JomGroupDataProvider fileDataProvider;

	final private int PICK_IMAGE = 1;
	final private int CAPTURE_IMAGE = 2;
	final private String ACTIVITES_LIST = "activites_list";
	final private String ANNOUNCEMENT_LIST = "announcement_list";
	final private String DISCUSSION_LIST = "discussion_list";
	final private int DOWNLOAD_FILE_LOCATION = 5;
	private String IN_USERID;
	private String MEMEBERLIST = "memberList";
	private String BANMEMBERLIST = "banmemberList";
	private String currentList = MEMEBERLIST;
	private String CURRENT_LIST = ACTIVITES_LIST;
	private String selectedImagePath;
	private String createEvent = "0";
	private String uploadPhoto = "0";
	private String addAlbum = "0";
	private String addVideo = "0";
	private boolean isCreateAnnouncement;
	private boolean isCreateDiscussion;
	private boolean isActivityResuming = false;
	private boolean isGuestListResuming = false;
	private int downlodIndex = 0;
	private int recordCommentCounter;
	private int recordCommentTotal;
	private int recordCommentLast;
	private boolean isSetGroupCoverPage;

	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.jom_group_details;
	}

	@Override
	public void initComponents() {

		lstGroupDetails = (ListView) findViewById(R.id.lstGroupDetails);
		groupDetailsHeaderLayout = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.jom_group_details_header_v30, null);
		listFooter = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.ijoomer_list_footer, null);
		lnrGroupHeader = (LinearLayout) findViewById(R.id.lnrGroupHeader);
		lnrGroupWriteComment = (LinearLayout) findViewById(R.id.lnrGroupWriteComment);
		lstGroupDetails.addHeaderView(groupDetailsHeaderLayout, null, false);
		lstGroupDetails.addFooterView(listFooter, null, false);
		lstGroupDetails.setAdapter(null);

		imgCoverPhoto = (ImageView) groupDetailsHeaderLayout.findViewById(R.id.imgCoverPhoto);
		imgAddEditCoverPhoto = (ImageView) groupDetailsHeaderLayout.findViewById(R.id.imgAddEditCoverPhoto);

		lnrPlayRecordComment = (LinearLayout) groupDetailsHeaderLayout.findViewById(R.id.lnrPlayRecordComment);
		lnrGroupActivitiesAnnouncementDiscussionList = (LinearLayout) groupDetailsHeaderLayout.findViewById(R.id.lnrGroupActivitiesAnnouncementDiscussionList);
		lnrGroupOptionsList = (LinearLayout) groupDetailsHeaderLayout.findViewById(R.id.lnrGroupOptionsList);
		lnrGroupCreateAnnouncement = (LinearLayout) groupDetailsHeaderLayout.findViewById(R.id.lnrGroupCreateAnnouncement);
		lnrGroupCreateDiscussion = (LinearLayout) groupDetailsHeaderLayout.findViewById(R.id.lnrGroupCreateDiscussion);
		lnrInvitation = (LinearLayout) groupDetailsHeaderLayout.findViewById(R.id.lnrInvitation);
		framGroupEditAvatar = (FrameLayout) groupDetailsHeaderLayout.findViewById(R.id.framGroupEditAvatar);
		txtGroupEdit = (IjoomerTextView) findViewById(R.id.txtGroupEdit);
		txtGroupSendEmail = (IjoomerTextView) findViewById(R.id.txtGroupSendEmail);
		txtGroupRemove = (IjoomerTextView) findViewById(R.id.txtGroupRemove);
		txtGroupTitle = (IjoomerTextView) groupDetailsHeaderLayout.findViewById(R.id.txtGroupTitle);
		txtGroupCreatedBy = (IjoomerTextView) groupDetailsHeaderLayout.findViewById(R.id.txtGroupCreatedBy);
		txtGroupCreatedOn = (IjoomerTextView) groupDetailsHeaderLayout.findViewById(R.id.txtGroupCreatedOn);
		txtGroupCategory = (IjoomerTextView) groupDetailsHeaderLayout.findViewById(R.id.txtGroupCategory);
		txtGroupUnpublish = (IjoomerTextView) findViewById(R.id.txtGroupUnpublish);
		txtGroupDetails = (IjoomerTextView) groupDetailsHeaderLayout.findViewById(R.id.txtGroupDetails);
		txtGroupLikeCount = (IjoomerTextView) groupDetailsHeaderLayout.findViewById(R.id.txtGroupLikeCount);
		txtGroupDislikeCount = (IjoomerTextView) groupDetailsHeaderLayout.findViewById(R.id.txtGroupDislikeCount);
		txtGroupFileCount = (IjoomerTextView) groupDetailsHeaderLayout.findViewById(R.id.txtGroupFileCount);
		txtGroupShare = (IjoomerTextView) groupDetailsHeaderLayout.findViewById(R.id.txtGroupShare);
		txtGroupReport = (IjoomerTextView) groupDetailsHeaderLayout.findViewById(R.id.txtGroupReport);
		txtGroupActivities = (IjoomerTextView) groupDetailsHeaderLayout.findViewById(R.id.txtGroupActivities);
		txtGroupAnnouncement = (IjoomerTextView) groupDetailsHeaderLayout.findViewById(R.id.txtGroupAnnouncement);
		txtGroupDiscussion = (IjoomerTextView) groupDetailsHeaderLayout.findViewById(R.id.txtGroupDiscussion);
		txtGroupMember = (IjoomerTextView) groupDetailsHeaderLayout.findViewById(R.id.txtGroupMember);
		txtGroupPhoto = (IjoomerTextView) groupDetailsHeaderLayout.findViewById(R.id.txtGroupPhoto);
		txtGroupVideo = (IjoomerTextView) groupDetailsHeaderLayout.findViewById(R.id.txtGroupVideo);
		txtGroupEvent = (IjoomerTextView) groupDetailsHeaderLayout.findViewById(R.id.txtGroupEvent);
		txtGroupNoAnnouncement = (IjoomerTextView) groupDetailsHeaderLayout.findViewById(R.id.txtGroupNoAnnouncement);
		txtGroupNoRecentActivities = (IjoomerTextView) groupDetailsHeaderLayout.findViewById(R.id.txtGroupNoRecentActivities);
		txtGroupNoDiscussion = (IjoomerTextView) groupDetailsHeaderLayout.findViewById(R.id.txtGroupNoDiscussion);
		txtPrivateGroupApprovalRequestList = (IjoomerTextView) groupDetailsHeaderLayout.findViewById(R.id.txtPrivateGroupApprovalRequestList);
		txtPrivateGroupAwiting = (IjoomerTextView) groupDetailsHeaderLayout.findViewById(R.id.txtPrivateGroupAwiting);
		txtPrivateGroup = (IjoomerTextView) groupDetailsHeaderLayout.findViewById(R.id.txtPrivateGroup);
		txtInvitationMessage = (IjoomerTextView) groupDetailsHeaderLayout.findViewById(R.id.txtInvitationMessage);
		txtInvitationAccept = (IjoomerTextView) groupDetailsHeaderLayout.findViewById(R.id.txtInvitationAccept);
		txtInvitationReject = (IjoomerTextView) groupDetailsHeaderLayout.findViewById(R.id.txtInvitationReject);
		txtTotalRecordComment = (IjoomerTextView) groupDetailsHeaderLayout.findViewById(R.id.txtTotalRecordComment);
		txtRecordUser = (IjoomerTextView) groupDetailsHeaderLayout.findViewById(R.id.txtRecordUser);
		voiceMessager = (IjoomerVoiceAndTextMessager) findViewById(R.id.voiceMessager);
		imgGroupAvatar = (ImageView) groupDetailsHeaderLayout.findViewById(R.id.imgGroupAvatar);
		imgGroupEditAvatar = (ImageView) groupDetailsHeaderLayout.findViewById(R.id.imgGroupEditAvatar);
		imgInvitationIcon = (ImageView) groupDetailsHeaderLayout.findViewById(R.id.imgInvitationIcon);
		btnGroupJoin = (IjoomerButton) groupDetailsHeaderLayout.findViewById(R.id.btnGroupJoin);
		btnGroupLeave = (IjoomerButton) groupDetailsHeaderLayout.findViewById(R.id.btnGroupLeave);
		btnGroupInviteFriend = (IjoomerButton) groupDetailsHeaderLayout.findViewById(R.id.btnGroupInviteFriend);
		btnGroupCreateAnnouncement = (IjoomerButton) groupDetailsHeaderLayout.findViewById(R.id.btnGroupCreateAnnouncement);
		btnGroupCreateDiscussion = (IjoomerButton) groupDetailsHeaderLayout.findViewById(R.id.btnGroupCreateDiscussion);
		btnPlayAll = (IjoomerVoiceButton) groupDetailsHeaderLayout.findViewById(R.id.btnPlayAll);
		btnPlayAll.setReportVoice(false);

		androidQuery = new AQuery(this);
		provider = new JomGroupDataProvider(this);
		wallDataProvider = new JomWallDataProvider(this);
		fileDataProvider = new JomGroupDataProvider(this);
		providerAnnoucement = new JomGroupDataProvider(this);
		providerDiscussion = new JomGroupDataProvider(this);

		getIntentData();
		setGroupDetails();
	}

	@Override
	public void prepareViews() {
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (isSetGroupCoverPage) {
			isSetGroupCoverPage = false;
			setGroupDetails();
		}
		isActivityResuming = true;
		if (IjoomerApplicationConfiguration.isReloadRequired()) {
			IjoomerApplicationConfiguration.setReloadRequired(false);
			announcementList = null;
			discussionList = null;
			activitiesList = null;
			if (CURRENT_LIST.equals(ANNOUNCEMENT_LIST)) {
				getAnnouncementList();
			} else if (CURRENT_LIST.equals(DISCUSSION_LIST)) {
				getDiscussionList();
			} else {
				try {
					if (option.getString(WALLLIST).equals("1")) {
						getGroupWall();
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == DOWNLOAD_FILE_LOCATION) {
				final String path = data.getStringExtra("IN_PATH");
				final String fileName = fileList.get(downlodIndex).get(NAME);
				androidQuery.download(fileList.get(downlodIndex).get(URL), new File(path + fileName), new AjaxCallback<File>() {
					@Override
					public void callback(String url, File object, AjaxStatus status) {
						super.callback(url, object, status);
						if (status.getCode() == 200) {
							fileList.get(downlodIndex).put(HITS, "" + (Integer.parseInt(fileList.get(downlodIndex).get(HITS)) + 1));
							fileListAdapter.notifyDataSetChanged();
							provider.downloadFile(fileList.get(downlodIndex).get(ID), new WebCallListener() {
								final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.download));

								@Override
								public void onProgressUpdate(int progressCount) {
									proSeekBar.setProgress(progressCount);
								}

								@Override
								public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
									if (responseCode == 200) {
										IjoomerUtilities.getCustomOkDialog(getString(R.string.download), getString(R.string.alert_message_file_downloaded), getString(R.string.ok),
												R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

											@Override
											public void NeutralMethod() {
												MediaScannerConnection.scanFile(JomGroupDetailsActivity_v30.this, new String[] { path + fileName }, null,
														new OnScanCompletedListener() {

													@Override
													public void onScanCompleted(String path, Uri uri) {

													}
												});
											}
										});
									} else {
										responseErrorMessageHandler(responseCode, false);
									}
								}
							});

						} else {
							IjoomerUtilities.getCustomOkDialog(getString(R.string.download), status.getMessage(), getString(R.string.ok), R.layout.ijoomer_ok_dialog,
									new CustomAlertNeutral() {

								@Override
								public void NeutralMethod() {

								}
							});
						}
					}
				});
			} else if (requestCode == PICK_IMAGE) {
				selectedImagePath = getAbsolutePath(data.getData());
				provider.editGroupAvatar(selectedImagePath, IN_GROUP_DETAILS.get(ID), new WebCallListener() {
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
							imgGroupAvatar.setImageBitmap(decodeFile(selectedImagePath));
							selectedImagePath = null;
						} else {
							responseErrorMessageHandler(responseCode, false);
						}
					}
				});
			} else if (requestCode == CAPTURE_IMAGE) {
				selectedImagePath = getImagePath();
				provider.editGroupAvatar(selectedImagePath, IN_GROUP_DETAILS.get(ID), new WebCallListener() {
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
							imgGroupAvatar.setImageBitmap(decodeFile(selectedImagePath));
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
				isSetGroupCoverPage = true;
				try {
					loadNew(JomAlbumsActivity.class, JomGroupDetailsActivity_v30.this, false, "IN_USERID", IN_USERID, "IN_PROFILE_COVER", "2|"+IN_GROUP_DETAILS.get(ID)+"");
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
					for (int i = 0; i < listData.size(); i++) {
						if (getAudio(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(TITLETAG)) != null) {
							btnPlayAll.setText(getAudioLength(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(TITLETAG)));
							txtRecordUser.setText(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(USER_NAME));
							recordCommentLast = i;
							btnPlayAll.setAudioPath(getAudio(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(TITLETAG)), false);
							break;
						}
						if (getAudio(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(CONTENT)) != null) {
							btnPlayAll.setText(getAudioLength(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(CONTENT)));
							txtRecordUser.setText(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(USER_NAME));
							recordCommentLast = i;
							btnPlayAll.setAudioPath(getAudio(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(CONTENT)), false);
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
						if (getAudio(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(TITLETAG)) != null) {
							btnPlayAll.setText(getAudioLength(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(TITLETAG)));
							txtRecordUser.setText(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(USER_NAME));
							recordCommentLast = i;
							btnPlayAll.setAudioPath(getAudio(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(TITLETAG)), true);
							break;
						}
						if (getAudio(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(CONTENT)) != null) {
							btnPlayAll.setText(getAudioLength(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(CONTENT)));
							txtRecordUser.setText(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(USER_NAME));
							recordCommentLast = i;
							btnPlayAll.setAudioPath(getAudio(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(CONTENT)), true);
							break;
						}
					}
				}
			}
		});

		txtInvitationAccept.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				provider.groupInvitation(IN_GROUP_DETAILS.get(ID), "1", new WebCallListener() {
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
		txtInvitationReject.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				provider.groupInvitation(IN_GROUP_DETAILS.get(ID), "0", new WebCallListener() {
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
		txtPrivateGroupApprovalRequestList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showWaitingApprovalMemberDialog();
			}
		});
		lstGroupDetails.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

				if (!isActivityResuming) {
					if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 2) {
						isActivityResuming = true;
						if (CURRENT_LIST.equals(ACTIVITES_LIST)) {
							if (!wallDataProvider.isCalling() && wallDataProvider.hasNextPage()) {
								listFooterVisible();

								wallDataProvider.getWallList(IN_GROUP_DETAILS.get(ID), "group", new WebCallListenerWithCacheInfo() {

									@Override
									public void onProgressUpdate(int progressCount) {
									}

									@Override
									public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2, int pageNo,
											int pageLimit, boolean fromCache) {
										listFooterInvisible();
										if (responseCode == 200) {
											if (!fromCache) {
												updateHeader(wallDataProvider.getNotificationData());
											}
											prepareList(data1, true, fromCache, pageNo, pageLimit);
											isActivityResuming = false;
										} else {
											responseErrorMessageHandler(responseCode, false);
										}
									}
								});
							}
						} else if (CURRENT_LIST.equals(DISCUSSION_LIST)) {
							if (!providerDiscussion.isCalling() && providerDiscussion.hasNextPage()) {
								listFooterVisible();
								providerDiscussion.getDiscussionList(IN_GROUP_DETAILS.get(ID), new WebCallListener() {

									@Override
									public void onProgressUpdate(int progressCount) {
									}

									@Override
									public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
										listFooterInvisible();
										if (responseCode == 200) {
											updateHeader(providerDiscussion.getNotificationData());
											prepareListDiscussion(data1, true);
											isActivityResuming = false;
										} else {
											responseErrorMessageHandler(responseCode, false);
										}
									}
								});
							}
						} else if (CURRENT_LIST.equals(ANNOUNCEMENT_LIST)) {
							if (!providerAnnoucement.isCalling() && providerAnnoucement.hasNextPage()) {
								listFooterVisible();
								providerAnnoucement.getAnnouncementList(IN_GROUP_DETAILS.get(ID), new WebCallListener() {

									@Override
									public void onProgressUpdate(int progressCount) {
									}

									@Override
									public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
										listFooterInvisible();
										if (responseCode == 200) {
											updateHeader(providerAnnoucement.getNotificationData());
											prepareListAnnouncement(data1, true);
											isActivityResuming = false;
										} else {
											responseErrorMessageHandler(responseCode, false);
										}
									}
								});
							}
						}

					}
				}
			}
		});

		txtGroupFileCount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!GROUP_DETAILS.get(FILES).equals("0")) {
					showFileDialog();
				}
			}
		});
		txtGroupEvent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					loadNew(JomEventActivity.class, JomGroupDetailsActivity_v30.this, false, "IN_USERID", IN_USERID, "IN_GROUP_ID", IN_GROUP_DETAILS.get(ID),
							"IN_GROUP_CREATE_EVENT", createEvent);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});

		txtGroupPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					loadNew(JomAlbumsActivity.class, JomGroupDetailsActivity_v30.this, false, "IN_USERID", IN_USERID, "IN_GROUP_ID", IN_GROUP_DETAILS.get(ID),
							"IN_GROUP_ADD_ALBUM", addAlbum, "IN_GROUP_UPLOAD_PHOTO", uploadPhoto);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});

		txtGroupVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					loadNew(JomVideoActivity.class, JomGroupDetailsActivity_v30.this, false, "IN_USERID", IN_USERID, "IN_PROFILE", "1", "IN_GROUP_ID", IN_GROUP_DETAILS.get(ID), "IN_GROUP_ADD_VIDEO",
							addVideo);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});

		txtGroupMember.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showMemberDialog();
			}
		});

		btnGroupInviteFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					loadNew(JomInviteFriendActivity.class, JomGroupDetailsActivity_v30.this, false, "IN_GROUP_ID", IN_GROUP_DETAILS.get(ID));
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});

		btnGroupCreateAnnouncement.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialogAnnouncementOrDiscussion = getAnnouncementOrDiscussionCreateDialog(getString(R.string.group_announcement), "", "", "",
						new AnnouncementAndDiscussionListner() {
					@Override
					public void onClick(String title, String message, String allowMemberToUploadFile) {
						provider.addOrEditGroupAnnouncement(IN_GROUP_DETAILS.get(ID), "0", title, message, allowMemberToUploadFile, new WebCallListener() {
							final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

							@Override
							public void onProgressUpdate(int progressCount) {
								proSeekBar.setProgress(progressCount);
							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								if (responseCode == 200) {
									updateHeader(provider.getNotificationData());
									IjoomerUtilities.getCustomOkDialog(getString(R.string.group), getString(R.string.group_announcement_added_successfully),
											getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

										@Override
										public void NeutralMethod() {
											dialogAnnouncementOrDiscussion.dismiss();
										}
									});
									getAnnouncementList();
								} else {
									responseErrorMessageHandler(responseCode, false);
								}
							}
						});

					}
				});

			}
		});

		btnGroupCreateDiscussion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				dialogAnnouncementOrDiscussion = getAnnouncementOrDiscussionCreateDialog(getString(R.string.group_discussion), "", "", "", new AnnouncementAndDiscussionListner() {
					@Override
					public void onClick(String title, String message, String allowMemberToUploadFile) {
						provider.addOrEditGroupDiscussion(IN_GROUP_DETAILS.get(ID), "0", title, message, allowMemberToUploadFile, new WebCallListener() {
							final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

							@Override
							public void onProgressUpdate(int progressCount) {
								proSeekBar.setProgress(progressCount);
							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								if (responseCode == 200) {
									updateHeader(provider.getNotificationData());
									IjoomerUtilities.getCustomOkDialog(getString(R.string.group), getString(R.string.group_discussion_added_successfully), getString(R.string.ok),
											R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

										@Override
										public void NeutralMethod() {
											dialogAnnouncementOrDiscussion.dismiss();

										}
									});
									getDiscussionList();
								} else {
									responseErrorMessageHandler(responseCode, false);
								}
							}
						});

					}
				});

			}
		});

		txtGroupActivities.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!(CURRENT_LIST.equals(ACTIVITES_LIST) || providerAnnoucement.isCalling() || providerDiscussion.isCalling())) {
					lstGroupDetails.setAdapter(null);
					try {
						if (GROUP_DETAILS.get(WALLPERMISSION).equals("1")) {
							lnrGroupWriteComment.setVisibility(View.VISIBLE);
						}
					} catch (Exception e) {
					}

					if (recordCommentTotal > 0) {
						lnrPlayRecordComment.setVisibility(View.VISIBLE);
						btnPlayAll.setCustomText(getString(R.string.play_all));
						txtTotalRecordComment.setText("(" + recordCommentTotal + ")");
					}
					lnrGroupCreateAnnouncement.setVisibility(View.GONE);
					lnrGroupCreateDiscussion.setVisibility(View.GONE);
					txtGroupNoAnnouncement.setVisibility(View.GONE);
					txtGroupNoDiscussion.setVisibility(View.GONE);

					txtGroupActivities.setTextColor(getResources().getColor(R.color.jom_blue));
					txtGroupAnnouncement.setTextColor(getResources().getColor(R.color.jom_txt_color));
					txtGroupDiscussion.setTextColor(getResources().getColor(R.color.jom_txt_color));
					CURRENT_LIST = ACTIVITES_LIST;
					isActivityResuming = true;
					if (activitiesList != null) {
						txtGroupAnnouncement.setClickable(false);
						txtGroupDiscussion.setClickable(false);
						prepareList(activitiesList, false, false, 0, 0);
						wallListAdapterWithHolder = getListAdapter();
						lstGroupDetails.setAdapter(wallListAdapterWithHolder);
						lstGroupDetails.setSelection(0);
						txtGroupAnnouncement.setClickable(true);
						txtGroupDiscussion.setClickable(true);
						isActivityResuming = false;
					} else {
						getGroupWall();
					}
				}
			}
		});

		txtGroupAnnouncement.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!(CURRENT_LIST.equals(ANNOUNCEMENT_LIST) || wallDataProvider.isCalling() || providerDiscussion.isCalling())) {
					lstGroupDetails.setAdapter(null);
					if (isCreateAnnouncement) {
						lnrGroupCreateAnnouncement.setVisibility(View.VISIBLE);
					}
					lnrGroupCreateDiscussion.setVisibility(View.GONE);
					lnrGroupWriteComment.setVisibility(View.GONE);
					txtGroupNoRecentActivities.setVisibility(View.GONE);
					txtGroupNoDiscussion.setVisibility(View.GONE);
					lnrPlayRecordComment.setVisibility(View.GONE);

					txtGroupAnnouncement.setTextColor(getResources().getColor(R.color.jom_blue));
					txtGroupActivities.setTextColor(getResources().getColor(R.color.jom_txt_color));
					txtGroupDiscussion.setTextColor(getResources().getColor(R.color.jom_txt_color));

					CURRENT_LIST = ANNOUNCEMENT_LIST;
					isActivityResuming = true;
					if (announcementList != null) {
						txtGroupActivities.setClickable(false);
						txtGroupDiscussion.setClickable(false);
						prepareListAnnouncement(announcementList, false);
						announcementListAdapterWithHolder = getAnnouncementListAdapter();
						lstGroupDetails.setAdapter(announcementListAdapterWithHolder);
						lstGroupDetails.setSelection(1);
						txtGroupActivities.setClickable(true);
						txtGroupDiscussion.setClickable(true);
						isActivityResuming = false;
					} else {
						getAnnouncementList();
					}
				}
			}
		});

		txtGroupDiscussion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!(CURRENT_LIST.equals(DISCUSSION_LIST) || wallDataProvider.isCalling() || providerAnnoucement.isCalling())) {
					lstGroupDetails.setAdapter(null);
					if (isCreateDiscussion) {
						lnrGroupCreateDiscussion.setVisibility(View.VISIBLE);
					}
					txtGroupNoRecentActivities.setVisibility(View.GONE);
					txtGroupNoAnnouncement.setVisibility(View.GONE);
					lnrGroupCreateAnnouncement.setVisibility(View.GONE);
					lnrGroupWriteComment.setVisibility(View.GONE);
					lnrPlayRecordComment.setVisibility(View.GONE);
					txtGroupActivities.setTextColor(getResources().getColor(R.color.jom_txt_color));
					txtGroupAnnouncement.setTextColor(getResources().getColor(R.color.jom_txt_color));
					txtGroupDiscussion.setTextColor(getResources().getColor(R.color.jom_blue));
					CURRENT_LIST = DISCUSSION_LIST;
					isActivityResuming = true;
					if (discussionList != null) {
						txtGroupActivities.setClickable(false);
						txtGroupAnnouncement.setClickable(false);
						prepareListDiscussion(discussionList, false);
						lstGroupDetails.setAdapter(getDiscussionListAdapter());
						lstGroupDetails.setSelection(0);
						txtGroupActivities.setClickable(true);
						txtGroupAnnouncement.setClickable(true);
						isActivityResuming = false;
					} else {
						getDiscussionList();
					}
				}
			}
		});

		btnGroupJoin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				provider.joinGroup(IN_GROUP_DETAILS.get(ID), new WebCallListener() {

					@Override
					public void onProgressUpdate(int progressCount) {

					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						if (responseCode == 200 || responseCode == 708) {
							updateHeader(provider.getNotificationData());
							IjoomerApplicationConfiguration.setReloadRequired(true);
							if (GROUP_DETAILS.get(ISPRIVATE).equals("1")) {
								if (responseCode == 708) {
									responseErrorMessageHandler(responseCode, false);
								}
								GROUP_DETAILS.put(ISWAITINGAPPROVAL, "1");
								txtPrivateGroupAwiting.setVisibility(View.VISIBLE);
								btnGroupJoin.setVisibility(View.GONE);
							} else {
								IjoomerUtilities.getCustomOkDialog(getString(R.string.group), getString(R.string.group_joined_successfully), getString(R.string.ok),
										R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

									@Override
									public void NeutralMethod() {
										finish();
									}
								});
							}

						} else {
							responseErrorMessageHandler(responseCode, false);
						}
					}
				});
			}
		});

		btnGroupLeave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				provider.leaveGroup(IN_GROUP_DETAILS.get(ID), new WebCallListener() {
					@Override
					public void onProgressUpdate(int progressCount) {

					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						if (responseCode == 200) {
							updateHeader(provider.getNotificationData());
							IjoomerApplicationConfiguration.setReloadRequired(true);
							IjoomerUtilities.getCustomOkDialog(getString(R.string.group), getString(R.string.group_leaveded_successfully), getString(R.string.ok),
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

		voiceMessager.setMessageHandler(new MessageHandler() {

			@Override
			public void onVoiceMessageRecordingComplete(String message, String voiceMessagePath) {
				provider.addWallPost(IN_GROUP_DETAILS.get(ID), message, voiceMessagePath, new WebCallListener() {
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
							getGroupWall();
						} else {
							responseErrorMessageHandler(responseCode, false);
						}
					}
				});
			}

			@Override
			public void onButtonSend(String message) {
				provider.addWallPost(IN_GROUP_DETAILS.get(ID), message, null, new WebCallListener() {
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
							getGroupWall();
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
		txtGroupUnpublish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				provider.unpublishGroup(IN_GROUP_DETAILS.get(ID), new WebCallListener() {
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
							IjoomerUtilities.getCustomOkDialog(getString(R.string.group), getString(R.string.group_unpublish_successfully), getString(R.string.ok),
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

		txtGroupReport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				IjoomerUtilities.getReportDialog(new ReportListner() {
					@Override
					public void onClick(String repotType, String message) {
						final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
						provider.reportGroupOrDiscussion(IN_GROUP_DETAILS.get(ID), "0", repotType + "  " + message, new WebCallListener() {

							@Override
							public void onProgressUpdate(int progressCount) {
								proSeekBar.setProgress(progressCount);
							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								if (responseCode == 200) {
									updateHeader(provider.getNotificationData());
									IjoomerUtilities.getCustomOkDialog(getString(R.string.group), getString(R.string.report_successfully), getString(R.string.ok),
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

		txtGroupSendEmail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getSendMailDialog(new ReportListner() {

					@Override
					public void onClick(String title, String message) {
						final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
						provider.sendMailToAllMember(IN_GROUP_DETAILS.get(ID), title, message, new WebCallListener() {

							@Override
							public void onProgressUpdate(int progressCount) {
								proSeekBar.setProgress(progressCount);
							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								if (responseCode == 200) {
									updateHeader(provider.getNotificationData());
									IjoomerUtilities.getCustomOkDialog(getString(R.string.group), getString(R.string.event_send_mail_successfully), getString(R.string.ok),
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

		imgGroupEditAvatar.setOnClickListener(new OnClickListener() {

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

		txtGroupEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				provider.addOrEditGroupFieldList(IN_GROUP_DETAILS.get(ID), new WebCallListener() {
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
								loadNew(JomGroupCreateActivity.class, JomGroupDetailsActivity_v30.this, true, "IN_FIELD_LIST", data1, "IN_GROUP_ID", IN_GROUP_DETAILS.get(ID));
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

		txtGroupRemove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				IjoomerUtilities.getCustomConfirmDialog(getString(R.string.group_title_remove), getString(R.string.are_you_sure), getString(R.string.yes), getString(R.string.no),
						new CustomAlertMagnatic() {

					@Override
					public void PositiveMethod() {
						provider.removeGroup(IN_GROUP_DETAILS.get(ID), new WebCallListener() {
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

		txtGroupShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					loadNew(IjoomerShareActivity.class, JomGroupDetailsActivity_v30.this, false, "IN_SHARE_CAPTION", IN_GROUP_DETAILS.get(TITLE), "IN_SHARE_DESCRIPTION",
							IN_GROUP_DETAILS.get(DESCRIPTION), "IN_SHARE_THUMB", GROUP_DETAILS.get(AVATAR), "IN_SHARE_SHARELINK", GROUP_DETAILS.get(SHARELINK));
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});

		txtGroupLikeCount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (GROUP_DETAILS.get(LIKED).equals("1")) {
					txtGroupLikeCount.setClickable(false);
					provider.unlikeGroup(IN_GROUP_DETAILS.get(ID), new WebCallListener() {

						@Override
						public void onProgressUpdate(int progressCount) {

						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								updateHeader(provider.getNotificationData());
								GROUP_DETAILS.put(LIKED, "0");
								GROUP_DETAILS.put(LIKES, String.valueOf(Integer.parseInt(GROUP_DETAILS.get(LIKES)) - 1));
								txtGroupLikeCount.setText(GROUP_DETAILS.get(LIKES));
							} else {
								responseErrorMessageHandler(responseCode, false);
							}
							txtGroupLikeCount.setClickable(true);
						}
					});
				} else {
					txtGroupLikeCount.setClickable(false);
					provider.likeGroup(IN_GROUP_DETAILS.get(ID), new WebCallListener() {

						@Override
						public void onProgressUpdate(int progressCount) {

						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								updateHeader(provider.getNotificationData());
								GROUP_DETAILS.put(LIKED, "1");
								GROUP_DETAILS.put(LIKES, String.valueOf(Integer.parseInt(GROUP_DETAILS.get(LIKES)) + 1));
								txtGroupLikeCount.setText(GROUP_DETAILS.get(LIKES));
								if (GROUP_DETAILS.get(DISLIKED).equals("1")) {
									GROUP_DETAILS.put(DISLIKES, String.valueOf(Integer.parseInt(GROUP_DETAILS.get(DISLIKES)) - 1));
									GROUP_DETAILS.put(DISLIKED, "0");
									txtGroupDislikeCount.setText(GROUP_DETAILS.get(DISLIKES));
								}
							} else {
								responseErrorMessageHandler(responseCode, false);
							}
							txtGroupLikeCount.setClickable(true);
						}
					});
				}
			}
		});

		txtGroupDislikeCount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (GROUP_DETAILS.get(DISLIKED).equals("1")) {
					txtGroupDislikeCount.setClickable(false);
					provider.unlikeGroup(IN_GROUP_DETAILS.get(ID), new WebCallListener() {

						@Override
						public void onProgressUpdate(int progressCount) {

						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								updateHeader(provider.getNotificationData());
								GROUP_DETAILS.put(DISLIKED, "0");
								GROUP_DETAILS.put(DISLIKES, String.valueOf(Integer.parseInt(GROUP_DETAILS.get(DISLIKES)) - 1));
								txtGroupDislikeCount.setText(GROUP_DETAILS.get(DISLIKES));
							} else {
								responseErrorMessageHandler(responseCode, false);
							}
							txtGroupDislikeCount.setClickable(true);
						}
					});
				} else {
					txtGroupDislikeCount.setClickable(false);
					provider.dislikeGroup(IN_GROUP_DETAILS.get(ID), new WebCallListener() {

						@Override
						public void onProgressUpdate(int progressCount) {

						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								updateHeader(provider.getNotificationData());
								GROUP_DETAILS.put(DISLIKED, "1");
								GROUP_DETAILS.put(DISLIKES, String.valueOf(Integer.parseInt(GROUP_DETAILS.get(DISLIKES)) + 1));
								txtGroupDislikeCount.setText(GROUP_DETAILS.get(DISLIKES));
								if (GROUP_DETAILS.get(LIKED).equals("1")) {
									GROUP_DETAILS.put(LIKES, String.valueOf(Integer.parseInt(GROUP_DETAILS.get(LIKES)) - 1));
									GROUP_DETAILS.put(LIKED, "0");
									txtGroupLikeCount.setText(GROUP_DETAILS.get(LIKES));
								}
							} else {
								responseErrorMessageHandler(responseCode, false);
							}
							txtGroupDislikeCount.setClickable(true);
						}
					});
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
		IN_GROUP_DETAILS = (HashMap<String, String>) getIntent().getSerializableExtra("IN_GROUP_DETAILS") == null ? new HashMap<String, String>()
				: (HashMap<String, String>) getIntent().getSerializableExtra("IN_GROUP_DETAILS");
		IN_USERID = getIntent().getStringExtra("IN_USERID") == null ? "0" : getIntent().getStringExtra("IN_USERID");
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
		IjoomerUtilities.getCustomOkDialog(getString(R.string.group), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())),
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
	 * This method used to set group details.
	 */
	private void setGroupDetails() {

		provider.getGroupDetails(IN_GROUP_DETAILS.get(ID), new WebCallListener() {
			final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

			@Override
			public void onProgressUpdate(int progressCount) {
				proSeekBar.setProgress(progressCount);
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				if (responseCode == 200) {
					updateHeader(provider.getNotificationData());
					GROUP_DETAILS = data1.get(0);
					if (GROUP_DETAILS.containsKey(ADMINMENU)) {
						try {
							adminMenu = new JSONObject(GROUP_DETAILS.get(ADMINMENU));
							if (adminMenu.getString(EDIT).equals("1")) {
								txtGroupEdit.setVisibility(View.VISIBLE);
							}
							if (adminMenu.getString(DELETEGROUP).equals("1")) {
								txtGroupRemove.setVisibility(View.VISIBLE);
								imgAddEditCoverPhoto.setVisibility(View.VISIBLE);
							}
							if (adminMenu.getString(SENDMAIL).equals("1")) {
								txtGroupSendEmail.setVisibility(View.VISIBLE);
							}
							if (adminMenu.getString(UNPUBLISHGROUP).equals("1")) {
								txtGroupUnpublish.setVisibility(View.VISIBLE);
							}

							if (adminMenu.getString(EDITAVATAR).equals("1")) {
								framGroupEditAvatar.setVisibility(View.VISIBLE);
							}
							if (adminMenu.getString(CREATEANNOUNCEMENT).equals("1")) {
								isCreateAnnouncement = true;
							}
						} catch (Exception e) {
						}

					} else {
						lnrGroupHeader.setVisibility(View.GONE);
					}

					if (GROUP_DETAILS.containsKey(OPTION)) {
						try {
							option = new JSONObject(GROUP_DETAILS.get(OPTION));

							if (option.getString(MEMBERLIST).equals("1")) {
								txtGroupMember.setVisibility(View.VISIBLE);
							}
							if (option.getString(ALBUMLIST).equals("1")) {
								txtGroupPhoto.setVisibility(View.VISIBLE);
							}
							if (option.getString(VIDEOLIST).equals("1")) {
								txtGroupVideo.setVisibility(View.VISIBLE);
							}
							if (option.getString(EVENTLIST).equals("1")) {
								txtGroupEvent.setVisibility(View.VISIBLE);
							}
							if (option.getString(ANNOUNCEMENTLIST).equals("1")) {
								txtGroupAnnouncement.setVisibility(View.VISIBLE);
							}
							if (option.getString(DISCUSSIONLIST).equals("1")) {
								txtGroupDiscussion.setVisibility(View.VISIBLE);
							}
							if (option.getString(WALLLIST).equals("1")) {
								txtGroupActivities.setVisibility(View.VISIBLE);
								getGroupWall();
							}
						} catch (Exception e) {
						}

					} else {
						lnrGroupOptionsList.setVisibility(View.GONE);
					}

					try {
						menu = new JSONObject(GROUP_DETAILS.get(MENU));

						if (GROUP_DETAILS.get(ISWAITINGAPPROVAL).equals("1")) {
							txtPrivateGroupAwiting.setVisibility(View.VISIBLE);
							btnGroupJoin.setVisibility(View.GONE);
							btnGroupLeave.setVisibility(View.GONE);
						} else if (menu.getString(JOINGROUP).equals("1")) {
							btnGroupJoin.setVisibility(View.VISIBLE);
							btnGroupLeave.setVisibility(View.GONE);
						} else if (menu.getString(LEAVEGROUP).equals("1")) {
							btnGroupJoin.setVisibility(View.GONE);
							btnGroupLeave.setVisibility(View.VISIBLE);
						} else {
							btnGroupJoin.setVisibility(View.GONE);
							btnGroupLeave.setVisibility(View.GONE);
						}
						if (menu.getString(SHAREGROUP).equals("1")) {
							txtGroupShare.setVisibility(View.VISIBLE);
						}
						if (menu.getString(REPORTGROUP).equals("1")) {
							txtGroupReport.setVisibility(View.VISIBLE);
						}
						if (menu.getString(INVITEFRIEND).equals("1")) {
							btnGroupInviteFriend.setVisibility(View.VISIBLE);
						}

						if (menu.getString(CREATEDISCUSSION).equals("1")) {
							isCreateDiscussion = true;
						}
						if (menu.getString(CREATEEVENT).equals("1")) {
							createEvent = "1";
						}
						if (menu.getString(UPLOADPHOTO).equals("1")) {
							uploadPhoto = "1";
						}
						if (menu.getString(CREATEALBUM).equals("1")) {
							addAlbum = "1";
						}
						if (menu.getString(ADDVIDEO).equals("1")) {
							addVideo = "1";
						}

					} catch (Exception e) {
					}

					if (GROUP_DETAILS.get(ISPRIVATE).equals("1")) {
						txtPrivateGroup.setVisibility(View.VISIBLE);
					}
					if (GROUP_DETAILS.get(ISINVITATION).equals("1")) {
						lnrInvitation.setVisibility(View.VISIBLE);
						androidQuery.id(imgInvitationIcon).image(GROUP_DETAILS.get(INVITATIONICON), true, true, getDeviceWidth(), 0);
						txtInvitationMessage.setText(addClickablePart(Html.fromHtml(GROUP_DETAILS.get(INVITATIONMESSAGE)), GROUP_DETAILS), BufferType.SPANNABLE);
						txtInvitationMessage.setMovementMethod(LinkMovementMethod.getInstance());
					}
					if (GROUP_DETAILS.get(LIKEALLOWED).equals("1")) {
						txtGroupLikeCount.setVisibility(View.VISIBLE);
						txtGroupDislikeCount.setVisibility(View.VISIBLE);
					}
					if (GROUP_DETAILS.containsKey(MEMBERWAITING) && Integer.parseInt(GROUP_DETAILS.get(MEMBERWAITING)) > 0) {
						txtPrivateGroupApprovalRequestList.setVisibility(View.VISIBLE);
						txtPrivateGroupApprovalRequestList.setText(String.format(getString(R.string.private_approval_request), Integer.parseInt(GROUP_DETAILS.get(MEMBERWAITING))));
					}
					if (GROUP_DETAILS.get(WALLPERMISSION).equals("1")) {
						lnrGroupWriteComment.setVisibility(View.VISIBLE);
					}
					if (txtGroupEdit.getVisibility() == View.GONE && txtGroupRemove.getVisibility() == View.GONE && txtGroupUnpublish.getVisibility() == View.GONE
							&& txtGroupSendEmail.getVisibility() == View.GONE) {
						lnrGroupHeader.setVisibility(View.GONE);
					}

					if (txtGroupMember.getVisibility() == View.GONE && txtGroupPhoto.getVisibility() == View.GONE && txtGroupVideo.getVisibility() == View.GONE
							&& txtGroupEvent.getVisibility() == View.GONE) {
						lnrGroupOptionsList.setVisibility(View.GONE);
					}

					if (txtGroupActivities.getVisibility() == View.GONE && txtGroupAnnouncement.getVisibility() == View.GONE && txtGroupDiscussion.getVisibility() == View.GONE) {
						lnrGroupActivitiesAnnouncementDiscussionList.setVisibility(View.GONE);
					}

					androidQuery.id(imgCoverPhoto).image(GROUP_DETAILS.get(COVER));
					androidQuery.id(imgGroupAvatar).image(GROUP_DETAILS.get(AVATAR), true, true, getDeviceWidth(), 0);
					txtGroupTitle.setText(IN_GROUP_DETAILS.get(TITLE));
					txtGroupCreatedBy.setText(String.format(getString(R.string.by), GROUP_DETAILS.get(USER_NAME)));
					txtGroupCreatedOn.setText(GROUP_DETAILS.get(DATE));
					txtGroupCategory.setText(GROUP_DETAILS.get(CATEGORY_NAME));
					IN_GROUP_DETAILS.put(DESCRIPTION, IN_GROUP_DETAILS.get(DESCRIPTION).replace("\n", " "));
					IN_GROUP_DETAILS.put(DESCRIPTION, IN_GROUP_DETAILS.get(DESCRIPTION).replace("\t", " "));
					IN_GROUP_DETAILS.put(DESCRIPTION, IN_GROUP_DETAILS.get(DESCRIPTION).replace("\r", " "));
					txtGroupDetails.setText(Html.fromHtml(IN_GROUP_DETAILS.get(DESCRIPTION)));
					IjoomerUtilities.IjoomerTextViewResizable(txtGroupDetails, 3, getString(R.string.see_more));
					txtGroupLikeCount.setText(GROUP_DETAILS.get(LIKES));
					txtGroupDislikeCount.setText(GROUP_DETAILS.get(DISLIKES));
					if (Integer.parseInt(GROUP_DETAILS.get(FILES)) > 0) {
						txtGroupFileCount.setText(Integer.parseInt(GROUP_DETAILS.get(FILES)) > 1 ? GROUP_DETAILS.get(FILES) + " " + getString(R.string.files) : GROUP_DETAILS
								.get(FILES) + " " + getString(R.string.file));
						txtGroupFileCount.setVisibility(View.VISIBLE);
					}
				} else {
					responseErrorMessageHandler(responseCode, true);
				}
			}
		});

	}

	/**
	 * This method used to get group wall.
	 */
	private void getGroupWall() {
		txtGroupAnnouncement.setClickable(false);
		txtGroupDiscussion.setClickable(false);
		wallDataProvider.restorePagingSettings();
		listFooterVisible();
		wallDataProvider.getWallList(IN_GROUP_DETAILS.get(ID), "group", new WebCallListenerWithCacheInfo() {

			@Override
			public void onProgressUpdate(int progressCount) {
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2, int pageNo, int pageLimit, boolean fromCache) {
				txtGroupNoAnnouncement.setVisibility(View.GONE);
				txtGroupNoDiscussion.setVisibility(View.GONE);
				txtGroupNoRecentActivities.setVisibility(View.GONE);

				listFooterInvisible();
				if (responseCode == 200) {
					if (!fromCache) {
						updateHeader(wallDataProvider.getNotificationData());
					}
					activitiesList = data1;
					prepareList(activitiesList, false, fromCache, pageNo, pageLimit);
					wallListAdapterWithHolder = getListAdapter();
					lstGroupDetails.setAdapter(wallListAdapterWithHolder);
					isActivityResuming = false;
				} else {
					lstGroupDetails.setAdapter(null);
					if (responseCode != 204) {
						responseErrorMessageHandler(responseCode, false);
					} else {
						txtGroupNoRecentActivities.setVisibility(View.VISIBLE);
					}
				}
				txtGroupAnnouncement.setClickable(true);
				txtGroupDiscussion.setClickable(true);
			}
		});
	}

	/**
	 * This method used to get announcement list.
	 */
	private void getAnnouncementList() {
		txtGroupActivities.setClickable(false);
		txtGroupDiscussion.setClickable(false);
		providerAnnoucement.restorePagingSettings();
		listFooterVisible();
		providerAnnoucement.getAnnouncementList(IN_GROUP_DETAILS.get(ID), new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {

			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				listFooterInvisible();
				txtGroupNoRecentActivities.setVisibility(View.GONE);
				txtGroupNoDiscussion.setVisibility(View.GONE);
				if (responseCode == 200) {
					txtGroupNoAnnouncement.setVisibility(View.GONE);
					updateHeader(providerAnnoucement.getNotificationData());
					announcementList = data1;
					prepareListAnnouncement(announcementList, false);
					announcementListAdapterWithHolder = getAnnouncementListAdapter();
					lstGroupDetails.setAdapter(announcementListAdapterWithHolder);
					lstGroupDetails.setSelection(0);
					isActivityResuming = false;
				} else {
					lstGroupDetails.setAdapter(null);
					if (responseCode != 204) {
						responseErrorMessageHandler(responseCode, false);
					} else {
						txtGroupNoAnnouncement.setVisibility(View.VISIBLE);
					}
				}
				txtGroupActivities.setClickable(true);
				txtGroupDiscussion.setClickable(true);
			}
		});
	}

	/**
	 * This method used to get discussion list.
	 */
	private void getDiscussionList() {
		txtGroupActivities.setClickable(false);
		txtGroupAnnouncement.setClickable(false);
		providerDiscussion.restorePagingSettings();
		listFooterVisible();
		providerDiscussion.getDiscussionList(IN_GROUP_DETAILS.get(ID), new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {

			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				listFooterInvisible();
				txtGroupNoRecentActivities.setVisibility(View.GONE);
				txtGroupNoAnnouncement.setVisibility(View.GONE);
				if (responseCode == 200) {
					txtGroupNoDiscussion.setVisibility(View.GONE);
					updateHeader(providerDiscussion.getNotificationData());
					discussionList = data1;
					prepareListDiscussion(discussionList, false);
					lstGroupDetails.setAdapter(getDiscussionListAdapter());
					lstGroupDetails.setSelection(0);
					isActivityResuming = false;
				} else {
					lstGroupDetails.setAdapter(null);
					if (responseCode != 204) {
						responseErrorMessageHandler(responseCode, false);
					} else {
						txtGroupNoDiscussion.setVisibility(View.VISIBLE);
					}
				}
				txtGroupActivities.setClickable(true);
				txtGroupAnnouncement.setClickable(true);
			}
		});
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
	 * This method used to show waiting approval member dialog.
	 */
	@SuppressWarnings("deprecation")
	private void showWaitingApprovalMemberDialog() {
		groupWaitingApprovalMemberList = null;
		providerGroupWaitingApprovalMember = new JomGroupDataProvider(this);
		isGuestListResuming = false;

		try {
			int popupWidth = getDeviceWidth() - convertSizeToDeviceDependent(50);
			int popupHeight = getDeviceHeight() - convertSizeToDeviceDependent(200);

			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = layoutInflater.inflate(R.layout.jom_group_member_popup, null);

			dialog = new PopupWindow(this);
			dialog.setContentView(layout);
			dialog.setWidth(popupWidth);
			dialog.setHeight(popupHeight);
			dialog.setFocusable(true);
			dialog.setBackgroundDrawable(new BitmapDrawable(getResources()));
			dialog.showAtLocation(layout, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);

			imgTagClose = (ImageView) layout.findViewById(R.id.imgTagClose);
			lstGroupMember = (ListView) layout.findViewById(R.id.lstGroupMember);
			pbrGroupMember = (ProgressBar) layout.findViewById(R.id.pbrGroupMember);
			txtGroupAllMember = (IjoomerTextView) layout.findViewById(R.id.txtGroupAllMember);
			txtGroupBanMember = (IjoomerTextView) layout.findViewById(R.id.txtGroupBanMember);
			txtGroupBanMember.setVisibility(View.GONE);
			lstGroupMember.addFooterView(listFooter, null, false);
			lstGroupMember.setAdapter(null);
			listFooterInvisible();

			groupWaitingApprovalMemberList();

			imgTagClose.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			lstGroupMember.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {

				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
					if (isGuestListResuming) {
						if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 1) {
							if (!providerGroupWaitingApprovalMember.isCalling() && providerGroupWaitingApprovalMember.hasNextPage()) {
								listFooterVisible();
								isGuestListResuming = false;
								providerGroupWaitingApprovalMember.getGroupWaitingMemberList(IN_GROUP_DETAILS.get(ID), new WebCallListener() {

									@Override
									public void onProgressUpdate(int progressCount) {

									}

									@Override
									public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
										pbrGroupMember.setVisibility(View.GONE);
										if (responseCode == 200) {
											groupWaitingApprovalMemberList = data1;
											if (groupWaitingApprovalMemberList.size() > 0) {
												prepareGuestList(groupWaitingApprovalMemberList, true);
												guestAdapter = getGuestListAdapter(true);
												lstGroupMember.setAdapter(guestAdapter);
												isGuestListResuming = true;
											} else {
												lstGroupMember.setAdapter(null);
											}

										} else {
											lstGroupMember.setAdapter(null);
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
	 * This method used to get waiting approval member list.
	 */
	private void groupWaitingApprovalMemberList() {
		pbrGroupMember.setVisibility(View.VISIBLE);
		providerGroupWaitingApprovalMember.getGroupWaitingMemberList(IN_GROUP_DETAILS.get(ID), new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {

			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				listFooterInvisible();
				pbrGroupMember.setVisibility(View.GONE);
				if (responseCode == 200) {
					groupWaitingApprovalMemberList = data1;
					if (groupWaitingApprovalMemberList.size() > 0) {
						prepareGuestList(groupWaitingApprovalMemberList, false);
						guestAdapter = getGuestListAdapter(true);
						lstGroupMember.setAdapter(guestAdapter);
					} else {
						lstGroupMember.setAdapter(null);
					}

				} else {
					lstGroupMember.setAdapter(null);
					responseErrorMessageHandler(responseCode, false);
				}
				isGuestListResuming = true;
			}
		});

	}

	/**
	 * This method used to show member dialog.
	 */
	@SuppressWarnings("deprecation")
	private void showMemberDialog() {
		groupAllMemberList = null;
		groupBanMemberList = null;
		currentList = MEMEBERLIST;
		providerGroupAllMember = new JomGroupDataProvider(this);
		providerGroupBanMember = new JomGroupDataProvider(this);
		isGuestListResuming = false;

		try {
			int popupWidth = getDeviceWidth() - convertSizeToDeviceDependent(50);
			int popupHeight = getDeviceHeight() - convertSizeToDeviceDependent(200);

			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = layoutInflater.inflate(R.layout.jom_group_member_popup, null);

			dialog = new PopupWindow(this);
			dialog.setContentView(layout);
			dialog.setWidth(popupWidth);
			dialog.setHeight(popupHeight);
			dialog.setFocusable(true);
			dialog.setBackgroundDrawable(new BitmapDrawable(getResources()));
			dialog.showAtLocation(layout, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);

			imgTagClose = (ImageView) layout.findViewById(R.id.imgTagClose);
			lstGroupMember = (ListView) layout.findViewById(R.id.lstGroupMember);
			pbrGroupMember = (ProgressBar) layout.findViewById(R.id.pbrGroupMember);
			txtGroupAllMember = (IjoomerTextView) layout.findViewById(R.id.txtGroupAllMember);
			txtGroupBanMember = (IjoomerTextView) layout.findViewById(R.id.txtGroupBanMember);
			lstGroupMember.addFooterView(listFooter, null, false);
			lstGroupMember.setAdapter(null);
			listFooterInvisible();

			if (adminMenu != null && adminMenu.getString(DELETEGROUP).equals("1")) {
				txtGroupBanMember.setVisibility(View.VISIBLE);
			} else {
				txtGroupBanMember.setVisibility(View.GONE);
			}
			groupAllMemberList();

			imgTagClose.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			txtGroupBanMember.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (!currentList.equals(BANMEMBERLIST)) {
						currentList = BANMEMBERLIST;
						txtGroupBanMember.setTextColor(getResources().getColor(R.color.jom_blue));
						txtGroupAllMember.setTextColor(getResources().getColor(R.color.jom_txt_color));
						isGuestListResuming = false;
						if (groupBanMemberList != null && groupBanMemberList.size() > 0) {
							prepareGuestList(groupBanMemberList, false);
							guestAdapter = getGuestListAdapter(false);
							lstGroupMember.setAdapter(guestAdapter);
							isGuestListResuming = true;
						} else {
							groupAllBanMemberList();
						}
					}
				}
			});

			txtGroupAllMember.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (!currentList.equals(MEMEBERLIST)) {
						currentList = MEMEBERLIST;
						txtGroupBanMember.setTextColor(getResources().getColor(R.color.jom_txt_color));
						txtGroupAllMember.setTextColor(getResources().getColor(R.color.jom_blue));
						isGuestListResuming = false;
						if (groupAllMemberList != null && groupAllMemberList.size() > 0) {
							prepareGuestList(groupAllMemberList, false);
							guestAdapter = getGuestListAdapter(false);
							lstGroupMember.setAdapter(guestAdapter);
							isGuestListResuming = true;
						} else {
							groupAllMemberList();
						}
					}
				}

			});

			lstGroupMember.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {

				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
					if (isGuestListResuming) {
						if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 1) {
							if (currentList.equals(MEMEBERLIST)) {
								if (!providerGroupAllMember.isCalling() && providerGroupAllMember.hasNextPage()) {
									listFooterVisible();
									isGuestListResuming = false;
									providerGroupBanMember.getGroupBanMemberList(IN_GROUP_DETAILS.get(ID), new WebCallListener() {
										@Override
										public void onProgressUpdate(int progressCount) {

										}

										@Override
										public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
											listFooterInvisible();
											if (responseCode == 200) {
												groupBanMemberList = data1;

												if (groupBanMemberList.size() > 0) {
													prepareGuestList(groupBanMemberList, true);
													guestAdapter = getGuestListAdapter(false);
													lstGroupMember.setAdapter(guestAdapter);
													isGuestListResuming = true;
												} else {
													lstGroupMember.setAdapter(null);
												}
											} else {
												lstGroupMember.setAdapter(null);
												responseErrorMessageHandler(responseCode, false);
											}
										}
									});
								}
							} else if (currentList.equals(BANMEMBERLIST)) {
								if (!providerGroupBanMember.isCalling() && providerGroupBanMember.hasNextPage()) {
									listFooterVisible();
									providerGroupBanMember.getGroupBanMemberList(IN_GROUP_DETAILS.get(ID), new WebCallListener() {

										@Override
										public void onProgressUpdate(int progressCount) {

										}

										@Override
										public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
											listFooterInvisible();
											if (responseCode == 200) {
												groupBanMemberList = data1;

												if (groupBanMemberList.size() > 0) {
													prepareGuestList(groupBanMemberList, true);
													guestAdapter = getGuestListAdapter(false);
													lstGroupMember.setAdapter(guestAdapter);
												} else {
													lstGroupMember.setAdapter(null);
												}
											} else {
												lstGroupMember.setAdapter(null);
												responseErrorMessageHandler(responseCode, false);
											}
										}
									});
								}
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
	 * This method used to get all member.
	 */
	private void groupAllMemberList() {
		pbrGroupMember.setVisibility(View.VISIBLE);
		providerGroupAllMember.getGroupMemberList(IN_GROUP_DETAILS.get(ID), new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {

			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				pbrGroupMember.setVisibility(View.GONE);

				if (responseCode == 200) {
					groupAllMemberList = data1;
					if (groupAllMemberList.size() > 0) {
						prepareGuestList(groupAllMemberList, false);
						guestAdapter = getGuestListAdapter(false);
						lstGroupMember.setAdapter(guestAdapter);

					} else {
						lstGroupMember.setAdapter(null);
					}

				} else {
					lstGroupMember.setAdapter(null);
					responseErrorMessageHandler(responseCode, false);
				}
				isGuestListResuming = true;
			}
		});

	}

	/**
	 * This method used to get ban member.
	 */
	private void groupAllBanMemberList() {
		pbrGroupMember.setVisibility(View.VISIBLE);
		providerGroupBanMember.getGroupBanMemberList(IN_GROUP_DETAILS.get(ID), new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {

			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				pbrGroupMember.setVisibility(View.GONE);
				if (responseCode == 200) {
					groupBanMemberList = data1;

					if (groupBanMemberList.size() > 0) {
						prepareGuestList(groupBanMemberList, false);
						guestAdapter = getGuestListAdapter(false);
						lstGroupMember.setAdapter(guestAdapter);
					} else {
						lstGroupMember.setAdapter(null);
					}
				} else {
					lstGroupMember.setAdapter(null);
					responseErrorMessageHandler(responseCode, false);
				}
				isGuestListResuming = true;
			}
		});

	}

	/**
	 * This method used to load file list.
	 */
	private void loadFileList() {
		fileDataProvider.restorePagingSettings();
		pbrGroupFiles.setVisibility(View.VISIBLE);
		fileDataProvider.getGroupFileList(IN_GROUP_DETAILS.get(ID), new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {

			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				pbrGroupFiles.setVisibility(View.GONE);
				if (responseCode == 200) {
					fileList = data1;
					prepareFileList(false);
					fileListAdapter = getFileListAdapter();
					lstGroupFiles.setAdapter(fileListAdapter);
					isGuestListResuming = true;
				} else {
					IjoomerUtilities.getCustomOkDialog(getString(R.string.group_files), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())),
							getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

						@Override
						public void NeutralMethod() {
							dialog.dismiss();
						}
					});
				}

			}
		});
	}

	/**
	 * This method used to show file dialog.
	 */
	@SuppressWarnings("deprecation")
	private void showFileDialog() {
		try {
			int popupWidth = getDeviceWidth() - convertSizeToDeviceDependent(50);
			int popupHeight = getDeviceHeight() - convertSizeToDeviceDependent(200);

			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = layoutInflater.inflate(R.layout.jom_group_file_popup, null);

			isGuestListResuming = false;
			dialog = new PopupWindow(this);
			dialog.setContentView(layout);
			dialog.setWidth(popupWidth);
			dialog.setHeight(popupHeight);
			dialog.setFocusable(true);
			dialog.setBackgroundDrawable(new BitmapDrawable(getResources()));
			dialog.showAtLocation(layout, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);

			imgTagClose = (ImageView) layout.findViewById(R.id.imgTagClose);
			lstGroupFiles = (ListView) layout.findViewById(R.id.lstGroupFiles);
			lstGroupFiles.addFooterView(listFooter, null, false);
			lstGroupFiles.setAdapter(null);

			pbrGroupFiles = (ProgressBar) layout.findViewById(R.id.pbrGroupFiles);
			pbrGroupFiles.setVisibility(View.GONE);
			loadFileList();

			imgTagClose.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			lstGroupFiles.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {

				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
					if (isGuestListResuming) {
						if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 1) {
							if (!fileDataProvider.isCalling()) {
								listFooterVisible();
								isGuestListResuming = false;
								fileDataProvider.getGroupFileList(IN_GROUP_DETAILS.get(ID), new WebCallListener() {

									@Override
									public void onProgressUpdate(int progressCount) {

									}

									@Override
									public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
										listFooterInvisible();
										if (responseCode == 200) {
											fileList = data1;
											prepareFileList(true);
											isGuestListResuming = true;
										} else {
											IjoomerUtilities.getCustomOkDialog(getString(R.string.group_files),
													getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok),
													R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

												@Override
												public void NeutralMethod() {
													dialog.dismiss();
												}
											});
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
	 * This method used to prepare list for group wall.
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
	public void prepareList(ArrayList<HashMap<String, String>> data, boolean append, boolean fromCache, int pageno, int pagelimit) {
		if (data != null && data.size() > 0) {
			if (!append) {
				listData.clear();
				recordCommentTotal = 0;
			} else {
				if (!fromCache) {
					int startIndex = ((pageno - 1) * pagelimit);
					int endIndex = wallListAdapterWithHolder.getCount();
					if (startIndex > 0) {
						for (int i = endIndex; i >= startIndex; i--) {
							try {
								wallListAdapterWithHolder.remove(wallListAdapterWithHolder.getItem(i));
								listData.remove(i);
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
					wallListAdapterWithHolder.add(item);
				} else {
					listData.add(item);
				}
			}
			if (recordCommentTotal > 1 && CURRENT_LIST.equals(ACTIVITES_LIST)) {
				lnrPlayRecordComment.setVisibility(View.VISIBLE);
				btnPlayAll.setCustomText(getString(R.string.play_all));
				txtTotalRecordComment.setText("(" + recordCommentTotal + ")");
			}

		}
	}

	/**
	 * This method used to prepare list for member.
	 * 
	 * @param data
	 *            represented member data
	 * @param append
	 *            represented data append
	 */
	private void prepareGuestList(ArrayList<HashMap<String, String>> data, boolean append) {
		if (data != null) {
			if (!append) {
				guestListData.clear();
			}
			for (HashMap<String, String> hashMap : data) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.jom_group_member_popup_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hashMap);
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
	 * This method used to prepare list for announcement.
	 * 
	 * @param data
	 *            represented announcement data
	 * @param append
	 *            represented data append
	 */
	public void prepareListAnnouncement(ArrayList<HashMap<String, String>> data, boolean append) {
		if (data != null) {
			if (!append) {
				listData.clear();
			}
			for (HashMap<String, String> hashMap : data) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.jom_group_announcement_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hashMap);
				item.setValues(obj);
				if (append) {
					announcementListAdapterWithHolder.add(item);
				} else {
					listData.add(item);
				}
			}

		}
	}

	/**
	 * This method used to prepare list for discussion.
	 * 
	 * @param data
	 *            represented discussion data
	 * @param append
	 *            represented data append
	 */
	public void prepareListDiscussion(ArrayList<HashMap<String, String>> data, boolean append) {
		if (data != null) {
			if (!append) {
				listData.clear();
			}
			for (HashMap<String, String> hashMap : data) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.jom_group_discussion_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hashMap);
				item.setValues(obj);
				if (append) {
					discussionListAdapterWithHolder.add(item);
				} else {
					listData.add(item);
				}
			}

		}
	}

	/**
	 * This method used to prepare list for file.
	 * 
	 * @param append
	 *            represented data append
	 */
	private void prepareFileList(boolean append) {

		if (fileList != null) {
			if (!append) {
				fileListData.clear();
			}
			for (HashMap<String, String> hashMap : fileList) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.jom_group_upload_file_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hashMap);
				item.setValues(obj);
				if (append) {
					fileListAdapter.add(item);
				} else {
					fileListData.add(item);
				}
			}

		}

	}

	/**
	 * This method used to prepare photo list album or photo wall.
	 * 
	 * @param lnrScrollable
	 *            represented scrollable layout
	 * @param data
	 *            represented photo list data
	 */
	private void preparePhotoList(ImageView firstimg, LinearLayout lnrScrollable, final HashMap<String, String> data) {
		JSONArray jsonArray;
		int length = 0;
		int fromposition = 0;
		try {
			jsonArray = new JSONArray(data.get(IMAGE_DATA));
			length = jsonArray.length() > 5 ? 5 : jsonArray.length();
		} catch (Throwable e1) {
			e1.printStackTrace();
			jsonArray = new JSONArray();
		}

		try {
			if(length == 1){
				LinearLayout.LayoutParams imgparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
				imgparams.gravity = Gravity.CENTER;
				firstimg.setLayoutParams(imgparams);

				androidQuery.id(firstimg).image(jsonArray.getJSONObject(0).getString(URL),true,true);

				firstimg.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						ArrayList<HashMap<String, String>> photoData = new ArrayList<HashMap<String, String>>();
						JSONArray jsonArray = null;
						try {
							jsonArray = new JSONArray(data.get(IMAGE_DATA));
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

						HashMap<String, String> IN_ALBUM = null;
						try {
							IN_ALBUM = (HashMap<String, String>) jsonToMap(new JSONObject(data.get(CONTENT_DATA)));
						} catch (JSONException e1) {
							e1.printStackTrace();
						}
						HashMap<String, String> usrData = new HashMap<String, String>(data);
						usrData.remove(ID);
						IN_ALBUM.putAll(usrData);
						try {
							loadNew(JomPhotoDetailsActivity.class, JomGroupDetailsActivity_v30.this, false, 
									"IN_PHOTO_LIST", photoData, "IN_SELECTED_INDEX",
									0, "IN_TOTAL_COUNT", 
									Integer.parseInt(IN_ALBUM.get(COUNT)), "IN_USERID", IN_USERID, "IN_ALBUM", IN_ALBUM);
						} catch (Throwable e) {
							e.printStackTrace();
						}

					}
				});

				lnrScrollable.removeAllViews();
			}else {
				if(length >= 5){
					fromposition = 1;
					LinearLayout.LayoutParams imgparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
					imgparams.gravity = Gravity.LEFT;
					firstimg.setLayoutParams(imgparams);

					androidQuery.id(firstimg).image(jsonArray.getJSONObject(0).getString(URL),true,true);

					firstimg.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							ArrayList<HashMap<String, String>> photoData = new ArrayList<HashMap<String, String>>();
							JSONArray jsonArray = null;
							try {
								jsonArray = new JSONArray(data.get(IMAGE_DATA));
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

							HashMap<String, String> IN_ALBUM = null;
							try {
								IN_ALBUM = (HashMap<String, String>) jsonToMap(new JSONObject(data.get(CONTENT_DATA)));
							} catch (JSONException e1) {
								e1.printStackTrace();
							}
							HashMap<String, String> usrData = new HashMap<String, String>(data);
							usrData.remove(ID);
							IN_ALBUM.putAll(usrData);
							try {
								loadNew(JomPhotoDetailsActivity.class, JomGroupDetailsActivity_v30.this, false, 
										"IN_PHOTO_LIST", photoData, "IN_SELECTED_INDEX",
										0, "IN_TOTAL_COUNT", Integer.parseInt(IN_ALBUM.get(COUNT)),
										"IN_USERID", IN_USERID, "IN_ALBUM", IN_ALBUM);
							} catch (Throwable e) {
								e.printStackTrace();
							}

						}
					});
				}else{
					fromposition = 0;
					firstimg.setVisibility(View.GONE);
				}

				System.out.println("DATA"+fromposition);
				lnrScrollable.removeAllViews();
				for (int i = fromposition; i < length; i++) {
					RelativeLayout lnrGuest = new RelativeLayout(this);
					lnrGuest.setTag(i);
					lnrGuest.setBackgroundColor(Color.parseColor(getString(R.color.jom_bg_color)));
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
					params.setMargins(5, 5, 5, 5);
					lnrGuest.setPadding(2, 2, 2, 2);

					ProgressBar progress = new ProgressBar(this, null, android.R.attr.progressBarStyleSmall);
					RelativeLayout.LayoutParams progressParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
					progress.setIndeterminate(true);
					progressParams.addRule(RelativeLayout.CENTER_IN_PARENT);

					lnrGuest.addView(progress, progressParams);

					ImageView imgUser = new ImageView(this); 		
					imgUser.setScaleType(ScaleType.FIT_XY);
					imgUser.setClickable(false);
					RelativeLayout.LayoutParams imgParams = new RelativeLayout.LayoutParams(convertSizeToDeviceDependent(50), convertSizeToDeviceDependent(50));
					imgParams.addRule(RelativeLayout.CENTER_IN_PARENT);

					try {
						androidQuery.id(imgUser).progress(progress).image(jsonArray.getJSONObject(i).getString(THUMB), true, true, getDeviceWidth(), 0);
					} catch (JSONException e) {
						e.printStackTrace();
					}

					lnrGuest.addView(imgUser, imgParams);
					lnrGuest.setTag(i);
					lnrGuest.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							ArrayList<HashMap<String, String>> photoData = new ArrayList<HashMap<String, String>>();
							JSONArray jsonArray = null;
							try {
								jsonArray = new JSONArray(data.get(IMAGE_DATA));
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

							HashMap<String, String> IN_ALBUM = null;
							try {
								IN_ALBUM = (HashMap<String, String>) jsonToMap(new JSONObject(data.get(CONTENT_DATA)));
							} catch (JSONException e1) {
								e1.printStackTrace();
							}
							HashMap<String, String> usrData = new HashMap<String, String>(data);
							usrData.remove(ID);
							IN_ALBUM.putAll(usrData);
							try {
								loadNew(JomPhotoDetailsActivity.class, JomGroupDetailsActivity_v30.this, false, "IN_PHOTO_LIST", photoData, "IN_SELECTED_INDEX",
										((Integer) v.getTag()).intValue(), "IN_TOTAL_COUNT", Integer.parseInt(IN_ALBUM.get(COUNT)), "IN_USERID", IN_USERID, "IN_ALBUM", IN_ALBUM);
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					});

					lnrScrollable.addView(lnrGuest, params);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * List adapter for member.
	 */

	private SmartListAdapterWithHolder getGuestListAdapter(final boolean isWaitingList) {

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.jom_group_member_popup_list_item, guestListData, new ItemView() {
			@Override
			public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {
				holder.imgGroupMemberAvatar = (ImageView) v.findViewById(R.id.imgGroupMemberAvatar);
				holder.txtGroupMemberName = (IjoomerTextView) v.findViewById(R.id.txtGroupMemberName);
				holder.btnGroupMemberRemove = (IjoomerButton) v.findViewById(R.id.btnGroupMemberRemove);
				holder.imgGroupMemberOnlineStatus = (ImageView) v.findViewById(R.id.imgGroupMemberOnlineStatus);
				holder.txtGroupMemberSetAsAdmin = (IjoomerTextView) v.findViewById(R.id.txtGroupMemberSetAsAdmin);
				holder.txtGroupMemberSetAsUser = (IjoomerTextView) v.findViewById(R.id.txtGroupMemberSetAsUser);
				holder.txtGroupMemberSetAsBan = (IjoomerTextView) v.findViewById(R.id.txtGroupMemberSetAsBan);
				holder.txtGroupMemberSetAsUnban = (IjoomerTextView) v.findViewById(R.id.txtGroupMemberSetAsUnban);
				holder.txtGroupMemberApproval = (IjoomerTextView) v.findViewById(R.id.txtGroupMemberApproval);
				holder.txtGroupMemberSetAsBan.setVisibility(View.GONE);
				holder.txtGroupMemberSetAsUnban.setVisibility(View.GONE);
				if (isWaitingList) {
					holder.txtGroupMemberApproval.setVisibility(View.VISIBLE);
				}

				holder.imgGroupMemberOnlineStatus.setImageDrawable(getResources().getDrawable(R.drawable.jom_friend_member_offline));
				@SuppressWarnings("unchecked")
				final HashMap<String, String> guest = (HashMap<String, String>) item.getValues().get(0);
				holder.txtGroupMemberName.setText(guest.get(USER_NAME));
				androidQuery.id(holder.imgGroupMemberAvatar).image(guest.get(USER_AVATAR), true, true, getDeviceWidth(), 0);

				if (guest.get(USER_ONLINE).equalsIgnoreCase("1")) {
					holder.imgGroupMemberOnlineStatus.setImageDrawable(getResources().getDrawable(R.drawable.jom_friend_member_online));
				}

				if (guest.get(CANREMOVE).equalsIgnoreCase("1")) {
					holder.btnGroupMemberRemove.setVisibility(View.VISIBLE);
				} else {
					holder.btnGroupMemberRemove.setVisibility(View.GONE);
				}

				if (guest.get(CANMEMBER).equalsIgnoreCase("1")) {
					holder.txtGroupMemberSetAsUser.setVisibility(View.VISIBLE);
				} else {
					holder.txtGroupMemberSetAsUser.setVisibility(View.GONE);
				}

				if (guest.get(CANADMIN).equalsIgnoreCase("1")) {
					holder.txtGroupMemberSetAsAdmin.setVisibility(View.VISIBLE);
				} else {
					holder.txtGroupMemberSetAsAdmin.setVisibility(View.GONE);
				}

				if (currentList.equals(MEMEBERLIST)) {
					if (guest.get(CANBAN).equalsIgnoreCase("1")) {
						holder.txtGroupMemberSetAsBan.setVisibility(View.VISIBLE);
						holder.txtGroupMemberSetAsUnban.setVisibility(View.GONE);
					}
				} else {
					if (guest.get(CANBAN).equalsIgnoreCase("1")) {
						holder.txtGroupMemberSetAsUnban.setVisibility(View.VISIBLE);
						holder.txtGroupMemberSetAsBan.setVisibility(View.GONE);
					}
				}

				holder.txtGroupMemberName.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (guest.get(USER_PROFILE).equalsIgnoreCase("1")) {
							gotoProfile(guest.get(USER_ID));
						}

					}
				});

				holder.txtGroupMemberApproval.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						provider.approvWaitingUser(guest.get(USER_ID), IN_GROUP_DETAILS.get(ID), new WebCallListener() {
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
									GROUP_DETAILS.put(MEMBERWAITING, "" + (Integer.parseInt(GROUP_DETAILS.get(MEMBERWAITING)) - 1));
									if (Integer.parseInt(GROUP_DETAILS.get(MEMBERWAITING)) <= 0) {
										txtPrivateGroupApprovalRequestList.setVisibility(View.GONE);
									} else {
										txtPrivateGroupApprovalRequestList.setText(String.format(getString(R.string.private_group_approval_awaiting),
												Integer.parseInt(GROUP_DETAILS.get(MEMBERWAITING))));
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

				holder.btnGroupMemberRemove.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						provider.removeGroupMember(guest.get(USER_ID), IN_GROUP_DETAILS.get(ID), new WebCallListener() {
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
										GROUP_DETAILS.put(MEMBERWAITING, "" + (Integer.parseInt(GROUP_DETAILS.get(MEMBERWAITING)) - 1));
										if (Integer.parseInt(GROUP_DETAILS.get(MEMBERWAITING)) <= 0) {
											txtPrivateGroupApprovalRequestList.setVisibility(View.GONE);
										} else {
											txtPrivateGroupApprovalRequestList.setText(String.format(getString(R.string.private_group_approval_awaiting),
													Integer.parseInt(GROUP_DETAILS.get(MEMBERWAITING))));
										}
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

				holder.txtGroupMemberSetAsAdmin.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						provider.setUserAsGroupAdmin(guest.get(USER_ID), IN_GROUP_DETAILS.get(ID), new WebCallListener() {
							final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

							@Override
							public void onProgressUpdate(int progressCount) {
								proSeekBar.setProgress(progressCount);
							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								if (responseCode == 200) {
									updateHeader(provider.getNotificationData());
									holder.txtGroupMemberSetAsAdmin.setVisibility(View.GONE);
									holder.txtGroupMemberSetAsUser.setVisibility(View.VISIBLE);

									if (isWaitingList) {
										GROUP_DETAILS.put(MEMBERWAITING, "" + (Integer.parseInt(GROUP_DETAILS.get(MEMBERWAITING)) - 1));
										if (Integer.parseInt(GROUP_DETAILS.get(MEMBERWAITING)) <= 0) {
											txtPrivateGroupApprovalRequestList.setVisibility(View.GONE);
										} else {
											txtPrivateGroupApprovalRequestList.setText(String.format(getString(R.string.private_group_approval_awaiting),
													Integer.parseInt(GROUP_DETAILS.get(MEMBERWAITING))));
										}
									} else if (currentList.equals(MEMEBERLIST)) {
										holder.txtGroupMemberSetAsBan.setVisibility(View.GONE);
										holder.txtGroupMemberSetAsUnban.setVisibility(View.GONE);
									} else {
										providerGroupAllMember.restorePagingSettings();
										providerGroupBanMember.restorePagingSettings();
										groupAllMemberList = null;
										groupBanMemberList = null;
										IjoomerApplicationConfiguration.setReloadRequired(true);
									}
									guestAdapter.remove(guestAdapter.getItem(position));
								} else {
									responseErrorMessageHandler(responseCode, false);
								}
							}
						});
					}
				});

				holder.txtGroupMemberSetAsUser.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						provider.setUserAsGroupMember(guest.get(USER_ID), IN_GROUP_DETAILS.get(ID), new WebCallListener() {
							final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

							@Override
							public void onProgressUpdate(int progressCount) {
								proSeekBar.setProgress(progressCount);
							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								if (responseCode == 200) {
									updateHeader(provider.getNotificationData());
									holder.txtGroupMemberSetAsAdmin.setVisibility(View.VISIBLE);
									holder.txtGroupMemberSetAsUser.setVisibility(View.GONE);
									if (currentList.equals(MEMEBERLIST)) {
										holder.txtGroupMemberSetAsBan.setVisibility(View.VISIBLE);
										holder.txtGroupMemberSetAsUnban.setVisibility(View.GONE);
									}

								} else {
									responseErrorMessageHandler(responseCode, false);
								}
							}
						});
					}
				});

				holder.txtGroupMemberSetAsBan.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						provider.setUserAsGroupBan(guest.get(USER_ID), IN_GROUP_DETAILS.get(ID), new WebCallListener() {
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
										GROUP_DETAILS.put(MEMBERWAITING, "" + (Integer.parseInt(GROUP_DETAILS.get(MEMBERWAITING)) - 1));
										if (Integer.parseInt(GROUP_DETAILS.get(MEMBERWAITING)) <= 0) {
											txtPrivateGroupApprovalRequestList.setVisibility(View.GONE);
										} else {
											txtPrivateGroupApprovalRequestList.setText(String.format(getString(R.string.private_group_approval_awaiting),
													Integer.parseInt(GROUP_DETAILS.get(MEMBERWAITING))));
										}
									}
									groupBanMemberList = null;
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

				holder.txtGroupMemberSetAsUnban.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						provider.setUserAsGroupUnban(guest.get(USER_ID), IN_GROUP_DETAILS.get(ID), new WebCallListener() {
							final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

							@Override
							public void onProgressUpdate(int progressCount) {
								proSeekBar.setProgress(progressCount);
							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								if (responseCode == 200) {
									updateHeader(provider.getNotificationData());
									guestAdapter.remove(guestAdapter.getItem(position));
									groupAllMemberList = null;
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
	 * List adapter for group wall.
	 * 
	 * @return represented {@link SmartListAdapterWithHolder}
	 */
	private SmartListAdapterWithHolder getListAdapter() {
		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.jom_wall_activity_list_item, listData, new ItemView() {

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
				holder.lnrWallOrActivityContentView = (LinearLayout) v.findViewById(R.id.lnrWallOrActivityContentView);
				holder.imgWallOrActvityUploadedPhotos = (ImageView) v.findViewById(R.id.imguploadedPhotos);
				holder.lnrContentImageScrollable = (LinearLayout) v.findViewById(R.id.lnrContentImageScrollable);
				holder.lnrWallOrActivityLikeCommnet = (LinearLayout) v.findViewById(R.id.lnrWallOrActivityLikeCommnet);
				holder.btnWallOrActivityRemove = (IjoomerButton) v.findViewById(R.id.btnWallOrActivityRemove);
				holder.imgWallOrActvityContentVideoImage = (ImageView) v.findViewById(R.id.imgWallOrActvityContentVideoImage);
				holder.btnPlayStopVoice = (IjoomerVoiceButton) v.findViewById(R.id.btnPlayStopVoice);
				holder.btnPlayStopVoice.setVisibility(View.GONE);
				holder.btnWallOrActivityRemove.setVisibility(View.GONE);
				holder.lnrWallOrActivityContentImage.setVisibility(View.GONE);
				holder.lnrWallOrActivityContentCoverPhoto.setVisibility(View.GONE);
				holder.lnrWallOrActivityContentVideo.setVisibility(View.GONE);
				holder.lnrWallOrActivityContentView.setVisibility(View.GONE);
				holder.txtWallOrActvityContent.setVisibility(View.GONE);
				@SuppressWarnings("unchecked")
				final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);

				androidQuery.id(holder.imgWallOrActvityUserAvatar).image(row.get(USER_AVATAR), true, true, getDeviceWidth(), 0);
				holder.txtWallOrActvityTitle.setMovementMethod(LinkMovementMethod.getInstance());
				holder.txtWallOrActvityTitle.setText(addClickablePart(Html.fromHtml(getPlainText(row.get(TITLETAG)).replace("\u25ba", "\u25b6")), row, row.get(TYPE), IN_USERID),
						BufferType.SPANNABLE);
				holder.txtWallOrActvityUserName.setText(row.get(USER_NAME));
				holder.txtWallOrActvityDate.setText(row.get(DATE));
				if (row.get(CONTENT).toString().trim().length() > 0) {
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
						|| row.get(TYPE).toString().trim().equals(GROUPSAVATARUPLOAD)) {
					holder.lnrWallOrActivityContentCoverPhoto.setVisibility(View.VISIBLE);
					try{
						JSONObject imageData = new JSONObject(row.get(IMAGE_DATA));
						androidQuery.id(holder.imgWallOrActvityCoverPhoto).progress(R.id.coverimgprogress).image(imageData.getString(URL),true,true);
					}catch (JSONException e){
						e.printStackTrace();
					}
				}else if (row.get(TYPE).toString().trim().equals(PHOTOS)) {
					holder.lnrWallOrActivityContentImage.setVisibility(View.VISIBLE);
					preparePhotoList(holder.imgWallOrActvityUploadedPhotos, holder.lnrContentImageScrollable, row);
				}else if (row.get(TYPE).toString().trim().equals(VIDEOS)) {
					holder.lnrWallOrActivityContentVideo.setVisibility(View.VISIBLE);

					try {
						androidQuery.id(holder.imgWallOrActvityContentVideoImage).image(new JSONObject(row.get(CONTENT_DATA)).getString(THUMB), true, true, getDeviceWidth(), 0);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}else if (row.get(TYPE).toString().trim().equals(EVENT)) {
					try{
						holder.lnrWallOrActivityContentView.setVisibility(View.VISIBLE);
						holder.lnrWallOrActivityContentView.removeAllViews();

						JSONObject eventData = new JSONObject(row.get(CONTENT_DATA));

						LayoutInflater inflater = LayoutInflater.from(JomGroupDetailsActivity_v30.this);
						View eventView = inflater.inflate(R.layout.jom_wall_activity_event_details_item, null);
						eventView.setTag(eventData);

						androidQuery.id(((ImageView) eventView.findViewById(R.id.imgEventAvatar))).image(eventData.getString(AVATAR),true,true);
						((IjoomerTextView) eventView.findViewById(R.id.txtEventTitle)).setText(eventData.getString(TITLE));
						((IjoomerTextView) eventView.findViewById(R.id.txtEventStartEndDate)).setText(eventData.getString(STARTDATE));
						((IjoomerTextView) eventView.findViewById(R.id.txEventLocation)).setText(eventData.getString(LOCATION));

						eventView.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								JSONObject jsonObject = (JSONObject) v.getTag();
								String IN_GROUP_ID = "0";
								if (jsonObject.has(GROUPID)) {
									try {
										IN_GROUP_ID = jsonObject.getString(GROUPID);
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}

								if (IjoomerGlobalConfiguration.getJomsocialVersion().equals(IjoomerGlobalConfiguration.JOMVERSION_V30)) {
									try {
										loadNew(JomEventDetailsActivity_v30.class, JomGroupDetailsActivity_v30.this, false, "IN_USERID", IN_USERID, "IN_EVENT_DETAILS", jsonToMap(jsonObject),
												"IN_GROUP_ID", IN_GROUP_ID);
									} catch (Throwable e) {
										e.printStackTrace();
									}
								} else {
									try {
										loadNew(JomEventDetailsActivity.class, JomGroupDetailsActivity_v30.this, false, "IN_USERID", IN_USERID, "IN_EVENT_DETAILS", jsonToMap(jsonObject),
												"IN_GROUP_ID", IN_GROUP_ID);
									} catch (Throwable e) {
										e.printStackTrace();
									}
								}
							}
						});
						holder.lnrWallOrActivityContentView.addView(eventView);
					}catch (Throwable e){
						e.printStackTrace();
					}
				}else if (row.get(TYPE).toString().trim().equals(DISCUSSION)) {
					try{
						holder.lnrWallOrActivityContentView.setVisibility(View.VISIBLE);
						holder.lnrWallOrActivityContentView.removeAllViews();

						JSONObject discussionData = new JSONObject(row.get(CONTENT_DATA));

						LayoutInflater inflater = LayoutInflater.from(JomGroupDetailsActivity_v30.this);
						View discussionView = inflater.inflate(R.layout.jom_wall_activity_discussion_details_item, null);
						discussionView.setTag(row);

						((IjoomerTextView) discussionView.findViewById(R.id.txtDiscussionTitle)).setText(discussionData.getString(TITLE));
						((IjoomerTextView) discussionView.findViewById(R.id.txtDiscussionDiscription)).setText(discussionData.getString(MESSAGE));

						discussionView.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								try {
									@SuppressWarnings("unchecked")
									HashMap<String, String> row = (HashMap<String, String>) v.getTag();
									final JSONObject jsonObject = new JSONObject(row.get(GROUP_DATA));
									final JSONObject jsonObjectDiscussion = new JSONObject(row.get(CONTENT_DATA));
									try {
										loadNew(JomGroupDiscussionDetailsActivity.class, JomGroupDetailsActivity_v30.this, false, "IN_DISCUSSION_DETAILS", jsonToMap(jsonObjectDiscussion),
												"IN_GROUP_DETAILS", jsonToMap(jsonObject));
									} catch (Throwable e) {
										e.printStackTrace();
									}
								} catch (Throwable e) {
									e.printStackTrace();
								}
							}
						});
						holder.lnrWallOrActivityContentView.addView(discussionView);
					}catch (JSONException e){
						e.printStackTrace();
					}
				}else if (row.get(TYPE).toString().trim().equals(ANNOUNCEMENT)) {
					try{
						holder.lnrWallOrActivityContentView.setVisibility(View.VISIBLE);
						holder.lnrWallOrActivityContentView.removeAllViews();

						JSONObject annoucementData = new JSONObject(row.get(CONTENT_DATA));

						LayoutInflater inflater = LayoutInflater.from(JomGroupDetailsActivity_v30.this);
						View annoucementView = inflater.inflate(R.layout.jom_wall_activity_annoucement_details_item, null);
						annoucementView.setTag(row);

						((IjoomerTextView) annoucementView.findViewById(R.id.txtAnnouncementTitle)).setText(annoucementData.getString(TITLE));
						((IjoomerTextView) annoucementView.findViewById(R.id.txtAnnouncementDiscription)).setText(annoucementData.getString(MESSAGE));

						annoucementView.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								try {
									@SuppressWarnings("unchecked")
									HashMap<String, String> row = (HashMap<String, String>) v.getTag();
									final JSONObject jsonObject = new JSONObject(row.get(GROUP_DATA));
									final JSONObject jsonObjectAnnouncement = new JSONObject(row.get(CONTENT_DATA));
									try {
										loadNew(JomGroupAnnouncementDetailsActivity.class, JomGroupDetailsActivity_v30.this, false, "IN_ANNOUCEMENT_DETAILS", jsonToMap(jsonObjectAnnouncement),
												"IN_GROUP_DETAILS", jsonToMap(jsonObject));
									} catch (Throwable e) {
										e.printStackTrace();
									}
								} catch (Throwable e) {
									e.printStackTrace();
								}
							}
						});
						holder.lnrWallOrActivityContentView.addView(annoucementView);
					}catch (JSONException e){
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
								loadNew(JomWallOrActivityDetailActivity.class, JomGroupDetailsActivity_v30.this, false, "IN_WALL_DETAILS", IN_WALL_DETAILS,
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
								loadNew(JomWallOrActivityDetailActivity.class, JomGroupDetailsActivity_v30.this, false, "IN_WALL_DETAILS", IN_WALL_DETAILS,
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
										try {
											if (row.get(LIKETYPE).equals(VIDEOS)) {
												JSONObject likes = new JSONObject(row.get(CONTENT_DATA));
												likes.put(LIKES, "" + (Integer.parseInt(holder.txtWallOrActivityLikeCount.getText().toString()) - 1));
												likes.put(LIKED, "0");

												row.put(CONTENT_DATA, likes.toString());
												holder.txtWallOrActvityTitle.setText(
														addClickablePart(Html.fromHtml(row.get(TITLETAG).replace("\u25ba", "\u25b6")), row, row.get(TYPE), IN_USERID),
														BufferType.SPANNABLE);
												holder.txtWallOrActvityContent.setText(addClickablePart(Html.fromHtml(row.get(CONTENT)), row, row.get(TYPE), IN_USERID),
														BufferType.SPANNABLE);
											}
										} catch (Exception e) {
										}
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
										try {
											if (row.get(LIKETYPE).equals(VIDEOS)) {
												JSONObject likes = new JSONObject(row.get(CONTENT_DATA));
												likes.put(LIKES, "" + (Integer.parseInt(holder.txtWallOrActivityLikeCount.getText().toString()) + 1));
												likes.put(LIKED, "1");

												row.put(CONTENT_DATA, likes.toString());
												holder.txtWallOrActvityTitle.setText(
														addClickablePart(Html.fromHtml(row.get(TITLETAG).replace("\u25ba", "\u25b6")), row, row.get(TYPE), IN_USERID),
														BufferType.SPANNABLE);
												holder.txtWallOrActvityContent.setText(addClickablePart(Html.fromHtml(row.get(CONTENT)), row, row.get(TYPE), IN_USERID),
														BufferType.SPANNABLE);
											}
										} catch (Exception e) {
										}
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
											wallListAdapterWithHolder.remove(wallListAdapterWithHolder.getItem(position));
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
							loadNew(JomWallOrActivityDetailActivity.class, JomGroupDetailsActivity_v30.this, false, "IN_WALL_DETAILS", IN_WALL_DETAILS,
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
							Intent lVideoIntent = new Intent(null, getVideoPlayURI(url), JomGroupDetailsActivity_v30.this, IjoomerMediaPlayer.class);
							startActivity(lVideoIntent);
						} catch (Exception e) {
							e.printStackTrace();
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

	/**
	 * List adapter for announcement.
	 * 
	 * @return represented {@link SmartListAdapterWithHolder}
	 */
	private SmartListAdapterWithHolder getAnnouncementListAdapter() {
		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(JomGroupDetailsActivity_v30.this, R.layout.jom_group_announcement_list_item, listData,
				new ItemView() {
			@Override
			public View setItemView(int position, View v, SmartListItem item, ViewHolder holder) {
				holder.txtAnnouncementTitle = (IjoomerTextView) v.findViewById(R.id.txtAnnouncementTitle);
				holder.txtAnnouncementStartedBy = (IjoomerTextView) v.findViewById(R.id.txtAnnouncementStartedBy);
				holder.txtAnnouncementStartedOn = (IjoomerTextView) v.findViewById(R.id.txtAnnouncementStartedOn);
				@SuppressWarnings("unchecked")
				final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);

				holder.txtAnnouncementTitle.setText(row.get(TITLE));
				holder.txtAnnouncementStartedBy.setText(row.get(USER_NAME));
				holder.txtAnnouncementStartedOn.setText(row.get(DATE));

				v.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						IN_GROUP_DETAILS.putAll(GROUP_DETAILS);
						try {
							loadNew(JomGroupAnnouncementDetailsActivity.class, JomGroupDetailsActivity_v30.this, false, "IN_ANNOUCEMENT_DETAILS", row, "IN_GROUP_DETAILS",
									IN_GROUP_DETAILS);
						} catch (Throwable e) {
							e.printStackTrace();
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

	/**
	 * List adapter for discussion.
	 * 
	 * @return represented {@link SmartListAdapterWithHolder}
	 */
	private SmartListAdapterWithHolder getDiscussionListAdapter() {
		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(JomGroupDetailsActivity_v30.this, R.layout.jom_group_discussion_list_item, listData,
				new ItemView() {
			@Override
			public View setItemView(int position, View v, SmartListItem item, ViewHolder holder) {
				holder.txtDiscussionTitle = (IjoomerTextView) v.findViewById(R.id.txtDiscussionTitle);
				holder.txtDiscussionStartedBy = (IjoomerTextView) v.findViewById(R.id.txtDiscussionStartedBy);
				holder.txtDiscussionReplies = (IjoomerTextView) v.findViewById(R.id.txtDiscussionReplies);
				@SuppressWarnings("unchecked")
				final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);

				holder.txtDiscussionTitle.setText(row.get(TITLE));
				holder.txtDiscussionStartedBy.setText(String.format(getString(R.string.group_discussion_startedby), row.get(USER_NAME)));
				holder.txtDiscussionReplies.setText(row.get(TOPICS) + " " + getString(R.string.replies));

				v.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						IN_GROUP_DETAILS.putAll(GROUP_DETAILS);
						try {
							loadNew(JomGroupDiscussionDetailsActivity.class, JomGroupDetailsActivity_v30.this, false, "IN_DISCUSSION_DETAILS", row, "IN_GROUP_DETAILS",
									IN_GROUP_DETAILS);
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				});

				return v;
			}

			public View setItemView(int position, View v, SmartListItem item) {
				return null;
			}
		});
		return adapterWithHolder;
	}

	/**
	 * List adapter for file
	 * 
	 * @return represented {@link SmartListAdapterWithHolder}
	 */
	private SmartListAdapterWithHolder getFileListAdapter() {

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.jom_group_upload_file_list_item, fileListData, new ItemView() {
			@Override
			public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {

				holder.txtFileTitle = (IjoomerTextView) v.findViewById(R.id.txtFileTitle);
				holder.txtFileHit = (IjoomerTextView) v.findViewById(R.id.txtFileHit);
				holder.txtFileSize = (IjoomerTextView) v.findViewById(R.id.txtFileSize);
				holder.txtFileDesc = (IjoomerTextView) v.findViewById(R.id.txtFileDesc);
				holder.btnFileRemove = (IjoomerButton) v.findViewById(R.id.btnFileRemove);
				@SuppressWarnings("unchecked")
				final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);
				holder.txtFileTitle.setText(row.get(NAME));
				holder.txtFileSize.setText(getString(R.string.size) + ":" + IjoomerUtilities.readableFileSize(Long.parseLong(row.get(SIZE))));
				holder.txtFileHit.setText(getString(R.string.hits) + ":" + row.get(HITS));
				holder.txtFileDesc.setText(String.format(getString(R.string.by), row.get(USER_NAME) + " " + row.get(DATE)));

				if (row.get(DELETEALLOWED).equals("1")) {
					holder.btnFileRemove.setVisibility(View.VISIBLE);
				} else {
					holder.btnFileRemove.setVisibility(View.GONE);
				}
				holder.txtFileTitle.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						downlodIndex = position;
						Intent intent = new Intent(JomGroupDetailsActivity_v30.this, IjoomerFileChooserActivity.class);
						startActivityForResult(intent, DOWNLOAD_FILE_LOCATION);
					}
				});
				holder.btnFileRemove.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						provider.removeFile(row.get(ID), new WebCallListener() {
							final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

							@Override
							public void onProgressUpdate(int progressCount) {
								proSeekBar.setProgress(progressCount);
							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								if (responseCode == 200) {
									IjoomerUtilities.getCustomOkDialog(getString(R.string.group_files), getString(R.string.file_removed_successfully), getString(R.string.ok),
											R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

										@Override
										public void NeutralMethod() {
											fileListAdapter.remove(fileListAdapter.getItem(position));
											if (fileListAdapter.getCount() <= 0) {
												dialog.dismiss();
											}
										}
									});
								} else {
									IjoomerUtilities.getCustomOkDialog(getString(R.string.group_files),
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
