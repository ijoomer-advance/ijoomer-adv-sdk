package com.ijoomer.components.sobipro;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import pl.mg6.android.maps.extensions.ClusteringSettings;
import pl.mg6.android.maps.extensions.GoogleMap;
import pl.mg6.android.maps.extensions.GoogleMap.InfoWindowAdapter;
import pl.mg6.android.maps.extensions.GoogleMap.OnInfoWindowClickListener;
import pl.mg6.android.maps.extensions.Marker;
import pl.mg6.android.maps.extensions.SupportMapFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
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
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.customviews.IjoomerRatingBar;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.sobipro.SobiproCategoriesDataProvider;
import com.ijoomer.src.R;

/**
 * Activity class for SobiproMapActivity view
 * 
 * @author tasol
 * 
 */

public class SobiproMapActivity extends SobiproMasterActivity implements SobiproTagHolder {
	private HashMap<Marker, HashMap<String, String>> markerHashMap;
	private GoogleMap googleMap;
	private AQuery androidQuery;
	private Bitmap bitmapScale;
	private Bitmap bitmapCreate;
	private static final double[] CLUSTER_SIZES = new double[] { 180, 160, 144, 120, 96 };
	private MutableData[] dataArray = { new MutableData(6, new LatLng(-50, 0)), new MutableData(28, new LatLng(-52, 1)), new MutableData(496, new LatLng(-51, -2)), };
	private Handler handler = new Handler();
	private String IN_TABLE;
	private ArrayList<String> IN_ENTRY_ID_ARRAY;
	private SobiproCategoriesDataProvider dataProvider;
	private int IN_POS;
	private String IN_PAGELAYOUT;
	private ArrayList<String> pageLayouts;

	/**
	 * This method represented to manage updated data.
	 */
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

	/**
	 * Overrides methods.
	 */

	@Override
	protected void onResume() {
		super.onResume();
		handler.post(dataUpdater);
	}

	@Override
	protected void onPause() {
		super.onPause();
		handler.removeCallbacks(dataUpdater);
	}

	@Override
	public int setLayoutId() {
		return R.layout.sobipro_map;
	}

	@Override
	public void prepareViews() {
	}

	@Override
	public void setActionListeners() {
	}

	@Override
	public void initComponents() {

		FragmentManager fm = getSupportFragmentManager();
		SupportMapFragment f = (SupportMapFragment) fm.findFragmentById(R.id.maps);
		googleMap = f.getExtendedMap();
		markerHashMap = new HashMap<Marker, HashMap<String, String>>();
		androidQuery = new AQuery(this);
		dataProvider = new SobiproCategoriesDataProvider(SobiproMapActivity.this);
		getIntentData();
		pageLayouts = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.sobipro_pageLayout)));
		androidQuery = new AQuery(SobiproMapActivity.this);
		googleMap.setClustering(new ClusteringSettings().iconDataProvider(new IjoomerMapClusterIconProvider(getResources())).addMarkersDynamically(true));

		googleMap.setInfoWindowAdapter(new InfoWindowAdapter() {

			private TextView tv;
			{

				tv = new TextView(SobiproMapActivity.this);
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
					}
				}
				return null;
			}

			@Override
			public View getInfoWindow(Marker marker) {
				try {
					if (!marker.isCluster()) {

						final HashMap<String, String> data = markerHashMap.get(marker);
						View view;
						switch (pageLayouts.indexOf(IN_PAGELAYOUT)) {

						case 2:
							view = LayoutInflater.from(SobiproMapActivity.this).inflate(R.layout.sobipro_restaurant_map_bubble, null);
							IjoomerTextView txtRestaurantTitle = (IjoomerTextView) view.findViewById(R.id.txtTitle);

							IjoomerRatingBar rtbRestaurantRating = (IjoomerRatingBar) view.findViewById(R.id.rtbRating);

							IjoomerTextView txtRestaurantDistance = (IjoomerTextView) view.findViewById(R.id.txtDistance);

							IjoomerTextView txtRestaurantPrice = (IjoomerTextView) view.findViewById(R.id.txtPrice);

							IjoomerTextView txtRestaurantGoodFor = (IjoomerTextView) view.findViewById(R.id.txtGoodFor);

							if (data.get("txtTitle") != null && data.get("txtTitle").length() > 0) {
								txtRestaurantTitle.setText(data.get("txtTitle"));
								txtRestaurantTitle.setVisibility(View.VISIBLE);
							}
							if (data.get("txtDistance") != null && data.get("txtDistance").length() > 0) {

								txtRestaurantDistance.setText(IjoomerUtilities.convertDistance(data.get("txtDistance"), IjoomerUtilities.MILE, IjoomerUtilities.MILE) + " "
										+ getString(R.string.sobipro_restaurant_miles_text));
								txtRestaurantDistance.setVisibility(View.VISIBLE);
							}
							if (data.get("txtPrice") != null && data.get("txtPrice").length() > 0) {
								txtRestaurantPrice.setText(data.get("txtPrice"));
								txtRestaurantPrice.setVisibility(View.VISIBLE);
							}
							if (data.get("txtGoodFor") != null && data.get("txtGoodFor").length() > 0) {
								txtRestaurantGoodFor.setText(data.get("txtGoodFor"));
								txtRestaurantGoodFor.setVisibility(View.VISIBLE);
							}
							if (data.get("txtAvgRating") != null && data.get("txtAvgRating").length() > 0) {
								rtbRestaurantRating.setFilledStarResourceId(R.drawable.sobipro_rating_transparent_entry_detail);
								rtbRestaurantRating.setHalfFilledStarResourceId(R.drawable.sobipro_rating_transparent_half);
								rtbRestaurantRating.setEmptyStarResourceId(R.drawable.sobipro_rating_empty_star);
								rtbRestaurantRating.setStarBgColor(getResources().getColor(R.color.sobipro_green));
								rtbRestaurantRating.setStarRating(Float.parseFloat(data.get("txtAvgRating")) / 2);

								rtbRestaurantRating.setVisibility(View.VISIBLE);
							}
							return view;
						default:

							view = LayoutInflater.from(SobiproMapActivity.this).inflate(R.layout.sobipro_map_bubble, null);

							LinearLayout lnrMapInfoWindow = (LinearLayout) view.findViewById(R.id.lnrMapInfoWindow);

							IjoomerTextView txtTitle = (IjoomerTextView) view.findViewById(R.id.txtTitlePopup);

							IjoomerRatingBar rtbRating = (IjoomerRatingBar) view.findViewById(R.id.rtbRatingPopup);

							IjoomerTextView txtReview = (IjoomerTextView) view.findViewById(R.id.txtReviewPopup);

							IjoomerTextView txtAddress = (IjoomerTextView) view.findViewById(R.id.txtAddressPopup);

							IjoomerTextView txtDistance = (IjoomerTextView) view.findViewById(R.id.txtDistancePopup);

							IjoomerTextView txtPrice = (IjoomerTextView) view.findViewById(R.id.txtPricePopup);

							if (data.get("txtTitle") != null && data.get("txtTitle").length() > 0) {
								txtTitle.setText(data.get("txtTitle"));
							} else {
								txtTitle.setVisibility(View.GONE);
							}

							if (data.get("txtAddress") != null && data.get("txtAddress").length() > 0) {
								txtAddress.setText(data.get("txtAddress"));
							} else {
								txtAddress.setVisibility(View.GONE);
							}
							try {
								if (data.get("txtDistance") != null && data.get("txtDistance").length() > 0) {
									txtDistance.setText(data.get("txtDistance") + " " + SobiproMapActivity.this.getString(R.string.sobipro_miles_text));
								} else {
									txtDistance.setVisibility(View.GONE);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}

							if (data.get("txtReviewCount") != null && data.get("txtReviewCount").length() > 0) {
								txtReview.setText(data.get("txtReviewCount") + " " + (SobiproMapActivity.this.getString(R.string.sobipro_detail_review_text)).toLowerCase());
							} else {
								txtReview.setVisibility(View.GONE);
							}

							if (data.get("txtPrice") != null && data.get("txtPrice").length() > 0) {
								if (data.get("txtPrice") != null && data.get("txtPrice").length() > 0) {
									int price = Integer.parseInt(data.get("txtPrice"));
									String price_final = "";
									String currency = data.get("txtCurrency");
									for (int i = 0; i < price; i++)
										price_final = price_final + currency;
									txtPrice.setText(price_final);
								} else {
									txtPrice.setVisibility(View.GONE);
								}

							}
							rtbRating.setFilledStarResourceId(R.drawable.sobipro_rating_transparent);
							rtbRating.setHalfFilledStarResourceId(R.drawable.sobipro_rating_transparent_half);
							rtbRating.setEmptyStarResourceId(R.drawable.sobipro_rating_empty_star);
							rtbRating.setStarBgColor(SobiproMasterActivity.themes[IN_POS].getBgColor());
							rtbRating.setStarRating(Float.parseFloat(data.get("txtAvgRating")) / 2);
							lnrMapInfoWindow.setBackgroundColor(SobiproMasterActivity.themes[IN_POS].getBgColor());
							return view;

						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});

		googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

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
					final HashMap<String, String> data = markerHashMap.get(marker);
					goToEntryDetail(data);
					marker.hideInfoWindow();
				}
			}
		});

		// MarkerGenerator.addMarkersInPoland(googleMap);
		// MarkerGenerator.addMarkersInWorld(googleMap);

		populateMap();
		setUpClusteringViews();
	}

	/**
	 * Class methods.
	 */

	/**
	 * This method used to get intent data.
	 */

	private void getIntentData() {
		try {
			IN_ENTRY_ID_ARRAY = getIntent().getStringArrayListExtra("IN_ENTRY_ID_ARRAY");
			IN_TABLE = getIntent().getStringExtra("IN_TABLE");
			IN_POS = getIntent().getIntExtra("IN_POS", 0);
			IN_PAGELAYOUT = getIntent().getStringExtra("IN_PAGELAYOUT");

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	/**
	 * This method is used to go in Entry Detail after clicking InfoWindow on
	 * map.
	 * 
	 * @param data
	 *            represents entry detail data which is going to display.
	 * 
	 */

	private void goToEntryDetail(HashMap<String, String> data) {
		try {
			ArrayList<String> ID_Array = new ArrayList<String>();
			ID_Array.add(data.get(ID));
			loadNew(SobiproEntryDetailActivity.class, SobiproMapActivity.this, false, "IN_ENTRY_ID_ARRAY", ID_Array, "IN_ENTRY_INDEX", 0, "IN_TABLE", IN_TABLE, "IN_POS", IN_POS,
					"IN_PAGELAYOUT", IN_PAGELAYOUT);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method is used to update the Data.
	 */

	private void onDataUpdate() {
		Marker m = googleMap.getMarkerShowingInfoWindow();
		if (m != null && !m.isCluster() && m.getData() instanceof MutableData) {
			m.showInfoWindow();
		}
	}

	/**
	 * This method is used to set map clustering view.
	 */

	private void setUpClusteringViews() {
		ClusteringSettings clusteringSettings = new ClusteringSettings();
		clusteringSettings.addMarkersDynamically(true);

		clusteringSettings.iconDataProvider(new IjoomerMapClusterIconProvider(getResources()));

		double clusterSize = CLUSTER_SIZES[3];
		clusteringSettings.clusterSize(clusterSize);

		googleMap.setClustering(clusteringSettings);
	}

	/**
	 * This method is used to populate map view with clustering.
	 */
	private void populateMap() {
		try {
			if (IN_ENTRY_ID_ARRAY != null && IN_ENTRY_ID_ARRAY.size() > 0) {

				for (int i = 0; i < IN_ENTRY_ID_ARRAY.size(); i++) {
					ArrayList<HashMap<String, String>> hashMap = dataProvider.getEntriesFromCache(IN_TABLE, IN_ENTRY_ID_ARRAY.get(i));
					try {
						if (i == 0) {
							googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(hashMap.get(i).get(LATITUDE)), Double.parseDouble(hashMap.get(i).get(
									LONGITUDE)))));
						}

						final HashMap<String, String> entryData = new HashMap<String, String>();
						switch (pageLayouts.indexOf(IN_PAGELAYOUT)) {

						case 2:

							try {
								if (hashMap.get(0).get(LATITUDE) != null && hashMap.get(0).get(LATITUDE).toString().trim().length() > 0 && hashMap.get(0).get(LONGITUDE) != null
										&& hashMap.get(0).get(LONGITUDE).toString().trim().length() > 0) {
									for (HashMap<String, String> row : hashMap) {
										if (row.get(LABELID).equalsIgnoreCase("field_good_for")) {
											entryData.put("txtGoodFor", row.get(VALUE));
										} else if (row.get(LABELID).equalsIgnoreCase("field_price")) {

											int price = Integer.parseInt(row.get(VALUE));
											String price_final = "";
											String currency = row.get(UNIT);
											if (price < 5) {
												for (int j = 0; j < price; j++)
													price_final = price_final + currency;

											} else {
												price_final = currency + currency + currency + currency + currency;
											}
											entryData.put("txtPrice", price_final);

										}

									}
									entryData.put("txtDistance", hashMap.get(0).get("distance"));
									entryData.put("txtTitle", hashMap.get(0).get(TITLE));
									entryData.put(ID, hashMap.get(0).get(ID));
									entryData.put("txtAvgRating", hashMap.get(0).get(AVERAGERATING));
									if (hashMap.get(0).get("img_galleries") != null
											&& ((IjoomerSuperMaster) SobiproMapActivity.this).getStringArray(hashMap.get(0).get("img_galleries"))[0] != null) {
										entryData.put("imgLogo", ((IjoomerSuperMaster) SobiproMapActivity.this).getStringArray(hashMap.get(0).get("img_galleries"))[0]);
									}
									entryData.put(LATITUDE, hashMap.get(0).get("latitude"));
									entryData.put(LONGITUDE, hashMap.get(0).get("longitude"));
									googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(hashMap.get(i).get(LATITUDE)), Double.parseDouble(hashMap.get(
											i).get(LONGITUDE)))));
									placeMarker(entryData, R.drawable.sobipro_map_custom_marker_green);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}

							break;
						default:

							try {

								if (hashMap.get(0).get(LATITUDE) != null && hashMap.get(0).get(LATITUDE).toString().trim().length() > 0 && hashMap.get(0).get(LONGITUDE) != null
										&& hashMap.get(0).get(LONGITUDE).toString().trim().length() > 0) {
									for (HashMap<String, String> row : hashMap) {
										if (row.get(LABELID).equalsIgnoreCase(FIELDCOMPANYLOGO)) {
											entryData.put("imgLogo", row.get(VALUE));
										}
										if (row.get(LABELID).equalsIgnoreCase(FIELDADDRESS)) {
											entryData.put("txtAddress", row.get(VALUE));
										}
										if (row.get(LABELID).equalsIgnoreCase(FIELDPRICE)) {
											entryData.put("txtPrice", row.get(VALUE));
											entryData.put("txtCurrency", row.get(UNIT));
										}
									}

									try {
										double dist = distanceFrom(Float.parseFloat(getLatitude()), Float.parseFloat(getLongitude()),
												Float.parseFloat(hashMap.get(0).get(LATITUDE)), Float.parseFloat(hashMap.get(0).get(LONGITUDE)));
										entryData.put("txtDistance", dist + "");

									} catch (Exception e) {
									}
									entryData.put("txtTitle", hashMap.get(0).get("title"));
									entryData.put(ID, hashMap.get(0).get(ID));
									entryData.put("txtAvgRating", hashMap.get(0).get(AVERAGERATING));
									entryData.put("txtReviewCount", hashMap.get(0).get(TOTALREVIEWCOUNT));

									entryData.put(LATITUDE, hashMap.get(0).get("latitude"));
									entryData.put(LONGITUDE, hashMap.get(0).get("longitude"));
									googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(hashMap.get(i).get(LATITUDE)), Double.parseDouble(hashMap.get(
											i).get(LONGITUDE)))));
									placeMarker(entryData, SobiproMasterActivity.themes[IN_POS].getMapMarkerDrawable());
								}
							} catch (Exception e) {
								e.printStackTrace();
							}

							break;

						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			} else {
				googleMap.setMyLocationEnabled(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is used to place markers at specific latitude and longitude
	 * 
	 * @param entryData
	 *            represented entry data which contains all the latitude and
	 *            longitude which will displayed on map.
	 */

	private void placeMarker(final HashMap<String, String> entryData, final int FrameDrawable) {
		try {
			androidQuery.ajax(entryData.get("imgLogo"), Bitmap.class, 0, new AjaxCallback<Bitmap>() {
				@Override
				public void callback(String url, Bitmap object, AjaxStatus status) {
					super.callback(url, object, status);

					if (object == null) {
						object = BitmapFactory.decodeResource(getResources(), R.drawable.sobipro_default_image);
					}
					markerHashMap.put(
							googleMap.addMarker(new MarkerOptions()
									.icon(BitmapDescriptorFactory.fromBitmap(combineImages(BitmapFactory.decodeResource(getResources(), FrameDrawable), object)))
									.title(entryData.get("txtTitle"))
									.position(new LatLng(Double.parseDouble(entryData.get(LATITUDE)), Double.parseDouble(entryData.get(LONGITUDE))))), entryData);

				}
			});
		} catch (Exception e) {

			markerHashMap.put(
					googleMap.addMarker(new MarkerOptions()
							.icon(BitmapDescriptorFactory.fromBitmap(combineImages(BitmapFactory.decodeResource(getResources(), FrameDrawable),
									BitmapFactory.decodeResource(getResources(), R.drawable.sobipro_default_image)))).title(entryData.get("txtTitle"))
							.position(new LatLng(Double.parseDouble(entryData.get(LATITUDE)), Double.parseDouble(entryData.get(LONGITUDE))))), entryData);
			e.printStackTrace();
		}

	}

	/**
	 * This method is used to combine frame image and entry image and make one
	 * image which will used to display as a marker.
	 * 
	 * @param frame
	 *            represented frame image.
	 * @param image
	 *            represented entry image.
	 * @return
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
	 * Custom class to handle Mutable Data.
	 */
	private static class MutableData {
		private int value;

		public MutableData(int value, LatLng position) {
			this.value = value;
		}
	}
}
