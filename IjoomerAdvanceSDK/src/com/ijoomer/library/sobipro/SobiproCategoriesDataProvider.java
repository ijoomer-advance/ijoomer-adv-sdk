package com.ijoomer.library.sobipro;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.caching.IjoomerCachingInsertListener;
import com.ijoomer.common.classes.IjoomerPagingProvider;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.weservice.IjoomerWebService;
import com.ijoomer.weservice.ProgressListener;
import com.ijoomer.weservice.WebCallListener;
import com.ijoomer.weservice.WebCallListenerWithCacheInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This Class Contains All Method Related To SobiproCategoriesDataProvider.
 *
 * @author tasol
 *
 */
public class SobiproCategoriesDataProvider extends IjoomerPagingProvider {

    private Context mContext;
    private final String NAME = "name";
    private final String VALUE = "value";
    private final String FIELDS = "fields";
    private final String SEARCH_FOR = "search_for";
    private final String GETSEARCH = "getsearch";
    private final String ISOBIPRO = "isobipro";
    private final String CATEGORIES = "categories";
    private final String SECTON_CATEGORIES = "sectionCategories";
    private final String SOBIPROSEARCHRESULT = "SobiproSearchResult";
    private final String SECTION_ID = "sectionID";
    private final String CAT_ID = "categoryID";
    private final String PAGENO = "pageNO";
    private final String SECTIONID = "sectionid";
    private final String CATID = "catid";
    private final String SORTBY = "sortBy";
    private final String SORTORDER = "sortOrder";
    private final String FEATUREDFIRST = "featuredFirst";
    private final String FILTERBY = "filterBy";
    private final String SEARCHPHRASE = "searchphrase";
    private final String TABLE_ENTRIES = "SobiproEntries";
    private final String TABLE_ENTRIES_FILTER = "SobiproEntriesFilter";
    private final String TABLE_RESTAURANT_ENTRIES = "SobiproRestaurantEntries";
    private final String TABLE_FAVOURITE = "SobiproFavouriteEntries";
    private final String TABLE_DEAL = "SobiproRestaurantDeals";
    private final String FAVOURITE = "favourite";
    private final String TABLE_SECTION_CATEGORIES = "sobipro_section_categories";
    private final String ID = "id";
    private boolean isCalling = false;
    private String LATITUDE = "latitude";
    private String LONGITUDE = "longitude";
    private String GETDEALS = "getDeals";

    private double lat;
    private double longi;

    private String COSLAT = "coslat";
    private String SINLAT = "sinlat";
    private String COSLNG = "coslng";
    private String SINLNG = "sinlng";

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

    public SobiproCategoriesDataProvider(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    /**
     * This Method is used to get Categories of specifies Section
     *
     * @param sectionID
     *            represented section id
     * @param catID
     *            represented category id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getSectionCategories(final String sectionID, final String catID, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();

                iw.addWSParam(EXTNAME, SOBIPRO);
                iw.addWSParam(EXTVIEW, ISOBIPRO);
                iw.addWSParam(EXTTASK, SECTON_CATEGORIES);
                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(SECTION_ID, sectionID);
                    taskData.put(CAT_ID, catID);
                } catch (Throwable e) {
                }

                iw.addWSParam(TASKDATA, taskData);

                if (IjoomerApplicationConfiguration.isCachEnable) {
                    try {
                        final ArrayList<HashMap<String, String>> data1 = new IjoomerCaching(mContext).getDataFromCache(TABLE_SECTION_CATEGORIES, "select * from'"
                                + TABLE_SECTION_CATEGORIES + "'where reqObject='" + iw.getWSParameter() + "' order by rowid");
                        if (data1 != null && data1.size() > 0) {
                            ((Activity) mContext).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    target.onProgressUpdate(100);
                                    target.onCallComplete(200, "", data1, null);

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
                        if (IjoomerApplicationConfiguration.isCachEnable) {
                            IjoomerCaching caching = new IjoomerCaching(mContext);
                            caching.setReqObject(iw.getWSParameter().toString());
                            caching.cacheData(iw.getJsonObject().getJSONArray(CATEGORIES), false, TABLE_SECTION_CATEGORIES);

                            return new IjoomerCaching(mContext).getDataFromCache(TABLE_SECTION_CATEGORIES,
                                    "select * from'" + TABLE_SECTION_CATEGORIES + "'where reqObject='" + iw.getWSParameter() + "' order by rowid");
                        } else {
                            return new IjoomerCaching(mContext).parseData(iw.getJsonObject().getJSONArray(CATEGORIES));

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
                super.onPostExecute(result);
                target.onProgressUpdate(100);
                target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
            }

        }.execute();
    }

    /**
     * This method is used to get entries by sorting and filtering parameters
     *
     * @param sectionID
     *            represented section id
     * @param catID
     *            represented category id
     * @param sortBy
     *            represented sorting options(rating,title)
     * @param sortOrder
     *            represented sorting order.(asc - for Ascending order ,desc -
     *            fore descending order)
     * @param filterBy
     *            represented filtering options.
     * @param target
     *            {@link WebCallListener}
     */
    public void getEntries(final String sectionID, final String catID, final String sortBy, final String sortOrder, final String filterBy, final String featuredFirst,
                           final WebCallListener target) {
        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, List<ArrayList<HashMap<String, String>>>>() {
                @Override
                protected List<ArrayList<HashMap<String, String>>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, SOBIPRO);
                    iw.addWSParam(EXTVIEW, ISOBIPRO);
                    iw.addWSParam(EXTTASK, SECTON_CATEGORIES);
                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(SECTION_ID, sectionID);
                        taskData.put(CAT_ID, catID);
                        taskData.put(PAGENO, getPageNo());
                        taskData.put(SORTBY, sortBy);
                        taskData.put(SORTORDER, sortOrder);
                        taskData.put(FILTERBY, filterBy);
                        taskData.put(FEATUREDFIRST, featuredFirst);

                    } catch (Throwable e) {
                    }

                    iw.addWSParam(TASKDATA, taskData);

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
                            iw.getJsonObject().remove(TOTAL);
                            if (iw.getJsonObject().getJSONArray("entries") != null) {
                                IjoomerCaching caching = new IjoomerCaching(mContext);
                                caching.addExtraColumn(PAGENO, (getPageNo() - 1) + "");
                                caching.addExtraColumn(PAGELIMIT, iw.getJsonObject().get(PAGELIMIT).toString());
                                caching.addExtraColumn(SECTIONID, iw.getJsonObject().get(SECTIONID).toString());
                                caching.addExtraColumn(CATID, iw.getJsonObject().get(CATID).toString());
                                caching.addExtraColumn(FAVOURITE, "0");
                                iw.getJsonObject().remove(PAGELIMIT);
                                iw.getJsonObject().remove(SECTIONID);
                                iw.getJsonObject().remove(CATID);
                                caching.setReqObject(iw.getWSParameter().toString());
                                if ((getPageNo() - 1) == 1) {
                                    caching.droapTable(TABLE_ENTRIES_FILTER);
                                }

                                caching.cacheData(iw.getJsonObject(), false, TABLE_ENTRIES_FILTER);

                                return getEntriesFromCacheByReqObject(TABLE_ENTRIES_FILTER, iw.getWSParameter().toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(List<ArrayList<HashMap<String, String>>> result) {
                    super.onPostExecute(result);
                    target.onProgressUpdate(100);
                    target.onCallComplete(getResponseCode(), getErrorMessage(), null, result);
                    isCalling = false;
                }

            }.execute();
        } else {
            isCalling = false;
            target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
            target.onProgressUpdate(100);
        }
    }

    /**
     * This method is used to get entries by sorting and filtering parameters
     *
     * @param id
     *            represented category id
     * @param sortBy
     *            represented sorting options(rating,title)
     * @param sortOrder
     *            represented sorting order.(asc - for Ascending order ,desc -
     *            fore descending order)
     * @param filterBy
     *            represented filtering options.
     * @param target
     *            {@link WebCallListener}
     */
    public void getRestaurantEntries(final String id, final String latitude, final String longitude, final String sortBy, final String sortOrder, final String filterBy,
                                     final String featuredFirst, final WebCallListener target) {
        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, List<ArrayList<HashMap<String, String>>>>() {

                @Override
                protected List<ArrayList<HashMap<String, String>>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, SOBIPRO);
                    iw.addWSParam(EXTVIEW, ISOBIPRO);
                    iw.addWSParam(EXTTASK, SECTON_CATEGORIES);
                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(SECTION_ID, id);
                        taskData.put(PAGENO, getPageNo());
                        taskData.put(SORTBY, sortBy);
                        taskData.put(SORTORDER, sortOrder);
                        taskData.put(FILTERBY, filterBy);
                        taskData.put(LATITUDE, latitude);
                        taskData.put(LONGITUDE, longitude);
                        taskData.put(FEATUREDFIRST, featuredFirst);
                    } catch (Throwable e) {
                    }

                    iw.addWSParam(TASKDATA, taskData);

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
                            iw.getJsonObject().remove(TOTAL);
                            if (iw.getJsonObject().getJSONArray("entries") != null) {
                                IjoomerCaching caching = new IjoomerCaching(mContext);
                                caching.addExtraColumn(PAGENO, (getPageNo() - 1) + "");
                                caching.addExtraColumn(PAGELIMIT, iw.getJsonObject().get(PAGELIMIT).toString());
                                caching.addExtraColumn(SECTIONID, iw.getJsonObject().get(SECTIONID).toString());
                                caching.addExtraColumn(CATID, iw.getJsonObject().get(CATID).toString());
                                caching.addExtraColumn(FAVOURITE, "0");
                                iw.getJsonObject().remove(PAGELIMIT);
                                iw.getJsonObject().remove(SECTIONID);
                                iw.getJsonObject().remove(CATID);
                                ((JSONObject) iw.getWSParameter().get(TASKDATA)).remove(LATITUDE);
                                ((JSONObject) iw.getWSParameter().get(TASKDATA)).remove(LONGITUDE);
                                caching.setReqObject(iw.getWSParameter().toString());
                                if ((getPageNo() - 1) == 1) {
                                    caching.droapTable(TABLE_ENTRIES_FILTER);
                                }

                                caching.cacheData(iw.getJsonObject(), false, TABLE_ENTRIES_FILTER);

                                return getEntriesFromCacheByReqObject(TABLE_ENTRIES_FILTER, iw.getWSParameter().toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(List<ArrayList<HashMap<String, String>>> result) {
                    super.onPostExecute(result);
                    target.onProgressUpdate(100);
                    target.onCallComplete(getResponseCode(), getErrorMessage(), null, result);
                    isCalling = false;
                }

            }.execute();
        } else {
            isCalling = false;
            target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
            target.onProgressUpdate(100);
        }
    }

    /**
     * This method is used to get entries
     *
     * @param sectionID
     *            represented section id
     * @param catID
     *            represented Category id
     * @param target
     *            {@link WebCallListener}
     */
    public void getEntries(final String sectionID, final String catID, final String featuredFirst, final WebCallListenerWithCacheInfo target) {
        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, List<ArrayList<HashMap<String, String>>>>() {

                IjoomerWebService iw = new IjoomerWebService();

                @Override
                protected List<ArrayList<HashMap<String, String>>> doInBackground(Void... params) {
                    iw.reset();

                    iw.addWSParam(EXTNAME, SOBIPRO);
                    iw.addWSParam(EXTVIEW, ISOBIPRO);
                    iw.addWSParam(EXTTASK, SECTON_CATEGORIES);

                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(SECTION_ID, sectionID);
                        taskData.put(CAT_ID, catID);
                        taskData.put(PAGENO, getPageNo());
                        taskData.put(FEATUREDFIRST, featuredFirst);

                    } catch (Throwable e) {
                    }

                    iw.addWSParam(TASKDATA, taskData);

                    if (IjoomerApplicationConfiguration.isCachEnable && !IjoomerApplicationConfiguration.isReloadRequired) {
                        try {
                            final List<ArrayList<HashMap<String, String>>> data = getEntriesFromCacheByReqObject(TABLE_ENTRIES, iw.getWSParameter().toString());
                            if (data != null && data.size() > 0) {
                                ((Activity) mContext).runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        target.onProgressUpdate(100);
                                        target.onCallComplete(200, "", null, data, getPageNo(), Integer.parseInt(data.get(0).get(0).get(PAGELIMIT)), true);

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
                            iw.getJsonObject().remove(TOTAL);
                            if (iw.getJsonObject().getJSONArray("entries") != null) {
                                IjoomerCaching caching = new IjoomerCaching(mContext);
                                caching.addExtraColumn(PAGENO, (getPageNo() - 1) + "");
                                caching.addExtraColumn(PAGELIMIT, iw.getJsonObject().get(PAGELIMIT).toString());
                                caching.addExtraColumn(SECTIONID, iw.getJsonObject().get(SECTIONID).toString());
                                caching.addExtraColumn(CATID, iw.getJsonObject().get(CATID).toString());
                                caching.addExtraColumn(FAVOURITE, "0");
                                iw.getJsonObject().remove(PAGELIMIT);
                                iw.getJsonObject().remove(SECTIONID);
                                iw.getJsonObject().remove(CATID);
                                caching.setReqObject(iw.getWSParameter().toString());
                                caching.cacheData(iw.getJsonObject(), false, TABLE_ENTRIES);
                                return getEntriesFromCacheByReqObject(TABLE_ENTRIES, iw.getWSParameter().toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(List<ArrayList<HashMap<String, String>>> result) {
                    super.onPostExecute(result);
                    ArrayList<HashMap<String, String>> arrayList = null;
                    if (result == null) {
                        try {
                            arrayList = new ArrayList<HashMap<String, String>>();
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put(SECTIONID, iw.getJsonObject().getString(SECTIONID));
                            arrayList.add(hashMap);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    target.onProgressUpdate(100);
                    target.onCallComplete(getResponseCode(), getErrorMessage(), arrayList, result, getPageNo() - 1, getPageLimit(), false);
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
     * This method is used to get entries
     *
     * @param id
     *            represented Section id
     * @param target
     *            {@link WebCallListener}
     */
    public void getRestaurantEntries(final String sectionID, final String latitude, final String longitude, final String featuredFirst, final WebCallListenerWithCacheInfo target) {
        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, List<ArrayList<HashMap<String, String>>>>() {

                IjoomerWebService iw = new IjoomerWebService();

                @Override
                protected List<ArrayList<HashMap<String, String>>> doInBackground(Void... params) {
                    iw.reset();

                    iw.addWSParam(EXTNAME, SOBIPRO);
                    iw.addWSParam(EXTVIEW, ISOBIPRO);
                    iw.addWSParam(EXTTASK, SECTON_CATEGORIES);

                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(SECTION_ID, sectionID);
                        taskData.put(LATITUDE, latitude);
                        taskData.put(LONGITUDE, longitude);
                        taskData.put(PAGENO, getPageNo());
                        taskData.put(FEATUREDFIRST, featuredFirst);

                    } catch (Throwable e) {
                    }

                    iw.addWSParam(TASKDATA, taskData);
                    if (IjoomerApplicationConfiguration.isCachEnable && getPageNo() == 1 && !IjoomerApplicationConfiguration.isReloadRequired) {
                        try {
                            lat = Double.parseDouble(latitude);
                            longi = Double.parseDouble(longitude);
                            final List<ArrayList<HashMap<String, String>>> data = getEntriesFromCacheByLocation(TABLE_RESTAURANT_ENTRIES);
                            if (data != null && data.size() > 0) {
                                ((Activity) mContext).runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Log.e("DATA_SIZE", data.size() + "");
                                        target.onProgressUpdate(100);
                                        target.onCallComplete(200, "", null, data, getPageNo(), Integer.parseInt(data.get(0).get(0).get(PAGELIMIT)), true);

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
                            iw.getJsonObject().remove(TOTAL);
                            if (iw.getJsonObject().getJSONArray("entries") != null) {
                                IjoomerCaching caching = new IjoomerCaching(mContext);
                                caching.addExtraColumn(PAGENO, (getPageNo() - 1) + "");
                                caching.addExtraColumn(PAGELIMIT, iw.getJsonObject().get(PAGELIMIT).toString());
                                caching.addExtraColumn(SECTIONID, iw.getJsonObject().get(SECTIONID).toString());
                                caching.addExtraColumn(CATID, iw.getJsonObject().get(CATID).toString());
                                caching.addExtraColumn(FAVOURITE, "0");

                                caching.addExtraColumn(COSLAT, "0");
                                caching.addExtraColumn(SINLAT, "0");
                                caching.addExtraColumn(COSLNG, "0");
                                caching.addExtraColumn(SINLNG, "0");

                                iw.getJsonObject().remove(PAGELIMIT);
                                iw.getJsonObject().remove(SECTIONID);
                                iw.getJsonObject().remove(CATID);

                                ((JSONObject) iw.getWSParameter().get(TASKDATA)).remove(LATITUDE);
                                ((JSONObject) iw.getWSParameter().get(TASKDATA)).remove(LONGITUDE);
                                caching.setReqObject(iw.getWSParameter().toString());

                                caching.setCachingInsertListener(new IjoomerCachingInsertListener() {

                                    @Override
                                    public void onBeforeInsert(ContentValues dataToInsert) {
                                        dataToInsert.put(COSLAT, Math.cos(deg2rad(Double.parseDouble(dataToInsert.get(LATITUDE).toString()))) + "");
                                        dataToInsert.put(SINLAT, Math.sin(deg2rad(Double.parseDouble(dataToInsert.get(LATITUDE).toString()))) + "");
                                        dataToInsert.put(COSLNG, Math.cos(deg2rad(Double.parseDouble(dataToInsert.get(LONGITUDE).toString()))) + "");
                                        dataToInsert.put(SINLNG, Math.sin(deg2rad(Double.parseDouble(dataToInsert.get(LONGITUDE).toString()))) + "");
                                    }
                                });

                                caching.cacheData(iw.getJsonObject(), false, TABLE_RESTAURANT_ENTRIES);
                                return getEntriesFromCacheByReqObject(TABLE_RESTAURANT_ENTRIES, iw.getWSParameter().toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(List<ArrayList<HashMap<String, String>>> result) {
                    super.onPostExecute(result);
                    ArrayList<HashMap<String, String>> arrayList = null;
                    if (result == null) {
                        try {
                            arrayList = new ArrayList<HashMap<String, String>>();
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put(SECTIONID, iw.getJsonObject().getString(SECTIONID));
                            arrayList.add(hashMap);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    target.onProgressUpdate(100);
                    target.onCallComplete(getResponseCode(), getErrorMessage(), arrayList, result, getPageNo() - 1, getPageLimit(), false);
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
     * This Method is used to get search result.
     *
     * @param searchPhrase
     *            represented search phrase options.
     * @param searchFor
     *            represented search keyword.
     * @param sid
     *            represented section id.
     * @param ArrayList
     *            <HashMap<String, String>> searchField represented search
     *            fields ArrayList.
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getSearchResult(final String searchPhrase, final String searchFor, final String sid, final ArrayList<HashMap<String, String>> searchField,
                                final String featuredFirst, final WebCallListener target) {
        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, List<ArrayList<HashMap<String, String>>>>() {

                @Override
                protected List<ArrayList<HashMap<String, String>>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, SOBIPRO);
                    iw.addWSParam(EXTVIEW, ISOBIPRO);
                    iw.addWSParam(EXTTASK, GETSEARCH);
                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(SECTION_ID, sid);
                        taskData.put(SEARCH_FOR, searchFor);
                        taskData.put(SEARCHPHRASE, searchPhrase);
                        taskData.put(FEATUREDFIRST, featuredFirst);
                        taskData.put(PAGENO, getPageNo());

                        JSONArray ja = null;
                        ja = new JSONArray();
                        for (HashMap<String, String> hashMap : searchField) {
                            if (hashMap.get(VALUE) != null && hashMap.get(VALUE).length() > 0) {
                                JSONObject value = new JSONObject();
                                try {
                                    value.put(hashMap.get(NAME), new JSONObject(hashMap.get(VALUE)));
                                } catch (Exception e) {
                                    value.put(hashMap.get(NAME), hashMap.get(VALUE));
                                }
                                ja.put(value);
                            }
                        }
                        taskData.put(FIELDS, ja);

                    } catch (Throwable e) {
                    }
                    iw.addWSParam(TASKDATA, taskData);

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
                            iw.getJsonObject().remove(TOTAL);
                            if (iw.getJsonObject().getJSONArray("entries") != null) {
                                IjoomerCaching caching = new IjoomerCaching(mContext);
                                caching.addExtraColumn(PAGENO, (getPageNo() - 1) + "");
                                caching.addExtraColumn(PAGELIMIT, iw.getJsonObject().get(PAGELIMIT).toString());
                                caching.addExtraColumn(SECTIONID, iw.getJsonObject().get(SECTIONID).toString());
                                caching.addExtraColumn(CATID, iw.getJsonObject().get(CATID).toString());
                                caching.addExtraColumn(FAVOURITE, "0");
                                iw.getJsonObject().remove(PAGELIMIT);
                                iw.getJsonObject().remove(SECTIONID);
                                iw.getJsonObject().remove(CATID);
                                caching.setReqObject(iw.getWSParameter().toString());
                                if ((getPageNo() - 1) == 1) {
                                    caching.droapTable(SOBIPROSEARCHRESULT);
                                }

                                caching.cacheData(iw.getJsonObject(), false, SOBIPROSEARCHRESULT);

                                return getEntriesFromCacheByReqObject(SOBIPROSEARCHRESULT, iw.getWSParameter().toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(List<ArrayList<HashMap<String, String>>> result) {
                    super.onPostExecute(result);
                    target.onProgressUpdate(100);
                    target.onCallComplete(getResponseCode(), getErrorMessage(), null, result);
                    isCalling = false;
                }

            }.execute();
        } else {
            isCalling = false;
            target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
            target.onProgressUpdate(100);
        }
    }

    /**
     * This method used to get entries from cached database
     *
     * @param table
     *            represented table name
     * @param entryID
     *            represented entry id
     * @return {@link ArrayList<HashMap<String, String>>}
     */
    public ArrayList<HashMap<String, String>> getEntriesFromCache(String table, String entryID) {

        try {

            ArrayList<HashMap<String, String>> fields = new IjoomerCaching(mContext).getDataFromCache(table, "select * from '" + table + "' where id= " + entryID
                    + " order by rowid");

            return fields;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method is used to get entries in sequence
     *
     * @param table
     *            represented table name
     * @param reqObject
     *            represented request call object of getting entries
     * @return {@link List<ArrayList<hashMap<String,String>>>}
     */
    public List<ArrayList<HashMap<String, String>>> getEntriesFromCacheByReqObject(String table, String reqObject) {

        try {
            List<ArrayList<HashMap<String, String>>> fieldList = new ArrayList<ArrayList<HashMap<String, String>>>();

            ArrayList<HashMap<String, String>> ids = new IjoomerCaching(mContext).getDataFromCache(table, "select id from '" + table + "'group by " + ID + " having reqObject='"
                    + reqObject + "' order by rowid");

            for (HashMap<String, String> hashMap : ids) {
                String id = hashMap.get(ID);

                ArrayList<HashMap<String, String>> fields = new IjoomerCaching(mContext).getDataFromCache(table, "select * from '" + table + "' where id= " + id
                        + " order by rowid");
                fieldList.add(fields);
            }
            return fieldList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method is used to get entries in sequence
     *
     * @param table
     *            represented table name
     * @param reqObject
     *            represented request call object of getting entries
     * @return {@link List<ArrayList<hashMap<String,String>>>}
     */
    public List<ArrayList<HashMap<String, String>>> getEntriesFromCacheByLocation(String table) {

        try {
            List<ArrayList<HashMap<String, String>>> fieldList = new ArrayList<ArrayList<HashMap<String, String>>>();
            String str = buildDistanceQuery();

            ArrayList<HashMap<String, String>> ids = new IjoomerCaching(mContext).getDataFromCache(table, "select id from " + table + " group by " + ID + "  order by " + str
                    + " desc");

            for (HashMap<String, String> hashMap : ids) {
                String id = hashMap.get(ID);

                ArrayList<HashMap<String, String>> fields = new IjoomerCaching(mContext).getDataFromCache(table, "SELECT " + table + ".*," + str + "'distance' from " + table
                        + " where id='" + id + "' order by rowid");

                fieldList.add(fields);

            }
            return fieldList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method is used to get Favourite entries
     *
     * @return {@link List<ArrayList<HashMap<String, String>>>}
     */
    public List<ArrayList<HashMap<String, String>>> getFavouriteEntries() {

        try {
            List<ArrayList<HashMap<String, String>>> fieldList = new ArrayList<ArrayList<HashMap<String, String>>>();

            ArrayList<HashMap<String, String>> ids = new IjoomerCaching(mContext).getDataFromCache(TABLE_FAVOURITE, "select distinct(id) from '" + TABLE_FAVOURITE
                    + "'order by pageLayout ");

            for (HashMap<String, String> hashMap : ids) {
                String id = hashMap.get(ID);

                ArrayList<HashMap<String, String>> fields = new IjoomerCaching(mContext).getDataFromCache(TABLE_FAVOURITE, "select * from '" + TABLE_FAVOURITE + "'where id= " + id
                        + "");
                fieldList.add(fields);
            }
            return fieldList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method is used to check entry has set as favourite
     *
     * @param id
     *            represented entry id
     * @param pageLayout
     *            represented pageLayout
     * @return {@link Boolean}
     */
    public boolean isFavourite(String id, String pageLayout) {
        ArrayList<HashMap<String, String>> ids = new IjoomerCaching(mContext).getDataFromCache(TABLE_FAVOURITE, "select distinct(id) from '" + TABLE_FAVOURITE + "' where id='"
                + id + "' and pageLayout='" + pageLayout + "' ");
        if (ids != null && ids.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * This method is used to get deals
     *
     * @param id
     *            represented section id
     * @param latitude
     *            represented current latitude
     * @param longitude
     *            represented current longitude
     * @param target
     *            {@link WebCallListener}
     */
    public void getDeal(final String id, final String latitude, final String longitude, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();

                iw.addWSParam(EXTNAME, SOBIPRO);
                iw.addWSParam(EXTVIEW, ISOBIPRO);
                iw.addWSParam(EXTTASK, GETDEALS);
                JSONObject taskData = new JSONObject();
                try {

                    taskData.put(SECTION_ID, id);
                    taskData.put(LATITUDE, latitude);
                    taskData.put(LONGITUDE, longitude);
                } catch (Throwable e) {
                    e.printStackTrace();
                }

                iw.addWSParam(TASKDATA, taskData);
                if (IjoomerApplicationConfiguration.isCachEnable) {
                    try {
                        lat = Double.parseDouble(latitude);
                        longi = Double.parseDouble(longitude);
                        String str = buildDistanceQuery();
                        final ArrayList<HashMap<String, String>> data = new IjoomerCaching(mContext).getDataFromCache(TABLE_DEAL, "select * from " + TABLE_DEAL + "  order by "
                                + str + " desc");

                        if (data != null && data.size() > 0) {
                            ((Activity) mContext).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    target.onProgressUpdate(100);
                                    target.onCallComplete(200, "", data, null);

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

                        if (iw.getJsonObject().getJSONArray("entries") != null) {
                            IjoomerCaching caching = new IjoomerCaching(mContext);

                            caching.addExtraColumn(COSLAT, "0");
                            caching.addExtraColumn(SINLAT, "0");
                            caching.addExtraColumn(COSLNG, "0");
                            caching.addExtraColumn(SINLNG, "0");

                            caching.setReqObject(iw.getWSParameter().toString());

                            caching.setCachingInsertListener(new IjoomerCachingInsertListener() {

                                @Override
                                public void onBeforeInsert(ContentValues dataToInsert) {
                                    dataToInsert.put(COSLAT, Math.cos(deg2rad(Double.parseDouble(dataToInsert.get(LATITUDE).toString()))) + "");
                                    dataToInsert.put(SINLAT, Math.sin(deg2rad(Double.parseDouble(dataToInsert.get(LATITUDE).toString()))) + "");
                                    dataToInsert.put(COSLNG, Math.cos(deg2rad(Double.parseDouble(dataToInsert.get(LONGITUDE).toString()))) + "");
                                    dataToInsert.put(SINLNG, Math.sin(deg2rad(Double.parseDouble(dataToInsert.get(LONGITUDE).toString()))) + "");
                                }
                            });

                            return caching.cacheData(iw.getJsonObject(), false, TABLE_DEAL);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
                super.onPostExecute(result);
                target.onProgressUpdate(100);
                target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
            }

        }.execute();
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /**
     * This method is used to get distance query
     *
     * @return {@link String}
     */
    private String buildDistanceQuery() {
        final double coslat = Math.cos(deg2rad(lat));
        final double sinlat = Math.sin(deg2rad(lat));
        final double coslng = Math.cos(deg2rad(longi));
        final double sinlng = Math.sin(deg2rad(longi));
        String str = "(" + coslat + "*" + COSLAT + "*(" + COSLNG + "*" + coslng + "+" + SINLNG + "*" + sinlng + ")+" + sinlat + "*" + SINLAT + ")";
        return str;
    }

    /**
     * This method is used to add entry in favourite entry table
     *
     * @param entry
     *            represented entry data
     * @param pageLayout
     *            represented pageLayout
     */
    public void addToFavourite(ArrayList<HashMap<String, String>> entry, String pageLayout) {
        try {
            for (int i = 0; i < entry.size(); i++) {
                entry.get(i).put("pageLayout", pageLayout);
                entry.get(i).remove(COSLAT);
                entry.get(i).remove(COSLNG);
                entry.get(i).remove(SINLAT);
                entry.get(i).remove(SINLNG);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        new IjoomerCaching(mContext).createTable(entry, TABLE_FAVOURITE);
    }

}
