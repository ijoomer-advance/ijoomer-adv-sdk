package com.ijoomer.components.sobipro;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.mg6.android.maps.extensions.GoogleMap;
import pl.mg6.android.maps.extensions.GoogleMap.OnMapClickListener;
import pl.mg6.android.maps.extensions.SupportMapFragment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.facebook.Session;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerMapPloyline;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.IjoomerWebviewClient;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.IjoomerRatingBar;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.sobipro.SobiproCategoriesDataProvider;
import com.ijoomer.src.R;
import com.smart.framework.ItemView;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

/**
 * This Fragment Contains All Method Related To SobiproEntryDetailFragment.
 * 
 * @author tasol
 * 
 */
public class SobiproRestaurantEntryDetailFragment extends SmartFragment implements SobiproTagHolder, OnMapClickListener {
	private ArrayList<HashMap<String, String>> entryArrayList;
	private String entryID;
	private String IN_TABLE;
	private ArrayList<SmartListItem> listReviewData;
	private ListView lstEntry;
	private AQuery androidAQuery;
	private boolean showExpandedReview = false;
	private SmartListAdapterWithHolder reviewListAdapterWithHolder;
	private View headerView;
	private ImageView imgEntry;
	private IjoomerTextView txtAddress, txtDistance, txtTitle, txtPrice, txtRestaurantType, txtFavourite, txtHours, txtOffer, txtOfferDate, txtGoodFor,
			txtDescription, txtTotalReview, txtRating, txtLblHours;
	private IjoomerRatingBar rtbRating;
	private SobiproCategoriesDataProvider dataProvider;
	private LinearLayout lnrCall, lnrMenu, lnrCheckin, lnrRating, lnrDirection, lnrRatingAvgLeft, lnrRatingAvgRight;
	private GoogleMap googleMap;
	private String phoneNumber = "";
	private String hoursToday = "", validFor = "", foodType = "", startDate = "", endDate = "";
	private ArrayList<String> permissions = new ArrayList<String>();
	private LinearLayout lnrMap;
	private String IN_PAGELAYOUT;
	private SupportMapFragment mapFragment;
	private LinearLayout lnrOffer;
	private String menu;
	private Timer t = null;
	private ImageView imgRestaurant;
	private PullToRefreshListView mPullRefreshListView;
	public static Context mContext;

	/**
	 * Constructor.
	 * 
	 * @param entryID
	 *            represented selected entry id.
	 * @param IN_TABLE
	 *            represented table name.
	 */

	public SobiproRestaurantEntryDetailFragment(String entryID, String IN_TABLE, String IN_PAGELAYOUT) {
		this.entryID = entryID;
		this.IN_TABLE = IN_TABLE;
		this.IN_PAGELAYOUT = IN_PAGELAYOUT;
	}

	/**
	 * Overrides methods.
	 */

	@Override
	public int setLayoutId() {
		return R.layout.sobipro_entry_detail_fragment_pull_to_gallery;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@SuppressLint("NewApi")
	@Override
	public void initComponents(View currentView) {
		dataProvider = new SobiproCategoriesDataProvider(getActivity());
		imgRestaurant = (ImageView) currentView.findViewById(R.id.imgRestaurant);
		androidAQuery = new AQuery(getActivity());
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		headerView = inflater.inflate(R.layout.sobipro_restaurant_entry_detail_header, null, false);
		listReviewData = new ArrayList<SmartListItem>();
		imgEntry = (ImageView) headerView.findViewById(R.id.imgEntry);
		txtTitle = (IjoomerTextView) headerView.findViewById(R.id.txtTitle);
		txtDistance = (IjoomerTextView) headerView.findViewById(R.id.txtDistance);
		txtAddress = (IjoomerTextView) headerView.findViewById(R.id.txtAddress);
		txtPrice = (IjoomerTextView) headerView.findViewById(R.id.txtPrice);
		txtRestaurantType = (IjoomerTextView) headerView.findViewById(R.id.txtRestaurantType);
		txtFavourite = (IjoomerTextView) headerView.findViewById(R.id.txtFavourite);
		txtHours = (IjoomerTextView) headerView.findViewById(R.id.txtHours);
		txtOffer = (IjoomerTextView) headerView.findViewById(R.id.txtOffer);
		txtOfferDate = (IjoomerTextView) headerView.findViewById(R.id.txtOfferDate);
		txtGoodFor = (IjoomerTextView) headerView.findViewById(R.id.txtGoodFor);
		txtDescription = (IjoomerTextView) headerView.findViewById(R.id.txtDescription);
		txtTotalReview = (IjoomerTextView) headerView.findViewById(R.id.txtTotalReview);
		lnrCall = (LinearLayout) headerView.findViewById(R.id.lnrCall);
		lnrMenu = (LinearLayout) headerView.findViewById(R.id.lnrMenu);
		lnrDirection = (LinearLayout) headerView.findViewById(R.id.lnrDirection);
		lnrCheckin = (LinearLayout) headerView.findViewById(R.id.lnrCheckin);
		lnrRating = (LinearLayout) headerView.findViewById(R.id.lnrRating);
		lnrRatingAvgLeft = (LinearLayout) headerView.findViewById(R.id.lnrRatingAvgLeft);
		lnrRatingAvgRight = (LinearLayout) headerView.findViewById(R.id.lnrRatingAvgRight);
		rtbRating = (IjoomerRatingBar) headerView.findViewById(R.id.rtbRating);
		lnrOffer = (LinearLayout) headerView.findViewById(R.id.lnrOffer);
		lnrMap = (LinearLayout) headerView.findViewById(R.id.lnrMap);
		txtLblHours = (IjoomerTextView) headerView.findViewById(R.id.txtLblHours);
		mPullRefreshListView = (PullToRefreshListView) currentView.findViewById(R.id.lstEntry);
		lstEntry = mPullRefreshListView.getRefreshableView();
		lstEntry.addHeaderView(headerView);
		mContext = getActivity();
	}

	@Override
	public void prepareViews(View currentView) {

		entryArrayList = dataProvider.getEntriesFromCache(IN_TABLE, entryID);
		if ((entryArrayList.get(0).get(LATITUDE) != null && entryArrayList.get(0).get(LATITUDE).length() > 0)
				&& (entryArrayList.get(0).get(LONGITUDE) != null && entryArrayList.get(0).get(LONGITUDE).length() > 0)) {
			lnrMap.setVisibility(View.VISIBLE);
			mapFragment = new SupportMapFragment();
			addFragment(lnrMap.getId(), mapFragment);
			t = new Timer();
			t.schedule(new TimerTask() {

				@Override
				public void run() {
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							try {

								googleMap = mapFragment.getExtendedMap();

								mapFragment.getView().setClickable(false);
								if (googleMap != null) {
									googleMap.getUiSettings().setZoomControlsEnabled(false);
									googleMap.getUiSettings().setCompassEnabled(false);
									googleMap.getUiSettings().setZoomGesturesEnabled(false);
									googleMap.getUiSettings().setScrollGesturesEnabled(false);
									googleMap.getUiSettings().setRotateGesturesEnabled(false);
									googleMap.getUiSettings().setTiltGesturesEnabled(false);
									googleMap.animateCamera(CameraUpdateFactory.zoomTo(6));
									placeMarker();
									t.cancel();
								}
								googleMap.setOnMapClickListener(new OnMapClickListener() {

									@Override
									public void onMapClick(LatLng position) {

										try {
											ArrayList<String> ID_ARRAY = new ArrayList<String>();
											ID_ARRAY.add(entryArrayList.get(0).get(ID));
											((SmartActivity) getActivity()).loadNew(SobiproMapActivity.class, getActivity(), false, "IN_ENTRY_ID_ARRAY", ID_ARRAY, "IN_TABLE",
													SOBIPRO_RESTAURANT_ENTRIES, "IN_PAGELAYOUT", IN_PAGELAYOUT);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								});

							} catch (Exception e) {
							}

						}
					});

				}
			}, 0, 500);
		}
		permissions.addAll(Arrays.asList("publish_checkins", "publish_stream", "publish_actions"));
		if (dataProvider.isFavourite(entryID, IN_PAGELAYOUT)) {
			txtFavourite.setEnabled(false);
			txtFavourite.setBackgroundResource(R.drawable.sobipro_favourite_desabled);
			dataProvider.addToFavourite(entryArrayList, IN_PAGELAYOUT);
		}
		prepareHeader();

		try {
			prepareReviewList(new IjoomerCaching(getActivity()).parseData(new JSONArray(entryArrayList.get(0).get(REVIEWRATING))), false);
		} catch (JSONException e) {
			prepareReviewList(null, false);
			e.printStackTrace();
		}
		reviewListAdapterWithHolder = getReviewListAdapter(listReviewData);
		lstEntry.setAdapter(reviewListAdapterWithHolder);

		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				new GetDataTask().execute();
			}
		});

		mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {

			}
		});

	}

	@Override
	public void setActionListeners(View currentView) {

		txtFavourite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dataProvider.addToFavourite(entryArrayList, IN_PAGELAYOUT);
				txtFavourite.setEnabled(false);
				txtFavourite.setBackgroundResource(R.drawable.sobipro_favourite_desabled);
				Toast.makeText(getActivity(), getString(R.string.sobipro_addtofavorite), Toast.LENGTH_SHORT).show();
				((SmartActivity) getActivity()).loadNew(SobiproFavouriteActivity.class, getActivity(), false);

			}
		});
		lnrCall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (phoneNumber != null && phoneNumber.length() > 0) {
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
					startActivity(intent);
				} else {
					((SmartActivity) getActivity()).ting(getString(R.string.sobipro_not_available_phone));
				}
			}
		});

		lnrDirection.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
					HashMap<String, String> map1 = new HashMap<String, String>();
					map1.put("latitude", ((SmartActivity) getActivity()).getLatitude());
					map1.put("longitude", ((SmartActivity) getActivity()).getLongitude());
					list.add(map1);
					HashMap<String, String> map2 = new HashMap<String, String>();
					map2.put("latitude", entryArrayList.get(0).get(LATITUDE));
					map2.put("longitude", entryArrayList.get(0).get(LONGITUDE));
					list.add(map2);
					((SmartActivity) getActivity()).loadNew(IjoomerMapPloyline.class, getActivity(), false, "IN_ADDRESS_LIST", list, "IN_DESTINATION_ROUND_SHOW", true);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		lnrCheckin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((SmartActivity) getActivity()).loadNew(SobiproFacebookLoginForCheckinActivity.class, getActivity(), false);
			}
		});

		lnrMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					((SmartActivity) getActivity()).loadNew(IjoomerWebviewClient.class, getActivity(), false, "IN_CONTENT", menu);
				} catch (Exception e) {
					e.printStackTrace();
					((SmartActivity) getActivity()).ting(getString(R.string.code204));
				}

			}
		});

		lnrRating.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					((SmartActivity) getActivity()).loadNew(SobiproReviewAddActivity.class, getActivity(), false, "IN_SECTION_ID", entryArrayList.get(0).get(SECTIONID),
							"IN_ENTRY_ID", entryArrayList.get(0).get(ID), "IN_CAT_ID", entryArrayList.get(0).get(CATID));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onPause() {
		try {
			super.onPause();
			t.cancel();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onStop() {
		try {
			super.onStop();
			t.cancel();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Class methods.
	 */

	/**
	 * This method is used to place marker on map.
	 */
	private void placeMarker() {
		try {
			googleMap.addMarker(new MarkerOptions().title(entryArrayList.get(0).get(TITLE)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
					.position(new LatLng(Double.parseDouble(entryArrayList.get(0).get(LATITUDE)), Double.parseDouble(entryArrayList.get(0).get(LATITUDE)))));
			googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(entryArrayList.get(0).get(LATITUDE)), Double.parseDouble(entryArrayList.get(0).get(
					LATITUDE)))));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			{
				Session.getActiveSession().onActivityResult(((SmartActivity) getActivity()), requestCode, resultCode, data);
			}
		}
	}

	// private void fetchFacebookPlaceId() {
	// Session.openActiveSession(getActivity(), true, new
	// Session.StatusCallback() {
	//
	// @Override
	// public void call(Session session, SessionState state, Exception
	// exception) {
	// if (session.isOpened()) {
	// final SeekBar proSeekBar =
	// IjoomerUtilities.getLoadingDialog(getString(R.string.facebook_nearby_progress_title));
	// if (((SmartActivity) getActivity()).getCurrentLocation() != null) {
	// Request.executePlacesSearchRequestAsync(session, ((SmartActivity)
	// getActivity()).getCurrentLocation(), 0, 0, null, new
	// Request.GraphPlaceListCallback() {
	//
	// @Override
	// public void onCompleted(List<GraphPlace> places, final Response response)
	// {
	// proSeekBar.setProgress(100);
	//
	// facebookLocationResponse = new IjoomerCaching((SmartActivity)
	// getActivity()).parseData(response.getGraphObject().getInnerJSONObject());
	// if (facebookLocationResponse != null && facebookLocationResponse.size() >
	// 0) {
	// faceBookCheckin();
	// }
	// }
	// });
	// } else {
	// Toast.makeText(((SmartActivity) getActivity()),
	// getString(R.string.facebook_location_not_found),
	// Toast.LENGTH_LONG).show();
	// }
	// }
	// }
	//
	// });
	// }

	// private void faceBookCheckin() {
	// Session session = Session.openActiveSession(((SmartActivity)
	// getActivity()), true, new Session.StatusCallback() {
	//
	// @Override
	// public void call(Session session, SessionState state, Exception
	// exception) {
	// if (session.isOpened()) {
	// try {
	//
	// if (((SmartActivity) getActivity()).getCurrentLocation() != null) {
	// location.put("latitude", new Double(((SmartActivity)
	// getActivity()).getCurrentLocation().getLatitude()));
	// location.put("longitude", new Double(((SmartActivity)
	// getActivity()).getCurrentLocation().getLongitude()));
	// } else {
	// Toast.makeText(((SmartActivity) getActivity()),
	// getString(R.string.facebook_location_not_found),
	// Toast.LENGTH_LONG).show();
	// return;
	// }
	//
	// } catch (Exception e) {
	//
	// e.printStackTrace();
	// }
	//
	// params = new Bundle();
	//
	// params.putString("place", facebookLocationResponse.get(0).get("id"));
	// params.putString("coordinates", location.toString());
	// }
	//
	// Request.Callback callback = new Request.Callback() {
	//
	// @Override
	// public void onCompleted(Response response) {
	// Toast.makeText(getActivity(), R.string.facebook_post_success,
	// Toast.LENGTH_LONG).show();
	// }
	// };
	// Request request = new Request(session, "me/checkins", params,
	// HttpMethod.POST, callback);
	//
	// RequestAsyncTask task = new RequestAsyncTask(request);
	// task.execute();
	// }
	//
	// });
	// }

	/**
	 * This method is used to handled and displayed some details of entry which
	 * is displayed and managed in listview header.
	 */

	public void prepareHeader() {
		try {

			if (entryArrayList.get(0).get(TITLE).trim().length() > 0) {
				txtTitle.setText(entryArrayList.get(0).get(TITLE));
			} else {
				txtTitle.setVisibility(View.INVISIBLE);
			}
			for (HashMap<String, String> row : entryArrayList) {

				if (row.get(LABELID).equalsIgnoreCase("field_price") && row.get(VALUE).length() > 0) {
					txtPrice.setVisibility(View.VISIBLE);
					int price = Integer.parseInt(row.get(VALUE));
					String price_final = "";
					String currency = row.get(UNIT);
					if (price < 5) {
						for (int i = 0; i < price; i++)
							price_final = price_final + currency;

					} else {
						price_final = currency + currency + currency + currency + currency;
					}
					txtPrice.setText(price_final);
				}

				if (row.get(LABELID).equalsIgnoreCase("field_good_for") && row.get(VALUE).length() > 0) {
					txtGoodFor.setText(Html.fromHtml("<font color='green'><b>" + getResources().getString(R.string.sobipro_restaurant_goodfor) + "</b></font>" + "  "
							+ row.get(VALUE)));

				} else {
					txtGoodFor.setVisibility(View.GONE);
				}

				if (row.get(LABELID).equalsIgnoreCase("field_deal_text") && row.get(VALUE).length() > 0) {
					txtOffer.setText(row.get(VALUE));
					txtOffer.setVisibility(View.VISIBLE);
					lnrOffer.setVisibility(View.VISIBLE);
				}

				if (row.get(LABELID).equalsIgnoreCase("field_address1") && row.get(VALUE).length() > 0) {

					txtAddress.setText(row.get(VALUE));

				}
				if (row.get(LABELID).equalsIgnoreCase("field_address2") && row.get(VALUE).length() > 0) {

					txtAddress.setText(txtAddress.getText() + "," + row.get(VALUE));
				}
				if (row.get(LABELID).equalsIgnoreCase("field_description") && row.get(VALUE).length() > 0) {

					txtDescription.setText(row.get(VALUE));
				} else {
					txtDescription.setVisibility(View.GONE);
				}

				if (row.get(LABELID).equalsIgnoreCase("field_distance") && row.get(VALUE).length() > 0) {
					txtDistance.setVisibility(View.VISIBLE);
					txtDistance.setText(row.get(VALUE) + " " + getActivity().getString(R.string.sobipro_miles_text));
				}

				if (row.get(LABELID).equalsIgnoreCase("field_phone") && row.get(VALUE).length() > 0) {
					phoneNumber = row.get(VALUE);
				}
				if (row.get(LABELID).equalsIgnoreCase("field_working_hours") && row.get(VALUE).length() > 0) {
					hoursToday = row.get(VALUE);
				}
				if (row.get(LABELID).equalsIgnoreCase("field_food_type") && row.get(VALUE).length() > 0) {
					foodType = row.get(VALUE);
				}
				if (row.get(LABELID).equalsIgnoreCase("field_valid_for") && row.get(VALUE).length() > 0) {
					validFor = row.get(VALUE);
					txtOfferDate.setText(validFor);
					txtOfferDate.setVisibility(View.VISIBLE);
					lnrOffer.setVisibility(View.VISIBLE);
				}
				if (row.get(LABELID).equalsIgnoreCase("field_deal_start") && row.get(VALUE).length() > 0) {
					startDate = row.get(VALUE);
				}
				if (row.get(LABELID).equalsIgnoreCase("field_deal_end") && row.get(VALUE).length() > 0) {
					endDate = row.get(VALUE);
				}
				if (row.get(LABELID).equalsIgnoreCase("field_menu") && row.get(VALUE).length() > 0) {
					menu = row.get(VALUE);
				}

			}

			try {
				double dist = 0;
				if ((int) Double.parseDouble(entryArrayList.get(0).get("distance")) > 0) {
					dist = IjoomerUtilities.convertDistance(entryArrayList.get(0).get("distance"), IjoomerUtilities.MILE, IjoomerUtilities.MILE);
					txtDistance.setText(dist + " " + getActivity().getString(R.string.sobipro_miles_text));
					txtDistance.setVisibility(View.VISIBLE);
				} else {
					dist = IjoomerUtilities.convertDistance(entryArrayList.get(0).get("distance"), IjoomerUtilities.DEGREE, IjoomerUtilities.MILE);
					txtDistance.setText(dist + " " + getActivity().getString(R.string.sobipro_miles_text));
					txtDistance.setVisibility(View.VISIBLE);
				}
			} catch (Exception e) {

			}
			String mainOfferDate = "";
			if (validFor.trim().length() > 0 && startDate.trim().length() > 0 && endDate.trim().length() > 0) {
				mainOfferDate = getString(R.string.sobipro_validfor) + validFor + " " + getString(R.string.sobipro_startdate) + startDate + " "
						+ getString(R.string.sobipro_enddate) + endDate;
			} else if (validFor.trim().length() == 0 && startDate.trim().length() > 0 && endDate.trim().length() > 0) {
				mainOfferDate = getString(R.string.sobipro_startdate) + startDate + " " + getString(R.string.sobipro_enddate) + endDate;
			} else {
				mainOfferDate = "";
			}
			txtOfferDate.setText(mainOfferDate);

			txtTotalReview.setText(getResources().getString(R.string.sobipro_reviews, entryArrayList.get(0).get("totalreviewcount")));
			try {
				JSONArray criterionaverage = new JSONArray(entryArrayList.get(0).get(CRITERIONAVERAGE));

				for (int i = 0; i < criterionaverage.length(); i++) {
					LinearLayout layout = new LinearLayout(getActivity());
					LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					LinearLayout.LayoutParams captionParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
					LinearLayout.LayoutParams ratingParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

					IjoomerRatingBar rtb = new IjoomerRatingBar(getActivity());

					rtb.setStarSize(11);
					rtb.setFilledStarResourceId(R.drawable.sobipro_rating_transparent_entry_detail);
					rtb.setHalfFilledStarResourceId(R.drawable.sobipro_rating_transparent_half_entry_detail);
					rtb.setEmptyStarResourceId(R.drawable.sobipro_rating_empty_star);
					rtb.setStarBgColor(getResources().getColor(R.color.sobipro_green));
					rtb.setStarRating(Float.parseFloat(((JSONObject) criterionaverage.get(i)).get(RATINGVOTE).toString()) / 2);
					ratingParam.setMargins(10, 0, 5, 0);
					layout.setGravity(Gravity.CENTER);
					IjoomerTextView txtCaption = new IjoomerTextView(getActivity());
					txtCaption.setGravity(Gravity.LEFT);
					txtCaption.setText(((JSONObject) criterionaverage.get(i)).get(CRITERIONNAME).toString());
					txtCaption.setSingleLine(true);
					txtCaption.setTextAppearance(getActivity(), R.style.ijoomer_textview_h2);
					txtCaption.setTextColor(getResources().getColor(R.color.sobipro_green));
					layout.addView(rtb, ratingParam);
					layout.addView(txtCaption, captionParam);
					if (i % 2 == 0)
						lnrRatingAvgLeft.addView(layout, param);
					else
						lnrRatingAvgRight.addView(layout);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				String[] image = ((IjoomerSuperMaster) getActivity()).getStringArray(entryArrayList.get(0).get(IMG_GALLERIES));

				androidAQuery.id(imgEntry).image(image[0], true, true, ((SmartActivity) getActivity()).getDeviceWidth(), R.drawable.sobipro_entry_default);
				androidAQuery.id(imgRestaurant).image(image[0], true, true, ((SmartActivity) getActivity()).getDeviceWidth(), R.drawable.sobipro_entry_default);

			} catch (Exception e) {
				e.printStackTrace();
			}
			rtbRating.setFilledStarResourceId(R.drawable.sobipro_rating_transparent_entry_detail);
			rtbRating.setHalfFilledStarResourceId(R.drawable.sobipro_rating_transparent_half_entry_detail);
			rtbRating.setEmptyStarResourceId(R.drawable.sobipro_rating_empty_star);
			rtbRating.setStarBgColor(getActivity().getResources().getColor(R.color.sobipro_green));
			rtbRating.setStarSize(14);
			rtbRating.setStarRating(Float.parseFloat(entryArrayList.get(0).get(AVERAGERATING)) / 2);
			if (foodType.trim().length() > 0) {
				txtRestaurantType.setText(foodType);
			} else {
				txtRestaurantType.setVisibility(View.INVISIBLE);
			}
			if (hoursToday.trim().length() > 0) {
				txtHours.setText(hoursToday);
				txtLblHours.setVisibility(View.VISIBLE);
			} else {
				txtHours.setVisibility(View.INVISIBLE);
			}
			if (entryArrayList.get(0).get(AVERAGERATING) != null)
				txtRating.setText(getString(R.string.sobipro_restaurant_detail_rating) + "(" + (Float.parseFloat(entryArrayList.get(0).get(AVERAGERATING)) / 2) + "/5");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * List adapter for review list.
	 * 
	 * @param listData
	 *            represented review data
	 * @return represented {@link SmartListAdapterWithHolder}
	 */

	public SmartListAdapterWithHolder getReviewListAdapter(ArrayList<SmartListItem> listData) {

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.sobipro_restaurant_review_list_item, listData, new ItemView() {

			@Override
			public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {
				holder.lnrReview = (LinearLayout) v.findViewById(R.id.lnrReview);
				holder.txtAddReview = (IjoomerTextView) v.findViewById(R.id.txtAddReview);
				holder.txtTitle = (IjoomerTextView) v.findViewById(R.id.txtTitle);
				holder.rtbRating = (IjoomerRatingBar) v.findViewById(R.id.rtbRating);
				holder.txtReview = (IjoomerTextView) v.findViewById(R.id.txtReview);
				holder.lnrPros = (LinearLayout) v.findViewById(R.id.lnrPros);
				holder.lnrCons = (LinearLayout) v.findViewById(R.id.lnrCons);
				holder.txtPositiveReview = (IjoomerTextView) v.findViewById(R.id.txtPositiveReview);
				holder.txtNegativeReview = (IjoomerTextView) v.findViewById(R.id.txtNegativeReview);
				holder.lnrRatingCriteria = (LinearLayout) v.findViewById(R.id.lnrRatingCriteria);
				holder.txtProsTitle = (IjoomerTextView) v.findViewById(R.id.txtProsTitle);
				holder.txtConsTitle = (IjoomerTextView) v.findViewById(R.id.txtConsTitle);
				holder.lnrRatingLeft = (LinearLayout) v.findViewById(R.id.lnrRatingLeft);
				holder.lnrRatingRight = (LinearLayout) v.findViewById(R.id.lnrRatingRight);
				holder.txtPlus = (IjoomerTextView) v.findViewById(R.id.txtMore);
				holder.lnrExpandedView = (LinearLayout) v.findViewById(R.id.lnrExpandedView);
				holder.imgSeparator = (ImageView) v.findViewById(R.id.imgSeparator);
				holder.txtLess = (IjoomerTextView) v.findViewById(R.id.txtLess);
				holder.txtReviewOn = (IjoomerTextView) v.findViewById(R.id.txtReviewOn);
				try {
					@SuppressWarnings("unchecked")
					HashMap<String, String> value = (HashMap<String, String>) item.getValues().get(0);

					holder.txtTitle.setText('"' + value.get(REVIEWTITLE) + '"');
					holder.rtbRating.setFilledStarResourceId(R.drawable.sobipro_rating_transparent_entry_detail);
					holder.rtbRating.setHalfFilledStarResourceId(R.drawable.sobipro_rating_transparent_half_entry_detail);
					holder.rtbRating.setEmptyStarResourceId(R.drawable.sobipro_rating_empty_star);
					holder.rtbRating.setStarBgColor(getActivity().getResources().getColor(R.color.sobipro_green));
					holder.rtbRating.setStarSize(14);
					holder.rtbRating.setStarRating(Float.parseFloat(value.get(AVERAGERATING)) / 2);
					holder.txtReview.setText(value.get(REVIEW));

					String date = changeDateFormat(value.get(REVIEWDATE));
					holder.txtReviewOn.setText(getResources().getString(R.string.sobipro_review_date, date));

					holder.txtPlus.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							showExpandedReview = true;
							reviewListAdapterWithHolder.notifyDataSetChanged();
							lstEntry.setSelection(position + 1);

						}
					});
					holder.txtLess.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							showExpandedReview = false;
							reviewListAdapterWithHolder.notifyDataSetChanged();
							lstEntry.setSelection(position + 1);
						}
					});
					if (showExpandedReview) {
						holder.txtPlus.setVisibility(View.GONE);
						holder.lnrExpandedView.setVisibility(View.VISIBLE);

						if (value.get(REVIEWPOSITIVES) != null && value.get(REVIEWPOSITIVES).length() > 0) {
							holder.lnrPros.setVisibility(View.VISIBLE);
							holder.txtPositiveReview.setText(value.get(REVIEWPOSITIVES));

						}
						if (value.get(REVIEWNEGATIVES) != null && value.get(REVIEWNEGATIVES).length() > 0) {
							holder.lnrCons.setVisibility(View.VISIBLE);
							holder.txtNegativeReview.setText(value.get(REVIEWNEGATIVES));
						}

						ArrayList<HashMap<String, String>> ratingCriterias = null;
						try {
							ratingCriterias = new IjoomerCaching(getActivity()).parseData(new JSONArray(value.get(RATINGS)));

							holder.lnrRatingLeft.removeAllViews();
							holder.lnrRatingRight.removeAllViews();

							for (int i = 0; i < ratingCriterias.size(); i++) {

								LinearLayout layout = new LinearLayout(getActivity());
								LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
								LinearLayout.LayoutParams captionParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
										LinearLayout.LayoutParams.WRAP_CONTENT, 1);
								LinearLayout.LayoutParams ratingParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
										LinearLayout.LayoutParams.WRAP_CONTENT);

								IjoomerRatingBar rtb = new IjoomerRatingBar(getActivity());

								rtb.setStarSize(11);
								rtb.setFilledStarResourceId(R.drawable.sobipro_rating_transparent_entry_detail);
								rtb.setHalfFilledStarResourceId(R.drawable.sobipro_rating_transparent_half_entry_detail);
								rtb.setEmptyStarResourceId(R.drawable.sobipro_rating_empty_star);
								rtb.setStarBgColor(getActivity().getResources().getColor(R.color.sobipro_green));
								rtb.setStarRating(Float.parseFloat(ratingCriterias.get(i).get(RATINGVOTE)) / 2);
								rtb.setGravity(Gravity.LEFT);
								ratingParam.setMargins(10, 0, 5, 0);
								layout.setGravity(Gravity.CENTER);
								IjoomerTextView txtCaption = new IjoomerTextView(getActivity());
								txtCaption.setText(ratingCriterias.get(i).get(CRITERIONNAME));
								txtCaption.setSingleLine(true);
								txtCaption.setTextAppearance(getActivity(), R.style.ijoomer_textview_h2);
								txtCaption.setTextColor(getResources().getColor(R.color.sobipro_green));
								layout.addView(rtb, ratingParam);
								layout.addView(txtCaption, captionParam);
								if (i % 2 == 0)
									holder.lnrRatingLeft.addView(layout, param);
								else
									holder.lnrRatingRight.addView(layout);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} else {
						holder.txtPlus.setVisibility(View.VISIBLE);
						holder.lnrExpandedView.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					e.printStackTrace();
					holder.lnrReview.setVisibility(View.GONE);
					holder.txtAddReview.setVisibility(View.VISIBLE);
					holder.txtAddReview.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							try {
								((SmartActivity) getActivity()).loadNew(SobiproReviewAddActivity.class, getActivity(), false, "IN_SECTION_ID",
										entryArrayList.get(0).get(SECTIONID), "IN_ENTRY_ID", entryArrayList.get(0).get(ID), "IN_CAT_ID", entryArrayList.get(0).get(CATID));
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
					});
				}
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
	 * This method is used to prepare initial list from response data for review
	 * list.
	 * 
	 * @param data
	 *            : data from response.
	 * @param append
	 *            represented for paging to show new data in same page.
	 * 
	 * 
	 */
	public void prepareReviewList(ArrayList<HashMap<String, String>> data, boolean append) {

		if (data != null) {

			listReviewData.clear();
			for (HashMap<String, String> hashMap : data) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.sobipro_restaurant_review_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hashMap);

				item.setValues(obj);

				listReviewData.add(item);
			}

		} else {
			SmartListItem item = new SmartListItem();
			item.setItemLayout(R.layout.sobipro_restaurant_review_list_item);
			ArrayList<Object> obj = new ArrayList<Object>();
			obj.add("");
			item.setValues(obj);
			listReviewData.add(item);
		}

	}

	@Override
	public void onMapClick(LatLng position) {

	}

	/**
	 * This methos is used to change Date format from yyyy-MM-dd hh:mm:ss to MMM
	 * dd yyyy
	 * 
	 * @param date
	 *            represents the Date which we need to format.
	 * @return changed Date.
	 */

	@SuppressLint("SimpleDateFormat")
	private String changeDateFormat(String date) {
		SimpleDateFormat recivedFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd yyyy");
		Date dateObj = null;
		try {
			try {
				dateObj = recivedFormat.parse(date);
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "-";
		}
		return outputFormat.format(dateObj);
	}

	/**
	 * Inner Class. It is used to make Pull to view gallery view.
	 * 
	 * @author tasol
	 * 
	 */

	private class GetDataTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
			}
			return "1";
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.equalsIgnoreCase("1")) {
				// lstEntry.setAdapter(reviewListAdapterWithHolder);
				mPullRefreshListView.onRefreshComplete();
				try {
					((SmartActivity) getActivity()).loadNew(SobiproGalleryActivity.class, getActivity(), false, "IN_IMAGES", entryArrayList.get(0).get(IMG_GALLERIES), "IN_INDEX",
							0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// super.onPostExecute(result);
		}
	}
}
