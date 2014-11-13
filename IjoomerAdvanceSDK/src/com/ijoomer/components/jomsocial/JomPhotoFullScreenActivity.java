package com.ijoomer.components.jomsocial;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ijoomer.common.classes.IjoomerFileChooserActivity;
import com.ijoomer.common.classes.IjoomerShareActivity;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.custom.interfaces.ReportListner;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.customviews.IjoomerViewPager;
import com.ijoomer.customviews.IjoomerVoiceAndTextMessager;
import com.ijoomer.customviews.IjoomerVoiceAndTextMessager.MessageHandler;
import com.ijoomer.library.jomsocial.JomGalleryDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertMagnatic;
import com.smart.framework.CustomAlertNeutral;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To JomPhotoTagActivity.
 * 
 * @author tasol
 * 
 */
public class JomPhotoFullScreenActivity extends JomMasterActivity {

	private IjoomerTextView txtPhotoRemove;
	private IjoomerTextView txtPhotoDownload;
	private IjoomerTextView txtPhotoShare;
	private IjoomerTextView txtPhotoReport;
	private IjoomerTextView txtPhotoLikeCount;
	private IjoomerTextView txtPhotoDislikeCount;
	private IjoomerTextView txtPhotoCommentCount;
	private IjoomerVoiceAndTextMessager voiceMessager;
	private static FrameLayout frmTop;
	private static FrameLayout frmBottom;
	private IjoomerTextView txtPhotoTagCount;
	private IjoomerViewPager viewPager;
	private ProgressBar pbrPhotoViewPager;

	private ArrayList<HashMap<String, String>> IN_PHOTO_LIST;
	private HashMap<String, String> IN_ALBUM;
	private PageAdapter adapter;
	private JomGalleryDataProvider provider;

	final private int FILE_LOCATION = 5;
	public static int IN_TOTAL_COUNT;
	private String IN_USERID;
	private int IN_SELECTED_INDEX;
	private boolean comingFromActivities = false;

	private AQuery androidQuery;

	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.jom_photo_full_screen;
	}

	@Override
	public void initComponents() {

		txtPhotoRemove = (IjoomerTextView) findViewById(R.id.txtPhotoRemove);
		txtPhotoDownload = (IjoomerTextView) findViewById(R.id.txtPhotoDownload);
		txtPhotoReport = (IjoomerTextView) findViewById(R.id.txtPhotoReport);
		txtPhotoShare = (IjoomerTextView) findViewById(R.id.txtPhotoShare);
		txtPhotoLikeCount = (IjoomerTextView) findViewById(R.id.txtPhotoLikeCount);
		txtPhotoDislikeCount = (IjoomerTextView) findViewById(R.id.txtPhotoDislikeCount);
		txtPhotoCommentCount = (IjoomerTextView) findViewById(R.id.txtPhotoCommentCount);
		txtPhotoTagCount = (IjoomerTextView) findViewById(R.id.txtPhotoTagCount);
		voiceMessager = (IjoomerVoiceAndTextMessager) findViewById(R.id.voiceMessager);

		frmTop = (FrameLayout) findViewById(R.id.frmTop);
		frmBottom = (FrameLayout) findViewById(R.id.frmBottom);
		txtPhotoTagCount = (IjoomerTextView) findViewById(R.id.txtPhotoTagCount);

		viewPager = (IjoomerViewPager) findViewById(R.id.viewPager);
		pbrPhotoViewPager = (ProgressBar) findViewById(R.id.pbrPhotoViewPager);

		provider = new JomGalleryDataProvider(this);
		androidQuery = new AQuery(this);

		IN_TOTAL_COUNT = 0;
		getIntentData();
	}

	@Override
	public void prepareViews() {
		if (IN_TOTAL_COUNT != IN_PHOTO_LIST.size() && (IN_PHOTO_LIST.size() - IN_SELECTED_INDEX) <= 3) {
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
					} else {
						IjoomerUtilities.getCustomOkDialog(getString(R.string.photo), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())),
								getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

									@Override
									public void NeutralMethod() {

									}
								});
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
	public void setActionListeners() {

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
							IN_PHOTO_LIST.get(IN_SELECTED_INDEX).put(COMMENTCOUNT, String.valueOf(Integer.parseInt(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(COMMENTCOUNT)) + 1));
							txtPhotoCommentCount.setText(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(COMMENTCOUNT));
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
							IN_PHOTO_LIST.get(IN_SELECTED_INDEX).put(COMMENTCOUNT, String.valueOf(Integer.parseInt(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(COMMENTCOUNT)) + 1));
							txtPhotoCommentCount.setText(IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(COMMENTCOUNT));
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

		txtPhotoShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					loadNew(IjoomerShareActivity.class, JomPhotoFullScreenActivity.this, false, "IN_SHARE_CAPTION", IN_PHOTO_LIST.get(IN_SELECTED_INDEX).get(CAPTION),
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
				Intent intent = new Intent(JomPhotoFullScreenActivity.this, IjoomerFileChooserActivity.class);
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

		frmBottom.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

		frmTop.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

		txtPhotoTagCount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					loadNew(JomPhotoTagActivity.class, JomPhotoFullScreenActivity.this, false, "IN_PHOTO_DATA", IN_PHOTO_LIST.get(viewPager.getCurrentItem()));
				} catch (Exception e) {
				}
			}
		});
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int pos) {

				IN_SELECTED_INDEX = pos;
				setPhotoDetail(IN_SELECTED_INDEX);
				if (IN_PHOTO_LIST.size() < IN_TOTAL_COUNT) {
					loadPhotoList();
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				frmBottom.setVisibility(View.GONE);
				frmTop.setVisibility(View.GONE);
			}
		});

	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
	}

	@Override
	public int setHeaderLayoutId() {
		return 0;
	}

	@Override
	public int setFooterLayoutId() {
		return 0;
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

		if (IN_TOTAL_COUNT > IN_PHOTO_LIST.size() && IN_PHOTO_LIST.size() <= 5) {
			comingFromActivities = true;
		} else {
			comingFromActivities = false;
		}

		if (IN_ALBUM.get(DELETEALLOWED).equals("1")) {
			txtPhotoRemove.setVisibility(View.VISIBLE);
		}
		if (IN_ALBUM.get(DELETEALLOWED).equals("1") && IN_ALBUM.get(USER_ID).equals("0")) {
			txtPhotoRemove.setVisibility(View.VISIBLE);
		}
		if (IN_ALBUM.containsKey(EDITALBUM) && IN_ALBUM.get(EDITALBUM).equals("1") && IN_ALBUM.get(USER_ID).equals("0")) {
			txtPhotoRemove.setVisibility(View.VISIBLE);
		} else if (IN_ALBUM.containsKey(EDITALBUM) && IN_ALBUM.get(EDITALBUM).equals("1")) {
			txtPhotoRemove.setVisibility(View.VISIBLE);
		}
		setPhotoDetail(IN_SELECTED_INDEX);
	}

	/**
	 * This method used to get page no at run time.
	 * 
	 * @return represented {@link Integer}
	 */
	public int getPageNoCall() {
		if (IN_TOTAL_COUNT > IN_PHOTO_LIST.size()) {

			if (comingFromActivities) {
				return IN_PHOTO_LIST.size() / (12 + 1);
			} else {
				return IN_PHOTO_LIST.size() / 12 + 1;

			}
		}
		return 0;
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

				@Override
				public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
					if (responseCode == 200) {
						IN_PHOTO_LIST.addAll(data1);
					} else {
						IjoomerUtilities.getCustomOkDialog(getString(R.string.photo), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())),
								getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

									@Override
									public void NeutralMethod() {

									}
								});
					}
				}
			});
		}
	}

	/**
	 * Inner class
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
			System.gc();
			JomPhotoFullScreenFragment fragment = new JomPhotoFullScreenFragment(IN_PHOTO_LIST.get(pos));
			return fragment;
		}

		@Override
		public int getCount() {
			return IN_TOTAL_COUNT;
		}

	}

	public static void toggleOptions() {
		if (frmBottom.getVisibility() == View.VISIBLE) {
			frmBottom.setVisibility(View.GONE);
			frmTop.setVisibility(View.GONE);
		} else {
			frmBottom.setVisibility(View.VISIBLE);
			frmTop.setVisibility(View.VISIBLE);
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
		if (IN_PHOTO_LIST.get(index).containsKey(USER_ID) && IN_PHOTO_LIST.get(index).get(USER_ID).equals("0")) {
			txtPhotoRemove.setClickable(true);
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
		IjoomerUtilities.getCustomOkDialog(getString(R.string.photo), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())),
				getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

					@Override
					public void NeutralMethod() {
						
					}
				});
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
							IjoomerUtilities.getCustomOkDialog(getString(R.string.download), getString(R.string.alert_message_file_downloaded), getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

								@Override
								public void NeutralMethod() {
									MediaScannerConnection.scanFile(JomPhotoFullScreenActivity.this, new String[] { path + fileName }, null, new OnScanCompletedListener() {

										@Override
										public void onScanCompleted(String path, Uri uri) {

										}
									});
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
			}
		}
	}

}
