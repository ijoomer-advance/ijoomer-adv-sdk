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
 * This Class Contains All Method Related To IcmsCategoryDataProvider.
 * 
 * @author tasol
 * 
 */
public class IcmsCategoryDataProvider extends IjoomerPagingProvider {
	private Context mContext;
	private final String PAGENO = "pageNO";
	private final String PAGELIMIT = "pageLimit";
	private final String CATEGORIES = "categories";
	private final String CODE = "code";
	private final String CATEGORY = "category";
	private final String ARTICLES = "articles";
	private final String ID = "id";
	private int pageLimit = 0;
	private boolean isCalling = false;
	private ArrayList<HashMap<String, String>> data2;
	private ArrayList<HashMap<String, String>> data1;

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
	public IcmsCategoryDataProvider(Context context) {
		super(context);
		mContext = context;
	}

	/***
	 * This method is used to get Categories
	 * 
	 * @param id
	 *            represented category id.(0- for all categories,id - for
	 *            category)
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void getCategories(final String id, final WebCallListenerWithCacheInfo target) {

		if (hasNextPage()) {
			isCalling = true;
			new AsyncTask<Void, Void, ArrayList<Object>>() {

				@Override
				protected ArrayList<Object> doInBackground(Void... params) {

					IjoomerWebService iw = new IjoomerWebService();
					iw.reset();

					iw.addWSParam(EXTNAME, ICMS);
					iw.addWSParam(EXTVIEW, CATEGORIES);
					iw.addWSParam(EXTTASK, CATEGORY);

					JSONObject taskData = new JSONObject();
					try {
						taskData.put(ID, id);
						taskData.put(PAGENO, "" + getPageNo());
					} catch (Throwable e) {
					}
					iw.addWSParam(TASKDATA, taskData);

					if (IjoomerApplicationConfiguration.isCachEnable) {
						data2 = new ArrayList<HashMap<String, String>>();
						data1 = new ArrayList<HashMap<String, String>>();
						try {

							data1 = new IjoomerCaching(mContext).getDataFromCache(ARTICLES, "select * from '" + ARTICLES + "' where reqObject='" + iw.getWSParameter()
									+ "'order by rowid");
							data2 = new IjoomerCaching(mContext).getDataFromCache(CATEGORIES, "select * from '" + CATEGORIES + "' where reqObject='" + iw.getWSParameter()
									+ "'order by rowid");
							if ((data1 != null && data1.size() > 0) || (data2 != null && data2.size() > 0)) {
								try {

									if (data1.get(0).get(PAGELIMIT) != null && data1.get(0).get(PAGELIMIT).length() > 0)
										pageLimit = Integer.parseInt(data1.get(0).get(PAGELIMIT));
									else
										pageLimit = Integer.parseInt(data1.get(0).get(PAGELIMIT));
								} catch (Exception e) {
									e.printStackTrace();
									pageLimit = 0;
								}
								((Activity) mContext).runOnUiThread(new Runnable() {

									@Override
									public void run() {
										target.onCallComplete(200, "", data1, data2, getPageNo(), pageLimit, true);
										target.onProgressUpdate(100);
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

					ArrayList<Object> data = new ArrayList<Object>();
					if (validateResponse(iw.getJsonObject())) {
						try {
							setPagingParams(Integer.parseInt(iw.getJsonObject().getString(PAGELIMIT)), Integer.parseInt(iw.getJsonObject().getString(TOTAL)));
							iw.getJsonObject().remove(CODE);
							iw.getJsonObject().remove(TOTAL);
							try {
								if (IjoomerApplicationConfiguration.isCachEnable) {

									IjoomerCaching caching = new IjoomerCaching(mContext);
									caching.setReqObject(iw.getWSParameter().toString());
									if (iw.getJsonObject().getJSONArray(ARTICLES).length() > 0) {
										caching.addExtraColumn(PAGELIMIT, iw.getJsonObject().getString(PAGELIMIT));
										caching.cacheData(iw.getJsonObject().getJSONArray(ARTICLES), false, ARTICLES);
									}

									data.add(caching.getDataFromCache(ARTICLES, "select * from '" + ARTICLES + "' where reqObject='" + iw.getWSParameter() + "'order by rowid"));
								}

								else
									data.add(new IjoomerCaching(mContext).parseData(iw.getJsonObject().getJSONArray(ARTICLES)));
							} catch (Throwable e) {
								data.add(new ArrayList<HashMap<String, String>>());
							}
							try {
								if (IjoomerApplicationConfiguration.isCachEnable) {
									IjoomerCaching caching = new IjoomerCaching(mContext);
									caching.setReqObject(iw.getWSParameter().toString());
									caching.cacheData(iw.getJsonObject().getJSONArray(CATEGORIES), false, CATEGORIES);
									data.add(caching.getDataFromCache(CATEGORIES, "select * from '" + CATEGORIES + "' where reqObject='" + iw.getWSParameter() + "'order by rowid"));

								} else
									data.add(new IjoomerCaching(mContext).parseData(iw.getJsonObject().getJSONArray(CATEGORIES)));
							} catch (Throwable e) {
								data.add(new ArrayList<HashMap<String, String>>());
							}
						} catch (Throwable e) {
							e.printStackTrace();
						}

					}
					return data;
				}

				@SuppressWarnings("unchecked")
				@Override
				protected void onPostExecute(ArrayList<Object> result) {
					super.onPostExecute(result);

					ArrayList<HashMap<String, String>> data1 = null;
					ArrayList<HashMap<String, String>> data2 = null;
					try {
						data1 = (ArrayList<HashMap<String, String>>) result.get(0);
						data2 = (ArrayList<HashMap<String, String>>) result.get(1);

					} catch (Throwable e) {
						e.printStackTrace();
					}
					try {
						if (data1.get(0).get(PAGELIMIT) != null && data1.get(0).get(PAGELIMIT).length() > 0)
							pageLimit = Integer.parseInt(data1.get(0).get(PAGELIMIT));
						else
							pageLimit = Integer.parseInt(data1.get(0).get(PAGELIMIT));
					} catch (Exception e) {
						e.printStackTrace();
						pageLimit = 0;
					}
					target.onCallComplete(getResponseCode(), getErrorMessage(), data1, data2, getPageNo(), pageLimit, false);
					isCalling = false;
					target.onProgressUpdate(100);
				}
			}.execute();
		} else {
			isCalling = false;

			target.onCallComplete(getResponseCode(), getErrorMessage(), null, null, 0, 0, false);
			target.onProgressUpdate(100);
		}

	}
}
