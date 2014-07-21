package com.ijoomer.components.jomsocial;

import android.annotation.SuppressLint;
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
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.jomsocial.JomGalleryDataProvider;
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
 * This Fragment Contains All Method Related To JomAlbumAllFragment.
 * 
 * @author tasol
 * 
 */
@SuppressLint("ValidFragment")
public class JomAlbumAllFragment extends SmartFragment implements JomTagHolder {

	private ListView lstAllAlbum;
	private LinearLayout listFooter;

	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder adapterAlbum;
	private JomGalleryDataProvider providerAllAlbum;
	private AQuery androidQuery;
	private SeekBar proSeekBar;

	private String IN_USERID;
	private String IN_GROUP_ID;
	private String IN_PROFILE_COVER;
	private String IN_GROUP_ADD_ALBUM;
	private String IN_GROUP_UPLOAD_PHOTO;
	private JomAlbumAddFragment addAlbumFragment;

	public JomAlbumAllFragment() {
	}

	/**
	 * Overrides method
	 */

	@Override
	public int setLayoutId() {
		return R.layout.jom_album_all_fragment;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents(View currentView) {
		listFooter = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.ijoomer_list_footer, null);
		lstAllAlbum = (ListView) currentView.findViewById(R.id.lstAllAlbum);
		lstAllAlbum.addFooterView(listFooter, null, false);
		lstAllAlbum.setAdapter(null);

		androidQuery = new AQuery(getActivity());
		providerAllAlbum = new JomGalleryDataProvider(getActivity());

		getIntentData();
	}

	@Override
	public void prepareViews(View currentView) {
		if (adapterAlbum == null) {
			getAllPhotos(true);
		} else {
			lstAllAlbum.setAdapter(adapterAlbum);
			getAllPhotos(false);
		}

	}

	@Override
	public void setActionListeners(View currentView) {
		lstAllAlbum.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

				if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 1) {
					if (!providerAllAlbum.isCalling() && providerAllAlbum.hasNextPage()) {
						listFooterVisible();
						providerAllAlbum.getAllAlbumList(IN_GROUP_ID, new WebCallListener() {

							@Override
							public void onProgressUpdate(int progressCount) {
							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								try {
									listFooterInvisible();
									if (responseCode == 200) {
										((JomMasterActivity) getActivity()).updateHeader(providerAllAlbum.getNotificationData());
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
		getAllPhotos(false);
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
		IN_GROUP_ADD_ALBUM = getActivity().getIntent().getStringExtra("IN_GROUP_ADD_ALBUM") == null ? "0" : getActivity().getIntent().getStringExtra("IN_GROUP_ADD_ALBUM");
		IN_GROUP_UPLOAD_PHOTO = getActivity().getIntent().getStringExtra("IN_GROUP_UPLOAD_PHOTO") == null ? "0" : getActivity().getIntent().getStringExtra("IN_GROUP_UPLOAD_PHOTO");
		IN_PROFILE_COVER = getActivity().getIntent().getStringExtra("IN_PROFILE_COVER") == null ? "0" : getActivity().getIntent().getStringExtra("IN_PROFILE_COVER");
	}

	/**
	 * This method used to get all photos.
	 * @param isProgressShow represented progress shown
	 */
	private void getAllPhotos(final boolean isProgressShow) {
		providerAllAlbum.restorePagingSettings();
		if (isProgressShow) {
			proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
		}
		providerAllAlbum.getAllAlbumList(IN_GROUP_ID, new WebCallListener() {

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
						((JomMasterActivity) getActivity()).updateHeader(providerAllAlbum.getNotificationData());
						prepareList(data1, false);
						lstAllAlbum.setAdapter(getListAdapter());
					} else {
						if (responseCode == 204) {
							IjoomerUtilities.getCustomOkDialog(getString(R.string.album), getString(getResources().getIdentifier("code" + responseCode, "string", getActivity().getPackageName())), getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

								@Override
								public void NeutralMethod() {
									if (responseCode == 204) {
										if (!IN_GROUP_ID.equals("0") && !IN_GROUP_ADD_ALBUM.equals("0")) {
											if (addAlbumFragment == null) {
												addAlbumFragment = new JomAlbumAddFragment();
											}
											((IjoomerSuperMaster) getActivity()).addFragment(R.id.lnrFragment, addAlbumFragment);
										} else if (!IN_GROUP_ID.equals("0")) {
											((JomAlbumsActivity) getActivity()).finish();
										}
									}
								}
							});
						} else {
							lstAllAlbum.setAdapter(null);
							responseErrorMessageHandler(responseCode, isProgressShow);
						}

					}
				} catch (Throwable e) {
				}

			}
		});
	}

	
	/**
	 * This method used to prepare list for all album data.
	 * @param data represented all album data list
	 * @param append represented append data
	 */
	private void prepareList(ArrayList<HashMap<String, String>> data, boolean append) {
		if (data != null) {
			if (!append) {
				listData.clear();
			}
			for (HashMap<String, String> hashMap : data) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.jom_album_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hashMap);
				item.setValues(obj);
				if (append) {
					adapterAlbum.add(item);
				} else {
					listData.add(item);
				}
			}
		}

	}

	/**
	 * This method used to shown response message.
	 * @param responseCode represented response code
	 * @param finishActivityOnConnectionProblem represented finish activity on connection problem 
	 */
	private void responseErrorMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {
		IjoomerUtilities.getCustomOkDialog(getString(R.string.album), getString(getResources().getIdentifier("code" + responseCode, "string", getActivity().getPackageName())), getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

			@Override
			public void NeutralMethod() {
				
			}
		});
	}

	/**
	 * List adapter for all album.
	 */
	private SmartListAdapterWithHolder getListAdapter() {

		adapterAlbum = new SmartListAdapterWithHolder(getActivity(), R.layout.jom_album_list_item, listData, new ItemView() {

			@SuppressWarnings("unchecked")
			@Override
			public View setItemView(int position, View v, SmartListItem item, ViewHolder holder) {

				holder.imgArrow = (ImageView) v.findViewById(R.id.imgArrow);
				holder.imgAlbumAvatar = (ImageView) v.findViewById(R.id.imgAlbumAvatar);
				holder.txtAlbumTitle = (IjoomerTextView) v.findViewById(R.id.txtAlbumTitle);
				holder.txtAlbumPhotoCount = (IjoomerTextView) v.findViewById(R.id.txtAlbumPhotoCount);
				holder.txtAlbumDateLocation = (IjoomerTextView) v.findViewById(R.id.txtAlbumDateLocation);
				holder.txAlbumBy = (IjoomerTextView) v.findViewById(R.id.txAlbumBy);

				final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);

				androidQuery.id(holder.imgAlbumAvatar).image(row.get(THUMB), true, true, ((SmartActivity) getActivity()).getDeviceWidth(), 0);
				holder.txtAlbumTitle.setText(row.get(NAME));
				holder.txtAlbumDateLocation.setMovementMethod(new ScrollingMovementMethod());
				if (row.get("location").trim().length() <= 0) {
					holder.txtAlbumDateLocation.setText(row.get(DATE));
				} else {
					holder.txtAlbumDateLocation.setText(row.get(DATE) + " @ " + row.get(LOCATION));
				}
				holder.txAlbumBy.setText(String.format(getString(R.string.by), row.get(USER_NAME).trim()));
				holder.txtAlbumPhotoCount.setText(String.format(getString(R.string.count_braket), Integer.parseInt(row.get(COUNT))));

				holder.txAlbumBy.setMovementMethod(LinkMovementMethod.getInstance());
				holder.txAlbumBy.setText(((JomMasterActivity) getActivity()).addClickablePart(Html.fromHtml(row.get(USER_NAME)), row), BufferType.SPANNABLE);

				holder.imgAlbumAvatar.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						adapterNewintent(row);
					}
				});
				holder.txtAlbumTitle.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						adapterNewintent(row);
					}
				});
				holder.txtAlbumPhotoCount.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						adapterNewintent(row);
					}
				});
				holder.imgArrow.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						adapterNewintent(row);

					}
				});
				return v;
			}

			@Override
			public View setItemView(int position, View v, SmartListItem item) {
				return null;
			}
		});
		return adapterAlbum;
	}
	
	/**
	 * This method used to load new intent for all album list data.
	 * @param row represented album data
	 */
	private void adapterNewintent(HashMap<String, String> row) {
		try {
			((SmartActivity) getActivity()).loadNew(JomAlbumsDetailsActivity.class, getActivity(), false, "IN_USERID", IN_USERID, "IN_ALBUM", row, "IN_GROUP_ID", IN_GROUP_ID, "IN_GROUP_UPLOAD_PHOTO", IN_GROUP_UPLOAD_PHOTO, "IN_PROFILE_COVER", IN_PROFILE_COVER);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}


}
