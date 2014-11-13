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
import com.ijoomer.weservice.WebCallListenerWithCacheInfo;
import com.smart.framework.CustomAlertMagnatic;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

/**
 * This Class Contains All Method Related To JomActivitiesActivity.
 * 
 * @author tasol
 * 
 */
public class JomActivitiesActivity extends JomMasterActivity {

	private LinearLayout lnrWriteWhatsInYourMind;
	private ListView pullListView;
	private View listFooter;
	private IjoomerTextView txtRecentActivities;
	private IjoomerVoiceAndTextMessager voiceMessager;
	private SeekBar proSeekBar;

	private AQuery androidQuery;
	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder listAdapterWithHolder;

	private JomWallDataProvider wallDataProvider;

	private String IN_USERID;

	/**
	 * Overrides method
	 */
	@Override
	public int setLayoutId() {
		return R.layout.jom_activity_list;
	}

	@Override
	public void initComponents() {
		pullListView = (ListView) findViewById(R.id.pullListView);
		lnrWriteWhatsInYourMind = (LinearLayout) findViewById(R.id.lnrWriteWhatsInYourMind);
		lnrWriteWhatsInYourMind.setVisibility(View.VISIBLE);
		listFooter = LayoutInflater.from(this).inflate(R.layout.ijoomer_list_footer, null);
		pullListView.addFooterView(listFooter, null, false);
		voiceMessager = (IjoomerVoiceAndTextMessager) findViewById(R.id.voiceMessager);
		txtRecentActivities = (IjoomerTextView) findViewById(R.id.txtRecentActivities);
		txtRecentActivities.setVisibility(View.VISIBLE);

		androidQuery = new AQuery(this);
		wallDataProvider = new JomWallDataProvider(this);

		getIntentData();
	}

	@Override
	public void prepareViews() {
		getWall(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (IjoomerApplicationConfiguration.isReloadRequired) {
			IjoomerApplicationConfiguration.setReloadRequired(false);
			getWall(false);
		}
	}

	@Override
	public void setActionListeners() {

		//      pullListView.setOnRefreshListener(new IjoomerPullToRefreshListView.OnRefreshListener() {
		//          @Override
		//          public void onRefresh() {
		//              getWall(false);
		//          }
		//      });
		pullListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 1) {
					if (!wallDataProvider.isCalling() && wallDataProvider.hasNextPage()) {
						listFooterVisible();
						wallDataProvider.getWallList(IN_USERID, "activity", new WebCallListenerWithCacheInfo() {

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

		voiceMessager.setMessageHandler(new MessageHandler() {

			@Override
			public void onVoiceMessageRecordingComplete(String message, String voiceMessagePath) {
				wallDataProvider.addOrPostOnActivities(message, voiceMessagePath, new WebCallListener() {
					final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

					@Override
					public void onProgressUpdate(int progressCount) {
						if (progressCount < 95) {
							proSeekBar.setProgress(progressCount);
						}
					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						if (responseCode == 200) {
							updateHeader(wallDataProvider.getNotificationData());
							wallDataProvider.restorePagingSettings();
							wallDataProvider.getWallList(IN_USERID, "activity", new WebCallListenerWithCacheInfo() {

								@Override
								public void onProgressUpdate(int progressCount) {
									if (progressCount > 94) {
										proSeekBar.setProgress(progressCount);
									}
								}

								@Override
								public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2, int pageNo,
										int pageLimit, boolean fromCache) {
									listFooterInvisible();
									if (responseCode == 200) {
										txtRecentActivities.setText(getString(R.string.recent_activities));
										if (data1 != null) {
											updateHeader(wallDataProvider.getNotificationData());
											prepareList(data1, false, fromCache, pageNo, pageLimit);
											listAdapterWithHolder = getListAdapter();
											pullListView.setAdapter(listAdapterWithHolder);
											pullListView.setSelection(0);
										}
									} else {
										if (responseCode == 204) {
											txtRecentActivities.setText(getString(R.string.no_recent_activities));
										} else {
											responseErrorMessageHandler(responseCode, false);
										}
									}
								}
							});
						} else {
							responseErrorMessageHandler(responseCode, false);
						}

					}
				});
			}

			@Override
			public void onButtonSend(String message) {
				wallDataProvider.addOrPostOnActivities(message, null, new WebCallListener() {
					final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

					@Override
					public void onProgressUpdate(int progressCount) {
						if (progressCount < 95) {
							proSeekBar.setProgress(progressCount);
						}
					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						if (responseCode == 200) {
							updateHeader(wallDataProvider.getNotificationData());
							wallDataProvider.restorePagingSettings();
							wallDataProvider.getWallList(IN_USERID, "activity", new WebCallListenerWithCacheInfo() {

								@Override
								public void onProgressUpdate(int progressCount) {
									if (progressCount > 94) {
										proSeekBar.setProgress(progressCount);
									}
								}

								@Override
								public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2, int pageNo,
										int pageLimit, boolean fromCache) {
									listFooterInvisible();
									if (responseCode == 200) {
										txtRecentActivities.setText(getString(R.string.recent_activities));
										if (data1 != null) {
											updateHeader(wallDataProvider.getNotificationData());
											prepareList(data1, false, fromCache, pageNo, pageLimit);
											listAdapterWithHolder = getListAdapter();
											pullListView.setAdapter(listAdapterWithHolder);
											pullListView.setSelection(0);
										}
									} else {
										if (responseCode == 204) {
											txtRecentActivities.setText(getString(R.string.no_recent_activities));
										} else {
											responseErrorMessageHandler(responseCode, false);
										}
									}
								}
							});
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
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
	}

	@Override
	protected void onDestroy() {
		// ijoomerAudioPlayer.stopAudio();
		super.onDestroy();
	}

	/**
	 * class method
	 */

	/**
	 * This method used to get intent data.
	 */
	private void getIntentData() {
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
	 * This method used to get activity wall data.
	 * 
	 * @param isDialogShow
	 *            represented progress dialog shown
	 */
	private void getWall(final boolean isDialogShow) {
		wallDataProvider.restorePagingSettings();
		if (isDialogShow) {
			proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
		}
		wallDataProvider.getWallList(IN_USERID, "activity", new WebCallListenerWithCacheInfo() {

			@Override
			public void onProgressUpdate(int progressCount) {
				if (isDialogShow) {
					proSeekBar.setProgress(progressCount);
				}
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2, int pageNo, int pageLimit, boolean fromCache) {
				if (responseCode == 200) {
					txtRecentActivities.setText(getString(R.string.recent_activities));
					if (data1 != null) {
						if(!fromCache){
							updateHeader(wallDataProvider.getNotificationData());
						}
						prepareList(data1, false, fromCache, pageNo, pageLimit);
						listAdapterWithHolder = getListAdapter();
						pullListView.setAdapter(listAdapterWithHolder);
						pullListView.setSelection(0);
					}
				} else {
					if (responseCode == 204) {
						txtRecentActivities.setText(getString(R.string.no_recent_activities));
					} else {
						responseErrorMessageHandler(responseCode, isDialogShow);
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
		IjoomerUtilities.getCustomOkDialog(getString(R.string.activities), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())),
				getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

			@Override
			public void NeutralMethod() {

			}
		});
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
							loadNew(JomPhotoDetailsActivity.class, JomActivitiesActivity.this, false, 
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
								loadNew(JomPhotoDetailsActivity.class, JomActivitiesActivity.this, false, 
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
								loadNew(JomPhotoDetailsActivity.class, JomActivitiesActivity.this, false, "IN_PHOTO_LIST", photoData, "IN_SELECTED_INDEX",
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
	 * This method used to prepare list form activities wall data.
	 * 
	 * @param data
	 *            represented wall data
	 * @param append
	 *            represented is data append
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
	 * List adapter activities wall.
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
				holder.lnrWallOrActivityLikeCommnet = (LinearLayout) v.findViewById(R.id.lnrWallOrActivityLikeCommnet);
				holder.btnWallOrActivityRemove = (IjoomerButton) v.findViewById(R.id.btnWallOrActivityRemove);
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

				if (row.containsKey(DELETEALLOWED) && row.get(DELETEALLOWED).equals("1")) {
					holder.btnWallOrActivityRemove.setVisibility(View.VISIBLE);
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
						androidQuery.id(holder.imgWallOrActvityCoverPhoto).progress(R.id.coverimgprogress)
						.image(imageData.getString(URL),true,true);
					}catch (Throwable e){
						e.printStackTrace();
					}
				}else if(row.get(TYPE).toString().trim().equals(PHOTOS)) {
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

						LayoutInflater inflater = LayoutInflater.from(JomActivitiesActivity.this);
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
										loadNew(JomEventDetailsActivity_v30.class, JomActivitiesActivity.this, false, "IN_USERID", IN_USERID, "IN_EVENT_DETAILS", jsonToMap(jsonObject),
												"IN_GROUP_ID", IN_GROUP_ID);
									} catch (Throwable e) {
										e.printStackTrace();
									}
								} else {
									try {
										loadNew(JomEventDetailsActivity.class, JomActivitiesActivity.this, false, "IN_USERID", IN_USERID, "IN_EVENT_DETAILS", jsonToMap(jsonObject),
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

						JSONObject groupData = new JSONObject(row.get(CONTENT_DATA));

						LayoutInflater inflater = LayoutInflater.from(JomActivitiesActivity.this);
						View groupView = inflater.inflate(R.layout.jom_wall_activity_group_details_item, null);
						groupView.setTag(groupData);

						androidQuery.id(((ImageView) groupView.findViewById(R.id.imgGroupAvatar))).image(groupData.getString(AVATAR),true,true);
						((IjoomerTextView) groupView.findViewById(R.id.txtGroupTitle)).setText(groupData.getString(TITLE));
						((IjoomerTextView) groupView.findViewById(R.id.txtGroupDescription)).setText(groupData.getString(DESCRIPTION));

						groupView.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								JSONObject jsonObject = (JSONObject) v.getTag();
								if (IjoomerGlobalConfiguration.getJomsocialVersion().equals(IjoomerGlobalConfiguration.JOMVERSION_V30)) {
									try {
										loadNew(JomGroupDetailsActivity_v30.class, JomActivitiesActivity.this, false, "IN_USERID", IN_USERID, "IN_GROUP_DETAILS", jsonToMap(jsonObject));
									} catch (Throwable e) {
										e.printStackTrace();
									}
								} else {

									try {
										loadNew(JomGroupDetailsActivity.class, JomActivitiesActivity.this, false, "IN_USERID", IN_USERID, "IN_GROUP_DETAILS", jsonToMap(jsonObject));
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

						LayoutInflater inflater = LayoutInflater.from(JomActivitiesActivity.this);
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
										loadNew(JomGroupDiscussionDetailsActivity.class, JomActivitiesActivity.this, false, "IN_DISCUSSION_DETAILS", jsonToMap(jsonObjectDiscussion),
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

						LayoutInflater inflater = LayoutInflater.from(JomActivitiesActivity.this);
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
										loadNew(JomGroupAnnouncementDetailsActivity.class, JomActivitiesActivity.this, false, "IN_ANNOUCEMENT_DETAILS", jsonToMap(jsonObjectAnnouncement),
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

				if (row.get(LIKED).toString().trim().equals("1")) {
					holder.txtWallOrActivityLike.setText(getString(R.string.unlike));
				} else {
					holder.txtWallOrActivityLike.setText(getString(R.string.like));

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
								loadNew(JomWallOrActivityDetailActivity.class, JomActivitiesActivity.this, false, "IN_WALL_DETAILS", IN_WALL_DETAILS, "IN_WALL_DETAILS_LIST_TYPE",
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
								loadNew(JomWallOrActivityDetailActivity.class, JomActivitiesActivity.this, false, "IN_WALL_DETAILS", IN_WALL_DETAILS, "IN_WALL_DETAILS_LIST_TYPE",
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
						if (row.get(LIKETYPE).equals(VIDEOS)) {
							try {
								likeID = new JSONObject(row.get(CONTENT_DATA)).getString(ID);
							} catch (JSONException e) {
								e.printStackTrace();
								likeID = "0";
							}
						} else {
							likeID = row.get(ID);
						}
						if (row.get(LIKED).toString().equals("1")) {
							holder.txtWallOrActivityLike.setClickable(false);
							wallDataProvider.unlikeWall(likeID, row.get(LIKETYPE), new WebCallListener() {
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
							wallDataProvider.likeWall(likeID, row.get(LIKETYPE), new WebCallListener() {
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
							loadNew(JomWallOrActivityDetailActivity.class, JomActivitiesActivity.this, false, "IN_WALL_DETAILS", IN_WALL_DETAILS, "IN_WALL_DETAILS_LIST_TYPE",
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
							Intent lVideoIntent = new Intent(null,getVideoPlayURI(url), JomActivitiesActivity.this, IjoomerMediaPlayer.class);
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
