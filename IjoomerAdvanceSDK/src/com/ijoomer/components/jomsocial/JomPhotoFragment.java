package com.ijoomer.components.jomsocial;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.library.jomsocial.JomGalleryDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertMagnatic;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Fragment Contains All Method Related To JomPhotoFragment.
 * 
 * @author tasol
 * 
 */
@SuppressLint("ValidFragment")
public class JomPhotoFragment extends SmartFragment {

	private GridView grdPhoto;
	private ProgressBar pbrPhotoGrid;

	private ArrayList<HashMap<String, String>> photoData;
	private ArrayList<SmartListItem> photoList;
	private HashMap<String, String> IN_ALBUM;
	private SmartListAdapterWithHolder gridAdapter;
	private JomGalleryDataProvider photoProvider;
	private AQuery androidQuery;

	private String IN_PROFILE_COVER;
	private String userID;
	private int pageNo;
	private boolean isInitial = false;

	/**
	 * Constructor
	 * 
	 * @param pageNo
	 *            represented page no
	 * @param IN_ALBUM
	 *            represented album data
	 * @param userID
	 *            represented user id
	 * @param profileCover
	 *            represented is profile cover
	 */
	public JomPhotoFragment(int pageNo, HashMap<String, String> IN_ALBUM, String userID, String profileCover) {
		this.IN_ALBUM = IN_ALBUM;
		this.pageNo = pageNo;
		this.userID = userID;
		this.IN_PROFILE_COVER = profileCover;
	}

	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.jom_photo_gridview;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents(View currentView) {
		grdPhoto = (GridView) currentView.findViewById(R.id.grdPhoto);
		pbrPhotoGrid = (ProgressBar) currentView.findViewById(R.id.pbrPhotoGrid);
	}

	@Override
	public void prepareViews(View currentView) {
		if (isInitial()) {
			notifyChanges();
		}
	}

	@Override
	public void setActionListeners(View currentView) {

	}

	/**
	 * Class methods
	 */

	/**
	 * This method used to notify photo view pager.
	 */
	public void notifyChanges() {

		if (photoList == null || photoList.size() <= 0) {
			photoProvider = new JomGalleryDataProvider(getActivity());
			photoProvider.restorePagingSettings();
			photoProvider.setPageNo(pageNo);
			photoProvider.setPageLimit(12);
			androidQuery = new AQuery(getActivity());
			photoList = new ArrayList<SmartListItem>();
			photoProvider.getPhotoList(IN_ALBUM.get("id"), userID, new WebCallListener() {

				@Override
				public void onProgressUpdate(int progressCount) {
				}

				@Override
				public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
					try {
						pbrPhotoGrid.setVisibility(View.GONE);
						if (responseCode == 200) {
							setPhotoData(data1);
							prepareGrid(data1);
							gridAdapter = getPhotoAdapter();
							grdPhoto.setAdapter(gridAdapter);
							gridAdapter.notifyDataSetChanged();
							grdPhoto.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

								}
							});
						} else {
							IjoomerUtilities.getCustomOkDialog(getString(R.string.photo),
									getString(getResources().getIdentifier("code" + responseCode, "string", getActivity().getPackageName())), getString(R.string.ok),
									R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

										@Override
										public void NeutralMethod() {

										}
									});
						}
					} catch (Exception e) {
					}

				}
			});
		} else {
			pbrPhotoGrid.setVisibility(View.GONE);
			gridAdapter = getPhotoAdapter();
			grdPhoto.setAdapter(gridAdapter);
			gridAdapter.notifyDataSetChanged();
		}

	}

	/**
	 * This method used to get photo data list.
	 * 
	 * @return represented {@link HashMap} list
	 */
	public ArrayList<HashMap<String, String>> getPhotoData() {
		return photoData;
	}

	/**
	 * This method used to set photo data list.
	 * 
	 * @param photoData
	 *            represented photo data
	 */
	public void setPhotoData(ArrayList<HashMap<String, String>> photoData) {
		this.photoData = photoData;
	}

	/**
	 * This method used to check is initial.
	 * 
	 * @return represented {@link Boolean}
	 */
	public boolean isInitial() {
		return isInitial;
	}

	/**
	 * This method used to set is initial.
	 * 
	 * @param isInitial
	 *            represented is initial
	 */
	public void setInitial(boolean isInitial) {
		this.isInitial = isInitial;
	}

	/**
	 * This method used to prepare list photo grid.
	 * 
	 * @param data
	 *            represented photo data.
	 */
	private void prepareGrid(ArrayList<HashMap<String, String>> data) {
		photoList.clear();

		for (HashMap<String, String> hmData : data) {
			try {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.jom_photo_grid_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hmData);
				item.setValues(obj);
				photoList.add(item);
			} catch (Exception e) {
			}

		}

	}

	private void setCoverPhoto(String photoid) {

		if (IjoomerGlobalConfiguration.getJomsocialVersion().equals(IjoomerGlobalConfiguration.JOMVERSION_V30)) {
			if (IN_PROFILE_COVER.equals("1")) {
				photoProvider.setProfileCoverV30(photoid, new WebCallListener() {
					final ProgressBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

					@Override
					public void onProgressUpdate(int progressCount) {
						proSeekBar.setProgress(progressCount);
					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						if (responseCode == 200) {
							IjoomerUtilities.getCustomOkDialog(getString(R.string.photo), getString(R.string.photo_set_as_profile_cover_successfully), getString(R.string.ok),
									R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

										@Override
										public void NeutralMethod() {
											JomAlbumsDetailsActivity.isResume = true;
											getActivity().finish();
										}
									});
						} else {
							IjoomerUtilities.getCustomOkDialog(getString(R.string.photo),
									getString(getResources().getIdentifier("code" + responseCode, "string", getActivity().getPackageName())), getString(R.string.ok),
									R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

										@Override
										public void NeutralMethod() {

										}
									});
						}
					}
				});
			} else if (IN_PROFILE_COVER.split("\\|")[0].equals("2")) {
				photoProvider.setGroupCoverV30(IN_PROFILE_COVER.split("\\|")[1], photoid, new WebCallListener() {
					final ProgressBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

					@Override
					public void onProgressUpdate(int progressCount) {
						proSeekBar.setProgress(progressCount);
					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						if (responseCode == 200) {
							IjoomerUtilities.getCustomOkDialog(getString(R.string.photo), getString(R.string.photo_set_as_profile_cover_successfully), getString(R.string.ok),
									R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

										@Override
										public void NeutralMethod() {
											JomAlbumsDetailsActivity.isResume = true;
											getActivity().finish();
										}
									});
						} else {
							IjoomerUtilities.getCustomOkDialog(getString(R.string.photo),
									getString(getResources().getIdentifier("code" + responseCode, "string", getActivity().getPackageName())), getString(R.string.ok),
									R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

										@Override
										public void NeutralMethod() {

										}
									});
						}
					}
				});
			} else if (IN_PROFILE_COVER.split("\\|")[0].equals("3")) {
				photoProvider.setEventCoverV30(IN_PROFILE_COVER.split("\\|")[1], photoid, new WebCallListener() {
					final ProgressBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

					@Override
					public void onProgressUpdate(int progressCount) {
						proSeekBar.setProgress(progressCount);
					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						if (responseCode == 200) {
							IjoomerUtilities.getCustomOkDialog(getString(R.string.photo), getString(R.string.photo_set_as_profile_cover_successfully), getString(R.string.ok),
									R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

										@Override
										public void NeutralMethod() {
											JomAlbumsDetailsActivity.isResume = true;
											getActivity().finish();
										}
									});
						} else {
							IjoomerUtilities.getCustomOkDialog(getString(R.string.photo),
									getString(getResources().getIdentifier("code" + responseCode, "string", getActivity().getPackageName())), getString(R.string.ok),
									R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

										@Override
										public void NeutralMethod() {

										}
									});
						}
					}
				});
			}
		} else {
			photoProvider.setProfileCover(photoid, new WebCallListener() {
				final ProgressBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

				@Override
				public void onProgressUpdate(int progressCount) {
					proSeekBar.setProgress(progressCount);
				}

				@Override
				public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
					if (responseCode == 200) {
						IjoomerUtilities.getCustomOkDialog(getString(R.string.photo), getString(R.string.photo_set_as_profile_cover_successfully), getString(R.string.ok),
								R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

									@Override
									public void NeutralMethod() {
										JomAlbumsDetailsActivity.isResume = true;
										getActivity().finish();
									}
								});
					} else {
						IjoomerUtilities.getCustomOkDialog(getString(R.string.photo),
								getString(getResources().getIdentifier("code" + responseCode, "string", getActivity().getPackageName())), getString(R.string.ok),
								R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

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
	 * List adapter for photo.
	 */

	private SmartListAdapterWithHolder getPhotoAdapter() {
		SmartListAdapterWithHolder listAdapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.jom_photo_grid_item, photoList, new ItemView() {

			@Override
			public View setItemView(final int position, View v, SmartListItem item, ViewHolder holder) {
				holder.imgAlbumphoto = (ImageView) v.findViewById(R.id.imgAlbumphoto);

				@SuppressWarnings("unchecked")
				final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);

				try {
					androidQuery.id(holder.imgAlbumphoto).image(row.get("thumb"), true, true, ((SmartActivity) getActivity()).getDeviceWidth(), 0);
				} catch (Throwable e) {
					e.printStackTrace();
				}
				holder.imgAlbumphoto.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						final ArrayList<HashMap<String, String>> photoData = new ArrayList<HashMap<String, String>>();
						ArrayList<JomPhotoFragment> fragmentList = ((JomAlbumsDetailsActivity) getActivity()).getPhotoFragmetStack();
						for (JomPhotoFragment jomPhotoFragment : fragmentList) {
							if (jomPhotoFragment.getPhotoData() != null && jomPhotoFragment.getPhotoData().size() > 0) {
								photoData.addAll(jomPhotoFragment.getPhotoData());
							}
						}
						final int selectedIndex = (position + (pageNo - 1) * 12);
						if (!IN_PROFILE_COVER.equals("0")) {
							IjoomerUtilities.getCustomConfirmDialog(getString(R.string.photo), getString(R.string.are_you_sure_set_profile_cover), getString(R.string.yes),
									getString(R.string.no), new CustomAlertMagnatic() {

										@Override
										public void PositiveMethod() {
											setCoverPhoto(photoData.get(selectedIndex).get("id"));
										}

										@Override
										public void NegativeMethod() {

										}
									});

						} else {
							try {
								((SmartActivity) getActivity()).loadNew(JomPhotoDetailsActivity.class, getActivity(), false, "IN_PHOTO_LIST", photoData, "IN_SELECTED_INDEX",
										selectedIndex, "IN_TOTAL_COUNT", photoProvider.getTotalCount(), "IN_ALBUM", IN_ALBUM);
								JomAlbumsDetailsActivity.isResume = true;
							} catch (Throwable e) {
								e.printStackTrace();
								JomAlbumsDetailsActivity.isResume = false;
							}
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
		return listAdapterWithHolder;
	}

}
