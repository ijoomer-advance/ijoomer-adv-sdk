package com.ijoomer.library.sobipro;

import android.content.Context;
import android.os.AsyncTask;

import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerPagingProvider;
import com.ijoomer.weservice.IjoomerWebService;
import com.ijoomer.weservice.ProgressListener;
import com.ijoomer.weservice.WebCallListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To SobiproAddEntryDataProvider.
 *
 * @author tasol
 *
 */

public class SobiproAddEntryDataProvider extends IjoomerPagingProvider {

    private Context mContext;
    private final String ENTRY_FIELDS = "isobipro";
    private final String GET_ENTRY_FIELDS = "addentryField";
    private final String GETENTRY = "addentryField";
    private final String SECTION_ID = "sectionID";
    private final String FORM = "form";
    private final String VALUE = "value";
    private final String FIELDS = "fields";
    private final String NAME = "name";
    private static String imagerPath = "";
    private static String IMAGE = "image";

    /**
     * Constructor
     *
     * @param mContext
     *            represented {@link Context}
     */

    public SobiproAddEntryDataProvider(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    /**
     * This method provide image path
     *
     * @return {@link String}
     */
    public static String getImagerPath() {
        return imagerPath;
    }

    /**
     * This method is used to add new entry
     *
     * @param entryFields
     *            represented entry fields
     * @param target
     *            {@link WebCallListener}
     */
    public void addEntry(final String sid, final ArrayList<HashMap<String, String>> entryFields, final WebCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();

                iw.addWSParam(EXTNAME, SOBIPRO);
                iw.addWSParam(EXTVIEW, ENTRY_FIELDS);
                iw.addWSParam(EXTTASK, GETENTRY);

                JSONObject taskData = new JSONObject();
                ArrayList<HashMap<String, String>> filePath = new ArrayList<HashMap<String, String>>();
                try {
                    taskData.put(FORM, "0");

                    JSONArray ja = null;
                    ja = new JSONArray();
                    for (HashMap<String, String> hashMap : entryFields) {
                        if (hashMap.containsKey(IMAGE)) {
                            HashMap<String, String> data = new HashMap<String, String>();
                            if (hashMap.get(IMAGE) != null && hashMap.get(IMAGE).length() > 0) {
                                data.put(hashMap.get(NAME), hashMap.get(IMAGE));
                                filePath.add(data);
                            }
                        } else {
                            if (hashMap.get(VALUE) != null && hashMap.get(VALUE).length() > 0) {
                                JSONObject value = new JSONObject();
                                value.put(hashMap.get(NAME), hashMap.get(VALUE));
                                ja.put(value);
                            }
                        }
                    }
                    taskData.put(FIELDS, ja);
                } catch (Throwable e) {
                    e.printStackTrace();
                }

                iw.addWSParam(TASKDATA, taskData);
                if (filePath.size() > 0) {
                    iw.WSCall(filePath, new ProgressListener() {

                        @Override
                        public void transferred(long num) {
                            if (num >= 100) {
                                target.onProgressUpdate(95);
                            } else {
                                target.onProgressUpdate((int) num);
                            }
                        }
                    });
                } else {
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
                }

                if (validateResponse(iw.getJsonObject())) {
                    try {
                        return new IjoomerCaching(mContext).parseData(iw.getJsonObject());

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
     * This Method is used to get dynamic fields for add entry.
     *
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getEntryFields(final String sid, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();

                iw.addWSParam(EXTNAME, SOBIPRO);
                iw.addWSParam(EXTVIEW, ENTRY_FIELDS);
                iw.addWSParam(EXTTASK, GET_ENTRY_FIELDS);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(SECTION_ID, sid);
                    taskData.put(FORM, "1");
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
                        return new IjoomerCaching(mContext).parseData(iw.getJsonObject());

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
}
