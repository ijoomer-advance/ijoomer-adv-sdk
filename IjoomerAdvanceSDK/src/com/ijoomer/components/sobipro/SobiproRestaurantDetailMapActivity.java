package com.ijoomer.components.sobipro;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ijoomer.src.R;

import pl.mg6.android.maps.extensions.GoogleMap;

public class SobiproRestaurantDetailMapActivity extends SobiproMasterActivity{

	GoogleMap googleMap;
	
	/**
	 * Overrides methods.
	 */
	@Override
	public int setLayoutId() {
		return R.layout.sobipro_restaurant_detail_map;
	}

	@Override
	public void initComponents() {
		googleMap = getMapView();
		if(googleMap!=null){
			googleMap.moveCamera(CameraUpdateFactory.zoomBy(15));
			placeMarker();
		}

	}

	@Override
	public void prepareViews() {
	}

	@Override
	public void setActionListeners() {
	}
	
	private void placeMarker()
	{
		googleMap.addMarker(new MarkerOptions().title("Ahmedabad").icon(
				BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED))
				.position(new LatLng(23, 72)));
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(23, 72)));
	}

}
