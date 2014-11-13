package com.ijoomer.plugins;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Address;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.IjoomerListView;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.plugins.PluginsWeatherDataProvider;
import com.ijoomer.src.R;
import com.smart.framework.CustomAlertMagnatic;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

/**
 * This Fragment Contains All Method Related To IcmsArchivedArticlesFragment.
 * 
 * @author tasol
 * 
 */
public class PluginsWeatherLocationActivity extends PluginsMasterActivity implements PluginsTagHolder {
	private IjoomerListView listLocation;
	private AutoCompleteTextView edtAddLocation;
	private PluginsWeatherDataProvider weatherDataProvider;
	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private ArrayList<String> LOCATION_ID_ARRAY;
	private SmartListAdapterWithHolder listAdapterWithHolder;
	private final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private final String OUT_JSON = "/json";
	private final String MAP_API_KEY = "AIzaSyAfhmNNTzNy4CpE4bNBMTawVl4ENUzgppc";

	@Override
	public int setLayoutId() {
		return R.layout.plugins_weather_locations;
	}

	@Override
	public void initComponents() {
		weatherDataProvider = new PluginsWeatherDataProvider(PluginsWeatherLocationActivity.this);
		listLocation = (IjoomerListView) findViewById(R.id.lstLocations);
		edtAddLocation = (AutoCompleteTextView) findViewById(R.id.edtAddLocation);
		LOCATION_ID_ARRAY = new ArrayList<String>();

	}

	@Override
	public void prepareViews() {

		edtAddLocation.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.ijoomer_map_address_auto_complete_list_item));
	}

	@Override
	protected void onResume() {
		prepareList(weatherDataProvider.getLocations());
		listAdapterWithHolder = getListAdapter(listData);
		if (listData.size() > 0) {
			listLocation.setAdapter(listAdapterWithHolder);
		}
		super.onResume();
	}

	@Override
	public void setActionListeners() {

		edtAddLocation.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {

				hideSoftKeyboard();
				String str = (String) adapterView.getItemAtPosition(position);
				String[] name = str.split(",");
				edtAddLocation.setText(str);
				Address address = IjoomerUtilities.getLatLongFromAddress(str);
				if (address != null) {
					String location = address.getLatitude() + "," + address.getLongitude();
					InsertLocation(name[0], location);
				} else {
					InsertLocation(name[0], name[0]);
				}

			}
		});
	}

	public void prepareList(ArrayList<HashMap<String, String>> data) {

		if (data != null) {

			listData.clear();
			LOCATION_ID_ARRAY.clear();
			for (int i = 0; i < data.size(); i++) {
				LOCATION_ID_ARRAY.add(data.get(i).get(ID));
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.plugins_weather_location_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(data.get(i));
				item.setValues(obj);
				listData.add(item);
			}
		}
	}

	public void InsertLocation(String name, String location) {
		try {

			HashMap<String, String> locationHash = new HashMap<String, String>();
			int id = getSmartApplication().readSharedPreferences().getInt(SP_LOCATION_ID, 0);
			locationHash.put(ID, id + "");
			locationHash.put(NAME, name);
			locationHash.put(LOCATION, location);
			if (weatherDataProvider.InsertRow(locationHash)) {
				LOCATION_ID_ARRAY.clear();
				LOCATION_ID_ARRAY.add(id + "");
				edtAddLocation.setText("");
				getSmartApplication().writeSharedPreferences(SP_LOCATION_ID, ++id);
				try {
					loadNew(PluginsWeatherActivity.class, PluginsWeatherLocationActivity.this, false, "IN_LOCATION_INDEX", "0", "IN_LOCATION_ID_ARRAY", LOCATION_ID_ARRAY);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public SmartListAdapterWithHolder getListAdapter(ArrayList<SmartListItem> listData) {

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(PluginsWeatherLocationActivity.this, R.layout.plugins_weather_location_list_item, listData,
				new ItemView() {

					@Override
					public View setItemView(final int position, View v, SmartListItem item, ViewHolder holder) {

						holder.txtLocation = (IjoomerTextView) v.findViewById(R.id.txtLocation);
						holder.imgRemoveLocation = (ImageView) v.findViewById(R.id.imgRemoveLocation);
						@SuppressWarnings("unchecked")
						final HashMap<String, String> value = (HashMap<String, String>) item.getValues().get(0);
						holder.imgRemoveLocation.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								IjoomerUtilities.getCustomConfirmDialog(getString(R.string.location_remove), getString(R.string.are_you_sure), getString(R.string.yes),
										getString(R.string.no), new CustomAlertMagnatic() {

											@Override
											public void PositiveMethod() {
												if (weatherDataProvider.deleteRow(value.get(ID))) {
													listAdapterWithHolder.remove(listAdapterWithHolder.getItem(position));
													LOCATION_ID_ARRAY.remove(position);
													listLocation.invalidate();
												}

											}

											@Override
											public void NegativeMethod() {

											}
										});

							}
						});

						holder.txtLocation.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								try {
									loadNew(PluginsWeatherActivity.class, PluginsWeatherLocationActivity.this, false, "IN_LOCATION_INDEX", position + "", "IN_LOCATION_ID_ARRAY",
											LOCATION_ID_ARRAY);
								} catch (Exception e) {

									e.printStackTrace();
								}
							}
						});

						holder.txtLocation.setText(value.get(NAME).trim());
						return v;
					}

					@Override
					public View setItemView(int position, View v, SmartListItem item) {
						return null;
					}
				});
		return adapterWithHolder;
	}

	private ArrayList<String> autocomplete(final String input) {
		ArrayList<String> resultList = null;
		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
			sb.append("?sensor=false&key=" + MAP_API_KEY);
			// sb.append("&components=country:in");
			sb.append("&input=" + URLEncoder.encode(input, "utf8"));
			URL url = new URL(sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}
		} catch (Exception e) {
			return resultList;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		try {
			// Create a JSON object hierarchy from the results
			JSONObject jsonObj = new JSONObject(jsonResults.toString());
			JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

			// Extract the Place descriptions from the results
			resultList = new ArrayList<String>(predsJsonArray.length());
			for (int i = 0; i < predsJsonArray.length(); i++) {
				resultList.add(predsJsonArray.getJSONObject(i).getString("description"));

			}
		} catch (JSONException e) {
		}

		return resultList;

	}

	private class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
		private ArrayList<String> resultList;

		public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}

		@Override
		public int getCount() {
			return resultList.size();
		}

		@Override
		public String getItem(int index) {
			return resultList.get(index);
		}

		@Override
		public Filter getFilter() {
			Filter filter = new Filter() {
				@Override
				protected FilterResults performFiltering(final CharSequence constraint) {
					final FilterResults filterResults = new FilterResults();
					if (constraint != null) {
						// Retrieve the autocomplete results.

						resultList = autocomplete(constraint.toString());
						filterResults.values = resultList;
						filterResults.count = resultList.size();
					}
					return filterResults;

				}

				@Override
				protected void publishResults(CharSequence constraint, FilterResults results) {
					if (results != null && results.count > 0) {
						notifyDataSetChanged();
					} else {
						notifyDataSetInvalidated();
					}
				}
			};
			return filter;
		}
	}

}