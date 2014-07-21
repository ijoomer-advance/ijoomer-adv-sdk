package com.ijoomer.library.plugins;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerPagingProvider;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.weservice.WebCallListenerWithCacheInfo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To PluginsYoutubeDataProvider.
 * 
 * @author tasol
 * 
 */
public class PluginsYoutubeDataProvider extends IjoomerPagingProvider {

	private Context mContext;
	private ArrayList<HashMap<String, String>> data1;

	private String YOUTUBEVIDEO = "youtubeVideo";
	private String YOUTUBEPLAYLIST = "youtubePlayList";
	private String reqObject;
	private boolean isCalling = false;
	private int startIndex;

	public boolean isCalling() {
		return isCalling;
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 *            represented {@link Context}
	 */
	public PluginsYoutubeDataProvider(Context context) {
		super(context);
		mContext = context;
		startIndex = 1;
	}

	/**
	 * This method used to get youtube videos.
	 * 
	 * @param PlayListID
	 *            represented youtube play list id
	 * @param target
	 *            represented {@link WebCallListenerWithCacheInfo}
	 */
	public void getYoutubeVideos(final String PlayListID, final WebCallListenerWithCacheInfo target) {
		if (hasNextPage()) {
			isCalling = true;
			new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

				@Override
				protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
					if (getPageNo() > 1)
						startIndex = ((getPageNo() - 1) * getPageLimit()) + 1;

					String url = "http://gdata.youtube.com/feeds/api/playlists/" + PlayListID + "?v=2&alt=jsonc&start-index=" + startIndex;

					if (IjoomerApplicationConfiguration.isCachEnable) {
						data1 = new ArrayList<HashMap<String, String>>();
						try {

							data1 = new IjoomerCaching(mContext).getDataFromCache(YOUTUBEVIDEO, "select * from " + YOUTUBEVIDEO + " where reqObject='" + url + "' order by rowid");

							if (data1 != null && data1.size() > 0) {
								((Activity) mContext).runOnUiThread(new Runnable() {

									@Override
									public void run() {
										target.onProgressUpdate(100);
										target.onCallComplete(200, "", data1, null, getPageNo(), Integer.parseInt(data1.get(0).get(PAGELIMIT)), true);
									}
								});

							}
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}

					JSONObject json = getJSONFromUrl(url);
					try {

						setPagingParams(Integer.parseInt(json.getJSONObject("data").getString(ITEMSPERPAGE)), Integer.parseInt(json.getJSONObject("data").getString(TOTALITEMS)));
						if (IjoomerApplicationConfiguration.isCachEnable) {

							IjoomerCaching caching = new IjoomerCaching(mContext);
							caching.setReqObject(url);
							if (json.getJSONObject("data").getJSONArray("items").length() > 0) {
								caching.addExtraColumn(PAGELIMIT, json.getJSONObject("data").getString(ITEMSPERPAGE));
								caching.addExtraColumn("playlistid", PlayListID);
								caching.cacheData(json.getJSONObject("data").getJSONArray("items"), false, YOUTUBEVIDEO);
							}
							return new IjoomerCaching(mContext).getDataFromCache(YOUTUBEVIDEO, "select * from " + YOUTUBEVIDEO + " where reqObject='" + url + "' order by rowid");
						} else {
							return new IjoomerCaching(mContext).parseData(json.getJSONObject("data").getJSONArray("items"));
						}
					} catch (Throwable e) {
						e.printStackTrace();
					}
					return null;
				}

				@Override
				protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
					super.onPostExecute(result);
					if (result != null)
						target.onCallComplete(getResponseCode(), getErrorMessage(), result, null, getPageNo(), Integer.parseInt(result.get(0).get(PAGELIMIT)), false);
					else
						target.onCallComplete(599, getErrorMessage(), result, null, getPageNo(), 0, false);
					target.onProgressUpdate(100);
					isCalling = false;

				}
			}.execute();
		} else {
			isCalling = false;

			target.onCallComplete(getResponseCode(), getErrorMessage(), null, null, 0, 0, false);
			target.onProgressUpdate(100);
		}
	}

	/**
	 * This method used to get youtube playlist.
	 * 
	 * @param userName
	 *            represented user name
	 * @param target
	 *            represented {@link WebCallListenerWithCacheInfo}
	 */
	public void getYoutubePlayLists(final String userName, final WebCallListenerWithCacheInfo target) {

		try {
			if (hasNextPage()) {
				isCalling = true;
				new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

					@Override
					protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
						if (getPageNo() > 1)
							startIndex = ((getPageNo() - 1) * getPageLimit()) + 1;
						reqObject = "http://gdata.youtube.com/feeds/api/users/" + userName + "/playlists?v=2&alt=jsonc&start-index=" + startIndex;

						if (IjoomerApplicationConfiguration.isCachEnable) {
							data1 = new ArrayList<HashMap<String, String>>();
							try {
								data1 = new IjoomerCaching(mContext).getDataFromCache(YOUTUBEPLAYLIST, "select * from " + YOUTUBEPLAYLIST + " where reqObject='" + reqObject
										+ "' order by rowid");
								if (data1 != null && data1.size() > 0) {

									((Activity) mContext).runOnUiThread(new Runnable() {

										@Override
										public void run() {
											target.onProgressUpdate(100);
											target.onCallComplete(200, "", data1, null, getPageNo(), Integer.parseInt(data1.get(0).get(PAGELIMIT)), true);

										}
									});

								}
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}

						JSONObject json = getJSONFromUrl(reqObject);
						try {

							setPagingParams(Integer.parseInt(json.getJSONObject("data").getString(ITEMSPERPAGE)),
									Integer.parseInt(json.getJSONObject("data").getString(TOTALITEMS)));

							if (IjoomerApplicationConfiguration.isCachEnable) {
								IjoomerCaching caching = new IjoomerCaching(mContext);
								caching.setReqObject(reqObject);
								if (json.getJSONObject("data").getJSONArray("items").length() > 0) {
									caching.addExtraColumn(PAGELIMIT, json.getJSONObject("data").getString("itemsPerPage"));
									caching.cacheData(json.getJSONObject("data").getJSONArray("items"), false, YOUTUBEPLAYLIST);
								}

								return new IjoomerCaching(mContext).getDataFromCache(YOUTUBEPLAYLIST, "select * from " + YOUTUBEPLAYLIST + "  where reqObject='" + reqObject
										+ "' order by rowid");

							} else {
								return new IjoomerCaching(mContext).parseData(json.getJSONObject("data").getJSONArray("items"));
							}
						} catch (Throwable e) {
							e.printStackTrace();
						}
						return null;
					}

					@Override
					protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
						super.onPostExecute(result);
						try {
							if (result != null)
								target.onCallComplete(getResponseCode(), getErrorMessage(), result, null, getPageNo(), Integer.parseInt(result.get(0).get(PAGELIMIT)), false);
							else
								target.onCallComplete(599, getErrorMessage(), result, null, getPageNo(), 0, false);
						} catch (Exception e) {
							e.printStackTrace();
						}

						target.onProgressUpdate(100);
						isCalling = false;
					}
				}.execute();
			} else {
				isCalling = false;

				target.onCallComplete(getResponseCode(), getErrorMessage(), null, null, 0, 0, false);
				target.onProgressUpdate(100);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method used to get json from url.
	 * 
	 * @param url
	 *            represented url
	 * @return represented {@link JSONObject}
	 */
	public JSONObject getJSONFromUrl(String url) {

		InputStream is = null;
		JSONObject jObj = null;
		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();

			HttpGet httpGet = new HttpGet(url);

			HttpResponse httpResponse = httpClient.execute(httpGet);
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

}
