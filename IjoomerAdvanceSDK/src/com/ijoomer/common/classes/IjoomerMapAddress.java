package com.ijoomer.common.classes;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.src.R;
import com.ijoomer.theme.ThemeManager;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.mg6.android.maps.extensions.GoogleMap;
import pl.mg6.android.maps.extensions.GoogleMap.OnMapClickListener;

/**
 * This Class Contains All Method Related To IjoomerMapAddress.
 * 
 * @author tasol
 * 
 */
public class IjoomerMapAddress extends IjoomerSuperMaster implements OnMapClickListener {

	private ListView lstMapAddress;
	private IjoomerEditText editSearch;
	private IjoomerTextView txtMapAddressHints;
	private GoogleMap googleMap;
	private ImageView imgSearch;
	private ProgressBar pbrLstMapAddress;

	ArrayList<HashMap<String, String>> addressList;

	/**
	 * Overrides method
	 */

	@Override
	public int setLayoutId() {
		return ThemeManager.getInstance().getMapAddress();
	}

	@Override
	public void initComponents() {
		googleMap = getMapView();

		lstMapAddress = (ListView) findViewById(R.id.lstMapAddress);
		pbrLstMapAddress = (ProgressBar) findViewById(R.id.pbrLstMapAddress);
		txtMapAddressHints = (IjoomerTextView) findViewById(R.id.txtMapAddressHints);
		editSearch = (IjoomerEditText) findViewById(R.id.editSearch);
		imgSearch = (ImageView) findViewById(R.id.imgSearch);

		googleMap.setMyLocationEnabled(true);
		googleMap.setOnMapClickListener(this);
	}

	@Override
	public void prepareViews() {
		try {
			Address address = IjoomerUtilities.getAddressFromLatLong(0, 0);
			setAddressData(address.getLatitude(), address.getLongitude());
			CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(address.getLatitude(), address.getLongitude())).tilt(50).zoom(15).build();
			googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		} catch (Exception e) {
		}
	}

	@Override
	public void setActionListeners() {
		imgSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				hideSoftKeyboard();
				if (editSearch.getText().toString().trim().length() > 0) {
					try {
						Address address = IjoomerUtilities.getLatLongFromAddress(editSearch.getText().toString().trim());
						CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(address.getLatitude(), address.getLongitude())).tilt(50).zoom(15).build();
						googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
						setAddressData(address.getLatitude(), address.getLongitude());
						editSearch.setText(null);
					} catch (Exception e) {
						editSearch.setText(null);
					}
				} else {
					editSearch.setError(getString(R.string.validation_value_required));
				}
			}
		});
	}

	@Override
	public void onMapClick(LatLng latitudeLongitude) {
		CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitudeLongitude.latitude, latitudeLongitude.longitude)).tilt(50).zoom(15).build();
		googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		setAddressData(latitudeLongitude.latitude, latitudeLongitude.longitude);

	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public int setHeaderLayoutId() {
		return 0;
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

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {

	}

	@Override
	public int setFooterLayoutId() {
		return 0;
	}

	/**
	 * L Class method
	 */

	/**
	 * This method used to getting address from lat-lng.
	 * 
	 * @param lat
	 *            represented latitude
	 * @param lng
	 *            represented longitude
	 */
	private void setAddressData(double lat, double lng) {
		Geocoder geocoder;
		if (lat != 0 && lng != 0) {
			geocoder = new Geocoder(IjoomerMapAddress.this);
			try {
				List<Address> list = geocoder.getFromLocation(lat, lng, 10);

				if (list != null && list.size() > 0) {
					if (txtMapAddressHints.getVisibility() == View.GONE) {
						pbrLstMapAddress.setVisibility(View.VISIBLE);
					}
					addressList = new ArrayList<HashMap<String, String>>();
					for (Address address : list) {
						HashMap<String, String> data = new HashMap<String, String>();
						if (address.getAddressLine(0).toString().trim().length() > 0) {
							data.put("address", address.getAddressLine(0));
							data.put("latitude", String.valueOf(address.getLatitude()));
							data.put("longitude", String.valueOf(address.getLongitude()));
							addressList.add(data);
						} else if (address.getAddressLine(1).toString().trim().length() > 0) {
							data.put("address", address.getAddressLine(1));
							data.put("latitude", String.valueOf(address.getLatitude()));
							data.put("longitude", String.valueOf(address.getLongitude()));
							addressList.add(data);
						} else if (address.getLocality().toString().trim().length() > 0) {
							data.put("address", address.getLocality());
							data.put("latitude", String.valueOf(address.getLatitude()));
							data.put("longitude", String.valueOf(address.getLongitude()));
							addressList.add(data);
						} else if (address.getAdminArea().toString().trim().length() > 0) {
							data.put("address", address.getAdminArea());
							data.put("latitude", String.valueOf(address.getLatitude()));
							data.put("longitude", String.valueOf(address.getLongitude()));
							addressList.add(data);
						} else if (address.getCountryName().toString().trim().length() > 0) {
							data.put("address", address.getAdminArea());
							data.put("latitude", String.valueOf(address.getLatitude()));
							data.put("longitude", String.valueOf(address.getLongitude()));
							addressList.add(data);
						}
					}

					lstMapAddress.setAdapter(getListAdapter(prepareList(addressList)));
					if (txtMapAddressHints.getVisibility() == View.VISIBLE) {
						lstMapAddress.setVisibility(View.VISIBLE);
						txtMapAddressHints.setVisibility(View.GONE);
					} else {
						pbrLstMapAddress.setVisibility(View.GONE);
					}

				} else {
					IjoomerUtilities.getCustomOkDialog(getString(R.string.address_from_map), getString(R.string.lat_long_not_found), getString(R.string.ok),
							R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

								@Override
								public void NeutralMethod() {

								}
							});
				}

			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public ArrayList<SmartListItem> prepareList(ArrayList<HashMap<String, String>> data) {
		ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
		for (HashMap<String, String> hashMap : data) {
			SmartListItem item = new SmartListItem();
			item.setItemLayout(R.layout.ijoomer_map_address_item);
			ArrayList<Object> obj = new ArrayList<Object>();
			obj.add(hashMap);
			item.setValues(obj);
			listData.add(item);
		}
		return listData;
	}

	/**
	 * List adapter
	 */

	private SmartListAdapterWithHolder getListAdapter(ArrayList<SmartListItem> data) {

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.ijoomer_map_address_item, data, new ItemView() {

			@Override
			public View setItemView(final int position, View v, final SmartListItem item, final ViewHolder holder) {

				holder.txtMapAddressData = (IjoomerTextView) v.findViewById(R.id.txtMapAddressData);

				@SuppressWarnings("unchecked")
				final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);

				holder.txtMapAddressData.setText(row.get("address"));
				holder.txtMapAddressData.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent();
						intent.putExtra("MAP_ADDRESSS_DATA", row);
						setResult(Activity.RESULT_OK, intent);
						finish();
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
