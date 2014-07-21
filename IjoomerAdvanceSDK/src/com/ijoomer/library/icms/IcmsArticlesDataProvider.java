package com.ijoomer.library.icms;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerPagingProvider;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.weservice.IjoomerWebService;
import com.ijoomer.weservice.ProgressListener;
import com.ijoomer.weservice.WebCallListener;
import com.ijoomer.weservice.WebCallListenerWithCacheInfo;


/**
 * This Class Contains All Method Related To IcmsArticlesDataProvider.
 * 
 * @author tasol
 * 
 */
public class IcmsArticlesDataProvider extends IjoomerPagingProvider {
	private Context mContext;
	private final String ARTICLES = "articles";
	private final String ARCHIVE = "archive";
	private final String FEATURED = "featured";
	private final String PAGENO = "pageNO";
	private final String PAGELIMIT = "pageLimit";
	private final String CODE = "code";
	private final String TOTAL = "total";
	private final String FAVOURITE = "favourite";
	private boolean isCalling = false;

	/**
	 * This method used to check provider execute any request call.
	 * 
	 * @return {@link boolean}
	 */
	public boolean isCalling() {
		return isCalling;
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 *            represented {@link Context}
	 */
	public IcmsArticlesDataProvider(Context context) {
		super(context);
		mContext = context;
	}

	/**
	 * This method is used to get Archived Articles
	 * 
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void getArchivedArticles(final WebCallListenerWithCacheInfo target) {
		if (hasNextPage()) {
			isCalling = true;

			new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

				@Override
				protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
					IjoomerWebService iw = new IjoomerWebService();
					iw.reset();

					iw.addWSParam(EXTNAME, ICMS);
					iw.addWSParam(EXTVIEW, ARTICLES);
					iw.addWSParam(EXTTASK, ARCHIVE);

					JSONObject taskData = new JSONObject();
					try {
						taskData.put(PAGENO, getPageNo());
					} catch (Throwable e) {
					}
					iw.addWSParam(TASKDATA, taskData);

					if (IjoomerApplicationConfiguration.isCachEnable) {
						try {
							final ArrayList<HashMap<String, String>> data1 = new IjoomerCaching(mContext).getDataFromCache(ARCHIVE, "select * from '" + ARCHIVE
									+ "' where reqObject='" + iw.getWSParameter() + "' order by rowid");
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

					iw.WSCall(new ProgressListener() {

						@Override
						public void transferred(long num) {
							if (num >= 100) {
								target.onProgressUpdate(95);
							} else {
								target.onProgressUpdate((int) num);
							}
						}
					});

					if (validateResponse(iw.getJsonObject())) {
						try {
							setPagingParams(Integer.parseInt(iw.getJsonObject().getString(PAGELIMIT)), Integer.parseInt(iw.getJsonObject().getString(TOTAL)));
							iw.getJsonObject().remove(CODE);
							iw.getJsonObject().remove(TOTAL);

							if (IjoomerApplicationConfiguration.isCachEnable) {
								IjoomerCaching caching = new IjoomerCaching(mContext);
								caching.setReqObject(iw.getWSParameter().toString());
								caching.addExtraColumn(PAGELIMIT, iw.getJsonObject().get(PAGELIMIT).toString());
								iw.getJsonObject().remove(PAGELIMIT);
								caching.cacheData(iw.getJsonObject(), false, ARCHIVE);
								return new IjoomerCaching(mContext).getDataFromCache(ARCHIVE, "select * from '" + ARCHIVE + "' where reqObject='" + iw.getWSParameter()
										+ "'order by rowid");
							} else {
								new IjoomerCaching(mContext).parseData(iw.getJsonObject());
							}
						} catch (Throwable e) {
						}
					}
					return null;
				}

				@Override
				protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
					super.onPostExecute(result);
					target.onProgressUpdate(100);
					target.onCallComplete(getResponseCode(), getErrorMessage(), result, null, getPageNo() - 1, getPageLimit(), false);
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
	 * This method is used to get Featured Articles
	 * 
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void getFeaturedArticles(final WebCallListenerWithCacheInfo target) {
		if (hasNextPage()) {
			isCalling = true;

			new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

				@Override
				protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
					IjoomerWebService iw = new IjoomerWebService();
					iw.reset();

					iw.addWSParam(EXTNAME, ICMS);
					iw.addWSParam(EXTVIEW, ARTICLES);
					iw.addWSParam(EXTTASK, FEATURED);

					JSONObject taskData = new JSONObject();
					try {
						taskData.put(PAGENO, getPageNo());
					} catch (Throwable e) {
					}
					iw.addWSParam(TASKDATA, taskData);

					if (IjoomerApplicationConfiguration.isCachEnable) {
						try {
							final ArrayList<HashMap<String, String>> data1 = new IjoomerCaching(mContext).getDataFromCache(FEATURED, "select * from '" + FEATURED
									+ "' where reqObject='" + iw.getWSParameter() + "' order by rowid");
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

					iw.WSCall(new ProgressListener() {

						@Override
						public void transferred(long num) {
							if (num >= 100) {
								target.onProgressUpdate(95);
							} else {
								target.onProgressUpdate((int) num);
							}
						}
					});

					if (validateResponse(iw.getJsonObject())) {
						try {
							setPagingParams(Integer.parseInt(iw.getJsonObject().getString(PAGELIMIT)), Integer.parseInt(iw.getJsonObject().getString(TOTAL)));
							iw.getJsonObject().remove(CODE);
							iw.getJsonObject().remove(TOTAL);

							if (IjoomerApplicationConfiguration.isCachEnable) {
								IjoomerCaching caching = new IjoomerCaching(mContext);
								caching.setReqObject(iw.getWSParameter().toString());
								caching.addExtraColumn(PAGELIMIT, iw.getJsonObject().get(PAGELIMIT).toString());
								iw.getJsonObject().remove(PAGELIMIT);
								caching.cacheData(iw.getJsonObject(), false, FEATURED);
								return new IjoomerCaching(mContext).getDataFromCache(FEATURED, "select * from '" + FEATURED + "' where reqObject='" + iw.getWSParameter()
										+ "' order by rowid");
							} else {
								new IjoomerCaching(mContext).parseData(iw.getJsonObject());
							}
						} catch (Throwable e) {
						}
					}
					return null;
				}

				@Override
				protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
					super.onPostExecute(result);
					target.onProgressUpdate(100);
					target.onCallComplete(getResponseCode(), getErrorMessage(), result, null, getPageNo() - 1, getPageLimit(), false);
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
	 * This method is used to fetch Favorite Articles from CacheTable
	 * 
	 * @return {@link ArrayList<HashMap<String, String>>}
	 */
	public ArrayList<HashMap<String, String>> getFavouriteArticles() {
		ArrayList<HashMap<String, String>> data1 = new ArrayList<HashMap<String, String>>();
		try {
			data1 = new IjoomerCaching(mContext).getDataFromCache(FAVOURITE, "select * from '" + FAVOURITE + "'");
			if (data1 != null && data1.size() > 0) {
				return data1;

			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;

	}

}
