package com.ijoomer.components.jomsocial;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView.BufferType;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerShareActivity;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.jomsocial.JomGalleryDataProvider;
import com.ijoomer.media.player.IjoomerMediaPlayer;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Fragment Contains All Method Related To JomVideoSearchFragment.
 * 
 * @author tasol
 * 
 */
@SuppressLint("ValidFragment")
public class JomVideoSearchFragment extends SmartFragment implements JomTagHolder {

	private ListView lstSearchVideo;
	private LinearLayout listFooter;
	private SeekBar proSeekBar;

	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder adapterVideo;
	private JomGalleryDataProvider providerSearchVideo;
	private JomGalleryDataProvider provider;
	private AQuery androidQuery;

	private String IN_USERID;
	private String IN_GROUP_ID;
	private String serachKeyword;

	public JomVideoSearchFragment() {
	}

	public void setSerachKeyword(String serachKeyword) {
		this.serachKeyword = serachKeyword;
	}

	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.jom_video_search_fragment;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents(View currentView) {
		listFooter = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.ijoomer_list_footer, null);
		lstSearchVideo = (ListView) currentView.findViewById(R.id.lstSearchVideo);
		lstSearchVideo.addFooterView(listFooter, null, false);
		lstSearchVideo.setAdapter(null);

		androidQuery = new AQuery(getActivity());
		providerSearchVideo = new JomGalleryDataProvider(getActivity());

		getIntentData();
	}

	@Override
	public void prepareViews(View currentView) {
		if (adapterVideo == null) {
			getSearchVideo(true);
		} else {
			lstSearchVideo.setAdapter(adapterVideo);
			getSearchVideo(false);
		}

	}

	@Override
	public void setActionListeners(View currentView) {
		lstSearchVideo.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

				if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 1) {
					if (!providerSearchVideo.isCalling() && providerSearchVideo.hasNextPage()) {
						listFooterVisible();
						providerSearchVideo.getSearchVideo(serachKeyword, new WebCallListener() {

							@Override
							public void onProgressUpdate(int progressCount) {
							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								try {
									listFooterInvisible();
									if (responseCode == 200) {
										((JomMasterActivity) getActivity()).updateHeader(providerSearchVideo.getNotificationData());
										prepareList(data1, true);
									} else {
										responseErrorMessageHandler(responseCode, false);
									}
								} catch (Throwable e) {
								}

							}
						});
					}
				}
			}
		});
	}

	/**
	 * Class methods
	 */
	
	/**
	 * This method used to update fragment.
	 */
	public void update() {
		getSearchVideo(false);
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
	 * This method used to get intent data.
	 */
	private void getIntentData() {
		IN_USERID = getActivity().getIntent().getStringExtra("IN_USERID") == null ? "0" : getActivity().getIntent().getStringExtra("IN_USERID");
		IN_GROUP_ID = getActivity().getIntent().getStringExtra("IN_GROUP_ID") == null ? "0" : getActivity().getIntent().getStringExtra("IN_GROUP_ID");
	}

	/**
	 * This method used to get search video.
	 * @param isProgressShow represented progress shown
	 */
	private void getSearchVideo(final boolean isProgressShow) {
		providerSearchVideo.restorePagingSettings();
		if (isProgressShow) {
			proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
		}
		providerSearchVideo.getSearchVideo(serachKeyword, new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {
				if (isProgressShow) {
					proSeekBar.setProgress(progressCount);
				}
			}

			@Override
			public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				try {
					if (responseCode == 200) {
						((JomMasterActivity) getActivity()).updateHeader(providerSearchVideo.getNotificationData());
						prepareList(data1, false);
						lstSearchVideo.setAdapter(getListAdapter());
					} else {
						lstSearchVideo.setAdapter(null);
						responseErrorMessageHandler(responseCode, isProgressShow);
					}
				} catch (Throwable e) {
				}

			}
		});
	}
	
	/**
	 * This method used to shown response message.
	 * @param responseCode represented response code
	 * @param finishActivityOnConnectionProblem represented finish activity on connection problem 
	 */
	private void responseErrorMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {
		IjoomerUtilities.getCustomOkDialog(getString(R.string.video), getString(getResources().getIdentifier("code" + responseCode, "string", getActivity().getPackageName())), getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

			@Override
			public void NeutralMethod() {
				
			}
		});
	}

	/**
	 * This method used to prepare list search video.
	 * @param data represented video data
	 * @param append represented data append
	 */
	public void prepareList(ArrayList<HashMap<String, String>> data, boolean append) {

		if (data != null) {
			if (!append) {
				listData.clear();
			}
			for (HashMap<String, String> hashMap : data) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.jom_video_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hashMap);
				item.setValues(obj);
				if (append) {
					adapterVideo.add(item);
				} else {
					listData.add(item);
				}
			}

		}
	}

	/**
	 * List adapter for search video.
	 */
	private SmartListAdapterWithHolder getListAdapter() {

		adapterVideo = new SmartListAdapterWithHolder(getActivity(), R.layout.jom_video_list_item, listData, new ItemView() {

			@SuppressWarnings("unchecked")
			@Override
			public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {

				holder.imgVideoAvatar = (ImageView) v.findViewById(R.id.imgVideoAvatar);
				holder.imgVideoArrow = (ImageView) v.findViewById(R.id.imgVideoArrow);
				holder.txtVideoTitle = (IjoomerTextView) v.findViewById(R.id.txtVideoTitle);
				holder.txtVideoBy = (IjoomerTextView) v.findViewById(R.id.txtVideoBy);

				holder.txtVideoDateLocation = (IjoomerTextView) v.findViewById(R.id.txtVideoDateLocation);
				holder.txtVideoLikeCount = (IjoomerTextView) v.findViewById(R.id.txtVideoLikeCount);
				holder.txtVideoDislikeCount = (IjoomerTextView) v.findViewById(R.id.txtVideoDislikeCount);
				holder.txtVideoCommentCount = (IjoomerTextView) v.findViewById(R.id.txtVideoCommentCount);
				holder.txtVideoShare = (IjoomerTextView) v.findViewById(R.id.txtVideoShare);

				final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);

				androidQuery.id(holder.imgVideoAvatar).image(row.get(THUMB), true, true, ((SmartActivity) getActivity()).getDeviceWidth(), 0);

				holder.txtVideoTitle.setText(row.get(CAPTION));
				holder.txtVideoBy.setText(row.get(USER_NAME));
				holder.txtVideoLikeCount.setText(row.get(LIKES));
				holder.txtVideoDislikeCount.setText(row.get(DISLIKES));
				holder.txtVideoCommentCount.setText(row.get(COMMENTCOUNT));

				holder.txtVideoBy.setMovementMethod(LinkMovementMethod.getInstance());
				holder.txtVideoBy.setText(((JomMasterActivity) getActivity()).addClickablePart(Html.fromHtml(row.get(USER_NAME)), row), BufferType.SPANNABLE);

				holder.txtVideoDateLocation.setMovementMethod(new ScrollingMovementMethod());
				if (row.get(LOCATION).trim().length() <= 0) {
					holder.txtVideoDateLocation.setText(row.get(DATE));
				} else {
					holder.txtVideoDateLocation.setText(row.get(DATE) + " @ " + row.get(LOCATION));
				}

				holder.imgVideoAvatar.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (!row.get(URL).toString().contains("youtube")) {
							Intent lVideoIntent = new Intent(null, Uri.parse("mp4://" + row.get(URL).toString()), getActivity(), IjoomerMediaPlayer.class);
							JomVideoActivity.isVideoPlay = true;
							JomVideoActivity.currentVideo = row;
							startActivity(lVideoIntent);

						} else {
							JomVideoActivity.isVideoPlay = true;
							JomVideoActivity.currentVideo = row;
							Intent lVideoIntent = new Intent(null, Uri.parse("ytv://" + row.get(URL).toString().split("=")[1] + ""), getActivity(), IjoomerMediaPlayer.class);
							startActivity(lVideoIntent);

						}

					}
				});
				holder.imgVideoArrow.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							((SmartActivity) getActivity()).loadNew(JomVideoDetailsActivity.class, getActivity(), false, "IN_USERID", IN_USERID, "IN_VIDEO_DETAILS", row, "IN_GROUPID", IN_GROUP_ID);
						} catch (Throwable e) {
							e.printStackTrace();
						}

					}
				});
				holder.txtVideoTitle.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							((SmartActivity) getActivity()).loadNew(JomVideoDetailsActivity.class, getActivity(), false, "IN_USERID", IN_USERID, "IN_VIDEO_DETAILS", row, "IN_GROUPID", IN_GROUP_ID);
						} catch (Throwable e) {
							e.printStackTrace();
						}

					}
				});
				holder.txtVideoLikeCount.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if (row.get(LIKED).equals("1")) {
							holder.txtVideoLikeCount.setClickable(false);
							provider.unlikeVideo(row.get(ID), new WebCallListener() {

								@Override
								public void onProgressUpdate(int progressCount) {

								}

								@Override
								public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
									if (responseCode == 200) {
										((JomMasterActivity) getActivity()).updateHeader(provider.getNotificationData());
										row.put(LIKED, "0");
										row.put(LIKES, String.valueOf(Integer.parseInt(row.get(LIKES)) - 1));
										holder.txtVideoLikeCount.setText(row.get(LIKES));
									} else {
										responseErrorMessageHandler(responseCode, false);
									}
									holder.txtVideoLikeCount.setClickable(true);
								}
							});
						} else {
							holder.txtVideoLikeCount.setClickable(false);
							provider.likeVideo(row.get(ID), new WebCallListener() {

								@Override
								public void onProgressUpdate(int progressCount) {

								}

								@Override
								public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
									if (responseCode == 200) {
										((JomMasterActivity) getActivity()).updateHeader(provider.getNotificationData());
										row.put(LIKED, "1");
										row.put(LIKES, String.valueOf(Integer.parseInt(row.get(LIKES)) + 1));
										holder.txtVideoLikeCount.setText(row.get(LIKES));
										if (row.get(DISLIKED).equals("1")) {
											row.put(DISLIKES, String.valueOf(Integer.parseInt(row.get(DISLIKES)) - 1));
											row.put(DISLIKED, "0");
											holder.txtVideoDislikeCount.setText(row.get(DISLIKES));
										}
									} else {
										responseErrorMessageHandler(responseCode, false);
									}
									holder.txtVideoLikeCount.setClickable(true);
								}
							});
						}
					}
				});

				holder.txtVideoDislikeCount.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						if (row.get(DISLIKED).equals("1")) {
							holder.txtVideoDislikeCount.setClickable(false);
							provider.unlikeVideo(row.get(ID), new WebCallListener() {

								@Override
								public void onProgressUpdate(int progressCount) {

								}

								@Override
								public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
									if (responseCode == 200) {
										((JomMasterActivity) getActivity()).updateHeader(provider.getNotificationData());
										row.put(DISLIKED, "0");
										row.put(DISLIKES, String.valueOf(Integer.parseInt(row.get(DISLIKES)) - 1));
										holder.txtVideoDislikeCount.setText(row.get(DISLIKES));
									} else {
										responseErrorMessageHandler(responseCode, false);
									}
									holder.txtVideoDislikeCount.setClickable(true);
								}
							});
						} else {
							holder.txtVideoDislikeCount.setClickable(false);
							provider.dislikeVideo(row.get(ID), new WebCallListener() {

								@Override
								public void onProgressUpdate(int progressCount) {

								}

								@Override
								public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
									if (responseCode == 200) {
										((JomMasterActivity) getActivity()).updateHeader(provider.getNotificationData());
										row.put(DISLIKED, "1");
										row.put(DISLIKES, String.valueOf(Integer.parseInt(row.get(DISLIKES)) + 1));
										holder.txtVideoDislikeCount.setText(row.get(DISLIKES));
										if (row.get(LIKED).equals("1")) {
											row.put(LIKES, String.valueOf(Integer.parseInt(row.get(LIKES)) - 1));
											row.put(LIKED, "0");
											holder.txtVideoLikeCount.setText(row.get(LIKES));
										}
									} else {
										responseErrorMessageHandler(responseCode, false);
									}
									holder.txtVideoDislikeCount.setClickable(true);
								}
							});
						}
					}
				});

				holder.txtVideoCommentCount.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						try {
							((SmartActivity) getActivity()).loadNew(JomVideoDetailsActivity.class, getActivity(), false, "IN_USERID", IN_USERID, "IN_VIDEO_DETAILS", row, "IN_GROUPID", IN_GROUP_ID);
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				});
				holder.txtVideoShare.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						try {
							((SmartActivity) getActivity()).loadNew(IjoomerShareActivity.class, getActivity(), false, "IN_SHARE_CAPTION", row.get(CAPTION), "IN_SHARE_DESCRIPTION", row.get(DESCRIPTION), "IN_SHARE_THUMB", row.get(THUMB), "IN_SHARE_SHARELINK", row.get(SHARELINK));
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
		return adapterVideo;

	}

}
