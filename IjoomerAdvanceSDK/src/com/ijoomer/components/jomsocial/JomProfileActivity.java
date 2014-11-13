package com.ijoomer.components.jomsocial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.provider.MediaStore;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView.BufferType;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.custom.interfaces.SelectImageDialogListner;
import com.ijoomer.customviews.IjoomerAudioPlayer.AudioListener;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerRadioButton;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.customviews.IjoomerVoiceAndTextMessager;
import com.ijoomer.customviews.IjoomerVoiceAndTextMessager.MessageHandler;
import com.ijoomer.customviews.IjoomerVoiceButton;
import com.ijoomer.library.jomsocial.JomEventDataProvider;
import com.ijoomer.library.jomsocial.JomGalleryDataProvider;
import com.ijoomer.library.jomsocial.JomProfileDataProvider;
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
 * This Class Contains All Method Related To JomProfileActivity.
 *
 * @author tasol
 *
 */
public class JomProfileActivity extends JomMasterActivity {
	private LinearLayout lnrUserStaus;
	private LinearLayout lnrWritePost;
	private ListView lstProfileDetail;
	private FrameLayout framEditImage;
	private FrameLayout framUserImage;
	private IjoomerTextView txtLike;
	private IjoomerTextView txtUnlike;
	private IjoomerTextView txtUserName;
	private IjoomerTextView txtUserVideo;
	private IjoomerTextView txtUserStatus;
	private IjoomerTextView txtRecentActivities;
	private IjoomerRadioButton txtUploadPhoto;
	private IjoomerRadioButton txtUploadVideo;
	private IjoomerRadioButton txtCreateEvent;
	private IjoomerRadioButton txtWritePost;
	private IjoomerTextView txtViewers;
	private IjoomerTextView txtFriend;
	private IjoomerTextView txtPhoto;
	private IjoomerTextView txtVideo;
	private IjoomerEditText editUserName;
	private ImageView imgUserImage;
	private ImageView imgEditPicture;
	private ImageView imgAddEditCover;
	private ImageView imgAddEditCoverPhoto;
	private ImageView imgUploadPhoto;
	private ImageView imgAbout;
	private ImageView imgFriend;
	private ImageView imgPhoto;
	private ImageView imgVideo;
	private ImageView imgMap;
	private IjoomerButton btnAddFriend;
	private IjoomerButton btnMessage;
	private IjoomerButton btnEdit;
	private IjoomerVoiceButton btnPlayStopVoice;
	private IjoomerVoiceAndTextMessager voiceMessager;
	private Spinner spnPostType;
	private View listHeader;
	private View listFooter;
	private SeekBar proSeekBar;

	private AQuery androidQuery;
	private ArrayList<HashMap<String, String>> PROFILE = new ArrayList<HashMap<String, String>>();
	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder listAdapterWithHolder;

	private JomProfileDataProvider provider;
	private JomWallDataProvider wallDataProvider;

	final private int PICK_IMAGE_USER_AVATAR = 1;
	final private int CAPTURE_IMAGE_USER_AVATAR = 2;
	final private int PICK_IMAGE_UPLOAD_PHOTO = 3;
	final private int CAPTURE_IMAGE_UPLOAD_PHOTO = 4;
	private String selectedImagePathUserAvatar;
	private String selectedImagePathPhotoUpload;
	private String IN_USERID;
	private boolean isSetProfileCoverPage = false;

	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.jom_profile_detail;
	}

	@Override
	public void initComponents() {
		getIntentData();
		
		androidQuery = new AQuery(this);
		provider = new JomProfileDataProvider(this);
		wallDataProvider = new JomWallDataProvider(this);

		lstProfileDetail = (ListView) findViewById(R.id.lstProfileDetail);
		listHeader = LayoutInflater.from(this).inflate(R.layout.jom_profile_header, null);
		listFooter = LayoutInflater.from(this).inflate(R.layout.ijoomer_list_footer, null);
		lstProfileDetail.addHeaderView(listHeader, null, false);
		lstProfileDetail.addFooterView(listFooter, null, false);
		lstProfileDetail.setAdapter(null);
		lnrWritePost = (LinearLayout) listHeader.findViewById(R.id.lnrWritePost);
		lnrUserStaus = (LinearLayout) listHeader.findViewById(R.id.lnrUserStaus);
		framEditImage = (FrameLayout) listHeader.findViewById(R.id.framEditImage);
		framUserImage = (FrameLayout) listHeader.findViewById(R.id.framUserImage);
		txtWritePost = (IjoomerRadioButton) listHeader.findViewById(R.id.txtWritePost);
		txtUserName = (IjoomerTextView) listHeader.findViewById(R.id.txtUserName);
		txtUserVideo = (IjoomerTextView) listHeader.findViewById(R.id.txtUserVideo);
		txtUserStatus = (IjoomerTextView) listHeader.findViewById(R.id.txtUserStatus);
		txtRecentActivities = (IjoomerTextView) listHeader.findViewById(R.id.txtRecentActivities);
		txtUploadPhoto = (IjoomerRadioButton) listHeader.findViewById(R.id.txtUploadPhoto);
		txtUploadVideo = (IjoomerRadioButton) listHeader.findViewById(R.id.txtUploadVideo);
		txtCreateEvent = (IjoomerRadioButton) listHeader.findViewById(R.id.txtCreateEvent);
		txtLike = (IjoomerTextView) listHeader.findViewById(R.id.txtLike);
		txtUnlike = (IjoomerTextView) listHeader.findViewById(R.id.txtUnlike);
		txtViewers = (IjoomerTextView) listHeader.findViewById(R.id.txtViewers);
		txtFriend = (IjoomerTextView) listHeader.findViewById(R.id.txtFriend);
		txtPhoto = (IjoomerTextView) listHeader.findViewById(R.id.txtPhoto);
		txtVideo = (IjoomerTextView) listHeader.findViewById(R.id.txtVideo);
		editUserName = (IjoomerEditText) listHeader.findViewById(R.id.editUserName);
		imgUploadPhoto = (ImageView) listHeader.findViewById(R.id.imgUploadPhoto);
		imgAddEditCover = (ImageView) listHeader.findViewById(R.id.imgAddEditCover);
		imgAddEditCoverPhoto = (ImageView) listHeader.findViewById(R.id.imgAddEditCoverPhoto);
		imgUserImage = (ImageView) listHeader.findViewById(R.id.imgUserImage);
		imgEditPicture = (ImageView) listHeader.findViewById(R.id.imgEditPicture);
		imgAbout = (ImageView) listHeader.findViewById(R.id.imgAbout);
		imgFriend = (ImageView) listHeader.findViewById(R.id.imgFriend);
		imgPhoto = (ImageView) listHeader.findViewById(R.id.imgPhoto);
		imgVideo = (ImageView) listHeader.findViewById(R.id.imgVideo);
		imgMap = (ImageView) listHeader.findViewById(R.id.imgMap);
		btnEdit = (IjoomerButton) listHeader.findViewById(R.id.btnEdit);
		btnAddFriend = (IjoomerButton) listHeader.findViewById(R.id.btnAddFriend);
		btnMessage = (IjoomerButton) listHeader.findViewById(R.id.btnMessage);
		spnPostType = (Spinner) listHeader.findViewById(R.id.spnPostType);
		btnPlayStopVoice = (IjoomerVoiceButton) listHeader.findViewById(R.id.btnPlayStopVoice);
		btnPlayStopVoice.setReportVoice(false);
		voiceMessager = (IjoomerVoiceAndTextMessager) listHeader.findViewById(R.id.voiceMessager);
		voiceMessager.setIsSmileyAllow(false);
	}

	@Override
	public void prepareViews() {
		try {
			if ((IN_USERID.equals("0")) && !provider.getNotificationData().getString(MESSAGENOTIFICATION).equals("0")) {
				btnMessage.setText(getString(R.string.message) + "  ( " + provider.getNotificationData().getString(MESSAGENOTIFICATION) + " )");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		getProfile();
		getWall();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isSetProfileCoverPage) {
			isSetProfileCoverPage = false;
			getProfile();
		}
		if (IjoomerApplicationConfiguration.isReloadRequired()) {
			IjoomerApplicationConfiguration.setReloadRequired(false);
			getProfile();
			getWall();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == PICK_IMAGE_USER_AVATAR) {
				selectedImagePathUserAvatar = getAbsolutePath(data.getData());
				imgUserImage.setImageBitmap(decodeFile(selectedImagePathUserAvatar));
			} else if (requestCode == CAPTURE_IMAGE_USER_AVATAR) {
				selectedImagePathUserAvatar = getImagePath();
				imgUserImage.setImageBitmap(decodeFile(selectedImagePathUserAvatar));
			} else if (requestCode == PICK_IMAGE_UPLOAD_PHOTO) {
				selectedImagePathPhotoUpload = getAbsolutePath(data.getData());
				imgUploadPhoto.setVisibility(View.VISIBLE);
				imgUploadPhoto.setImageBitmap(decodeFile(selectedImagePathPhotoUpload));
			} else if (requestCode == CAPTURE_IMAGE_UPLOAD_PHOTO) {
				imgUploadPhoto.setVisibility(View.VISIBLE);
				selectedImagePathPhotoUpload = getImagePath();
				imgUploadPhoto.setImageBitmap(decodeFile(selectedImagePathPhotoUpload));
			} else {
				super.onActivityResult(requestCode, resultCode, data);
			}
		}

	}

	@Override
	public void setActionListeners() {

		//        lstProfileDetail.setOnRefreshListener(new IjoomerPullToRefreshListView.OnRefreshListener() {
		//            @Override
		//            public void onRefresh() {
		//                getProfile();
		//                getWall();
		//            }
		//        });

		txtUserStatus.setMovementMethod(new ScrollingMovementMethod());
		txtUserVideo.setMovementMethod(new ScrollingMovementMethod());

		voiceMessager.setMessageHandler(new MessageHandler() {

			@Override
			public void onVoiceMessageRecordingComplete(final String message, String voiceMessagePath) {
				hideSoftKeyboard();
				if (imgUploadPhoto.getVisibility() == View.VISIBLE && selectedImagePathPhotoUpload != null && selectedImagePathPhotoUpload.length() > 0) {
					startPhotoUpload(selectedImagePathPhotoUpload, message, voiceMessagePath);
				} else {
					final JomWallDataProvider provider = new JomWallDataProvider(JomProfileActivity.this);
					provider.addOrPostOnWall(IN_USERID, message, getPrivacyCode(spnPostType.getSelectedItem().toString()), voiceMessagePath, new WebCallListener() {
						final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

						@Override
						public void onProgressUpdate(int progressCount) {
							proSeekBar.setProgress(progressCount);
						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								updateHeader(provider.getNotificationData());
								if (IN_USERID.equals("0")) {
									try {
										PROFILE.get(0).put(USER_STATUS, ((JSONObject) data2).getString("voice"));
									} catch (JSONException e) {
										e.printStackTrace();
									}
									if (getPlainText(PROFILE.get(0).get(USER_STATUS)).length() > 0) {
										txtUserStatus.setText(getPlainText(PROFILE.get(0).get(USER_STATUS)));
									} else {
										txtUserStatus.setVisibility(View.GONE);
									}

									if (getAudio(PROFILE.get(0).get(USER_STATUS)) != null) {

										btnPlayStopVoice.setVisibility(View.VISIBLE);
										btnPlayStopVoice.setText(getAudioLength(PROFILE.get(0).get(USER_STATUS)));
										btnPlayStopVoice.setAudioPath(getAudio(PROFILE.get(0).get(USER_STATUS)), false);
										btnPlayStopVoice.setAudioListener(new AudioListener() {

											@Override
											public void onReportClicked() {
												reportVoice(getAudio(PROFILE.get(0).get(USER_STATUS)));
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
								}
								getWall();
							} else {
								responseErrorMessageHandler(responseCode, false);
							}

						}
					});
				}
			}

			@Override
			public void onButtonSend(final String message) {
				hideSoftKeyboard();
				if (imgUploadPhoto.getVisibility() == View.VISIBLE && selectedImagePathPhotoUpload != null && selectedImagePathPhotoUpload.length() > 0) {
					startPhotoUpload(selectedImagePathPhotoUpload, message, null);
				} else {
					final JomWallDataProvider provider = new JomWallDataProvider(JomProfileActivity.this);
					provider.addOrPostOnWall(IN_USERID, message, getPrivacyCode(spnPostType.getSelectedItem().toString()), null, new WebCallListener() {
						final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

						@Override
						public void onProgressUpdate(int progressCount) {
							proSeekBar.setProgress(progressCount);
						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								updateHeader(provider.getNotificationData());
								if (IN_USERID.equals("0")) {
									PROFILE.get(0).put(USER_STATUS, message);
									if (getPlainText(PROFILE.get(0).get(USER_STATUS)).length() > 0) {
										txtUserStatus.setVisibility(View.VISIBLE);
										txtUserStatus.setText(getPlainText(PROFILE.get(0).get(USER_STATUS)));
									} else {
										txtUserStatus.setVisibility(View.GONE);
									}
									btnPlayStopVoice.setVisibility(View.GONE);
								}
								getWall();
							} else {
								responseErrorMessageHandler(responseCode, false);
							}

						}
					});
				}
			}

			@Override
			public void onToggle(int messager) {

			}
		});

		imgAddEditCoverPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				isSetProfileCoverPage = true;
				try {
					loadNew(JomAlbumsActivity.class, JomProfileActivity.this, false, "IN_USERID", IN_USERID, "IN_PROFILE_COVER", "1");
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});

		txtWritePost.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imgUploadPhoto.setVisibility(View.GONE);

				voiceMessager.setMessageHint(R.string.what_is_on_your_mind);
				voiceMessager.setCanBlank(false);
				if (voiceMessager.getCurrentMessager() == IjoomerVoiceAndTextMessager.TEXT) {
					voiceMessager.toggelMessager();
				}
			}
		});

		txtCreateEvent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final JomEventDataProvider eventprovider = new JomEventDataProvider(JomProfileActivity.this);

				eventprovider.addOrEditEventFieldList("0", "0", new WebCallListener() {
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
								loadNew(JomEventCreateActivity.class, JomProfileActivity.this, false, "IN_FIELD_LIST", data1, "IN_EVENT_ID", "0", "IN_GROUP_ID", "0");
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
		txtUploadVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					loadNew(JomVideoActivity.class, JomProfileActivity.this, false, "IN_USERID", IN_USERID, "IN_PROFILE", "1");
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});

		txtUploadPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				voiceMessager.setCanBlank(true);
				if (voiceMessager.getCurrentMessager() == IjoomerVoiceAndTextMessager.TEXT) {
					voiceMessager.toggelMessager();
				}
				voiceMessager.setMessageHint(R.string.say_about_photo);
				IjoomerUtilities.selectImageDialog(new SelectImageDialogListner() {

					@Override
					public void onPhoneGallery() {
						Intent intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						startActivityForResult(Intent.createChooser(intent, ""), PICK_IMAGE_UPLOAD_PHOTO);
					}

					@Override
					public void onCapture() {
						final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
						startActivityForResult(intent, CAPTURE_IMAGE_UPLOAD_PHOTO);
					}
				});
			}
		});

		txtUserVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				try {
					String url = new JSONObject(PROFILE.get(0).get(PROFILE_VIDEO)).getString(URL);
					Intent lVideoIntent = new Intent(null,getVideoPlayURI(url), JomProfileActivity.this, IjoomerMediaPlayer.class);
					startActivity(lVideoIntent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		lstProfileDetail.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 2) {
					if (!wallDataProvider.isCalling() && wallDataProvider.hasNextPage()) {
						listFooterVisible();
						wallDataProvider.getWallList(IN_USERID, "wall", new WebCallListenerWithCacheInfo() {

							@Override
							public void onProgressUpdate(int progressCount) {
							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2, int pageNo, int pageLimit,
									boolean fromCache) {
								listFooterInvisible();
								if (responseCode == 200) {
									if(!fromCache){
										updateHeader(wallDataProvider.getNotificationData());
									}
									prepareList(data1, true, fromCache, pageNo, pageLimit);
								} else {
									responseErrorMessageHandler(responseCode, false);
								}
							}
						});
					}
				}
			}
		});
		txtLike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (PROFILE.get(0).containsKey(LIKED) && PROFILE.get(0).get(LIKED).equals("1")) {
					txtLike.setClickable(false);
					provider.unlikeUserProfile(IN_USERID, new WebCallListener() {
						final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

						@Override
						public void onProgressUpdate(int progressCount) {
							proSeekBar.setProgress(progressCount);

						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								updateHeader(provider.getNotificationData());
								PROFILE.get(0).put(LIKED, "0");
								PROFILE.get(0).put(LIKES, String.valueOf(Integer.parseInt(PROFILE.get(0).get(LIKES)) - 1));
								setLikeDislike();
							} else {
								responseErrorMessageHandler(responseCode, false);
							}
							txtLike.setClickable(true);

						}
					});

				} else {
					txtLike.setClickable(false);
					provider.likeUserProfile(IN_USERID, new WebCallListener() {
						final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

						@Override
						public void onProgressUpdate(int progressCount) {
							proSeekBar.setProgress(progressCount);

						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								updateHeader(provider.getNotificationData());
								PROFILE.get(0).put(LIKED, "1");
								if (PROFILE.get(0).containsKey(DISLIKED) && PROFILE.get(0).get(DISLIKED).equals("1")) {
									PROFILE.get(0).put(DISLIKES, String.valueOf(Integer.parseInt(PROFILE.get(0).get(DISLIKES)) - 1));
									PROFILE.get(0).put(DISLIKED, "0");
								}
								PROFILE.get(0).put(LIKES, String.valueOf(Integer.parseInt(PROFILE.get(0).get(LIKES)) + 1));
								setLikeDislike();
							} else {
								responseErrorMessageHandler(responseCode, false);
							}
							txtLike.setClickable(true);
						}
					});
				}
			}
		});

		txtUnlike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (PROFILE.get(0).containsKey(DISLIKED) && PROFILE.get(0).get(DISLIKED).equals("1")) {
					txtUnlike.setClickable(false);
					provider.unlikeUserProfile(IN_USERID, new WebCallListener() {
						final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

						@Override
						public void onProgressUpdate(int progressCount) {
							proSeekBar.setProgress(progressCount);

						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								updateHeader(provider.getNotificationData());
								PROFILE.get(0).put(DISLIKED, "0");
								PROFILE.get(0).put(DISLIKES, String.valueOf(Integer.parseInt(PROFILE.get(0).get(DISLIKES)) - 1));
								setLikeDislike();
							} else {
								responseErrorMessageHandler(responseCode, false);
							}
							txtUnlike.setClickable(true);
						}
					});

				} else {
					txtUnlike.setClickable(false);
					provider.dislikeUserProfile(IN_USERID, new WebCallListener() {
						final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

						@Override
						public void onProgressUpdate(int progressCount) {
							proSeekBar.setProgress(progressCount);

						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								updateHeader(provider.getNotificationData());
								PROFILE.get(0).put(DISLIKED, "1");
								if (PROFILE.get(0).containsKey(LIKED) && PROFILE.get(0).get(LIKED).equals("1")) {
									PROFILE.get(0).put(LIKES, String.valueOf(Integer.parseInt(PROFILE.get(0).get(LIKES)) - 1));
									PROFILE.get(0).put(LIKED, "0");
								}
								PROFILE.get(0).put(DISLIKES, String.valueOf(Integer.parseInt(PROFILE.get(0).get(DISLIKES)) + 1));
								setLikeDislike();
							} else {
								responseErrorMessageHandler(responseCode, false);
							}
							txtUnlike.setClickable(true);
						}
					});
				}

			}
		});

		imgEditPicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				IjoomerUtilities.selectImageDialog(new SelectImageDialogListner() {

					@Override
					public void onPhoneGallery() {
						Intent intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						startActivityForResult(Intent.createChooser(intent, ""), PICK_IMAGE_USER_AVATAR);
					}

					@Override
					public void onCapture() {
						final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
						startActivityForResult(intent, CAPTURE_IMAGE_USER_AVATAR);
					}
				});
			}
		});

		btnEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				if (((IjoomerButton) v).getText().toString().equalsIgnoreCase(getString(R.string.save))) {
					hideSoftKeyboard();

					if (editUserName.getText().toString().trim().length() <= 0) {
						editUserName.setError(getString(R.string.validation_value_required));
					} else {
						if (selectedImagePathUserAvatar != null || !editUserName.getText().toString().trim().equals(txtUserName.getText().toString().trim())) {

							provider.updateUserProfile(selectedImagePathUserAvatar, editUserName.getText().toString().trim().equals(txtUserName.getText().toString().trim()) ? null
									: editUserName.getText().toString().trim(), new WebCallListener() {
								final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

								@Override
								public void onProgressUpdate(int progressCount) {
									proSeekBar.setProgress(progressCount);
								}

								@Override
								public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
									if (responseCode == 200) {
										updateHeader(provider.getNotificationData());
										selectedImagePathUserAvatar = null;
										txtUserName.setText(editUserName.getText());
									} else {
										responseErrorMessageHandler(responseCode, false);
									}

									((IjoomerButton) v).setText(getString(R.string.edit));
									framEditImage.setVisibility(View.GONE);
									txtUserName.setVisibility(View.VISIBLE);
									editUserName.setVisibility(View.GONE);
								}
							});
						} else {
							((IjoomerButton) v).setText(getString(R.string.edit));
							framEditImage.setVisibility(View.GONE);
							txtUserName.setVisibility(View.VISIBLE);
							editUserName.setVisibility(View.GONE);
						}
					}
				} else {
					((IjoomerButton) v).setText(getString(R.string.save));

					framEditImage.setVisibility(View.VISIBLE);
					txtUserName.setVisibility(View.GONE);
					editUserName.setVisibility(View.VISIBLE);
					editUserName.setText(txtUserName.getText());
					editUserName.requestFocus();
				}
			}
		});

		btnAddFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (PROFILE.get(0).get(ISFRIEND).equalsIgnoreCase("1")) {
					IjoomerUtilities.getCustomConfirmDialog(getString(R.string.friend_title_remove), getString(R.string.are_you_sure), getString(R.string.yes),
							getString(R.string.no), new CustomAlertMagnatic() {

						@Override
						public void PositiveMethod() {
							provider.removeFriend(IN_USERID, new WebCallListener() {
								final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

								@Override
								public void onProgressUpdate(int progressCount) {
									proSeekBar.setProgress(progressCount);
								}

								@Override
								public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {

									if (responseCode == 200) {
										IjoomerApplicationConfiguration.setReloadRequired(true);
										updateHeader(provider.getNotificationData());
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
				} else if (PROFILE.get(0).containsKey(ISFRIENDREQBY) && PROFILE.get(0).get(ISFRIENDREQBY).equals("1")) {
					IjoomerUtilities.getCustomOkDialog(getString(R.string.add_friend), getString(R.string.friend_request_by), getString(R.string.ok), R.layout.ijoomer_ok_dialog,
							new CustomAlertNeutral() {

						@Override
						public void NeutralMethod() {

						}
					});
				} else if (PROFILE.get(0).containsKey(ISFRIENDREQTO) && PROFILE.get(0).get(ISFRIENDREQTO).equals("1")) {
					IjoomerUtilities.getCustomOkDialog(getString(R.string.add_friend), getString(R.string.friend_request_to), getString(R.string.ok), R.layout.ijoomer_ok_dialog,
							new CustomAlertNeutral() {

						@Override
						public void NeutralMethod() {

						}
					});
				} else {
					showAddFriendDialog();
				}

			}
		});

		btnMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!IN_USERID.equals("0")) {
					try {
						loadNew(JomMessageComposeActivity.class, JomProfileActivity.this, false, "IN_USERID", IN_USERID, "IN_USERNAME", PROFILE.get(0).get(USER_NAME));
					} catch (Throwable e) {
						e.printStackTrace();
					}
				} else {
					try {
						loadNew(JomMessageActivity.class, JomProfileActivity.this, false, "IN_USERID", IN_USERID);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			}
		});

		imgAbout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new JomProfileDataProvider(JomProfileActivity.this).getUserDetails(IN_USERID, new WebCallListener() {
					final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

					@Override
					public void onProgressUpdate(int progressCount) {
						proSeekBar.setProgress(progressCount);
					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						if (responseCode == 200) {
							try {
								loadNew(JomProfileDetailsActivity.class, JomProfileActivity.this, false, "IN_USERID", IN_USERID);
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
		imgPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					loadNew(JomAlbumsActivity.class, JomProfileActivity.this, false, "IN_USERID", IN_USERID);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});
		imgVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					isSetProfileCoverPage=true;
					loadNew(JomVideoActivity.class, JomProfileActivity.this, false, "IN_USERID", IN_USERID);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});
		imgMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					loadNew(JomMapActivity.class, JomProfileActivity.this, false, "IN_MAPLIST", PROFILE, "IS_SHOW_BUBBLE", true);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});
		imgFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					loadNew(JomFriendListActivity.class, JomProfileActivity.this, false, "IN_USERID", IN_USERID);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});
		imgVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					loadNew(JomVideoActivity.class, JomProfileActivity.this, false, "IN_USERID", IN_USERID);
				} catch (Throwable e) {
					e.printStackTrace();
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
	private void getIntentData() {
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
		IjoomerUtilities.getCustomOkDialog(getString(R.string.dialog_loading_profile), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())),
				getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

			@Override
			public void NeutralMethod() {

			}
		});
	}

	/**
	 * This method used to get user profile.
	 */
	private void getProfile() {
		proSeekBar = null;
		if (!provider.isUserProfileExists() || !IN_USERID.equals("0")) {
			proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
		}
		provider.getUserProfile(IN_USERID, new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {
				if (proSeekBar != null) {
					proSeekBar.setProgress(progressCount);
				}
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				try{
					if (responseCode == 200) {
						if (data1 != null) {
							PROFILE = data1;
							setProfileDetail();
						}
					} else {
						responseErrorMessageHandler(responseCode, proSeekBar != null ? true : false);
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}

		});

	}

	/**
	 * This method used to get user wall.
	 */
	private void getWall() {
		listFooterVisible();
		wallDataProvider.restorePagingSettings();
		wallDataProvider.getWallList(IN_USERID, "wall", new WebCallListenerWithCacheInfo() {

			@Override
			public void onProgressUpdate(int progressCount) {
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2, int pageNo, int pageLimit, boolean fromCache) {
				try{
					//lstProfileDetail.onRefreshComplete();
					listFooterInvisible();
					if (responseCode == 200) {
						if (data1 != null) {
							txtRecentActivities.setText(getString(R.string.recent_activities));
							if(!fromCache){
								updateHeader(wallDataProvider.getNotificationData());
							}
							prepareList(data1, false, fromCache, pageNo, pageLimit);
							listAdapterWithHolder = getListAdapter();
							lstProfileDetail.setAdapter(listAdapterWithHolder);
						}
					} else {
						if (responseCode == 204) {
							txtRecentActivities.setText(getString(R.string.no_recent_activities));
							lstProfileDetail.setAdapter(null);
						} else {
							responseErrorMessageHandler(responseCode, false);
						}
					}
				}catch (Exception e){
					e.printStackTrace();
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
	 * This method used to start upload photo.
	 *
	 * @param path
	 *            represented photo path
	 * @param caption
	 *            represented photo caption
	 * @param voiceFilePath
	 *            represented voice file path(optional)
	 */
	private void startPhotoUpload(final String path, final String caption, final String voiceFilePath) {
		final JomGalleryDataProvider provider = new JomGalleryDataProvider(this);
		IjoomerUtilities.addToNotificationBar(getString(R.string.photo_upload_starts), getString(R.string.upload_photo), getString(R.string.photo_upload_starts));
		provider.uploadPhotoDefaultAlbum(path, caption, voiceFilePath, new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {

				if (responseCode == 200) {
					IjoomerUtilities.addToNotificationBar(getString(R.string.photo_upload_successfully), getString(R.string.upload_photo),
							getString(R.string.photo_upload_successfully));
					if (IjoomerUtilities.mSmartAndroidActivity instanceof JomProfileActivity) {
						getWall();
					}
				} else {
					if (errorMessage != null && errorMessage.length() > 0) {
						IjoomerUtilities.addToNotificationBar(getString(R.string.photo_upload_failure), getString(R.string.upload_photo), errorMessage);
					} else {
						IjoomerUtilities.addToNotificationBar(getString(R.string.photo_upload_failure), getString(R.string.upload_photo),
								getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())));
					}
				}
			}
		});
		imgUploadPhoto.setVisibility(View.GONE);
		selectedImagePathPhotoUpload = null;

	}

	/**
	 * This method used to set profile details.
	 */
	public void setProfileDetail() {

		spnPostType.setAdapter(new IjoomerUtilities.MyCustomAdapter(this, new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.wall_post_type)))));

		txtUserName.setText(PROFILE.get(0).get(USER_NAME));
		PROFILE.get(0).put(USER_STATUS, PROFILE.get(0).get(USER_STATUS).replace("\n", " "));
		if (getPlainText(PROFILE.get(0).get(USER_STATUS)).length() > 0) {
			btnPlayStopVoice.setVisibility(View.GONE);
			txtUserStatus.setVisibility(View.VISIBLE);
			txtUserStatus.setText(getPlainText(PROFILE.get(0).get(USER_STATUS)));
		}

		if (getAudio(PROFILE.get(0).get(USER_STATUS)) != null) {
			txtUserStatus.setVisibility(View.GONE);
			btnPlayStopVoice.setVisibility(View.VISIBLE);
			btnPlayStopVoice.setText(getAudioLength(PROFILE.get(0).get(USER_STATUS)));
			btnPlayStopVoice.setAudioPath(getAudio(PROFILE.get(0).get(USER_STATUS)), false);
			btnPlayStopVoice.setAudioListener(new AudioListener() {

				@Override
				public void onReportClicked() {
					reportVoice(getAudio(PROFILE.get(0).get(USER_STATUS)));
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
		if (PROFILE.get(0).containsKey(PROFILE_VIDEO)) {
			try {
				txtUserVideo.setText(new JSONObject(PROFILE.get(0).get(PROFILE_VIDEO)).getString(TITLE));
			} catch (Throwable e) {
				e.printStackTrace();
			}
			txtUserVideo.setVisibility(View.VISIBLE);
		}
		if (PROFILE.get(0).containsKey("coverpic") && PROFILE.get(0).get("coverpic").toString().length() > 0) {
			imgAddEditCover.setVisibility(View.VISIBLE);
			androidQuery.id(imgAddEditCover).image(PROFILE.get(0).get("coverpic"), true, true, getDeviceWidth(), 0);
		} else {
			imgAddEditCover.setVisibility(View.GONE);
			RelativeLayout.LayoutParams userImageLayoutParams = (RelativeLayout.LayoutParams) framUserImage.getLayoutParams();
			userImageLayoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.lnrUserLayout);

			LinearLayout.LayoutParams statusLayoutParams = (LinearLayout.LayoutParams) lnrUserStaus.getLayoutParams();
			statusLayoutParams.setMargins(convertSizeToDeviceDependent(90), 0, 0, 0);

		}

		if (IN_USERID == null || IN_USERID.equals("0")) {
			if (IjoomerGlobalConfiguration.isPhotoUpload()) {
				imgAddEditCoverPhoto.setVisibility(View.VISIBLE);
			}
			btnEdit.setVisibility(View.VISIBLE);
			txtWritePost.setChecked(true);
			txtUploadPhoto.setVisibility(View.VISIBLE);
			txtUploadVideo.setVisibility(View.VISIBLE);
			txtCreateEvent.setVisibility(View.VISIBLE);
			if (!IjoomerGlobalConfiguration.isEventCreate()) {
				txtCreateEvent.setVisibility(View.GONE);
			}
			if (!IjoomerGlobalConfiguration.isPhotoUpload()) {
				txtUploadPhoto.setVisibility(View.GONE);
			}

		}
		txtLike.setText(PROFILE.get(0).get(LIKES));
		txtUnlike.setText(PROFILE.get(0).get(DISLIKES));
		if (PROFILE.get(0).get(ISPROFILELIKE).equalsIgnoreCase("0")) {
			txtLike.setEnabled(false);
			txtUnlike.setEnabled(false);
		}
		if (PROFILE.get(0).get(ISFRIEND).equalsIgnoreCase("1") || IN_USERID.equals("0")) {
			btnAddFriend.setText(getString(R.string.already_friend));
			if (IN_USERID.equals("0")) {
				btnAddFriend.setVisibility(View.GONE);
			}
			lnrWritePost.setVisibility(View.VISIBLE);
		}
		txtFriend.setText(PROFILE.get(0).get(TOTALFRIENDS));
		txtPhoto.setText(PROFILE.get(0).get(TOTALPHOTOS));
		txtViewers.setText(PROFILE.get(0).get(VIEWCOUNT));
		txtVideo.setText(PROFILE.get(0).get(TOTALVIDEOS));

		androidQuery.id(imgUserImage).image(PROFILE.get(0).get(USER_AVATAR), true, true, getDeviceWidth(), 0);

	}

	/**
	 * This method used to show add friend dialog.
	 */
	private void showAddFriendDialog() {
		final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
		dialog.setContentView(R.layout.jom_add_new_friend_dialog);
		final IjoomerEditText edtMessage = (IjoomerEditText) dialog.findViewById(R.id.edtMessage);
		IjoomerButton btnCancel = (IjoomerButton) dialog.findViewById(R.id.btnCancel);
		IjoomerButton btnSend = (IjoomerButton) dialog.findViewById(R.id.btnSend);
		btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				if (edtMessage.getText().toString().trim().length() <= 0) {
					edtMessage.setError(getString(R.string.validation_value_required));
				} else {
					final SeekBar progressBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
					provider.addFriend(IN_USERID, edtMessage.getText().toString().trim(), new WebCallListener() {

						@Override
						public void onProgressUpdate(int progressCount) {
							progressBar.setProgress(progressCount);
						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								updateHeader(provider.getNotificationData());
								PROFILE.get(0).put(ISFRIENDREQTO, "1");
								dialog.dismiss();
							} else {
								responseErrorMessageHandler(responseCode, false);
							}
						}
					});
				}
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/**
	 * This method used to set like and dislike.
	 */
	private void setLikeDislike() {
		txtLike.setText(PROFILE.get(0).get(LIKES));
		txtUnlike.setText(PROFILE.get(0).get(DISLIKES));

	}

	/**
	 * This method used to prepare list user wall photo.
	 *
	 * @param lnrScrollable
	 *            represented scrollable view
	 * @param data
	 *            represented wall photo data
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
							loadNew(JomPhotoDetailsActivity.class, JomProfileActivity.this, false, 
									"IN_PHOTO_LIST", photoData, "IN_SELECTED_INDEX",
									0, "IN_TOTAL_COUNT", Integer.parseInt(IN_ALBUM.get(COUNT)), "IN_USERID", IN_USERID, "IN_ALBUM", IN_ALBUM);
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
								loadNew(JomPhotoDetailsActivity.class, JomProfileActivity.this, false, "IN_PHOTO_LIST", photoData,
										"IN_SELECTED_INDEX",0, "IN_TOTAL_COUNT", Integer.parseInt(IN_ALBUM.get(COUNT)), "IN_USERID", IN_USERID, "IN_ALBUM", IN_ALBUM);
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
								loadNew(JomPhotoDetailsActivity.class, JomProfileActivity.this, false, "IN_PHOTO_LIST", photoData, "IN_SELECTED_INDEX",
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
	 * This method used to prepare list user wall.
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
		if (data != null) {
			if (!append) {
				listData.clear();
			} else {
				if (!fromCache) {
					int startIndex = ((pageno - 1) * pagelimit);
					int endIndex = listAdapterWithHolder.getCount();
					if (startIndex > 0) {
						for (int i = endIndex; i >= startIndex; i--) {
							try {
								listAdapterWithHolder.remove(listAdapterWithHolder.getItem(i));
								listData.remove(i);
							} catch (Exception e) {
							}
						}
					}
				}
			}
			for (HashMap<String, String> hashMap : data) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.jom_wall_activity_list_item);
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
	 * List adapter for user wall.
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
				holder.imgWallOrActvityUploadedPhotos = (ImageView) v.findViewById(R.id.imguploadedPhotos);
				holder.lnrWallOrActivityContentCoverPhoto = (RelativeLayout) v.findViewById(R.id.lnrWallOrActivityContentCoverPhoto);
				holder.imgWallOrActvityCoverPhoto = (ImageView) v.findViewById(R.id.imgWallOrActvityCoverPhoto);
				holder.lnrWallOrActivityContentVideo = (LinearLayout) v.findViewById(R.id.lnrWallOrActivityContentVideo);
				holder.lnrWallOrActivityContentView = (LinearLayout) v.findViewById(R.id.lnrWallOrActivityContentView);
				holder.lnrContentImageScrollable = (LinearLayout) v.findViewById(R.id.lnrContentImageScrollable);
				holder.imgWallOrActvityContentVideoImage = (ImageView) v.findViewById(R.id.imgWallOrActvityContentVideoImage);
				holder.btnWallOrActivityRemove = (IjoomerButton) v.findViewById(R.id.btnWallOrActivityRemove);
				holder.lnrWallOrActivityLikeCommnet = (LinearLayout) v.findViewById(R.id.lnrWallOrActivityLikeCommnet);
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
						}

						@Override
						public void onComplete() {
						}
					});

				}

				if (row.get(TYPE).toString().trim().equals(COVERUPLOAD) || row.get(TYPE).toString().trim().equals(AVATARUPLOAD)
						|| row.get(TYPE).toString().trim().equals(GROUPSAVATARUPLOAD)|| row.get(TYPE).toString().trim().equals(EVENTSAVATARUPLOAD)) {
					holder.lnrWallOrActivityContentCoverPhoto.setVisibility(View.VISIBLE);
					try{
						JSONObject imageData = new JSONObject(row.get(IMAGE_DATA));
						androidQuery.id(holder.imgWallOrActvityCoverPhoto).progress(R.id.coverimgprogress).image(imageData.getString(URL),true,true);
					}catch (Throwable e){
						e.printStackTrace();
					}
				} else if (row.get(TYPE).toString().trim().equals(PHOTOS)) {
					holder.lnrWallOrActivityContentImage.setVisibility(View.VISIBLE);
					preparePhotoList(holder.imgWallOrActvityUploadedPhotos, holder.lnrContentImageScrollable, row);
				}else if (row.get(TYPE).toString().trim().equals(VIDEOS)) {
					holder.lnrWallOrActivityContentVideo.setVisibility(View.VISIBLE);

					try {
						androidQuery.id(holder.imgWallOrActvityContentVideoImage).image(new JSONObject(row.get(CONTENT_DATA)).getString(THUMB), true, true, getDeviceWidth(), 0);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}else if (row.get(TYPE).toString().trim().equals(EVENT)) {
					try{
						holder.lnrWallOrActivityContentView.setVisibility(View.VISIBLE);
						holder.lnrWallOrActivityContentView.removeAllViews();

						JSONObject eventData = new JSONObject(row.get(CONTENT_DATA));

						LayoutInflater inflater = LayoutInflater.from(JomProfileActivity.this);
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
										loadNew(JomEventDetailsActivity_v30.class, JomProfileActivity.this, false, "IN_USERID", IN_USERID, "IN_EVENT_DETAILS", jsonToMap(jsonObject),
												"IN_GROUP_ID", IN_GROUP_ID);
									} catch (Throwable e) {
										e.printStackTrace();
									}
								} else {
									try {
										loadNew(JomEventDetailsActivity.class, JomProfileActivity.this, false, "IN_USERID", IN_USERID, "IN_EVENT_DETAILS", jsonToMap(jsonObject),
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
				}else if (row.get(TYPE).toString().trim().equals(GROUP)) {
					try{
						holder.lnrWallOrActivityContentView.setVisibility(View.VISIBLE);
						holder.lnrWallOrActivityContentView.removeAllViews();

						JSONObject eventData = new JSONObject(row.get(CONTENT_DATA));

						LayoutInflater inflater = LayoutInflater.from(JomProfileActivity.this);
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
										loadNew(JomGroupDetailsActivity_v30.class, JomProfileActivity.this, false, "IN_USERID", IN_USERID, "IN_GROUP_DETAILS", jsonToMap(jsonObject));
									} catch (Throwable e) {
										e.printStackTrace();
									}
								} else {

									try {
										loadNew(JomGroupDetailsActivity.class, JomProfileActivity.this, false, "IN_USERID", IN_USERID, "IN_GROUP_DETAILS", jsonToMap(jsonObject));
									} catch (Throwable e) {
										e.printStackTrace();
									}
								}
							}
						});
						holder.lnrWallOrActivityContentView.addView(groupView);
					}catch (Throwable e){
						e.printStackTrace();
					}
				}else if (row.get(TYPE).toString().trim().equals(DISCUSSION)) {
					try{
						holder.lnrWallOrActivityContentView.setVisibility(View.VISIBLE);
						holder.lnrWallOrActivityContentView.removeAllViews();

						JSONObject discussionData = new JSONObject(row.get(CONTENT_DATA));

						LayoutInflater inflater = LayoutInflater.from(JomProfileActivity.this);
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
										loadNew(JomGroupDiscussionDetailsActivity.class, JomProfileActivity.this, false, "IN_DISCUSSION_DETAILS", jsonToMap(jsonObjectDiscussion),
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

						LayoutInflater inflater = LayoutInflater.from(JomProfileActivity.this);
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
										loadNew(JomGroupAnnouncementDetailsActivity.class, JomProfileActivity.this, false, "IN_ANNOUCEMENT_DETAILS", jsonToMap(jsonObjectAnnouncement),
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
								loadNew(JomWallOrActivityDetailActivity.class, JomProfileActivity.this, false, "IN_WALL_DETAILS", IN_WALL_DETAILS, "IN_WALL_DETAILS_LIST_TYPE",
										LIKES, "IN_USERID", IN_USERID);
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
								loadNew(JomWallOrActivityDetailActivity.class, JomProfileActivity.this, false, "IN_WALL_DETAILS", IN_WALL_DETAILS, "IN_WALL_DETAILS_LIST_TYPE",
										COMMENTS, "IN_USERID", IN_USERID);
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
											listAdapterWithHolder.remove(listAdapterWithHolder.getItem(position));
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
							loadNew(JomWallOrActivityDetailActivity.class, JomProfileActivity.this, false, "IN_WALL_DETAILS", IN_WALL_DETAILS, "IN_WALL_DETAILS_LIST_TYPE",
									COMMENTS, "IN_USERID", IN_USERID);
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
							Intent lVideoIntent = new Intent(null,getVideoPlayURI(url), JomProfileActivity.this, IjoomerMediaPlayer.class);
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

}
