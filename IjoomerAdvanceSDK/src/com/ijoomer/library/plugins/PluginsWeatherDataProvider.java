package com.ijoomer.library.plugins;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.caching.IjoomerDataHelper;
import com.ijoomer.common.classes.IjoomerResponseValidator;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.weservice.WebCallListener;

/**
 * This Class Contains All Method Related To PluginsYoutubeDataProvider.
 * 
 * @author tasol
 * 
 */
public class PluginsWeatherDataProvider extends IjoomerResponseValidator {
	private Context mContext;
	private String numberOfDays = "6";
	private String reqObject;
	private String apiKey = "r4qx489vz3kys8a93h8r4uzj";
	private ArrayList<ArrayList<HashMap<String, String>>> weatherData;
	private String CURRENT_CONDITION = "current_condition";
	private String WEATHER = "weather";
	private SQLiteDatabase sd;
	private String LOCATION = "Location";
	private String TODAYWEATHER = "todayWeather";
	private final String DAILYWEATHER = "dailyWeather";
	private ArrayList<HashMap<String, String>> data2;
	private ArrayList<HashMap<String, String>> data1;

	/**
	 * Constructor
	 * 
	 * @param context
	 *            represented {@link Context}
	 */
	public PluginsWeatherDataProvider(Context context) {
		super(context);
		mContext = context;
	}

	/**
	 * This method is used to get current_weather detail and weather_forcasting
	 * for specified number of days.
	 * 
	 * @param Location
	 *            represented (city_name) or (latitude,longitude)
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void getWeatherInfo(String Location, final WebCallListener target) {
		weatherData = new ArrayList<ArrayList<HashMap<String, String>>>();
		reqObject = "http://api.worldweatheronline.com/free/v1/weather.ashx?q=" + Location + "&format=json&num_of_days=" + numberOfDays + "&key=" + apiKey + "";
		new AsyncTask<Void, Void, ArrayList<ArrayList<HashMap<String, String>>>>() {

			protected void onPreExecute() {
				if (IjoomerApplicationConfiguration.isCachEnable) {
					data1 = new ArrayList<HashMap<String, String>>();
					data2 = new ArrayList<HashMap<String, String>>();
					try {
						data1 = new IjoomerCaching(mContext).getDataFromCache(TODAYWEATHER, "select * from '" + TODAYWEATHER + "' where reqObject='" + reqObject
								+ "'order by rowid");
						data2 = new IjoomerCaching(mContext).getDataFromCache(DAILYWEATHER, "select * from '" + DAILYWEATHER + "' where reqObject='" + reqObject
								+ "'order by rowid");
						if ((data1 != null && data1.size() > 0) || (data2 != null && data2.size() > 0)) {

							target.onCallComplete(200, "", data1, data2);
							target.onProgressUpdate(100);
						}
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			};

			@Override
			protected ArrayList<ArrayList<HashMap<String, String>>> doInBackground(Void... params) {

				JSONObject jsonObject = getJSONFromUrl(reqObject, target);
				try {
					if (IjoomerApplicationConfiguration.isCachEnable) {

						IjoomerCaching caching = new IjoomerCaching(mContext);
						caching.setReqObject(reqObject);
						if (jsonObject.getJSONObject("data").getJSONArray(CURRENT_CONDITION).length() > 0) {
							caching.cacheData(jsonObject.getJSONObject("data").getJSONArray(CURRENT_CONDITION), false, TODAYWEATHER);
						}
						caching = new IjoomerCaching(mContext);
						caching.setReqObject(reqObject);
						if (jsonObject.getJSONObject("data").getJSONArray(WEATHER).length() > 0) {
							caching.cacheData(jsonObject.getJSONObject("data").getJSONArray(WEATHER), false, DAILYWEATHER);
						}
						weatherData.add(caching.getDataFromCache(TODAYWEATHER, "select * from '" + TODAYWEATHER + "' where reqObject='" + reqObject + "'order by rowid"));
						weatherData.add(caching.getDataFromCache(DAILYWEATHER, "select * from '" + DAILYWEATHER + "' where reqObject='" + reqObject + "'order by rowid"));
					}
					return weatherData;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<ArrayList<HashMap<String, String>>> result) {
				if (result != null && result.size() > 0) {
					target.onCallComplete(200, getErrorMessage(), result.get(0), result.get(1));
				} else {
					target.onCallComplete(204, getErrorMessage(), null, null);
				}
				target.onProgressUpdate(100);
				super.onPostExecute(result);
			}
		}.execute();

	}

	/**
	 * This method used to get json from url.
	 * 
	 * @param url
	 *            represented url
	 * @return represented {@link JSONObject}
	 */
	public JSONObject getJSONFromUrl(String url, WebCallListener target) {

		InputStream is = null;
		JSONObject jObj = null;
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			target.onProgressUpdate(95);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			jObj = new JSONObject(getResponseBody(is));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return jObj;
	}

	/**
	 * This method used to get response body.
	 * 
	 * @param instream
	 *            represented input stream
	 * @return represented {@link String}
	 * @throws IOException
	 *             represented {@link IOException}
	 * @throws ParseException
	 *             represented {@link ParseException}
	 */
	public String getResponseBody(final InputStream instream) throws IOException, ParseException {

		if (instream == null) {
			return "";
		}

		StringBuilder buffer = new StringBuilder();

		BufferedReader reader = new BufferedReader(new InputStreamReader(instream, "utf-8"));

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}

		} finally {
			instream.close();
			reader.close();
		}
		System.out.println(buffer.toString());
		return buffer.toString();

	}

	public boolean InsertRow(HashMap<String, String> row) {
		try {
			sd = IjoomerDataHelper.getInstance(mContext).getDB();
			ArrayList<String> columns;
			String query = "CREATE TABLE IF NOT EXISTS " + LOCATION + " (";
			columns = new ArrayList<String>(Arrays.asList(mContext.getResources().getStringArray(
					mContext.getResources().getIdentifier(LOCATION, "array", mContext.getPackageName()))));
			if (columns.size() > 0) {
				for (String columnName : columns) {
					query = query + columnName + " TEXT,";
				}
				query = query.substring(0, query.length() - 1) + ");";
				System.out.println("query : " + query);
				sd.execSQL(query);
			}
			IjoomerDataHelper.getInstance(mContext).addTable(LOCATION);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ContentValues con = new ContentValues();
			sd.beginTransaction();
			con.clear();
			Iterator<String> it = row.keySet().iterator();
			while (it.hasNext()) {
				String columnName = it.next();
				con.put(columnName, row.get(columnName));
			}
			sd.insertWithOnConflict(LOCATION, null, con, SQLiteDatabase.CONFLICT_REPLACE);
			sd.setTransactionSuccessful();
			sd.endTransaction();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean deleteRow(String id) {
		try {
			new IjoomerCaching(mContext).deleteDataFromCache("delete from '" + LOCATION + "' where id='" + id + "'");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public ArrayList<HashMap<String, String>> getLocations() {
		ArrayList<HashMap<String, String>> data1 = new ArrayList<HashMap<String, String>>();
		try {
			data1 = new IjoomerCaching(mContext).getDataFromCache(LOCATION, "select * from '" + LOCATION + "'");
			if (data1 != null && data1.size() > 0) {
				return data1;

			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<HashMap<String, String>> getLocation(String id) {
		ArrayList<HashMap<String, String>> data1 = new ArrayList<HashMap<String, String>>();
		try {
			data1 = new IjoomerCaching(mContext).getDataFromCache(LOCATION, "select * from '" + LOCATION + "' where id='" + id + "'");
			if (data1 != null && data1.size() > 0) {
				return data1;

			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

}
