package com.ijoomer.components.jomsocial;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.androidquery.AQuery;
import com.ijoomer.caching.IjoomerCachingConstants;
import com.ijoomer.common.classes.IjoomerSpannable;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.custom.interfaces.AnnouncementAndDiscussionListner;
import com.ijoomer.custom.interfaces.ReportListner;
import com.ijoomer.customviews.IjoomerAudioPlayer;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerCheckBox;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.jomsocial.JomNotificationDataProvider;
import com.ijoomer.library.jomsocial.JomProfileDataProvider;
import com.ijoomer.library.jomsocial.JomReportVoiceDataProvider;
import com.ijoomer.media.player.IjoomerMediaPlayer;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To JomMasterActivity.
 * 
 * @author tasol
 * 
 */
public abstract class JomMasterActivity extends IjoomerSuperMaster implements JomTagHolder {

	private ImageView imgFriendNotification;
	private ImageView imgMessageNotification;
	private ImageView imgGlobalNotification;
	private IjoomerTextView txtFriendNotification;
	private IjoomerTextView txtMessageNotification;
	private IjoomerTextView txtGlobalNotification;
	private PopupWindow popup;

	private ArrayList<SmartListItem> listItemData = new ArrayList<SmartListItem>();
	private ArrayList<HashMap<String, String>> listData = null;
	private SmartListAdapterWithHolder listAdapterWithHolder;

	private JomNotificationDataProvider provider;
	public JomProfileDataProvider providerProfile;

	private int POPUP_FRIEND_REQUEST = 0;
	private int POPUP_MESSAGES = 1;
	private int POPUP_GLOBAL = 2;

	public JomMasterActivity() {
		super();
		IjoomerCachingConstants.unNormalizeFields = JomCachingConstants.getUnnormlizeFields();
		IjoomerCachingConstants.unRepetedFields = JomCachingConstants.getUnRepetedField();
	}

	/**
	 * Overrides methods
	 */

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public int setFooterLayoutId() {
		return R.layout.ijoomer_footer;
	}

	@Override
	public int setHeaderLayoutId() {
		return R.layout.jom_header;
	}

	@Override
	public int setTabBarDividerResId() {
		return 0;
	}

	@Override
	public int setTabItemLayoutId() {
		return 0;
	}

	@Override
	public String[] setTabItemNames() {
		return null;
	}

	@Override
	public int[] setTabItemOffDrawables() {
		return null;
	}

	@Override
	public int[] setTabItemOnDrawables() {
		return null;
	}

	@Override
	public int[] setTabItemPressDrawables() {
		return null;
	}

	@Override
	public void loadHeaderComponents() {
		super.loadHeaderComponents();

		try {
			imgFriendNotification = (ImageView) getHeaderView().findViewById(R.id.imgFriendNotification);
			imgMessageNotification = (ImageView) getHeaderView().findViewById(R.id.imgMessageNotification);
			imgGlobalNotification = (ImageView) getHeaderView().findViewById(R.id.imgGlobalNotification);

			txtGlobalNotification = (IjoomerTextView) getHeaderView().findViewById(R.id.txtGlobalNotification);
			txtMessageNotification = (IjoomerTextView) getHeaderView().findViewById(R.id.txtMessageNotification);
			txtFriendNotification = (IjoomerTextView) getHeaderView().findViewById(R.id.txtFriendNotification);

			imgFriendNotification.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showPopup(POPUP_FRIEND_REQUEST);
					imgFriendNotification.setImageResource(R.drawable.jom_notification_friend);
				}
			});

			imgMessageNotification.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showPopup(POPUP_MESSAGES);
					imgMessageNotification.setImageResource(R.drawable.jom_notification_message);
				}
			});

			imgGlobalNotification.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showPopup(POPUP_GLOBAL);
					imgGlobalNotification.setImageResource(R.drawable.jom_notification_group);
				}
			});
			updateHeader(new JomNotificationDataProvider(this).getNotificationData());
		} catch (Exception e) {
		}

	}
	@Override
	protected void onResume() {
		super.onResume();
		IjoomerCachingConstants.unNormalizeFields = JomCachingConstants.getUnnormlizeFields();
		IjoomerCachingConstants.unRepetedFields = JomCachingConstants.getUnRepetedField();
		updateHeader(new JomNotificationDataProvider(this).getNotificationData());
	}
	@Override
	protected void onPause() {
		super.onPause();
		IjoomerAudioPlayer.stopAll();
	}
	@Override
	public View setBottomAdvertisement() {

		return null;// getAdvertisement("0445b7141d9d4e1b");
	}

	@Override
	public View setTopAdvertisement() {

		return null; // getAdvertisement("0445b7141d9d4e1b");
	}


	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

	}

	/**
	 * Class methods
	 */


	/**
	 * This method used to update Jom header.
	 * @param notificationData represented header data.
	 */
	public void updateHeader(JSONObject notificationData) {
		try {
			if (!notificationData.getString(FRIENDNOTIFICATION).equals("0")) {
				txtFriendNotification.setVisibility(View.VISIBLE);
				txtFriendNotification.setText(notificationData.getString(FRIENDNOTIFICATION));
				imgFriendNotification.setImageResource(R.drawable.jom_notification_friend_hover);
			} else {
				txtFriendNotification.setVisibility(View.GONE);
				imgFriendNotification.setImageResource(R.drawable.jom_notification_friend);
			}

			if (!notificationData.getString(MESSAGENOTIFICATION).equals("0")) {
				txtMessageNotification.setText(notificationData.getString(MESSAGENOTIFICATION));
				txtMessageNotification.setVisibility(View.VISIBLE);
				imgMessageNotification.setImageResource(R.drawable.jom_notification_message_hover);
			} else {
				txtMessageNotification.setVisibility(View.GONE);
				imgMessageNotification.setImageResource(R.drawable.jom_notification_message);
			}

			if (!notificationData.getString(GLOBALNOTIFICATION).equals("0")) {
				txtGlobalNotification.setText(notificationData.getString(GLOBALNOTIFICATION));
				txtGlobalNotification.setVisibility(View.VISIBLE);
				imgGlobalNotification.setImageResource(R.drawable.jom_notification_group_hover);
			} else {
				txtGlobalNotification.setVisibility(View.GONE);
				imgGlobalNotification.setImageResource(R.drawable.jom_notification_group);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method used to goto user profile.
	 * @param userID represented user id
	 */
	public void gotoProfile(final String userID) {
		try {
			loadNew(JomProfileActivity.class, JomMasterActivity.this, false, "IN_USERID", userID);
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}


	/**
	 * This method used to show Jom notification Popup.
	 * @param popType represented poptype(POPUP_FRIEND_REQUEST,POPUP_GLOBAL,POPUP_MESSAGES)
	 */
	@SuppressWarnings("deprecation")
	private void showPopup(final int popType) {
		provider = new JomNotificationDataProvider(this);
		int popupWidth = getDeviceWidth() - convertSizeToDeviceDependent(20);
		int popupHeight = getDeviceHeight() - convertSizeToDeviceDependent(110);

		LinearLayout viewGroup = (LinearLayout) findViewById(R.id.popup);
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = layoutInflater.inflate(R.layout.jom_notification_popup, viewGroup);

		popup = new PopupWindow(this);
		popup.setContentView(layout);
		popup.setWidth(popupWidth);
		popup.setHeight(popupHeight);
		popup.setFocusable(true);
		popup.setBackgroundDrawable(new BitmapDrawable(getResources()));
		popup.showAtLocation(layout, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);

		IjoomerTextView textNotificationHeader = (IjoomerTextView) layout.findViewById(R.id.textNotificationHeader);
		if (popType == POPUP_FRIEND_REQUEST) {
			textNotificationHeader.setText(getString(R.string.friend_request));
		} else if (popType == POPUP_GLOBAL) {
			textNotificationHeader.setText(getString(R.string.global_notification));
		} else if (popType == POPUP_MESSAGES) {
			textNotificationHeader.setText(getString(R.string.unread_msg));
		}
		IjoomerButton btnClose = (IjoomerButton) layout.findViewById(R.id.btnClose);
		final ProgressBar pbrPopup = (ProgressBar) layout.findViewById(R.id.pbrPopup);
		final IjoomerTextView textnocontent = (IjoomerTextView) layout.findViewById(R.id.textnocontent);
		final ListView lstNotificationData = (ListView) layout.findViewById(R.id.lstNotificationData);

		final JomNotificationDataProvider notificationDataProvider = new JomNotificationDataProvider(this);
		notificationDataProvider.getNotifications(new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {

			}

			@SuppressWarnings("unchecked")
			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				if (responseCode == 200) {
					updateHeader(notificationDataProvider.getNotificationData());
					if (popType == POPUP_FRIEND_REQUEST) {
						listData = (ArrayList<HashMap<String, String>>) ((ArrayList<Object>) data2).get(0);
						textnocontent.setText(getString(R.string.no_pending_approval));
					} else if (popType == POPUP_GLOBAL) {
						listData = (ArrayList<HashMap<String, String>>) ((ArrayList<Object>) data2).get(1);
					} else if (popType == POPUP_MESSAGES) {
						listData = (ArrayList<HashMap<String, String>>) ((ArrayList<Object>) data2).get(2);
						textnocontent.setText(getString(R.string.no_new_messages));
					}

					if(listData!=null && listData.size() > 0){
						listAdapterWithHolder = getListAdapter(prepareNotificationList(popType, listData), popType);
						lstNotificationData.setAdapter(listAdapterWithHolder);
						pbrPopup.setVisibility(View.GONE);
					}else{
						pbrPopup.setVisibility(View.GONE);
						textnocontent.setVisibility(View.VISIBLE);
					}
				}
			}
		});

		btnClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popup.dismiss();
			}
		});

	}

	/**
	 * This method used to get announcement or discussion create dialog.
	 * @param dialogTitle represented dialog title
	 * @param title represented announcement or discussion title
	 * @param message represented announcement or discussion message
	 * @param isUploadFile represented announcement or discussion upload file permission
	 * @param target represented {@link AnnouncementAndDiscussionListner}
	 * @return represented {@link Dialog}
	 */
	public Dialog getAnnouncementOrDiscussionCreateDialog(final String dialogTitle, final String title, final String message, final String isUploadFile,
			final AnnouncementAndDiscussionListner target) {

		final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
		dialog.setContentView(R.layout.jom_group_discussion_announcement_dialog);
		dialog.show();

		final IjoomerTextView txtDiscussionAnnouncementTitle = (IjoomerTextView) dialog.findViewById(R.id.txtDiscussionAnnouncementTitle);
		txtDiscussionAnnouncementTitle.setText(dialogTitle);
		final ImageView imgDiscussionAnnouncementClose = (ImageView) dialog.findViewById(R.id.imgDiscussionAnnouncementClose);
		final IjoomerEditText edtDiscussionAnnouncementTitle = (IjoomerEditText) dialog.findViewById(R.id.edtDiscussionAnnouncementTitle);
		final IjoomerEditText edtDiscussionAnnouncementMessage = (IjoomerEditText) dialog.findViewById(R.id.edtDiscussionAnnouncementMessage);
		final IjoomerCheckBox chbDiscussionAnnouncementAllowMemberUploadFile = (IjoomerCheckBox) dialog.findViewById(R.id.chbDiscussionAnnouncementAllowMemberUploadFile);

		IjoomerButton btnCreate = (IjoomerButton) dialog.findViewById(R.id.btnCreate);
		IjoomerButton btnCancel = (IjoomerButton) dialog.findViewById(R.id.btnCancel);
		if (title.trim().length() > 0 && message.trim().length() > 0 && isUploadFile.trim().length() > 0) {
			edtDiscussionAnnouncementTitle.setText(title);
			edtDiscussionAnnouncementMessage.setText(message);
			chbDiscussionAnnouncementAllowMemberUploadFile.setChecked(isUploadFile.equals("1") ? true : false);
			btnCreate.setText(getString(R.string.save));
		}

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});

		btnCreate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean validationFlag = true;

				if (edtDiscussionAnnouncementTitle.getText().toString().trim().length() <= 0) {
					validationFlag = false;
					edtDiscussionAnnouncementTitle.setError(getString(R.string.validation_value_required));

				}
				if (edtDiscussionAnnouncementMessage.getText().toString().trim().length() <= 0) {
					validationFlag = false;
					edtDiscussionAnnouncementMessage.setError(getString(R.string.validation_value_required));
				}

				if (validationFlag) {
					target.onClick(edtDiscussionAnnouncementTitle.getText().toString(), edtDiscussionAnnouncementMessage.getText().toString(),
							chbDiscussionAnnouncementAllowMemberUploadFile.isChecked() ? "1" : "0");
				}

			}
		});

		imgDiscussionAnnouncementClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});

		return dialog;

	}


	/**
	 * This method used to add clicable part on spannable string.
	 * @param strSpanned represented spannable string
	 * @param row represented spannable data
	 * @return represented {@link SpannableStringBuilder}
	 */
	public SpannableStringBuilder addClickablePart(Spanned strSpanned, final HashMap<String, String> row) {
		String str = strSpanned.toString();
		SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

		if (str.contains(row.get(USER_NAME))) {
			ssb.setSpan(new IjoomerSpannable(Color.parseColor(getString(R.color.jom_blue)), true) {
				final String IN_USERID = row.get(USER_ID);

				@Override
				public void onClick(View widget) {
					if (row.get(USER_PROFILE).equals("1")) {
						gotoProfile(IN_USERID);
					}

				}
			}, str.indexOf(row.get(USER_NAME)), str.indexOf(row.get(USER_NAME)) + row.get(USER_NAME).length(), 0);

		}
		return ssb;

	}

	/**
	 * This method used to add clicable part on spannable string.
	 * @param strSpanned represented spannable string
	 * @param row represented spannable data
	 * @param type represented spannable data type
	 * @param userId represented  id
	 * @return
	 */
	public SpannableStringBuilder addClickablePart(Spanned strSpanned, final HashMap<String, String> row, String type, final String userId) {
		String str = strSpanned.toString();
		SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

		if (str.contains(row.get(USER_NAME))) {
			ssb.setSpan(new IjoomerSpannable(Color.parseColor(getString(R.color.jom_blue)), true) {
				final String IN_USERID = row.get(USER_ID);

				@Override
				public void onClick(View widget) {
					gotoProfile(IN_USERID);
				}
			}, str.indexOf(row.get(USER_NAME)), str.indexOf(row.get(USER_NAME)) + row.get(USER_NAME).length(), 0);

		}

		if (type.equalsIgnoreCase(FRIENDS)) {
			try {
				final JSONObject jsonObject = new JSONObject(row.get(CONTENT_DATA));
				if (jsonObject != null && str.contains(jsonObject.getString(USER_NAME))) {
					final String IN_USERID = jsonObject.getString(USER_ID);
					ssb.setSpan(new IjoomerSpannable(Color.parseColor(getString(R.color.jom_blue)), true) {

						@Override
						public void onClick(View widget) {
							try {
								if (jsonObject.getString(USER_PROFILE).equals("1")) {
									if (IN_USERID.equals("0")) {
										// listView.setSelection(0);
									} else {
										gotoProfile(IN_USERID);
									}
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}, str.indexOf(jsonObject.getString(USER_NAME)), str.indexOf(jsonObject.getString(USER_NAME)) + jsonObject.getString(USER_NAME).length(), 0);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (type.equalsIgnoreCase(COVERUPLOAD)) {
			try {

				if(row.containsKey(EVENT_DATA) && row.get(EVENT_DATA).trim().length()>0){
					final JSONObject jsonObject = new JSONObject(row.get(EVENT_DATA));
					if (jsonObject != null && str.contains(jsonObject.getString(TITLE))) {

						ssb.setSpan(new IjoomerSpannable(Color.parseColor(getString(R.color.jom_blue)), true) {
							@Override
							public void onClick(View widget) {
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
										loadNew(JomEventDetailsActivity_v30.class, JomMasterActivity.this, false, "IN_USERID", userId, "IN_EVENT_DETAILS", jsonToMap(jsonObject),
												"IN_GROUP_ID", IN_GROUP_ID);
									} catch (Throwable e) {
										e.printStackTrace();
									}
								} else {
									try {
										loadNew(JomEventDetailsActivity.class, JomMasterActivity.this, false, "IN_USERID", userId, "IN_EVENT_DETAILS", jsonToMap(jsonObject),
												"IN_GROUP_ID", IN_GROUP_ID);
									} catch (Throwable e) {
										e.printStackTrace();
									}
								}
							}
						}, str.indexOf(jsonObject.getString(TITLE)), str.indexOf(jsonObject.getString(TITLE)) + jsonObject.getString(TITLE).length(), 0);

					}
				}else if(row.containsKey(GROUP_DATA) && row.get(GROUP_DATA).trim().length()>0){
					final JSONObject jsonObject = new JSONObject(row.get(GROUP_DATA));
					if (jsonObject != null && str.contains(jsonObject.getString(TITLE))) {
						ssb.setSpan(new IjoomerSpannable(Color.parseColor(getString(R.color.jom_blue)), true) {

							@Override
							public void onClick(View widget) {
								if (IjoomerGlobalConfiguration.getJomsocialVersion().equals(IjoomerGlobalConfiguration.JOMVERSION_V30)) {
									try {
										loadNew(JomGroupDetailsActivity_v30.class, JomMasterActivity.this, false, "IN_USERID", userId, "IN_GROUP_DETAILS", jsonToMap(jsonObject));
									} catch (Throwable e) {
										e.printStackTrace();
									}
								} else {

									try {
										loadNew(JomGroupDetailsActivity.class, JomMasterActivity.this, false, "IN_USERID", userId, "IN_GROUP_DETAILS", jsonToMap(jsonObject));
									} catch (Throwable e) {
										e.printStackTrace();
									}
								}
							}
						}, str.indexOf(jsonObject.getString(TITLE)), str.indexOf(jsonObject.getString(TITLE)) + jsonObject.getString(TITLE).length(), 0);

					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}else if (type.equalsIgnoreCase(GROUP)) {
			try {
				final JSONObject jsonObject = new JSONObject(row.get(CONTENT_DATA));
				if (jsonObject != null && str.contains(jsonObject.getString(TITLE))) {
					ssb.setSpan(new IjoomerSpannable(Color.parseColor(getString(R.color.jom_blue)), true) {

						@Override
						public void onClick(View widget) {
							if (IjoomerGlobalConfiguration.getJomsocialVersion().equals(IjoomerGlobalConfiguration.JOMVERSION_V30)) {
								try {
									loadNew(JomGroupDetailsActivity_v30.class, JomMasterActivity.this, false, "IN_USERID", userId, "IN_GROUP_DETAILS", jsonToMap(jsonObject));
								} catch (Throwable e) {
									e.printStackTrace();
								}
							} else {

								try {
									loadNew(JomGroupDetailsActivity.class, JomMasterActivity.this, false, "IN_USERID", userId, "IN_GROUP_DETAILS", jsonToMap(jsonObject));
								} catch (Throwable e) {
									e.printStackTrace();
								}
							}
						}
					}, str.indexOf(jsonObject.getString(TITLE)), str.indexOf(jsonObject.getString(TITLE)) + jsonObject.getString(TITLE).length(), 0);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (type.equalsIgnoreCase(ANNOUNCEMENT)) {
			try {
				final JSONObject jsonObject = new JSONObject(row.get(GROUP_DATA));
				final JSONObject jsonObjectAnnouncement = new JSONObject(row.get(CONTENT_DATA));
				if (jsonObjectAnnouncement != null && str.contains(jsonObjectAnnouncement.getString(TITLE))) {
					ssb.setSpan(new IjoomerSpannable(Color.parseColor(getString(R.color.jom_blue)), true) {

						@Override
						public void onClick(View widget) {
							try {
								loadNew(JomGroupAnnouncementDetailsActivity.class, JomMasterActivity.this, false, "IN_ANNOUCEMENT_DETAILS", jsonToMap(jsonObjectAnnouncement),
										"IN_GROUP_DETAILS", jsonToMap(jsonObject));
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					}, str.indexOf(jsonObjectAnnouncement.getString(TITLE)), str.indexOf(jsonObjectAnnouncement.getString(TITLE))
					+ jsonObjectAnnouncement.getString(TITLE).length(), 0);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (type.equalsIgnoreCase(DISCUSSION)) {

			try {
				final JSONObject jsonObject = new JSONObject(row.get(GROUP_DATA));
				final JSONObject jsonObjectDiscussion = new JSONObject(row.get(CONTENT_DATA));
				if (jsonObjectDiscussion != null && str.contains(jsonObjectDiscussion.getString(TITLE))) {
					ssb.setSpan(new IjoomerSpannable(Color.parseColor(getString(R.color.jom_blue)), true) {

						@Override
						public void onClick(View widget) {
							try {
								loadNew(JomGroupDiscussionDetailsActivity.class, JomMasterActivity.this, false, "IN_DISCUSSION_DETAILS", jsonToMap(jsonObjectDiscussion),
										"IN_GROUP_DETAILS", jsonToMap(jsonObject));
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					}, str.indexOf(jsonObjectDiscussion.getString(TITLE)), str.indexOf(jsonObjectDiscussion.getString(TITLE)) + jsonObjectDiscussion.getString(TITLE).length(), 0);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (type.equalsIgnoreCase(EVENT)) {
			try {
				final JSONObject jsonObject = new JSONObject(row.get(CONTENT_DATA));
				if (jsonObject != null && str.contains(jsonObject.getString(TITLE))) {

					ssb.setSpan(new IjoomerSpannable(Color.parseColor(getString(R.color.jom_blue)), true) {
						@Override
						public void onClick(View widget) {
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
									loadNew(JomEventDetailsActivity_v30.class, JomMasterActivity.this, false, "IN_USERID", userId, "IN_EVENT_DETAILS", jsonToMap(jsonObject),
											"IN_GROUP_ID", IN_GROUP_ID);
								} catch (Throwable e) {
									e.printStackTrace();
								}
							} else {
								try {
									loadNew(JomEventDetailsActivity.class, JomMasterActivity.this, false, "IN_USERID", userId, "IN_EVENT_DETAILS", jsonToMap(jsonObject),
											"IN_GROUP_ID", IN_GROUP_ID);
								} catch (Throwable e) {
									e.printStackTrace();
								}
							}
						}
					}, str.indexOf(jsonObject.getString(TITLE)), str.indexOf(jsonObject.getString(TITLE)) + jsonObject.getString(TITLE).length(), 0);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (type.equalsIgnoreCase(PHOTOS)) {
			try {
				final JSONObject jsonObject = new JSONObject(row.get(CONTENT_DATA));
				if (jsonObject != null && str.contains(jsonObject.getString(NAME))) {
					ssb.setSpan(new IjoomerSpannable(Color.parseColor(getString(R.color.jom_blue)), true) {

						@Override
						public void onClick(View widget) {

							String IN_GROUP_ID = "0";
							String IN_GROUP_UPLOAD_PHOTO = "0";
							try {
								if (row.containsKey(GROUP_DATA)) {
									IN_GROUP_ID = new JSONObject(row.get(CONTENT_DATA)).getString(GROUPID);
								}
								if (jsonObject.has(UPLOADPHOTO)) {
									IN_GROUP_UPLOAD_PHOTO = jsonObject.getString(UPLOADPHOTO);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
							try {
								row.putAll(new HashMap<String, String>(jsonToMap(jsonObject)));
							} catch (JSONException e1) {
								e1.printStackTrace();
							}
							try {
								loadNew(JomAlbumsDetailsActivity.class, JomMasterActivity.this, false, "IN_USERID", userId, "IN_ALBUM", row, "IN_GROUP_ID", IN_GROUP_ID,
										"IN_GROUP_UPLOAD_PHOTO", IN_GROUP_UPLOAD_PHOTO);
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					}, str.indexOf(jsonObject.getString(NAME)), str.indexOf(jsonObject.getString(NAME)) + jsonObject.getString(NAME).length(), 0);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (type.equalsIgnoreCase(VIDEOS)) {

			try {
				final JSONObject jsonObject = new JSONObject(row.get(CONTENT_DATA));
				if (jsonObject != null && str.contains(getString(R.string.video).toLowerCase())) {
					ssb.setSpan(new IjoomerSpannable(Color.parseColor(getString(R.color.jom_blue)), true) {

						@Override
						public void onClick(View widget) {
							String IN_GROUP_ID = "0";
							if (jsonObject.has(GROUPID)) {
								try {
									IN_GROUP_ID = jsonObject.getString(GROUPID);
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
							try {
								row.putAll(new HashMap<String, String>(jsonToMap(jsonObject)));
							} catch (JSONException e1) {
								e1.printStackTrace();
							}
							try {
								loadNew(JomVideoDetailsActivity.class, JomMasterActivity.this, false, "IN_USERID", userId, "IN_VIDEO_DETAILS", row, "IN_GROUP_ID", IN_GROUP_ID);
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					}, str.indexOf(getString(R.string.video).toLowerCase()), str.indexOf(getString(R.string.video).toLowerCase())
					+ getString(R.string.video).toLowerCase().length(), 0);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				final JSONObject jsonObject = new JSONObject(row.get(CONTENT_DATA));
				if (jsonObject != null && str.contains(jsonObject.getString(CAPTION))) {
					ssb.setSpan(new IjoomerSpannable(Color.parseColor(getString(R.color.jom_blue)), true) {

						@Override
						public void onClick(View widget) {

							HashMap<String, String> map = null;
							try {
								map = (HashMap<String, String>) jsonToMap(jsonObject);
							} catch (JSONException e) {
								e.printStackTrace();
							}
							if (!map.get(URL).toString().contains(YOUTUBE)) {
								Intent lVideoIntent = new Intent(null, Uri.parse("mp4://" + map.get(URL).toString()), JomMasterActivity.this, IjoomerMediaPlayer.class);
								startActivity(lVideoIntent);

							} else {
								Intent lVideoIntent = new Intent(null, Uri.parse("ytv://" + map.get(URL).toString().split("=")[1] + ""), JomMasterActivity.this,
										IjoomerMediaPlayer.class);
								startActivity(lVideoIntent);

							}

						}
					}, str.indexOf(jsonObject.getString(CAPTION)), str.indexOf(jsonObject.getString(CAPTION)) + jsonObject.getString(CAPTION).length(), 0);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (row.containsKey(GROUP_DATA) && !type.equalsIgnoreCase(COVERUPLOAD) && row.get(GROUP_DATA).trim().length()>0) {
			try {
				final JSONObject jsonObject = new JSONObject(row.get(GROUP_DATA));
				if (jsonObject != null && str.contains(jsonObject.getString(TITLE))) {
					ssb.setSpan(new IjoomerSpannable(Color.parseColor(getString(R.color.jom_blue)), true) {

						@Override
						public void onClick(View widget) {
							if (IjoomerGlobalConfiguration.getJomsocialVersion().equals(IjoomerGlobalConfiguration.JOMVERSION_V30)) {
								try {
									loadNew(JomGroupDetailsActivity_v30.class, JomMasterActivity.this, false, "IN_USERID", userId, "IN_GROUP_DETAILS", jsonToMap(jsonObject));
								} catch (Throwable e) {
									e.printStackTrace();
								}
							} else {
								try {
									loadNew(JomGroupDetailsActivity.class, JomMasterActivity.this, false, "IN_USERID", userId, "IN_GROUP_DETAILS", jsonToMap(jsonObject));
								} catch (Throwable e) {
									e.printStackTrace();
								}
							}
						}
					}, str.indexOf(jsonObject.getString(TITLE)), str.indexOf(jsonObject.getString(TITLE)) + jsonObject.getString(TITLE).length(), 0);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (row.containsKey(EVENT_DATA) && !type.equalsIgnoreCase(COVERUPLOAD) && row.get(EVENT_DATA).trim().length()>0) {
			try {
				final JSONObject jsonObject = new JSONObject(row.get(EVENT_DATA));
				if (jsonObject != null && str.contains(jsonObject.getString(TITLE))) {
					ssb.setSpan(new IjoomerSpannable(Color.parseColor(getString(R.color.jom_blue)), true) {

						@Override
						public void onClick(View widget) {
							String IN_GROUP_ID = "0";
							if (jsonObject.has(GROUPID)) {
								try {
									IN_GROUP_ID = jsonObject.getString(GROUPID);
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
							try {
								loadNew(JomEventDetailsActivity.class, JomMasterActivity.this, false, "IN_USERID", userId, "IN_EVENT_DETAILS", jsonToMap(jsonObject),
										"IN_GROUP_ID", IN_GROUP_ID);
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					}, str.indexOf(jsonObject.getString(TITLE)), str.indexOf(jsonObject.getString(TITLE)) + jsonObject.getString(TITLE).length(), 0);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return ssb;
	}


	/**
	 * This method used to prepare list for notification.
	 * @param type represented notification type
	 * @param data represented notification data
	 * @return represented {@link SmartListItem} list
	 */
	private ArrayList<SmartListItem> prepareNotificationList(final int type, ArrayList<HashMap<String, String>> data) {
		listItemData.clear();
		if (data != null) {
			for (HashMap<String, String> hashMap : listData) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.jom_notification_item);
				ArrayList<Object> values = new ArrayList<Object>();
				values.add(hashMap);
				item.setValues(values);
				listItemData.add(item);
			}
		}
		return listItemData;
	}

	/**
	 * List adapter for notification.
	 */

	private SmartListAdapterWithHolder getListAdapter(ArrayList<SmartListItem> listData, final int type) {
		final AQuery androidAQuery = new AQuery(this);
		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.jom_notification_item, listData, new ItemView() {

			@SuppressWarnings("unchecked")
			@Override
			public View setItemView(final int position, View v, final SmartListItem item, ViewHolder holder) {

				final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);
				holder.lnrFriendRequest = (LinearLayout) v.findViewById(R.id.lnrFriendRequest);
				holder.imgFriendRequestUser = (ImageView) v.findViewById(R.id.imgFriendRequestUser);
				holder.txtFriendRequestUserName = (IjoomerTextView) v.findViewById(R.id.txtFriendRequestUserName);
				holder.txtFriendRequestMessage = (IjoomerTextView) v.findViewById(R.id.txtFriendRequestMessage);
				holder.btnFriendRequestAccept = (IjoomerButton) v.findViewById(R.id.btnFriendRequestAccept);
				holder.btnFriendRequestReject = (IjoomerButton) v.findViewById(R.id.btnFriendRequestReject);

				holder.lnrNotificationMessage = (LinearLayout) v.findViewById(R.id.lnrNotificationMessage);
				holder.imgNotificationMessageUser = (ImageView) v.findViewById(R.id.imgNotificationMessageUser);
				holder.txtNotificationMessageUserName = (IjoomerTextView) v.findViewById(R.id.txtNotificationMessageUserName);
				holder.txtNotificationMessageMessage = (IjoomerTextView) v.findViewById(R.id.txtNotificationMessageMessage);
				holder.txtNotificationMessageDate = (IjoomerTextView) v.findViewById(R.id.txtNotificationMessageDate);

				holder.lnrGlobal = (LinearLayout) v.findViewById(R.id.lnrGlobal);
				holder.imgGlobalNotificationMessageUser = (ImageView) v.findViewById(R.id.imgGlobalNotificationMessageUser);
				holder.txtGlobalNotificationMessagetitle = (IjoomerTextView) v.findViewById(R.id.txtGlobalNotificationMessagetitle);
				holder.txtGlobalNotificationMessageDate = (IjoomerTextView) v.findViewById(R.id.txtGlobalNotificationMessageDate);

				if (type == POPUP_FRIEND_REQUEST) {
					holder.lnrFriendRequest.setVisibility(View.VISIBLE);
					holder.lnrNotificationMessage.setVisibility(View.GONE);
					holder.lnrGlobal.setVisibility(View.GONE);

					androidAQuery.id(holder.imgFriendRequestUser).image(row.get(USER_AVATAR), true, true);
					holder.txtFriendRequestUserName.setText(row.get(USER_NAME));
					holder.txtFriendRequestMessage.setText(row.get(MESSAGE));

					holder.btnFriendRequestAccept.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							final SeekBar seekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
							provider.approveFriendRequest(row.get(CONNECTION_ID), new WebCallListener() {

								@Override
								public void onProgressUpdate(int progressCount) {
									seekBar.setProgress(progressCount);
								}

								@Override
								public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
									if (responseCode == 200) {
										IjoomerApplicationConfiguration.setReloadRequired(true);
										updateHeader(provider.getNotificationData());
										listAdapterWithHolder.remove(listAdapterWithHolder.getItem(position));
									}
								}
							});
						}
					});

					holder.btnFriendRequestReject.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							final SeekBar seekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
							provider.rejectFriendRequest(row.get(CONNECTION_ID), new WebCallListener() {

								@Override
								public void onProgressUpdate(int progressCount) {
									seekBar.setProgress(progressCount);
								}

								@Override
								public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
									if (responseCode == 200) {
										updateHeader(provider.getNotificationData());
										listAdapterWithHolder.remove(listAdapterWithHolder.getItem(position));
									}
								}
							});
						}
					});
					holder.imgFriendRequestUser.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							gotoProfile(row.get(USER_ID));
						}
					});

				} else if (type == POPUP_GLOBAL) {
					holder.lnrFriendRequest.setVisibility(View.GONE);
					holder.lnrNotificationMessage.setVisibility(View.GONE);
					holder.lnrGlobal.setVisibility(View.VISIBLE);

					if(row.containsKey(USER_AVATAR)){
						androidAQuery.id(holder.imgGlobalNotificationMessageUser).image(row.get(USER_AVATAR), true, true);
					}else{
						androidAQuery.id(holder.imgGlobalNotificationMessageUser).image(row.get(AVATAR), true, true);
					}

					holder.txtGlobalNotificationMessagetitle.setText(row.get(NOTIF_TITLE));
					holder.txtGlobalNotificationMessageDate.setText(row.get(DATE));

					holder.lnrGlobal.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (row.get(TYPE).equals(EVENTS)) {
								if (IjoomerGlobalConfiguration.getJomsocialVersion().equals(IjoomerGlobalConfiguration.JOMVERSION_V30)) {
									try {
										loadNew(JomEventDetailsActivity_v30.class, JomMasterActivity.this, false, "IN_USERID", "0", "IN_EVENT_DETAILS", row, "IN_GROUP_ID",
												row.containsKey(GROUPID) ? row.get(GROUPID) : "0");
									} catch (Throwable e) {
										e.printStackTrace();
									}
								} else {
									try {
										loadNew(JomEventDetailsActivity.class, JomMasterActivity.this, false, "IN_USERID", "0", "IN_EVENT_DETAILS", row, "IN_GROUP_ID",
												row.containsKey(GROUPID) ? row.get(GROUPID) : "0");
									} catch (Throwable e) {
										e.printStackTrace();
									}
								}
							} else if (row.get(TYPE).equals(GROUPS)) {
								if (IjoomerGlobalConfiguration.getJomsocialVersion().equals(IjoomerGlobalConfiguration.JOMVERSION_V30)) {
									try {
										loadNew(JomGroupDetailsActivity_v30.class, JomMasterActivity.this, false, "IN_USERID", "0", "IN_GROUP_DETAILS", row);
									} catch (Throwable e) {
										e.printStackTrace();
									}
								} else {
									try {
										loadNew(JomGroupDetailsActivity.class, JomMasterActivity.this, false, "IN_USERID", "0", "IN_GROUP_DETAILS", row);
									} catch (Throwable e) {
										e.printStackTrace();
									}
								}
							} else if (row.get(TYPE).equals(VIDEO)) {
								try {
									loadNew(JomVideoDetailsActivity.class, JomMasterActivity.this, false, "IN_USERID", "0", "IN_VIDEO_DETAILS", row, "IN_GROUP_ID",
											row.containsKey(GROUPID) ? row.get(GROUPID) : "0");
								} catch (Throwable e) {
									e.printStackTrace();
								}
							} else if (row.get(TYPE).equals(PHOTOS)) {

							} else if (row.get(TYPE).equals(PROFILE)) {
								gotoProfile(row.get(USER_ID));
							}else if(row.get(TYPE).equals(MESSAGE)){
								try {
									loadNew(JomMessageDetailsActivity.class, JomMasterActivity.this, false, "IN_MESSAGE_DETAILS", row);
								} catch (Throwable e) {
									e.printStackTrace();
								}
							}
						}
					});

				} else if (type == POPUP_MESSAGES) {
					holder.lnrFriendRequest.setVisibility(View.GONE);
					holder.lnrNotificationMessage.setVisibility(View.VISIBLE);
					holder.lnrGlobal.setVisibility(View.GONE);

					androidAQuery.id(holder.imgNotificationMessageUser).image(row.get(USER_AVATAR), true, true);
					holder.txtNotificationMessageUserName.setText(row.get(USER_NAME));
					holder.txtNotificationMessageMessage.setText(row.get(SUBJECT));
					holder.txtNotificationMessageDate.setText(row.get(DATE));

					holder.lnrNotificationMessage.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							try {
								loadNew(JomMessageDetailsActivity.class, JomMasterActivity.this, false, "IN_MESSAGE_DETAILS", row);
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					});

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


	/**
	 * This method used to get voice path from voice data.
	 * @param strData represented voice data
	 * @return represented {@link String}
	 */
	public String getAudio(String strData) {

		if (strData.contains("{voice}")) {
			strData = strData.substring(strData.indexOf("}"), strData.length());
			strData = strData.substring(1, strData.indexOf("{"));
			strData = strData.split("&")[0];
			return strData;
		}
		return null;
	}


	/**
	 * This method used to get simple text from voice data.
	 * @param strData represented voice data
	 * @return represented {@link String}
	 */
	public String getPlainText(String strData) {

		if (strData.contains("{voice}")) {
			strData = strData.substring(0, strData.indexOf("{voice}"));
		}
		return strData;
	}

	/**
	 * This method used to get voice length from voice data. 
	 * @param strData represented voice data
	 * @return represented {@link String}
	 */
	public String getAudioLength(String strData) {

		try {
			if (strData.contains("{voice}")) {
				strData = strData.substring(strData.indexOf("}"), strData.length());
				strData = strData.substring(1, strData.indexOf("{"));
				strData = strData.split("&")[1];
				return strData;
			}
		} catch (Exception e) {
		}
		return "0";
	}


	/**
	 * This method used to voice report.
	 * @param voiceFilePath represented voice path
	 */
	public void reportVoice(final String voiceFilePath){
		IjoomerUtilities.getReportDialog(new ReportListner() {

			@Override
			public void onClick(String repotType, String message) {
				final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
				new JomReportVoiceDataProvider(JomMasterActivity.this).reportGroupOrDiscussion(voiceFilePath, repotType + " " + message, new WebCallListener() {

					@Override
					public void onProgressUpdate(int progressCount) {
						proSeekBar.setProgress(progressCount);
					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						if (responseCode == 200) {
							IjoomerUtilities.getCustomOkDialog(getString(R.string.voice_report), getString(R.string.report_successfully), getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

								@Override
								public void NeutralMethod() {

								}
							});
						} else {
							IjoomerUtilities.getCustomOkDialog(getString(R.string.voice_report), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

								@Override
								public void NeutralMethod() {

								}
							});
						}
					}
				});
			}
		});
	}


}
