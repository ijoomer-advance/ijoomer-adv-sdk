package com.ijoomer.library.sobipro;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
 * This Class Contains All Method Related To SobiproReviewsDataProvider.
 *
 * @author tasol
 *
 */
public class SobiproReviewsDataProvider extends IjoomerPagingProvider {

    private Context mContext;
    private final String ISOBIPRO = "isobipro";
    private final String ADDREVIEW = "addreview";
    private final String SECTION = "section";
    private final String FORM = "form";
    private final String TITLE = "title";
    private final String VALUE = "value";
    private final String SID = "sid";
    private final String REVIEW = "review";
    private final String RATING = "rating";

    /**
     * Constructor
     *
     * @param mContext
     *            represented {@link Context}
     */
    public SobiproReviewsDataProvider(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    /**
     * This method is used to get fields for adding review
     *
     * @param section_id
     *            represented section id
     * @param target
     *            {@link WebCallListener}
     */
    public void getReviewFields(final String section_id, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();

                iw.addWSParam(EXTNAME, SOBIPRO);
                iw.addWSParam(EXTVIEW, ISOBIPRO);
                iw.addWSParam(EXTTASK, ADDREVIEW);
                JSONObject taskData = new JSONObject();
                try {

                    taskData.put(SECTION, section_id);
                    taskData.put(FORM, "1");
                } catch (Throwable e) {
                    e.printStackTrace();
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

    /**
     * This method is used to add review
     *
     * @param entryID
     *            represented entry id
     * @param sectionID
     *            represented section id
     * @param reviewFields
     *            represented review fields values
     * @param ratingFields
     *            represented rating fields values
     * @param target
     *            {@link WebCallListener}
     */
    public void addReview(final String entryID, final String sectionID, final ArrayList<HashMap<String, String>> reviewFields,
                          final ArrayList<HashMap<String, String>> ratingFields, final WebCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, SOBIPRO);
                iw.addWSParam(EXTVIEW, ISOBIPRO);
                iw.addWSParam(EXTTASK, ADDREVIEW);
                JSONObject taskData = new JSONObject();
                try {

                    JSONArray jsonArrayReview = new JSONArray();
                    JSONArray jsonArrayRating = new JSONArray();
                    JSONObject jsonObject = new JSONObject();
                    for (HashMap<String, String> hashMap : reviewFields) {

                        jsonObject.put(hashMap.get(TITLE), hashMap.get(VALUE));
                    }
                    jsonObject.put(SID, entryID);
                    jsonObject.put(SECTION, sectionID);

                    jsonArrayReview.put(jsonObject);
                    jsonObject = new JSONObject();
                    for (HashMap<String, String> hashMap : ratingFields) {
                        if (hashMap.get(VALUE) != null && hashMap.get(VALUE).length() > 0)
                            jsonObject.put(hashMap.get(TITLE), hashMap.get(VALUE));
                    }
                    jsonArrayRating.put(jsonObject);
                    taskData.put(FORM, "0");
                    taskData.put(REVIEW, jsonArrayReview);
                    taskData.put(RATING, jsonArrayRating);
                    Log.e("taskdata", taskData.toString());
                } catch (Throwable e) {
                    e.printStackTrace();
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
                    return new IjoomerCaching(mContext).parseData(iw.getJsonObject());
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
