package com.ijoomer.components.jomsocial;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.ijoomer.custom.interfaces.CustomClickListner;
import com.ijoomer.custom.interfaces.ReportListner;
import com.ijoomer.customviews.IjoomerAudioPlayer.AudioListener;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerListView;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.customviews.IjoomerVoiceAndTextMessager;
import com.ijoomer.customviews.IjoomerVoiceAndTextMessager.MessageHandler;
import com.ijoomer.customviews.IjoomerVoiceButton;
import com.ijoomer.library.jomsocial.JomGalleryDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertMagnatic;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This Class Contains All Method Related To JomPhotoDetailsActivity.
 * 
 * @author tasol
 * 
 */
public class JomPhotoDetailsActivity extends JomMasterActivity {

	private LinearLayout listFooter;
	private IjoomerListView lstPhotoComment;
	private LinearLayout lnrPhotoHeader;
	private LinearLayout lnrPlayRecordComment;
	private IjoomerTextView txtPhotoAsCoverPage;
	private IjoomerTextView txtPhotAsProfilePicture;
	private IjoomerTextView txtPhotoCaption;
	private IjoomerTextView txtPhotoRemove;
	private IjoomerTextView txtPhotoDownload;
	private IjoomerTextView txtPhotoShare;
	private IjoomerTextView txtPhotoReport;
	private IjoomerTextView txtPhotoLikeCount;
	private IjoomerTextView txtPhotoDislikeCount;
	private IjoomerTextView txtPhotoCommentCount;
	private IjoomerTextView txtPhotoTagCount;
	private IjoomerTextView txtPhotoCurrentView;
	private IjoomerTextView txtTotalRecordComment;
	private IjoomerTextView txtRecordUser;
	private IjoomerButton btnPhotoCaptionEdit;
	private IjoomerVoiceButton btnPlayAll;
	private IjoomerVoiceAndTextMessager voiceMessager;
	private IjoomerVoiceAndTextMessager voiceMessagerPhotoCaption;
	private ProgressBar pbrPhotoViewPager;
	private ViewPager viewPager;
	private ViewGroup photoDeatilHeaderLayout;

	private AQuery androidQuery;
	private ArrayList<HashMap<String, String>> IN_PHOTO_LIST;
	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, ArrayList<HashMap<String, String>>> commentListStack = new HashMap<Integer, ArrayList<HashMap<String, String>>>();
	private HashMap<String, String> IN_ALBUM;
	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder commentAdapter;
	private PageAdapter adapter;

	private JomGalleryDataProvider commentProvider;
	private JomGalleryDataProvider provider;

	final private int FILE_LOCATION = 5;
	public static int IN_TOTAL_COUNT;
	public static boolean isSetCoverChanged;
	private String IN_USERID;
	private int IN_SELECTED_INDEX;
	private int recordCommentCounter;
	private int recordCommentTotal;
	private int recordCommentLast;
	private boolean isAddComment = false;

	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.jom_photo_details;
	}

	@Override
	public void initComponents() {

		photoDeatilHeaderLayout = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.jom_photo_details_header, null);
		listFooter = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.ijoomer_list_footer, null);
		lstPhotoComment = (IjoomerListView) findViewById(R.id.lstPhotoComment);
		lstPhotoComment.addHeaderView(photoDeatilHeaderLayout, null, false);
		lstPhotoComment.addFooterView(listFooter, null, false);
		lstPhotoComment.setAdapter(null);
		lnrPhotoHeader = (LinearLayout) findViewById(R.id.lnrPhotoHeader);
		lnrPlayRecordComment = (LinearLayout) photoDeatilHeaderLayout.findViewById(R.id.lnrPlayRecordComment);
		txtPhotoRemove = (IjoomerTextView) photoDeatilHeaderLayout.findViewById(R.id.txtPhotoRemove);
		txtPhotoDownload = (IjoomerTextView) photoDeatilHeaderLayout.findViewById(R.id.txtPhotoDownload);
		txtPhotoReport = (IjoomerTextView) photoDeatilHeaderLayout.findViewById(R.id.txtPhotoReport);
		txtPhotoShare = (IjoomerTextView) photoDeatilHeaderLayout.findViewById(R.id.txtPhotoShare);
		txtPhotoLikeCount = (IjoomerTextView) photoDeatilHeaderLayout.findViewById(R.id.txtPhotoLikeCount);
		txtPhotoDislikeCount = (IjoomerTextView) photoDeatilHeaderLayout.findViewById(R.id.txtPhotoDislikeCount);
		txtPhotoCommentCount = (IjoomerTextView) photoDeatilHeaderLayout.findViewById(R.id.txtPhotoCommentCount);
		txtPhotoTagCount = (IjoomerTextView) photoDeatilHeaderLayout.findViewById(R.id.txtPhotoTagCount);
		txtPhotoCaption = (IjoomerTextView) photoDeatilHeaderLayout.findViewById(R.id.txtPhotoCaption);
		txtPhotoAsCoverPage = (IjoomerTextView) findViewById(R.id.txtPhotoAsCoverPage);
		txtPhotAsProfilePicture = (IjoomerTextView) findViewById(R.id.txtPhotAsProfilePicture);
		txtPhotoCurrentView = (IjoomerTextView) photoDeatilHeaderLayout.findViewById(R.id.txtPhotoCurrentView);
		txtTotalRecordComment = (IjoomerTextView) photoDeatilHeaderLayout.findViewById(R.id.txtTotalRecordComment);
		txtRecordUser = (IjoomerTextView) photoDeatilHeaderLayout.findViewById(R.id.txtRecordUser);
		voiceMessager = (IjoomerVoiceAndTextMessager) findViewById(R.id.voiceMessager);
		voiceMessagerPhotoCaption = (IjoomerVoiceAndTextMessager) photoDeatilHeaderLayout.findViewById(R.id.voiceMessagerPhotoCaption);
		btnPhotoCaptionEdit = (IjoomerButton) photoDeatilHeaderLayout.findViewById(R.id.btnPhotoCaptionEdit);
		pbrPhotoViewPager = (ProgressBar) photoDeatilHeaderLayout.findViewById(R.id.pbrPhotoViewPager);
		btnPlayAll = (IjoomerVoiceButton) photoDeatilHeaderLayout.findViewById(R.id.btnPlayAll);
		btnPlayAll.setReportVoice(false);
		viewPager = (ViewPager) photoDeatilHeaderLayout.findViewById(R.id.viewPager);

		androidQuery = new AQuery(this);
		commentProvider = new JomGalleryDataProvider(this);
		provider = new JomGalleryDataProvider(JomPhotoDetailsActivity.this);
		IN_TOTAL_COUNT = 0;
		isSetCoverChanged = false;

		getIntentData();
	}

	@Override
	public void prepareViews() {
		pbrPhotoViewPager.setVisibility(View.GONE);
		if (IN_TOTAL_COUNT != IN_PHOTO_LIST.size() && (IN_PHOTO_LIST.size() - IN_SELECTED_INDEX) <= 2) {
			provider.restorePagingSettings();
			provider.setPageNo(getPageNoCall());
			provider.setPageLimit(12);
			pbrPhotoViewPager.setVisibility(View.VISIBLE);
			provider.getPhotoList(IN_ALBUM.get(ID), IN_USERID, new WebCallListener() {

				@Override
				public void onProgressUpdate(int progressCount) {

				}

				@Override
				public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
					if (responseCode == 200) {
						IN_PHOTO_LIST.addAll(data1);
						adapter = new PageAdapter(getSupportFragmentManager());
						viewPager.setAdapter(adapter);
						viewPager.setCurrentItem(IN_SELECTED_INDEX, true);
						pbrPhotoViewPager.setVisibility(View.GONE);
						updateHeader(provider.getNotificationData());
					} else {
						responseErrorMessageHandler(responseCode, false);
					}
				}
			});
		} else {
			adapter = new PageAdapter(getSupportFragmentManager());
			viewPager.setAdapter(adapter);
			viewPager.setCurrentItem(IN_SELECTED_INDEX, true);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == FILE_LOCATION) {
				final String path = data.getStringExtra("IN_PATH");
				final String fileName = IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(URL).split("/")[IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(URL).split("/").length - 1];
				androidQuery.download(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(URL), new File(path + fileName), new AjaxCallback<File>() {
					@Override
					public void callback(String url, File object, AjaxStatus status) {
						super.callback(url, object, status);
						if (status.getCode() == 200) {
							IjoomerUtilities.getCustomOkDialog(getString(R.string.download), getString(R.string.alert_message_file_downloaded), getString(R.string.ok),
									R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

										@Override
										public void NeutralMethod() {
											MediaScannerConnection.scanFile(JomPhotoDetailsActivity.this, new String[] { path + fileName }, null, new OnScanCompletedListener() {

												@Override
												public void onScanCompleted(String path, Uri uri) {

												}
											});
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
			}
		}
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
					recordCommentCounter = 0;
					recordCommentLast = 0;
					txtRecordUser.setVisibility(View.VISIBLE);
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

		photoDeatilHeaderLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});

		btnPhotoCaptionEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				txtPhotoCaption.setVisibility(View.GONE);
				btnPhotoCaptionEdit.setVisibility(View.GONE);
				voiceMessagerPhotoCaption.setVisibility(View.VISIBLE);
				voiceMessagerPhotoCaption.setMessageString(txtPhotoCaption.getText().toString());
				txtPhotoCurrentView.setVisibility(View.GONE);
			}
		});

		lstPhotoComment.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 2) {
					if (!commentProvider.isCalling() && commentProvider.hasNextPage()) {
						listFooterVisible();
						commentProvider.getPhotoCommentList(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(ID), new WebCallListener() {

							@Override
							public void onProgressUpdate(int progressCount) {

							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								listFooterInvisible();
								if (responseCode == 200) {
									updateHeader(commentProvider.getNotificationData());
									prepareList(data1, true);
								} else {
									responseErrorMessageHandler(responseCode, false);
								}
							}
						});
					}
				}
			}
		});

		txtPhotAsProfilePicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				provider.setAsProfileAvatar(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(ID), new WebCallListener() {
					final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

					@Override
					public void onProgressUpdate(int progressCount) {
						proSeekBar.setProgress(progressCount);
					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						if (responseCode == 200) {
							updateHeader(provider.getNotificationData());
							IjoomerUtilities.getCustomOkDialog(getString(R.string.photo), getString(R.string.photo_set_as_profile_avatar_successfully), getString(R.string.ok),
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

		txtPhotoAsCoverPage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				provider.setAsCoverPage(IN_ALBUM.get(ID), IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(ID), new WebCallListener() {
					final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

					@Override
					public void onProgressUpdate(int progressCount) {
						proSeekBar.setProgress(progressCount);
					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						if (responseCode == 200) {
							updateHeader(provider.getNotificationData());
							isSetCoverChanged = true;
							IjoomerApplicationConfiguration.setReloadRequired(true);
							IjoomerUtilities.getCustomOkDialog(getString(R.string.photo), getString(R.string.photo_set_as_cover_page_successfully), getString(R.string.ok),
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

		txtPhotoTagCount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					loadNew(JomPhotoTagActivity.class, JomPhotoDetailsActivity.this, false, "IN_PHOTO_DATA", IN_PHOTO_LIST.get(viewPager.getCurrentItem()));
				} catch (Exception e) {
				}
			}
		});

		txtPhotoRemove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				IjoomerUtilities.getCustomConfirmDialog(getString(R.string.album_title_remove), getString(R.string.are_you_sure), getString(R.string.yes), getString(R.string.no),
						new CustomAlertMagnatic() {

							@Override
							public void PositiveMethod() {
								pbrPhotoViewPager.setVisibility(View.VISIBLE);
								provider.removePhoto(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(ID), new WebCallListener() {

									@Override
									public void onProgressUpdate(int progressCount) {
									}

									@Override
									public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
										pbrPhotoViewPager.setVisibility(View.GONE);
										if (responseCode == 200) {
											updateHeader(provider.getNotificationData());
											IjoomerApplicationConfiguration.setReloadRequired(true);
											IN_TOTAL_COUNT = IN_TOTAL_COUNT - 1;
											IN_PHOTO_LIST.remove(IN_SELECTED_INDEX);
											if (IN_PHOTO_LIST.size() <= 0) {
												finish();
											} else {
												IN_SELECTED_INDEX = IN_SELECTED_INDEX - 1;
												if (IN_SELECTED_INDEX <= 0) {
													IN_SELECTED_INDEX = IN_SELECTED_INDEX + 1;
												} else {
													IN_SELECTED_INDEX = IN_SELECTED_INDEX - 1;
												}
												adapter.notifyDataSetChanged();
												viewPager.setCurrentItem(((IN_TOTAL_COUNT - 1) > IN_SELECTED_INDEX) ? IN_SELECTED_INDEX + 1 : IN_SELECTED_INDEX - 1, true);
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

		voiceMessagerPhotoCaption.setMessageHandler(new MessageHandler() {

			@Override
			public void onVoiceMessageRecordingComplete(final String message, String voiceMessagePath) {
				provider.setPhotoCaption(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(ID), message, voiceMessagePath, new WebCallListener() {
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
							try {
								IN_PHOTO_LIST.get(IN_SELECTED_INDEX).put(CAPTION, ((JSONObject) data2).getString("voice"));
							} catch (JSONException e) {
								e.printStackTrace();
							}
							txtPhotoCaption.setText(message);
							txtPhotoCaption.setVisibility(View.VISIBLE);
							btnPhotoCaptionEdit.setVisibility(View.VISIBLE);
							voiceMessagerPhotoCaption.setVisibility(View.GONE);
							txtPhotoCurrentView.setVisibility(View.VISIBLE);
							adapter.notifyDataSetChanged();
						} else {
							responseErrorMessageHandler(responseCode, false);
						}
					}
				});
			}

			@Override
			public void onButtonSend(final String message) {
				provider.setPhotoCaption(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(ID), message, null, new WebCallListener() {
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
							IN_PHOTO_LIST.get(IN_SELECTED_INDEX).put(CAPTION, message);
							txtPhotoCaption.setText(message);
							txtPhotoCaption.setVisibility(View.VISIBLE);
							btnPhotoCaptionEdit.setVisibility(View.VISIBLE);
							voiceMessagerPhotoCaption.setVisibility(View.GONE);
							txtPhotoCurrentView.setVisibility(View.VISIBLE);
							adapter.notifyDataSetChanged();
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

		voiceMessager.setMessageHandler(new MessageHandler() {

			@Override
			public void onVoiceMessageRecordingComplete(String message, String voiceMessagePath) {
				provider.addPhotoComment(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(ID), message, voiceMessagePath, new WebCallListener() {
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
							isAddComment = true;
							IN_PHOTO_LIST.get(IN_SELECTED_INDEX).put(COMMENTCOUNT, String.valueOf(Integer.parseInt(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(COMMENTCOUNT)) + 1));
							txtPhotoCommentCount.setText(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(COMMENTCOUNT));
							getComment();
						} else {
							responseErrorMessageHandler(responseCode, false);
						}
					}
				});
			}

			@Override
			public void onButtonSend(String message) {
				provider.addPhotoComment(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(ID), message, null, new WebCallListener() {
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
							isAddComment = true;
							IN_PHOTO_LIST.get(IN_SELECTED_INDEX).put(COMMENTCOUNT, String.valueOf(Integer.parseInt(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(COMMENTCOUNT)) + 1));
							txtPhotoCommentCount.setText(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(COMMENTCOUNT));
							getComment();
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
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int pos) {
				txtPhotoCaption.setVisibility(View.VISIBLE);
				voiceMessagerPhotoCaption.setVisibility(View.GONE);
				txtPhotoCurrentView.setVisibility(View.VISIBLE);
				IN_SELECTED_INDEX = pos;
				setPhotoDetail(IN_SELECTED_INDEX);

				if (IN_PHOTO_LIST.size() < IN_TOTAL_COUNT) {
					loadPhotoList();
				}

				getComment();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		txtPhotoShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					loadNew(IjoomerShareActivity.class, JomPhotoDetailsActivity.this, false, "IN_SHARE_CAPTION", IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(CAPTION),
							"IN_SHARE_DESCRIPTION", IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(CAPTION), "IN_SHARE_THUMB", IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(URL),
							"IN_SHARE_SHARELINK", IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(SHARELINK));
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});
		txtPhotoDownload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(JomPhotoDetailsActivity.this, IjoomerFileChooserActivity.class);
				startActivityForResult(intent, FILE_LOCATION);
			}
		});

		txtPhotoLikeCount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(LIKED).equals("1")) {
					txtPhotoLikeCount.setClickable(false);
					provider.unlikePhoto(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(ID), new WebCallListener() {

						@Override
						public void onProgressUpdate(int progressCount) {

						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								updateHeader(provider.getNotificationData());
								IjoomerApplicationConfiguration.setReloadRequired(true);
								IN_PHOTO_LIST.get(IN_SELECTED_INDEX).put(LIKED, "0");
								IN_PHOTO_LIST.get(IN_SELECTED_INDEX).put(LIKES, String.valueOf(Integer.parseInt(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(LIKES)) - 1));
								txtPhotoLikeCount.setText(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(LIKES));
							} else {
								responseErrorMessageHandler(responseCode, false);
							}
							txtPhotoLikeCount.setClickable(true);
						}
					});
				} else {
					txtPhotoLikeCount.setClickable(false);
					provider.likePhoto(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(ID), new WebCallListener() {

						@Override
						public void onProgressUpdate(int progressCount) {

						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								updateHeader(provider.getNotificationData());
								IjoomerApplicationConfiguration.setReloadRequired(true);
								IN_PHOTO_LIST.get(IN_SELECTED_INDEX).put(LIKED, "1");
								IN_PHOTO_LIST.get(IN_SELECTED_INDEX).put(LIKES, String.valueOf(Integer.parseInt(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(LIKES)) + 1));
								txtPhotoLikeCount.setText(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(LIKES));
								if (IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(DISLIKED).equals("1")) {
									IN_PHOTO_LIST.get(IN_SELECTED_INDEX).put(DISLIKES, String.valueOf(Integer.parseInt(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(DISLIKES)) - 1));
									IN_PHOTO_LIST.get(IN_SELECTED_INDEX).put(DISLIKED, "0");
									txtPhotoDislikeCount.setText(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(DISLIKES));
								}
							} else {
								responseErrorMessageHandler(responseCode, false);
							}
							txtPhotoLikeCount.setClickable(true);
						}
					});
				}
			}
		});

		txtPhotoDislikeCount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(DISLIKED).equals("1")) {
					txtPhotoDislikeCount.setClickable(false);
					provider.unlikePhoto(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(ID), new WebCallListener() {

						@Override
						public void onProgressUpdate(int progressCount) {

						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								updateHeader(provider.getNotificationData());
								IjoomerApplicationConfiguration.setReloadRequired(true);
								IN_PHOTO_LIST.get(IN_SELECTED_INDEX).put(DISLIKED, "0");
								IN_PHOTO_LIST.get(IN_SELECTED_INDEX).put(DISLIKES, String.valueOf(Integer.parseInt(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(DISLIKES)) - 1));
								txtPhotoDislikeCount.setText(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(DISLIKES));
							} else {
								responseErrorMessageHandler(responseCode, false);
							}
							txtPhotoDislikeCount.setClickable(true);
						}
					});
				} else {
					txtPhotoDislikeCount.setClickable(false);
					provider.dislikePhoto(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(ID), new WebCallListener() {

						@Override
						public void onProgressUpdate(int progressCount) {

						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								updateHeader(provider.getNotificationData());
								IjoomerApplicationConfiguration.setReloadRequired(true);
								IN_PHOTO_LIST.get(IN_SELECTED_INDEX).put(DISLIKED, "1");
								IN_PHOTO_LIST.get(IN_SELECTED_INDEX).put(DISLIKES, String.valueOf(Integer.parseInt(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(DISLIKES)) + 1));
								txtPhotoDislikeCount.setText(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(DISLIKES));
								if (IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(LIKED).equals("1")) {
									IN_PHOTO_LIST.get(IN_SELECTED_INDEX).put(LIKES, String.valueOf(Integer.parseInt(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(LIKES)) - 1));
									IN_PHOTO_LIST.get(IN_SELECTED_INDEX).put(LIKED, "0");
									txtPhotoLikeCount.setText(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(LIKES));
								}
							} else {
								responseErrorMessageHandler(responseCode, false);
							}
							txtPhotoDislikeCount.setClickable(true);
						}
					});
				}
			}
		});

		txtPhotoReport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				IjoomerUtilities.getReportDialog(new ReportListner() {

					@Override
					public void onClick(String repotType, String message) {
						final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
						provider.reportPhoto(IN_USERID, IN_ALBUM.get(ID), IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(ID), repotType + "  " + message, new WebCallListener() {

							@Override
							public void onProgressUpdate(int progressCount) {
								proSeekBar.setProgress(progressCount);
							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								if (responseCode == 200) {
									updateHeader(provider.getNotificationData());
									IjoomerUtilities.getCustomOkDialog(getString(R.string.photo), getString(R.string.report_successfully), getString(R.string.ok),
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
		IN_PHOTO_LIST = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("IN_PHOTO_LIST") == null ? new ArrayList<HashMap<String, String>>()
				: (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("IN_PHOTO_LIST");
		IN_ALBUM = (HashMap<String, String>) getIntent().getSerializableExtra("IN_ALBUM") == null ? new HashMap<String, String>() : (HashMap<String, String>) getIntent()
				.getSerializableExtra("IN_ALBUM");
		IN_USERID = getIntent().getStringExtra("IN_USERID") == null ? "0" : getIntent().getStringExtra("IN_USERID");
		IN_SELECTED_INDEX = getIntent().getIntExtra("IN_SELECTED_INDEX", 0);
		IN_TOTAL_COUNT = getIntent().getIntExtra("IN_TOTAL_COUNT", 1);

		if (IN_ALBUM.get(DELETEALLOWED).equals("1")) {
			lnrPhotoHeader.setVisibility(View.VISIBLE);
			txtPhotoRemove.setVisibility(View.VISIBLE);
			txtPhotAsProfilePicture.setVisibility(View.GONE);
		}
		if (IN_ALBUM.get(DELETEALLOWED).equals("1") && IN_ALBUM.get(USER_ID).equals("0")) {
			lnrPhotoHeader.setVisibility(View.VISIBLE);
			txtPhotoRemove.setVisibility(View.VISIBLE);
			txtPhotAsProfilePicture.setVisibility(View.VISIBLE);
			btnPhotoCaptionEdit.setVisibility(View.VISIBLE);
		}
		if (IN_ALBUM.containsKey(EDITALBUM) && IN_ALBUM.get(EDITALBUM).equals("1") && IN_ALBUM.get(USER_ID).equals("0")) {
			lnrPhotoHeader.setVisibility(View.VISIBLE);
			txtPhotoRemove.setVisibility(View.VISIBLE);
			txtPhotAsProfilePicture.setVisibility(View.VISIBLE);
		} else if (IN_ALBUM.containsKey(EDITALBUM) && IN_ALBUM.get(EDITALBUM).equals("1")) {
			lnrPhotoHeader.setVisibility(View.VISIBLE);
			txtPhotoRemove.setVisibility(View.VISIBLE);
			txtPhotAsProfilePicture.setVisibility(View.GONE);
		}

		setPhotoDetail(IN_SELECTED_INDEX);
		getComment();

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
		IjoomerUtilities.getCustomOkDialog(getString(R.string.photo), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())),
				getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

					@Override
					public void NeutralMethod() {
						
					}
				});
	}

	/**
	 * This method used to get photo comment.
	 */
	private void getComment() {

		if (!IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(COMMENTCOUNT).equals("0")) {

			if (commentListStack.containsKey(IN_SELECTED_INDEX) && commentListStack.get(IN_SELECTED_INDEX).size() > 0 && !isAddComment) {
				prepareList(commentListStack.get(IN_SELECTED_INDEX), false);
				commentAdapter = getListAdapter();
				lstPhotoComment.setAdapter(commentAdapter);
			} else {
				commentProvider.restorePagingSettings();
				commentProvider.getPhotoCommentList(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(ID), new WebCallListener() {

					@Override
					public void onProgressUpdate(int progressCount) {

					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						if (responseCode == 200) {
							updateHeader(commentProvider.getNotificationData());
							commentListStack.put(IN_SELECTED_INDEX, data1);
							prepareList(data1, false);
							commentAdapter = getListAdapter();
							lstPhotoComment.setAdapter(commentAdapter);
						} else {
							if (responseCode != 204) {
								responseErrorMessageHandler(responseCode, false);
							}
						}
					}
				});
			}

		} else {
			lstPhotoComment.setAdapter(null);
			lnrPlayRecordComment.setVisibility(View.GONE);
		}
	}

	/**
	 * This method used to set photo details.
	 * 
	 * @param index
	 *            represented phot index
	 */
	private void setPhotoDetail(int index) {
		txtPhotoLikeCount.setText(IN_PHOTO_LIST.get(index).get(LIKES));
		txtPhotoDislikeCount.setText(IN_PHOTO_LIST.get(index).get(DISLIKES));
		txtPhotoCommentCount.setText(IN_PHOTO_LIST.get(index).get(COMMENTCOUNT));
		txtPhotoTagCount.setText(IN_PHOTO_LIST.get(index).get(TAGS));
		txtPhotoCurrentView.setText(index + 1 + " " + getString(R.string.photo_of) + " " + IN_TOTAL_COUNT);
		if (IN_PHOTO_LIST.get(index).containsKey(USER_ID) && IN_PHOTO_LIST.get(index).get(USER_ID).equals("0")) {
			txtPhotAsProfilePicture.setClickable(true);
			txtPhotoAsCoverPage.setClickable(true);
			txtPhotoRemove.setClickable(true);
		}
		txtPhotoCaption.setText(getPlainText(IN_PHOTO_LIST.get(index).get(CAPTION)));
	}

	/**
	 * This method used to get page call at run time.
	 * 
	 * @return represented {@link Integer}
	 */
	public int getPageNoCall() {
		if (IN_TOTAL_COUNT > IN_PHOTO_LIST.size()) {
			return (IN_PHOTO_LIST.size() / 12) + 1;
		}
		return 0;
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
	 * This method used to load photo list.
	 */
	private void loadPhotoList() {
		if (!provider.isCalling()) {
			provider.restorePagingSettings();
			provider.setPageNo(getPageNoCall());
			provider.setPageLimit(12);
			provider.getPhotoList(IN_ALBUM.get(ID), IN_USERID, new WebCallListener() {

				@Override
				public void onProgressUpdate(int progressCount) {

				}

				@SuppressWarnings({ "rawtypes", "unchecked" })
				@Override
				public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
					if (responseCode == 200) {
						updateHeader(provider.getNotificationData());
						HashSet set = new HashSet();
						set.addAll(IN_PHOTO_LIST);
						set.addAll(data1);
						IN_PHOTO_LIST.clear();
						IN_PHOTO_LIST.addAll(set);
					} else {
						responseErrorMessageHandler(responseCode, false);
					}
				}
			});
		}
	}

	/**
	 * This method used to prepare list photo comment.
	 * 
	 * @param data
	 *            represented comment data
	 * @param append
	 *            represented data append
	 * @return represented {@link SmartListItem} list
	 */
	public ArrayList<SmartListItem> prepareList(ArrayList<HashMap<String, String>> data, boolean append) {
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

		return listData;
	}

	/**
	 * List adapter for photo comment.
	 */

	private SmartListAdapterWithHolder getListAdapter() {
		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(JomPhotoDetailsActivity.this, R.layout.jom_comment_list_item, listData, new ItemView() {
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

				if (row.containsKey(DELETEALLOWED) && row.get(DELETEALLOWED).equals("1")) {
					holder.btnCommentRemove.setVisibility(View.VISIBLE);
				}

				holder.btnCommentRemove.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						IjoomerUtilities.getCustomConfirmDialog(getString(R.string.video), getString(R.string.are_you_sure), getString(R.string.yes), getString(R.string.no),
								new CustomAlertMagnatic() {

									@Override
									public void PositiveMethod() {
										provider.removePhotoComment(row.get(ID), new WebCallListener() {
											final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

											@Override
											public void onProgressUpdate(int progressCount) {
												proSeekBar.setProgress(progressCount);
											}

											@Override
											public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
												if (responseCode == 200) {
													updateHeader(provider.getNotificationData());
													commentAdapter.remove(commentAdapter.getItem(position));
													if (getAudio(row.get(COMMENT)) != null) {
														recordCommentTotal -= 1;
														txtTotalRecordComment.setText("(" + recordCommentTotal + ")");
														if (recordCommentTotal == 1) {
															lnrPlayRecordComment.setVisibility(View.GONE);
														}
													}
													IN_PHOTO_LIST.get(IN_SELECTED_INDEX).put(COMMENTCOUNT,
															String.valueOf(Integer.parseInt(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(COMMENTCOUNT)) - 1));
													txtPhotoCommentCount.setText(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(COMMENTCOUNT));
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

	/**
	 * Inner Class
	 */

	private class PageAdapter extends FragmentStatePagerAdapter {

		public PageAdapter(FragmentManager fm) {
			super(fm);
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public Fragment getItem(int pos) {
			JomPhotoDetailFragment fragment = new JomPhotoDetailFragment(IN_PHOTO_LIST.get(pos).get(URL), IN_PHOTO_LIST.get(pos).get(CAPTION), new CustomClickListner() {

				@Override
				public void onClick(String value) {
					try {
						loadNew(JomPhotoFullScreenActivity.class, JomPhotoDetailsActivity.this, false, "IN_PHOTO_LIST", IN_PHOTO_LIST, "IN_SELECTED_INDEX", IN_SELECTED_INDEX,
								"IN_TOTAL_COUNT", IN_TOTAL_COUNT, "IN_ALBUM", IN_ALBUM);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			return fragment;
		}

		@Override
		public int getCount() {
			return IN_TOTAL_COUNT;
		}

	}

}
