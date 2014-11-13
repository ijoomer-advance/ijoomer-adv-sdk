package com.ijoomer.common.classes;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ijoomer.src.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.mg6.android.maps.extensions.GoogleMap;

/**
 * This Class Contains All Method Related To IjoomerMapPloyline.
 * 
 * @author tasol
 * 
 */
public class IjoomerMapPloyline extends IjoomerSuperMaster {

	private GoogleMap googleMap;

	ArrayList<HashMap<String, String>> IN_ADDRESS_LIST;

	private boolean IN_DESTINATION_ROUND_SHOW;

	/**
	 * Overrides method
	 */

	@Override
	public int setLayoutId() {
		return R.layout.ijoomer_map_ployline;
	}

	@Override
	public void initComponents() {
		googleMap = getMapView();
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(3));

		getIntentData();
	}

	@Override
	public void prepareViews() {
		try {
			if (IN_ADDRESS_LIST.size() >= 2) {
				new connectAsyncTask(makeURL(
						Double.parseDouble(IN_ADDRESS_LIST.get(0).get(
								"latitude")),
						Double.parseDouble(IN_ADDRESS_LIST.get(0).get(
								"longitude")),
						Double.parseDouble(IN_ADDRESS_LIST.get(1).get(
								"latitude")),
						Double.parseDouble(IN_ADDRESS_LIST.get(1).get(
								"longitude")))).execute();
			}
		} catch (Exception e) {
		}
	}

	@SuppressWarnings("unchecked")
	private void getIntentData() {
		IN_ADDRESS_LIST = ((ArrayList<HashMap<String, String>>) getIntent()
				.getSerializableExtra("IN_ADDRESS_LIST") == null ? new ArrayList<HashMap<String, String>>()
				: (ArrayList<HashMap<String, String>>) getIntent()
						.getSerializableExtra("IN_ADDRESS_LIST"));
		IN_DESTINATION_ROUND_SHOW = getIntent().getBooleanExtra(
				"IN_DESTINATION_ROUND_SHOW", false);

	}

	@Override
	public int setFooterLayoutId() {
		return 0;
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {

	}

	@Override
	public void setActionListeners() {
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

	/**
	 * Class method
	 */

	/**
	 * This method used to get between shortest latitude-longitude list start to
	 * end latitude-longitude.
	 * 
	 * @param sourcelat
	 *            represented start latitude
	 * @param sourcelog
	 *            represented start longitude
	 * @param destlat
	 *            represented destination latitude
	 * @param destlog
	 *            represented destination longitude
	 * @return represented {@link String}
	 */
	public String makeURL(double sourcelat, double sourcelog, double destlat,
			double destlog) {
		StringBuilder urlString = new StringBuilder();
		urlString.append("http://maps.googleapis.com/maps/api/directions/json");
		urlString.append("?origin=");// from
		urlString.append(Double.toString(sourcelat));
		urlString.append(",");
		urlString.append(Double.toString(sourcelog));
		urlString.append("&destination=");// to
		urlString.append(Double.toString(destlat));
		urlString.append(",");
		urlString.append(Double.toString(destlog));
		urlString.append("&sensor=false&mode=driving&alternatives=true");
		return urlString.toString();
	}

	/**
	 * This method used to draw path to start to end.
	 * 
	 * @param result
	 *            represented {@link String}
	 */
	public void drawPath(String result) {

		try {
			// Tranform the string into a json object
			final JSONObject json = new JSONObject(result);
			JSONArray routeArray = json.getJSONArray("routes");
			JSONObject routes = routeArray.getJSONObject(0);
			JSONObject overviewPolylines = routes
					.getJSONObject("overview_polyline");
			String encodedString = overviewPolylines.getString("points");
			List<LatLng> list = decodePoly(encodedString);

			for (int z = 0; z < list.size() - 1; z++) {
				LatLng src = list.get(z);
				LatLng dest = list.get(z + 1);
				googleMap.addPolyline(new PolylineOptions()
						.add(new LatLng(src.latitude, src.longitude),
								new LatLng(dest.latitude, dest.longitude))
						.width(5).color(Color.BLUE).geodesic(true));

				if (IN_DESTINATION_ROUND_SHOW) {
					PolylineOptions options = new PolylineOptions();
					int radius = 1;
					int numPoints = 50;
					double phase = 2 * Math.PI / numPoints;
					for (int i = 0; i <= numPoints; i++) {
						if (z == 0) {
							options.add(new LatLng(src.latitude + radius
									* Math.sin(i * phase), src.longitude
									+ radius * Math.cos(i * phase)));
							googleMap.addPolyline(options.color(Color.GREEN)
									.width(3));
						} else if (z == list.size() - 2) {
							options.add(new LatLng(dest.latitude + radius
									* Math.sin(i * phase), dest.longitude
									+ radius * Math.cos(i * phase)));
							googleMap.addPolyline(options.color(Color.RED)
									.width(3));
						} else {
							break;
						}
					}
				}
			}

			googleMap.moveCamera(CameraUpdateFactory.newLatLng(list.get(0)));

		} catch (JSONException e) {

		}
	}

	/**
	 * This method used to decode poly line drawn between start to end.
	 * 
	 * @param encoded
	 *            represented {@link String}
	 * @return represented {@link List<T>}
	 */
	private List<LatLng> decodePoly(String encoded) {

		List<LatLng> poly = new ArrayList<LatLng>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;

		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;

			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			LatLng p = new LatLng((((double) lat / 1E5)),
					(((double) lng / 1E5)));
			poly.add(p);
		}

		return poly;
	}

	/**
	 * Inner class
	 * 
	 * @author tasol
	 */

	private class connectAsyncTask extends AsyncTask<Void, Void, String> {
		private ProgressDialog progressDialog;
		String url;

		connectAsyncTask(String urlPass) {
			url = urlPass;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(IjoomerMapPloyline.this);
			progressDialog.setMessage("Fetching route, Please wait...");
			progressDialog.setIndeterminate(true);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(Void... params) {
			JSONParser jParser = new JSONParser();
			String json = jParser.getJSONFromUrl(url);
			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressDialog.hide();
			if (result != null) {
				drawPath(result);
			}
		}
	}

	public class JSONParser {

		InputStream is = null;
		JSONObject jObj = null;
		String json = "";

		// constructor
		public JSONParser() {
		}

		public String getJSONFromUrl(String url) {

			// Making HTTP request
			try {
				// defaultHttpClient
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}

				json = sb.toString();
				is.close();
			} catch (Exception e) {
				Log.e("Buffer Error", "Error converting result " + e.toString());
			}
			return json;

		}
	}

}
