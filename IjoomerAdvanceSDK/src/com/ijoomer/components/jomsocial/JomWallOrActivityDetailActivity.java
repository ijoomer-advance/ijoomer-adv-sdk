package com.ijoomer.components.jomsocial;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView.BufferType;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.customviews.IjoomerAudioPlayer.AudioListener;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.customviews.IjoomerVoiceAndTextMessager;
import com.ijoomer.customviews.IjoomerVoiceAndTextMessager.MessageHandler;
import com.ijoomer.customviews.IjoomerVoiceButton;
import com.ijoomer.library.jomsocial.JomWallDataProvider;
import com.ijoomer.media.player.IjoomerMediaPlayer;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertMagnatic;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

/**
 * This Class Contains All Method Related To JomWallOrActivityDetailActivity.
 * 
 * @author tasol
 * 
 */
public class JomWallOrActivityDetailActivity extends JomMasterActivity {

	private LinearLayout lnrWallOrActivityDetailContentImage;
	private RelativeLayout lnrWallOrActivityContentCoverPhoto;
	private RelativeLayout lnrWallOrActivityDetailContentVideo;
	private LinearLayout lnrWallOrActivityDetailContentView;
	private LinearLayout lnrWallOrActivityDetailsWriteComment;
	private LinearLayout lnrContentImageScrollable;
	private ListView listView;
	private IjoomerTextView txtWallOrActvityDetailTitle;
	private IjoomerTextView txtWallOrActvityDetailContent;
	private IjoomerTextView txtWallOrActvityDetailUserName;
	private IjoomerTextView txtWallOrActvityDetailDate;
	private IjoomerTextView txtWallOrActvityDetailLikeCount;
	private IjoomerTextView txtWallOrActvityDetailCommentCount;
	private ImageView imgWallOrActvityDetailContentVideoImage;
	private ImageView imgWallOrActvityDetailUserAvatar;
	private ImageView imgWallOrActvityCoverPhoto;
	private ImageView imgWallOrActvityDetailUploadedPhotos;
	private IjoomerVoiceAndTextMessager voiceMessager;
	private IjoomerVoiceButton btnPlayStopVoice;
	private View headerView;
	private View listFooter;
	private AQuery androidQuery;
	private ArrayList<HashMap<String, String>> IN_WALL_DETAILS;
	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder listAdapterWithHolder;

	private JomWallDataProvider wallDataProvider;

	private String IN_WALL_DETAILS_LIST_TYPE;
	private String IN_USERID;

	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.jom_wall_activity_detail_list;
	}

	@SuppressWarnings("static-access")
	@Override
	public void initComponents() {

		headerView = (View) getLayoutInflater().from(this).inflate(R.layout.jom_wall_activity_detail_header, null);
		listFooter = LayoutInflater.from(this).inflate(R.layout.ijoomer_list_footer, null);
		listView = (ListView) findViewById(R.id.listView);
		listView.addHeaderView(headerView, null, false);
		listView.addFooterView(listFooter, null, false);
		listView.setAdapter(null);
		lnrWallOrActivityDetailsWriteComment = (LinearLayout) findViewById(R.id.lnrWallOrActivityDetailsWriteComment);
		lnrWallOrActivityDetailContentImage = (LinearLayout) headerView.findViewById(R.id.lnrWallOrActivityDetailContentImage);
		imgWallOrActvityDetailUploadedPhotos = (ImageView) headerView.findViewById(R.id.imguploadedPhotos);
		lnrContentImageScrollable = (LinearLayout) headerView.findViewById(R.id.lnrContentImageScrollable);
		lnrWallOrActivityContentCoverPhoto = (RelativeLayout) findViewById(R.id.lnrWallOrActivityContentCoverPhoto);
		imgWallOrActvityCoverPhoto = (ImageView) findViewById(R.id.imgWallOrActvityCoverPhoto);
		lnrWallOrActivityDetailContentVideo = (RelativeLayout) headerView.findViewById(R.id.lnrWallOrActivityDetailContentVideo);
		txtWallOrActvityDetailUserName = (IjoomerTextView) headerView.findViewById(R.id.txtWallOrActvityDetailUserName);
		txtWallOrActvityDetailDate = (IjoomerTextView) headerView.findViewById(R.id.txtWallOrActvityDetailDate);
		txtWallOrActvityDetailTitle = (IjoomerTextView) headerView.findViewById(R.id.txtWallOrActvityDetailTitle);
		txtWallOrActvityDetailContent = (IjoomerTextView) headerView.findViewById(R.id.txtWallOrActvityDetailContent);
		txtWallOrActvityDetailLikeCount = (IjoomerTextView) headerView.findViewById(R.id.txtWallOrActvityDetailLikeCount);
		txtWallOrActvityDetailCommentCount = (IjoomerTextView) headerView.findViewById(R.id.txtWallOrActvityDetailCommentCount);
		imgWallOrActvityDetailContentVideoImage = (ImageView) headerView.findViewById(R.id.imgWallOrActvityDetailContentVideoImage);
		lnrWallOrActivityDetailContentView = (LinearLayout) headerView.findViewById(R.id.lnrWallOrActivityDetailContentView);
		imgWallOrActvityDetailUserAvatar = (ImageView) headerView.findViewById(R.id.imgWallOrActvityDetailUserAvatar);
		voiceMessager = (IjoomerVoiceAndTextMessager) findViewById(R.id.voiceMessager);
		btnPlayStopVoice = (IjoomerVoiceButton) headerView.findViewById(R.id.btnPlayStopVoice);
		androidQuery = new AQuery(this);
		listData = new ArrayList<SmartListItem>();

		wallDataProvider = new JomWallDataProvider(this);

		getIntentData();
	}

	@Override
	public void prepareViews() {
		if (IN_WALL_DETAILS_LIST_TYPE.equals(LIKES)) {
			getLikeList();
		} else {
			try {
				if (!IN_WALL_DETAILS.get(0).get(COMMENTCOUNT).equals("0")) {
					getCommentList();
				}
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void setActionListeners() {

		if (getAudio(IN_WALL_DETAILS.get(0).get(TITLETAG)) != null) {

			btnPlayStopVoice.setVisibility(View.VISIBLE);
			btnPlayStopVoice.setText(getAudioLength(IN_WALL_DETAILS.get(0).get(TITLETAG)));
			btnPlayStopVoice.setAudioPath(getAudio(IN_WALL_DETAILS.get(0).get(TITLETAG)), false);
			btnPlayStopVoice.setAudioListener(new AudioListener() {

				@Override
				public void onReportClicked() {
					reportVoice(getAudio(IN_WALL_DETAILS.get(0).get(TITLETAG)));
				}

				@Override
				public void onPrepared() {
				}

				@Override
				public void onPlayClicked(boolean isplaying) {
				}

				@Override
				public void onComplete() {
				}
			});

		}

		if (getAudio(IN_WALL_DETAILS.get(0).get(CONTENT)) != null) {

			btnPlayStopVoice.setVisibility(View.VISIBLE);
			btnPlayStopVoice.setText(getAudioLength(IN_WALL_DETAILS.get(0).get(CONTENT)));
			btnPlayStopVoice.setAudioPath(getAudio(IN_WALL_DETAILS.get(0).get(CONTENT)), false);
			btnPlayStopVoice.setAudioListener(new AudioListener() {

				@Override
				public void onReportClicked() {
					reportVoice(getAudio(IN_WALL_DETAILS.get(0).get(CONTENT)));
				}

				@Override
				public void onPrepared() {
				}

				@Override
				public void onPlayClicked(boolean isplaying) {
				}

				@Override
				public void onComplete() {
				}
			});

		}

		txtWallOrActvityDetailLikeCount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				String likeID = "0";
				if (IN_WALL_DETAILS.get(0).get(LIKETYPE).equals(VIDEOS)) {
					try {
						likeID = new JSONObject(IN_WALL_DETAILS.get(0).get(CONTENT_DATA)).getString(ID);
					} catch (JSONException e) {
						e.printStackTrace();
						likeID = "0";
					}
				} else {
					likeID = IN_WALL_DETAILS.get(0).get(ID);
				}
				if (IN_WALL_DETAILS.get(0).get(LIKED).equals("1")) {
					wallDataProvider.unlikeWall(likeID, IN_WALL_DETAILS.get(0).get(LIKETYPE), new WebCallListener() {
						SeekBar skBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

						@Override
						public void onProgressUpdate(int progressCount) {
							skBar.setProgress(progressCount);
						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								IjoomerApplicationConfiguration.setReloadRequired(true);
								updateHeader(wallDataProvider.getNotificationData());
								IN_WALL_DETAILS.get(0).put(LIKED, "0");
								IN_WALL_DETAILS.get(0).put(LIKECOUNT, String.valueOf(Integer.parseInt(IN_WALL_DETAILS.get(0).get(LIKECOUNT)) - 1));
								txtWallOrActvityDetailLikeCount.setText(IN_WALL_DETAILS.get(0).get(LIKECOUNT));
								getLikeList();
							} else {
								responseErrorMessageHandler(responseCode, false);
							}
						}
					});
				} else {
					wallDataProvider.likeWall(likeID, IN_WALL_DETAILS.get(0).get(LIKETYPE), new WebCallListener() {
						SeekBar skBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

						@Override
						public void onProgressUpdate(int progressCount) {
							skBar.setProgress(progressCount);
						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								IjoomerApplicationConfiguration.setReloadRequired(true);
								updateHeader(wallDataProvider.getNotificationData());
								IN_WALL_DETAILS.get(0).put(LIKED, "1");
								IN_WALL_DETAILS.get(0).put(LIKECOUNT, String.valueOf(Integer.parseInt(IN_WALL_DETAILS.get(0).get(LIKECOUNT)) + 1));
								txtWallOrActvityDetailLikeCount.setText(IN_WALL_DETAILS.get(0).get(LIKECOUNT));
								getLikeList();
							} else {
								responseErrorMessageHandler(responseCode, false);
							}
						}
					});
				}

			}
		});
		imgWallOrActvityDetailContentVideoImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					String videoPath = new JSONObject(IN_WALL_DETAILS.get(0).get(CONTENT_DATA)).getString(VIDEO_PATH);
					Intent lVideoIntent = new Intent(null,getVideoPlayURI(videoPath), JomWallOrActivityDetailActivity.this, IjoomerMediaPlayer.class);
					startActivity(lVideoIntent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		voiceMessager.setMessageHandler(new MessageHandler() {

			@Override
			public void onVoiceMessageRecordingComplete(String message, String voiceMessagePath) {
				new JomWallDataProvider(JomWallOrActivityDetailActivity.this).writeComment(IN_WALL_DETAILS.get(0).get(ID), message, voiceMessagePath, new WebCallListener() {

					@Override
					public void onProgressUpdate(int progressCount) {
					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						if (responseCode == 200) {
							IjoomerApplicationConfiguration.setReloadRequired(true);
							updateHeader(wallDataProvider.getNotificationData());
							IN_WALL_DETAILS.get(0).put(COMMENTCOUNT, String.valueOf(Integer.parseInt(IN_WALL_DETAILS.get(0).get(COMMENTCOUNT)) + 1));
							txtWallOrActvityDetailCommentCount.setText(String.format(getString(R.string.comment_count), Integer.parseInt(IN_WALL_DETAILS.get(0).get(COMMENTCOUNT))));
							getCommentList();
						} else {
							responseErrorMessageHandler(responseCode, false);
						}
					}
				});
			}

			@Override
			public void onButtonSend(String message) {
				new JomWallDataProvider(JomWallOrActivityDetailActivity.this).writeComment(IN_WALL_DETAILS.get(0).get(ID), message, null, new WebCallListener() {

					@Override
					public void onProgressUpdate(int progressCount) {
					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						if (responseCode == 200) {
							IjoomerApplicationConfiguration.setReloadRequired(true);
							updateHeader(wallDataProvider.getNotificationData());
							IN_WALL_DETAILS.get(0).put(COMMENTCOUNT, String.valueOf(Integer.parseInt(IN_WALL_DETAILS.get(0).get(COMMENTCOUNT)) + 1));
							txtWallOrActvityDetailCommentCount.setText(String.format(getString(R.string.comment_count), Integer.parseInt(IN_WALL_DETAILS.get(0).get(COMMENTCOUNT))));
							getCommentList();
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

		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 2) {
					if (!wallDataProvider.isCalling() && wallDataProvider.hasNextPage()) {
						listFooterVisible();
						if (IN_WALL_DETAILS_LIST_TYPE.equals(LIKES)) {
							wallDataProvider.getLikeList(IN_WALL_DETAILS.get(0).get(ID), new WebCallListener() {

								@Override
								public void onProgressUpdate(int progressCount) {
								}

								@Override
								public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
									listFooterInvisible();
									if (responseCode == 200) {
										updateHeader(wallDataProvider.getNotificationData());
										prepareList(data1, true);
									}
								}
							});
						} else {
							wallDataProvider.getCommentList(IN_WALL_DETAILS.get(0).get(ID), new WebCallListener() {

								@Override
								public void onProgressUpdate(int progressCount) {
								}

								@Override
								public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
									listFooterInvisible();
									if (responseCode == 200) {
										updateHeader(wallDataProvider.getNotificationData());
										prepareList(data1, true);
									}
								}
							});
						}
					}
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
		IN_WALL_DETAILS = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("IN_WALL_DETAILS") == null ? new ArrayList<HashMap<String, String>>()
				: (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("IN_WALL_DETAILS");
		IN_WALL_DETAILS_LIST_TYPE = getIntent().getStringExtra("IN_WALL_DETAILS_LIST_TYPE") == null ? "0" : getIntent().getStringExtra("IN_WALL_DETAILS_LIST_TYPE");
		IN_USERID = getIntent().getStringExtra("IN_USERID") == null ? "0" : getIntent().getStringExtra("IN_USERID");

		txtWallOrActvityDetailUserName.setText(IN_WALL_DETAILS.get(0).get(USER_NAME));
		txtWallOrActvityDetailDate.setText(IN_WALL_DETAILS.get(0).get(DATE));
		androidQuery.id(imgWallOrActvityDetailUserAvatar).image(IN_WALL_DETAILS.get(0).get(USER_AVATAR), true, true, getDeviceWidth(), 0);
		txtWallOrActvityDetailTitle.setMovementMethod(LinkMovementMethod.getInstance());
		txtWallOrActvityDetailTitle.setText(
				addClickablePart(Html.fromHtml(getPlainText(IN_WALL_DETAILS.get(0).get(TITLETAG)).replace("\u25ba", "\u25b6")), IN_WALL_DETAILS.get(0),
						IN_WALL_DETAILS.get(0).get(TYPE), IN_USERID), BufferType.SPANNABLE);

		if (IN_WALL_DETAILS.get(0).get(CONTENT).trim().length() > 0) {
			txtWallOrActvityDetailContent.setVisibility(View.VISIBLE);
			txtWallOrActvityDetailContent.setMovementMethod(LinkMovementMethod.getInstance());
			IN_WALL_DETAILS.get(0).put(CONTENT, IN_WALL_DETAILS.get(0).get(CONTENT).replace("\n", " "));
			IN_WALL_DETAILS.get(0).put(CONTENT, IN_WALL_DETAILS.get(0).get(CONTENT).replace("\t", " "));
			IN_WALL_DETAILS.get(0).put(CONTENT, IN_WALL_DETAILS.get(0).get(CONTENT).replace("\r", " "));

			txtWallOrActvityDetailContent.setText(
					addClickablePart(Html.fromHtml(getPlainText(IN_WALL_DETAILS.get(0).get(CONTENT))), IN_WALL_DETAILS.get(0), IN_WALL_DETAILS.get(0).get(TYPE), IN_USERID),
					BufferType.SPANNABLE);
		}

		if (IN_WALL_DETAILS.get(0).get(TYPE).toString().trim().equals(COVERUPLOAD) || IN_WALL_DETAILS.get(0).get(TYPE).toString().trim().equals(AVATARUPLOAD)
				|| IN_WALL_DETAILS.get(0).get(TYPE).toString().trim().equals(GROUPSAVATARUPLOAD)|| IN_WALL_DETAILS.get(0).get(TYPE).toString().trim().equals(EVENTSAVATARUPLOAD)) {
			lnrWallOrActivityContentCoverPhoto.setVisibility(View.VISIBLE);
			try{
				JSONObject imageData = new JSONObject(IN_WALL_DETAILS.get(0).get(IMAGE_DATA));
				androidQuery.id(imgWallOrActvityCoverPhoto).progress(R.id.coverimgprogress).image(imageData.getString(URL),true,true);
			}catch (JSONException e){
				e.printStackTrace();
			}
		}else if (IN_WALL_DETAILS.get(0).get(TYPE).toString().trim().equals(PHOTOS)) {
			lnrWallOrActivityDetailContentImage.setVisibility(View.VISIBLE);
			preparePhotoList(imgWallOrActvityDetailUploadedPhotos,lnrContentImageScrollable, IN_WALL_DETAILS.get(0));
		}else if (IN_WALL_DETAILS.get(0).get(TYPE).toString().trim().equals(VIDEOS)) {
			lnrWallOrActivityDetailContentVideo.setVisibility(View.VISIBLE);
			try {
				androidQuery.id(imgWallOrActvityDetailContentVideoImage).image(new JSONObject(IN_WALL_DETAILS.get(0).get(CONTENT_DATA)).getString(THUMB), true, true,
						getDeviceWidth(), 0);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else if (IN_WALL_DETAILS.get(0).get(TYPE).toString().trim().equals(EVENT)) {
			try{
				lnrWallOrActivityDetailContentView.setVisibility(View.VISIBLE);
				lnrWallOrActivityDetailContentView.removeAllViews();

				JSONObject eventData = new JSONObject(IN_WALL_DETAILS.get(0).get(CONTENT_DATA));

				LayoutInflater inflater = LayoutInflater.from(JomWallOrActivityDetailActivity.this);
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
								loadNew(JomEventDetailsActivity_v30.class, JomWallOrActivityDetailActivity.this, false, "IN_USERID", IN_USERID, "IN_EVENT_DETAILS", jsonToMap(jsonObject),
										"IN_GROUP_ID", IN_GROUP_ID);
							} catch (Throwable e) {
								e.printStackTrace();
							}
						} else {
							try {
								loadNew(JomEventDetailsActivity.class, JomWallOrActivityDetailActivity.this, false, "IN_USERID", IN_USERID, "IN_EVENT_DETAILS", jsonToMap(jsonObject),
										"IN_GROUP_ID", IN_GROUP_ID);
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					}
				});
				lnrWallOrActivityDetailContentView.addView(eventView);
			}catch (JSONException e){
				e.printStackTrace();
			}
		}else if (IN_WALL_DETAILS.get(0).get(TYPE).toString().trim().equals(GROUP)) {
			try{
				lnrWallOrActivityDetailContentView.setVisibility(View.VISIBLE);
				lnrWallOrActivityDetailContentView.removeAllViews();

				JSONObject eventData = new JSONObject(IN_WALL_DETAILS.get(0).get(CONTENT_DATA));

				LayoutInflater inflater = LayoutInflater.from(JomWallOrActivityDetailActivity.this);
				View groupView = inflater.inflate(R.layout.jom_wall_activity_group_details_item, null);
				groupView.setTag(eventData);

				androidQuery.id(((ImageView) groupView.findViewById(R.id.imgGroupAvatar))).image(eventData.getString(AVATAR),true,true);
				((IjoomerTextView) groupView.findViewById(R.id.txtGroupTitle)).setText(eventData.getString(TITLE));
				((IjoomerTextView) groupView.findViewById(R.id.txtGroupDescription)).setText(eventData.getString(DESCRIPTION));

				groupView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						JSONObject jsonObject = (JSONObject) v.getTag();
						if (IjoomerGlobalConfiguration.getJomsocialVersion().equals(IjoomerGlobalConfiguration.JOMVERSION_V30)) {
							try {
								loadNew(JomGroupDetailsActivity_v30.class, JomWallOrActivityDetailActivity.this, false, "IN_USERID", IN_USERID, "IN_GROUP_DETAILS", jsonToMap(jsonObject));
							} catch (Throwable e) {
								e.printStackTrace();
							}
						} else {

							try {
								loadNew(JomGroupDetailsActivity.class, JomWallOrActivityDetailActivity.this, false, "IN_USERID", IN_USERID, "IN_GROUP_DETAILS", jsonToMap(jsonObject));
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					}
				});
				lnrWallOrActivityDetailContentView.addView(groupView);
			}catch (JSONException e){
				e.printStackTrace();
			}
		}else if (IN_WALL_DETAILS.get(0).get(TYPE).toString().trim().equals(DISCUSSION)) {
			try{
				lnrWallOrActivityDetailContentView.setVisibility(View.VISIBLE);
				lnrWallOrActivityDetailContentView.removeAllViews();

				JSONObject discussionData = new JSONObject(IN_WALL_DETAILS.get(0).get(CONTENT_DATA));

				LayoutInflater inflater = LayoutInflater.from(JomWallOrActivityDetailActivity.this);
				View discussionView = inflater.inflate(R.layout.jom_wall_activity_discussion_details_item, null);
				discussionView.setTag(IN_WALL_DETAILS.get(0));

				((IjoomerTextView) discussionView.findViewById(R.id.txtDiscussionTitle)).setText(discussionData.getString(TITLE));
				((IjoomerTextView) discussionView.findViewById(R.id.txtDiscussionDiscription)).setText(discussionData.getString(MESSAGE));

				discussionView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							HashMap<String, String> row = (HashMap<String, String>) v.getTag();
							final JSONObject jsonObject = new JSONObject(row.get(GROUP_DATA));
							final JSONObject jsonObjectDiscussion = new JSONObject(row.get(CONTENT_DATA));
							try {
								loadNew(JomGroupDiscussionDetailsActivity.class, JomWallOrActivityDetailActivity.this, false, "IN_DISCUSSION_DETAILS", jsonToMap(jsonObjectDiscussion),
										"IN_GROUP_DETAILS", jsonToMap(jsonObject));
							} catch (Throwable e) {
								e.printStackTrace();
							}
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				});
				lnrWallOrActivityDetailContentView.addView(discussionView);
			}catch (JSONException e){
				e.printStackTrace();
			}
		}else if (IN_WALL_DETAILS.get(0).get(TYPE).toString().trim().equals(ANNOUNCEMENT)) {
			try{
				lnrWallOrActivityDetailContentView.setVisibility(View.VISIBLE);
				lnrWallOrActivityDetailContentView.removeAllViews();

				JSONObject annoucementData = new JSONObject(IN_WALL_DETAILS.get(0).get(CONTENT_DATA));

				LayoutInflater inflater = LayoutInflater.from(JomWallOrActivityDetailActivity.this);
				View annoucementView = inflater.inflate(R.layout.jom_wall_activity_annoucement_details_item, null);
				annoucementView.setTag(IN_WALL_DETAILS.get(0));

				((IjoomerTextView) annoucementView.findViewById(R.id.txtAnnouncementTitle)).setText(annoucementData.getString(TITLE));
				((IjoomerTextView) annoucementView.findViewById(R.id.txtAnnouncementDiscription)).setText(annoucementData.getString(MESSAGE));

				annoucementView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							HashMap<String, String> row = (HashMap<String, String>) v.getTag();
							final JSONObject jsonObject = new JSONObject(row.get(GROUP_DATA));
							final JSONObject jsonObjectAnnouncement = new JSONObject(row.get(CONTENT_DATA));
							try {
								loadNew(JomGroupAnnouncementDetailsActivity.class, JomWallOrActivityDetailActivity.this, false, "IN_ANNOUCEMENT_DETAILS", jsonToMap(jsonObjectAnnouncement),
										"IN_GROUP_DETAILS", jsonToMap(jsonObject));
							} catch (Throwable e) {
								e.printStackTrace();
							}
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				});
				lnrWallOrActivityDetailContentView.addView(annoucementView);
			}catch (JSONException e){
				e.printStackTrace();
			}
		}

		if (IN_WALL_DETAILS.get(0).get(COMMENTALLOWED).equals("0")) {
			lnrWallOrActivityDetailsWriteComment.setVisibility(View.GONE);
		}
		if (IN_WALL_DETAILS_LIST_TYPE.equals(COMMENTS)) {
			txtWallOrActvityDetailCommentCount.setVisibility(View.VISIBLE);
			txtWallOrActvityDetailCommentCount.setText(String.format(getString(R.string.comment_count), Integer.parseInt(IN_WALL_DETAILS.get(0).get(COMMENTCOUNT))));
		} else {
			lnrWallOrActivityDetailsWriteComment.setVisibility(View.GONE);
			txtWallOrActvityDetailLikeCount.setVisibility(View.VISIBLE);
			if (IN_WALL_DETAILS.get(0).get(LIKEALLOWED).equals("0")) {
				txtWallOrActvityDetailLikeCount.setClickable(false);
			}
			txtWallOrActvityDetailLikeCount.setText(String.format(getString(R.string.like_count), Integer.parseInt(IN_WALL_DETAILS.get(0).get(LIKECOUNT))));
		}

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
		IjoomerUtilities.getCustomOkDialog(getString(R.string.wall_details), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())),
				getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

			@Override
			public void NeutralMethod() {

			}
		});
	}

	/**
	 * This method used to get wall comment list.
	 */
	private void getCommentList() {
		wallDataProvider.restorePagingSettings();
		listFooterVisible();
		wallDataProvider.getCommentList(IN_WALL_DETAILS.get(0).get(ID), new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				listFooterInvisible();
				if (responseCode == 200) {
					updateHeader(wallDataProvider.getNotificationData());
					prepareList(data1, false);
					listAdapterWithHolder = getListAdapter();
					listView.setAdapter(listAdapterWithHolder);
					listView.setSelection(0);
				} else {
					listView.setAdapter(null);
					if (responseCode != 204) {
						responseErrorMessageHandler(responseCode, false);
					}
				}
			}
		});
	}

	/**
	 * This method used to get wall like list.
	 */
	private void getLikeList() {
		wallDataProvider.restorePagingSettings();
		wallDataProvider.getLikeList(IN_WALL_DETAILS.get(0).get(ID), new WebCallListener() {
			final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

			@Override
			public void onProgressUpdate(int progressCount) {
				proSeekBar.setProgress(progressCount);
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				if (responseCode == 200) {
					updateHeader(wallDataProvider.getNotificationData());
					prepareList(data1, false);
					listAdapterWithHolder = getListAdapter();
					listView.setAdapter(listAdapterWithHolder);
					listView.setSelection(0);
				} else {
					listView.setAdapter(null);
					responseErrorMessageHandler(responseCode, false);
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
							loadNew(JomPhotoDetailsActivity.class, JomWallOrActivityDetailActivity.this, false, 
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
								loadNew(JomPhotoDetailsActivity.class, JomWallOrActivityDetailActivity.this, false, 
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
								loadNew(JomPhotoDetailsActivity.class, JomWallOrActivityDetailActivity.this, false, "IN_PHOTO_LIST", photoData, "IN_SELECTED_INDEX",
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
	 * This method used to prepare list wall comment.
	 * 
	 * @param data
	 *            represented wall comment data
	 * @param append
	 *            represented data append
	 */
	public void prepareList(ArrayList<HashMap<String, String>> data, boolean append) {
		if (data != null) {
			if (!append) {
				listData.clear();
			}
			for (HashMap<String, String> hashMap : data) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.jom_comment_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hashMap);
				item.setValues(obj);
				if (append) {
					listAdapterWithHolder.add(item);
				} else {
					listData.add(item);
				}
			}

		}
	}

	/**
	 * List adapter for wall comment.
	 */
	private SmartListAdapterWithHolder getListAdapter() {
		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(JomWallOrActivityDetailActivity.this, R.layout.jom_comment_list_item, listData,
				new ItemView() {
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

				if (IN_WALL_DETAILS_LIST_TYPE.equals(COMMENTS)) {
					androidQuery.id(holder.imgCommentUserAvatar).image(row.get(USER_AVATAR), true, true, getDeviceWidth(), 0);
					holder.txtCommentTitle.setText(getPlainText(row.get(COMMENT_TEXT)));
					holder.txtCommentUserName.setText(row.get(USER_NAME));
					holder.txtCommentDate.setText(row.get(DATE));
					if (row.containsKey(DELETEALLOWED) && row.get(DELETEALLOWED).equals("1")) {
						holder.btnCommentRemove.setVisibility(View.VISIBLE);
					}

					if (getAudio(row.get(COMMENT_TEXT)) != null) {

						holder.btnPlayStopVoice.setVisibility(View.VISIBLE);
						holder.btnPlayStopVoice.setText(getAudioLength(row.get(COMMENT_TEXT)));
						holder.btnPlayStopVoice.setAudioPath(getAudio(row.get(COMMENT_TEXT)), false);
						holder.btnPlayStopVoice.setAudioListener(new AudioListener() {

							@Override
							public void onReportClicked() {
								reportVoice(getAudio(row.get(COMMENT_TEXT)));
							}

							@Override
							public void onPrepared() {
							}

							@Override
							public void onPlayClicked(boolean isplaying) {
							}

							@Override
							public void onComplete() {
							}
						});

					}

					holder.btnCommentRemove.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							IjoomerUtilities.getCustomConfirmDialog(getString(R.string.wall_remove), getString(R.string.are_you_sure), getString(R.string.yes),
									getString(R.string.no), new CustomAlertMagnatic() {

								@Override
								public void PositiveMethod() {
									wallDataProvider.removeComment(row.get(COMMENT_ID), new WebCallListener() {
										final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

										@Override
										public void onProgressUpdate(int progressCount) {
											proSeekBar.setProgress(progressCount);
										}

										@Override
										public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
											if (responseCode == 200) {
												IjoomerApplicationConfiguration.setReloadRequired(true);
												updateHeader(wallDataProvider.getNotificationData());
												listAdapterWithHolder.remove(listAdapterWithHolder.getItem(position));
												IN_WALL_DETAILS.get(0).put(COMMENTCOUNT,
														String.valueOf(Integer.parseInt(IN_WALL_DETAILS.get(0).get(COMMENTCOUNT)) - 1));
												txtWallOrActvityDetailCommentCount.setText(String.format(getString(R.string.comment_count),
														Integer.parseInt(IN_WALL_DETAILS.get(0).get(COMMENTCOUNT))));
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
				} else {
					holder.txtCommentUserName.setText(row.get(USER_NAME));
					holder.txtCommentTitle.setVisibility(View.GONE);
					holder.txtCommentDate.setVisibility(View.GONE);
					holder.imgCommentUserAvatar.setVisibility(View.GONE);
				}

				holder.txtCommentUserName.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (row.get(USER_PROFILE).equals("1")) {
							gotoProfile(row.get(USER_ID));
						}
					}
				});
				holder.imgCommentUserAvatar.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
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
