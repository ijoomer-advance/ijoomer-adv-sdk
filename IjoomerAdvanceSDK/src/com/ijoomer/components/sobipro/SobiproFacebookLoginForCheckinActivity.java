package com.ijoomer.components.sobipro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphPlace;
import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.FacebookLoginForCheckinMainFragment;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerRadioButton;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.src.R;
import com.smart.framework.ItemView;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

/**
 * This Class Contains All Method Related To FacebookLoginActivity.
 * 
 * @author tasol
 * 
 */
public class SobiproFacebookLoginForCheckinActivity extends FragmentActivity {

	private LinearLayout lnrPbr;
	private FacebookLoginForCheckinMainFragment mainFragment;
	private UiLifecycleHelper uiHelper;

	private ArrayList<HashMap<String, String>> facebookLocationResponse;
	private JSONObject location;
	private Bundle params;
	private PopupWindow popup;
	private LinearLayout lnrListFooter;
	private ArrayList<SmartListItem> listDataVenues = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder listAdapterWithHolderVenues;
	private String venueId = "";

	/**
	 * Overrides method
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		facebookLocationResponse = new ArrayList<HashMap<String, String>>();
		if (facebookLocationResponse != null && facebookLocationResponse.size() > 0) {
			facebookLocationResponse.clear();
		}
		location = new JSONObject();
		if (savedInstanceState == null) {
			mainFragment = new FacebookLoginForCheckinMainFragment();
			getSupportFragmentManager().beginTransaction().add(android.R.id.content, mainFragment).commit();
		} else {
			mainFragment = (FacebookLoginForCheckinMainFragment) getSupportFragmentManager().findFragmentById(android.R.id.content);
		}

		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);

	}

	@Override
	public void onResume() {
		super.onResume();
		lnrPbr = (LinearLayout) findViewById(R.id.lnrPbr);
		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		} else {
			Session.openActiveSession(this, true, callback);
		}

		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CANCELED) {
			finish();
		} else {
			uiHelper.onActivityResult(requestCode, resultCode, data);
		}

	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	/**
	 * Class method
	 */

	/**
	 * This method used to after session state change listner.
	 * 
	 * @param session
	 *            represent {@link Session}
	 * @param state
	 *            represent {@link SessionState}
	 * @param exception
	 *            represent exception
	 */
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		if (state.isOpened()) {
			fetchFacebookPlaceId();
		} else if (state.isClosed()) {
			Session.openActiveSession(this, true, callback);
		}
	}

	/**
	 * This method used to get session status callback change listner.
	 */
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	/**
	 * This method used to get facebook user data.
	 */
	@SuppressWarnings("deprecation")
	private void fetchFacebookPlaceId() {
		lnrPbr.setVisibility(View.VISIBLE);
		if (((SmartActivity) SobiproRestaurantEntryDetailFragment.mContext).getCurrentLocation() != null) {
			Request.executePlacesSearchRequestAsync(Session.getActiveSession(), ((SmartActivity) SobiproRestaurantEntryDetailFragment.mContext).getCurrentLocation(), 0, 50, null,
					new Request.GraphPlaceListCallback() {

						@Override
						public void onCompleted(List<GraphPlace> places, final Response response) {
							lnrPbr.setVisibility(View.GONE);
							try {
								facebookLocationResponse = new IjoomerCaching(((SmartActivity) SobiproRestaurantEntryDetailFragment.mContext)).parseData(response.getGraphObject()
										.getInnerJSONObject().getJSONArray("data"));
							} catch (Exception e) {
							}
							if (facebookLocationResponse != null && facebookLocationResponse.size() > 0) {

								showPopup(facebookLocationResponse);
							}
						}
					});
		} else {
			Toast.makeText(((SmartActivity) SobiproRestaurantEntryDetailFragment.mContext), getString(R.string.facebook_location_not_found), Toast.LENGTH_LONG).show();
		}

	}

	@SuppressWarnings("deprecation")
	private void showPopup(ArrayList<HashMap<String, String>> listdata) {

		int popupWidth = ((SmartActivity) SobiproRestaurantEntryDetailFragment.mContext).getDeviceWidth()
				- ((SmartActivity) SobiproRestaurantEntryDetailFragment.mContext).convertSizeToDeviceDependent(50);
		int popupHeight = ((SmartActivity) SobiproRestaurantEntryDetailFragment.mContext).getDeviceHeight()
				- ((SmartActivity) SobiproRestaurantEntryDetailFragment.mContext).convertSizeToDeviceDependent(180);

		LinearLayout viewGroup = (LinearLayout) findViewById(R.id.lnrPopup);
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = layoutInflater.inflate(R.layout.sobipro_restaurant_checkin_friendlist_popup, viewGroup);

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

		final ProgressBar pbrPopup = (ProgressBar) layout.findViewById(R.id.pbrPopup);
		final ListView listView = (ListView) layout.findViewById(R.id.listView);
		lnrListFooter = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.ijoomer_list_footer, null);
		listView.addFooterView(lnrListFooter, null, false);
		if (listDataVenues == null || listDataVenues.size() <= 0) {
			pbrPopup.setVisibility(View.GONE);
			prepareListVenues(facebookLocationResponse, false);
			listAdapterWithHolderVenues = getListAdapter(listDataVenues);
			listView.setAdapter(listAdapterWithHolderVenues);
		} else {
			pbrPopup.setVisibility(View.GONE);
			listView.setAdapter(listAdapterWithHolderVenues);
			listFooterInvisible();
		}

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				venueId = ((HashMap<String, String>) listDataVenues.get(arg2).getValues().get(0)).get("id");
				popup.dismiss();
				faceBookCheckin(venueId);
			}
		});

	}

	@SuppressLint("UseValueOf")
	private void faceBookCheckin(String venueId) {
		lnrPbr.setVisibility(View.VISIBLE);
		try {
			if (((SmartActivity) SobiproRestaurantEntryDetailFragment.mContext).getCurrentLocation() != null) {
				location.put("latitude", new Double(((SmartActivity) SobiproRestaurantEntryDetailFragment.mContext).getCurrentLocation().getLatitude()));
				location.put("longitude", new Double(((SmartActivity) SobiproRestaurantEntryDetailFragment.mContext).getCurrentLocation().getLongitude()));
			} else {
				Toast.makeText(((SmartActivity) SobiproRestaurantEntryDetailFragment.mContext), getString(R.string.facebook_location_not_found), Toast.LENGTH_LONG).show();
				return;
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		params = new Bundle();
		params.putString("place", venueId);
		params.putString("coordinates", location.toString());

		Request.Callback callback = new Request.Callback() {

			@Override
			public void onCompleted(Response response) {
				lnrPbr.setVisibility(View.GONE);
				Toast.makeText(((SmartActivity) SobiproRestaurantEntryDetailFragment.mContext), R.string.facebook_post_success, Toast.LENGTH_LONG).show();
				finish();
			}
		};
		Request request = new Request(Session.getActiveSession(), "me/checkins", params, HttpMethod.POST, callback);

		RequestAsyncTask task = new RequestAsyncTask(request);
		task.execute();

	}

	public void prepareListVenues(ArrayList<HashMap<String, String>> data, boolean append) {
		if (data != null) {
			if (!append) {
				listDataVenues.clear();
			}
			for (HashMap<String, String> hashMap : data) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.sobipro_restaurant_checkin_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hashMap);
				item.setValues(obj);
				if (append) {
					listAdapterWithHolderVenues.add(item);
				} else {
					listDataVenues.add(item);
				}
			}
		}
	}

	public SmartListAdapterWithHolder getListAdapter(ArrayList<SmartListItem> listData) {

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.sobipro_restaurant_checkin_list_item, listData, new ItemView() {

			@Override
			public View setItemView(int position, View v, SmartListItem item) {
				return null;
			}

			@Override
			public View setItemView(int position, View v, SmartListItem item, ViewHolder holder) {

				holder.txtLocationValue = (IjoomerTextView) v.findViewById(R.id.txtLocationValue);
				@SuppressWarnings("unchecked")
				HashMap<String, String> value = (HashMap<String, String>) item.getValues().get(0);
				holder.txtLocationValue.setText(value.get("name").toString().trim());
				holder.txtLocationValue.setTag(value.get("id").toString());
				// v.setTag(value.get("id").toString());
				return v;

			}

		});
		return adapterWithHolder;
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
}
