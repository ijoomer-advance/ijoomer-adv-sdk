package com.ijoomer.components.jomsocial;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ijoomer.common.classes.IjoomerFileChooserActivity;
import com.ijoomer.common.classes.IjoomerShareActivity;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.custom.interfaces.AnnouncementAndDiscussionListner;
import com.ijoomer.custom.interfaces.ReportListner;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.customviews.IjoomerVoiceAndTextMessager;
import com.ijoomer.customviews.IjoomerVoiceAndTextMessager.MessageHandler;
import com.ijoomer.library.jomsocial.JomGroupDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertMagnatic;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To JomGroupDiscussionDetailsActivity.
 * 
 * @author tasol
 * 
 */
public class JomGroupDiscussionDetailsActivity extends JomMasterActivity {

	private LinearLayout lnrGroupDiscussionHeader;
	private LinearLayout listFooter;
	private LinearLayout lnrDiscussionRepliesLocked;
	private LinearLayout lnrGroupDiscussionWriteReplies;
	private ListView lstGroupDiscussionReplies;
	private ListView lstGroupFiles;
	private IjoomerTextView txtGroupDiscussionEdit;
	private IjoomerTextView txtGroupDiscussionRemove;
	private IjoomerTextView txtGroupDiscussionLockUnlock;
	private IjoomerTextView txtGroupDiscussionsUploadFile;
	private IjoomerTextView txtDiscussionDetailsTitle;
	private IjoomerTextView txtDiscussionDetailsCreated;
	private IjoomerTextView txtDiscussionDetailsDescription;
	private IjoomerTextView txtDiscussionDetailsFiles;
	private IjoomerTextView txtDiscussionDetailsShare;
	private IjoomerTextView txtDiscussionDetailsReport;
	private IjoomerEditText edtDiscussionDetailsReplies;
	private IjoomerVoiceAndTextMessager voiceMessager;
	private ImageView imgDiscussionDetailsAvatar;
	private ImageView imgTagClose;
	private ViewGroup discussionDetailsHeaderLayout;
	private Dialog dialogAnnouncementOrDiscussion = null;
	private PopupWindow dialog;
	private ProgressBar pbrGroupFiles;

	private AQuery androidQuery;
	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();;
	private ArrayList<SmartListItem> fileListData = new ArrayList<SmartListItem>();
	private ArrayList<HashMap<String, String>> fileList;
	private HashMap<String, String> IN_DISCUSSION_DETAILS;
	private HashMap<String, String> IN_GROUP_DETAILS;
	private SmartListAdapterWithHolder replayListAdapter;
	private SmartListAdapterWithHolder fileListAdapter;

	private JomGroupDataProvider provider;
	private JomGroupDataProvider providerReplies;
	private JomGroupDataProvider fileDataProvider;

	final private int DOWNLOAD_FILE_LOCATION = 5;
	final private int UPLOAD_FILE_LOCATION = 4;
	private int downlodIndex = 0;
	private int editRepliesIndex = -1;

	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.jom_group_discussion_details;
	}

	@Override
	public void initComponents() {

		discussionDetailsHeaderLayout = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.jom_group_discussion_details_header, null);
		lstGroupDiscussionReplies = (ListView) findViewById(R.id.lstGroupDiscussionReplies);
		listFooter = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.ijoomer_list_footer, null);
		lstGroupDiscussionReplies.addHeaderView(discussionDetailsHeaderLayout);
		lstGroupDiscussionReplies.addFooterView(listFooter, null, false);
		lstGroupDiscussionReplies.setAdapter(null);
		lnrGroupDiscussionWriteReplies = (LinearLayout) findViewById(R.id.lnrGroupDiscussionWriteReplies);
		lnrDiscussionRepliesLocked = (LinearLayout) findViewById(R.id.lnrDiscussionRepliesLocked);
		lnrGroupDiscussionHeader = (LinearLayout) findViewById(R.id.lnrGroupDiscussionHeader);
		txtDiscussionDetailsTitle = (IjoomerTextView) discussionDetailsHeaderLayout.findViewById(R.id.txtDiscussionDetailsTitle);
		txtDiscussionDetailsCreated = (IjoomerTextView) discussionDetailsHeaderLayout.findViewById(R.id.txtDiscussionDetailsCreated);
		txtDiscussionDetailsDescription = (IjoomerTextView) discussionDetailsHeaderLayout.findViewById(R.id.txtDiscussionDetailsDescription);
		txtDiscussionDetailsFiles = (IjoomerTextView) discussionDetailsHeaderLayout.findViewById(R.id.txtDiscussionDetailsFiles);
		txtDiscussionDetailsShare = (IjoomerTextView) discussionDetailsHeaderLayout.findViewById(R.id.txtDiscussionDetailsShare);
		txtDiscussionDetailsReport = (IjoomerTextView) discussionDetailsHeaderLayout.findViewById(R.id.txtDiscussionDetailsReport);
		txtGroupDiscussionEdit = (IjoomerTextView) findViewById(R.id.txtGroupDiscussionEdit);
		txtGroupDiscussionRemove = (IjoomerTextView) findViewById(R.id.txtGroupDiscussionRemove);
		txtGroupDiscussionLockUnlock = (IjoomerTextView) findViewById(R.id.txtGroupDiscussionLockUnlock);
		txtGroupDiscussionsUploadFile = (IjoomerTextView) findViewById(R.id.txtGroupDiscussionsUploadFile);
		imgDiscussionDetailsAvatar = (ImageView) discussionDetailsHeaderLayout.findViewById(R.id.imgDiscussionDetailsAvatar);
		voiceMessager = (IjoomerVoiceAndTextMessager) findViewById(R.id.voiceMessager);

		androidQuery = new AQuery(this);
		provider = new JomGroupDataProvider(this);
		providerReplies = new JomGroupDataProvider(this);
		fileDataProvider = new JomGroupDataProvider(this);

		getIntentData();
		setDiscussionDetails();

	}

	@Override
	public void prepareViews() {
		if (Integer.parseInt(IN_DISCUSSION_DETAILS.get(TOPICS)) > 0) {
			getRepliesList();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

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
										IjoomerUtilities.getCustomOkDialog(getString(R.string.download), getString(R.string.alert_message_file_downloaded), getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

											@Override
											public void NeutralMethod() {
												MediaScannerConnection.scanFile(JomGroupDiscussionDetailsActivity.this, new String[] { path + fileName }, null, new OnScanCompletedListener() {

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
							IjoomerUtilities.getCustomOkDialog(getString(R.string.download), status.getMessage(), getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

								@Override
								public void NeutralMethod() {

								}
							});
						}
					}
				});
			} else if (requestCode == UPLOAD_FILE_LOCATION) {
				final String path = data.getStringExtra("IN_PATH");
				onResume();
				provider.uploadDiscussionFile(path, IN_DISCUSSION_DETAILS.get(ID), new WebCallListener() {
					final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.upload));

					@Override
					public void onProgressUpdate(int progressCount) {
						proSeekBar.setProgress(progressCount);
					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						if (responseCode == 200) {
							IjoomerUtilities.getCustomOkDialog(getString(R.string.upload_file), getString(R.string.alert_message_file_uploaded), getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

								@Override
								public void NeutralMethod() {
									IN_DISCUSSION_DETAILS.put(FILES, "" + (Integer.parseInt(IN_DISCUSSION_DETAILS.get(FILES)) + 1));
									txtDiscussionDetailsFiles.setText(Integer.parseInt(IN_DISCUSSION_DETAILS.get(FILES)) > 1 ? IN_DISCUSSION_DETAILS.get(FILES) + " " + getString(R.string.files) : IN_DISCUSSION_DETAILS.get(FILES) + " "
											+ getString(R.string.file));
								}
							});
						} else {
							responseErrorMessageHandler(responseCode, false);
						}
					}
				});
			}
		}
	}

	@Override
	public void setActionListeners() {

		voiceMessager.setMessageHandler(new MessageHandler() {

			@Override
			public void onVoiceMessageRecordingComplete(String message, String voiceMessagePath) {

			}

			@Override
			public void onToggle(int messager) {

			}

			@SuppressWarnings("unchecked")
			@Override
			public void onButtonSend(String message) {
				if (message.length() > 0) {
					if (editRepliesIndex == -1) {
						final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
						provider.addOrEditDiscussionReplies(IN_DISCUSSION_DETAILS.get(ID), "0", message, new WebCallListener() {

							@Override
							public void onProgressUpdate(int progressCount) {
								proSeekBar.setProgress(progressCount);
							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								if (responseCode == 200) {
									IjoomerApplicationConfiguration.setReloadRequired(true);
									updateHeader(provider.getNotificationData());
									providerReplies.restorePagingSettings();
									getRepliesList();
								} else {
									responseErrorMessageHandler(responseCode, false);
								}
							}
						});
					} else if (editRepliesIndex != -1 && !((HashMap<String, String>) listData.get(editRepliesIndex).getValues().get(0)).get(MESSAGE).equals(message)) {
						final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
						provider.addOrEditDiscussionReplies(IN_DISCUSSION_DETAILS.get(ID), ((HashMap<String, String>) listData.get(editRepliesIndex).getValues().get(0)).get(ID), message, new WebCallListener() {

							@Override
							public void onProgressUpdate(int progressCount) {
								proSeekBar.setProgress(progressCount);
							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								if (responseCode == 200) {
									IjoomerApplicationConfiguration.setReloadRequired(true);
									updateHeader(provider.getNotificationData());
									providerReplies.restorePagingSettings();
									getRepliesList();
								} else {
									responseErrorMessageHandler(responseCode, false);
								}
							}
						});
					} else {
						replayListAdapter.notifyDataSetChanged();
					}

				} else {
					edtDiscussionDetailsReplies.setError(getString(R.string.validation_value_required));
				}
				editRepliesIndex = -1;
			}
		});

		lstGroupDiscussionReplies.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 2) {
					if (!providerReplies.isCalling() && providerReplies.hasNextPage()) {
						listFooterVisible();
						providerReplies.getDiscussionReplayList(IN_DISCUSSION_DETAILS.get(ID), new WebCallListener() {

							@Override
							public void onProgressUpdate(int progressCount) {

							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								listFooterInvisible();
								if (responseCode == 200) {
									prepareListDiscussionReplies(data1, true);
								} else {
									responseErrorMessageHandler(responseCode, false);
								}
							}
						});
					}
				}
			}
		});

		txtDiscussionDetailsFiles.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!IN_DISCUSSION_DETAILS.get(FILES).equals("0")) {
					showFileDialog();
				}
			}
		});

		txtGroupDiscussionsUploadFile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(JomGroupDiscussionDetailsActivity.this, IjoomerFileChooserActivity.class);
				intent.putExtra("IN_ISOPENFILE", true);
				startActivityForResult(intent, UPLOAD_FILE_LOCATION);

			}
		});

		txtGroupDiscussionLockUnlock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				provider.lockUnlockDiscussion(IN_GROUP_DETAILS.get(ID), IN_DISCUSSION_DETAILS.get(ID), new WebCallListener() {
					final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

					@Override
					public void onProgressUpdate(int progressCount) {
						proSeekBar.setProgress(progressCount);
					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						if (responseCode == 200) {
							IjoomerApplicationConfiguration.setReloadRequired(true);
							if (IN_DISCUSSION_DETAILS.get(ISLOCKED).equals("1")) {
								lnrGroupDiscussionWriteReplies.setVisibility(View.VISIBLE);
								lnrDiscussionRepliesLocked.setVisibility(View.GONE);
								IN_DISCUSSION_DETAILS.put(ISLOCKED, "0");
								txtGroupDiscussionLockUnlock.setText(getString(R.string.lock));
								IjoomerUtilities.getCustomOkDialog(getString(R.string.group), getString(R.string.group_discussion_unlock_successfully), getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

									@Override
									public void NeutralMethod() {

									}
								});
							} else {
								IN_DISCUSSION_DETAILS.put(ISLOCKED, "1");
								lnrDiscussionRepliesLocked.setVisibility(View.VISIBLE);
								lnrGroupDiscussionWriteReplies.setVisibility(View.GONE);
								txtGroupDiscussionLockUnlock.setText(getString(R.string.unlock));
								IjoomerUtilities.getCustomOkDialog(getString(R.string.group), getString(R.string.group_discussion_lock_successfully), getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

									@Override
									public void NeutralMethod() {

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

		txtDiscussionDetailsReport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				IjoomerUtilities.getReportDialog(new ReportListner() {
					@Override
					public void onClick(String repotType, String message) {
						final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
						provider.reportGroupOrDiscussion(IN_GROUP_DETAILS.get(ID), IN_DISCUSSION_DETAILS.get(ID), repotType + "  " + message, new WebCallListener() {

							@Override
							public void onProgressUpdate(int progressCount) {
								proSeekBar.setProgress(progressCount);
							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								if (responseCode == 200) {
									updateHeader(provider.getNotificationData());
									IjoomerUtilities.getCustomOkDialog(getString(R.string.group), getString(R.string.report_successfully), getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

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

		txtGroupDiscussionEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialogAnnouncementOrDiscussion = getAnnouncementOrDiscussionCreateDialog(getString(R.string.group_discussion), IN_DISCUSSION_DETAILS.get(TITLE), IN_DISCUSSION_DETAILS.get(MESSAGE), IN_DISCUSSION_DETAILS.get(FILEPERMISSION),
						new AnnouncementAndDiscussionListner() {

							@Override
							public void onClick(final String title, final String message, final String allowMemberToUploadFile) {
								if (!IN_DISCUSSION_DETAILS.get(TITLE).equals(title) || !IN_DISCUSSION_DETAILS.get(MESSAGE).equals(message) || !IN_DISCUSSION_DETAILS.get(FILEPERMISSION).equals(allowMemberToUploadFile)) {
									provider.addOrEditGroupDiscussion(IN_GROUP_DETAILS.get(ID), IN_DISCUSSION_DETAILS.get(ID), title, message, allowMemberToUploadFile, new WebCallListener() {
										final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

										@Override
										public void onProgressUpdate(int progressCount) {
											proSeekBar.setProgress(progressCount);
										}

										@Override
										public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
											if (responseCode == 200) {
												dialogAnnouncementOrDiscussion.dismiss();
												IjoomerApplicationConfiguration.setReloadRequired(true);
												IN_DISCUSSION_DETAILS.put(TITLE, title);
												IN_DISCUSSION_DETAILS.put(MESSAGE, message);
												txtDiscussionDetailsTitle.setText(IN_DISCUSSION_DETAILS.get(TITLE));
												txtDiscussionDetailsDescription.setText(IN_DISCUSSION_DETAILS.get(MESSAGE));
											} else {
												responseErrorMessageHandler(responseCode, false);
											}
										}
									});
								}
							}
						});
			}
		});

		txtGroupDiscussionRemove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				IjoomerUtilities.getCustomConfirmDialog(getString(R.string.group_title_remove), getString(R.string.are_you_sure), getString(R.string.yes), getString(R.string.no), new CustomAlertMagnatic() {

					@Override
					public void PositiveMethod() {
						provider.removeDiscussion(IN_GROUP_DETAILS.get(ID), IN_DISCUSSION_DETAILS.get(ID), new WebCallListener() {
							final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

							@Override
							public void onProgressUpdate(int progressCount) {
								proSeekBar.setProgress(progressCount);
							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								if (responseCode == 200) {
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

		txtDiscussionDetailsShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					loadNew(IjoomerShareActivity.class, JomGroupDiscussionDetailsActivity.this, false, "IN_SHARE_CAPTION", IN_DISCUSSION_DETAILS.get(TITLE), "IN_SHARE_DESCRIPTION", IN_DISCUSSION_DETAILS.get(MESSAGE), "IN_SHARE_THUMB",
							IN_DISCUSSION_DETAILS.get(USER_AVATAR), "IN_SHARE_SHARELINK", IN_DISCUSSION_DETAILS.get(SHARELINK));
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});

		imgDiscussionDetailsAvatar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (IN_DISCUSSION_DETAILS.get(USER_PROFILE).equals("1")) {
					gotoProfile(IN_DISCUSSION_DETAILS.get(USER_ID));
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
		IN_DISCUSSION_DETAILS = (HashMap<String, String>) getIntent().getSerializableExtra("IN_DISCUSSION_DETAILS") == null ? new HashMap<String, String>() : (HashMap<String, String>) getIntent().getSerializableExtra("IN_DISCUSSION_DETAILS");
		IN_GROUP_DETAILS = (HashMap<String, String>) getIntent().getSerializableExtra("IN_GROUP_DETAILS") == null ? new HashMap<String, String>() : (HashMap<String, String>) getIntent().getSerializableExtra("IN_GROUP_DETAILS");
	}

	/**
	 * This method used to set discussion details.
	 */
	private void setDiscussionDetails() {
		androidQuery.id(imgDiscussionDetailsAvatar).image(IN_DISCUSSION_DETAILS.get(USER_AVATAR), true, true, getDeviceWidth(), 0);
		txtDiscussionDetailsTitle.setText(IN_DISCUSSION_DETAILS.get(TITLE));
		txtDiscussionDetailsCreated.setText(IN_DISCUSSION_DETAILS.get(DATE) + " " + String.format(getString(R.string.by), IN_DISCUSSION_DETAILS.get(USER_NAME)));
		txtDiscussionDetailsDescription.setText(IN_DISCUSSION_DETAILS.get(MESSAGE));
		txtDiscussionDetailsFiles.setText(Integer.parseInt(IN_DISCUSSION_DETAILS.get(FILES)) > 1 ? IN_DISCUSSION_DETAILS.get(FILES) + " " + getString(R.string.files) : IN_DISCUSSION_DETAILS.get(FILES) + " " + getString(R.string.file));

		if (IN_DISCUSSION_DETAILS.get(FILEPERMISSION).equals("1") || IN_GROUP_DETAILS.get(ISADMIN).equals("1") || IN_GROUP_DETAILS.get(ISCOMMUNITYADMIN).equals("1")) {
			txtGroupDiscussionsUploadFile.setVisibility(View.VISIBLE);
		}
		if (IN_GROUP_DETAILS.get(ISADMIN).equals("1") || IN_GROUP_DETAILS.get(ISCOMMUNITYADMIN).equals("1") || IN_DISCUSSION_DETAILS.get(USER_ID).equals("0")) {
			txtGroupDiscussionEdit.setVisibility(View.VISIBLE);
			txtGroupDiscussionRemove.setVisibility(View.VISIBLE);
			txtGroupDiscussionLockUnlock.setVisibility(View.VISIBLE);

			if (IN_DISCUSSION_DETAILS.get(ISLOCKED).equals("1")) {
				txtGroupDiscussionLockUnlock.setText(getString(R.string.unlock));
				lnrGroupDiscussionWriteReplies.setVisibility(View.GONE);
				lnrDiscussionRepliesLocked.setVisibility(View.VISIBLE);
			} else {
				txtGroupDiscussionLockUnlock.setText(getString(R.string.lock));
				lnrDiscussionRepliesLocked.setVisibility(View.GONE);
			}
		}
		if (txtGroupDiscussionEdit.getVisibility() == View.GONE && txtGroupDiscussionRemove.getVisibility() == View.GONE && txtGroupDiscussionsUploadFile.getVisibility() == View.GONE && txtGroupDiscussionLockUnlock.getVisibility() == View.GONE) {
			lnrGroupDiscussionHeader.setVisibility(View.GONE);
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
	 * This method used to shown response message.
	 * 
	 * @param responseCode
	 *            represented response code
	 * @param finishActivityOnConnectionProblem
	 *            represented finish activity on connection problem
	 */
	private void responseErrorMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {
		IjoomerUtilities.getCustomOkDialog(getString(R.string.group_discussion), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok), R.layout.ijoomer_ok_dialog,
				new CustomAlertNeutral() {

					@Override
					public void NeutralMethod() {
						
					}
				});
	}

	/**
	 * This method used to get replies list.
	 */
	private void getRepliesList() {

		providerReplies.getDiscussionReplayList(IN_DISCUSSION_DETAILS.get(ID), new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {

			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				if (responseCode == 200) {
					prepareListDiscussionReplies(data1, false);
					replayListAdapter = getDiscussionListAdapter();
					lstGroupDiscussionReplies.setAdapter(replayListAdapter);
				} else {
					if (responseCode != 204) {
						responseErrorMessageHandler(responseCode, true);
					}
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

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method used to load file list.
	 */
	private void loadFileList() {
		fileDataProvider.restorePagingSettings();
		pbrGroupFiles.setVisibility(View.VISIBLE);
		fileDataProvider.getDiscussionFileList(IN_DISCUSSION_DETAILS.get(ID), new WebCallListener() {

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
				} else {
					IjoomerUtilities.getCustomOkDialog(getString(R.string.group_files), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok), R.layout.ijoomer_ok_dialog,
							new CustomAlertNeutral() {

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
	 * This method used to prepare list for replies.
	 * 
	 * @param data
	 *            represented replies data
	 * @param append
	 *            represented data append
	 */
	public void prepareListDiscussionReplies(ArrayList<HashMap<String, String>> data, boolean append) {
		if (data != null) {
			if (!append) {
				listData.clear();
			}
			for (HashMap<String, String> hashMap : data) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.jom_group_discussion_replies_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hashMap);
				item.setValues(obj);
				if (append) {

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
	 * List adapter for replies.
	 */

	private SmartListAdapterWithHolder getDiscussionListAdapter() {
		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(JomGroupDiscussionDetailsActivity.this, R.layout.jom_group_discussion_replies_list_item, listData, new ItemView() {
			@Override
			public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {
				holder.imgDiscussionRepliesAvatar = (ImageView) v.findViewById(R.id.imgDiscussionRepliesAvatar);
				holder.txtDiscussionRepliesUser = (IjoomerTextView) v.findViewById(R.id.txtDiscussionRepliesUser);
				holder.txtDiscussionRepliesTitle = (IjoomerTextView) v.findViewById(R.id.txtDiscussionRepliesTitle);
				holder.txtDiscussionRepliesDate = (IjoomerTextView) v.findViewById(R.id.txtDiscussionRepliesDate);
				holder.txtDiscussionRepliesEdit = (IjoomerTextView) v.findViewById(R.id.txtDiscussionRepliesEdit);
				holder.imgDiscussionRepliesRemove = (ImageView) v.findViewById(R.id.imgDiscussionRepliesRemove);
				holder.txtDiscussionRepliesEdit.setVisibility(View.GONE);

				@SuppressWarnings("unchecked")
				final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);

				androidQuery.id(holder.imgDiscussionRepliesAvatar).image(row.get(USER_AVATAR), true, true, getDeviceWidth(), 0);
				holder.txtDiscussionRepliesUser.setText(row.get(USER_NAME));
				holder.txtDiscussionRepliesTitle.setText(row.get(MESSAGE));

				holder.txtDiscussionRepliesDate.setText(IjoomerUtilities.calculateTimesAgo(IjoomerUtilities.getMillisecondsTimeZone(Long.parseLong(row.get(TIMESTAMP)))));

				holder.txtDiscussionRepliesEdit.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						editRepliesIndex = position;
						holder.txtDiscussionRepliesEdit.setVisibility(View.GONE);
						voiceMessager.requestFocus();
						showSoftKeyboard();
						voiceMessager.setMessageString(holder.txtDiscussionRepliesTitle.getText().toString());
					}
				});

				if (row.get(DELETEALLOWED).equals("1")) {
					holder.imgDiscussionRepliesRemove.setVisibility(View.VISIBLE);
				} else {
					holder.imgDiscussionRepliesRemove.setVisibility(View.GONE);
				}

				if (row.get(USER_ID).equals("0")) {
					long minute = IjoomerUtilities.getDfferenceInMinute(IjoomerUtilities.getMillisecondsTimeZone(Long.parseLong(row.get(TIMESTAMP))));
					if (minute < 15) {
						holder.txtDiscussionRepliesEdit.setVisibility(View.VISIBLE);
					} else {
						holder.txtDiscussionRepliesEdit.setVisibility(View.GONE);
					}
				} else {
					holder.txtDiscussionRepliesEdit.setVisibility(View.GONE);
				}

				holder.imgDiscussionRepliesRemove.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						IjoomerUtilities.getCustomConfirmDialog(getString(R.string.wall_remove), getString(R.string.are_you_sure), getString(R.string.yes), getString(R.string.no), new CustomAlertMagnatic() {

							@Override
							public void PositiveMethod() {
								provider.removeDiscussionReplay(IN_GROUP_DETAILS.get(ID), IN_DISCUSSION_DETAILS.get(ID), row.get(ID), new WebCallListener() {
									final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

									@Override
									public void onProgressUpdate(int progressCount) {
										proSeekBar.setProgress(progressCount);
									}

									@Override
									public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
										if (responseCode == 200) {
											IjoomerApplicationConfiguration.setReloadRequired(true);
											replayListAdapter.remove(replayListAdapter.getItem(position));
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

				holder.imgDiscussionRepliesAvatar.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (row.get(USER_PROFILE).equals("1")) {
							gotoProfile(row.get(USER_ID));
						}
					}
				});

				holder.txtDiscussionRepliesUser.setOnClickListener(new OnClickListener() {

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

	/**
	 * List adapter for file.
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
						Intent intent = new Intent(JomGroupDiscussionDetailsActivity.this, IjoomerFileChooserActivity.class);
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
									IjoomerUtilities.getCustomOkDialog(getString(R.string.group_files), getString(R.string.file_removed_successfully), getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

										@Override
										public void NeutralMethod() {
											fileListAdapter.remove(fileListAdapter.getItem(position));
										}
									});
								} else {
									IjoomerUtilities.getCustomOkDialog(getString(R.string.group_files), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok), R.layout.ijoomer_ok_dialog,
											new CustomAlertNeutral() {

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
