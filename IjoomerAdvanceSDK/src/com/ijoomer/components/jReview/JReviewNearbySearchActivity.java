package com.ijoomer.components.jReview;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import pl.mg6.android.maps.extensions.ClusteringSettings;
import pl.mg6.android.maps.extensions.GoogleMap;
import pl.mg6.android.maps.extensions.GoogleMap.InfoWindowAdapter;
import pl.mg6.android.maps.extensions.GoogleMap.OnInfoWindowClickListener;
import pl.mg6.android.maps.extensions.Marker;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.LatLngBounds.Builder;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ijoomer.common.classes.IjoomerMapClusterIconProvider;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerRatingBar;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.jReview.JReviewDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;

/**
 * This Class Contains All Method Related To JomMapActivity.
 *
 * @author tasol
 *
 */
public class JReviewNearbySearchActivity extends JReviewMasterActivity implements OnInfoWindowClickListener {

	private GoogleMap googleMap;
	private IjoomerEditText edtMiles;
	private IjoomerButton btnGo;
	private ProgressBar progress;

	private String IN_CAPTION;
	private ArrayList<String> ARTICLEIDS_ARRAY;
	private ArrayList<String> CATEGORYIDS_ARRAY;
	private ArrayList<HashMap<String, String>> IN_MAPLIST;

	private boolean IN_SHOW_BUBBLE;
	private AQuery androidQuery;
	private HashMap<Marker, HashMap<String, String>> markerHashMap;
	Bitmap bitmapCreate = null;
	Bitmap bitmapScale = null;

	// 180 - 8*8,160 - 9*9,144 - 10*10,120 - 12*12 and 96 - 15*15 grids
	// respectively on zoom level 2.
	private final double[] CLUSTER_SIZES = new double[] { 180, 160, 144, 120, 96 };

	private MutableData[] dataArray = { new MutableData(6, new LatLng(-50, 0)), new MutableData(28, new LatLng(-52, 1)), new MutableData(496, new LatLng(-51, -2)), };
	private Handler handler = new Handler();
	private Runnable dataUpdater = new Runnable() {

		@Override
		public void run() {
			for (MutableData data : dataArray) {
				data.value = 7 + 3 * data.value;
			}
			onDataUpdate();
			handler.postDelayed(this, 1000);
		}
	};

	private JReviewDataProvider dataProvider;

	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.jreview_nearby_map;
	}

	@Override
	public void initComponents() {
		getIntentData();
		googleMap = getMapView();
		googleMap.moveCamera(CameraUpdateFactory.zoomBy(1));

		progress = (ProgressBar) findViewById(R.id.progressBar);
		edtMiles = (IjoomerEditText) findViewById(R.id.edit_miles);
		edtMiles.setText("5");
		btnGo = (IjoomerButton) findViewById(R.id.btn_go);

		androidQuery = new AQuery(this);
		dataProvider = new JReviewDataProvider(this);
	}

	@Override
	public void prepareViews() {
		((TextView) getHeaderView().findViewById(R.id.txtHeader)).setText(
				IN_CAPTION);

		getNearbyResults(edtMiles.getText().toString());
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (IN_SHOW_BUBBLE) {
			handler.post(dataUpdater);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (IN_SHOW_BUBBLE) {
			handler.removeCallbacks(dataUpdater);
		}
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		if (marker.isCluster()) {
			List<Marker> markers = marker.getMarkers();
			Builder builder = LatLngBounds.builder();
			for (Marker m : markers) {
				builder.include(m.getPosition());
			}
			LatLngBounds bounds = builder.build();
			googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, getResources().getDimensionPixelSize(R.dimen.padding)));
		} else {
			try {
				final HashMap<String, String> data = markerHashMap.get(marker);
				loadNew(JReviewArticleDetailActivity.class, JReviewNearbySearchActivity.this, false, "IN_PAGE", PAGEARTICLES, 
						"IN_CAPTION", IN_CAPTION, "IN_ARTICLE_INDEX", data.get(POSITION),
						"IN_ARTICLE_ID_ARRAY", ARTICLEIDS_ARRAY,"IN_CATEGORY_ID_ARRAY", CATEGORYIDS_ARRAY);
			} catch (Exception e) {
				e.printStackTrace();
			}
			marker.hideInfoWindow();
		}
	}

	@Override
	public void setActionListeners() {
		btnGo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(edtMiles.getText().toString().length() > 0){
					progress.setVisibility(View.VISIBLE);
					getNearbyResults(edtMiles.getText().toString());
				}else{
					edtMiles.setError(getString(R.string.validation_value_required));
				}
			}
		});
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {

	}

	/**
	 * Class methods
	 */

	/**
	 * This method used to set clustering view.
	 */
	private void setUpClusteringViews() {
		ClusteringSettings clusteringSettings = new ClusteringSettings();
		clusteringSettings.addMarkersDynamically(true);

		clusteringSettings.iconDataProvider(new IjoomerMapClusterIconProvider(getResources()));

		double clusterSize = CLUSTER_SIZES[0];
		clusteringSettings.clusterSize(clusterSize);

		googleMap.setClustering(clusteringSettings);
	}

	private void getIntentData(){
		try {
			JSONObject IN_OBJ = new JSONObject(getIntent().getStringExtra("IN_OBJ"));
			IN_CAPTION = IN_OBJ.getString("itemcaption");
		} catch (Exception e) {
		}
	}

	private void getNearbyResults(String distance){
		dataProvider.getnearbyResult(getLatitude(), getLongitude(), distance, new WebCallListener() {

			@Override
			public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				if(responseCode == 200){
					try {
						googleMap.clear();
						CATEGORYIDS_ARRAY = new ArrayList<String>();
						ARTICLEIDS_ARRAY = new ArrayList<String>();
						markerHashMap = new HashMap<Marker, HashMap<String, String>>();
						IN_MAPLIST = new ArrayList<HashMap<String,String>>();
						for(int i = 0; i < data1.size(); i++){
							ArrayList<HashMap<String, String>> articles = dataProvider.getSingleArticle(data1.get(i).get(CATEGORY_ID),data1.get(i).get(ARTICLEID));
							if(articles != null && articles.size() > 0){
								IN_MAPLIST.add(articles.get(0));
							}
						}
						//create article listing
						prepareList(IN_MAPLIST,false);

						if(IN_MAPLIST != null && IN_MAPLIST.size() > 0){
							populateMap();
						}else{
							progress.setVisibility(View.GONE);
							IjoomerUtilities.getCustomOkDialog(getString(R.string.jreview_nearby), getString(R.string.jreview_nocontentfound), getString(R.string.ok), R.layout.ijoomer_ok_dialog,
									new CustomAlertNeutral() {

								@Override
								public void NeutralMethod() {
								}
							});
						}
					} catch (Exception e) {
						e.printStackTrace();
						progress.setVisibility(View.GONE);
					}
				} else if(responseCode == 204){
					progress.setVisibility(View.GONE);
					IjoomerUtilities.getCustomOkDialog(getString(R.string.jreview_nearby), getString(R.string.jreview_nocontentfound), getString(R.string.ok), R.layout.ijoomer_ok_dialog,
							new CustomAlertNeutral() {

						@Override
						public void NeutralMethod() {
						}
					});
				} else{
					progress.setVisibility(View.GONE);
					IjoomerUtilities.getCustomOkDialog(getString(R.string.jreview_nearby), getString(getResources()
							.getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok), R.layout.ijoomer_ok_dialog,
							new CustomAlertNeutral() {

						@Override
						public void NeutralMethod() {

						}
					});
				}
			}

			@Override
			public void onProgressUpdate(int progressCount) {

			}

		});
	}

	/**
	 * This method used to prepare list of category and article ids.
	 * 
	 * @param articleData
	 *            represented article data
	 */
	public void prepareList(ArrayList<HashMap<String, String>> articleData,boolean append) {
		if (!append) {
			CATEGORYIDS_ARRAY.clear();
			ARTICLEIDS_ARRAY.clear();
		}

		if (articleData != null && articleData.size() > 0) {

			for (int i = 0; i < articleData.size(); i++) {
				CATEGORYIDS_ARRAY.add(articleData.get(i).get(CATEGORY_ID));
				ARTICLEIDS_ARRAY.add(articleData.get(i).get(ARTICLEID));
			}
		}
	}

	/**
	 * This method used to populate map.
	 */
	private void populateMap() {
		if (IN_MAPLIST != null && IN_MAPLIST.size() > 0) {
			try{
				googleMap.setClustering(new ClusteringSettings().iconDataProvider(new IjoomerMapClusterIconProvider(getResources())).addMarkersDynamically(true));
				for (int i = 0; i < IN_MAPLIST.size(); i++){
					HashMap<String, String> mapItem = IN_MAPLIST.get(i);
					if (mapItem.get(LAT) != null && mapItem.get(LAT).toString().trim().length() > 0 && mapItem.get(LONG) != null
							&& mapItem.get(LONG).toString().trim().length() > 0) {
						mapItem.put(POSITION, ""+i);
						placeMarker(mapItem);
					}
				}
				googleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(IN_MAPLIST.get(0).get(LAT)), Double.parseDouble(IN_MAPLIST.get(0).get(
						LONG)))));
				googleMap.setInfoWindowAdapter(new InfoAdapter());
				googleMap.setOnInfoWindowClickListener(this);
				setUpClusteringViews();
			}catch (Exception e){
				e.printStackTrace();
			}
		} else {
			googleMap.setMyLocationEnabled(true);
		}

		progress.setVisibility(View.GONE);
	}

	/**
	 * This method used to place marker on map.
	 *
	 * @param markerData
	 *            represented markar data.
	 */
	private void placeMarker(final HashMap<String, String> markerData) {
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

	/**
	 * This method used to on map data update.
	 */
	private void onDataUpdate() {
		Marker m = googleMap.getMarkerShowingInfoWindow();
		if (m != null && !m.isCluster() && m.getData() instanceof MutableData) {
			m.showInfoWindow();
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

	/**
	 * Custom marker info adapter.
	 *
	 * @author tasol
	 *
	 */
	class InfoAdapter implements InfoWindowAdapter {

		private TextView tv;
		{
			tv = new TextView(JReviewNearbySearchActivity.this);
			tv.setTextColor(Color.BLACK);
		}

		private Collator collator = Collator.getInstance();
		private Comparator<Marker> comparator = new Comparator<Marker>() {
			public int compare(Marker lhs, Marker rhs) {
				String leftTitle = lhs.getTitle();
				String rightTitle = rhs.getTitle();
				if (leftTitle == null && rightTitle == null) {
					return 0;
				}
				if (leftTitle == null) {
					return 1;
				}
				if (rightTitle == null) {
					return -1;
				}
				return collator.compare(leftTitle, rightTitle);
			}
		};

		@Override
		public View getInfoContents(Marker marker) {
			if (marker.isCluster()) {
				List<Marker> markers = marker.getMarkers();
				int i = 0;
				String text = "";
				while (i < 3 && markers.size() > 0) {
					Marker m = Collections.min(markers, comparator);
					String title = m.getTitle();
					if (title == null) {
						break;
					}
					text += title + "\n";
					markers.remove(m);
					i++;
				}
				if (text.length() == 0) {
					text = "Markers with mutable data";
				} else if (markers.size() > 0) {
					text += "and " + markers.size() + " more...";
				} else {
					text = text.substring(0, text.length() - 1);
				}
				tv.setText(text);
				return tv;
			} else {
				Object data = marker.getData();
				if (data instanceof MutableData) {
					MutableData mutableData = (MutableData) data;
					tv.setText("Value: " + mutableData.value);
					return tv;
				} else {
					return null;
				}
			}
		}

		@Override
		public View getInfoWindow(Marker marker) {
			if (!marker.isCluster()) {
				final HashMap<String, String> data = markerHashMap.get(marker);

				View view = LayoutInflater.from(JReviewNearbySearchActivity.this).inflate(R.layout.jreview_article_map_bubble, null);

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
			} else {
				return null;
			}

		}

	}


	/**
	 * Inner class
	 * @author tasol
	 *
	 */
	private class MutableData {

		private int value;
		@SuppressWarnings("unused")
		private LatLng position;

		public MutableData(int value, LatLng position) {
			this.value = value;
			this.position = position;
		}
	}

}
