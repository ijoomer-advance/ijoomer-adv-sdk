package com.ijoomer.components.jReview;

import java.util.ArrayList;
import java.util.HashMap;

import pl.mg6.android.maps.extensions.GoogleMap;
import pl.mg6.android.maps.extensions.GoogleMap.InfoWindowAdapter;
import pl.mg6.android.maps.extensions.GoogleMap.OnInfoWindowClickListener;
import pl.mg6.android.maps.extensions.Marker;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.customviews.IjoomerRatingBar;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.src.R;

public class JReviewArticleDetailMapActivity extends JReviewMasterActivity 
implements OnInfoWindowClickListener{

	GoogleMap googleMap;
	private AQuery androidQuery;
	private HashMap<Marker, HashMap<String, String>> markerHashMap;
	private ArrayList<HashMap<String, String>> IN_MAPLIST;
	Bitmap bitmapCreate = null;
	Bitmap bitmapScale = null;


	/**
	 * Overrides methods.
	 */
	@Override
	public int setLayoutId() {
		return R.layout.jreview_map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initComponents() {
		googleMap = getMapView();
		googleMap.moveCamera(CameraUpdateFactory.zoomBy(1));
		androidQuery = new AQuery(this);
		IN_MAPLIST = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("IN_MAPLIST");
		markerHashMap = new HashMap<Marker, HashMap<String, String>>();
	}

	@Override
	public void prepareViews() {
		((TextView) getHeaderView().findViewById(R.id.txtHeader)).setText(getIntent().getStringExtra(ARTICLENAME));
		placeMarker(IN_MAPLIST.get(0));
		googleMap
		.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(IN_MAPLIST.get(0).get(LAT)), Double.parseDouble(IN_MAPLIST.get(0).get(LONG)))));
		googleMap.setInfoWindowAdapter(new InfoAdapter());
	}

	@Override
	public void setActionListeners() {

	}

	private void placeMarker(final HashMap<String, String> markerData)
	{
		String[] images = getStringArray(markerData.get(IMAGES));
		if(images != null){
			androidQuery.ajax(images[0], Bitmap.class, 0, new AjaxCallback<Bitmap>() {
				@Override
				public void callback(String url, Bitmap object, AjaxStatus status) {
					super.callback(url, object, status);
					if (object == null) {
						object = BitmapFactory.decodeResource(getResources(), R.drawable.jreview_default_image);
					}
					markerHashMap.put(googleMap.addMarker(new MarkerOptions().title(markerData.get(ARTICLENAME))
							.icon(BitmapDescriptorFactory.fromBitmap(combineImages(BitmapFactory.decodeResource(getResources(), R.drawable.ijoomer_map_custom_marker), object)))
							.position(new LatLng(Double.parseDouble(markerData.get(LAT)), Double.parseDouble(markerData.get(LONG))))), markerData);
					if (bitmapCreate != null) {
						bitmapCreate.recycle();
						bitmapCreate = null;
					}
				}
			});
		}else{
			markerHashMap.put(googleMap.addMarker(new MarkerOptions().title(markerData.get(ARTICLENAME))
					.icon(BitmapDescriptorFactory.fromBitmap(combineImages(BitmapFactory.decodeResource(getResources(), R.drawable.ijoomer_map_custom_marker), BitmapFactory.decodeResource(getResources(), R.drawable.jreview_default_image))))
					.position(new LatLng(Double.parseDouble(markerData.get(LAT)), Double.parseDouble(markerData.get(LONG))))), markerData);
		}
	}


	@Override
	public void onInfoWindowClick(Marker marker) {
		marker.hideInfoWindow();
	}


	/**
	 * Custom marker info adapter.
	 *
	 * @author tasol
	 *
	 */
	class InfoAdapter implements InfoWindowAdapter {

		@Override
		public View getInfoWindow(Marker marker) {
			final HashMap<String, String> data = markerHashMap.get(marker);

			View view = LayoutInflater.from(JReviewArticleDetailMapActivity.this).inflate(R.layout.jreview_article_map_bubble, null);

			IjoomerTextView txtTitle= (IjoomerTextView) view.findViewById(R.id.txtTitle);
			IjoomerRatingBar userRatingBar = (IjoomerRatingBar) view.findViewById(R.id.jreviewarticleuserratingBar);
			IjoomerTextView txtUserRatingCount= (IjoomerTextView) view.findViewById(R.id.jreviewarticleuserratingcount);
			IjoomerTextView txtUserRating = (IjoomerTextView) view.findViewById(R.id.jreviewarticleuserrating);
			IjoomerRatingBar editorRatingBar = (IjoomerRatingBar) view.findViewById(R.id.jreviewarticleeditorratingBar);
			IjoomerTextView txtEditorRatingCount= (IjoomerTextView) view.findViewById(R.id.jreviewarticleeditorratingcount);
			IjoomerTextView  txtEditorRating = (IjoomerTextView) view.findViewById(R.id.jreviewarticleeditorrating);

			txtTitle.setText(data.get(ARTICLENAME));
			userRatingBar.setStarRating(Float.parseFloat(data.get(OVERALLAVERAGERATING)));
			txtUserRating.setText("("+IjoomerUtilities.parseFloat(data.get(OVERALLAVERAGERATING))+")");
			txtUserRatingCount.setText("("+data.get(USERRATINGCOUNT)+")");

			editorRatingBar.setStarRating(Float.parseFloat(data.get(EDITORRATING)));
			txtEditorRating.setText("("+IjoomerUtilities.parseFloat(data.get(EDITORRATING))+")");
			txtEditorRatingCount.setText("("+data.get(EDITORRATINGCOUNT)+")");

			return view;

		}

		@Override
		public View getInfoContents(Marker marker) {
			// TODO Auto-generated method stub
			return null;
		}

	}

	/**
	 * This method used to combine custom image on map marker icon.
	 *
	 * @param frame
	 *            represented default icon frame bitmap.
	 * @param image
	 *            represented dynamic icon bitmap.
	 * @return represented {@link Bitmap}
	 */
	public Bitmap combineImages(Bitmap frame, Bitmap image) {

		bitmapScale = Bitmap.createScaledBitmap(image, convertSizeToDeviceDependent(45), convertSizeToDeviceDependent(40), true);

		bitmapCreate = Bitmap.createBitmap(frame.getWidth(), frame.getHeight(), Bitmap.Config.ARGB_8888);

		Canvas comboImage = new Canvas(bitmapCreate);

		comboImage.drawBitmap(bitmapScale, convertSizeToDeviceDependent(7), convertSizeToDeviceDependent(7), null);
		comboImage.drawBitmap(frame, 0, 0, null);
		if (frame != null) {
			try {
				bitmapScale.recycle();
				frame.recycle();
				image.recycle();
				bitmapScale = null;
				frame = null;
				image = null;
			} catch (Throwable e) {
			}
		}
		return bitmapCreate;
	}

}
