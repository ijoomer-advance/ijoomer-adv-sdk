package com.ijoomer.plugins;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.RequestBatch;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.Builder;
import com.facebook.Session.OpenRequest;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.model.GraphPlace;
import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.custom.interfaces.SelectImageDialogListner;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerCheckBox;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerListView;
import com.ijoomer.customviews.IjoomerRadioButton;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.plugins.PluginsFriendsDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * This Class Contains PluginsFacebookCheckinActivity.
 * 
 * @author tasol
 * 
 */
public class PluginsFacebookCheckinActivity extends PluginsMasterActivity {

	private LinearLayout lnrListFooter;
	private LinearLayout lnrPhotoLayout;
	private LayoutInflater inflater;
	private IjoomerListView listLocation;
	private RelativeLayout rlPhotoLayout;
	private IjoomerEditText edtMessage;
	private IjoomerRadioButton imgPlace;
	private IjoomerRadioButton imgPhotos;
	private IjoomerRadioButton imgFriends;
	private Button btnOk;
	private ImageView imgAddPhoto;
	private ImageView imgSelectedPhoto;
	private ImageView imgRemovePhoto;
	private AutoCompleteTextView edtExtraMessage;
	private View headerView;
	private PopupWindow popup;
	
	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private ArrayList<SmartListItem> listDataFriends = new ArrayList<SmartListItem>();
	private HashMap<String, String> selectedFriends = new HashMap<String, String>();
	private ArrayList<String> locationArray = new ArrayList<String>();
	private ArrayList<String> friendListArray = new ArrayList<String>();
	private ArrayList<String> idArray = new ArrayList<String>();
	private ArrayList<HashMap<String, String>> facebookLocationResponse;
	ArrayList<HashMap<String, String>> friendList;
	private ArrayList<String> permissions = new ArrayList<String>();
	private ArrayList<String> selectedImageArray;
	private ArrayAdapter<String> adapter;
	private ArrayAdapter<String> friendAdapter;
	private SmartListAdapterWithHolder listAdapterWithHolder;
	private SmartListAdapterWithHolder listAdapterWithHolderFriend;
	private PluginsFriendsDataProvider providerFriend;
	private JSONObject location;
	private AQuery androidQuery;
	private Bitmap selectedImage;

	private String placeArray[];
	private String friendsArray[];
	private String LOCATION_NAME = "name";
	private String IN_USERID = "0";
	private String locationName = "";
	private String status = "";
	private String withFriend = "";
	private String atLocation = "";
	private String selectedFriendsFromPopup = "";
	private String id;
	private String selectedImagePathUserAvatar;
	@SuppressWarnings("unused")
	private byte[] photoData;
	final private int PICK_IMAGE_USER_AVATAR = 1;
	final private int CAPTURE_IMAGE_USER_AVATAR = 2;
	private int k = -1;
	private boolean isFromVenue = false;

	
	/**
	 * Overrides methods
	 */
	@Override
	public int setLayoutId() {
		return R.layout.plugins_facebook_checkin;
	}

	@Override
	public void initComponents() {

		inflater = LayoutInflater.from(this);
		listLocation = (IjoomerListView) findViewById(R.id.lstNearbyVenues);
		headerView = inflater.inflate(R.layout.plugins_facebook_post_dialog, null);
		listLocation.addHeaderView(headerView);
		listLocation.setAdapter(null);
		lnrPhotoLayout = (LinearLayout) headerView.findViewById(R.id.lnrPhotoLayout);
		edtMessage = (IjoomerEditText) headerView.findViewById(R.id.edtMessage);
		edtExtraMessage = (AutoCompleteTextView) headerView.findViewById(R.id.edtExtraMessage);
		imgAddPhoto = (ImageView) headerView.findViewById(R.id.imgAddPhotos);
		imgPlace = (IjoomerRadioButton) headerView.findViewById(R.id.imgPlace);
		imgPhotos = (IjoomerRadioButton) headerView.findViewById(R.id.imgPhotos);
		imgFriends = (IjoomerRadioButton) headerView.findViewById(R.id.imgFriends);
		btnOk = (Button) headerView.findViewById(R.id.btnOk);
		
		facebookLocationResponse = new ArrayList<HashMap<String, String>>();
		friendList = new ArrayList<HashMap<String, String>>();
		androidQuery = new AQuery(this);
		selectedImageArray = new ArrayList<String>();
		location = new JSONObject();
		


	}

	@Override
	public void prepareViews() {
		((TextView) getHeaderView().findViewById(R.id.txtHeader)).setText(getScreenCaption());
		permissions.addAll(Arrays.asList("photo_upload","publish_checkins","publish_stream","publish_actions"));
		callVenueSession();
	}

	@Override
	public void setActionListeners() {

		listLocation.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				edtExtraMessage.setVisibility(View.VISIBLE);
				edtExtraMessage.setHint(R.string.where_are_you);
				edtExtraMessage.setText(facebookLocationResponse.get(arg2 - 1).get("name"));
				locationName = facebookLocationResponse.get(arg2 - 1).get("name");

				status = edtMessage.getText().toString();
				status = status.replace(atLocation, "");
				status = status.replace(withFriend, "");

				atLocation = String.format(getString(R.string.facebook_status_location_string), locationName);
				status = status + " " + withFriend + " " + atLocation;
				edtMessage.setText(status.trim());
				id = facebookLocationResponse.get(arg2 - 1).get("id");
			}
		});

		edtExtraMessage.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					if (imgFriends.isChecked()) {
						status = edtMessage.getText().toString();
						status = status.replace(atLocation, "");
						status = status.replace(withFriend, "");

						withFriend = String.format(getString(R.string.facebook_status_friend_string), edtExtraMessage.getText().toString());
						if (edtExtraMessage.getText().toString().trim().length() == 0) {
							withFriend = "";
						}
						status = status + " " + withFriend + " " + atLocation;
						edtMessage.setText(status.trim());
					} else if (imgPlace.isChecked() || imgPhotos.isChecked()) {
						status = edtMessage.getText().toString();
						status = status.replace(atLocation, "");
						status = status.replace(withFriend, "");

						atLocation = String.format(getString(R.string.facebook_status_location_string), edtExtraMessage.getText().toString());
						if (edtExtraMessage.getText().toString().trim().length() == 0) {
							atLocation = "";
						}
						status = status + " " + withFriend + " " + atLocation;
						edtMessage.setText(status.trim());
					}
				} else if (hasFocus) {
					if (imgPlace.isChecked() || imgPhotos.isChecked()) {
						edtExtraMessage.setAdapter(adapter);
					} else if (imgFriends.isChecked()) {
						edtExtraMessage.setAdapter(friendAdapter);
					} else {
						edtExtraMessage.setAdapter(null);
					}
				}

			}
		});

		imgPlace.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (facebookLocationResponse != null && facebookLocationResponse.size() > 0) {
					facebookLocationResponse.clear();
				}
				edtExtraMessage.setHint(getString(R.string.where_are_you));
				callVenueSession();
			}
		});

		imgFriends.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				edtExtraMessage.setHint(getString(R.string.whoyouwith));
				showPopup(edtExtraMessage);
			}
		});

		imgPhotos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showSelectImageDialog();
			}
		});

		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String temp = "";
				if (selectedImage != null) {
					temp = checkForLocationId();
					uploadPhotoOnfacebook(temp);
				} else if (edtMessage.getText().toString().trim().length() > 0) {
					temp = checkForLocationId();
					postStatusOnFacebook(temp);
				} else {
					edtMessage.setError(getString(R.string.validation_value_required));
				}
			}
		});

		edtExtraMessage.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View arg1, int position, long arg3) {

				if (imgPlace.isChecked() || imgPhotos.isChecked()) {
					locationName = (String) adapter.getItemAtPosition(position);
					status = edtMessage.getText().toString();
					status = status.replace(atLocation, "");
					status = status.replace(withFriend, "");
					atLocation = String.format(getString(R.string.facebook_status_location_string), locationName);
					status = status + " " + withFriend + " " + atLocation;
					edtMessage.setText(status.trim());

					for (int i = 0; i < facebookLocationResponse.size(); i++) {
						if (facebookLocationResponse.get(i).get("name").equalsIgnoreCase(locationName)) {
							id = facebookLocationResponse.get(i).get("id");
						}
					}

				} else if (imgFriends.isChecked()) {
					selectedFriendsFromPopup = (String) adapter.getItemAtPosition(position);
					status = edtMessage.getText().toString();
					status = status.replace(atLocation, "");
					status = status.replace(withFriend, "");
					withFriend = String.format(getString(R.string.facebook_status_friend_string), selectedFriendsFromPopup);
					status = status + " " + withFriend + " " + atLocation;
					edtMessage.setText(status.trim());
				}
			}
		});

	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == PICK_IMAGE_USER_AVATAR) {
				selectedImagePathUserAvatar = getAbsolutePath(data.getData());
				selectedImageArray.add(selectedImagePathUserAvatar);
				selectedImage = decodeFile(selectedImagePathUserAvatar);
				setPhotoLayout();
			} else if (requestCode == CAPTURE_IMAGE_USER_AVATAR) {
				selectedImagePathUserAvatar = getImagePath();
				selectedImage = decodeFile(selectedImagePathUserAvatar);
				setPhotoLayout();
				convertBitmapToBite();
			} else {
				Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
				if (isFromVenue) {
					callVenueSession();
				}

			}
		}
	}
	
	@Override
	public String[] setTabItemNames() {
		return null;
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
	public int[] setTabItemOnDrawables() {
		return null;
	}

	@Override
	public int[] setTabItemOffDrawables() {
		return null;
	}

	@Override
	public int[] setTabItemPressDrawables() {
		return null;
	}

	
	/**
	 * Class methods
	 */
	/**
	 * This method used to show select image dialog.
	 */
	private void showSelectImageDialog() {
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

	
	/**
	 * This method used to check for lcation id.
	 * @return represented {@link String}
	 */
	private String checkForLocationId() {
		String tempValue = "";
		for (int i = 0; i < facebookLocationResponse.size(); i++) {
			if (edtMessage.getText().toString().contains(facebookLocationResponse.get(i).get("name"))) {
				if (edtMessage.getText().toString().trim().contains(atLocation)) {
					tempValue = edtMessage.getText().toString().replace(atLocation, "");
					return tempValue;
				} else {
					tempValue = edtMessage.getText().toString();
					return tempValue;
				}
			} else {
				tempValue = edtMessage.getText().toString();
			}
		}
		return tempValue;
	}

	
	/**
	 * This method used to upload photo on facebook.
	 * @param message represented message
	 */
	private void uploadPhotoOnfacebook(final String message) {
		Session.openActiveSession(this, true, new Session.StatusCallback() {

			@Override
			public void call(Session session, SessionState state, Exception exception) {
				if (session.isOpened()) {
					Request request = null;
					final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.facebook_post_progress_title));

					RequestBatch requestBatch = new RequestBatch();
					for (final String requestId : selectedImageArray) {
						Bitmap image = decodeFile(requestId);
						request = Request.newUploadPhotoRequest(Session.getActiveSession(), image, new Request.Callback() {
							@Override
							public void onCompleted(Response response) {
								proSeekBar.setProgress(100);
								lnrPhotoLayout.removeAllViews();
								edtMessage.setText("");
								edtExtraMessage.setText("");
								atLocation = "";
								withFriend = "";
								selectedImageArray.clear();
								Toast.makeText(PluginsFacebookCheckinActivity.this, R.string.facebook_post_success, Toast.LENGTH_LONG).show();
							}
						});
						Bundle params = request.getParameters();
						for (int i = 0; i < facebookLocationResponse.size(); i++) {
							if (atLocation.contains(facebookLocationResponse.get(i).get("name"))) {
								params.putString("place", id);
							}
						}
						if (message.trim().length() > 0) {
							params.putString("caption", message);
						}
						request.setParameters(params);
						requestBatch.add(request);
					}
					Request.executeBatchAsync(requestBatch);
				}
			}
		});
	}

	/**
	 * This method used to post status on facebook.
	 * @param message represented status message
	 */
	@SuppressWarnings("unused")
	private void postStatusOnFacebook(final String message) {
		Session session = openActiveSession(PluginsFacebookCheckinActivity.this, true, new Session.StatusCallback() {
			@SuppressLint("UseValueOf")
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				if (session.isOpened()) {
					final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.facebook_post_progress_title));
					try {

						if (getCurrentLocation() != null) {
							location.put("latitude", new Double(getCurrentLocation().getLatitude()));
							location.put("longitude", new Double(getCurrentLocation().getLongitude()));
						} else {
							Toast.makeText(PluginsFacebookCheckinActivity.this, getString(R.string.facebook_location_not_found), Toast.LENGTH_LONG).show();
							return;
						}

					} catch (Exception e) {

						e.printStackTrace();
					}
					Bundle params = new Bundle();
					for (int i = 0; i < facebookLocationResponse.size(); i++) {
						if (atLocation.contains(facebookLocationResponse.get(i).get("name"))) {
							params.putString("place", id);
							params.putString("coordinates", location.toString());
						}
					}
					if (edtMessage.getText().toString().trim().length() > 0) {
						params.putString("message", message);
					}
					Request.Callback callback = new Request.Callback() {

						@Override
						public void onCompleted(Response response) {
							lnrPhotoLayout.removeAllViews();
							edtMessage.setText("");
							edtExtraMessage.setText("");
							proSeekBar.setProgress(100);
							Toast.makeText(PluginsFacebookCheckinActivity.this, R.string.facebook_post_success, Toast.LENGTH_LONG).show();
						}
					};
					Request request = new Request(session, "me/checkins", params, HttpMethod.POST, callback);

					RequestAsyncTask task = new RequestAsyncTask(request);
					task.execute();
				}
			}

		}, permissions);
	}

	
	/**
	 * This method used to call venue session.
	 */
	private void callVenueSession() {
		isFromVenue = true;
		Session.openActiveSession(this, true, new Session.StatusCallback() {

			@SuppressWarnings("deprecation")
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				if (session.isOpened()) {
					if (getCurrentLocation() != null) {
						final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.facebook_nearby_progress_title));
						Request.executePlacesSearchRequestAsync(session, getCurrentLocation(), 0, 0, null, new Request.GraphPlaceListCallback() {

							@Override
							public void onCompleted(List<GraphPlace> places, final Response response) {
								isFromVenue = false;
								proSeekBar.setProgress(100);

								facebookLocationResponse = new IjoomerCaching(PluginsFacebookCheckinActivity.this).parseData(response.getGraphObject().getInnerJSONObject());
								locationArray.clear();
								idArray.clear();
								for (int i = 0; i < facebookLocationResponse.size(); i++) {
									locationArray.add(facebookLocationResponse.get(i).get("name"));

									idArray.add(facebookLocationResponse.get(i).get("id"));
								}
								placeArray = new String[locationArray.size()];
								placeArray = locationArray.toArray(placeArray);

								prepareList(facebookLocationResponse, false);
								listAdapterWithHolder = getListAdapter(listData);
								listLocation.setAdapter(listAdapterWithHolder);

								adapter = new ArrayAdapter<String>(PluginsFacebookCheckinActivity.this, android.R.layout.simple_dropdown_item_1line, placeArray);
							}
						});
					} else {
						Toast.makeText(PluginsFacebookCheckinActivity.this, getString(R.string.facebook_location_not_found), Toast.LENGTH_LONG).show();
					}
				}
			}
		});
	}

	
	/**
	 * This method used to show friend dialog.
	 * @param editAddFriend represented {@link AutoCompleteTextView}
	 */
	@SuppressWarnings("deprecation")
	private void showPopup(final AutoCompleteTextView editAddFriend) {

		int popupWidth = getDeviceWidth() - convertSizeToDeviceDependent(50);
		int popupHeight = getDeviceHeight() - convertSizeToDeviceDependent(180);

		LinearLayout viewGroup = (LinearLayout) findViewById(R.id.lnrPopup);
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = layoutInflater.inflate(R.layout.plugins_facebook_friendlist_popup, viewGroup);

		popup = new PopupWindow(this);
		popup.setContentView(layout);
		popup.setWidth(popupWidth);
		popup.setHeight(popupHeight);
		popup.setFocusable(true);
		popup.setBackgroundDrawable(new BitmapDrawable(getResources()));
		popup.showAtLocation(layout, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);

		IjoomerButton btnCancel = (IjoomerButton) layout.findViewById(R.id.btnCancel);
		final IjoomerRadioButton rdbSelectAll = (IjoomerRadioButton) layout.findViewById(R.id.rdbSelectAll);
		final IjoomerRadioButton rdbSelectNone = (IjoomerRadioButton) layout.findViewById(R.id.rdbSelectNone);
		rdbSelectAll.setVisibility(View.GONE);
		rdbSelectNone.setVisibility(View.GONE);
		IjoomerButton btnDone = (IjoomerButton) layout.findViewById(R.id.btnDone);

		final ProgressBar pbrPopup = (ProgressBar) layout.findViewById(R.id.pbrPopup);
		final ListView listView = (ListView) layout.findViewById(R.id.listView);
		lnrListFooter = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.ijoomer_list_footer, null);
		listView.addFooterView(lnrListFooter, null, false);

		if (listDataFriends == null || listDataFriends.size() <= 0) {
			providerFriend = new PluginsFriendsDataProvider(this);
			providerFriend.getFriendsList(IN_USERID, new WebCallListener() {

				@Override
				public void onProgressUpdate(int progressCount) {
				}

				@Override
				public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {

					pbrPopup.setVisibility(View.GONE);
					rdbSelectAll.setVisibility(View.VISIBLE);
					rdbSelectNone.setVisibility(View.VISIBLE);
					if (responseCode == 200) {
						for (HashMap<String, String> hashMap : data1) {
							hashMap.put("isChecked", "false");
						}
						if (data1 != null && data1.size() > 0) {
							prepareListFriend(data1, false);
							friendList = data1;
							listAdapterWithHolderFriend = getFriendListAdapter();
							listView.setAdapter(listAdapterWithHolderFriend);
							if (selectedFriends.size() == listAdapterWithHolderFriend.getCount()) {
								rdbSelectAll.setChecked(true);
								rdbSelectNone.setChecked(false);
							} else if (selectedFriends.size() == 0) {
								rdbSelectNone.setChecked(true);
								rdbSelectAll.setChecked(false);
							}
							friendListArray.clear();
							for (int i = 0; i < friendList.size(); i++) {
								friendListArray.add(friendList.get(i).get(USER_NAME));
							}
							friendsArray = new String[friendListArray.size()];
							friendsArray = friendListArray.toArray(friendsArray);

							friendAdapter = new ArrayAdapter<String>(PluginsFacebookCheckinActivity.this, android.R.layout.simple_dropdown_item_1line, friendsArray);

						}
					} else {
						rdbSelectAll.setVisibility(View.GONE);
						rdbSelectNone.setVisibility(View.GONE);
						responseErrorMessageHandler(responseCode, false);
					}

				}
			});
		} else {

			pbrPopup.setVisibility(View.GONE);
			listView.setAdapter(listAdapterWithHolderFriend);
			listFooterInvisible();
			if (selectedFriends.size() == listAdapterWithHolderFriend.getCount()) {
				rdbSelectAll.setChecked(true);
				rdbSelectNone.setChecked(false);
			} else if (selectedFriends.size() == 0) {
				rdbSelectNone.setChecked(true);
				rdbSelectAll.setChecked(false);
			}
			rdbSelectAll.setVisibility(View.VISIBLE);
			rdbSelectNone.setVisibility(View.VISIBLE);
		}

		rdbSelectAll.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View arg0) {
				if (listAdapterWithHolderFriend != null) {
					rdbSelectNone.setChecked(false);
					int size = listAdapterWithHolderFriend.getCount();
					for (int i = 0; i < size; i++) {
						((HashMap<String, String>) ((SmartListItem) listAdapterWithHolderFriend.getItem(i)).getValues().get(0)).put("isChecked", "true");
					}
					listAdapterWithHolderFriend.notifyDataSetChanged();
				}
			}
		});
		rdbSelectNone.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
				if (listAdapterWithHolderFriend != null) {
					rdbSelectAll.setChecked(false);
					int size = listAdapterWithHolderFriend.getCount();
					for (int i = 0; i < size; i++) {
						((HashMap<String, String>) ((SmartListItem) listAdapterWithHolderFriend.getItem(i)).getValues().get(0)).put("isChecked", "false");
					}
					listAdapterWithHolderFriend.notifyDataSetChanged();
				}
			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popup.dismiss();
			}
		});

		btnDone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String selectedFriendIDS = "";
				selectedFriendsFromPopup = "";

				for (Iterator<String> iterator = PluginsFacebookCheckinActivity.this.selectedFriends.keySet().iterator(); iterator.hasNext();) {
					String type = iterator.next();

					if (iterator.hasNext()) {
						selectedFriendsFromPopup += PluginsFacebookCheckinActivity.this.selectedFriends.get(type) + ",";
						selectedFriendIDS += type + ",";
					} else {
						selectedFriendsFromPopup += PluginsFacebookCheckinActivity.this.selectedFriends.get(type);
						selectedFriendIDS += type;
					}

				}
				editAddFriend.setText(selectedFriendsFromPopup);
				editAddFriend.setTag(selectedFriendIDS);

				status = edtMessage.getText().toString();
				status = status.replace(atLocation, "");
				status = status.replace(withFriend, "");

				withFriend = String.format(getString(R.string.facebook_status_friend_string), selectedFriendsFromPopup);

				status = status + " " + withFriend + " " + atLocation;
				edtMessage.setText(status.trim());

				popup.dismiss();
			}
		});

		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
			}

			@Override
			public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 1) {
					if (!providerFriend.isCalling() && providerFriend.hasNextPage()) {
						listFooterVisible();
						providerFriend.getFriendsList(IN_USERID, new WebCallListener() {

							@Override
							public void onProgressUpdate(int progressCount) {
							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								listFooterInvisible();
								if (responseCode == 200) {

									for (HashMap<String, String> hashMap : data1) {
										hashMap.put("isChecked", "false");
									}
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
	}

	/**
	 * This method used to visible list footer
	 */
	public void listFooterVisible() {
		lnrListFooter.setVisibility(View.VISIBLE);
	}

	/**
	 * This method used to gone list footer
	 */
	public void listFooterInvisible() {
		lnrListFooter.setVisibility(View.GONE);
	}

	
	private void responseErrorMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {
		IjoomerUtilities.getCustomOkDialog(getString(R.string.select_friend_facebook), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())),
				getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

					@Override
					public void NeutralMethod() {
						
					}
				});
	}

	
	/**
	 * This method used to prepare list facebook friend.
	 * @param data represented friend data
	 * @param append represented data append
	 */
	public void prepareListFriend(ArrayList<HashMap<String, String>> data, boolean append) {
		if (data != null) {
			if (!append) {
				listDataFriends.clear();
			}
			for (HashMap<String, String> hashMap : data) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.plugins_facebook_friend_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hashMap);
				item.setValues(obj);
				if (append) {
					listAdapterWithHolderFriend.add(item);
				} else {
					listDataFriends.add(item);
				}
			}
		}
	}


	/**
	 * This method used to convert bitmap to bite.
	 */
	private void convertBitmapToBite() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		photoData = baos.toByteArray();
	}

	/**
	 * This method used to set photo layout.
	 */
	private void setPhotoLayout() {
		k++;
		rlPhotoLayout = new RelativeLayout(this);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		rlPhotoLayout.setLayoutParams(lp);

		imgSelectedPhoto = new ImageView(this);
		imgRemovePhoto = new ImageView(this);
		RelativeLayout.LayoutParams vp = new RelativeLayout.LayoutParams(100, 100);
		RelativeLayout.LayoutParams vpRemove = new RelativeLayout.LayoutParams(20, 20);
		vp.setMargins(10, 5, 0, 0);
		imgSelectedPhoto.setLayoutParams(vp);
		imgSelectedPhoto.setScaleType(ImageView.ScaleType.FIT_XY);
		imgSelectedPhoto.setMaxHeight(100);
		imgSelectedPhoto.setMaxWidth(100);
		imgSelectedPhoto.setImageBitmap(selectedImage);
		imgSelectedPhoto.setId(k);
		imgSelectedPhoto.setTag(k);

		vpRemove.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		imgRemovePhoto.setLayoutParams(vpRemove);
		imgRemovePhoto.setMaxHeight(20);
		imgRemovePhoto.setMaxWidth(20);
		imgRemovePhoto.setImageResource(R.drawable.wall_remove);
		rlPhotoLayout.setId(k);

		imgRemovePhoto.setTag(rlPhotoLayout);

		rlPhotoLayout.addView(imgSelectedPhoto);
		rlPhotoLayout.addView(imgRemovePhoto);

		lnrPhotoLayout.addView(rlPhotoLayout);
		imgAddPhoto.setVisibility(View.VISIBLE);
		imgAddPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showSelectImageDialog();
			}
		});

		imgRemovePhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RelativeLayout temp = (RelativeLayout) v.getTag();
				temp.setVisibility(View.GONE);

				ImageView tempImg = (ImageView) temp.getChildAt(0);
				int tempId = (Integer) tempImg.getTag();
				selectedImageArray.remove(tempId);
			}
		});
	}

	/**
	 * This method used to open active session.
	 * @param activity represented {@link Activity}
	 * @param allowLoginUI represented allow login ui
	 * @param callback represented {@link StatusCallback}
	 * @param permissions represented permission
	 * @return represented {@link Session}
	 */
	private Session openActiveSession(Activity activity, boolean allowLoginUI, StatusCallback callback, ArrayList<String> permissions) {

		OpenRequest openRequest = new OpenRequest(activity).setPermissions(permissions).setCallback(callback);
		Session session = new Builder(activity).build();
		if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState()) || allowLoginUI) {
			Session.setActiveSession(session);
			session.openForPublish(openRequest);
			return session;
		}
		return Session.getActiveSession();
	}


	
	/**
	 * This method used to prepare list facebook checkin.
	 * @param data represented checkin data
	 * @param append represented data append
	 */
	public void prepareList(ArrayList<HashMap<String, String>> data, boolean append) {

		if (data != null) {

			if (!append) {
				listData.clear();
			}
			for (HashMap<String, String> hashMap : data) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.plugins_facebook_checkin_list_item);
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
	 * List adapter for facebook checkin.
	 * @param listData represented checkin data
	 * @return represented {@link SmartListAdapterWithHolder}
	 */
	public SmartListAdapterWithHolder getListAdapter(ArrayList<SmartListItem> listData) {

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.plugins_facebook_checkin_list_item, listData, new ItemView() {

			@Override
			public View setItemView(int position, View v, SmartListItem item, ViewHolder holder) {

				holder.txtLocationValue = (IjoomerTextView) v.findViewById(R.id.txtLocationValue);
				@SuppressWarnings("unchecked")
				HashMap<String, String> value = (HashMap<String, String>) item.getValues().get(0);
				holder.txtLocationValue.setText(value.get(LOCATION_NAME).toString().trim());
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
	 * List adapter for friend.
	 * @return represented {@link SmartListAdapterWithHolder}
	 */
	private SmartListAdapterWithHolder getFriendListAdapter() {
		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.plugins_facebook_friend_list_item, listDataFriends, new ItemView() {

			@Override
			public View setItemView(int position, View v, SmartListItem item, ViewHolder holder) {

				holder.friendmembertxtName = (IjoomerTextView) v.findViewById(R.id.txtName);
				holder.friendmemberImage = (ImageView) v.findViewById(R.id.imgFriend);
				holder.friendmemberimgOnlineStatus = (ImageView) v.findViewById(R.id.imgOnlineStatus);
				holder.chkSelectFriend = (IjoomerCheckBox) v.findViewById(R.id.chkSelectFriend);
				holder.txtInvited = (IjoomerTextView) v.findViewById(R.id.txtInvited);
				holder.chkSelectFriend.setVisibility(View.VISIBLE);
				holder.friendmemberimgOnlineStatus.setVisibility(View.GONE);
				@SuppressWarnings("unchecked")
				final HashMap<String, String> friend = (HashMap<String, String>) item.getValues().get(0);
				holder.chkSelectFriend.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton ButtonView, boolean isChecked) {
						if (isChecked) {
							selectedFriends.put(friend.get(USER_ID), friend.get(USER_NAME));
						} else {
							selectedFriends.remove(friend.get(USER_ID));
						}
						friend.put("isChecked", "" + isChecked);
						listAdapterWithHolderFriend.notifyDataSetChanged();
					}
				});
				holder.chkSelectFriend.setChecked(Boolean.parseBoolean(friend.get("isChecked").toString()));

				holder.friendmembertxtName.setText(friend.get(USER_NAME));
				androidQuery.id(holder.friendmemberImage).image(friend.get(USER_AVATAR), true, true, getDeviceWidth(), 0);

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
